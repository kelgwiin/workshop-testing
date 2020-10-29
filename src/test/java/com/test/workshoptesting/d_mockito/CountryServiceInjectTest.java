package com.test.workshoptesting.d_mockito;

import com.test.workshoptesting.model.Country;
import com.test.workshoptesting.repository.CountryRepository;
import com.test.workshoptesting.service.CountryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class CountryServiceInjectTest {
    @InjectMocks
    private CountryService countryService;
    @Mock
    private CountryRepository countryRepository;

    @BeforeEach
    void init() {
        initMocks(this);
    }

    @Test
    @DisplayName("Number of countries = 2")
    void testGetAllCountries() {
        //GIVEN
        var country = Country.builder()
                .name("Country 01")
                .build();

        when(countryRepository.readAllCountries())
                .thenReturn(List.of(country, country));

        //EXECUTE
        var response = countryService.getAllCountries();
        assertThat(response).hasSize(2);

    }
}
