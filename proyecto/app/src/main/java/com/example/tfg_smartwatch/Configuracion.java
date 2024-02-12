package com.example.tfg_smartwatch;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Clase encargada de recoger la configuración de la aplicacion. Esta configuración se ha de descargar del servidor, por lo que puede cambiar durante la ejecución.
 */
public class Configuracion {
    private static Configuracion instance;                      // Implementacion patron Singleton
    private String androidID;
    private String telefono;
    private long intervaloMonitorizacionUbicacion;              //En milisegundos
    private long intervaloComprobacionRetiradaAcelerometro;     //En milisegundos
    private int comprobacionesAcelerometro;
    private boolean retirado;
    private boolean monitoreoCaidaActivo;
    private boolean monitoreoRetiradaActivo;
    private boolean finProcesoCaida = false;    // Para saber si un ciclo de notificacion de caida ha finalizado


    /**
     * Implementación del patrón Singleton.
     * Crea la clase con valores por defecto para la configuración, pero la configuración final debería descargarse.
     */
    private Configuracion() {
        telefono = "638749180";
        retirado = false;
        intervaloMonitorizacionUbicacion = 30000;
        intervaloComprobacionRetiradaAcelerometro = 10000;
        comprobacionesAcelerometro = 2;
        monitoreoCaidaActivo = true;
        monitoreoRetiradaActivo = true;
    }

    /**
     * Implementación del patrón Singleton.
     * Devuelve la instancia de la clase. En caso de ser la primera vez que se invoca, crea la instancia y luego la devuelve.
     *
     * @return instancia de la Clase.
     */
    public static synchronized Configuracion getInstance() {
        if (instance == null) {
            instance = new Configuracion();
        }
        return instance;
    }

    /**
     * Actualiza la configuracion de la aplicacion dados los datos descargados del servidor.
     *
     * @param config configuracion descargada del servidor.
     */
    public void actualizaConfiguracion(JSONObject config) {
        // TODO: todos los atributos de esta clase deberian descargarse
        // actualmente solo se descarga que monitorizaciones se quieren llevar a cabo, pues es lo que esta implementado en el servidor

        if (config == null) {
            Log.e("CAMBIO_CONFIG", "El JSON se encontraba vacío.");
            return;
        }

        try {
            monitoreoRetiradaActivo = config.getBoolean("takeoff");
            monitoreoCaidaActivo = config.getBoolean("fall");
        } catch (JSONException e) {
            Log.e("CAMBIO_CONFIG", "Error procesando JSON de datos.", e);
        }
    }

    public String getTELEFONO() {
        return telefono;
    }

    public long getIntervaloMonitorizacionUbicacion() {
        return intervaloMonitorizacionUbicacion;
    }

    public long getIntervaloComprobacionAcelerometro() {
        return intervaloComprobacionRetiradaAcelerometro;
    }

    public int getComprobacionesDefectoAcelerometro() {
        return comprobacionesAcelerometro;
    }

    public boolean isRetirado() {
        return retirado;
    }

    public void setRetirado(boolean retirado) {
        this.retirado = retirado;
    }

    public boolean isMonitoreoCaidaActivo() {
        return monitoreoCaidaActivo;
    }

    public boolean isMonitoreoRetiradaActivo() {
        return monitoreoRetiradaActivo;
    }

    public String getAndroidID() {
        return this.androidID;
    }

    public void setAndroidID(String androidID) {
        this.androidID = androidID;
    }

    public boolean getFinProcesoCaida() {
        return finProcesoCaida;
    }

    public void setFinProcesoCaida(boolean finProcesoCaida) {
        this.finProcesoCaida = finProcesoCaida;
    }
}
