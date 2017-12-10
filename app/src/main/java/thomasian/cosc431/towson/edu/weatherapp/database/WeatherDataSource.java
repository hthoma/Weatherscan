package thomasian.cosc431.towson.edu.weatherapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import thomasian.cosc431.towson.edu.weatherapp.models.Weather;


public class WeatherDataSource {

    private static WeatherDataSource instance;
    private WeatherDBHelper dbHelper;

    public WeatherDataSource(Context ctx) {
        dbHelper = new WeatherDBHelper(ctx);
    }




    public static WeatherDataSource getInstance(Context ctx) {

        if(instance == null) {
            instance = new WeatherDataSource(ctx);
        }
        return instance;

    }

    public ArrayList<Weather> getAllWeather() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + DatabaseContract.TABLE_NAME, null);
        ArrayList<Weather> weathers = new ArrayList<>();
        while(cursor.moveToNext()) {
            Weather weather = new Weather();
            String id = cursor.getString(cursor.getColumnIndex(DatabaseContract.WEATHER_ID));
            String name = cursor.getString(cursor.getColumnIndex(DatabaseContract.CITY_NAME));

            weather.setCityname(name);
            weather.setID(id);


            weathers.add(weather);
        }
        return weathers;
    }

    public void addWeather(Weather weather) {
        ContentValues cv = weatherToContentValues(weather);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.insert("'"+DatabaseContract.TABLE_NAME+ "'", null,cv);
    }

    public void updateWeather(Weather weather) {
        ContentValues cv = weatherToContentValues(weather);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.update(DatabaseContract.TABLE_NAME,cv,DatabaseContract.WEATHER_ID + "=" + weather.getID(),null);
    }



    public void deleteReminder(Weather weather) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        db.delete(DatabaseContract.TABLE_NAME, "'"+ DatabaseContract.WEATHER_ID + "' = '"  + weather.getID() + "'",null);
        }




    private ContentValues weatherToContentValues(Weather weather) {
        ContentValues cv = new ContentValues();
        cv.put("'"+DatabaseContract.WEATHER_ID+"'", weather.getID());
        cv.put("'"+DatabaseContract.CITY_NAME+"'", weather.getCityname());
        return cv;
    }

}
