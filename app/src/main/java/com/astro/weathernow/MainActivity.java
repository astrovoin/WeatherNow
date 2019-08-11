package com.astro.weathernow;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences sharedPref;
    private static final String WEATHER_LOCATION = "location";
    private static final String WEATHER_LOCATION_DEFAULT = "Irkutsk";

    TextView cityField, detailsField, currentTemperatureField, humidity_field, pressure_field, updatedField;
    ProgressBar loader;
    ImageView selectCity;
    String city, OPEN_WEATHER_MAP_API = "acfe5130bfbb7a72e0c1dc06e1c2d5c7";
    WeatherBroadcastReceiver weatherReceiver;
    Intent weatherService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        final String key = "1";

        weatherReceiver = new WeatherBroadcastReceiver(this);
        sharedPref = getPreferences(MODE_PRIVATE);
      //  city = sharedPref.getString(key, getString(R.string.start_city));
        loader = findViewById(R.id.loader);
        selectCity = findViewById(R.id.imageView);
        cityField = findViewById(R.id.city_field);
        updatedField = findViewById(R.id.updated_field);
        detailsField = findViewById(R.id.details_field);
      //  currentTemperatureField = findViewById(R.id.current_temperature_field);
    //    humidity_field = findViewById(R.id.humidity_field);
        pressure_field = findViewById(R.id.pressure_field);


        loadPreferences();
       // taskLoadUp(city);
        startWeatherService();


        selectCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setTitle(getString(R.string.сhange_сity));
                final EditText input = new EditText(MainActivity.this);
                input.setText(sharedPref.getString(key, city));
                alertDialog.setView(input);

                alertDialog.setPositiveButton(getString(R.string.change),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                city = input.getText().toString();
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString(key, city);
                                editor.commit();
                                savePreferences();
                                stopService(weatherService);
                                startWeatherService();
                              //  taskLoadUp(city);

                            }
                        });
                alertDialog.setNegativeButton(getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                alertDialog.show();
            }
        });


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        savePreferences();
    }

    private void savePreferences() {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(WEATHER_LOCATION, WEATHER_LOCATION_DEFAULT);
        editor.commit();
    }

    private void loadPreferences() {
        String text = sharedPref.getString(WEATHER_LOCATION, WEATHER_LOCATION_DEFAULT);
        city = text;
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(
                WeatherService.ACTION_WEATHER_UPDATED);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(weatherReceiver, intentFilter);
        startWeatherService();
    }

    private void startWeatherService() {
        weatherService = new Intent(MainActivity.this,WeatherService.class);

        weatherService.putExtra(WeatherService.EXTRA_LOCATION, sharedPref.getString(WEATHER_LOCATION, WEATHER_LOCATION_DEFAULT));
        startService(weatherService);
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopService(weatherService);
        unregisterReceiver(weatherReceiver);
    }

/*
    public void taskLoadUp(String query) {
        if (Function.isNetworkAvailable(getApplicationContext())) {
            DownloadWeather task = new DownloadWeather();
            task.execute(query);
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }
    }

    class DownloadWeather extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loader.setVisibility(View.VISIBLE);

        }

        protected String doInBackground(String... args) {
            String xml = Function.excuteGet("http://api.openweathermap.org/data/2.5/weather?q=" + args[0] +
                    "&units=metric&appid=" + OPEN_WEATHER_MAP_API);
            return xml;
        }

        @Override
        protected void onPostExecute(String xml) {

            try {
                JSONObject json = new JSONObject(xml);
                if (json != null) {
                    JSONObject details = json.getJSONArray("weather").getJSONObject(0);
                    JSONObject main = json.getJSONObject("main");
                    DateFormat df = DateFormat.getDateTimeInstance();

                    cityField.setText(json.getString("name").toUpperCase(Locale.US) + ", " + json.getJSONObject("sys").getString("country"));
                    detailsField.setText(details.getString("description").toUpperCase(Locale.US));
                    currentTemperatureField.setText(String.format("%.0f", main.getDouble("temp")) + "°C");
                    humidity_field.setText("Humidity: " + main.getString("humidity") + "%");
                    pressure_field.setText("Pressure: " + main.getString("pressure") + " hPa");
                    updatedField.setText(df.format(new Date(json.getLong("dt") * 1000)));


                    loader.setVisibility(View.GONE);

                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), getString(R.string.error_check_city), Toast.LENGTH_SHORT).show();
            }


        }


    }
*/

}