package com.internship.asiancountries;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Country.class}, version = 1, exportSchema = false)
public abstract class AppDB extends RoomDatabase {
    public abstract CountryDao countryDao();
}
