package database;

import dataTypes.Time;
import dataTypes.tuples.Pair;
import dataTypes.*;
import dataTypes.tuples.Triplet;

import java.sql.*;
import java.util.*;

public class Database implements DatabaseInterface {

    private final String databaseUrl;

    public Database(String databasePath) {
        databaseUrl = "jdbc:sqlite:" + databasePath;
    }

    @Override
    public Optional<List<LineName>> getStopData(StopName stopName) throws SQLException {
        try (Connection connection = DriverManager.getConnection(databaseUrl)) {
            System.out.println("Connection to SQLite has been established.");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(Queries.getStopLinesQuery(stopName));

            List<LineName> stopLines = new ArrayList<>();
            while (resultSet.next()) {
                stopLines.add(new LineName(resultSet.getString("lname")));
            }
            if (stopLines.isEmpty()) return Optional.empty();

            return Optional.of(stopLines);
        }
    }

    @Override
    public Optional<Pair<StopName, List<Triplet<Integer, StopName, TimeDiff>>>> getLineFirstStopAndLineSegmentsData(LineName lineName) throws SQLException {
        try (Connection connection = DriverManager.getConnection(databaseUrl)) {
            System.out.println("Connection to SQLite has been established.");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(Queries.getLineFirstStopAndLidQuery(lineName));
            if (!resultSet.next()) return Optional.empty();

            StopName firstStop = new StopName(resultSet.getString("firstStop"));
            int lid = resultSet.getInt("lid");

            resultSet = statement.executeQuery(Queries.getLineSegmentsDataQuery(lid));

            List<Triplet<Integer, StopName, TimeDiff>> lineSegmentsData = new ArrayList<>();
            while (resultSet.next()) {
                int segmentIndex = resultSet.getInt("sIndex");
                StopName nextStop = new StopName(resultSet.getString("nextStop"));
                TimeDiff timeDiff = new TimeDiff(resultSet.getLong("timeDiff"));
                lineSegmentsData.add(new Triplet<>(segmentIndex, nextStop, timeDiff));
            }
            if (lineSegmentsData.isEmpty()) throw new SQLIntegrityConstraintViolationException("Line with no line segments in database.");

            return Optional.of(new Pair<>(firstStop, lineSegmentsData));
        }
    }

    @Override
    public Optional<Map<Time, Pair<Integer, List<Integer>>>> getBussesAndPassengers(LineName lineName, Time time) {
        return Optional.empty();
    }
}
