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
            if (game.getGameId() <= 2175) {
                gameType = "REGULAR_SEASON";
            } else if (game.getGameId() <= 2295) {
                gameType = "CONFERENCE_TOURNAMENT";
            } else if (game.getGameId() <= 2310) {
                gameType = "NIT";
            } else if (game.getGameId() <= 2318) {
                gameType = "FIRST_SIXTEEN";
            } else {
                gameType = "NT";
            }
            gamesToBackload.add(new Game.Builder()
                    .gameId(game.getGameId())
                    .season(game.getSeason())
                    .neutralSite(game.getNeutralSite())
                    .homeTeamId(game.getHomeTeamId())
                    .awayTeamId(game.getAwayTeamId())
                    .homeTeamName(game.getHomeTeamName())
                    .awayTeamName(game.getAwayTeamName())
                    .winningTeamId(game.getWinningTeamId())
                    .winningTeamName(game.getWinningTeamName())
                    .winningTeamScore(game.getWinningTeamScore())
                    .losingTeamId(game.getLosingTeamId())
                    .losingTeamName(game.getLosingTeamName())
                    .losingTeamScore(game.getLosingTeamScore())
                    .winningCoachName(game.getWinningCoachName())
                    .losingCoachName(game.getLosingCoachName())
                    .gameType(game.getGameType())
                    .build());
        }
        gamesHandler.backload(gamesToBackload);
    }
}
