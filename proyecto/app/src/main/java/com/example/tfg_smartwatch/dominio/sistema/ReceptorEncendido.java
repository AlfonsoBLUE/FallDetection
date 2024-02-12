package com.example.tfg_smartwatch.dominio.sistema;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.tfg_smartwatch.MainActivity;

/**
 * Clase encargada de detectar si el dispositivo se ha encendido tras apagarse con la aplicacion en funcionamiento.
 */
public class ReceptorEncendido extends BroadcastReceiver {
    /**
     * Metodo que detecta si el dispositivo se ha encendido de nuevo y se inicia la aplicacion.
     *
     * @param context The Context in which the receiver is running.
     * @param intent  The Intent being received.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Intent inicio = new Intent(context, MainActivity.class);
            inicio.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(inicio);
        }
    }
}
