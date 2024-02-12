package com.example.tfg_smartwatch;

import com.google.android.gms.location.Priority;

/**
 * Clase encargada de recoger los valores que se mantienen constantes a lo largo de toda la ejecución (solo deberían cambiar al recompilarse).
 *
 *
 * <p>
 * La app va a tener dos modos de monitorizar la ubicacion (MODO_MONITORIZACION_UBICACION):
 * * Modo 1: Alta prioridad, el tiempo que se establece en el intervalo es el que se cumple en la monitorizacon.
 * * Modo 2: Ahorro de bateria, se prioriza el ahorro de batería pero se pierde precision en la ubicacion y en el intervalo de monitorizacion.
 * <p>
 * La app va a tener tres modos de monitorizar la caida (MODO_MONITORIZACION_CAIDA):
 * * Modo 1: Monitorizacion mediante angulos, los umbrales a utilizar para determinar si se ha dado una caida son GSVM y el Angulo.
 * * Modo 2: Monitorizacion mediante angulos, los umbrales a utilizar para determinar si se ha dado una caida son SVM y el Angulo.
 * * Modo 3: Monitorizacion mediante golpe posterior, los umbrales a utilizar para determinar si se ha dado una caida son SVM y el Impacto.
 * <p>
 * La app va a tener el modo de ejecucion en debug (MODO_DEBUG):
 * * False: Modo de utilizacion para usuarios. No se guardan datos recogidos por los sensores.
 * * True: Modo de utilizacion para desarrolladores. Se guardan (y muestran por pantalla) datos recogidos por los sensores.
 */
public class Constantes {
    /**URLS PARA CONEXION CON EL SERVIDOR**/
    public static String URL_SERVIDOR_BASE = "https://trackcare.app.bluece.eu/api/";
    public static String URL_SERVIDOR_SUBIDA = URL_SERVIDOR_BASE + "positions/";
    public static String URL_SERVIDOR_CONFIGURACION = URL_SERVIDOR_BASE + "smartwatches/configuration/";


    /**MODOS DE MONITORIZACION DE UBICACION**/
    public static final int MODO_MONITORIZACION_UBICACION = 1;      // Modo alta prioridad, se respeta el intervalo
    //public static final int MODO_MONITORIZACION_UBICACION = 2;    // Modo ahorro bateria, no se respeta el intervalo

    /**MODOS DE MONITORIZACION DE CAIDA**/
    //public static final int MODO_MONITORIZACION_CAIDA = 1;      // Modo caida mediante angulos (con GSVM)
    //public static final int MODO_MONITORIZACION_CAIDA = 2;      // Modo caida mediante angulos (con SVM)
    public static final int MODO_MONITORIZACION_CAIDA = 3;    // Modo caida mediante impacto final


    /**MODO DE DESARROLLADORES**/
    public static final boolean MODO_DEBUG = false;       // Modo para usuarios, no se guardan datos
    //public static final boolean MODO_DEBUG = true;      // Modo para desarrolladores, se guardan datos


    /**UMBRALES PARA ALGORITMOS**/
    public static final float UMBRAL_RETIRADA_ACELEROMETRO = 0.2f;
    public static final float UMBRAL_RETIRADA_PROXIMIDAD = 3.0f;
    public static final float UMBRAL_CAIDA_GSVM = 0.8f;
    public static final float UMBRAL_CAIDA_ANGULO = 60.0f;
    public static final int COMPROBACIONES_CAIDA = 20;
    public static final int CUMPLIMIENTOS_CAIDA = 15;
    public static final float UMBRAL_CAIDA_SVM = 1.0f;
    public static final float UMBRAL_CAIDA_IMPACTO = 109f;
    public static final int TIEMPO_DURACION_CAIDA = 2000;

    /**
     * Devuelve la prioridad a utilizar en el Sensor de Ubicacion dado el modo de ejecucion seleccionado.
     *
     * @return prioridad a utilizar.
     */
    public static int getPrioridad() {
        if (MODO_MONITORIZACION_UBICACION == 1) {
            return Priority.PRIORITY_HIGH_ACCURACY;
        } else if (MODO_MONITORIZACION_UBICACION == 2) {
            return Priority.PRIORITY_BALANCED_POWER_ACCURACY;
        }
        return Priority.PRIORITY_BALANCED_POWER_ACCURACY;
    }
}
