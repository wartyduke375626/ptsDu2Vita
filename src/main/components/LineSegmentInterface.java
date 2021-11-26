package components;

import dataTypes.*;
import dataTypes.tuples.Pair;
import dataTypes.tuples.Triplet;

public interface LineSegmentInterface {

    Pair<Time, StopName> nextStop(Time startTime);

    Triplet<Time, StopName, Boolean> nextStopAndUpdateReachable(Time startTime);

    void incrementCapacity(Time startTime);
}
