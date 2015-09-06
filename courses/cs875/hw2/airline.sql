-- CS775/875 - Spring 2015
-- Assignment #2
-- Test Database - SQL Source File

-- Modify the database name to your username.
-- USE airline;
USE smx227;


CREATE TABLE flightinfo
(
  
   airline	CHAR(8),
   flightnbr	INT,
   segnbr	INT,
   origin	CHAR(10),
   dest	  	CHAR(10),
   etd		INT,
   eta		INT,
   airmiles	INT,

   PRIMARY KEY (airline, flightnbr, segnbr)
);

INSERT INTO flightinfo VALUES
('aa',73,1,'ohare','logan',9,11,100),
('af',99,1,'lax','ohare',7,12,500),
('af',99,2,'ohare','logan',12,14,100),
('af',99,3,'logan','heathrow',15,22,1000),
('da',120,1,'jfk','orlando',6,8,200);


CREATE TABLE departure
(
   airline	CHAR(8),
   flightnbr	INT,
   segnbr	INT,
   date		DATE,
   tailnbr	CHAR(15),

   PRIMARY KEY (airline, flightnbr, segnbr, date),
   FOREIGN KEY (airline, flightnbr, segnbr) REFERENCES flightinfo (airline, flightnbr, segnbr)
);

INSERT INTO departure VALUES
('aa',73,1,'2012-11-19','XYZ123'),
('af',99,1,'2012-10-30','ABC123'),
('af',99,2,'2012-10-30','ABC123'),
('af',99,3,'2012-10-30','ABC123'),
('af',99,1,'2012-11-02','DEF456'),
('af',99,2,'2012-11-02','DEF456'),
('af',99,3,'2012-11-02','DEF456'),
('da',120,1,'2012-12-01','GHI789'),
('da',120,1,'2012-12-02','GHI789'),
('da',120,1,'2012-12-03','GHI789'),
('da',120,1,'2012-12-04','GHI789'),
('da',120,1,'2012-12-05','GHI789');

CREATE TABLE passenger 
(
   ssn		INT,
   name		CHAR(30),
   phone	CHAR(10),
   miles	INT,

   PRIMARY KEY (ssn)
);

INSERT INTO passenger VALUES
(111,'john','8675309',1000),
(333,'amy','5551234',10);

CREATE TABLE crew
(
   ssn		INT,
   name		CHAR(30),
   phone	CHAR(10),
   job		CHAR(16),

   PRIMARY KEY (ssn)
);

INSERT INTO crew VALUES
(111,'john','8675309','pilot'),
(222,'mary','3273931','pilot'),
(444,'mark','4521879','flight attendant');

CREATE TABLE reservation
(
   ssn		INT,
   airline	CHAR(8),
   flightnbr	INT,
   segnbr	INT,
   date		DATE,
   seat		CHAR(4),

   PRIMARY KEY (ssn, airline, flightnbr, segnbr, date),
   FOREIGN KEY (ssn) REFERENCES passenger (ssn),
   FOREIGN KEY (airline, flightnbr, segnbr, date) REFERENCES departure (airline, flightnbr, segnbr, date)
);

INSERT INTO reservation VALUES
(111,'aa',73,1,'2012-11-19','2F'),
(333,'af',99,1,'2012-10-30','1A'),
(333,'af',99,2,'2012-10-30','1A'),
(333,'af',99,3,'2012-10-30','1A');

CREATE TABLE schedule
(
   ssn		INT,
   airline	CHAR(8),
   flightnbr	INT,
   segnbr	INT,
   date		DATE,

   PRIMARY KEY (ssn, airline, flightnbr, segnbr, date),
   FOREIGN KEY (ssn) REFERENCES crew (ssn),
   FOREIGN KEY (airline, flightnbr, segnbr, date) REFERENCES departure (airline, flightnbr, segnbr, date)
);

INSERT INTO schedule VALUES
(222,'aa',73,1,'2012-11-19'),
(111,'af',99,1,'2012-10-30'),
(111,'af',99,2,'2012-10-30'),
(111,'af',99,3,'2012-11-02'),
(222,'af',99,1,'2012-11-02'),
(222,'af',99,2,'2012-11-02'),
(222,'af',99,3,'2012-11-02'),
(111,'da',120,1,'2012-12-01'),
(111,'da',120,1,'2012-12-02'),
(111,'da',120,1,'2012-12-03'),
(111,'da',120,1,'2012-12-04'),
(111,'da',120,1,'2012-12-05'),
(444,'da',120,1,'2012-12-01'),
(444,'da',120,1,'2012-12-02'),
(444,'da',120,1,'2012-12-03'),
(444,'da',120,1,'2012-12-04'),
(444,'da',120,1,'2012-12-05');

SELECT * FROM flightinfo;
SELECT * FROM departure;
SELECT * FROM passenger;
SELECT * FROM crew;
SELECT * FROM reservation;
SELECT * FROM schedule;

