package thomasian.cosc431.towson.edu.weatherapp;

/**
 * Created by hthoma on 11/20/17.
 */

import android.os.AsyncTask;

import org.json.JSONObject;

public class FetchRvWeather extends AsyncTask<String,Integer,JSONObject> {

    private static String OPEN_WEATHER_MAP_API =
            "http://api.openweathermap.org/data/2.5/weather?q=%s&units=imperial";

    @Override
    protected JSONObject doInBackground(String... strings) {
        return null;
    }




/*then you use


    @Override
    protected JSONObject doInBackground(String... strings) {
            try {
                URL url;
                if(metric)
                    url = new URL(String.format("http://api.openweathermap.org/data/2.5/weather?q=%s&units=metric", strings));
                else
                    url = new URL(String.format("http://api.openweathermap.org/data/2.5/weather?q=%s&units=imperial", strings));

                HttpURLConnection connection =
                        (HttpURLConnection)url.openConnection();

                connection.addRequestProperty("x-api-key",
                        "4395b9c2cf80a89f7347816081680ecb");

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));

                StringBuffer json = new StringBuffer(1024);
                String tmp="";
                while((tmp=reader.readLine())!=null)
                    json.append(tmp).append("\n");
                reader.close();

                JSONObject data = new JSONObject(json.toString());

                if(data.getInt("cod") != 200){
                    return null;
                }

                return data;
            }catch(Exception e){
                return null;
            }
        }
    }
    */
}
