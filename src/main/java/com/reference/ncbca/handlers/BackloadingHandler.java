package com.reference.ncbca.handlers;

import com.reference.ncbca.model.dao.Game;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BackloadingHandler {

    private final GamesHandler gamesHandler;

    public BackloadingHandler(GamesHandler gamesHandler) {
        this.gamesHandler = gamesHandler;
    }

    public void backloadGames() {
        List<Game> gamesList = gamesHandler.listAll();
        List<Game> gamesToBackload = new ArrayList<>();
        for (Game game: gamesList) {
            String gameType = null;
            if (game.gameId() <= 2175) {
                gameType = "REGULAR_SEASON";
            } else if (game.gameId() <= 2295) {
                gameType = "CONFERENCE_TOURNAMENT";
            } else if (game.gameId() <= 2310) {
                gameType = "NIT";
            } else if (game.gameId() <= 2318) {
                gameType = "FIRST_SIXTEEN";
            } else {
                gameType = "NT";
            }
            gamesToBackload.add(new Game(game.gameId(), game.season(), game.neutralSite(), game.homeTeamId(), game.awayTeamId(), game.homeTeamName(), game.awayTeamName(), game.winningTeamId(), game.winningTeamName(), game.winningTeamScore(), game.losingTeamId(), game.losingTeamName(), game.losingTeamScore(), game.winningCoachName(), game.losingCoachName(), gameType));
        }
        gamesHandler.backload(gamesToBackload);
    }
}
