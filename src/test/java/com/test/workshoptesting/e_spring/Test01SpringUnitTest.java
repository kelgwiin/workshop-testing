package com.test.workshoptesting.e_spring;

import com.test.workshoptesting.repository.CountryInMemoryRepository;
import com.test.workshoptesting.repository.CountryRepository;
import com.test.workshoptesting.service.CountryService;
import com.test.workshoptesting.service.SchoolService;
import com.test.workshoptesting.service.SyncDataService;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        CountryService.class,
        SchoolService.class,
        CountryInMemoryRepository.class
})
@TestPropertySource(
        properties = {
                "spring.application.name=Testing Workshop modified"
        }
)
@Log
class Test01SpringUnitTest {
    @Autowired
    private CountryService countryService;

    @SpyBean
    private CountryRepository countryRepository;

    @SpyBean
    private SchoolService schoolService;

    @MockBean
    private SyncDataService syncDataService;

    @BeforeEach
    void init() {
        ReflectionTestUtils.setField(countryService, "somePrivateField", "with reflection");
    }

    @SneakyThrows
    @Test
    @DisplayName("Test All Countries: Stubbing methods calls")
    void testGetAllCountries() {
        //GIVEN
        doAnswer(invocation -> {
            log.info("STUBBED VERSION - SyncDataService::: Syncing population data");
            return null;
        }).when(syncDataService).syncPopulationData();

        assertThat(countryService.getServiceName())
                .isEqualTo("Testing Workshop modified with reflection");

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
