package com.example.tfg_smartwatch.interfaz;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;

import com.example.tfg_smartwatch.Configuracion;
import com.example.tfg_smartwatch.Constantes;
import com.example.tfg_smartwatch.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;

/**
 * Representa la pantalla de confirmación que se muestra al usuario en caso de haberse dado una CAIDA.
 */
public class CaidaActivity extends ConfirmacionActivity {

    private String datos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        datos = getIntent().getExtras().getString("datos");
        actualizarTextos(R.string.caida_texto, R.string.caida_aceptar, R.string.caida_cancelar);
        playSonido();
        playVibracion();
        startContador();
    }

    /**
     * Emite un sonido para llamar la atención del usuario.
     */
    private void playSonido() {
        // TODO: En algunos dispositivos no funciona correctamente. Es un problema recurrente documentado de Android.
        try {
            Uri notificacion = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone sonido = RingtoneManager.getRingtone(getApplicationContext(), notificacion);
            sonido.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Emite una vibración para llamar la atención del usuario.
     */
    private void playVibracion() {
        Vibrator vibrador = (Vibrator) getApplicationContext().getSystemService(VIBRATOR_SERVICE);
        VibrationEffect effect = VibrationEffect.createOneShot(1500, VibrationEffect.DEFAULT_AMPLITUDE);
        vibrador.vibrate(effect);
    }

    @Override
    protected void logicaFinContador() {
        background.enviaNotificacionCaidaGrave();
        Configuracion.getInstance().setFinProcesoCaida(true);
        datos = datos + "CAIDA GRAVE";
        guardarDatos();
        finish();
    }

    @Override
    protected void logicaAceptar() {
        background.enviaNotificacionCaidaLeve();
        Configuracion.getInstance().setFinProcesoCaida(true);
        datos = datos + "CAIDA LEVE";
        guardarDatos();
        finish();
    }

    @Override
    protected void logicaCancelar() {
        background.enviaNotificacionCaidaFalsa();
        Configuracion.getInstance().setFinProcesoCaida(true);
        datos = datos + "CAIDA FALSA";
        guardarDatos();
        finish();
    }

    @Override
    protected void logicaTexto() {
        // En caso de deteccion de caida, las pulsaciones en el texto no han de hacer nada
    }

    /**
     * Guarda los datos de la caída en un fichero de texto. En caso de no poder hacerlo, los muestra en el Log.
     */
    private void guardarDatos() {

        if(!Constantes.MODO_DEBUG){
            return;
        }

        String nombreArchivo = "datos" + LocalDateTime.now().toString();
        byte[] datosPreparados = datos.getBytes();

        try {
            // Se prefiere guardar el archivo en almacenamiento externo
            escribirFichero(nombreArchivo, datosPreparados, true);

        } catch (IOException e) {
            Log.e("GUARDADO_DATOS", "Error escribiendo el fichero de datos.", e);
            try {
                // Si no se puede, se intenta guardar en el almacenamiento interno
                escribirFichero(nombreArchivo, datosPreparados, false);

            } catch (IOException ex) {
                Log.e("GUARDADO_DATOS", "Error escribiendo el fichero de datos.", ex);
            }
        } finally {
            Log.i("ALGORITMO_CAIDA", datos);
        }
    }

    private void escribirFichero(String nombreArchivo, byte[] datosPreparados, boolean externo) throws IOException {

        File archivo = null;
        if (externo) {
            archivo = new File(getExternalFilesDir(null), nombreArchivo);
        } else {
            archivo = new File(getFilesDir(), nombreArchivo);
        }

        OutputStream os = new FileOutputStream(archivo);
        os.write(datosPreparados);
        os.close();
    }
}
