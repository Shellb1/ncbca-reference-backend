package com.reference.ncbca.controller;

import com.reference.ncbca.handlers.CoachesHandler;
import com.reference.ncbca.handlers.GamesHandler;
import com.reference.ncbca.handlers.NitHandler;
import com.reference.ncbca.model.CoachSummary;
import com.reference.ncbca.model.NitGame;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GamesController {

    private final NitHandler nitHandler;

    public GamesController(NitHandler nitHandler) {
        this.nitHandler = nitHandler;
    }

    @GetMapping("/getNitTeamsForSeason")
    public List<NitGame> getNitGames(@RequestParam("season") Integer season) {
        return nitHandler.getNitGamesForSeason(season);
    }
}
