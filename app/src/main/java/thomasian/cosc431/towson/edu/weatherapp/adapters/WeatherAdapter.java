package thomasian.cosc431.towson.edu.weatherapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import thomasian.cosc431.towson.edu.weatherapp.IController;
import thomasian.cosc431.towson.edu.weatherapp.MainActivity;
import thomasian.cosc431.towson.edu.weatherapp.R;
import thomasian.cosc431.towson.edu.weatherapp.database.WeatherDataSource;
import thomasian.cosc431.towson.edu.weatherapp.fragments.WeatherFragment;
import thomasian.cosc431.towson.edu.weatherapp.models.Weather;
import thomasian.cosc431.towson.edu.weatherapp.prefrences.CityPref;

/**
 * Created by hthoma on 12/9/17.
 */

public class WeatherAdapter extends RecyclerView.Adapter<WeatherViewHolder> {
    List<Weather> weathers;
    IController controller;
    Context context;
    WeatherDataSource weatherdata;
    WeatherFragment frag;
    Activity theactivity;
    Boolean ismetric;
    public WeatherAdapter(List<Weather> weathers, IController controller, Context context, WeatherFragment frag, Activity theactivity,Boolean ismetric) {
        this.weathers = weathers;
        this.controller = controller;
        this.context = context;
        this.frag = frag;
        this.theactivity = theactivity;
        this.ismetric = ismetric;
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
        final Weather weather = weathers.get(position);
       final int elementloc = position;
        holder.bindWeather(weather, new CityPref(theactivity).getUnits());
        holder.layout.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View view) {

                weatherdata = new WeatherDataSource(context);
                weatherdata.deleteWeather(weather);

                weathers.remove(elementloc);
                notifyItemRemoved(elementloc);

                notifyItemRangeChanged(elementloc, weathers.size());

                return true;

            }
        });
        holder.layout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.d("WeatherAdapter", weather.getCityname());
                if (!weather.getTemp().equals("Cannot Find City")) {
                    new CityPref(theactivity).setCity(weathers.get(elementloc).getCityname());
                    ((MainActivity) theactivity).changeCity(weather.getCityname());
                }
                else
                    Toast.makeText(view.getContext(),"That is not a valid city",Toast.LENGTH_SHORT).show();



            }
        });


    }

    @Override
    public int getItemCount() {
        if(!(weathers == null))
       return weathers.size();
        else
            return 0;
    }



}
