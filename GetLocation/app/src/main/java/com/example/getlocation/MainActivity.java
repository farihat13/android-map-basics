package com.example.getlocation;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Calendar;
import java.util.Date;

// https://www.tutorialspoint.com/android/android_location_based_services.htm
public class MainActivity extends Activity {

    Button btnShowLocation;
    private static final int REQUEST_CODE_PERMISSION = 2;
    String mLocationPermission = Manifest.permission.ACCESS_FINE_LOCATION;
    String mStoragePermission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    String mStoragePermission2 = Manifest.permission.READ_EXTERNAL_STORAGE;
    TextView textView;
    // GPSTracker class
    GPSTracker gps;
    FileOutputStream file;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView);

        try {
            if (ActivityCompat.checkSelfPermission(this, mStoragePermission) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{mStoragePermission}, REQUEST_CODE_PERMISSION);
                // If any permission above not allowed by user, this condition will execute every time, else your else part will work
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            file = new FileOutputStream(getFilesDir() + "/fariha.txt", true);
            textView.setText(getFilesDir() + "/fariha.txt");
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, e.getMessage() + "\nFailed", Toast.LENGTH_LONG).show();
        }

        try {
            if (ActivityCompat.checkSelfPermission(this, mLocationPermission) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{mLocationPermission}, REQUEST_CODE_PERMISSION);

                // If any permission above not allowed by user, this condition will execute every time, else your else part will work
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        btnShowLocation = findViewById(R.id.button);

        // show location button click event
        btnShowLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // create class object
                gps = new GPSTracker(MainActivity.this);

                // check if GPS enabled
                if (gps.canGetLocation()) {

                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                    // \n is for new line
                    String toastText = "Lat: " + latitude + ", Long: " + longitude;
                    textView.setText(toastText);
                    if (file != null) {
                        Date currentTime = Calendar.getInstance().getTime();
                        PrintStream printstream = new PrintStream(file);
                        printstream.println(currentTime + " " + toastText);
                        printstream.flush();
                        Toast.makeText(getApplicationContext(), toastText, Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplicationContext(), "could not save location", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gps.showSettingsAlert();
                }

            }
        });
    }
}