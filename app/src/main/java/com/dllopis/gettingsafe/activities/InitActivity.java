package com.dllopis.gettingsafe.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.dllopis.gettingsafe.R;
import com.dllopis.gettingsafe.model.Preferencias;

public class InitActivity extends AppCompatActivity {

    Preferencias preferencias;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);
        preferencias = Preferencias.init(getApplicationContext());

        preferencias.initPrefs();
        ActivityCompat.requestPermissions(InitActivity.this,
                new String[]{Manifest.permission.READ_CONTACTS,
                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.SEND_SMS,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.INTERNET},
                1);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        preferencias.initPrefs();
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // La aplicación muestra la ventana de inicio y espera dos segundos para enviarte a la siguiente ventana.
                    new Handler().postDelayed(() -> {
                        //Si existen datos de usuario porque ya inició la aplicación antes entonces se envia directamente a la ventana principal.
                        if (preferencias.getUserData() != null) {
                            Intent main = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(main);
                            finish();
                            //Al no haber datos de usuario y ser la primera vez que entra se dirige a la ventana de datos para poder usar la aplicación.
                        } else {
                            Intent userData = new Intent(getApplicationContext(), UserDataActivity.class);
                            startActivity(userData);
                            finish();
                        }
                    }, 2000);   //2 seconds
                } else {
                    Toast.makeText(InitActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
}
