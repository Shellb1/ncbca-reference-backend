package com.reference.ncbca.dao;

import com.reference.ncbca.dao.mappers.SeasonMetricsMapper;
import com.reference.ncbca.model.dao.SeasonMetrics;
import com.reference.ncbca.util.DaoHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class SeasonMetricsDao {

    @Value("${database.hostname}")
    private String databaseHostName;

    @Value("${database.username}")
    private String userName;

    @Value("${database.password}")
    private String password;

    private final SeasonMetricsMapper mapper;
    private static final String LOAD_METRICS_SQL = "INSERT INTO season_metrics (team_id, team_name, season, rpi, sos, srs) VALUES (?, ?, ?, ?, ?, ?)";
    public SeasonMetricsDao(SeasonMetricsMapper seasonMetricsMapper) {
        this.mapper = seasonMetricsMapper;
    }

    public void load(List<SeasonMetrics> seasonMetrics) {
        try (Connection connection = DaoHelper.connect("jdbc:mysql://" + databaseHostName + "/ncbca_reference?user=" + userName + "&password=" + password);
             PreparedStatement statement = connection.prepareStatement(LOAD_METRICS_SQL)) {;
            for (SeasonMetrics metrics : seasonMetrics) {
                statement.setInt(1, metrics.teamId());
                statement.setString(2, metrics.teamName());
                statement.setInt(3, metrics.season());
                statement.setDouble(4, metrics.rpi());
                statement.setDouble(5, metrics.sos());
                statement.setDouble(6, metrics.srs());
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load season metrics", e);
        }
    }

    public List<SeasonMetrics> listAllSeasonMetricsForSeason(Integer season) {
        try (Connection connection = DaoHelper.connect("jdbc:mysql://" + databaseHostName + "/ncbca_reference?user=" + userName + "&password=" + password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM season_metrics WHERE season = ?")) {
            statement.setInt(1, season);
            try (ResultSet resultSet = statement.executeQuery()) {
                return mapper.mapResult(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load season metrics", e);
        }
    }
}
