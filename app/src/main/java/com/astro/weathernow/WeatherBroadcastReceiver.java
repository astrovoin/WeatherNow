package com.astro.weathernow;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

public class WeatherBroadcastReceiver extends BroadcastReceiver {
    TextView temperature;
    TextView humidity;

    WeatherBroadcastReceiver(Activity activity){
        this.temperature = activity.findViewById(R.id.current_temperature_field);
        this.humidity = activity.findViewById(R.id.humidity_field);

    }
    @Override
    public void onReceive(Context context, Intent intent) {
        temperature.setText(intent.getIntExtra(WeatherService.EXTRA_TEMPERATURE, 0));
        humidity.setText(intent.getIntExtra(WeatherService.EXTRA_HUMIDITY, 0));

    }
}
