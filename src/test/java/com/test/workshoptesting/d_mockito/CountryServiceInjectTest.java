package com.test.workshoptesting.d_mockito;

import com.test.workshoptesting.exception.MyException;
import com.test.workshoptesting.model.Country;
import com.test.workshoptesting.repository.CountryRepository;
import com.test.workshoptesting.service.CountryService;
import com.test.workshoptesting.service.SchoolService;
import com.test.workshoptesting.service.SyncDataService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CountryServiceInjectTest {
    @InjectMocks
    private CountryService countryService;
    @Mock
    private CountryRepository countryRepository;
    @Mock
    private SchoolService schoolService;
    @Spy
    private SyncDataService syncDataService;

    @Test
    @DisplayName("Number of countries = 2")
    void testGetAllCountries() throws MyException {
        //GIVEN
        var country = Country.builder()
                .name("Country 01")
                .build();

        when(countryRepository.readAllCountries())
                .thenReturn(List.of(country, country));

        //EXECUTE
        var response = countryService.getAllCountries();

        //ASSERT
        assertThat(response).hasSize(2);
        verify(schoolService).openSchool();
    }
}
