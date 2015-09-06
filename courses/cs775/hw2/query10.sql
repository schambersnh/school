CREATE VIEW smx227_reserved(ssn, airline, flightnbr, segnbr)
AS SELECT ssn, airline, flightnbr, segnbr FROM crew NATURAL JOIN reservation;

CREATE VIEW smx227_piloted(ssn2, airline, flightnbr, segnbr)
AS SELECT ssn, airline, flightnbr, segnbr FROM crew NATURAL JOIN schedule;

SELECT ssn, name FROM crew WHERE ssn IN(
SELECT ssn FROM smx227_reserved A JOIN smx227_piloted B
ON A.ssn=B.ssn2 AND A.airline=B.airline AND A.flightnbr=B.flightnbr AND A.segnbr=B.segnbr);

DROP VIEW smx227_reserved;
DROP VIEW smx227_piloted;
