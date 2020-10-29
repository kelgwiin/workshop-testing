package com.test.workshoptesting.b_parameterized_tests;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregationException;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.stream.Stream;

import static com.test.workshoptesting.b_parameterized_tests.ExampleParameterizedTest.DaysOfWeek.SATURDAY;
import static com.test.workshoptesting.b_parameterized_tests.ExampleParameterizedTest.DaysOfWeek.SUNDAY;
import static com.test.workshoptesting.util.Constants.SMALL_TESTS;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumingThat;

@Tag(SMALL_TESTS)
class ExampleParameterizedTest {
    private DaysOfWeek daysOfWeek;

    //-----------------
    //PREDEFINED SOURCE
    //-----------------
    @ParameterizedTest
    @EmptySource
    @NullSource
    void itShouldExecuteTestCasesForString(String testCase) {
        assertTrue(StringUtils.isBlank(testCase));
    }

    //Parameterized Test with Enum
    @ParameterizedTest
    @EnumSource(value = DaysOfWeek.class, names = {"SATURDAY", "SUNDAY"})
    void testEnumSource(DaysOfWeek day) {
        assertTrue(() -> day == SUNDAY || day == SATURDAY);
    }

    //Parameterized Test with Value Source
    @ValueSource(ints = {2, 4, 6})
    void testValueSource(int number) {
        assertFalse(() -> number > 6);
    }

    //----------
    //CSV SOURCE
    //----------
    @ParameterizedTest
    @CsvSource({
            "Peter,23,Lawyer",
            "Joseph,34,Architect",
            "Joseph,21,Architect",
            "Mateusz,28,Lead Software Engineer",
            "Luis,28,Lead Business Analyst + Scrum Master",
    })
    void testCsvSource(String name, int age, String profession) {
        assertionEmployee(name, age, profession);
    }

    private void assertionEmployee(String name, int age, String profession) {
        assumingThat(
                () -> profession.equalsIgnoreCase("Architect"),//pre-condition
                () -> assertTrue(age >= 30, "The age is under 30")//executable
        );

        assertNotNull(name);
        assertTrue(() -> profession.length() <= 22, "The profession has exceeded the maximum of 22 characters");
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/test-cases.csv", numLinesToSkip = 1, delimiter = ',')
    void testCsvFileSource(String name, int age, String profession) {
        assertionEmployee(name, age, profession);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/test-cases.csv", numLinesToSkip = 1)
    void testCsvFileSource_usingArgumentAccessor(ArgumentsAccessor args) {
        assertionEmployee(args.getString(0), args.getInteger(1), args.getString(2));
    }


    @ParameterizedTest
    @CsvFileSource(resources = "/test-cases.csv", numLinesToSkip = 1)
    void testCsvFileSource_usingAggregateWith(@AggregateWith(EmployeeAggregator.class) Employee employee) {
        assertionEmployee(employee.getName(), employee.getAge(), employee.getProfession());
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/test-cases.csv", numLinesToSkip = 1)
    void testCsvFileSource_usingCustomAnnotation(@CsvToEmployee Employee employee) {
        assertionEmployee(employee.getName(), employee.getAge(), employee.getProfession());
    }

    //-------------
    //CUSTOM SOURCE
    //-------------
    @ParameterizedTest
    @MethodSource("employeeProvider")
    void testMethodSource(Employee employee) {
        assertionEmployee(employee.getName(), employee.getAge(), employee.getProfession());
    }

    private static Stream<Employee> employeeProvider() {
        return Stream
                .generate(() -> new Employee("John", 21, "Recruiter"))
                .limit(50);
    }

    //Implicit Provider
    @ParameterizedTest
    @MethodSource
    void testMethodSource_usingImplicitProvider(Employee employee) {
        assertionEmployee(employee.getName(), employee.getAge(), employee.getProfession());
    }

    private static Stream<Employee> testMethodSource_usingImplicitProvider(){
        return employeeProvider();
    }

    public enum DaysOfWeek {
        MONDAY,
        TUESDAY,
        WEDNESDAY,
        THURSDAY,
        FRIDAY,
        SATURDAY,
        SUNDAY
    }

    @Data
    @AllArgsConstructor
    public static class Employee {
        private String name;
        private int age;
        private String profession;
    }

    public static class EmployeeAggregator implements ArgumentsAggregator {
        @Override
        public Employee aggregateArguments(ArgumentsAccessor argumentsAccessor, ParameterContext context)
                throws ArgumentsAggregationException {
            return new Employee(argumentsAccessor.getString(0), argumentsAccessor.getInteger(1),
                    argumentsAccessor.getString(2));
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.PARAMETER)
    @AggregateWith(EmployeeAggregator.class)
    public @interface CsvToEmployee {
    }
}
