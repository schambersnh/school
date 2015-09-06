#ifndef TRANSACTIONLIST_H
#define TRANSACTIONLIST_H

/* this class defines an ordered list of transactions.
 * Transactions may be inserted, based on the transaction
 * relational operators, and the list may be printed.
*/

#include <iostream>
using namespace std;

#include "Transaction.h"

class TransactionList {

    public:

        TransactionList();
            // constructs empty list

        TransactionList(const TransactionList &v);
            // copy constructor

        ~TransactionList();
            // destructor

        TransactionList & operator = (const TransactionList &v);
            // assignment operator

        void insert(const Transaction &v);
            // inserts transaction into list, ordered on
            // transaction.  Duplicates OK.

        void output(ostream & s) const;
            // outputs transactions, indented, one per line

    private:

        // implementation note: doubly linked chain, with both
        // head and tail sentinel

        struct Elem {
            Transaction info;
            Elem *next;  // towards tail
            Elem *back;  // towards head
        };

        Elem *head;
            
        // implementation note: these should be implemented and 
        // then used in destructor, copy constructor, and assignment
        // operator.

        static void deleteAll(Elem *p);
            // deletes all elements in chain

        static void copy(Elem *&newHead, const Elem *oldHead);
            // copys chain at oldHead to newHead.
};

ostream & operator << (ostream &s, const TransactionList & v);
    // outputs using named operation

#endif
