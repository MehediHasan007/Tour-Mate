
package com.example.tourmate.WeatherPojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Clouds {

    @SerializedName("all")
    @Expose
    private String all;//Integer

    public String getAll() {
        return all;
    }

    public void setAll(String all) {
        this.all = all;
    }

}
