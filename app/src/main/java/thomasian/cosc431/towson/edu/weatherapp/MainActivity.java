package thomasian.cosc431.towson.edu.weatherapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.ArrayList;

import thomasian.cosc431.towson.edu.weatherapp.adapters.WeatherAdapter;
import thomasian.cosc431.towson.edu.weatherapp.database.WeatherDataSource;
import thomasian.cosc431.towson.edu.weatherapp.fragments.WeatherFragment;
import thomasian.cosc431.towson.edu.weatherapp.models.Weather;
import thomasian.cosc431.towson.edu.weatherapp.prefrences.CityPref;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, IController {
    private static final int ADD_SONG_REQUEST_CODE = 42;
    private static final String TAG = MainActivity.class.getSimpleName();



    ImageButton textButton, textButton2, textButton3,textButton4, textButton7;
    WeatherDataSource weatherdata;
    RecyclerView recyclerView;
    public WeatherAdapter adapter;

    ArrayList<Weather> weathers= new ArrayList<Weather>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        weatherdata = new WeatherDataSource(getBaseContext());
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new WeatherFragment())
                    .commit();
        }

        weathers = weatherdata.getAllWeather();
        recyclerView = (RecyclerView)findViewById(R.id.weatherlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new WeatherAdapter(weathers, (IController)this,getApplicationContext());
        recyclerView.setAdapter(adapter);





        bindView();
    }
    private void addLocs(){


    }
    private void bindView() {
        textButton = (ImageButton) findViewById(R.id.imageButton);

        textButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WeatherFragment wf = (WeatherFragment)getSupportFragmentManager()
                        .findFragmentById(R.id.container);

                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setData(Uri.parse("sms:"));
                sendIntent.putExtra("sms_body", wf.TextFriend());
                startActivity(sendIntent);

            }
        });


        textButton3 = (ImageButton) findViewById(R.id.imageButton3);

        textButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WeatherFragment wf = (WeatherFragment)getSupportFragmentManager()
                        .findFragmentById(R.id.container);
                Intent webIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse("https://m.openweathermap.org/find?q=" + wf.getCity()));
                startActivity(webIntent);

            }
        });

        textButton7 = (ImageButton) findViewById(R.id.imageButton7);
        textButton7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Add city");
                final EditText input = new EditText(MainActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);
                Log.d("MainActivity","FUCK");
                builder.setPositiveButton("Go", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        weathers.add(new Weather(input.getText().toString()));
                        weatherdata.addWeather(new Weather(input.getText().toString()));
                        adapter.notifyDataSetChanged();
                    }
                });
                builder.show();

            }
        });


        textButton4 = (ImageButton) findViewById(R.id.imageButton4);

        textButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WeatherFragment wf = (WeatherFragment)getSupportFragmentManager()
                        .findFragmentById(R.id.container);
              wf.changeUnit();
                wf.refreshData();

            }
        });


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.weather, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.change_city){
            showInputDialog();
        }
        return false;
    }

    private void showInputDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Change city");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("Go", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                changeCity(input.getText().toString());
            }
        });
        builder.show();
    }

    public void changeCity(String city){
        WeatherFragment wf = (WeatherFragment)getSupportFragmentManager()
                .findFragmentById(R.id.container);
        wf.changeCity(city);
        new CityPref(this).setCity(city);
    }

    public void changeUnit(){
        WeatherFragment wf = (WeatherFragment)getSupportFragmentManager()
                .findFragmentById(R.id.container);

        wf.changeUnit();
        new CityPref(this).setUnits();
    }


    @Override
    public void onClick(View view) {

    }

    @Override
    public void deleteWeather(Weather weather) {

    }

    @Override
    public void launchAddWeather() {

    }

    @Override
    public void handleNewWeatherResult(Intent data) {

    }

    @Override
    public void logWeather() {
        for(Weather weather : weathers) {
            Log.d("TAG", weather.toString());
        }
    }
}
