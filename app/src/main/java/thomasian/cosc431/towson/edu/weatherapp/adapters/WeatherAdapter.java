package thomasian.cosc431.towson.edu.weatherapp.adapters;

import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import thomasian.cosc431.towson.edu.weatherapp.IController;
import thomasian.cosc431.towson.edu.weatherapp.R;
import thomasian.cosc431.towson.edu.weatherapp.models.Weather;
/**
 * Created by hthoma on 12/9/17.
 */

public class WeatherAdapter extends RecyclerView.Adapter<WeatherViewHolder> {
    List<Weather> weathers;
    IController controller;

    public WeatherAdapter(List<Weather> weathers, IController controller) {
        this.weathers = weathers;
        this.controller = controller;
    }


    @Override
    public WeatherViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.adtl_weather_frag, parent, false);
        WeatherViewHolder vh = new WeatherViewHolder(view, controller);
        return vh;
    }


    @Override
    public void onBindViewHolder(WeatherViewHolder holder, int position) {
        Weather weather = weathers.get(position);
        holder.bindWeather(weather);
        Handler handler = new Handler();

    }

    @Override
    public int getItemCount() {
        if(!(weathers == null))
       return weathers.size();
        else
            return 0;
    }



}
