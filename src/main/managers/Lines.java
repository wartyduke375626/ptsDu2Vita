package managers;

import components.LineInterface;
import components.LineSegmentInterface;
import dataTypes.*;
import dataTypes.tuples.Triplet;
import exceptions.IncorrectUserInputException;
import factories.FactoryInterface;

import java.sql.SQLException;
import java.util.*;

public class Lines implements LinesInterface {

    private final FactoryInterface factory;
    private Map<LineName, LineInterface> lines = new HashMap<>();

    public Lines(FactoryInterface factory) {
        this.factory = factory;
    }

    private void loadLine(LineName line, Time time) throws SQLException, IncorrectUserInputException {
        if (lines.containsKey(line)) throw new IllegalStateException("Line has already been loaded.");
        Optional<LineInterface> newLine = factory.createLine(line, time);
        if (newLine.isEmpty()) throw new IncorrectUserInputException("No such line in database.");
        lines.put(line, newLine.get());
    }

    @Override
    public void updateReachable(List<LineName> lines, StopName stop, Time time) throws SQLException, IncorrectUserInputException {
        for (LineName line : lines) {
            if (!this.lines.containsKey(line)) loadLine(line, time);
            this.lines.get(line).updateReachable(time, stop);
        }
    }

    @Override
    public Triplet<StopName, Time, TimeDiff> updateCapacityAndGetPreviousStop(LineName line, StopName stop, Time time) {
        if (!lines.containsKey(line)) throw new NoSuchElementException("Line has not been loaded yet.");
        return lines.get(line).updateCapacityAndGetPreviousStop(stop, time);
    }

    @Override
    public void saveUpdatedLineSegments() throws SQLException {
        List<LineSegmentInterface> modifiedLineSegments = new ArrayList<>();
        for (LineName line : lines.keySet()) {
            List<LineSegmentInterface> lineSegments = lines.get(line).getLineSegments();
            for (LineSegmentInterface lineSegment : lineSegments) {
                if (!lineSegment.getUpdatedBusses().isEmpty()) modifiedLineSegments.add(lineSegment);
            }
        }
        factory.updateDatabase(modifiedLineSegments);
    }

    @Override
    public void clean() {
        lines = new HashMap<>();
    }
}
