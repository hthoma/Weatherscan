package thomasian.cosc431.towson.edu.weatherapp.models;

import org.json.JSONObject;

import java.util.Date;

import thomasian.cosc431.towson.edu.weatherapp.R;
import thomasian.cosc431.towson.edu.weatherapp.fragments.WeatherFragment;

/**
 * Created by hthoma on 12/9/17.
 */

public class Weather {
    String cityname = "Baltimore";
    String weathericon = "&#xf014;";
    String temp = "60";
    boolean metric = false;
    WeatherFragment wf = new WeatherFragment();

    public Weather(String cityname) {
        this.cityname = cityname;
    }

    public Weather(String cityname, String weathericon, String temp) {
        this.cityname = cityname;
        this.weathericon = weathericon;
        this.temp = temp;
    }

    public Weather() {
    }

    public boolean isMetric() {
        return metric;
    }

    public void setMetric(boolean metric) {
        this.metric = metric;
    }

    public void updateweatherinfo(JSONObject json){

        }



    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public String getWeathericon() {
        return weathericon;
    }

    public void setWeathericon(String weathericon) {
        this.weathericon = weathericon;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    private void setWeatherIcon(int actualId, long sunrise, long sunset){
        int id = actualId / 100;
        String icon = "";
        if(actualId == 800){
            long currentTime = new Date().getTime();
            if(currentTime>=sunrise && currentTime<sunset) {
                icon = wf.getString(R.string.weather_sunny);
            } else {
                icon = wf.getString(R.string.weather_clear_night);
            }
        } else {
            switch(id) {
                case 2 : icon = wf.getString(R.string.weather_thunder);
                    break;
                case 3 : icon = wf.getString(R.string.weather_drizzle);
                    break;
                case 7 : icon = wf.getString(R.string.weather_foggy);
                    break;
                case 8 : icon = wf.getString(R.string.weather_cloudy);
                    break;
                case 6 : icon = wf.getString(R.string.weather_snowy);
                    break;
                case 5 : icon = wf.getString(R.string.weather_rainy);
                    break;
            }
        }
       setWeathericon(icon);
    }
}




