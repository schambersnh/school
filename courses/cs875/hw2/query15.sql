SELECT P1.ssn, (sum(airmiles)+P1.miles) AS airmiles
FROM passenger P1 NATURAL JOIN reservation NATURAL JOIN flightinfo
WHERE ssn NOT IN(SELECT ssn FROM crew)
GROUP BY ssn
UNION
SELECT P2.ssn, miles FROM passenger P2
LEFT JOIN reservation R1 
ON P2.ssn=R1.ssn 
WHERE P2.ssn NOT IN(SELECT ssn FROM crew) AND R1.airline IS NULL AND R1.airline IS NULL AND R1.airline IS NULL;
