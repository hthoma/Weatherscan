package thomasian.cosc431.towson.edu.weatherapp;

import java.util.ArrayList;
import java.util.List;

import thomasian.cosc431.towson.edu.weatherapp.models.Weather;


public class WeatherModel implements IModel {

   List<Weather> reminders;

    public void makeWeathers(){}

   public WeatherModel() {
        reminders = new ArrayList<>();
       makeSongs();
   }


    public void makeSongs() {


    }


    @Override
    public List<Weather> getWeather() {
        return null;
    }

    @Override
    public Weather getWeather(String weatherId) {
        return null;
    }

    @Override
    public void removeWeather(Weather weather) {

    }

    @Override
    public void updateWeather(Weather weather) {

    }

    @Override
    public void addWeather(Weather weather) {

    }

    @Override
    public void makeWeather() {

    }
}
