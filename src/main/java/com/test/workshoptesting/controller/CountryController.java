package com.test.workshoptesting.controller;

import com.test.workshoptesting.model.Country;
import com.test.workshoptesting.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/countries")
@RequiredArgsConstructor
public class CountryController {
    private final CountryService countryService;

    @GetMapping
    public List<Country> retrieveAllCountries(){
        return countryService.getAllCountries();
    }
}

