package thomasian.cosc431.towson.edu.weatherapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import thomasian.cosc431.towson.edu.weatherapp.IPresenter;
import thomasian.cosc431.towson.edu.weatherapp.R;
import thomasian.cosc431.towson.edu.weatherapp.models.Weather;
/**
 * Created by hthoma on 12/9/17.
 */

public class WeatherViewHolder extends RecyclerView.ViewHolder {
    Weather weather;
    TextView locationTv;
    TextView weathericonTv;
    TextView weathertempTv;
    IPresenter controller;
    public WeatherViewHolder(View itemView, final IPresenter controller){
        super(itemView);
        this.controller = controller;
        locationTv = (TextView)itemView.findViewById(R.id.city_field);
        weathericonTv = (TextView)itemView.findViewById(R.id.weather_icon);
        weathertempTv = (TextView)itemView.findViewById(R.id.current_temperature_field);


    }

    public void bindWeather(Weather weather) {
        locationTv.setText(weather.getCityname());
        weathericonTv.setText(weather.getWeathericon());
        weathertempTv.setText(weather.getTemp());

        this.weather = weather;
    }


}
