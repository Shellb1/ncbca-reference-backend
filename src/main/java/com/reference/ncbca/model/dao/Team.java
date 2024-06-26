package com.reference.ncbca.model.dao;

import java.util.Objects;

public record Team(Integer teamId, String name, Integer conferenceId, String conferenceName, String coach, Boolean active) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Team team = (Team) o;
        return Objects.equals(name, team.name) && Objects.equals(teamId, team.teamId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamId, name);
    }
}
