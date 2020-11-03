package com.test.workshoptesting.f_fancy_libs;

import lombok.Getter;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.awaitility.proxy.AwaitilityClassProxy.to;
import static org.hamcrest.Matchers.equalTo;

class AwaitilityTestingTest {

    private TestAsyncService service;

    @BeforeEach
    void init() {
        service = new TestAsyncService();
    }

    @Test
    void testAsyncMethodCall() {
        var asyncService = new TestAsyncService();

        asyncService.startProcess(20_000, 15_000);

        await()
            .atMost(4, SECONDS)
            .untilAsserted(() -> assertThat(asyncService.getResult()).isEqualTo(35_000));
    }


    @Test
    void testAsyncMethodCall_PROXY() {
        service.startProcess(20_000, 15_000);

        await()
            .atMost(4, SECONDS)
            .untilCall(to(service).getResult(), equalTo(35_000));
    }

    public static class TestAsyncService {
        @Getter
        private int result;

        @SneakyThrows
        public void startProcess(int a, int b) {
            Thread.sleep(3000);
            result = a + b;
        }
    }
}
