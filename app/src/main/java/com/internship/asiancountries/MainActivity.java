package com.internship.asiancountries;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView lv;
    private ProgressBar pbar;
    private AppCompatButton refresh, clear;
    private ArrayAdapter<String> ad;
    private ArrayList<String> al;
    public static List<Country> countries;
    public static AppDB rdb;
    Intent it;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rdb = Room.databaseBuilder(getApplicationContext(), AppDB.class, "Country").build();

        lv = findViewById(R.id.listView);
        refresh = findViewById(R.id.refresh);
        clear = findViewById(R.id.clear);
        pbar = findViewById(R.id.pbar);
        pbar.setIndeterminate(true);


        al = new ArrayList<String>();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                it = new Intent(MainActivity.this, MainActivity2.class);
                it.putExtra("countryName", lv.getItemAtPosition(i).toString());
                startActivity(it);
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                countries = rdb.countryDao().getCountries();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        al.clear();
                        for(Country country: countries) {
                            al.add(country.getName());
                        }
                        ad = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_content, al);
                        lv.setAdapter(ad);
                    }
                });
            }
        }).start();

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(internetIsConnected()){
                            HttpData fetch = new HttpData(MainActivity.this);
                            fetch.start();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    pbar.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                        else{
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(),"No Internet Connection!!",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        countries = rdb.countryDao().getCountries();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                al.clear();
                                pbar.setVisibility(View.GONE);
                                for(Country country: countries) {
                                    al.add(country.getName());
                                }
                                ad = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_content, al);
                                lv.setAdapter(ad);
                            }
                        });
                    }
                }).start();
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        rdb.countryDao().clearData();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ad.clear();
                            }
                        });
                    }
                }).start();
            }
        });
    }

    private boolean internetIsConnected() {
        try {
            String command = "ping -c 1 google.com";
            return (Runtime.getRuntime().exec(command).waitFor() == 0);
        } catch (Exception e) {
            return false;
        }
    }
}