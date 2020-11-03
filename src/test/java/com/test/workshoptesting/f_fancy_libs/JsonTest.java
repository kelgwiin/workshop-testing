package com.test.workshoptesting.f_fancy_libs;

import org.json.JSONException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

class JsonTest {

    @Test
    @DisplayName("Test: comparing unsorted fields")
    void testJson() throws JSONException {
        var expected =
            "{\n" +
                "    \"name\": \"Poland\",\n" +
                "    \"population\": \"20000000\",\n" +
                "    \"continent\": \"EUROPE\" \n" +
                "}";
        var actual =
            "{\n" +
                "    \"continent\": \"EUROPE\",\n" +
                "    \"population\": \"20000000\",\n" +
                "    \"name\": \"Poland\",\n" +
                "    \"otherField\": \"test\" \n" +
                "}";

        JSONAssert.assertEquals(expected, actual, JSONCompareMode.LENIENT);
    }

    @Test
    @DisplayName("Test: comparing unsorted fields - FAIL")
    void testJson_FAIL() throws JSONException {
        var expected =
            "{\n" +
                "    \"name\": \"Poland\",\n" +
                "    \"population\": \"20000000\",\n" +
                "    \"continent\": \"EUROPE\" \n" +
                "}";
        var actual =
            "{\n" +
                "    \"continent\": \"EUROPE\",\n" +
                "    \"population\": \"20000000\",\n" +
                "    \"name\": \"Poland\",\n" +
                "    \"otherField\": \"test\" \n" +
                "}";

        JSONAssert.assertEquals(expected, actual, JSONCompareMode.NON_EXTENSIBLE);
    }
}
