package com.internship.asiancountries;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;


import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGImageView;

import org.json.JSONArray;
import org.json.JSONException;

import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity2 extends AppCompatActivity {

    Intent it;
    private TextView name, capital, region, subRegion, population, borders, languages;
    private SVGImageView flag;
    SVG svg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        name = findViewById(R.id.name);
        capital = findViewById(R.id.capital);
        region = findViewById(R.id.region);
        subRegion = findViewById(R.id.subregion);
        population = findViewById(R.id.population);
        borders = findViewById(R.id.borders);
        languages = findViewById(R.id.languages);
        flag = findViewById(R.id.flag);

        it = getIntent();
        String countryName = it.getStringExtra("countryName");

        new Thread(new Runnable() {
            @Override
            public void run() {
                Country country = MainActivity.rdb.countryDao().getCountry(countryName);

                try {
                    URL url = new URL(country.getFlag());
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    svg = SVG.getFromInputStream(conn.getInputStream());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                StringBuilder bor = new StringBuilder();
                StringBuilder lan = new StringBuilder();
                try {
                    JSONArray ja = new JSONArray(country.getBorders());
                    for(int i=0; i<ja.length();i++){
                        bor.append(MainActivity.rdb.countryDao().getCountryByAlpha3(ja.getString(i))).append(", ");
                    }

                    JSONArray jb = new JSONArray(country.getLanguages());
                    for(int i=0;i<jb.length();i++){
                        lan.append(jb.getJSONObject(i).get("name")).append("/").append(jb.getJSONObject(i).get("nativeName")).append(", ");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        name.setText(country.getName());
                        capital.setText(country.getCapital());
                        region.setText(country.getRegion());
                        subRegion.setText(country.getSubRegion());
                        population.setText(country.getPopulation());
                        borders.setText(bor);
                        languages.setText(lan);
                        try{
                            flag.setSVG(svg);
                        } catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                });
            }
        }).start();
    }
}