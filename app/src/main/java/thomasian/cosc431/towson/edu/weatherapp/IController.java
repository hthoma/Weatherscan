package thomasian.cosc431.towson.edu.weatherapp;

import android.content.Intent;

import thomasian.cosc431.towson.edu.weatherapp.models.Weather;

/**
 * Created by randy on 9/22/17.
 */

public interface IController {
    void deleteWeather(Weather weather);
    void launchAddWeather();
    void handleNewWeatherResult(Intent data);
    void logWeather();
}
