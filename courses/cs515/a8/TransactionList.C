

/* this class defines an ordered list of transactions.
 * Transactions may be inserted, based on the transaction
 * relational operators, and the list may be printed.
*/

#include <iostream>
using namespace std;

#include "Transaction.h"
#include "TransactionList.h"

        TransactionList::TransactionList()
            // constructs empty list
	{
		Transaction lowSent;
		Transaction highSent;
		head = new Elem;
		head -> back = 0;
		head -> info = lowSent.getLowSentinel();
		head -> next = new Elem;
		head -> next -> info = highSent.getHighSentinel();
		head -> next -> back = head;
		head -> next -> next = 0;
	}

        TransactionList::TransactionList(const TransactionList &v)
            // copy constructor
	{

	Elem * p1 = v.head;
	Elem * p2 = head;
	Elem * prev;
	if(v.head == 0)
	{
	head = 0;
	}
	else
	{
	head = new Elem;
	head -> info = v.head -> info;
	head -> back = 0;
	p1 = v.head -> next;
	prev = head;
	while(p1)
	{
	p2 = new Elem;
	p2 -> info = p1 -> info;
	p2 -> back = prev;
	p1 = p1 -> next;
	prev -> next = p2;
	prev = p2;
	}
	p2 -> next = 0;
	}

	}

        TransactionList::~TransactionList()
            // destructor
	{
		Elem * p = head;
		while(head != 0)
		{
		p = head;
		head = head -> next;
		delete p;
		}
		
	}

        TransactionList & TransactionList::operator = (const TransactionList &v)
            // assignment operator
	{
		//////////////destruct code///////////////
			if(this != &v)
		{
			Elem * p = head;
			while(head != 0)
			{
			p = head;
			head = head -> next;
			delete p;
		}
		//////////////////////////////////////////

		//////////////copy code///////////////////
			Elem * p1 = v.head;
			Elem * p2 = head;
			Elem * prev;
			if(v.head == 0)
			{
			head = 0;
			}
			else
			{
			head = new Elem;
			head -> info = v.head -> info;
			head -> back = 0;
			p1 = v.head -> next;
			prev = head;
			while(p1)
			{
			p2 = new Elem;
			p2 -> info = p1 -> info;
			p2 -> back = prev;
			p1 = p1 -> next;
			prev -> next = p2;
			prev = p2;
			}
			p2 -> next = 0;
			}
		///////////////////////////////////
			return (*this);
		}
		else
		{
			return (*this);
		}
	}

        void TransactionList::insert(const Transaction &v)
            // inserts transaction into list, ordered on
            // transaction.  Duplicates OK.
	{
		Elem * ptr = new Elem;
		ptr -> info = v;

		Elem * p = head -> next;
		
		while(p -> next != 0 && p -> info <= v)
		{
			p = p -> next;
		}
		if(p -> info > v)
		{
			ptr -> next = p;
			ptr -> back = p -> back;
			p -> back -> next = ptr;
			p -> back = ptr;	
		}
	}

        void TransactionList::output(ostream & s) const
            // outputs transactions, indented, one per line
	{
		Elem * p = head -> next;
		while(p -> next)
		{
			cout << "\t" << p->info << endl;
			p = p-> next;
		}
	}

	ostream & operator << (ostream &s, const TransactionList & v)
   	 // outputs using named operation
	{
		v.output(s);
		return s;
	}


