package com.reference.ncbca.handlers;

import com.reference.ncbca.dao.SeasonMetricsDao;
import com.reference.ncbca.model.dao.SeasonMetrics;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class SeasonMetricsHandler {

    private final SeasonMetricsDao seasonMetricsDao;

    public SeasonMetricsHandler(SeasonMetricsDao seasonMetricsDao) {
        this.seasonMetricsDao = seasonMetricsDao;
    }

    public void load(List<SeasonMetrics> metrics) {
        seasonMetricsDao.load(metrics);
    }

    public List<SeasonMetrics> getSeasonMetricsForSeason(Integer season) {
        return seasonMetricsDao.listAllSeasonMetricsForSeason(season);
    }
}
