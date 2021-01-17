package bg.sofia.uni.fmi.mjt.weather.dto;

public class WeatherCondition {
    private final String description;

    public WeatherCondition(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public int hashCode() {
        return description.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        WeatherCondition condition = (WeatherCondition) obj;

        return description.equals(condition.getDescription());
    }
}
