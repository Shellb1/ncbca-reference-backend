package com.reference.ncbca.dao;

import com.reference.ncbca.dao.mappers.NTSeedsMapper;
import com.reference.ncbca.model.NTSeed;
import com.reference.ncbca.util.DaoHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

@Repository
public class NTSeedsDao {

    @Value("${database.hostname}")
    private String databaseHostName;

    @Value("${database.username}")
    private String userName;

    @Value("${database.password}")
    private String password;

    private static final String INSERT_SQL = "INSERT INTO nt_seeds (team_id, team_name, season, seed) VALUES (?, ?, ?, ?)";

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
}
