package com.reference.ncbca.controller;

import com.reference.ncbca.handlers.CoachesHandler;
import com.reference.ncbca.model.CoachSummary;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CoachesController {

    private final CoachesHandler coachesHandler;

    public CoachesController(CoachesHandler coachesHandler) {
        this.coachesHandler = coachesHandler;
    }

    @GetMapping("/coachSummary")
    public CoachSummary getCoachSummary(@RequestParam("coachName") String coachName) {
        return coachesHandler.getCoachSummary(coachName);
    }

}
