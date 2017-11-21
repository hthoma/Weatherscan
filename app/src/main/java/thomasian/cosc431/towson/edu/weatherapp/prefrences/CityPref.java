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

    public void setUnits(){
        prefs.edit().putBoolean("metric",!(prefs.getBoolean("metric",false))).commit();

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