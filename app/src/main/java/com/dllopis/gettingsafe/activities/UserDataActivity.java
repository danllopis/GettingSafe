package com.dllopis.gettingsafe.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dllopis.gettingsafe.R;
import com.dllopis.gettingsafe.model.Preferencias;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserDataActivity extends AppCompatActivity {

    private boolean doubleBackToExitPressedOnce = false;
    private String emergencyContactNumber;
    @BindView(R.id.saveButton)
    CardView saveDataButton;

    @BindView(R.id.userName)
    EditText userNameEdit;

    @BindView(R.id.codeSpinner)
    Spinner contactMethodSpinner;

    @BindView(R.id.emergencyContactButton)
    CardView emergencyContactButton;

    @BindView(R.id.emergencyContactText)
    TextView emergencyContactText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_data);
        ButterKnife.bind(this);
        final Preferencias preferencias = Preferencias.init(getApplicationContext());

        ArrayList<String> userData = preferencias.getUserData();
        if(userData!=null){
            userNameEdit.append(userData.get(0));
            emergencyContactText.setText(userData.get(1));
            contactMethodSpinner.setSelection(((ArrayAdapter) contactMethodSpinner.getAdapter()).getPosition(userData.get(2)));
        }

        saveDataButton.setOnClickListener(v -> {
            ArrayList<String> UserDataUpdate = new ArrayList<>();
            UserDataUpdate.add(userNameEdit.getText().toString());
            UserDataUpdate.add(emergencyContactText.getText().toString());
            UserDataUpdate.add(contactMethodSpinner.getSelectedItem().toString());
            UserDataUpdate.add(emergencyContactNumber);

            preferencias.setUserData(UserDataUpdate);
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        });

        emergencyContactButton.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK,ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(intent, 1);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Uri contactData = data.getData();
                Cursor c = getContentResolver().query(contactData, null, null, null, null);
                if (c.moveToFirst()) {
                    String contactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                    String hasNumber = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                    String num = "";
                    if (Integer.valueOf(hasNumber) == 1) {
                        Cursor numbers = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                        while (numbers.moveToNext()) {
                            emergencyContactNumber = numbers.getString(numbers.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                        }

                        String name = c.getString(c.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                        emergencyContactText.setText(name);
                    }
                }
            }
        }
    }
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, R.string.exit, Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}