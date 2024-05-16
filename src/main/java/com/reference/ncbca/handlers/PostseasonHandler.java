package com.reference.ncbca.handlers;


import com.reference.ncbca.dao.PostseasonDao;
import com.reference.ncbca.model.PostseasonGame;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostseasonHandler {

    private final PostseasonDao postseasonDao;

    public PostseasonHandler(PostseasonDao postseasonDao) {
        this.postseasonDao = postseasonDao;
    }

    public void load(List<PostseasonGame> games) {
        postseasonDao.insert(games);
    }

    public List<PostseasonGame> getPostseasonGamesForYearAndTeam(Integer season, String teamName) {
        List<PostseasonGame> games = postseasonDao.listPostseasonGamesForSeason(season);
        games.removeIf(game -> !teamName.equals(game.winningTeamName()) && !teamName.equals(game.losingTeamName()));
        return games;
    }


}
