package thomasian.cosc431.towson.edu.weatherapp.adapters;

import android.graphics.Typeface;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.kwabenaberko.openweathermaplib.Units;
import com.kwabenaberko.openweathermaplib.implementation.OpenWeatherMapHelper;
import com.kwabenaberko.openweathermaplib.models.currentweather.CurrentWeather;

import thomasian.cosc431.towson.edu.weatherapp.IController;
import thomasian.cosc431.towson.edu.weatherapp.R;
import thomasian.cosc431.towson.edu.weatherapp.database.WeatherDataSource;
import thomasian.cosc431.towson.edu.weatherapp.models.Weather;

import static android.content.ContentValues.TAG;

/**
 * Created by hthoma on 12/9/17.
 */

public class WeatherViewHolder extends RecyclerView.ViewHolder {
    Weather weather;
    TextView locationTv;
    TextView weathericonTv;
    TextView weathertempTv;
    ConstraintLayout layout;
    IController controller;
    Typeface weatherFont;
    OpenWeatherMapHelper helper = new OpenWeatherMapHelper();
    WeatherDataSource weatherdata;

    public WeatherViewHolder(View itemView, final IController controller){
        super(itemView);

        this.controller = controller;
        locationTv = (TextView)itemView.findViewById(R.id.city_field);
        weathericonTv = (TextView)itemView.findViewById(R.id.weather_icon);
        weathertempTv = (TextView)itemView.findViewById(R.id.current_temperature_field);
        layout = (ConstraintLayout)itemView.findViewById(R.id.WeatherLayout);




    }

    public void bindWeather(final Weather weather, final Boolean ismetric) {
        helper.setApiKey("4395b9c2cf80a89f7347816081680ecb");
        if(ismetric)
        helper.setUnits(Units.METRIC);
        else
        helper.setUnits(Units.IMPERIAL);
        weatherFont = Typeface.createFromAsset(itemView.getContext().getAssets(), "weather.ttf");
        helper.getCurrentWeatherByCityName(weather.getCityname(), new OpenWeatherMapHelper.CurrentWeatherCallback() {
            @Override
            public void onSuccess(CurrentWeather currentWeather) {
                Log.v(TAG,
                        "Coordinates: " + currentWeather.getCoord().getLat() + ", " + currentWeather.getCoord().getLat() + "\n"
                                + "Weather Description: " + currentWeather.getWeatherArray().get(0).getId() + "\n"
                                + "Max Temperature: " + currentWeather.getMain().getTemp() + "\n"
                                + "Wind Speed: " + currentWeather.getWind().getSpeed() + "\n"
                                + "City, Country: " + currentWeather.getName() + ", " + currentWeather.getSys().getCountry()
                );
                if(ismetric) {
                    weathertempTv.setText(Double.toString(currentWeather.getMain().getTemp()) + " °C");
                    weather.setTemp(Double.toString(currentWeather.getMain().getTemp()) + " °C");
                    weathericonTv.setTypeface(weatherFont);
                    weathericonTv.setText(getWeatherIcon(safeLongToInt(currentWeather.getWeatherArray().get(0).getId())));
                }
               else{
                    weathertempTv.setText(Double.toString(currentWeather.getMain().getTemp()) + " °F");
                    weather.setTemp(Double.toString(currentWeather.getMain().getTemp()) + " °F");
                    weathericonTv.setTypeface(weatherFont);
                    weathericonTv.setText(getWeatherIcon(safeLongToInt(currentWeather.getWeatherArray().get(0).getId())));


                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                weather.setTemp("Cannot Find City");
                Log.v(TAG, throwable.getMessage());
                weathertempTv.setText("Not Valid");
                weathericonTv.setText("");
            }
        });

        locationTv.setText(weather.getCityname());


        this.weather = weather;


        layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                try {
                    weatherdata = new WeatherDataSource(itemView.getContext());
                    weatherdata.deleteWeather(weather);


                    Log.d("WeatherViewHolder",weather.getCityname() + " Deleted!");
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }


        });
    }


    private String getWeatherIcon(int actualId){

        String icon = "";
        if(actualId == 800)
            icon = itemView.getContext().getString(R.string.weather_sunny);
        else {
            int id = actualId / 100;
            Log.d("WeatherFrag", Integer.toString(id));

            switch (id) {
                case 2:
                    icon = itemView.getContext().getString(R.string.weather_thunder);
                    break;
                case 3:
                    icon = itemView.getContext().getString(R.string.weather_drizzle);
                    break;
                case 7:
                    icon = itemView.getContext().getString(R.string.weather_foggy);
                    break;
                case 8:
                    icon = itemView.getContext().getString(R.string.weather_cloudy);
                    break;
                case 6:
                    icon = itemView.getContext().getString(R.string.weather_snowy);
                    break;
                case 5:
                    icon = itemView.getContext().getString(R.string.weather_rainy);
                    break;
            }
        }
        return icon;
    }

    public static int safeLongToInt(long l) {
        if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
            throw new IllegalArgumentException
                    (l + " cannot be cast to int without changing its value.");
        }
        return (int) l;
    }

}
