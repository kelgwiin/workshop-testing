package com.test.workshoptesting.d_mockito;

import com.test.workshoptesting.model.Country;
import com.test.workshoptesting.repository.CountryRepository;
import com.test.workshoptesting.service.CountryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class CountryServiceTest {
    private CountryService countryService;
    @Mock
    private CountryRepository countryRepository;

    @Captor
    private ArgumentCaptor<String> stringArgumentCaptor;

    @BeforeEach
    void init() {
        initMocks(this);
        countryService = new CountryService(countryRepository);
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

        ///ASSERT
        assertThat(response).hasSize(2);
        verify(countryRepository).readAllCountries();

    }


    @Test
    void testReadCountryByName() {
        //GIVEN
        var country = Country.builder()
                .name("Poland")
                .build();

        when(countryRepository.readByName(anyString()))
                .thenReturn(country);

        //EXECUTE
        var response = countryService.readByName("Poland");

        ///ASSERT
        verify(countryRepository).readByName(stringArgumentCaptor.capture());
        assertThat(stringArgumentCaptor.getValue()).isEqualTo("Poland");

        verify(countryRepository).readByName(anyString());
        verify(countryRepository).readByName("Poland");
        verify(countryRepository).readByName(eq("Poland"));

        verify(countryRepository, never()).readAllCountries();

    }
}
