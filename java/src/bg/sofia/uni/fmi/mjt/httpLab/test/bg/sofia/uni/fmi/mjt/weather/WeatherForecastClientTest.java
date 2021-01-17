package bg.sofia.uni.fmi.mjt.weather;

import bg.sofia.uni.fmi.mjt.weather.dto.WeatherCondition;
import bg.sofia.uni.fmi.mjt.weather.dto.WeatherData;
import bg.sofia.uni.fmi.mjt.weather.dto.WeatherForecast;
import bg.sofia.uni.fmi.mjt.weather.exceptions.LocationNotFoundException;
import bg.sofia.uni.fmi.mjt.weather.exceptions.WeatherForecastClientException;
import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WeatherForecastClientTest {

    @Mock
    private HttpClient httpClientMock;

    @Mock
    private HttpResponse<String> httpResponseMock;

    private WeatherForecastClient client;

    @Before
    public void setUp() {
        client = new WeatherForecastClient(httpClientMock);
    }

    @Test(expected = LocationNotFoundException.class)
    public void testGetForecastLocationDoesNotExist() throws Exception {
        when(httpClientMock.send(Mockito.any(HttpRequest.class), ArgumentMatchers.<BodyHandler<String>>any()))
                .thenReturn(httpResponseMock);
        when(httpResponseMock.statusCode()).thenReturn(404);
        client.getForecast("RANDOMCITY");
    }

    @Test(expected = LocationNotFoundException.class)
    public void testGetForecastLocationDoesNotExistCityWithSpace() throws Exception {
        when(httpClientMock.send(Mockito.any(HttpRequest.class), ArgumentMatchers.<BodyHandler<String>>any()))
                .thenReturn(httpResponseMock);
        when(httpResponseMock.statusCode()).thenReturn(404);
        client.getForecast("RANDOM CITY");
    }

    @Test(expected = WeatherForecastClientException.class)
    public void testGetForecastCouldNotBeRetrieved() throws Exception {
        when(httpClientMock.send(Mockito.any(HttpRequest.class), ArgumentMatchers.<BodyHandler<String>>any()))
                .thenReturn(httpResponseMock);
        when(httpResponseMock.statusCode()).thenReturn(301);
        client.getForecast("TESTCITY");
    }

    @Test
    public void testGetForecastExactlyRight() throws Exception {
        when(httpClientMock.send(Mockito.any(HttpRequest.class), ArgumentMatchers.<BodyHandler<String>>any()))
                .thenReturn(httpResponseMock);
        when(httpResponseMock.statusCode()).thenReturn(200);

        WeatherCondition condition = new WeatherCondition("sunny");
        WeatherData data = new WeatherData(10.0, 12);
        WeatherForecast forecast = new WeatherForecast(new WeatherCondition[] {condition}, data);
        String jsonForecast = new Gson().toJson(forecast);

        when(httpResponseMock.body()).thenReturn(jsonForecast);

        WeatherForecast clientForecast = client.getForecast("Sofia");

        assertEquals(data, clientForecast.getWeatherData());
        assertEquals(condition, clientForecast.getWeatherConditions()[0]);
    }
}