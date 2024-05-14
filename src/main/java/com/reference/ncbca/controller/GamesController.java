package com.reference.ncbca.controller;

import com.reference.ncbca.handlers.PostseasonHandler;
import com.reference.ncbca.model.PostseasonGame;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GamesController {

    private final PostseasonHandler postseasonHandler;

    public GamesController(PostseasonHandler postseasonHandler) {
        this.postseasonHandler = postseasonHandler;
    }

    @GetMapping("/getPostseasonGamesForTeam")
    public List<PostseasonGame> getPostseasonGamesForTeam(@RequestParam("season") Integer season,
                                                          @RequestParam("teamName") String teamName) {
        return postseasonHandler.getPostseasonGamesForYearAndTeam(season, teamName);
    }
}
