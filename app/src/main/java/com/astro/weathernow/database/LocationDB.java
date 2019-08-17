package com.astro.weathernow.database;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class LocationDB extends RealmObject {
    @PrimaryKey
    private int id;
    private String location;
    private RealmList<WeatherDB> weather;

    public LocationDB(){}
    public LocationDB(int id, String location){
        this.location = location;
        this.id = id;
        weather = new RealmList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void addWeather(int hum, float temp){
        this.weather.add(new WeatherDB(hum, temp));
    }
}
