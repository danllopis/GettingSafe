package com.dllopis.gettingsafe.activities;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.content.SharedPreferences;

import com.dllopis.gettingsafe.R;
import com.dllopis.gettingsafe.model.Preferencias;

public class InitActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);
        final Preferencias preferencias = Preferencias.init(getApplicationContext());
        preferencias.initPrefs();

        // La aplicación muestra la ventana de inicio y espera dos segundos para enviarte a la siguiente ventana.
        new Handler().postDelayed(new Runnable() {
            public void run() {
                //Si existen datos de usuario porque ya inició la aplicación antes entonces se envia directamente a la ventana principal.
                if(preferencias.getUserData()!=null){
                    Intent main = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(main);
                    finish();
                    //Al no haber datos de usuario y ser la primera vez que entra se dirige a la ventana de datos para poder usar la aplicación.
                } else {
                    Intent userData = new Intent(getApplicationContext(), UserDataActivity.class);
                    startActivity(userData);
                    finish();
                }
            }
        }, 2000);   //2 seconds
    }
}