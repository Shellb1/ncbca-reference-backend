package com.reference.ncbca.config;

import com.opencsv.CSVReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class AppConfig {

    @Bean("conferencesMap")
    public Map<Integer, String> conferencesMap() {
        Map<Integer, String> conferencesMap = new HashMap<Integer, String>();
        conferencesMap.put(0, "Atlantic Coast Conference");
        conferencesMap.put(1, "Big Ten Conference");
        conferencesMap.put(2, "Southeastern Conference");
        conferencesMap.put(3, "Pacific Coast Conference");
        conferencesMap.put(4, "Big XVI Conference");
        conferencesMap.put(5, "Big East Conference");
        conferencesMap.put(6, "American Athletic Conference");
        conferencesMap.put(7, "Mountain West Conference");
        return conferencesMap;
    }
}
