package managers;

import dataTypes.*;

import java.util.List;

public interface LinesInterface {

    void updateReachable(List<LineName> lines, StopName stopName, Time time);

    StopName updateCapacityAndGetPreviousStop(LineName line, StopName stop, Time time);

    void clean();
}