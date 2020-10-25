package com.test.workshoptesting.a_junit;

import com.test.workshoptesting.model.Continent;
import com.test.workshoptesting.model.Country;
import com.test.workshoptesting.util.AsyncUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;

import java.time.Duration;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumingThat;

@Tag("SmallTests")
class CountryTest {
    private Country country;

    @BeforeEach
    void setUp(){
        country = Country.builder()
                .name("Italy")
                .continent(Continent.EUROPE)
                .build();
    }

    @Test
    @DisplayName("Test country classification")
    void itShouldClassifyTheCountry(){
        //EXECUTE
        country.classifyCountry();

        //ASSERTIONS
        assertTrue(()-> StringUtils.isNotBlank(country.getName()));
        assertEquals("Old Continent", country.getLabel());
    }

    @Test
    @DisplayName("Test country classification - with Assumptions")
    void itShouldClassifyTheCountry_withAssumptions(){
        assumingThat
                (()-> Objects.isNull(country.getContinent()),
                ()-> country.classifyCountry());

        //ASSERTIONS
        assertNull(country.getLabel());
    }


    @Test
    @DisplayName("Test country classification - with Assumptions - [ERROR]")
    void itShouldClassifyTheCountry_withAssumptions_ERROR(){
        assumingThat
                (()-> Objects.isNull(country.getContinent()),
                ()-> country.classifyCountry());

        //ASSERTIONS
        assertEquals("Old Continent", country.getLabel());
    }

    @Test
    @DisplayName("Test for timeouts")
    void testTimeOut(){
        //ASSERTIONS
        assertTimeout(Duration.ofMillis(400),
                AsyncUtil::waitTwoSecondAndLog,
                "Timeout exceeded [400] for AsyncUtil#waitTwoSecondAndReturn call");

        assertTimeout(Duration.ofMillis(600),
                AsyncUtil::waitTwoSecondAndLog,
                "Timeout exceeded [600] for AsyncUtil#waitTwoSecondAndReturn call");

    }

}
