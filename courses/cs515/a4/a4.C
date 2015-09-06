/*
Stephen Chambers
Professor Johnson
10/6/11
This program is a batch style program that takes in workers and whether they picked up apples
from a tree or from the ground. There are different pay rates for these two scenarios, and 
the pay is calculated at the end of the program. Each worker is stored in an unordered chain.
*/

#include <iostream>
#include "DryMeasure.h"
#include <string>
#include "Worker.h"
#include "chain.h"
using namespace std;

int main()
{
	float treeRate;
	float dropRate;
	char ch;
	DryMeasure picked;
	string name;

	cin >> treeRate >> dropRate;
	

	Elem * head = NULL;
	Elem * p;
	
	cout << "rate for apples picked from tree: " << treeRate << "\nrate for drops picked up: " << dropRate << "\n";
	cin >> name;
	/*If worker is not in chain, add worker to head. If worker is in chain,
		update info of current worker in chain*/
	while(cin)
	{
	Worker work = Worker(name);
	cin >> ch >> picked;
	

	//set p to start of chain 
	p = head;
	
	//keep traversing until p is found or end of chain is reached
	while(p != 0 && p->info != work)
	{
		p = p->next;
	}
	if(p != 0)
	{
	//match, update worker
	p->info.update(ch, picked);
	cout << name << " -- " << ch << " -- " << picked << "\n";
	}
	else
	{
	//no match, add worker to head
	Elem * ptr = new Elem;
	ptr->info = work;
	ptr->next = head;
	head = ptr;
	ptr->info.update(ch, picked);
	cout << name << " -- " << ch << " -- " << picked << "\n";
	}
	cin >> name;	
	}
	
	/*Traverse and output each worker in the chain while deallocating*/
	cout << "\n--------------------------------------\n\n";
	p = head;
	while(p!= 0)
	{
		cout << p->info;
		cout << "\n" << "pay: " << p->info.getPay(treeRate, dropRate) << "\n";
		Elem * prev = p;
		p = p->next;
		delete prev;
	}
	return 0;
}


	
	
