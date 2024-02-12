package com.example.tfg_smartwatch.dominio.listeners.luz_ambiental;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import com.example.tfg_smartwatch.Background;

/**
 * Clase que representa el listener para detectar cambios en los valores del sensor de luz ambiental. Esta clase no tiene funcionalidad en la aplicacion,
 * se deja estructurada como se comenta en la memoria para trabajos futuros.
 */
public class RetiradaLuzAmbientalListener implements SensorEventListener {

    //Si se usa esta clase, estos valores deben introducirse con la clase Configuracion
    private static final int COMPROBACIONES_DEFECTO = 3;
    private int comprobaciones = COMPROBACIONES_DEFECTO;
    private static final int INTERVALO = 5000; //En milisegundos
    private static final float UMBRAL_VALORES_IGUALES = 1.0f;
    private float intensidadActual;
    private float intensidadAnterior;
    private long tiempoUltimo;
    private boolean enComprobacion;
    private int contadorCalibrar; //Los primeros valores varian hasta que se calibra el sensor a la luz


    private final Object semaforo = new Object();

    private Background context;

    /**
     * Metodo que modifica los valores de las variables de comprobacion a false.
     */
    public void setEnComprobacionFalse(){
        enComprobacion=false;
    }

    /**
     * Constructor para crear una instancia de la clase.
     * @param context Contexto de la aplicacion
     */
    public RetiradaLuzAmbientalListener(Background context) {
        this.context = context;
        enComprobacion=false;
        contadorCalibrar = 5;
    }

    /**
     * Metodo para detectar los cambios en el sensor. Se calcula la intensidad obtenida con la inmediatamente anterior
     * para comprobar que no son significativamente diferentes y detectar una posible retirada.
     * @param event the {@link android.hardware.SensorEvent SensorEvent}.
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        synchronized (semaforo){
            intensidadActual = event.values[0];
            long tiempoAhora = System.currentTimeMillis();
            if((tiempoAhora-tiempoUltimo) > INTERVALO){
                if(!enComprobacion){
                    float calculo = Math.abs(intensidadActual-intensidadAnterior);
                    contadorCalibrar --;
                    if(calculo < UMBRAL_VALORES_IGUALES){
                        if (comprobaciones == 0){
                            enComprobacion=true;
                            comprobaciones --;
                            Background activityPrincipal = Background.getInstance();
                            activityPrincipal.gestionaRetirada("LUZAMBIENTE");
                        }else{
                            comprobaciones --;
                        }
                    }else if(intensidadAnterior > 0 && contadorCalibrar < 1){
                        Background activityPrincipal = Background.getInstance();
                        activityPrincipal.setComprobacionesDefecto();
                    }
                }
                intensidadAnterior = intensidadActual;
                tiempoUltimo = tiempoAhora;
            }
        }

    }

    /**
     * Metodo para establecer el valor de las variables de comprobacion en el valor configurado por defecto.
     */
    public void setComprobacionesDefecto(){
        comprobaciones = COMPROBACIONES_DEFECTO;
        contadorCalibrar =5;
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
