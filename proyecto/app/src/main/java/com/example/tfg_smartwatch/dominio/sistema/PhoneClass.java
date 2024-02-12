package com.example.tfg_smartwatch.dominio.sistema;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

/**
 * Clase encargada de realizar la llamada correspondiente al numero configurado cuando se notifica una emergencia.
 */
public class PhoneClass {
    private final Context context;

    /**
     * Constructor para crear una instancia de la clase
     *
     * @param context Contexto de la aplicacion
     */
    public PhoneClass(Context context) {
        this.context = context;
    }

    /**
     * Metodo encargado de realizar la llamada gracias al intent comun ACTION_CALL.
     *
     * @param telefono Telefono al que se realiza la llamada
     */
    public void llamar(String telefono) {
        if (compruebaPermisos()) {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + telefono));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    /**
     * Metodo encargado de comprobar si se han concedido los permisos necesarios por parte del usuario, y si no es asi los solicita.
     *
     * @return
     */
    private boolean compruebaPermisos() {
        return context.checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED;
    }
}
