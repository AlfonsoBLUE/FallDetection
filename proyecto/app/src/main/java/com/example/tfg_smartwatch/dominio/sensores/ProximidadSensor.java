package com.example.tfg_smartwatch.dominio.sensores;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;

import com.example.tfg_smartwatch.Configuracion;
import com.example.tfg_smartwatch.dominio.listeners.proximidad.RetiradaProximidadListener;

/**
 * Esta clase se encarga de gestionar el sensor de proximidad y registrar los listeners para su monitorizacion.
 */
public class ProximidadSensor {

    private static SensorManager sensorManager;
    private static Sensor sensorProximidad;
    private static RetiradaProximidadListener retiradaProximidadListener;

    /**
     * Constructor para crear una instancia de la clase.
     * @param context Contexto de la aplicacion.
     */
    public ProximidadSensor(Context context){
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensorProximidad = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        retiradaProximidadListener = new RetiradaProximidadListener();
    }

    /**
     * Metodo que inicia la monitorizacion del sensor de proximidad.
     */
    public void startProximidad(){
        // TODO: FIX NUEVA API
        // Para poder utilizar SENSOR_DELAY_FASTEST es necesario pedir un nuevo permiso
        sensorManager.registerListener(retiradaProximidadListener, sensorProximidad, SensorManager.SENSOR_DELAY_FASTEST);
    }

    /**
     * Metodo que detiene la monitorizacion del sensor de proximidad.
     */
    public void stopProximidad(){
        sensorManager.unregisterListener(retiradaProximidadListener);
    }


    /**
     * Inicia y/o detiene la monitorización de los Listeners del sensor de proximidad teniendo en cuenta la configuración actual de la aplicación.
     */
    public void actualizarComprobaciones() {
        Configuracion config = Configuracion.getInstance();

        if (config.isMonitoreoRetiradaActivo()) {
            // No es necesario comprobar si ya esta registrado pues lo hace android y no se puede tener dos veces
            // TODO: FIX NUEVA API
            // Para poder utilizar SENSOR_DELAY_FASTEST es necesario pedir un nuevo permiso
            sensorManager.registerListener(retiradaProximidadListener, sensorProximidad, SensorManager.SENSOR_DELAY_FASTEST);
        } else {
            sensorManager.unregisterListener(retiradaProximidadListener);
        }
    }
}

