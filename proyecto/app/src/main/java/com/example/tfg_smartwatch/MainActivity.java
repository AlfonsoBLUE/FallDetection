package com.example.tfg_smartwatch;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * Clase principal de la aplicacion. Se ejecuta al iniciar la aplicacion.
 */
public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private boolean tieneProximidad = false;

    /**
     * Metodo que se ejecuta al crear la clase. Se encarga de comprobar si tiene sensor de proximidad y de solicitar permisos al usuario.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SensorManager sensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY) != null) {
            tieneProximidad = true;
        }

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, /*REQUEST_CALL_PHONE_PERMISSION*/1);
        }

        //Permisos para sensor de frecuencia cardiaca.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            // No es necesario solicitar permisos en tiempo de ejecución

        } else {
            // Verifica si se tienen los permisos necesarios, si no se solicitan
            if (checkSelfPermission(Manifest.permission.BODY_SENSORS) == PackageManager.PERMISSION_GRANTED) {
            } else {
                requestPermissions(new String[]{Manifest.permission.BODY_SENSORS}, 1);
            }
        }


        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WAKE_LOCK}, 1);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, 5);

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                REQUEST_LOCATION_PERMISSION);

    }

    /**
     * Metodo que se ejecuta cuando se tienen los permisos necesarios. Comprueba todos los permisos e inicia la actividad en segundo plano.
     *
     * @deprecated
     *
     * @param requestCode  The request code passed
     * @param permissions  The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *                     which is either {@link android.content.pm.PackageManager#PERMISSION_GRANTED}
     *                     or {@link android.content.pm.PackageManager#PERMISSION_DENIED}. Never null.
     */
    @Override
    @Deprecated
    // TODO: Este metodo esta deprecado, en un futuro habra que cambiarlo
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,
                            new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
                            REQUEST_LOCATION_PERMISSION);
                    return;
                }

                LocationManager locationManager = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                }
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    Log.d("ERROR", "No se encuentra un proveedor de GPS disponible");
                } else {
                    Intent serviceIntent = new Intent(this, Background.class);
                    serviceIntent.putExtra("proximidad", tieneProximidad);
                    startForegroundService(serviceIntent);
                    finish();
                }
            } else {
                Log.d("Error", "No se han otrogado permisos");
            }
        }
    }
}