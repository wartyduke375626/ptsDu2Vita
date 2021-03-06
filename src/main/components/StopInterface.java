package components;

import dataTypes.*;
import dataTypes.tuples.Pair;
import exceptions.IncorrectUserInputException;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface StopInterface {

    void updateReachableAt(Time time, LineName line) throws SQLException, IncorrectUserInputException;

    Pair<Time, Optional<LineName>> getReachableAt();

    StopName getStopName();

    List<LineName> getLines() throws SQLException, IncorrectUserInputException;
}
