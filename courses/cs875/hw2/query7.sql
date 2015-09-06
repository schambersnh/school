SELECT DISTINCT ssn, name FROM crew NATURAL JOIN schedule S JOIN flightinfo F
ON S.airline=F.airline AND S.flightnbr=F.flightnbr AND S.segnbr=F.segnbr
WHERE origin='heathrow' OR dest='heathrow' AND job='pilot';
