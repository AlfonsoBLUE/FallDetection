package com.example.tfg_smartwatch.persistencia.servidor;

import android.util.Log;

import com.example.tfg_smartwatch.Configuracion;
import com.example.tfg_smartwatch.Constantes;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Clase utilizada para establecer conexion con la API del servidor realizando solicitudes HTTP.
 */
public class APIServidor {

    /**
     * Metodo de que realiza una solicitud HTTP de tipo POST al servidor.
     *
     * @param json JSON que se envia al servidor
     */
    public void post(String json) {
        try {
            URL url = new URL(Constantes.URL_SERVIDOR_SUBIDA);
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod("POST");
            conexion.setRequestProperty("Content-Type", "application/json");
            conexion.setRequestProperty("Accept", "application/json");
            conexion.setDoOutput(true);
            conexion.setDoInput(true);


            //Escribe json en el cuerpo de la solicitud
            OutputStreamWriter writer = new OutputStreamWriter(conexion.getOutputStream());
            writer.write(json);
            writer.flush();

            //Leer respuesta del servidor
            BufferedReader reader = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String response;
            while ((response = reader.readLine()) != null) {
                stringBuilder.append(response.trim());
            }
            //Cerrar conexiones
            writer.close();
            reader.close();
            conexion.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metodo de que realiza una solicitud HTTP de tipo GET al servidor. Concretamente a la URL de descarga de configuracion.
     * En caso de que se descrague correctamente, obtiene un JSON con pares configuracion-valor.
     *
     * @return el string correspondiente al cuerpo de la solicitud. JSONObject si se descarga correctamente; NULL en caso contrario.
     */
    public JSONObject getConfig() {
        JSONObject resultado = null;

        try {
            String urlString = Constantes.URL_SERVIDOR_CONFIGURACION + Configuracion.getInstance().getAndroidID();
            // Preparar y abrir conexion
            URL url = new URL(urlString);
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod("GET");
            conexion.setRequestProperty("Accept", "application/json");
            conexion.setDoInput(true);

            // Leer respuesta del servidor
            BufferedReader reader = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
            String respuesta;
            StringBuilder respuestaString = new StringBuilder();
            while ((respuesta = reader.readLine()) != null) {
                respuestaString.append(respuesta.trim());
            }

            // Convertir respuesta en json
            resultado = new JSONObject(respuestaString.toString());

            // Cerrar conexiones
            reader.close();
            conexion.disconnect();


        } catch (IOException e) {
            Log.e("CONEXION_SERVER", "Error en la peticion GET.", e);
            resultado = null;
        } catch (JSONException ex) {
            Log.e("CONEXION_SERVER", "Error procesando la respuesta obtenida.", ex);
            resultado = null;
        }

        return resultado;
    }
}
