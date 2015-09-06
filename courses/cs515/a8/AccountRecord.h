#ifndef ACCOUNTRECORD_H
#define ACCOUNTRECORD_H

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

class AccountRecord {

    public:

        AccountRecord();
            // constructs record with empty id, no transactions

        AccountRecord(const string & id);
            // constructs record with specified id, no
            // transactions

        void addTransaction(const Transaction &info);
            // adds transaction to account record

        void output(ostream &s) const;
            // outputs id and all transactions

        bool operator == (const AccountRecord &v) const;
        bool operator != (const AccountRecord &v) const;
        bool operator >= (const AccountRecord &v) const;
        bool operator > (const AccountRecord &v) const;
        bool operator < (const AccountRecord &v) const;
        bool operator <= (const AccountRecord &v) const;
            // equality and relational operators based on account
            // id

    private:
        string acctId;
        TransactionList transactions;

};

ostream & operator << (ostream &s, const AccountRecord &v);
    // outputs using named operation

#endif
