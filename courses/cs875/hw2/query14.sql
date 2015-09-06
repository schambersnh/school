SELECT ssn, sum(airmiles) AS airmiles
FROM passenger NATURAL JOIN reservation NATURAL JOIN flightinfo
WHERE ssn NOT IN (SELECT ssn FROM crew)
GROUP BY ssn
