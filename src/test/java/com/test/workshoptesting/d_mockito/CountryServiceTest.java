package com.test.workshoptesting.d_mockito;

import com.test.workshoptesting.exception.MyException;
import com.test.workshoptesting.model.Country;
import com.test.workshoptesting.repository.CountryRepository;
import com.test.workshoptesting.service.CountryService;
import com.test.workshoptesting.service.SchoolService;
import com.test.workshoptesting.service.SyncDataService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class CountryServiceTest {
    private CountryService countryService;
    @Mock
    private CountryRepository countryRepository;

    private SyncDataService syncDataService;

    private SchoolService schoolService;

    @Captor
    private ArgumentCaptor<String> stringArgumentCaptor;

    @BeforeEach
    void init() {
        initMocks(this);
        syncDataService = mock(SyncDataService.class);
        schoolService = spy(new SchoolService());

        countryService = new CountryService(countryRepository, syncDataService, schoolService);
    }

    @SneakyThrows
    @Test
    @DisplayName("Number of countries = 2")
    void testGetAllCountries() {
        //GIVEN
        var country = Country.builder()
                .name("Country 01")
                .build();

        when(countryRepository.readAllCountries())
                .thenReturn(List.of(country, country));

        doNothing()
                .when(syncDataService).syncPopulationData();

        //EXECUTE
        var response = countryService.getAllCountries();

        ///ASSERT
        assertThat(response).hasSize(2);
        verify(countryRepository).readAllCountries();
        verify(syncDataService, only()).syncPopulationData();
        verify(schoolService, only()).openSchool();

    }

    @SneakyThrows
    @Test
    void testGetAllCountries_with_Exception() {
        when(countryRepository.readAllCountries())
                .thenReturn(Collections.emptyList());
        doThrow(new MyException("some error #002"))
                .when(syncDataService)
                .syncPopulationData();

        assertThatThrownBy(() -> countryService.getAllCountries())
                .isInstanceOf(MyException.class)
                .hasMessageContaining("#002");
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
