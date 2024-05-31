package com.reference.ncbca.model.dao;

public class Season {
    private Integer teamId;
    private String teamName;
    private Integer gamesWon;
    private Integer gamesLost;
    private Integer seasonYear;
    private String coach;
    private String conferenceName;
    private SeasonMetrics seasonMetrics;

    public Season() {
    }

    public Season(Integer teamId, String teamName, Integer gamesWon, Integer gamesLost, Integer seasonYear, String coach, String conferenceName, SeasonMetrics seasonMetrics) {
        this.teamId = teamId;
        this.teamName = teamName;
        this.gamesWon = gamesWon;
        this.gamesLost = gamesLost;
        this.seasonYear = seasonYear;
        this.coach = coach;
        this.conferenceName = conferenceName;
        this.seasonMetrics = seasonMetrics;
    }

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Integer getGamesWon() {
        return gamesWon;
    }

    public void setGamesWon(Integer gamesWon) {
        this.gamesWon = gamesWon;
    }

    public Integer getGamesLost() {
        return gamesLost;
    }

    public void setGamesLost(Integer gamesLost) {
        this.gamesLost = gamesLost;
    }

    public Integer getSeasonYear() {
        return seasonYear;
    }

    public void setSeasonYear(Integer seasonYear) {
        this.seasonYear = seasonYear;
    }

    public String getCoach() {
        return coach;
    }

    public void setCoach(String coach) {
        this.coach = coach;
    }

    public String getConferenceName() {
        return conferenceName;
    }

    public void setConferenceName(String conferenceName) {
        this.conferenceName = conferenceName;
    }

    public SeasonMetrics getSeasonMetrics() {
        return seasonMetrics;
    }

    public void setSeasonMetrics(SeasonMetrics seasonMetrics) {
        this.seasonMetrics = seasonMetrics;
    }
}
