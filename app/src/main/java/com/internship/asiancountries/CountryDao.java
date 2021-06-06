package com.internship.asiancountries;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CountryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public void addCountry(Country country);

    @Query("select * from Country where name = (:countryName)")
    public Country getCountry(String countryName);

    @Query("select * from Country")
    public List<Country> getCountries();

    @Query("select name from Country where alpha3Code = (:alpha3)")
    public String getCountryByAlpha3(String alpha3);

    @Query("delete from Country")
    public void clearData();
}
