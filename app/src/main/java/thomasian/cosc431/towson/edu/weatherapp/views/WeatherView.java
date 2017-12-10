package thomasian.cosc431.towson.edu.weatherapp.views;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import thomasian.cosc431.towson.edu.weatherapp.IController;
import thomasian.cosc431.towson.edu.weatherapp.R;
import thomasian.cosc431.towson.edu.weatherapp.models.Weather;

public class WeatherView {


    Weather weather;
    TextView locationTv;
    TextView weathericonTv;
    TextView weathertempTv;
    private Weather currentWeather;
    private LinearLayout layout;

    public void hide() {
        layout.setVisibility(View.GONE);
    }

    public void show() {
        layout.setVisibility(View.VISIBLE);
    }

    private IController controller;

    public WeatherView(final IController controller, LinearLayout layout) {
        this.controller = controller;
        this.layout = layout;
        locationTv = (TextView)layout.findViewById(R.id.city_field);
        weathericonTv = (TextView)layout.findViewById(R.id.weather_icon);
        weathertempTv = (TextView)layout.findViewById(R.id.current_temperature_field);
    }

    // sets all the widgets of the song view based on the Song's values
    public void renderWeather(Weather weather) {
        locationTv.setText(weather.getCityname());
        currentWeather = weather;
    }
}