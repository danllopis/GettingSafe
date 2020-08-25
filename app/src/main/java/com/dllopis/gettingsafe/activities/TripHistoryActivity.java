package com.dllopis.gettingsafe.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.dllopis.gettingsafe.R;
import com.dllopis.gettingsafe.model.DBHelper;
import com.dllopis.gettingsafe.model.TripAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TripHistoryActivity extends AppCompatActivity {

    private boolean doubleBackToExitPressedOnce = false;

    @BindView(R.id.backButton)
    CardView backButton;

    @BindView(R.id.lastsTripsList)
    ListView lastsTripsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_history);
        ButterKnife.bind(this);

        DBHelper dbHelper = new DBHelper(this);
        ArrayList trips = (ArrayList) dbHelper.getLastsTrips();
        TripAdapter adapter = new TripAdapter(this ,trips);

        lastsTripsList.setAdapter(adapter);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });
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