package com.reference.ncbca.controller;


import com.reference.ncbca.handlers.SeasonMetricsHandler;
import com.reference.ncbca.model.SeasonMetrics;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SeasonMetricsController {

    private final SeasonMetricsHandler seasonMetricsHandler;

    public SeasonMetricsController(SeasonMetricsHandler seasonMetricsHandler) {
        this.seasonMetricsHandler = seasonMetricsHandler;
    }

    @GetMapping("/season-metrics")
    public List<SeasonMetrics> listAllSeasonMetricsForSeason(@RequestParam Integer season) {
        return seasonMetricsHandler.getSeasonMetricsForSeason(season);
    }
}
