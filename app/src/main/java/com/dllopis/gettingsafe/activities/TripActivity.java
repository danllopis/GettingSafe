package com.dllopis.gettingsafe.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.dllopis.gettingsafe.R;
import com.dllopis.gettingsafe.model.DBHelper;
import com.dllopis.gettingsafe.model.Preferencias;
import com.dllopis.gettingsafe.model.Trayecto;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TripActivity extends AppCompatActivity {

    public static final String NOTIFICATION = "timer_notification";
    private NotificationManagerCompat notificationManager;
    private NotificationCompat.Builder builder;

    private String emergencyContact;
    private String contactMethod;
    private String userName;
    private long time, timeWasted;

    @BindView(R.id.remainingTime)
    TextView remainingTime;

    @BindView(R.id.cancelButton)
    CardView cancelButton;
    @BindView(R.id.arriveButton)
    CardView arriveButton;

    @BindView(R.id.addMinutesButton)
    CardView addMinutesButton;

    @BindView(R.id.numberPicker)
    NumberPicker numberPicker;

    Bundle extras;
    CountDownTimer cd, emergencyCD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);
        ButterKnife.bind(this);
        DBHelper dbHelper = new DBHelper(this);
        final Preferencias preferencias = Preferencias.init(getApplicationContext());

        ArrayList<String> userData = preferencias.getUserData();
        userName = userData.get(0);
        contactMethod = userData.get(2);
        emergencyContact = userData.get(3);

        createNotificationChannel();
        createNotification();

        extras = getIntent().getExtras();
        int minutes = extras.getInt("time",0);

        remainingTime.setText(minutes + " minutes");

        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(10);

        setTimers(minutes);

        setOnClickListeners(dbHelper, minutes);
    }

    private void setOnClickListeners(DBHelper dbHelper, int minutes) {
        addMinutesButton.setOnClickListener(view -> {
            cd.cancel();
            emergencyCD.cancel();
            long aux = time + TimeUnit.MINUTES.toMillis(numberPicker.getValue());
            cd = new CountDownTimer(aux, 1000) {
                @Override
                public void onTick(long l) {
                    remainingTime.setText("" + String.format("%d min, %d sec",
                            TimeUnit.MILLISECONDS.toMinutes(l),
                            TimeUnit.MILLISECONDS.toSeconds(l) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(l))));
                    time = l;
                    timeWasted = aux - l;
                }

                @Override
                public void onFinish() {
                    timeWasted = TimeUnit.MINUTES.toMillis(numberPicker.getValue()) + TimeUnit.MINUTES.toMillis(minutes);
                    Toast.makeText(TripActivity.this, "Iniciada espera para contacto", Toast.LENGTH_SHORT).show();
                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(500);
                    notificationManager.notify(1, builder.build());
                    emergencyCD.start();
                }
            }.start();
        });

        cancelButton.setOnClickListener(view -> {
            cd.cancel();
            emergencyCD.cancel();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        });

        arriveButton.setOnClickListener(view -> {
            cd.cancel();
            emergencyCD.cancel();
            dbHelper.addTrip(new Trayecto(1, extras.getString("origin").split(",")[0], extras.getString("destiny"), getMethod(extras.getString("method")), (int) TimeUnit.MILLISECONDS.toMinutes(timeWasted)));
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        });
    }

    private void setTimers(int minutes) {
        emergencyCD = new CountDownTimer(TimeUnit.MINUTES.toMillis(5), 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {

                emergencyContact();
            }
        };

        cd = new CountDownTimer(TimeUnit.MINUTES.toMillis(minutes), 1000) {
            @Override
            public void onTick(long l) {
                remainingTime.setText("" + String.format("%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes(l),
                        TimeUnit.MILLISECONDS.toSeconds(l) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(l))));
                time = l;
                timeWasted = TimeUnit.MINUTES.toMillis(minutes) - l;
            }

            @Override
            public void onFinish() {
                timeWasted = TimeUnit.MINUTES.toMillis(minutes);
                Toast.makeText(TripActivity.this, "Iniciada espera para contacto", Toast.LENGTH_SHORT).show();
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(500);
                notificationManager.notify(1, builder.build());
                emergencyCD.start();
            }
        }.start();
    }

    private void createNotification() {
        Intent fullScreenIntent = new Intent(this, TripActivity.class);
        PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(this, 0,
                fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        builder = new NotificationCompat.Builder(this, NOTIFICATION)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("Tiempo agotado")
                .setContentText("Pulse a la para volver y detener el tiempo o añadir minutos.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setFullScreenIntent(fullScreenPendingIntent, true);
        notificationManager = NotificationManagerCompat.from(this);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "timerNotificaction";
            String description = "Notification for the timer";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(NOTIFICATION, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void emergencyContact() {
        String[] methods = getResources().getStringArray(R.array.contact_method);

        if (contactMethod.equals(methods[0])) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", emergencyContact, null));
            startActivity(intent);
        } else if (contactMethod.equals(methods[1])) {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(emergencyContact, null, userName + " no llegó a " + extras.getString("destiny") + "partiendo desde "+extras.getString("origin").split(",")[0]+".", null, null);
        }
    }

    private String getMethod(String method){
        String[] tripMethods = getResources().getStringArray(R.array.trip_method);

        if(method.equals("walking")){
            return tripMethods[0];
        } else if(method.equals("driving")){
            return tripMethods[1]
;        } else {
            return tripMethods[2];
        }
    }
}