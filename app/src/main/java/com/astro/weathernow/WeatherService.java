package com.astro.weathernow;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.astro.weathernow.database.LocationDB;
import com.astro.weathernow.model.WeatherResponse;


import io.realm.Realm;

public class WeatherService extends Service {

    public static final String ACTION_WEATHER_UPDATED = "weather_updated";
    public static final String EXTRA_HUMIDITY = "humidity";
    public static final String EXTRA_TEMPERATURE = "temperature";
    public static final String EXTRA_LOCATION = "location";
    public static final String EXTRA_CITY = "city";
    Thread thread;

    WeatherRequest request;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        final String location = intent.getStringExtra(EXTRA_LOCATION);
        //добавляем в базу текущее местоположение если еще не добавлено
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        if (realm.where(LocationDB.class).equalTo("location", location).count() == 0) {
            realm.copyToRealm(new LocationDB((int) realm.where(LocationDB.class).count(), location));
        }
        realm.commitTransaction();
        realm.close();

        request = new WeatherRequest(intent.getStringExtra(EXTRA_LOCATION));
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!Thread.interrupted()) {
                  try {
                      WeatherResponse response = request.run();
                      //сохраняем значение погоды для текущего местоположения
                         Realm realm = Realm.getDefaultInstance();
                        realm.beginTransaction();
                         realm.where(LocationDB.class).
                                  equalTo("location", location).
                                  findFirst().
                                  addWeather(response.getMain().getHumidity(), response.getMain().getTemp());
                          realm.commitTransaction();
                          realm.close();
                      Intent responseIntent = new Intent();
                      responseIntent.setAction(ACTION_WEATHER_UPDATED);
                      responseIntent.addCategory(Intent.CATEGORY_DEFAULT);
                      responseIntent.putExtra(EXTRA_HUMIDITY, response.getMain().getHumidity());
                      responseIntent.putExtra(EXTRA_TEMPERATURE, (int) (response.getMain().getTemp() - 273));

                      sendBroadcast(responseIntent);
                  } catch ( Exception e) {
                      e.printStackTrace();
                  }

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
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        thread.interrupt();
        super.onDestroy();
    }
}
