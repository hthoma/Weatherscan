package thomasian.cosc431.towson.edu.weatherapp.prefrences;

/**
 * Created by hthoma on 11/20/17.
 */

import android.app.Activity;
import android.content.SharedPreferences;

public class CityPref {

    SharedPreferences prefs;

    public CityPref(Activity activity){
        prefs = activity.getPreferences(Activity.MODE_PRIVATE);
    }


    public void setUnits(Boolean metric){
        prefs.edit().putBoolean("metric",metric).commit();

    }



    public Boolean getUnits(){
        return prefs.getBoolean("metric",false);
    }

    public String getCity(){
        return prefs.getString("city", "Baltimore, US");
    }

    public void setCity(String city){
        prefs.edit().putString("city", city).commit();
    }


}