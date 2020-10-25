package com.test.workshoptesting.repository;

import com.test.workshoptesting.model.Continent;
import com.test.workshoptesting.model.Country;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CountryInMemoryRepository implements CountryRepository {
    private final List<Country> IN_MEMORY_COUNTRIES = new ArrayList<>(5);

    @PostConstruct
    public void init() {
        var poland = Country.builder()
                .name("Poland")
                .continent(Continent.EUROPE)
                .population(37_000_000).build();
        var venezuela = Country.builder()
                .name("Venezuela")
                .continent(Continent.AMERICA)
                .population(28_000_000).build();

        var spain = Country.builder()
                .name("Spain")
                .continent(Continent.EUROPE)
                .population(46_000_000).build();

        IN_MEMORY_COUNTRIES.add(poland);
        IN_MEMORY_COUNTRIES.add(venezuela);
        IN_MEMORY_COUNTRIES.add(spain);
    }

    public List<Country> readAllCountries() {
        return IN_MEMORY_COUNTRIES;
    }

    @Override
    public Country readByName(String countryName) {
        return IN_MEMORY_COUNTRIES.stream()
                .filter(c -> c.getName().equalsIgnoreCase(countryName))
                .findFirst()
                .orElseThrow();
    }
}
