package com.test.workshoptesting.service;

import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

@Service
@Log
public class SchoolService {
    
    public void openSchool() {
        log.info("SchoolService::: Opening an school");
    }
}
