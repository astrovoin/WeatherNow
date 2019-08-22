package com.astro.weathernow;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import io.realm.Realm;


// 1. Не показывает ни температуру ни влажность. Думаю, дело в сервисе. Не знаю как делать
// 2. Как сделать, чтобы показывался город после ввода? В WeatherBroadcastReceiver сделать наподобие показа температуры и влажности?
// Но тогда как правильно это сделать, чтобы все работало?

public class MainActivity extends AppCompatActivity {
    private SharedPreferences sharedPref;
    private static final String WEATHER_LOCATION = "location";
    private static String WEATHER_LOCATION_DEFAULT = "Irkutsk";

    TextView cityField, detailsField, currentTemperatureField, humidity_field, pressure_field, updatedField;
    ImageView selectCity;
    TextView temperature;
    Intent weatherService;
    WeatherBroadcastReceiver weatherReceiver;
    String OPEN_WEATHER_MAP_API = "acfe5130bfbb7a72e0c1dc06e1c2d5c7";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Realm.init(this);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);


        //      initRetrofit();
        initGui();

        initPreferences();

        // requestRetrofit(sharedPref.getString(WEATHER_LOCATION, WEATHER_LOCATION_DEFAULT), OPEN_WEATHER_MAP_API);
        startWeatherService();
        initEvents();


        Picasso picasso = Picasso.with(this);
        picasso.load("https://img1.goodfon.ru/original/2560x1600/8/84/peyzazhi-priroda-trava-holmy.jpg")
               .into((ImageView) findViewById(R.id.background_view));


    }

    private void initGui() {
        selectCity = findViewById(R.id.imageView);
        cityField = findViewById(R.id.city_field);
        updatedField = findViewById(R.id.updated_field);
        detailsField = findViewById(R.id.details_field);
        pressure_field = findViewById(R.id.pressure_field);
        temperature = findViewById(R.id.current_temperature_field);
    }

    // Обработка клика
    private void initEvents() {


        selectCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setTitle(getString(R.string.сhange_сity));
                final EditText input = new EditText(MainActivity.this);
                input.setText(sharedPref.getString(WEATHER_LOCATION, cityField.getText().toString()));
                alertDialog.setView(input);

                alertDialog.setPositiveButton(getString(R.string.change),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {


                                WEATHER_LOCATION_DEFAULT = input.getText().toString();
                                //   savePreferences();
                                //   requestRetrofit(WEATHER_LOCATION_DEFAULT, OPEN_WEATHER_MAP_API);

                                savePreferences();
                                stopService(weatherService);
                                startWeatherService();

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

    private void initPreferences() {
        sharedPref = getPreferences(MODE_PRIVATE);
        loadPreferences();

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
        sharedPref.getString(WEATHER_LOCATION, WEATHER_LOCATION_DEFAULT);
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
        weatherService = new Intent(MainActivity.this, WeatherService.class);

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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        savePreferences();
    }

    private void initPreferences() {
        sharedPref = getPreferences(MODE_PRIVATE);
        loadPreferences();

    }

    private void savePreferences() {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(WEATHER_LOCATION, WEATHER_LOCATION_DEFAULT);
        //editor.apply();
        editor.commit();
    }

    private void loadPreferences() {

        sharedPref.getString(WEATHER_LOCATION, WEATHER_LOCATION_DEFAULT);


    }








    private void initRetrofit() {
        Retrofit retrofit;
        retrofit = new Retrofit.Builder()
// Базовая часть адреса
                .baseUrl("https://api.openweathermap.org")
// Конвертер, необходимый для преобразования JSON в объекты
                .addConverterFactory(GsonConverterFactory.create())
                .build();
// Создаем объект, при помощи которого будем выполнять запросы
        openWeather = retrofit.create(OpenWeather.class);
    }

    private void requestRetrofit(final String city, String keyApi) {
        openWeather.loadWeather(city, keyApi)
                .enqueue(new Callback<WeatherResponse>() {
                    @Override
                    public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                        if (response.body() != null) {
                            double tmp = response.body().getMain().getTemp() - 273.15;
                            int tmpint = (int) tmp;
                            temperature.setText(tmpint + " C°");
                            cityField.setText(sharedPref.getString(WEATHER_LOCATION, WEATHER_LOCATION_DEFAULT));
                            // cityField.setText(WEATHER_LOCATION_DEFAULT);
                        }

                    }

                    @Override
                    public void onFailure(Call<WeatherResponse> call, Throwable t) {
                        temperature.setText("Error");
                    }
                });

    }
*/
}