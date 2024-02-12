package com.example.tfg_smartwatch.dominio.listeners.acelerometro;

import android.util.Log;

import com.example.tfg_smartwatch.Background;
import com.example.tfg_smartwatch.Constantes;

/**
 * Representa el Listener que detecta caídas mediante los valores relacionados con el Angulo.
 * Como primer umbral utiliza GSVM y como segundo ANGULO.
 */
public class CaidaAnguloSVMAcelerometroListener extends CaidaAcelerometroListener {
    private int comprobacionesCaida;
    private int cumplimientosCaida;

    /**
     * Constructor para crear una instancia de la clase.
     *
     * @param context Contexto de la aplicacion
     */
    public CaidaAnguloSVMAcelerometroListener(Background context) {
        super(context);

        comprobacionesCaida = 0;
        cumplimientosCaida = 0;
    }

    @Override
    protected void comprobarCaida(float[] valoresActuales) {
        double x2 = valoresActuales[0] * valoresActuales[0];
        double y2 = valoresActuales[1] * valoresActuales[1];
        double z2 = valoresActuales[2] * valoresActuales[2];

        if (!posibleCaida) {
            double svm = Math.sqrt(x2 + y2 + z2);

            if (Constantes.MODO_DEBUG) {
                String log = "SVM: " + svm;
                Log.i("MEDIDA_ACELEROMETRO", log);
            }

            if (svm < Constantes.UMBRAL_CAIDA_GSVM) {
                posibleCaida = true;

                if (Constantes.MODO_DEBUG) {
                    datos = datos + "SVM: " + svm + "\n";
                }
            }
        }

        if (posibleCaida && !confirmadaCaida) {
            comprobacionesCaida++;

            double angle = Math.atan(Math.sqrt(y2 + z2) / x2) * (180 / Math.PI);

            if (Constantes.MODO_DEBUG) {
                datos = datos + "ANGULO: " + angle + "\n";
            }

            if (angle > Constantes.UMBRAL_CAIDA_ANGULO) {
                cumplimientosCaida++;
            }

            if (comprobacionesCaida == Constantes.COMPROBACIONES_CAIDA) {

                if (Constantes.MODO_DEBUG) {
                    datos = datos + "CUMPLIMIENTOS: " + cumplimientosCaida + "\n";
                    Log.i("ALGORITMO_CAIDA", datos);
                }

                if (cumplimientosCaida >= Constantes.CUMPLIMIENTOS_CAIDA) {

                    confirmadaCaida = true;

                    Background background = Background.getInstance();
                    background.mostrarConfirmacionCaida(context, datos);

                } else {
                    resetearCaida();
                }
            }
        }
    }
}
