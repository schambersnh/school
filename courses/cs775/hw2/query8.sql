SELECT  airline, flightnbr, MAX(segnbr), origin, dest, etd, eta, airmiles, segnbr FROM flightinfo
WHERE dest='heathrow'
GROUP BY airline, flightnbr
