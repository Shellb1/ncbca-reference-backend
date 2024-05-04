package com.reference.ncbca.dao;

import com.reference.ncbca.dao.mappers.NitMapper;
import com.reference.ncbca.model.NitGame;
import com.reference.ncbca.util.DaoHelper;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

@Repository
public class NitDao {

    private static final String CONNECTION_STRING = "jdbc:sqlite:src/main/resources/databases/nit.db";
    private static final String INSERT_SQL = "INSERT INTO nit(game_id, season) VALUES(?,?)";
    private static final String LIST_NIT_TEAMS_SQL = "SELECT * FROM nit WHERE season = ?";

    private final NitMapper nitMapper;

    public NitDao(NitMapper nitMapper) {
        this.nitMapper = nitMapper;
    }

    public void insert(List<NitGame> teams) {
        try (Connection conn = DaoHelper.connect(CONNECTION_STRING)) {
            for (NitGame nitTeam: teams) {
                PreparedStatement pstmt = conn.prepareStatement(INSERT_SQL);
                pstmt.setInt(1, nitTeam.gameId());
                pstmt.setInt(2, nitTeam.season());
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<NitGame> listNitGamesForSeason(Integer season) {
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
