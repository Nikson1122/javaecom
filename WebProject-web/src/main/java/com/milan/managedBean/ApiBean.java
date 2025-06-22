/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.


 */
package com.milan.managedBean;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;

/**
 *
 * @author dell
 */
@ManagedBean
@RequestScoped
public class ApiBean  {
   
    private String city = "Kathmandu";
    private String weatherResult;
     private String description;
    private String iconCode;
    private double temp;
    private int humidity;
    private double windSpeed;
    private double lat;
    private double lon;
    private String country;
    private String cityName;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIconCode() {
        return iconCode;
    }

    public void setIconCode(String iconCode) {
        this.iconCode = iconCode;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
    
 
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getWeatherResult() {
        return weatherResult;
    }

    public void setWeatherResult(String weatherResult) {
        this.weatherResult = weatherResult;
    }
    
    public void fetchWeather(){
        try{
           String apikey ="6daafd6ff37b79e8abb8b4b04307f00d";
            
             String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q="
                    + city + "&appid=" + apikey+ "&units=metric";
             
             URL url =new URL(apiUrl);
             HttpURLConnection conn = (HttpURLConnection) url.openConnection();
             conn.setRequestMethod("GET");
             
         BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream())
            );
        String inputLine;
        StringBuilder content = new StringBuilder();
        while((inputLine = in.readLine()) != null){
            content.append(inputLine);
        }
        in.close();
         conn.disconnect();
         
//         weatherResult = content.toString();


            JSONObject obj = new JSONObject(content.toString());

            this.cityName = obj.getString("name");
            this.country = obj.getJSONObject("sys").getString("country");

            JSONObject coord = obj.getJSONObject("coord");
            this.lat = coord.getDouble("lat");
            this.lon = coord.getDouble("lon");

            JSONObject main = obj.getJSONObject("main");
            this.temp = main.getDouble("temp");
            this.humidity = main.getInt("humidity");

            JSONObject wind = obj.getJSONObject("wind");
            this.windSpeed = wind.getDouble("speed");

            JSONArray weatherArray = obj.getJSONArray("weather");
            JSONObject weather = weatherArray.getJSONObject(0);
            this.description = weather.getString("description");
            this.iconCode = weather.getString("icon");


        
             
             

        }
        catch(Exception e){
            weatherResult = "Error fetching data:" + e.getMessage();
        }
    }
    
    
}

