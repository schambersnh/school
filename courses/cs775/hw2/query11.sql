SELECT DISTINCT P.ssn, P.name, R.airline, R.flightnbr, R.date
FROM passenger P, 
	(SELECT ssn, airline, flightnbr, date, COUNT(*) AS scount
     FROM reservation 
     GROUP BY ssn, airline, flightnbr, date) R,
    (SELECT airline, flightnbr, date, COUNT(*) AS scount
     FROM departure
     GROUP BY airline, flightnbr, date) D
WHERE
    R.airline=D.airline AND
    R.flightnbr=D.flightnbr AND
    R.scount=D.scount AND
    R.ssn=P.ssn
