package com.example.tfg_smartwatch.interfaz;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tfg_smartwatch.Background;
import com.example.tfg_smartwatch.R;

/**
 * Representa las pantallas de confirmación que se muestran al usuario.
 */
public abstract class ConfirmacionActivity extends AppCompatActivity {
    protected boolean clickTexto;
    Background background;
    private TextView mensaje;
    private Button botonAceptar;
    private Button botonCancelar;
    private boolean clickBoton;
    private String textoCancelar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_confirmacion);

        mensaje = findViewById(R.id.textViewDescripcion);
        botonAceptar = findViewById(R.id.buttonAceptar);
        botonCancelar = findViewById(R.id.buttonCancelar);
        background = Background.getInstance();

        clickTexto = false;
        clickBoton = false;
        textoCancelar = "";
    }

    protected void actualizarTextos(int descripcion, int aceptar, int cancelar) {
        mensaje.setText(descripcion);
        botonAceptar.setText(aceptar);
        botonCancelar.setText(cancelar);
        textoCancelar = getString(cancelar);
    }

    /**
     * Inicia un contador que representa el tiempo que tiene el usuario para contestar a la pantalla de confirmacion.
     */
    public void startContador() {
        CountDownTimer contador = new CountDownTimer(7000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int segundos = (int) (millisUntilFinished / 1000);
                String textoBoton = textoCancelar + "(" + segundos + ")";
                botonCancelar.setText(textoBoton);
            }

            @Override
            public void onFinish() {
                if (!clickTexto && !clickBoton) {
                    logicaFinContador();
                }
            }
        };
        contador.start();
    }

    /**
     * Gestionar la pulsacion en el boton aceptar.
     *
     * @param v Vista
     */
    public void clickOnBotonAceptar(View v) {
        clickBoton = true;
        logicaAceptar();
    }

    /**
     * Gestiona la pulsacion en el boton cancelar.
     *
     * @param v Vista
     */
    public void clickOnBotonCancelar(View v) {
        clickBoton = true;
        logicaCancelar();
    }

    /**
     * Gestiona la pulsacion en el texto.
     *
     * @param v Vista
     */
    public void clickOnTexto(View v) {
        logicaTexto();
    }

    /**
     * Logica que de ejecutarse cuando el contador llega a cero
     */
    protected abstract void logicaFinContador();

    /**
     * Logica que de ejecutarse cuando se pulsa el boton aceptar.
     */
    protected abstract void logicaAceptar();

    /**
     * Logica que de ejecutarse cuando se pulsa el boton cancelar.
     */
    protected abstract void logicaCancelar();

    /**
     * Logica que de ejecutarse cuando se pulsa en el texto.
     */
    protected abstract void logicaTexto();

}
