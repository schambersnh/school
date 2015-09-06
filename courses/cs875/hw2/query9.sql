SELECT ssn, name FROM crew C 
WHERE NOT EXISTS(
SELECT * FROM crew NATURAL JOIN reservation NATURAL JOIN flightinfo WHERE ssn=C.ssn AND (origin='ohare' OR dest='ohare')
UNION
SELECT * FROM crew NATURAL JOIN schedule NATURAL JOIN flightinfo WHERE ssn=C.ssn AND (origin='ohare' OR dest='ohare'));

