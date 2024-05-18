package com.reference.ncbca.controller;

import com.reference.ncbca.handlers.LoadNBLHandler;
import com.reference.ncbca.handlers.NBLHandler;
import com.reference.ncbca.model.DraftPick;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class NBLController {

    private final NBLHandler nblHandler;

    public NBLController(NBLHandler nblHandler, LoadNBLHandler loadNBLHandler) {
        this.nblHandler = nblHandler;
    }

    @GetMapping("/getDraftPicks")
    public List<DraftPick> getDraftPicksForCoach(@RequestParam("coachName") String coachName) {
        return nblHandler.getAllDraftPicksForCoach(coachName);
    }

    @GetMapping("/getDraftPicksBySeason")
    public List<DraftPick> getDraftPicksForSeason(@RequestParam("season") Integer season) {
        return nblHandler.getAllDraftPicksForSeason(season);
    }

}
