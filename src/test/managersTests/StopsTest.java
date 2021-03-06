package managersTests;

import components.LineInterface;
import components.LineSegmentInterface;
import components.StopInterface;
import dataTypes.*;

import dataTypes.tuples.Pair;
import exceptions.IncorrectUserInputException;
import factories.FactoryInterface;
import managers.Stops;

import managers.StopsInterface;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.*;

import static org.junit.Assert.*;

public class StopsTest {

    private FactoryInterface factory;
    private Stops stops;
    private final Map<StopName, Time> factoryStops = Map.of(
            new StopName("Stop A"), new Time(10),
            new StopName("Stop B"), new Time(20),
            new StopName("Stop C"), new Time(30)
    );

    @Before
    public void setUp() {
        factory = new FactoryInterface() {
            @Override
            public void setStops(StopsInterface stops) {
            }

            @Override
            public Optional<StopInterface> createStop(StopName stopName) {
                if (!factoryStops.containsKey(stopName)) return Optional.empty();
                return Optional.of(new StopInterface() {
                    @Override
                    public void updateReachableAt(Time time, LineName line) {

                    }

                    @Override
                    public Pair<Time, Optional<LineName>> getReachableAt() {
                        return new Pair<>(factoryStops.get(stopName), Optional.empty());
                    }

                    @Override
                    public StopName getStopName() {
                        return stopName;
                    }

                    @Override
                    public List<LineName> getLines() {
                        return null;
                    }
                });
            }

            @Override
            public Optional<LineInterface> createLine(LineName lineName, Time time) {
                return Optional.empty();
            }

            @Override
            public void updateDatabase(List<LineSegmentInterface> lineSegments) {

            }
        };
        stops = new Stops(factory);
    }

    @Test
    public void earliestReachableStopAfterTest() throws SQLException, IncorrectUserInputException {
        assertTrue(stops.earliestReachableStopAfter(new Time(10)).isEmpty());
        stops.loadStop(new StopName("Stop A"));
        stops.loadStop(new StopName("Stop B"));

        Optional<Pair<List<StopName>, Time>> tmp = stops.earliestReachableStopAfter(new Time(5));
        assertTrue(tmp.isPresent());
        Pair<List<StopName>, Time> data = tmp.get();
        assertEquals(data.getFirst().get(0), new StopName("Stop A"));
        assertEquals(data.getSecond(), new Time(10));

        tmp = stops.earliestReachableStopAfter(new Time(10));
        assertTrue(tmp.isPresent());
        data = tmp.get();
        assertEquals(data.getFirst().get(0), new StopName("Stop B"));
        assertEquals(data.getSecond(), new Time(20));

        assertTrue(stops.earliestReachableStopAfter(new Time(22)).isEmpty());
        stops.loadStop(new StopName("Stop C"));

        tmp = stops.earliestReachableStopAfter(new Time(22));
        assertTrue(tmp.isPresent());
        data = tmp.get();
        assertEquals(data.getFirst().get(0), new StopName("Stop C"));
        assertEquals(data.getSecond(), new Time(30));

        assertThrows(IncorrectUserInputException.class, () -> stops.loadStop(new StopName("Stop D")));
        assertThrows(NoSuchElementException.class, () -> stops.getStop(new StopName("Stop D")));
    }
}
