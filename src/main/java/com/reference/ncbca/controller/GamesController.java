package com.reference.ncbca.controller;

import com.reference.ncbca.handlers.CoachesHandler;
import com.reference.ncbca.handlers.GamesHandler;
import com.reference.ncbca.handlers.NitHandler;
import com.reference.ncbca.handlers.NtHandler;
import com.reference.ncbca.model.CoachSummary;
import com.reference.ncbca.model.NitGame;
import com.reference.ncbca.model.NtGame;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GamesController {

    private final NitHandler nitHandler;
    private final NtHandler ntHandler;

    public GamesController(NitHandler nitHandler, NtHandler ntHandler) {
        this.nitHandler = nitHandler;
        this.ntHandler = ntHandler;
    }

    @GetMapping("/getNitGamesForSeason")
    public List<NitGame> getNitGames(@RequestParam("season") Integer season) {
        return nitHandler.getNitGamesForSeason(season);
    }

    @GetMapping("/getNtGamesForSeason")
    public List<NtGame> getNtGames(@RequestParam("season") Integer season) {
        return ntHandler.getNtGamesForSeason(season);
    }


}
