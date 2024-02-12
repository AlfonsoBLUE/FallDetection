package com.example.tfg_smartwatch.dominio.listeners.acelerometro;

import android.util.Log;

import com.example.tfg_smartwatch.Background;
import com.example.tfg_smartwatch.Constantes;

/**
 * Representa el Listener que detecta caídas mediante los valores relacionados con el Impacto.
 * Como primer umbral utiliza SVM y como segundo IMPACTO.
 */
public class CaidaImpactoAcelerometroListener extends CaidaAcelerometroListener {
    private Long caidaTimestamp;

    /**
     * Constructor para crear una instancia de la clase.
     *
     * @param context Contexto de la aplicacion
     */
    public CaidaImpactoAcelerometroListener(Background context) {
        super(context);

        caidaTimestamp = (long) 0;
    }

    @Override
    protected void comprobarCaida(float[] valoresActuales) {
        double x2 = valoresActuales[0] * valoresActuales[0];
        double y2 = valoresActuales[1] * valoresActuales[1];
        double z2 = valoresActuales[2] * valoresActuales[2];

        double svm = Math.sqrt(x2 + y2 + z2);

        if (Constantes.MODO_DEBUG) {
            String log = "SVM: " + svm;
            Log.i("MEDIDA_ACELEROMETRO", log);
        }

        if (!posibleCaida && (svm < Constantes.UMBRAL_CAIDA_SVM)) {

            posibleCaida = true;
            caidaTimestamp = System.currentTimeMillis();

            if (Constantes.MODO_DEBUG) {
                datos = datos + "SVM Caida Libre: " + svm + "\n";
            }
        }

        if (posibleCaida && !confirmadaCaida) {

            // Si se pasa mucho tiempo sin impacto se asume que no hay caida
            if ((System.currentTimeMillis() - caidaTimestamp) > Constantes.TIEMPO_DURACION_CAIDA) {
                posibleCaida = false;
                resetearCaida();
                return;
            }

            if (svm > Constantes.UMBRAL_CAIDA_IMPACTO) {

                confirmadaCaida = true;

                if (Constantes.MODO_DEBUG) {
                    datos = datos + "SVM Impacto: " + svm + "\n";
                    Log.i("ALGORITMO_CAIDA", datos);
                }

                Background background = Background.getInstance();
                background.mostrarConfirmacionCaida(context, datos);
            }
        }
    }
}
