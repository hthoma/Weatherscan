package thomasian.cosc431.towson.edu.weatherapp;

import java.util.List;

import thomasian.cosc431.towson.edu.weatherapp.models.Weather;

/**
 * Created by randy on 10/9/17.
 */

public class MainPresenter implements IPresenter {

    private final IView view;
    private final IModel model;

    public MainPresenter(IView view, IModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void handleNewWeatherResult(Weather weather) {
        model.addWeather(weather);
        view.refresh();
    }



    @Override
    public List<Weather> getWeatherFromModel() {
        return model.getWeather();
    }



    @Override
    public void logWeather() {
        for(Weather reminder : model.getWeather()) {

        }
    }

    @Override
    public void deleteWeather(Weather reminder) {
        model.removeWeather(reminder);
        view.refresh();
    }

    @Override
    public void launchAddWeatherActivity() {
        view.launchNewWeather();
    }
}
