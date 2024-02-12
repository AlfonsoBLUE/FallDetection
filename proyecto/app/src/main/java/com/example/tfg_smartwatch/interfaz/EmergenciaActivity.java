package com.example.tfg_smartwatch.interfaz;

import android.os.Bundle;

import com.example.tfg_smartwatch.R;

public class EmergenciaActivity extends ConfirmacionActivity {

    private int clicks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actualizarTextos(R.string.emergencia_texto, R.string.emergencia_aceptar, R.string.emergencia_cancelar);
        clicks = 0;
        startContador();
    }

    @Override
    public void logicaFinContador() {
        finish();
    }

    @Override
    protected void logicaAceptar() {
        background.enviaNotificacionEmergencia();
        finish();
    }

    @Override
    protected void logicaCancelar() {
        finish();
    }

    @Override
    protected void logicaTexto() {
        clickTexto = true;
        clicks++;
        if (clicks == 5) {
            background.descargarNuevaConfig();
            clicks = 0;
            // TODO: mostrar dialog indicando que se ha descargado
            finish();
        }
    }
}
