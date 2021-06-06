package com.internship.asiancountries;

import android.content.Context;

import androidx.room.RoomDatabase;

import org.json.*;

import java.net.*;
import java.io.*;
import java.sql.SQLException;

public class HttpData extends Thread {



    Context context;
    public static StringBuilder resp = new StringBuilder();

    public HttpData(Context context){
        this.context = context;
    }
    
    public void run () {
        try {
            URL url = new URL("https://restcountries.eu/rest/v2/region/asia");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader bfr = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine = "";

            while ((inputLine = bfr.readLine()) != null) {
                resp.append(inputLine);
            }

            bfr.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        JSONArray ja;
        Country country = new Country();

        try {
            ja = new JSONArray(resp.toString());
            for(int i=0; i<ja.length();i++){
                country.setName(ja.getJSONObject(i).getString("name"));
                country.setCapital(ja.getJSONObject(i).getString("capital"));
                country.setFlag(ja.getJSONObject(i).getString("flag"));
                country.setRegion(ja.getJSONObject(i).getString("region"));
                country.setSubRegion(ja.getJSONObject(i).getString("subregion"));
                country.setPopulation(ja.getJSONObject(i).getString("population"));
                country.setBorders(ja.getJSONObject(i).getString("borders"));
                country.setLanguages(ja.getJSONObject(i).getString("languages"));
                country.setAlpha3Code(ja.getJSONObject(i).getString("alpha3Code"));
                MainActivity.rdb.countryDao().addCountry(country);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}