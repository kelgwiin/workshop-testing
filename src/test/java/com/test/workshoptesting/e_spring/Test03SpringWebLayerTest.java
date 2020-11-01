package com.test.workshoptesting.e_spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.workshoptesting.controller.CountryController;
import com.test.workshoptesting.model.Continent;
import com.test.workshoptesting.model.Country;
import com.test.workshoptesting.service.CountryService;
import lombok.extern.java.Log;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ActiveProfiles(profiles = {"test"})
@TestPropertySource(
    properties = {"app.suffix=-someSuffix"}
)
@Log
class Test03SpringWebLayerTest {
    @Mock
    private CountryService countryService;

    private String url;

    private MockMvc mockMvc;

    private CountryController countryController;

    @Captor
    private ArgumentCaptor<String> stringArgumentCaptor;

    @BeforeEach
    void init() {
        initMocks(this);
        url = "/api/v1/countries/Peru";

        countryController = spy(new CountryController(countryService));
        mockMvc = MockMvcBuilders.standaloneSetup(countryController)
            //addInterceptors()
            .build();
    }

    @Test
    void testGetCountryByName() throws Exception {
        //GIVEN
        var country = Country.builder()
            .name("Peru")
            .continent(Continent.AMERICA)
            .build();
        when(countryService.readByName(anyString())).thenReturn(country);

        //EXECUTE
        var response = mockMvc
            .perform(
                get(url)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().is2xxSuccessful())
            .andReturn().getResponse();

        //VERIFY
        ObjectMapper objectMapper = new ObjectMapper();
        var actualCountry = objectMapper.readValue(response.getContentAsString(), Country.class);

        assertThat(actualCountry).isEqualTo(country);
        verify(countryService).readByName(anyString());
        verify(countryController, only()).getCountry(stringArgumentCaptor.capture());
        assertThat(stringArgumentCaptor.getValue()).isEqualTo("Peru");
    }
}
