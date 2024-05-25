package com.reference.ncbca.controller;

import com.reference.ncbca.handlers.AllTimeHandler;
import com.reference.ncbca.model.AllTimeProgramRanking;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AllTimeController {

    private final AllTimeHandler allTimeHandler;

    public AllTimeController(AllTimeHandler allTimeHandler) {
        this.allTimeHandler = allTimeHandler;
    }

    @GetMapping("/allTimeProgramRankings")
    public List<AllTimeProgramRanking> allTimeProgramRankingList() {
        return allTimeHandler.getAllTimeProgramRankings();
    }
}
