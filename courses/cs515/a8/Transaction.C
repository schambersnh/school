

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
#include "Transaction.h"
using namespace std;

        Transaction::Transaction()
            // constructs empty transaction
	{
		float tollAmount = 0.0;
        string highway = "";  
        string entryTime = "";
        int entryGate = 0;
        string exitTime = "";
        int exitGate = 0;
	}

        void Transaction::input(istream &s)
            // inputs transaction in order shown above
	{
		s >> tollAmount >> highway >> entryTime >> entryGate >> exitTime >> exitGate;
	}

        void Transaction::output(ostream &s) const
            // outputs transaction in order shown above
	{
		s << tollAmount << " " << highway << " " << entryTime << " " << entryGate << " " << exitTime << " " << exitGate;
	}
    
        Transaction Transaction::getLowSentinel()
            // returns impossibly small (for relational
            // operators) transaction
	{
		Transaction temp;
		temp.exitTime = "000000000000";
		return temp;
	}

        Transaction Transaction::getHighSentinel()
            // returns impossibly large (for relational
            // operators) transaction
	{
		Transaction temp;
		temp.exitTime = "999999999999";
		return temp;
	}

	// relational and equality operators based on
            // datetime value
        bool Transaction::operator == (const Transaction &v) const
	{
		return exitTime == v.exitTime;
	}

        bool Transaction::operator != (const Transaction &v) const
	{
		return exitTime != v.exitTime;
	}

        bool Transaction::operator >= (const Transaction &v) const
	{
		return exitTime >= v.exitTime;
	}

        bool Transaction::operator > (const Transaction &v) const
	{
		return exitTime > v.exitTime;
	}

        bool Transaction::operator < (const Transaction &v) const
	{
		return exitTime < v.exitTime;
	}

        bool Transaction::operator <= (const Transaction &v) const
	{
		return exitTime <= v.exitTime;
	}
            

	istream & operator >> (istream &s, Transaction &v)
    	// inputs transaction in order specified above 
	{
		v.input(s);
		return s;
	}

	ostream & operator << (ostream &s, const Transaction &v)
    	// outputs transaction in same order as input
	{
		v.output(s);
		return s;
	}


