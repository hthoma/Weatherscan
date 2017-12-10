package thomasian.cosc431.towson.edu.weatherapp.models;

/**
 * Created by hthoma on 12/9/17.
 */

public class Weather {
    String cityname,weathericon,temp;

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

}
