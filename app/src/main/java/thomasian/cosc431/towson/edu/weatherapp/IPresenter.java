package thomasian.cosc431.towson.edu.weatherapp;

import java.util.List;

import thomasian.cosc431.towson.edu.weatherapp.models.Weather;

/**
 * Created by hthoma on 12/9/17.
 */

public interface IPresenter {
    void deleteWeather(Weather weather);
    void handleNewWeatherResult(Weather weather);
    List<Weather> getWeatherFromModel();
    void logWeather();
    void launchAddWeatherActivity();
}
