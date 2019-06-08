package com.abhi.weatherapp.model.db;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class WeatherHistoryDto
{
    @Id
    long id;
    String Temperature;
    String CityName;

    public WeatherHistoryDto(String temperature, String cityName) {
        Temperature = temperature;
        CityName = cityName;
    }

    public  WeatherHistoryDto(){}
    public String getTemperature() {
        return Temperature;
    }

    public void setTemperature(String temperature) {
        Temperature = temperature;
    }

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String cityName) {
        CityName = cityName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
