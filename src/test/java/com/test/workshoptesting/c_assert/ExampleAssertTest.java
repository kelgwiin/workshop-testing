package com.test.workshoptesting.c_assert;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@Log
class ExampleAssertTest {
    private TestCase testCase;

    @BeforeEach
    void setUp() {
        testCase = TestCase.builder()
                .caseNumber(2)
                .name("TestCase Assert")
                .aliases(
                        Stream.generate(() -> "SomeAlias").limit(5).collect(Collectors.toList())
                )
                .build();
    }

    @Test
    void assertJMethods() {
        assertThat(testCase.getName())
                .isEqualTo("TestCase Assert");
        assertThat(testCase.getName()).isNotBlank();

        assertThat(testCase.getName())
                .isNotBlank()
                .isEqualTo("TestCase Assert")
                .containsIgnoringCase("assert")
                .startsWith("Test");

        //Arrays
        assertThat(testCase.getAliases())
                .hasSize(5);

        var numbers = List.of(2, 4, 6 , 8 , 20, 30, 40);

        assertThat(numbers)
                .filteredOn(num-> num >= 20)
                .containsExactlyInAnyOrder(40, 30, 20);

        //Given Fields
        var actualTestCase = TestCase.builder()
                .name("Creative name #0007")
                .caseNumber(7)
                .build();

        var expectedTestCase = TestCase.builder()
                .name("Creative name #0007")
                .caseNumber(7)
                .otherField("I'm other creative field")
                .build();

        assertThat(expectedTestCase)
                .usingRecursiveComparison()
                .ignoringFields("otherField", "aliases")
                .isEqualTo(actualTestCase);


    }

    @Test
    void assertJMethods_Exceptions(){
        assertThatThrownBy(TestCase::getSomeException)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Not implemented yet");

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(TestCase::getSomeException);

        assertThatCode(()->TestCase.runMethodNoExceptions())
                .doesNotThrowAnyException();
    }



    @Test
    void assertJMethods_FAIL() {
        assertThat(testCase.getName())
                .as("Comparing the testCase name")
                .isEqualTo("TestCase Assert - Not Equal");

    }


    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class TestCase {
        private String name;
        private int caseNumber;
        private String otherField;
        private List<String> aliases;

        public static void getSomeException() {
            throw new IllegalStateException("Not implemented yet");
        }

        public static void runMethodNoExceptions() {
            log.info("This method does not throw any exception");
        }

    }
}
