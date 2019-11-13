package com.example.demoservices;




import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

public class MyService6 extends Service {
    String GPS_FILTER = "matos.action.GPSFIX";
    Thread serviceThread;
    LocationManager lm;
    GPSListener myLocationListener;
//    private Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            Log.e("MyService5Async-Handler", "Handler got from MyService5Async: "
//                    + (String)msg.obj);
//        }
//    };
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public void onStart(Intent intent, int startId) {
        Log.e("GpsService-onStart", "I am alive-GPS!");
        serviceThread = new Thread(new Runnable() {
            public void run() {
                // getGPSFix_Version1(); // uses NETWORK provider
                getGPSFix_Version2(); // uses GPS chip provider
            }// run
        });
        serviceThread.start();
    }// onStart

    public void getGPSFix_Version2() {
        try {
            Looper.prepare();
// try to get your GPS location using the
// LOCATION.SERVIVE provider

// This listener will catch and disseminate location updates
            myLocationListener = new GPSListener();
// define update frequency for GPS readings
            long minTime = 2000; // 2 seconds
            float minDistance = 5; // 5 meter
// request GPS updates
            lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                Toast.makeText(this,"aaaaaaaa",Toast.LENGTH_LONG).show();
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, myLocationListener);

            }

           // LocationManager.NETWORK_PROVIDER, 5000, 0, locationListener

            Looper.loop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("GpsService-onDestroy", "I am dead-GPS");
        try {
            lm.removeUpdates(myLocationListener);
           // isRunning = false;
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }// onDestroy

    private class GPSListener implements LocationListener {
        public void onLocationChanged(Location location) {
// capture location data sent by current provider
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
// assemble data bundle to be broadcasted
            Intent myFilteredResponse = new Intent(GPS_FILTER);
            myFilteredResponse.putExtra("latitude", latitude);
            myFilteredResponse.putExtra("longitude", longitude);
            myFilteredResponse.putExtra("provider", location.getProvider());
            Log.e(">>GPS_Service<<", "Lat:" + latitude + " lon:" + longitude);
// send the location data out
            sendBroadcast(myFilteredResponse);
//            Message msg = handler.obtainMessage(5, "Lat:" + latitude + " lon:" + longitude);
//            handler.sendMessage(msg);
        }
        public void onProviderDisabled(String provider) {
        }
        public void onProviderEnabled(String provider) {
        }
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };// GPSListener class
}
