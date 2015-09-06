

/* this class provides a binary search tree of account records.
 * Records are compared using the relational operators provided
 * by the Account Record class.  Records may be inserted and
 * found, and the tree may be printed (in-order traversal).
*/

#include <iostream>
using namespace std;

#include "AccountRecord.h"
#include "AccountDB.h"

        AccountDB::AccountDB()
            // constructs empty tree
	{
		root = 0;
	}

        AccountDB::~AccountDB()
            // destructor
	{
		deleteAll(root);
	}
        bool AccountDB::insert(const AccountRecord &v)
            // inserts record into tree.  Success/fail, fails if
            // v is already in tree
	{
		insert(v, root);
	}
        AccountRecord * AccountDB::find(const AccountRecord &v)
            // tries to find matching record in tree.  If found,
            // pointer to record is returned, otherwise null is
            // returned.  Record found may be modified through
            // pointer.  Used when tree is non-const
	{
		Node * r = root;
		while (r && r -> info != v)
		{
			if (r -> info > v)
			{
				r = r -> left;
			}
			else
			{
				r = r -> right;
			}
		}
		if(r != 0)
		{
			return &r -> info;
		}
		else
		{
			return NULL;
		}
	}

        const AccountRecord * AccountDB::find(const AccountRecord &v) const
            // tries to find matching record in tree.  If found,
            // pointer to record is returned, otherwise null is
            // returned.  Record found may NOT be modified
            // through pointer.  Used when tree is const.
	{
			Node * r = root;
			while (r && r -> info != v)
		{
			if (r -> info > v)
			{
				r = r -> left;
			}
			else
			{
				r = r -> right;
			}
		}
			if(r != 0)
			{
				return &r -> info;
			}
			else
			{
				return NULL;
			}
	}
        void AccountDB::output(ostream &s) const
            // outputs tree in ascending order.
	{
		output(s, root);
	}
		
         bool AccountDB::insert(const AccountRecord &v, Node *&p)
            // class function, internal use -- inserts record
            // into tree.  Fails if v in tree
	{
		if(p == 0)
		{
			p = new Node;
			p -> info = v;
			p -> left = p -> right = 0;
		}
		else if(p -> info > v)
		{
			insert(v, p -> left);
		}
		else
		{
			insert(v, p -> right);
		}
	}

         void AccountDB::output(ostream &s, const Node *p)
            // class function, internal use -- outputs tree
            // information to stream in inorder traversal order
	{
		if(p != 0)
		{
			output(s, p -> left);
			s << p -> info;
			output(s, p-> right);
		}
	}

         void AccountDB::deleteAll(Node *p)
            // class function, recursively deletes all nodes in
            // tree
	{
		if(p != 0)
		{
		deleteAll(p->left);
		deleteAll(p->right);
		delete p;
		}
		
	}



	ostream & operator << (ostream &s, const AccountDB &v)
   	// outputs using keyword operation
	{
		 v.output(s);
		 return s;
	}


