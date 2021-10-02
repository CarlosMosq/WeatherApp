package com.company.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Pick_City extends AppCompatActivity {
    ImageView weatherIcon2;
    EditText searchBar;
    Button searchButton;
    TextView cityName2;
    TextView temperature2;
    TextView weatherSummary2;
    TextView humidityData2;
    TextView maxTempData2;
    TextView minTempData2;
    TextView pressureData2;
    TextView windSpeedData2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_city);

        weatherIcon2 = findViewById(R.id.weatherIcon2);
        searchBar = findViewById(R.id.searchBar);
        searchButton = findViewById(R.id.buttonSearch);
        cityName2 = findViewById(R.id.cityName2);
        temperature2 = findViewById(R.id.temperature2);
        weatherSummary2 = findViewById(R.id.weatherSummary2);
        humidityData2 = findViewById(R.id.humidityData2);
        maxTempData2 = findViewById(R.id.maxTempData2);
        minTempData2 = findViewById(R.id.minTempData2);
        pressureData2 = findViewById(R.id.pressureData2);
        windSpeedData2 = findViewById(R.id.windSpeedData2);

        searchButton.setOnClickListener(v -> {
            String cityName = searchBar.getText().toString();
            getWeatherData(cityName);
            searchBar.setText("");
        });


    }

    public void getWeatherData(String cityName) {
        WeatherAPI weatherAPI = RetrofitWeather.getRetrofit().create(WeatherAPI.class);
        Call<WeatherMap> call = weatherAPI.getWeatherMapWithCityName(cityName);

        call.enqueue(new Callback<WeatherMap>() {
            @Override
            public void onResponse(Call<WeatherMap> call, Response<WeatherMap> response) {
                if(response.isSuccessful()) {
                    cityName2.setText(response.body().getName() + " , " + response.body().getSys().getCountry());
                    temperature2.setText(response.body().getMain().getTemp() + "ºC");
                    weatherSummary2.setText(response.body().getWeather().get(0).getDescription());
                    humidityData2.setText(" : " + response.body().getMain().getHumidity() + "%");
                    maxTempData2.setText(" : " + response.body().getMain().getTempMax() + "ºC");
                    minTempData2.setText(" : " + response.body().getMain().getTempMin() + "ºC");
                    pressureData2.setText(" : " + response.body().getMain().getPressure());
                    windSpeedData2.setText(" : " + response.body().getWind().getSpeed());

                    String iconCode = response.body().getWeather().get(0).getIcon();
                    Picasso.get()
                            .load(String.format("https://openweathermap.org/img/wn/%s@2x.png", iconCode))
                            .placeholder(R.drawable.ic_launcher_background)
                            .into(weatherIcon2);
                }
                else {
                    Toast.makeText(Pick_City.this, "Please Enter Valid City Name", Toast.LENGTH_SHORT).show();
                }  
            }

            @Override
            public void onFailure(Call<WeatherMap> call, Throwable t) {

            }
        });
    }
}