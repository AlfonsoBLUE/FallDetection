package com.example.tfg_smartwatch.dominio.sensores;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;

import com.example.tfg_smartwatch.Background;
import com.example.tfg_smartwatch.Configuracion;
import com.example.tfg_smartwatch.Constantes;
import com.example.tfg_smartwatch.dominio.listeners.acelerometro.CaidaAcelerometroListener;
import com.example.tfg_smartwatch.dominio.listeners.acelerometro.CaidaAnguloGSVMAcelerometroListener;
import com.example.tfg_smartwatch.dominio.listeners.acelerometro.CaidaAnguloSVMAcelerometroListener;
import com.example.tfg_smartwatch.dominio.listeners.acelerometro.CaidaImpactoAcelerometroListener;
import com.example.tfg_smartwatch.dominio.listeners.acelerometro.RetiradaAcelerometroListener;

/**
 * Clase encargada de administrar el Acelerometro.
 * Permite obtenerlo asi como registrar/eliminar listeners para su monitorización.
 */
public class AcelerometroSensor {

    private static SensorManager sensorManager;
    private static Sensor sensorAcelerometro;
    private static RetiradaAcelerometroListener retiradaAcelerometroListener;
    private static CaidaAcelerometroListener caidaAcelerometroListener;
    Background background;
    private boolean iniciado = false;

    /**
     * Constructor para crear una instancia de la clase.
     *
     * @param context Contexto de la aplicacion.
     */
    public AcelerometroSensor(Background context) {
        background = context;
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensorAcelerometro = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        retiradaAcelerometroListener = new RetiradaAcelerometroListener(context);
        retiradaAcelerometroListener.setTiempo();

        if (Constantes.MODO_MONITORIZACION_CAIDA == 1) {
            caidaAcelerometroListener = new CaidaAnguloGSVMAcelerometroListener(context);
        } else if (Constantes.MODO_MONITORIZACION_CAIDA == 1) {
            caidaAcelerometroListener = new CaidaAnguloSVMAcelerometroListener(context);
        } else {
            caidaAcelerometroListener = new CaidaImpactoAcelerometroListener(context);
        }

    }

    /**
     * Metodo que establece el indicador de comprobaciones a false en el listener.
     */
    public void setEnComprobacionFalse() {
        retiradaAcelerometroListener.setEnComprobacionFalse();
    }

    /**
     * Inicia la monitorización de todos los Listeners del acelerometro.
     */
    public void startAcelerometro() {
        if (!iniciado) {
            iniciado = true;
            sensorManager.registerListener(retiradaAcelerometroListener, sensorAcelerometro, SensorManager.SENSOR_DELAY_NORMAL);

            // TODO: FIX NUEVA API
            // Para poder utilizar SENSOR_DELAY_FASTEST es necesario pedir un nuevo permiso
            sensorManager.registerListener(caidaAcelerometroListener, sensorAcelerometro, SensorManager.SENSOR_DELAY_FASTEST);
        }
    }

    /**
     * Detiene la monitorización de todos los Listeners del acelerometro.
     */
    public void stopAcelerometro() {
        if (iniciado) {
            iniciado = false;
            sensorManager.unregisterListener(retiradaAcelerometroListener);
            sensorManager.unregisterListener(caidaAcelerometroListener);
        }
    }

    /**
     * Metodo que establece el numero de comprobaciones por defecto en el listener.
     */
    public void setComprobacionesDefecto() {
        retiradaAcelerometroListener.setComprobacionesDefecto();
    }

    /**
     * Inicia y/o detiene la monitorización de los Listeners del acelerometro teniendo en cuenta la configuración actual de la aplicación.
     */
    public void actualizarComprobaciones() {
        Configuracion config = Configuracion.getInstance();

        if (config.isMonitoreoRetiradaActivo()) {
            // No es necesario comprobar si ya esta registrado pues lo hace android y no se puede tener dos veces
            sensorManager.registerListener(retiradaAcelerometroListener, sensorAcelerometro, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            sensorManager.unregisterListener(retiradaAcelerometroListener);
        }

        if (config.isMonitoreoCaidaActivo()) {
            // No es necesario comprobar si ya esta registrado pues lo hace android y no se puede tener dos veces
            // TODO: FIX NUEVA API
            // Para poder utilizar SENSOR_DELAY_FASTEST es necesario pedir un nuevo permiso
            sensorManager.registerListener(caidaAcelerometroListener, sensorAcelerometro, SensorManager.SENSOR_DELAY_FASTEST);
        } else {
            sensorManager.unregisterListener(caidaAcelerometroListener);
        }
    }

}
