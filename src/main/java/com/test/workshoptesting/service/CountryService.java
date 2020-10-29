package com.test.workshoptesting.service;

import com.test.workshoptesting.model.Continent;
import com.test.workshoptesting.model.Country;
import com.test.workshoptesting.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CountryService {
    private final CountryRepository countryRepository;
    private String somePrivateField;

    public List<Country> getAllCountries() {
        return countryRepository.readAllCountries().stream()
                .map(this::classifyCountry)
                .collect(Collectors.toList());
    }

    public Country readByName(String countryName){
        return countryRepository.readByName(countryName);
    }

    private Country classifyCountry(Country country) {
        country.classifyCountry();
        return country;
    }
}
