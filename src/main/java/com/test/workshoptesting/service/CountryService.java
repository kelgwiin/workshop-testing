package com.test.workshoptesting.service;

import com.test.workshoptesting.exception.MyException;
import com.test.workshoptesting.model.Country;
import com.test.workshoptesting.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.SPACE;

@Service
@RequiredArgsConstructor
public class CountryService {
    private final CountryRepository countryRepository;
    private final SyncDataService syncDataService;
    private final SchoolService schoolService;

    private String somePrivateField;

    @Value("${spring.application.name}")
    private String appName;

    @Value("${app.suffix:}")
    private String suffixServiceName;

    @PostConstruct
    private void init() {
        somePrivateField = "someDummyData";
    }

    public String getServiceName() {
        return appName + SPACE + somePrivateField + suffixServiceName;
    }


    public List<Country> getAllCountries() throws MyException {
        schoolService.openSchool();

        syncDataService.syncPopulationData();

        return countryRepository.readAllCountries().stream()
                .map(this::classifyCountry)
                .collect(Collectors.toList());
    }

    public Country readByName(String countryName) {
        return countryRepository.readByName(countryName);
    }

    private Country classifyCountry(Country country) {
        country.classifyCountry();
        return country;
    }
}
