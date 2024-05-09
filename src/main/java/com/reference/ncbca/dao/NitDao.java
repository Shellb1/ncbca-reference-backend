package com.reference.ncbca.dao;

import com.reference.ncbca.dao.mappers.NitMapper;
import com.reference.ncbca.model.NitGame;
import com.reference.ncbca.util.DaoHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

@Repository
public class NitDao {

    @Value("${database.hostname}")
    private String databaseHostName;

    @Value("${database.username}")
    private String userName;

    @Value("${database.password}")
    private String password;

    private static final String INSERT_SQL = "INSERT INTO nit(game_id, season) VALUES(?,?)";
    private static final String LIST_NIT_TEAMS_SQL = "SELECT * FROM nit WHERE season = ?";

    private final NitMapper nitMapper;

    public NitDao(NitMapper nitMapper) {
        this.nitMapper = nitMapper;
    }

    public void insert(List<NitGame> teams) {
        String CONNECTION_STRING = "jdbc:mysql://" + databaseHostName + "/ncbca_reference?user=" + userName + "&password=" + password;
        try (Connection conn = DaoHelper.connect(CONNECTION_STRING)) {
            PreparedStatement pstmt = conn.prepareStatement(INSERT_SQL);
            for (NitGame nitTeam: teams) {
                pstmt.setInt(1, nitTeam.gameId());
                pstmt.setInt(2, nitTeam.season());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            pstmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<NitGame> listNitGamesForSeason(Integer season) {
        String CONNECTION_STRING = "jdbc:mysql://" + databaseHostName + "/ncbca_reference?user=" + userName + "&password=" + password;
        try (Connection conn = DaoHelper.connect(CONNECTION_STRING)) {
            PreparedStatement pstmt = conn.prepareStatement(LIST_NIT_TEAMS_SQL);
            pstmt.setInt(1, season);
            ResultSet results = pstmt.executeQuery();
            return nitMapper.mapResult(results);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return Collections.emptyList();
    }


}
