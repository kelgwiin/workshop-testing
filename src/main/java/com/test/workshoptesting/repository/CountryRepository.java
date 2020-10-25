package com.test.workshoptesting.repository;

import com.test.workshoptesting.model.Country;

import java.util.List;

public interface CountryRepository {
    List<Country> readAllCountries();
    Country readByName(final String countryName);
}
