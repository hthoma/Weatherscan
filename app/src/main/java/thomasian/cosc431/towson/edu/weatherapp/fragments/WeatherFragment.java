package thomasian.cosc431.towson.edu.weatherapp.fragments;

import android.content.Context;
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

import com.kwabenaberko.openweathermaplib.Units;
import com.kwabenaberko.openweathermaplib.implementation.OpenWeatherMapHelper;
import com.kwabenaberko.openweathermaplib.models.threehourforecast.ThreeHourForecast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import thomasian.cosc431.towson.edu.weatherapp.FetchWeather;
import thomasian.cosc431.towson.edu.weatherapp.R;
import thomasian.cosc431.towson.edu.weatherapp.prefrences.CityPref;

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
    TextView Day1Tv;
    TextView Day2Tv;
    TextView Day3Tv;
    TextView Icon1Tv;
    TextView Icon2Tv;
    TextView Icon3Tv;
    Handler handler;
    Context context;
    public WeatherFragment() {
        handler = new Handler();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.weather_frag, container, false);
        cityField = (TextView) rootView.findViewById(R.id.city_field);
        updatedField = (TextView) rootView.findViewById(R.id.updated_field);
        detailsField = (TextView) rootView.findViewById(R.id.details_field);
        Icon1Tv = (TextView)rootView.findViewById(R.id.Icon1);
        Icon2Tv = (TextView)rootView.findViewById(R.id.Icon2);
        Icon3Tv = (TextView)rootView.findViewById(R.id.Icon3);
        currentTemperatureField = (TextView) rootView.findViewById(R.id.current_temperature_field);
        weatherIcon = (TextView) rootView.findViewById(R.id.weather_icon);
        weatherIcon.setTypeface(weatherFont);
        Icon1Tv.setTypeface(weatherFont);
        Icon2Tv.setTypeface(weatherFont);
        Icon3Tv.setTypeface(weatherFont);

        Day1Tv = (TextView)rootView.findViewById(R.id.Day1);
        Day2Tv = (TextView)rootView.findViewById(R.id.Day2);
        Day3Tv = (TextView)rootView.findViewById(R.id.Day3);

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weatherFont = Typeface.createFromAsset(getActivity().getAssets(), "weather.ttf");

        updateWeatherData(new CityPref(getActivity()).getCity());


    }






    private void set3Day(final String city){

        OpenWeatherMapHelper helper = new OpenWeatherMapHelper();
        helper.setApiKey("4395b9c2cf80a89f7347816081680ecb");
        helper.setUnits(Units.IMPERIAL);
        weatherFont = Typeface.createFromAsset(getContext().getAssets(), "weather.ttf");
        helper.getThreeHourForecastByCityName(city, new OpenWeatherMapHelper.ThreeHourForecastCallback() {
            @Override
            public void onSuccess(ThreeHourForecast threeHourForecast) {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, 1);
                Date date = calendar.getTime();
                Day1Tv.setText(new SimpleDateFormat("EE", Locale.ENGLISH).format(date.getTime()));
                calendar.add(Calendar.DATE, 1);
                date = calendar.getTime();
                Day2Tv.setText(new SimpleDateFormat("EE", Locale.ENGLISH).format(date.getTime()));
                calendar.add(Calendar.DATE, 1);
                date = calendar.getTime();
                Day3Tv.setText(new SimpleDateFormat("EE", Locale.ENGLISH).format(date.getTime()));
                Icon1Tv.setText(getWeatherIcon(safeLongToInt(threeHourForecast.getThreeHourWeatherArray().get(1).getWeatherArray().get(0).getId())));
                Log.d("WeatherFrag",Integer.toString(safeLongToInt(threeHourForecast.getThreeHourWeatherArray().get(1).getWeatherArray().get(0).getId())));
                Icon2Tv.setText(getWeatherIcon(safeLongToInt(threeHourForecast.getThreeHourWeatherArray().get(2).getWeatherArray().get(0).getId())));
                Log.d("WeatherFrag",Integer.toString(safeLongToInt(threeHourForecast.getThreeHourWeatherArray().get(2).getWeatherArray().get(0).getId())));
                Icon3Tv.setText(getWeatherIcon(safeLongToInt(threeHourForecast.getThreeHourWeatherArray().get(3).getWeatherArray().get(0).getId())));
                Log.d("WeatherFrag",Integer.toString(safeLongToInt(threeHourForecast.getThreeHourWeatherArray().get(3).getWeatherArray().get(0).getId())));

                Log.v("WeatherFragment",
                        "City/Country: "+ threeHourForecast.getCity().getName() + "/" + threeHourForecast.getCity().getCountry() +"\n"
                                +"Forecast Array Count: " + threeHourForecast.getCnt() +"\n"
                                //For this example, we are logging details of only the first forecast object in the forecasts array
                                +"First Forecast Weather Description: (" + new SimpleDateFormat("EE", Locale.ENGLISH).format(date.getTime()) + ") "  + threeHourForecast.getThreeHourWeatherArray().get(1).getWeatherArray().get(0).getId() +"\n"
                                +"Second Forecast Weather Description: " + threeHourForecast.getThreeHourWeatherArray().get(2).getWeatherArray().get(0).getId() +"\n"
                                +"Third Forecast Weather Description: " + threeHourForecast.getThreeHourWeatherArray().get(3).getWeatherArray().get(0).getId() +"\n"
                );




            }


            @Override
            public void onFailure(Throwable throwable) {
                Log.v("WeatherFragment", throwable.getMessage());
            }
        });

    }

    private void updateWeatherData(final String city) {
        new Thread() {
            public void run() {
                final JSONObject json = FetchWeather.getJSON(getActivity(), city, getActivity());
                Log.d("UpdateWeatherData",json.toString() +"");
                if (json == null) {
                    handler.post(new Runnable() {
                        public void run() {
                            Toast.makeText(context,
                                    context.getString(R.string.place_not_found),
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







    public static int safeLongToInt(long l) {
        if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
            throw new IllegalArgumentException
                    (l + " cannot be cast to int without changing its value.");
        }
        return (int) l;
    }

    private void renderWeather(JSONObject json){
        try {
            cityField.setText(json.getString("name").toUpperCase(Locale.US) +
                    ", " +
                    json.getJSONObject("sys").getString("country"));

            JSONObject details = json.getJSONArray("weather").getJSONObject(0);
            Log.d("renderWeather","Got weather");
            JSONObject main = json.getJSONObject("main");
            Log.d("renderWeather","Got main");
            JSONObject wind = json.getJSONObject("wind");
            Log.d("renderWeather","Got wind");
            if (new CityPref(getActivity()).getUnits()){
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
            Log.d("renderWeather","Got weathericon");
            set3Day(json.getString("name").toUpperCase(Locale.US));
            Log.d("renderWeather","Got 3day");
        }catch(JSONException e){
            e.printStackTrace();
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

    private String getWeatherIcon(int actualId){

        String icon = "";
        if(actualId == 800)
            icon = getActivity().getString(R.string.weather_sunny);
        else {
            int id = actualId / 100;
            Log.d("WeatherFrag", Integer.toString(id));

            switch (id) {
                case 2:
                    icon = getActivity().getString(R.string.weather_thunder);
                    break;
                case 3:
                    icon = getActivity().getString(R.string.weather_drizzle);
                    break;
                case 7:
                    icon = getActivity().getString(R.string.weather_foggy);
                    break;
                case 8:
                    icon = getActivity().getString(R.string.weather_cloudy);
                    break;
                case 6:
                    icon = getActivity().getString(R.string.weather_snowy);
                    break;
                case 5:
                    icon = getActivity().getString(R.string.weather_rainy);
                    break;
            }
        }
        return icon;
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
        updateWeatherData(new CityPref(getActivity()).getCity());
    }


}