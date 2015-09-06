/*Stephen Chambers
Brian Johnson
Program 7
11/10/11
This program uses two unique data structures, Binary Search Trees, and Doubly-Linked Chains.
The program takes in simulated transponder information being sent from toll booths/gates. The
information comes in at random, and is ordered on 'exitTime'. The transactions recieved from the
aforementioned toll booths/gates are stored in a doubly-linked chain, and these chains are part of 
an AccountRecord, which wholds a chain of transactions and a unique ID name. These account records are
then stored on a Binary Search Tree, and the tree is printed after all the information has been collected.
*/

#include <iostream>
using namespace std;
#include "TransactionList.h"
#include "Transaction.h"
#include "AccountRecord.h"
#include "AccountDB.h"
#include <string.h>

/*main program, creates a tree, takes in the account id and transaction, and determines whether or not
the record is in the tree already. If it is, simply update the transaction, if its not, put it into the 
tree and update the transaction. Output tree at the end of the program.*/
int main()
{
	AccountDB tree;
	string accountID;
	Transaction t;
	cin >> accountID;
	cin >> t;
	while(cin)
	{
		AccountRecord * p;
		p = tree.find(accountID);
		if(p == 0)
		{
			AccountRecord temp = AccountRecord(accountID);
			temp.addTransaction(t);
			tree.insert(temp);
		}
		else
		{
			p -> addTransaction(t);
		}
		cin >> accountID;
		cin >> t;
	}
	cout << tree;
	return 0;
}
