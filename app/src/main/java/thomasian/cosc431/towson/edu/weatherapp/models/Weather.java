package thomasian.cosc431.towson.edu.weatherapp.models;

import java.util.UUID;

/**
 * Created by hthoma on 12/9/17.
 */

public class Weather {
    String cityname = "Baltimore";
    String temp = "Can find";
    String ID;

    public Weather(String cityname) {
        this.cityname = cityname;
        ID = UUID.randomUUID().toString();
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getTemp() {
        return temp;
    }

    public Weather() {
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }




    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }


}




