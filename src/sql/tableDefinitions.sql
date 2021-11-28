PRAGMA foreign_keys = ON;

DROP TABLE IF EXISTS busSegment;
DROP TABLE IF EXISTS bus;
DROP TABLE IF EXISTS stop_line;
DROP TABLE IF EXISTS lineSegment;
DROP TABLE IF EXISTS line;
DROP TABLE IF EXISTS stop;

CREATE TABLE stop(
	sid INTEGER PRIMARY KEY,
	sname VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE line(
	lid INTEGER PRIMARY KEY,
	lname VARCHAR(50) UNIQUE NOT NULL,
	firstStop INTEGER REFERENCES stop(sid) ON DELETE RESTRICT ON UPDATE CASCADE DEFERRABLE INITIALLY IMMEDIATE
);

CREATE TABLE lineSegment(
	lsid INTEGER PRIMARY KEY,
	lid INTEGER NOT NULL REFERENCES line(lid) ON DELETE RESTRICT ON UPDATE CASCADE DEFERRABLE INITIALLY IMMEDIATE,
	sIndex INTEGER NOT NULL,
	timeDiff INTEGER NOT NULL,
	nextStop INTEGER REFERENCES stop(sid) ON DELETE RESTRICT ON UPDATE CASCADE DEFERRABLE INITIALLY IMMEDIATE,
	UNIQUE (lid, sIndex),
	CHECK (timeDiff > 0)
);

CREATE TABLE stop_line(
	sid INTEGER NOT NULL REFERENCES stop(sid) ON DELETE RESTRICT ON UPDATE CASCADE DEFERRABLE INITIALLY IMMEDIATE,
	lid INTEGER NOT NULL REFERENCES stop(sid) ON DELETE RESTRICT ON UPDATE CASCADE DEFERRABLE INITIALLY IMMEDIATE
);

CREATE TABLE bus(
	bid INTEGER PRIMARY KEY,
	lid INTEGER NOT NULL REFERENCES line(lid) ON DELETE RESTRICT ON UPDATE CASCADE DEFERRABLE INITIALLY IMMEDIATE,
	startTime INTEGER NOT NULL,
	capacity INTEGER NOT NULL,
	UNIQUE (bid, capacity),
	UNIQUE (lid, startTime),
	CHECK (startTime >= 0 AND capacity > 0)
);

CREATE TABLE busSegment(
	bid INTEGER NOT NULL,
	lsid INTEGER NOT NULL REFERENCES lineSegment(lsid) ON DELETE RESTRICT ON UPDATE CASCADE DEFERRABLE INITIALLY IMMEDIATE,
	capacity INTEGER NOT NULL,
	passengers INTEGER NOT NULL,
	PRIMARY KEY (bid, lsid),
	FOREIGN KEY (bid, capacity) REFERENCES bus(bid, capacity) ON DELETE RESTRICT ON UPDATE CASCADE DEFERRABLE INITIALLY IMMEDIATE,
	CHECK (passengers >= 0 AND passengers <= capacity)
);