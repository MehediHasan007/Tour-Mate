package com.example.tourmate.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tourmate.Adapter.WeatherAdapter;
import com.example.tourmate.R;
import com.example.tourmate.RetrofitClass;
import com.example.tourmate.Service;
import com.example.tourmate.WeatherPojo.WeatherPojoClass;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherActivity extends AppCompatActivity {

    private TextView windTV,temparatureTV,humidityTV;

    private RecyclerView recyclerViewId;
    private WeatherAdapter weatherAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        windTV = findViewById(R.id.windTV);
        temparatureTV = findViewById(R.id.temparatureTV);
        humidityTV = findViewById(R.id.humidityTV);

        recyclerViewId = findViewById(R.id.recyclerViewId);

        getWeatherDetails();
    }

    private void getWeatherDetails() {
        Service weatherService=  RetrofitClass.getRetrofitInstance().create(Service.class);

        String url = String.format("forecast?lat=23.7805733&lon=90.2792379&units=metric&appid=565f3c033002a3840c7bf4d2f5c210a7");

        Call<WeatherPojoClass> call = weatherService.getWeatherData(url);
        call.enqueue(new Callback<WeatherPojoClass>() {
            @Override
            public void onResponse(Call<WeatherPojoClass> call, Response<WeatherPojoClass> response) {
                if(response.code()==200){
                    WeatherPojoClass weatherPojoClass = response.body();
                    //Log.d("TAG",""+weatherPojoClass.getList().get(0).getWind().getSpeed());

                    String wind = String.valueOf(weatherPojoClass.getList().get(0).getWind().getSpeed());
                    String temparature = String.valueOf(weatherPojoClass.getList().get(0).getMain().getTemp());
                    String humidity =  String.valueOf(weatherPojoClass.getList().get(0).getMain().getHumidity());
                    String dateTime = String.valueOf(weatherPojoClass.getList().get(0).getDtTxt());
                    windTV.setText("Wind:"+wind+"Km/h");
                    temparatureTV.setText("Temp:"+temparature+"°C");
                    humidityTV.setText("Humidity:"+humidity+"%");

                    weatherAdapter = new WeatherAdapter(weatherPojoClass);
                    recyclerViewId.setLayoutManager(new LinearLayoutManager(WeatherActivity.this));
                    recyclerViewId.setAdapter(weatherAdapter);

                }
            }

            @Override
            public void onFailure(Call<WeatherPojoClass> call, Throwable t) {
                Toast.makeText(WeatherActivity.this,t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
