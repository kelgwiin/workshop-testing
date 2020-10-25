package com.test.workshoptesting.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.java.Log;

@Log
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AsyncUtil {
    @SneakyThrows
    public static void waitTwoSecondAndLog(){
        Thread.sleep(500);
        log.info("Message with timeout");
    }
}
