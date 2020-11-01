package com.test.workshoptesting.e_spring;

import com.test.workshoptesting.repository.CountryRepository;
import com.test.workshoptesting.service.CountryService;
import com.test.workshoptesting.service.SchoolService;
import com.test.workshoptesting.service.SyncDataService;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.only;

@SpringBootTest
@ActiveProfiles(profiles = {"test"})
@TestPropertySource(
        properties = {"app.suffix=-someSuffix"}
)
@Log
class Test02SpringIntegrationTest {
    @Autowired
    private CountryService countryService;

    @SpyBean
    private CountryRepository countryRepository;

    @SpyBean
    private SchoolService schoolService;

    @SpyBean
    private SyncDataService syncDataService;

    @SneakyThrows
    @Test
    @DisplayName("Test All Countries: Stubbing methods calls")
    void testGetAllCountries() {
        assertThat(countryService.getServiceName())
                .isEqualTo("Testing Workshop - Getting the title using test profile someDummyData-someSuffix");

        var expectedCountryNames = Set.of("Poland", "Venezuela", "Spain");

        assertThat(countryService.getAllCountries())
                .hasSize(3)
                .allMatch(country -> expectedCountryNames.contains(country.getName()));

        verify(countryRepository, only()).readAllCountries();
        verify(countryRepository, never()).readByName(anyString());
        verify(syncDataService).syncPopulationData();
        verify(schoolService).openSchool();
    }
}
