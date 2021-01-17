package bg.sofia.uni.fmi.mjt.weather;

import bg.sofia.uni.fmi.mjt.weather.dto.WeatherForecast;
import bg.sofia.uni.fmi.mjt.weather.exceptions.LocationNotFoundException;
import bg.sofia.uni.fmi.mjt.weather.exceptions.WeatherForecastClientException;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class WeatherForecastClient {
    private final HttpClient client;
    private static final String API_KEY = "API_KEY";
    private static final Gson GSON = new Gson();

    public WeatherForecastClient(HttpClient weatherHttpClient) {
        this.client = weatherHttpClient;
    }

    /**
     * Fetches the weather forecast for the specified city.
     *
     * @return the forecast
     * @throws LocationNotFoundException      if the city is not found
     * @throws WeatherForecastClientException if information regarding the weather for this
     *                                        location could not be retrieved
     */
    public WeatherForecast getForecast(String city) throws WeatherForecastClientException {
        try {
            city = city.replaceAll(" ", "%20");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://api.openweathermap.org/data/2.5/weather?q=" + city
                            + "&units=metric&lang=bg&appid=" + API_KEY))
                    .build();

            var response = this.client.send(request, HttpResponse.BodyHandlers.ofString());

            int statusCode = response.statusCode();

            if (statusCode == 404) {
                throw new LocationNotFoundException();
            }

            if (statusCode != 200) {
                throw new WeatherForecastClientException();
            }

            return GSON.fromJson(response.body(), WeatherForecast.class);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void main(String[] args) throws WeatherForecastClientException {
        HttpClient client = HttpClient.newBuilder().build();
        WeatherForecastClient f = new WeatherForecastClient(client);
        f.getForecast("random City");
    }
}
