package com.test.workshoptesting.service;

import com.test.workshoptesting.exception.MyException;
import lombok.Data;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

@Service
@Data
@Log
public class SyncDataService {
    public void syncPopulationData() throws MyException {
        log.info("SyncDataService::: Syncing population data");
    }
}
