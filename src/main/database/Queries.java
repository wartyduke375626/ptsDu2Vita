package database;

import dataTypes.LineName;
import dataTypes.StopName;
import dataTypes.Time;
import dataTypes.TimeDiff;

public class Queries {

    public static String getStopLinesQuery(StopName stopName) {
        return "SELECT l.lname\n" +
                "FROM stop s, line l, stop_line sl\n" +
                "WHERE s.sname = '"+ stopName.toString() +"' AND s.sid = sl.sid AND l.lid = sl.lid";
    }

    public static String getLineFirstStopAndLidQuery(LineName lineName) {
        return "SELECT s.sname, l.lid\n" +
                "FROM line l, stop s\n" +
                "WHERE l.lname = '" + lineName.toString() + "' AND s.sid = l.firststop";
    }

    public static String getLineSegmentsDataQuery(int lid) {
        return "SELECT s.sname, ls.timeDiff, ls.sIndex\n" +
                "FROM lineSegment ls, stop s\n" +
                "WHERE ls.lid = " + lid + " AND s.sid = ls.nextStop";
    }

    public static String getBusesDataAndBidsQuery(LineName lineName, Time time, TimeDiff maxStartTimeDifference) {
        return "SELECT b.startTime, b.capacity, b.bid\n" +
                "FROM line l, bus b\n" +
                "WHERE l.lname = '" + lineName.toString() + "' AND b.lid = l.lid AND " +
                "b.startTime <= " + (time.getTime()+maxStartTimeDifference.getTime()) + " AND " +
                "b.startTime >= " + (time.getTime()-maxStartTimeDifference.getTime());
    }

    public static String getBusSegmentsPassengersQuery(int bid) {
        return "SELECT bs.passengers, ls.sIndex\n" +
                "FROM busSegment bs, lineSegment ls\n" +
                "WHERE bs.bid = " + bid + " AND ls.lsid = bs.lsid";
    }

}
