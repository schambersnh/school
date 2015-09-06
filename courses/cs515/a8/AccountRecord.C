
/* this class defines an record for a transponder account to be
 * used in the toll system.  Each record contains the account
 * id (a space delimited string) and the current set of transactions
 * made by the transponder(s) associated with the account.
 * Records may be compared (using the account number),
 * transactions added, and the record may be output.
*/

#include <iostream>
#include <string>
using namespace std;

#include "TransactionList.h"
#include "Transaction.h"
#include "AccountRecord.h"

        AccountRecord::AccountRecord()
            // constructs record with empty id, no transactions
	{
		acctId = "";
	}

        AccountRecord::AccountRecord(const string & id)
            // constructs record with specified id, no
            // transactions
	{
		acctId = id;
	}

        void AccountRecord::addTransaction(const Transaction &info)
            // adds transaction to account record
	{
		transactions.insert(info);
	}

        void AccountRecord::output(ostream &s) const
            // outputs id and all transactions
	{
		s << acctId << endl << transactions;
	}

	// equality and relational operators based on account
            // id
        bool AccountRecord::operator == (const AccountRecord &v) const
	{
		return acctId == v.acctId;
	}

        bool AccountRecord::operator != (const AccountRecord &v) const
	{
		return acctId != v.acctId;
	}

        bool AccountRecord::operator >= (const AccountRecord &v) const
	{
		return acctId >= v.acctId;
	}

        bool AccountRecord::operator > (const AccountRecord &v) const
	{
		return acctId > v.acctId;
	}

        bool AccountRecord::operator < (const AccountRecord &v) const
	{
		return acctId < v.acctId;
	}

        bool AccountRecord::operator <= (const AccountRecord &v) const
	{
		return acctId <= v.acctId;
	}
	
   
	ostream & operator << (ostream &s, const AccountRecord &v)
    	// outputs using named operation
	{
		v.output(s);
		return s;
	}


