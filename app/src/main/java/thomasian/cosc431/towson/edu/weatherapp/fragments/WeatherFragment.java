package thomasian.cosc431.towson.edu.weatherapp.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import thomasian.cosc431.towson.edu.weatherapp.prefrences.CityPref;
import thomasian.cosc431.towson.edu.weatherapp.FetchWeather;
import thomasian.cosc431.towson.edu.weatherapp.R;

/**
 * Created by hthoma on 11/20/17.
 */

public class WeatherFragment extends Fragment {
    Typeface weatherFont;

    TextView cityField;
    TextView updatedField;
    TextView detailsField;
    TextView currentTemperatureField;
    TextView weatherIcon;
    Boolean metric = false;
    Handler handler;

    public WeatherFragment() {
        handler = new Handler();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.weather_frag, container, false);
        cityField = (TextView) rootView.findViewById(R.id.city_field);
        updatedField = (TextView) rootView.findViewById(R.id.updated_field);
        detailsField = (TextView) rootView.findViewById(R.id.details_field);
        currentTemperatureField = (TextView) rootView.findViewById(R.id.current_temperature_field);
        weatherIcon = (TextView) rootView.findViewById(R.id.weather_icon);
        weatherIcon.setTypeface(weatherFont);
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weatherFont = Typeface.createFromAsset(getActivity().getAssets(), "weather.ttf");
        updateWeatherData(new CityPref(getActivity()).getCity());
    }


    private void updateWeatherData(final String city) {
        new Thread() {
            public void run() {
                final JSONObject json = FetchWeather.getJSON(getActivity(), city, metric);
                if (json == null) {
                    handler.post(new Runnable() {
                        public void run() {
                            Toast.makeText(getActivity(),
                                    getActivity().getString(R.string.place_not_found),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        public void run() {
                            renderWeather(json);
                        }
                    });
                }
            }
        }.start();
    }

    private void renderWeather(JSONObject json){
        try {
            cityField.setText(json.getString("name").toUpperCase(Locale.US) +
                    ", " +
                    json.getJSONObject("sys").getString("country"));

            JSONObject details = json.getJSONArray("weather").getJSONObject(0);
            JSONObject main = json.getJSONObject("main");
            JSONObject wind = json.getJSONObject("wind");
            if (metric){
                detailsField.setText(
                        details.getString("description").toUpperCase(Locale.US) +
                                "\n" + "Humidity: " + main.getString("humidity") + "%" +
                                "\n" + "Wind Speed: " + wind.getString("speed") +" " + "m/s");
                currentTemperatureField.setText(
                        String.format("%.2f", main.getDouble("temp"))+ " °C");
            }
            else{
                detailsField.setText(
                        details.getString("description").toUpperCase(Locale.US) +
                                "\n" + "Humidity: " + main.getString("humidity") + "%" +
                                "\n" + "Wind Speed: " + wind.getString("speed") +" " + "mi/h");
                currentTemperatureField.setText(
                        String.format("%.2f", main.getDouble("temp"))+ " °F");
            }

            DateFormat df = DateFormat.getDateTimeInstance();
            String updatedOn = df.format(new Date(json.getLong("dt")*1000));
            updatedField.setText("Last update: " + updatedOn);

            setWeatherIcon(details.getInt("id"),
                    json.getJSONObject("sys").getLong("sunrise") * 1000,
                    json.getJSONObject("sys").getLong("sunset") * 1000);

        }catch(Exception e){
            Log.d("Weather Frag", "One or more fields not found in the JSON data");
        }
    }


    private void setWeatherIcon(int actualId, long sunrise, long sunset){
        int id = actualId / 100;
        String icon = "";
        if(actualId == 800){
            long currentTime = new Date().getTime();
            if(currentTime>=sunrise && currentTime<sunset) {
                icon = getActivity().getString(R.string.weather_sunny);
            } else {
                icon = getActivity().getString(R.string.weather_clear_night);
            }
        } else {
            switch(id) {
                case 2 : icon = getActivity().getString(R.string.weather_thunder);
                    break;
                case 3 : icon = getActivity().getString(R.string.weather_drizzle);
                    break;
                case 7 : icon = getActivity().getString(R.string.weather_foggy);
                    break;
                case 8 : icon = getActivity().getString(R.string.weather_cloudy);
                    break;
                case 6 : icon = getActivity().getString(R.string.weather_snowy);
                    break;
                case 5 : icon = getActivity().getString(R.string.weather_rainy);
                    break;
            }
        }
        weatherIcon.setText(icon);
    }
    public void changeCity(String city){
        updateWeatherData(city);
    }

    public void refreshData(){
        updateWeatherData(new CityPref(getActivity()).getCity());
    }

    public String TextFriend(){

        return("The temperature in " + cityField.getText().toString() + " is " + currentTemperatureField.getText().toString()+ " ! I sent this from Harry's weather app. Isnt that cool?");
    }

    public String getCity(){
        return cityField.getText().toString();

    }

    public void changeUnit(){
        metric = !metric;
    }

}