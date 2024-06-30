package com.reference.ncbca.model.dao;

public class SeasonMetrics {
    private String teamName;
    private Integer teamId;
    private Integer season;
    private Double rpi;
    private Double sos;
    private Double srs;
    private String q1Record;
    private String q2Record;
    private String q3Record;
    private String q4Record;

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    public Integer getSeason() {
        return season;
    }

    public void setSeason(Integer season) {
        this.season = season;
    }

    public Double getRpi() {
        return rpi;
    }

    public void setRpi(Double rpi) {
        this.rpi = rpi;
    }

    public Double getSos() {
        return sos;
    }

    public void setSos(Double sos) {
        this.sos = sos;
    }

    public Double getSrs() {
        return srs;
    }

    public void setSrs(Double srs) {
        this.srs = srs;
    }

    public String getQ1Record() {
        return q1Record;
    }

    public void setQ1Record(String q1Record) {
        this.q1Record = q1Record;
    }

    public String getQ2Record() {
        return q2Record;
    }

    public void setQ2Record(String q2Record) {
        this.q2Record = q2Record;
    }

    public String getQ3Record() {
        return q3Record;
    }

    public void setQ3Record(String q3Record) {
        this.q3Record = q3Record;
    }

    public String getQ4Record() {
        return q4Record;
    }

    public void setQ4Record(String q4Record) {
        this.q4Record = q4Record;
    }
}
