#ifndef TRANSACTION_H
#define TRANSACTION_H

/* this class defines an account transaction on an entry-exit
 * gate toll highway for cars equipped with transponders.  Each
 * transaction has: a string representing the highway; an entry
 * gate number and entry time; an exit gate number and an
 * exit time; and an amount of toll which is to be charged to the
 * account.
 *
 * transactions may be input, output, and compared.  Comparisons
 * are based on exit time.
 *
 * input/output of transaction information is in the following
 * order
 *
 *   tollAmount highway entryTime entryGate exitTime exitGate
 *
 * where: the toll amount is a real; the highway is a space
 * delimited string; the times are space delimited strings; and
 * the gates are integers 
 *
 * time is always yyyymmddhhmm  (year month day hour minute)
*/

#include <iostream>
#include <string>
using namespace std;

class Transaction {

    public:
        Transaction();
            // constructs empty transaction

        void input(istream &s);
            // inputs transaction in order shown above

        void output(ostream &s) const;
            // outputs transaction in order shown above
    
        static Transaction getLowSentinel();
            // returns impossibly small (for relational
            // operators) transaction

        static Transaction getHighSentinel();
            // returns impossibly large (for relational
            // operators) transaction

        bool operator == (const Transaction &v) const;
        bool operator != (const Transaction &v) const;
        bool operator >= (const Transaction &v) const;
        bool operator > (const Transaction &v) const;
        bool operator < (const Transaction &v) const;
        bool operator <= (const Transaction &v) const;
            // relational and equality operators based on
            // datetime value

    private:
        float tollAmount;
        string highway;  
            // implementation note: exact fit C-style string
        string entryTime;
        int entryGate;
        string exitTime;
        int exitGate;

};

istream & operator >> (istream &s, Transaction &v);
    // inputs transaction in order specified above 

ostream & operator << (ostream &s, const Transaction &v);
    // outputs transaction in same order as input

#endif
