(SELECT A.airline, A.flightnbr, A.segnbr, A.date
FROM departure AS A LEFT JOIN reservation AS B
ON A.airline=B.airline AND A.flightnbr=B.flightnbr AND A.segnbr=B.segnbr
WHERE B.airline IS NULL AND B.flightnbr IS NULL AND B.segnbr IS NULL
) UNION
(SELECT airline, flightnbr, segnbr, date 
 FROM reservation
 WHERE ssn IN (SELECT ssn FROM crew));


