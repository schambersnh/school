#ifndef ACCOUNTDB_H
#define ACCOUNTDB_H

/* this class provides a binary search tree of account records.
 * Records are compared using the relational operators provided
 * by the Account Record class.  Records may be inserted and
 * found, and the tree may be printed (in-order traversal).
*/

#include <iostream>
using namespace std;

#include "AccountRecord.h"

class AccountDB {


    public:
        AccountDB();
            // constructs empty tree

        ~AccountDB();
            // destructor

        bool insert(const AccountRecord &v);
            // inserts record into tree.  Success/fail, fails if
            // v is already in tree

        AccountRecord * find(const AccountRecord &v);
            // tries to find matching record in tree.  If found,
            // pointer to record is returned, otherwise null is
            // returned.  Record found may be modified through
            // pointer.  Used when tree is non-const

        const AccountRecord * find(const AccountRecord &v) const;
            // tries to find matching record in tree.  If found,
            // pointer to record is returned, otherwise null is
            // returned.  Record found may NOT be modified
            // through pointer.  Used when tree is const.

        void output(ostream &s) const;
            // outputs tree in ascending order.

    private:
        AccountDB(const AccountDB &v);
        AccountDB& operator =(const AccountDB &v);
            // copy constructor, assignment operator 
            // -- not implemented

            // implementation note: find is iterative
            // (non-recursive).  There are two versions of find, 
            // one which allows mutation of record found, and one
            // which does not.  The code for the two are
            // essentially identical

            // implementation note: insert can be done
            // non-recursively, or can be done recursively using
            // static work function provided below

            // implementation note: output needs to be done
            // recursively, using the static work function
            // provided below

            // implementation note: destruction needs to call
            // recursive procedure to destruct tree, using static
            // function provided below

        struct Node {
            AccountRecord info;
            Node *left;
            Node *right;
        };

        Node * root;

        static bool insert(const AccountRecord &v, Node *&p);
            // class function, internal use -- inserts record
            // into tree.  Fails if v in tree

        static void output(ostream &s, const Node *p);
            // class function, internal use -- outputs tree
            // information to stream in inorder traversal order

        static void deleteAll(Node *p);
            // class function, recursively deletes all nodes in
            // tree

};

ostream & operator << (ostream &s, const AccountDB &v);
    // outputs using keyword operation

#endif
