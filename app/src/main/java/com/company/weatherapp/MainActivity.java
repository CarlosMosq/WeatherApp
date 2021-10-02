package com.company.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    ImageView weatherIcon;
    TextView cityName;
    TextView temperature;
    TextView weatherSummary;
    TextView humidityData;
    TextView maxTempData;
    TextView minTempData;
    TextView pressureData;
    TextView windSpeedData;
    FloatingActionButton addCityButton;

    LocationManager locationManager;
    LocationListener locationListener;
    double lat, lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weatherIcon = findViewById(R.id.weatherIcon);
        cityName = findViewById(R.id.cityName);
        temperature = findViewById(R.id.temperature);
        weatherSummary = findViewById(R.id.weatherSummary);
        humidityData = findViewById(R.id.humidityData);
        maxTempData = findViewById(R.id.maxTempData);
        minTempData = findViewById(R.id.minTempData);
        pressureData = findViewById(R.id.pressureData);
        windSpeedData = findViewById(R.id.windSpeedData);
        addCityButton = findViewById(R.id.addCityButton);


        addCityButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Pick_City.class);
            startActivity(intent);
        });

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                lat = location.getLatitude();
                lon = location.getLongitude();

                Log.e("lat: ", String.valueOf(lat));
                Log.e("lon: ", String.valueOf(lon));

                getWeatherData(lat, lon);
            }
        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 50, locationListener);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 1 && permissions.length > 0 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 50, locationListener);
        }
    }

    public void getWeatherData(double lat, double lon) {
        WeatherAPI weatherAPI = RetrofitWeather.getRetrofit().create(WeatherAPI.class);
        Call<WeatherMap> call = weatherAPI.getWeatherMapWithLocation(lat, lon);

        call.enqueue(new Callback<WeatherMap>() {
            @Override
            public void onResponse(Call<WeatherMap> call, Response<WeatherMap> response) {
                cityName.setText(response.body().getName() + " , " + response.body().getSys().getCountry());
                temperature.setText(response.body().getMain().getTemp() + "ºC");
                weatherSummary.setText(response.body().getWeather().get(0).getDescription());
                humidityData.setText(" : " + response.body().getMain().getHumidity() + "%");
                maxTempData.setText(" : " + response.body().getMain().getTempMax() + "ºC");
                minTempData.setText(" : " + response.body().getMain().getTempMin() + "ºC");
                pressureData.setText(" : " + response.body().getMain().getPressure());
                windSpeedData.setText(" : " + response.body().getWind().getSpeed());

                String iconCode = response.body().getWeather().get(0).getIcon();
                Picasso.get().load(String.format("https://openweathermap.org/img/wn/%s@2x.png", iconCode)).placeholder(R.drawable.ic_launcher_background).into(weatherIcon);

            }

            @Override
            public void onFailure(Call<WeatherMap> call, Throwable t) {

            }
        });
    }


}