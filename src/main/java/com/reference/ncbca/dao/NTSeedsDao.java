package com.reference.ncbca.dao;

import com.reference.ncbca.dao.mappers.NTSeedsMapper;
import com.reference.ncbca.model.dao.NTSeed;
import com.reference.ncbca.util.DaoHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class NTSeedsDao {

    @Value("${database.hostname}")
    private String databaseHostName;

    @Value("${database.username}")
    private String userName;

    @Value("${database.password}")
    private String password;

    private static final String INSERT_SQL = "INSERT INTO nt_seeds (team_id, team_name, season, seed) VALUES (?, ?, ?, ?)";
    private static final String GET_SEED_BY_TEAM_AND_SEASON_SQL = "SELECT * FROM nt_seeds WHERE team_id = ? AND season = ?";
    private static final String GET_ALL_SEEDS_FOR_TEAM_SQL = "SELECT * FROM nt_seeds WHERE team_name = ?";
    private static final String GET_ALL_SEEDS_FOR_SEASON_SQL = "SELECT * FROM nt_seeds WHERE season = ?";



    private final NTSeedsMapper mapper;

    public NTSeedsDao(NTSeedsMapper mapper) {
        this.mapper = mapper;
    }

    public void load(List<NTSeed> ntSeeds) {
        String CONNECTION_STRING = "jdbc:mysql://" + databaseHostName + "/ncbca_reference?user=" + userName + "&password=" + password;
        try (Connection connection = DaoHelper.connect(CONNECTION_STRING);
             PreparedStatement statement = connection.prepareStatement(INSERT_SQL)) {
            for (NTSeed seed: ntSeeds) {
                statement.setInt(1, seed.teamId());
                statement.setString(2, seed.teamName());
                statement.setInt(3, seed.season());
                statement.setInt(4, seed.seed());
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public Optional<NTSeed> getNTSeedForTeamAndYear(Integer teamId, Integer season) {
        String CONNECTION_STRING = "jdbc:mysql://" + databaseHostName + "/ncbca_reference?user=" + userName + "&password=" + password;
        try (Connection connection = DaoHelper.connect(CONNECTION_STRING);
             PreparedStatement statement = connection.prepareStatement(GET_SEED_BY_TEAM_AND_SEASON_SQL)) {
            statement.setInt(1, teamId);
            statement.setInt(2, season);
            ResultSet results = statement.executeQuery();
            List<NTSeed> mappedResults = mapper.mapNTSeeds(results);
            if (!mappedResults.isEmpty()) {
                return Optional.of(mappedResults.getFirst());
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return Optional.empty();
    }

    public List<NTSeed> getAllNTSeedsForTeam(String teamName) {
        String CONNECTION_STRING = "jdbc:mysql://" + databaseHostName + "/ncbca_reference?user=" + userName + "&password=" + password;
        try (Connection connection = DaoHelper.connect(CONNECTION_STRING);
             PreparedStatement statement = connection.prepareStatement(GET_ALL_SEEDS_FOR_TEAM_SQL)) {
            statement.setString(1, teamName);
            ResultSet results = statement.executeQuery();
            return mapper.mapNTSeeds(results);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return Collections.emptyList();
    }

    public List<NTSeed> getAllNTSeedsForSeason(Integer season) {
        String CONNECTION_STRING = "jdbc:mysql://" + databaseHostName + "/ncbca_reference?user=" + userName + "&password=" + password;
        try (Connection connection = DaoHelper.connect(CONNECTION_STRING);
             PreparedStatement statement = connection.prepareStatement(GET_ALL_SEEDS_FOR_SEASON_SQL)) {
            statement.setInt(1, season);
            ResultSet results = statement.executeQuery();
            return mapper.mapNTSeeds(results);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return Collections.emptyList();
    }
}
