package thomasian.cosc431.towson.edu.weatherapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import thomasian.cosc431.towson.edu.weatherapp.IController;
import thomasian.cosc431.towson.edu.weatherapp.R;
import thomasian.cosc431.towson.edu.weatherapp.database.WeatherDataSource;
import thomasian.cosc431.towson.edu.weatherapp.fragments.WeatherFragment;
import thomasian.cosc431.towson.edu.weatherapp.models.Weather;
/**
 * Created by hthoma on 12/9/17.
 */

public class WeatherAdapter extends RecyclerView.Adapter<WeatherViewHolder> {
    List<Weather> weathers;
    IController controller;
    Context context;
    WeatherDataSource weatherdata;
    WeatherFragment frag;
    public WeatherAdapter(List<Weather> weathers, IController controller, Context context,WeatherFragment frag) {
        this.weathers = weathers;
        this.controller = controller;
        this.context = context;
        this.frag = frag;
    }


    @Override
    public WeatherViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.adtl_weather_frag, parent, false);
        WeatherViewHolder vh = new WeatherViewHolder(view, controller);


        return vh;
    }


    @Override
    public void onBindViewHolder(WeatherViewHolder holder, final int position) {
        final Weather weather = weathers.get(position);
        holder.bindWeather(weather);
        holder.layout.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View view) {

                weatherdata = new WeatherDataSource(context);
                weatherdata.deleteWeather(weather);

                weathers.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, weathers.size());
                return true;

            }
        });
        holder.layout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                frag.changeCity(weather.getCityname());
                frag.refreshData();
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

    public void delete(int position) {
        weathers.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, weathers.size());
    }

}
