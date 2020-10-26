package com.test.workshoptesting.a_junit;

import com.test.workshoptesting.model.Continent;
import com.test.workshoptesting.model.Country;
import com.test.workshoptesting.util.AsyncUtil;
import com.test.workshoptesting.util.Constants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.java.Log;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;

import java.time.Duration;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumingThat;

@Tag(Constants.SMALL_TESTS)
@Log
class CountryTest {
    private Country country;
    private static int numberOfCases;

    @BeforeEach
    void setUp() {
        country = Country.builder()
                .name("Italy")
                .continent(Continent.EUROPE)
                .build();
    }

    @AfterEach
    void afterEach() {
        numberOfCases++;
    }

    @BeforeAll
    static void beforeAll() {
        numberOfCases = 0;
    }

    @AfterAll
    static void afterAll() {
        log.info("Number of cases run #" + numberOfCases);
        numberOfCases = 0;
    }

    @Test
    @DisplayName("Test country classification")
    void itShouldClassifyTheCountry() {
        //EXECUTE
        country.classifyCountry();

        //ASSERTIONS
        assertTrue(StringUtils.isNotBlank(country.getName()));
        assertTrue(() -> StringUtils.isNotBlank(country.getName()));
        assertFalse(Objects.isNull(country.getLabel()));
        assertEquals("Old Continent", country.getLabel());
    }

    @Test
    @DisplayName("Test country classification - with Assumptions")
    void itShouldClassifyTheCountry_withAssumptions_NULL() {
        assumingThat
                (() -> Objects.isNull(country.getContinent()),
                        () -> country.classifyCountry());

        //ASSERTIONS
        assertNull(country.getLabel());
    }


    @Test
    @DisplayName("Test country classification - with Assumptions - [ERROR]")
    void itShouldClassifyTheCountry_withAssumptions_ERROR() {
        assumingThat
                (() -> Objects.isNull(country.getContinent()),
                        () -> country.classifyCountry());

        //ASSERTIONS
        assertEquals("Old Continent", country.getLabel());
    }

    @Test
    @DisplayName("Test for timeouts")
    void testTimeout_ERROR() {
        //ASSERTIONS
        assertTimeout(Duration.ofMillis(700),
                AsyncUtil::waitTwoSecondAndLog,
                "Timeout not exceeded [700] for AsyncUtil#waitTwoSecondAndReturn call");

        assertTimeout(Duration.ofMillis(400),
                AsyncUtil::waitTwoSecondAndLog,
                "Timeout exceeded [400] for AsyncUtil#waitTwoSecondAndReturn call");
    }

    @Test
    void testTimeoutPreemptively_ERROR() {
        //Aborts the process when timeout exceeded
        assertTimeoutPreemptively(Duration.ofMillis(400),
                AsyncUtil::waitTwoSecondAndLog,
                "[Preemptively] Timeout exceeded [400] for AsyncUtil#waitTwoSecondAndReturn call");
    }

    @Test
    void testAssertAll() {
        //GIVEN
        var text = "someText";
        var testCase = new TestCase("Test 01", 22);

        assertAll(
                () -> assertTrue(text.isEmpty(), "Checking text"),
                () -> assertEquals(22, testCase.getCaseNumber(), "Checking case number")
        );
    }

    @RepeatedTest(value = 5, name = "Some name {currentRepetition}/{totalRepetitions}")
    void testRepeatedTimes(RepetitionInfo repetitionInfo) {
        var text = "someText";
        var testCase = new TestCase("Test 01", 22 + repetitionInfo.getCurrentRepetition());

        assertTrue(() -> testCase.getCaseNumber() == 22 + repetitionInfo.getCurrentRepetition());

    }

    @Test
    @DisplayName("Test getting some exception")
    void testException(){
        assertThrows(
                IllegalStateException.class,
                TestCase::getSomeException);

        assertDoesNotThrow(TestCase::runMethodNoExceptions);
    }

    @AllArgsConstructor
    @Data
    public static class TestCase {
        private String name;
        private int caseNumber;

        public static void getSomeException(){
            throw new IllegalStateException("Not implemented yet");
        }

        public static void runMethodNoExceptions(){
            log.info("This method does not throw any exception");
        }

    }

}
