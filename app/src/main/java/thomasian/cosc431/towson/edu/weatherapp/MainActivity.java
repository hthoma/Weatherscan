package thomasian.cosc431.towson.edu.weatherapp;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import thomasian.cosc431.towson.edu.weatherapp.adapters.WeatherAdapter;
import thomasian.cosc431.towson.edu.weatherapp.database.WeatherDataSource;
import thomasian.cosc431.towson.edu.weatherapp.fragments.WeatherFragment;
import thomasian.cosc431.towson.edu.weatherapp.models.Weather;
import thomasian.cosc431.towson.edu.weatherapp.prefrences.CityPref;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, IController {
    private static final int ADD_SONG_REQUEST_CODE = 42;
    private static final String TAG = MainActivity.class.getSimpleName();


    ImageButton textButton, textButton2, textButton3, textButton4, textButton7, mylocationbutton;
    WeatherDataSource weatherdata;
    RecyclerView recyclerView;
    public WeatherAdapter adapter;
    WeatherFragment frag = new WeatherFragment();

    ArrayList<Weather> weathers = new ArrayList<Weather>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        weatherdata = new WeatherDataSource(getBaseContext());
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, frag)
                    .commit();
        }

        weathers = weatherdata.getAllWeather();
        recyclerView = (RecyclerView) findViewById(R.id.weatherlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new WeatherAdapter(weathers, this, getApplicationContext(), frag);
        recyclerView.setAdapter(adapter);


        bindView();
    }

    private void addLocs() {


    }

    private void bindView() {
        textButton = (ImageButton) findViewById(R.id.imageButton);

        textButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WeatherFragment wf = (WeatherFragment) getSupportFragmentManager()
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
                WeatherFragment wf = (WeatherFragment) getSupportFragmentManager()
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
                final CheckBox cb = new CheckBox(MainActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);
                Log.d("MainActivity", "FUCK");
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

        mylocationbutton = (ImageButton) findViewById(R.id.myloc);
        mylocationbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Log.d("MainActivity","Permissions test failed");
                }
                else{
                    LocationManager lm = (LocationManager) getSystemService(MainActivity.this.LOCATION_SERVICE);
                    Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    Log.d("MainActivity",location.getLongitude() + " " + location.getLatitude() );
                    double longitude = location.getLongitude();
                    double latitude = location.getLatitude();
                    Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                    List<Address> addresses = null;
                    try {
                        addresses = geocoder.getFromLocation(longitude, latitude, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String cityName = addresses.get(0).getAddressLine(0);
                        weathers.add(new Weather(cityName));
                        weatherdata.addWeather(new Weather(cityName));
                        adapter.notifyDataSetChanged();
                    }

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

