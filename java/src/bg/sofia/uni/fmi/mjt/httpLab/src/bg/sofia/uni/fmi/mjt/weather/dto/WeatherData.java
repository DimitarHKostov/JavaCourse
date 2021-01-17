package bg.sofia.uni.fmi.mjt.weather.dto;

import com.google.gson.annotations.SerializedName;

public class WeatherData {
    private final double temp;

    @SerializedName("feels_like")
    private final double feelsLike;

    public WeatherData(double temp, double feelsLike) {
        this.temp = temp;
        this.feelsLike = feelsLike;
    }

    public double getTemp() {
        return temp;
    }

    public double getFeels_like() {
        return feelsLike;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(temp);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        WeatherData data = (WeatherData) obj;

        return temp == data.getTemp() && feelsLike == data.getFeels_like();
    }
}
