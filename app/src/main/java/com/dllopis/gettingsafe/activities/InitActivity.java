package com.dllopis.gettingsafe.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.dllopis.gettingsafe.R;

public class InitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // La aplicación muestra la ventana de inicio y espera dos segundos para enviarte a la siguiente ventana.
        new Handler().postDelayed(new Runnable() {
            public void run() {
                //Si existen datos de usuario porque ya inició la aplicación antes entonces se envia directamente a la ventana principal.
                if(true){
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