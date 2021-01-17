package bg.sofia.uni.fmi.mjt.weather.dto;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public class WeatherForecast {
    @SerializedName("weather")
    private final WeatherCondition[] weatherConditions;

    @SerializedName("main")
    private final WeatherData weatherData;

    public WeatherForecast(WeatherCondition[] weather, WeatherData main) {
        this.weatherConditions = weather;
        this.weatherData = main;
    }

    public WeatherCondition[] getWeatherConditions() {
        return weatherConditions;
    }

    public WeatherData getWeatherData() {
        return weatherData;
    }

    @Override
    public int hashCode() {
        return weatherData.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        WeatherForecast forecast = (WeatherForecast) obj;

        return Arrays.equals(weatherConditions, forecast.getWeatherConditions())
                && weatherData.equals(forecast.getWeatherData());
    }
}
