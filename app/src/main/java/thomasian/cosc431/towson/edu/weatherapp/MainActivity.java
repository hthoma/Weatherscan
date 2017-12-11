package thomasian.cosc431.towson.edu.weatherapp;

import android.Manifest;
import android.content.Context;
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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import thomasian.cosc431.towson.edu.weatherapp.adapters.WeatherAdapter;
import thomasian.cosc431.towson.edu.weatherapp.database.WeatherDataSource;
import thomasian.cosc431.towson.edu.weatherapp.fragments.WeatherFragment;
import thomasian.cosc431.towson.edu.weatherapp.models.Weather;
import thomasian.cosc431.towson.edu.weatherapp.prefrences.CityPref;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, IController, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 949;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
    ImageButton textButton, textButton2, textButton3, textButton4, textButton7, mylocationbutton;
    WeatherDataSource weatherdata;
    RecyclerView recyclerView;
    public WeatherAdapter adapter;
    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private LocationManager locationManager;
    private LocationRequest mLocationRequest;
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
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSIONS_REQUEST_LOCATION);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

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
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSIONS_REQUEST_LOCATION);
                }
                else {
                    mLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                    Log.d("MainActivity", mLocation.getLatitude() + " " +mLocation.getLongitude() );
                    if (Double.isNaN(mLocation.getLatitude())  || Double.isNaN(mLocation.getLongitude()) ) {
                        Log.d("MainActivity", "Cant find location");
                    } else {

                        String cityName;
                        Geocoder gcd = new Geocoder(MainActivity.this, Locale.getDefault());
                        List<Address> addresses = null;
                        try {
                            addresses = gcd.getFromLocation(mLocation.getLatitude(), mLocation.getLongitude(), 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (addresses.size() > 0) {
                           cityName = addresses.get(0).getLocality();
                        }
                        else {
                            cityName = "Could not find City";
                        }
                        weathers.add(new Weather(cityName));
                        weatherdata.addWeather(new Weather(cityName));
                        adapter.notifyDataSetChanged();
                    }
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
    public String getLocationName(double lattitude, double longitude) {

        String cityName = "Not Found";
        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
        try {

            List<Address> addresses = gcd.getFromLocation(lattitude, longitude,
                    10);

            for (Address adrs : addresses) {
                if (adrs != null) {

                    String city = adrs.getLocality();
                    if (city != null && !city.equals("")) {
                        cityName = city;
                        System.out.println("city ::  " + cityName);
                    } else {

                    }
                    // // you should also try with addresses.get(0).toSring();

                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cityName;

    }


    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        } startLocationUpdates();
        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if(mLocation == null){
            startLocationUpdates();
        }
        if (mLocation != null) {
            double latitude = mLocation.getLatitude();
            double longitude = mLocation.getLongitude();
        } else {
            // Toast.makeText(this, "Location not Detected", Toast.LENGTH_SHORT).show();
        }
    }

    protected void startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(MIN_TIME_BW_UPDATES)
                .setFastestInterval(MIN_TIME_BW_UPDATES);
        // Request location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
        Log.d("reque", "--->>>>");
    }

    @Override
    public void onConnectionSuspended(int i) {

        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }
    @Override
    public void onLocationChanged(Location location) {

    }

}

