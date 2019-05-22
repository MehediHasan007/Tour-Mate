package com.example.tourmate.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tourmate.R;
import com.example.tourmate.WeatherPojo.List;
import com.example.tourmate.WeatherPojo.WeatherPojoClass;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {
    private WeatherPojoClass weatherPojoClass;

    public WeatherAdapter(WeatherPojoClass weatherPojoClass) {
        this.weatherPojoClass = weatherPojoClass;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.weather_recycler_item,viewGroup,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        List currentWeather = weatherPojoClass.getList().get(i);

        viewHolder.dateTimeTV.setText(String.valueOf(weatherPojoClass.getList().get(i).getDtTxt()));
        viewHolder.temparatureTV.setText(String.valueOf("Temp:"+weatherPojoClass.getList().get(i).getMain().getTemp()+"°C"));
        viewHolder.skyNatureTV.setText(String.valueOf(weatherPojoClass.getList().get(i).getWeather().get(0).getDescription()));
        viewHolder.windTV.setText(String.valueOf("Wind:"+weatherPojoClass.getList().get(i).getWind().getSpeed()+"Km/h"));
        viewHolder.humidityTV.setText(String.valueOf("Humidity:"+weatherPojoClass.getList().get(i).getMain().getHumidity()+"%"));


    }

    @Override
    public int getItemCount() {
        return weatherPojoClass.getList().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView dateTimeTV,temparatureTV,windTV,humidityTV,skyNatureTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTimeTV = itemView.findViewById(R.id.dateTimeTV);
            temparatureTV = itemView.findViewById(R.id.temparatureTV);
            windTV = itemView.findViewById(R.id.windTV);
            humidityTV = itemView.findViewById(R.id.humidityTV);
            skyNatureTV = itemView.findViewById(R.id.skyNatureTV);
        }

    }
}
