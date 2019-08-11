package com.astro.weathernow;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.astro.weathernow.model.WeatherResponse;


public class WeatherService extends Service{
    public static final String ACTION_WEATHER_UPDATED = "weather_updated";
    public static final String EXTRA_HUMIDITY = "humidity";
    public static final String EXTRA_TEMPERATURE = "temperature";
    public static final String EXTRA_LOCATION = "location";
    Thread thread;
    WeatherRequest request;


    @Override
    public void onCreate() {

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        request = new WeatherRequest(intent.getStringExtra(EXTRA_LOCATION));
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!Thread.interrupted()){
                    WeatherResponse response = request.run();
                    Intent responseIntent = new Intent();
                    responseIntent.setAction(ACTION_WEATHER_UPDATED);
                    responseIntent.addCategory(Intent.CATEGORY_DEFAULT);
                    responseIntent.putExtra(EXTRA_HUMIDITY, response.getMain().getHumidity());
                    responseIntent.putExtra(EXTRA_TEMPERATURE, (int)(response.getMain().getTemp() - 273));
                    sendBroadcast(responseIntent);
                    try {
                        Thread.sleep(30000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        thread.interrupt();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
