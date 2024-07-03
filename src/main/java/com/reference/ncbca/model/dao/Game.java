package com.reference.ncbca.model.dao;

public final class Game {
    private final Integer gameId;
    private final Integer season;
    private final Boolean neutralSite;
    private final Integer homeTeamId;
    private final Integer awayTeamId;
    private final String homeTeamName;
    private final String awayTeamName;
    private final Integer winningTeamId;
    private final String winningTeamName;
    private final Integer winningTeamScore;
    private final Integer losingTeamId;
    private final String losingTeamName;
    private final Integer losingTeamScore;
    private final String winningCoachName;
    private final String losingCoachName;
    private final String gameType;

    private Game(Builder builder) {
        this.gameId = builder.gameId;
        this.season = builder.season;
        this.neutralSite = builder.neutralSite;
        this.homeTeamId = builder.homeTeamId;
        this.awayTeamId = builder.awayTeamId;
        this.homeTeamName = builder.homeTeamName;
        this.awayTeamName = builder.awayTeamName;
        this.winningTeamId = builder.winningTeamId;
        this.winningTeamName = builder.winningTeamName;
        this.winningTeamScore = builder.winningTeamScore;
        this.losingTeamId = builder.losingTeamId;
        this.losingTeamName = builder.losingTeamName;
        this.losingTeamScore = builder.losingTeamScore;
        this.winningCoachName = builder.winningCoachName;
        this.losingCoachName = builder.losingCoachName;
        this.gameType = builder.gameType;
    }

    // Getters
    public Integer getGameId() {
        return gameId;
    }

    public Integer getSeason() {
        return season;
    }

    public Boolean getNeutralSite() {
        return neutralSite;
    }

    public Integer getHomeTeamId() {
        return homeTeamId;
    }

    public Integer getAwayTeamId() {
        return awayTeamId;
    }

    public String getHomeTeamName() {
        return homeTeamName;
    }

    public String getAwayTeamName() {
        return awayTeamName;
    }

    public Integer getWinningTeamId() {
        return winningTeamId;
    }

    public String getWinningTeamName() {
        return winningTeamName;
    }

    public Integer getWinningTeamScore() {
        return winningTeamScore;
    }

    public Integer getLosingTeamId() {
        return losingTeamId;
    }

    public String getLosingTeamName() {
        return losingTeamName;
    }

    public Integer getLosingTeamScore() {
        return losingTeamScore;
    }

    public String getWinningCoachName() {
        return winningCoachName;
    }

    public String getLosingCoachName() {
        return losingCoachName;
    }

    public String getGameType() {
        return gameType;
    }

    // Builder class
    public static class Builder {
        private Integer gameId;
        private Integer season;
        private Boolean neutralSite;
        private Integer homeTeamId;
        private Integer awayTeamId;
        private String homeTeamName;
        private String awayTeamName;
        private Integer winningTeamId;
        private String winningTeamName;
        private Integer winningTeamScore;
        private Integer losingTeamId;
        private String losingTeamName;
        private Integer losingTeamScore;
        private String winningCoachName;
        private String losingCoachName;
        private String gameType;

        public Builder gameId(Integer gameId) {
            this.gameId = gameId;
            return this;
        }

        public Builder season(Integer season) {
            this.season = season;
            return this;
        }

        public Builder neutralSite(Boolean neutralSite) {
            this.neutralSite = neutralSite;
            return this;
        }

        public Builder homeTeamId(Integer homeTeamId) {
            this.homeTeamId = homeTeamId;
            return this;
        }

        public Builder awayTeamId(Integer awayTeamId) {
            this.awayTeamId = awayTeamId;
            return this;
        }

        public Builder homeTeamName(String homeTeamName) {
            this.homeTeamName = homeTeamName;
            return this;
        }

        public Builder awayTeamName(String awayTeamName) {
            this.awayTeamName = awayTeamName;
            return this;
        }

        public Builder winningTeamId(Integer winningTeamId) {
            this.winningTeamId = winningTeamId;
            return this;
        }

        public Builder winningTeamName(String winningTeamName) {
            this.winningTeamName = winningTeamName;
            return this;
        }

        public Builder winningTeamScore(Integer winningTeamScore) {
            this.winningTeamScore = winningTeamScore;
            return this;
        }

        public Builder losingTeamId(Integer losingTeamId) {
            this.losingTeamId = losingTeamId;
            return this;
        }

        public Builder losingTeamName(String losingTeamName) {
            this.losingTeamName = losingTeamName;
            return this;
        }

        public Builder losingTeamScore(Integer losingTeamScore) {
            this.losingTeamScore = losingTeamScore;
            return this;
        }

        public Builder winningCoachName(String winningCoachName) {
            this.winningCoachName = winningCoachName;
            return this;
        }

        public Builder losingCoachName(String losingCoachName) {
            this.losingCoachName = losingCoachName;
            return this;
        }

        public Builder gameType(String gameType) {
            this.gameType = gameType;
            return this;
        }

        public Game build() {
            return new Game(this);
        }
    }
}
