
package com.example.tourmate.WeatherPojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WeatherPojoClass {

    @SerializedName("cod")
    @Expose
    private String cod;
    @SerializedName("message")
    @Expose
    private Double message;
    @SerializedName("cnt")
    @Expose
    private String cnt;//Integer
    @SerializedName("list")
    @Expose
    private java.util.List<com.example.tourmate.WeatherPojo.List> list = null;
    @SerializedName("city")
    @Expose
    private City city;

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public Double getMessage() {
        return message;
    }

    public void setMessage(Double message) {
        this.message = message;
    }

    public String getCnt() {
        return cnt;
    }

    public void setCnt(String cnt) {
        this.cnt = cnt;
    }

    public java.util.List<com.example.tourmate.WeatherPojo.List> getList() {
        return list;
    }

    public void setList(java.util.List<com.example.tourmate.WeatherPojo.List> list) {
        this.list = list;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

}
