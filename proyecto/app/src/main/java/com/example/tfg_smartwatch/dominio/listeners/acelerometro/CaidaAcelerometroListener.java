package com.example.tfg_smartwatch.dominio.listeners.acelerometro;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import com.example.tfg_smartwatch.Background;
import com.example.tfg_smartwatch.Configuracion;

/**
 * Representa los Listeners que se encargan de la detección de caídas.
 */
public abstract class CaidaAcelerometroListener implements SensorEventListener {
    protected final Background context;
    protected final Configuracion configuracion;
    protected boolean posibleCaida;     // Saber que se ha cumplido el primer umbral
    protected boolean confirmadaCaida;  // Saber que se ha cumplido el segundo umbral (necesario para que no se repitan detecciones)
    protected String datos;


    /**
     * Constructor para crear una instancia de la clase.
     *
     * @param context Contexto de la aplicacion
     */
    protected CaidaAcelerometroListener(Background context) {
        this.context = context;
        configuracion = Configuracion.getInstance();

        resetearCaida();
    }

    /**
     * Metodo que detecta un cambio en los valores del sensor. Dado este cambio, se procede a la deteccion de la caida.
     *
     * @param sensorEvent the {@link android.hardware.SensorEvent SensorEvent}.
     */
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float[] valoresActuales = sensorEvent.values.clone();

        // Si se ha dado una caida previa cuyo ciclo ha acabado, se resetea el estado de comprobacion
        if (confirmadaCaida && configuracion.getFinProcesoCaida()){
            resetearCaida();
        }

        if (!configuracion.isRetirado()) {
            comprobarCaida(valoresActuales);
        }
    }

    /**
     * Algoritmo de deteccion de caidas.
     *
     * @param valoresActuales valores recogidos por el acelerometro.
     */
    protected abstract void comprobarCaida(float[] valoresActuales);

    /**
     * Resetea el estado de comprobacion de la caida.
     */
    protected void resetearCaida(){
        posibleCaida = false;
        confirmadaCaida = false;
        datos = "";
        configuracion.setFinProcesoCaida(false);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }
}
