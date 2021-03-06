package components;

import dataTypes.*;
import dataTypes.tuples.Pair;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Stop implements StopInterface {

    private final StopName stopName;
    private final List<LineName> lines;
    private Time reachableAt = new Time(Long.MAX_VALUE);
    private LineName reachableVia = null;

    public Stop(StopName stopName, List<LineName> lines) {
        this.stopName = new StopName(stopName);
        this.lines = Collections.unmodifiableList(lines);
    }

    @Override
    public void updateReachableAt(Time time, LineName line) {
        if (time == null) throw new IllegalArgumentException("Time cannot be null.");
        if (time.compareTo(reachableAt) < 0) {
            reachableAt = new Time(time);
            if (line == null) reachableVia = null;
            else reachableVia = new LineName(line);
        }
    }

    @Override
    public Pair<Time, Optional<LineName>> getReachableAt() {
        if (reachableVia == null) return new Pair<>(new Time(reachableAt), Optional.empty());
        else return new Pair<>(new Time(reachableAt), Optional.of(new LineName(reachableVia)));
    }

    @Override
    public StopName getStopName() {
        return new StopName(stopName);
    }

    @Override
    public List<LineName> getLines() {
        return lines;
    }
}
