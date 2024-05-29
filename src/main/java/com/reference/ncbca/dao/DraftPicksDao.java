package com.reference.ncbca.dao;

import com.reference.ncbca.model.dao.DraftPick;
import com.reference.ncbca.util.DaoHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import com.reference.ncbca.dao.mappers.DraftPicksMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class DraftPicksDao {

    @Value("${database.hostname}")
    private String databaseHostName;

    @Value("${database.username}")
    private String userName;

    @Value("${database.password}")
    private String password;

    private final DraftPicksMapper draftPicksMapper;

    public DraftPicksDao(DraftPicksMapper draftPicksMapper) {
        this.draftPicksMapper = draftPicksMapper;
    }

    private static final String INSERT_SQL = "INSERT INTO drafted_players (player_name, college_team_name, round, pick, season, position, coach_name) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String SELECT_BY_SEASON_SQL = "SELECT * FROM drafted_players WHERE season = ?";
    private static final String SELECT_BY_TEAM_SQL = "SELECT * FROM drafted_players WHERE college_team_name = ?";
    private static final String SELECT_BY_COACH_SQL = "SELECT * FROM drafted_players WHERE coach_name = ?";

    public void load(List<DraftPick> draftPickList) {
        String CONNECTION_STRING = "jdbc:mysql://" + databaseHostName + "/ncbca_reference?user=" + userName + "&password=" + password;
        try (Connection connection = DaoHelper.connect(CONNECTION_STRING)) {
            PreparedStatement statement = connection.prepareStatement(INSERT_SQL);
            for (DraftPick draftPick : draftPickList) {
                statement.setString(1, draftPick.playerName());
                statement.setString(2, draftPick.collegeTeamName());
                statement.setInt(3, draftPick.round());
                statement.setInt(4, draftPick.pick());
                statement.setInt(5, draftPick.season());
                statement.setString(6, draftPick.position());
                statement.setString(7, draftPick.coachName());
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<DraftPick> getDraftPicksBySeason(int season) {
        String CONNECTION_STRING = "jdbc:mysql://" + databaseHostName + "/ncbca_reference?user=" + userName + "&password=" + password;
        try (Connection connection = DaoHelper.connect(CONNECTION_STRING); PreparedStatement statement = connection.prepareStatement(SELECT_BY_SEASON_SQL)) {
            statement.setInt(1, season);
            ResultSet resultSet = statement.executeQuery();
            return draftPicksMapper.mapResult(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<DraftPick> getDraftPicksByTeam(String teamName) {
        String CONNECTION_STRING = "jdbc:mysql://" + databaseHostName + "/ncbca_reference?user=" + userName + "&password=" + password;
        try (Connection connection = DaoHelper.connect(CONNECTION_STRING); PreparedStatement statement = connection.prepareStatement(SELECT_BY_TEAM_SQL)) {
            statement.setString(1, teamName);
            ResultSet resultSet = statement.executeQuery();
            return draftPicksMapper.mapResult(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<DraftPick> getDraftPicksByCoach(String coachName) {
        String CONNECTION_STRING = "jdbc:mysql://" + databaseHostName + "/ncbca_reference?user=" + userName + "&password=" + password;
        try (Connection connection = DaoHelper.connect(CONNECTION_STRING); PreparedStatement statement = connection.prepareStatement(SELECT_BY_COACH_SQL)) {
            statement.setString(1, coachName);
            ResultSet resultSet = statement.executeQuery();
            return draftPicksMapper.mapResult(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
