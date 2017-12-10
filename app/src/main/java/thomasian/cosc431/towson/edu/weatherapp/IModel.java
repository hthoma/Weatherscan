package thomasian.cosc431.towson.edu.weatherapp;

import java.util.List;

import thomasian.cosc431.towson.edu.weatherapp.models.Weather;

/**
 * Created by hthoma on 12/9/17.
 */

public interface IModel {
    List<Weather> getWeather();
    Weather getWeather(String weatherId);
    void removeWeather(Weather weather);
    void updateWeather(Weather weather);
    void addWeather (Weather weather);
    void makeWeather();
}
