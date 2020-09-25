package com.dllopis.gettingsafe.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dllopis.gettingsafe.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;


public class InitTripActivity extends AppCompatActivity {


    private boolean doubleBackToExitPressedOnce = false;
    @BindView(R.id.backButton)
    CardView backButton;

    @BindView(R.id.destinyTextEdit)
    EditText destinyTextEdit;

    @BindView(R.id.methodTripSpinner)
    Spinner methodTripSpinner;

    @BindView(R.id.startTripButton)
    CardView startTripButton;

    @BindView(R.id.tripTime)
    TextView tripTimeText;

    private int time = 0;
    private Location lastKnownLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Address address;
    private String mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init_trip);

        ButterKnife.bind(this);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        getDeviceLocation();

        backButton.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        });

        startTripButton.setOnClickListener(view -> {
            if (!destinyTextEdit.getText().toString().isEmpty()) {

                Toast.makeText(this, R.string.tripStart, Toast.LENGTH_SHORT).show();
                Intent trip = new Intent(getApplicationContext(), TripActivity.class);
                trip.putExtra("time", time);
                trip.putExtra("destiny", destinyTextEdit.getText().toString().trim());
                trip.putExtra("origin", address.getAddressLine(0));
                trip.putExtra("method", mode);
                startActivity(trip);
                finish();
            } else {
                Toast.makeText(this, "Inserte el destino.", Toast.LENGTH_SHORT).show();
            }
        });

        destinyTextEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                getTripTime();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        methodTripSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    mode = "walking";
                } else if (i == 1) {
                    mode = "driving";
                } else {
                    mode="transit";
                }

                if(!destinyTextEdit.getText().toString().isEmpty())
                    getTripTime();
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void getTripTime() {
        String url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins="+address.getAddressLine(0)+"&destinations="+destinyTextEdit.getText().toString()+"&mode="+mode+"&key="+getResources().getString(R.string.google_maps_key);

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray jsonObject = response.getJSONArray("rows");
                        JSONObject jsonArray = jsonObject.getJSONObject(0);
                        JSONArray elements = jsonArray.getJSONArray("elements");
                        JSONObject duration = elements.getJSONObject(0);

                        String triptime = duration.getJSONObject("duration").getString("text");
                        time = Integer.parseInt(triptime.replaceAll("[^\\d]+",""));
                        tripTimeText.setText(time + " min");
                    } catch (JSONException e) {
                        tripTimeText.setText("");
                    }
                }, error -> {
        });

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }


    private void getDeviceLocation() {
        try {
            Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
            locationResult.addOnCompleteListener(this, task -> {
                if (task.isSuccessful()) {
                    lastKnownLocation = task.getResult();
                    if (lastKnownLocation != null) {
                        try {
                            address = (new Geocoder(getApplicationContext()).getFromLocation(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude(), 1).get(0));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage(), e);
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

        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }
}