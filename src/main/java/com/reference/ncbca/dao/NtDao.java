package com.reference.ncbca.dao;

import com.reference.ncbca.dao.mappers.NitMapper;
import com.reference.ncbca.dao.mappers.NtMapper;
import com.reference.ncbca.model.NitGame;
import com.reference.ncbca.model.NtGame;
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
public class NtDao {

    @Value("${database.hostname}")
    private String databaseHostName;

    @Value("${database.username}")
    private String userName;

    @Value("${database.password}")
    private String password;

    private static final String INSERT_SQL = "INSERT INTO nt_games(game_id, season) VALUES(?,?)";
    private static final String LIST_NT_GAMES_SQL = "SELECT * FROM nt_games WHERE season = ?";

    private final NtMapper ntMapper;

    public NtDao(NtMapper nitMapper) {
        this.ntMapper = nitMapper;
    }

    public void insert(List<NtGame> teams) {
        String CONNECTION_STRING = "jdbc:mysql://" + databaseHostName + "/ncbca_reference?user=" + userName + "&password=" + password;
        try (Connection conn = DaoHelper.connect(CONNECTION_STRING)) {
            for (NtGame ntGame: teams) {
                PreparedStatement pstmt = conn.prepareStatement(INSERT_SQL);
                pstmt.setInt(1, ntGame.gameId());
                pstmt.setInt(2, ntGame.season());
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<NtGame> listNtGamesForSeason(Integer season) {
        String CONNECTION_STRING = "jdbc:mysql://" + databaseHostName + "/ncbca_reference?user=" + userName + "&password=" + password;
        try (Connection conn = DaoHelper.connect(CONNECTION_STRING)) {
            PreparedStatement pstmt = conn.prepareStatement(LIST_NT_GAMES_SQL);
            pstmt.setInt(1, season);
            ResultSet results = pstmt.executeQuery();
            return ntMapper.mapNtGameResults(results);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return Collections.emptyList();
    }
}
