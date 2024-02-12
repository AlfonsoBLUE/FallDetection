package com.example.tfg_smartwatch.dominio.sistema;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.tfg_smartwatch.Background;

/**
 * Clase que extiende de BroadcastReceiver encargada de detectar la pulsacion en el boton de emergencia.
 */
public class ReceptorEmergencia extends BroadcastReceiver {
    /**
     * Metodo encargado de detectar la pulsacion en el boton.
     *
     * @param context The Context in which the receiver is running.
     * @param intent  The Intent being received.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Background background = Background.getInstance();
        background.mostrarConfirmacionEmergencia(context);
    }
}
