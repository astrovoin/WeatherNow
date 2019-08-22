package com.astro.weathernow.database;

import io.realm.RealmObject;

public class WeatherDB extends RealmObject {
    private int humidity;
    private float temperature;

    public WeatherDB(){}
    public WeatherDB(int hum, float temp){
        this.humidity = hum;
        this.temperature = temp;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

}
