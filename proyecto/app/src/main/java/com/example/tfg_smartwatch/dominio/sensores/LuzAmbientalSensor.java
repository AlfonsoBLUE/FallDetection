package com.example.tfg_smartwatch.dominio.sensores;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;

import com.example.tfg_smartwatch.Background;
import com.example.tfg_smartwatch.dominio.listeners.luz_ambiental.RetiradaLuzAmbientalListener;

/**
 * Esta clase se encarga de controlar el sensor de proximidad del dispositivo asi como de agregar listener para obtener sus valores.
 */
public class LuzAmbientalSensor {

    private static SensorManager sensorManager;
    private static Sensor sensorLuz;
    private static RetiradaLuzAmbientalListener listenerLuz ;
    private boolean iniciado = false;

    /**
     * Constructor para crear una instancia de la clase.
     * @param context Contexto de la aplicacion.
     */
    public LuzAmbientalSensor(Background context){
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensorLuz = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        listenerLuz = new RetiradaLuzAmbientalListener(context);
        if(listenerLuz == null){
            //Notificar error
        }

    }

    /**
     * Metodo para modificar el valor de las variables de comprobacion a false.
     */
    public void setEnComprobacionFalse(){
        listenerLuz.setEnComprobacionFalse();
    }

    /**
     * Metodo que inicia la monitorizacion de los valores del sensor.
     */
    public void startLuzAmbiental(){
        if(!iniciado) {
            sensorManager.registerListener(listenerLuz, sensorLuz, SensorManager.SENSOR_DELAY_NORMAL);
            iniciado = true;
        }
    }

    /**
     * Metodo que detiene la monitorizacion de los valores del sensor.
     */
    public void stopLuzAmbiental(){
        if(iniciado){
            iniciado=false;
            sensorManager.unregisterListener(listenerLuz);
        }
    }

    /**
     * Metodo para modificar el valor del numero de comprobaciones a las configuradas por defecto.
     */
    public void setComprobacionesDefecto(){
        listenerLuz.setComprobacionesDefecto();;
    }
}
