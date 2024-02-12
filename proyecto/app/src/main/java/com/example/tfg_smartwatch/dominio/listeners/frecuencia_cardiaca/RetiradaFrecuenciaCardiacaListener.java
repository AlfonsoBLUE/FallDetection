package com.example.tfg_smartwatch.dominio.listeners.frecuencia_cardiaca;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.CountDownTimer;

/**
 * Clase que representa el listener para detectar cambios en los valores del sensor de frecuencia cardiaca. Esta clase no tiene funcionalidad en la aplicacion,
 * se deja estructurada como se comenta en la memoria para trabajos futuros.
 */
public class RetiradaFrecuenciaCardiacaListener implements SensorEventListener {

    //Si se usa esta clase, estos valores deben introducirse con la clase Configuracion
    public SensorManager sensorManager;
    private CountDownTimer contador;
    private static final long INTERVALO = 10000; //En milisegudos
    private static final long STEP = 1000; //Bajara de 1 segundo en 1 segundo

    /**
     * Metodo que inicia un contador con el que se detecta si se lleva sin entrar varios segundos en el metodo OnSensorChanged.
     * Si este contador llega  0 se detecta que no se han modificado los valores de las pulsaciones y es posible una retirada.
     */
    public void startContador(){
        contador= new CountDownTimer(INTERVALO, STEP) {
            @Override
            public void onTick(long millisUntilFinished) {}
            @Override
            public void onFinish() {

            }
        };
        contador.start();
    }

    /**
     * Metodo para iniciar de nuevo el contador.
     */
    public void reiniciaContador(){
        contador.cancel();
        contador.start();
    }

    /**
     * Metodo que se ejecuta cuando los valores han cambiado. Si se detectan valores fuera de los normales se detecta una posible retirada.
     * @param event the {@link android.hardware.SensorEvent SensorEvent}.
     */
    @Override
    public void onSensorChanged(SensorEvent event) {

        float pulsaciones = event.values[0];

        if (pulsaciones < 60 || pulsaciones > 150){
            //Detecta posible retirada.
        }
        if (pulsaciones == 0){
            //Detecta posible retirada.
        }
        reiniciaContador();
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        if (sensor.getType() == Sensor.TYPE_HEART_RATE && accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE) {
            //Detecta posible retirada.
        }
    }
}
