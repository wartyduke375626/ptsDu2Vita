package managers;

import components.LineInterface;
import dataTypes.*;
import factories.FactoryInterface;

import java.util.*;

public class Lines implements LinesInterface {

    private final FactoryInterface factory;
    private Map<LineName, LineInterface> lines = new HashMap<>();

    public Lines(FactoryInterface factory) {
        this.factory = factory;
    }

    private void loadLine(LineName line, Time time) {
        if (lines.containsKey(line)) throw new IllegalStateException("Line has already been loaded.");
        Optional<LineInterface> newLine = factory.createLine(line, time);
        if (newLine.isEmpty()) throw new NoSuchElementException("No such line in database.");
        lines.put(line, newLine.get());
    }

    @Override
    public void updateReachable(List<LineName> lines, StopName stop, Time time) {
        for (LineName line : lines) {
            if (!this.lines.containsKey(line)) loadLine(line, time);
            this.lines.get(line).updateReachable(time, stop);
        }
    }

    @Override
    public StopName updateCapacityAndGetPreviousStop(LineName line, StopName stop, Time time) {
        if (!lines.containsKey(line)) throw new NoSuchElementException("Line has not been loaded yet.");
        return lines.get(line).updateCapacityAndGetPreviousStop(stop, time);
    }

    @Override
    public void clean() {
        lines = new HashMap<>();
    }
}