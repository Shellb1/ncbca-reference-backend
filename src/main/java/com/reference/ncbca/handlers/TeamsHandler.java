package com.reference.ncbca.handlers;

import com.reference.ncbca.dao.TeamsDao;
import com.reference.ncbca.model.Team;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamsHandler {

    private TeamsDao teamsDao;

    public TeamsHandler(TeamsDao teamsDao) {
        this.teamsDao = teamsDao;
    }

    public Team getTeam(String id) {
        return null;
    }

    public void loadTeams(List<Team> teams) {
        teamsDao.insert(teams);
    }
}
