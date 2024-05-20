package com.reference.ncbca.controller;

import com.reference.ncbca.handlers.TeamsHandler;
import com.reference.ncbca.model.Team;
import com.reference.ncbca.model.TeamSeasonSummary;
import com.reference.ncbca.model.TeamSummary;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TeamsController {

    private final TeamsHandler teamsHandler;

    public TeamsController(TeamsHandler teamsHandler) {
        this.teamsHandler = teamsHandler;
    }

    @GetMapping("/teamSeasonSummary")
    public TeamSeasonSummary getTeamSeasonSummary(@RequestParam("teamName") String teamName,
                                            @RequestParam("year") Integer year) {
        return teamsHandler.buildTeamSummary(teamName, year);
    }

    @GetMapping("/allTeams")
    public List<Team> listAllTeams() {
        return teamsHandler.listAllTeams();
    }

    @GetMapping("/teamSummary")
    public TeamSummary getTeamSummary(@RequestParam("teamName") String teamName) {
        return teamsHandler.getTeamSummary(teamName);
    }

}
