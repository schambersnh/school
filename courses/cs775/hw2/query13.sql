SELECT P1.ssn, P2.ssn FROM passenger P1, passenger P2
WHERE P1.ssn NOT IN(SELECT ssn FROM crew) AND P2.ssn NOT IN(SELECT ssn FROM crew)
AND NOT EXISTS(
        SELECT * FROM reservation R2 WHERE ssn=P2.ssn
        AND EXISTS(SELECT * FROM reservation R1 
                    WHERE ssn=P1.ssn AND R1.airline=R2.airline AND R1.segnbr=R2.segnbr 
                    AND R1.date=R2.date)
    ) AND P1.ssn != P2.ssn 
