package thomasian.cosc431.towson.edu.weatherapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class WeatherDBHelper extends SQLiteOpenHelper {



    private static final String DATABASE_NAME = "weatherdatabase2.db";
    private static final int DB_VERSION = 4;

    private static final String CREATE_TABLE =
            "create table " + DatabaseContract.TABLE_NAME + " ( '" +
                    DatabaseContract.WEATHER_ID + "' text primary key, '" +
                    DatabaseContract.CITY_NAME + "' text); ";




    public WeatherDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
