package com.example.tfg_smartwatch.dominio.sensores;

import android.content.pm.PackageManager;
import android.location.Location;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.example.tfg_smartwatch.Background;
import com.example.tfg_smartwatch.Configuracion;
import com.example.tfg_smartwatch.persistencia.MensajeJSON;
import com.example.tfg_smartwatch.persistencia.local.DatabaseLocal;
import com.example.tfg_smartwatch.persistencia.servidor.APIServidor;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/**
 * Clase encargada de monitorizar la ubicacion del dispositivo. Se monitoriza en funcion de un intervalo y una prioridad especificadas
 * por el usuario en la clase configuracion.
 */
public class UbicacionSensor {
    private final Background mainActivity;
    private final FusedLocationProviderClient ubicacion;
    APIServidor apiServidor;
    DatabaseLocal db;
    List<MensajeJSON> listaDB;
    LocationRequest monitorizadorUbicacion;

    /**
     * Constructor para crear una instancia de la clase.
     *
     * @param mainActivity           Contexto de la aplicacion
     * @param ubicacion              Proveedor de ubicacion
     * @param monitorizadorUbicacion Objeto de la clase LocationRequest para la monitorizacion
     */
    public UbicacionSensor(Background mainActivity, FusedLocationProviderClient ubicacion, LocationRequest monitorizadorUbicacion) {
        this.mainActivity = mainActivity;
        this.ubicacion = ubicacion;
        this.monitorizadorUbicacion = monitorizadorUbicacion;
        listaDB = new ArrayList<>();
        apiServidor = new APIServidor();
        db = DatabaseLocal.getInstance(mainActivity);
    }

    /**
     * Metodo encargado de modificar el valor de la variable de retirada a true.
     */
    public void setRetiradoTrue() {
        Configuracion.getInstance().setRetirado(true);
    }

    /**
     * Metodo encargado de modificar el valor de la variable de retirada a false.
     */
    public void setRetiradoFalse() {
        Configuracion.getInstance().setRetirado(false);
    }

    /**
     * Metodo encargado de realizar la monitorizacion de la ubicacion con el intervalo indicado.
     */
    public void monitorizaUbicacion() {
        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    if (!Configuracion.getInstance().isRetirado()) {
                        MensajeJSON json = creaJSON(location, "MONITORING");
                        //Comprobacion conexion a internet
                        if (!mainActivity.isNetOk(mainActivity)) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    db.mensajeJsonDAO().insert(json);
                                }
                            }).start();


                        } else {
                            Gson gson = new Gson();
                            //Envio de todos los mensajes almacenados en la base de datos local
                            Thread getAllThread = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    listaDB = db.mensajeJsonDAO().getAll();
                                }
                            });
                            getAllThread.start();
                            try {
                                getAllThread.join();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            for (int i = 0; i < listaDB.size(); i++) {
                                String jsonSend = gson.toJson(listaDB.get(i));
                                apiServidor.post(String.valueOf(jsonSend));
                            }
                            //Se vacia la base de datos
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    db.clearAllTables();
                                }
                            }).start();
                            //Envio de la ubicacion actual
                            String jsonEnvio = gson.toJson(json);
                            apiServidor.post(String.valueOf(jsonEnvio));
                        }
                    }
                }

            }
        };

        if (ActivityCompat.checkSelfPermission(mainActivity, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mainActivity, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        ubicacion.requestLocationUpdates(monitorizadorUbicacion, locationCallback, null);
    }

    /**
     * Metodo encargado de realizar el envio de la notificacion de retirada y emergencia al servidor.
     *
     * @param type Tipo de mensaje que se envia
     */
    public void enviaNotificacion(String type) {
        if (type.equals("TAKEOFF")) {
            setRetiradoTrue();
        }

        if (ActivityCompat.checkSelfPermission(mainActivity, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mainActivity, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        ubicacion.getLastLocation().addOnSuccessListener(location -> {
            MensajeJSON json = creaJSON(location, type);
            //Comprobacion conexion a internet
            if (!mainActivity.isNetOk(mainActivity)) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        db.mensajeJsonDAO().insert(json);
                    }
                }).start();
            } else {
                Gson gson = new Gson();
                String jsonEnvio = gson.toJson(json);
                apiServidor.post(String.valueOf(jsonEnvio));
            }
        });
    }


    /**
     * Metodo que se encarga de crear el mensaje tipo JSON que se envia al servidor con la estructura adecuada.
     *
     * @param location Ubicacion del dispositivo
     * @param type     Tipo de mensaje que se envia
     * @return Mensaje tipo JSON
     */
    public MensajeJSON creaJSON(Location location, String type) {
        MensajeJSON json = new MensajeJSON();
        json.setType(type);
        json.setAndroidId(Settings.Secure.getString(mainActivity.getContentResolver(), Settings.Secure.ANDROID_ID));
        json.setLatitude(location.getLatitude());
        json.setLongitude(location.getLongitude());
        json.setAltitude(location.getAltitude());
        json.setAccuracy(location.getAccuracy());
        json.setBattery(mainActivity.getBattery(mainActivity));
        if (location.getProvider().equals("gps")) {
            json.setSource("GNSS");
        } else {
            json.setSource("FUSED");
        }
        //Fecha en formato UTC String
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String fechaUtc = sdf.format(Calendar.getInstance().getTime());
        json.setDeviceTimestamp(fechaUtc);
        return json;
    }

    /**
     * Descarga la configuración de la aplicación que depende del servidor.
     *
     * @return Objeto JSON que representa la configuración descargada.
     */
    public JSONObject descargarConfig() {
        // TODO: la APIServidor deberia ser algo global, no tener que pasarse por aqui
        return apiServidor.getConfig();
    }
}
