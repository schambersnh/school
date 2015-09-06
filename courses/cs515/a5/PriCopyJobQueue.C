

/* this class defines a priority queue holding copy jobs.
 * Priorities are integers, the larger the integer the higher the
 * priority.  The oldest highest priority job is always at the
 * head of the queue.
*/

#include <iostream>
using namespace std;
#include "CopyJob.h"
#include "PriCopyJobQueue.h"


        PriCopyJobQueue::PriCopyJobQueue()
            // constructs empty queue
	{
		head = NULL;
	}

        PriCopyJobQueue::~PriCopyJobQueue()
            // destructor
	{
	destructCode();
	}

        PriCopyJobQueue::PriCopyJobQueue(const PriCopyJobQueue &v)
            // copy constructor
	{
	copyCode(v);
	}

        PriCopyJobQueue & PriCopyJobQueue::operator = (const PriCopyJobQueue &v)
            // assignment operator
	{
	if(this != &v)
	{

	(*this).destructCode();
	copyCode(v);
	return (*this);
	
	}
	else
	{
	return (*this);
	}
	}

        void PriCopyJobQueue::enqueue(int priority, const CopyJob &v)
            // inserts job onto queue according to priority
	{
	Elem * prev = 0;
	Elem * p = head;
	Elem *ptr;
	while(p!= 0 && priority <= p-> priority)
	{
	prev = p;
	p = p -> next;
	}
	ptr = new Elem;
	ptr -> info = v;
	ptr-> next = p;
	ptr ->priority = priority;
	if(prev == 0)
	{
	head = ptr;
	}
	else
	{
	prev -> next = ptr;
	}
	
	}
				
	

        bool PriCopyJobQueue::dequeue()
            // removes job at head of queue
            // success/fail -- returns true if non-empty, false
            // if empty
	{

	if(!(*this).empty())
	{
		Elem * p;
		p = head;
		head = head -> next;
		delete p;
		return true;
	}

	else
	{
		return false;
	}

	}

        bool PriCopyJobQueue::front(CopyJob &v) const
            // passes back job at head of queue
            // success/fail -- returns true if non-empty, false
            // if empty
	{
	if(!(*this).empty())
	{
	v = head -> info;
	return true;	
	}
	else
	{
	return false;
	}

	}

        bool PriCopyJobQueue::empty() const
            // returns true if empty queue, false otherwise
	{
	return head == 0;
	}

        void PriCopyJobQueue::output(ostream &s) const
            // outputs queue to specified stream
	{
	cout << "following jobs in queue\n";
	Elem * p = head;
	while(p!= 0)
	{
	cout << p-> priority << " -- " << p -> info << "\n";
	p = p->next;
	}
	}


        // note: implemented as ordered list based on priority,
        // non-ascending order, stable insertion (equal item
        // placed towards tail)

        void PriCopyJobQueue::copyCode(const PriCopyJobQueue & v)
            // copys v into object invoked on
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
	head -> priority = v.head -> priority;
	p1 = v.head -> next;
	prev = head;
	p2 = head -> next;
	while(p1)
	{
	p2 = new Elem;
	p2 -> info = p1 -> info;
	p2 -> priority = p1 -> priority;
	p1 = p1 -> next;
	prev -> next = p2;
	prev = p2;
	p2 = p2 -> next;
	}
	p2 = 0;
	}
	
	}

        void PriCopyJobQueue::destructCode()
            // deletes chain of object invoked on
	{
	while(!(*this).empty())
	{
	(*this).dequeue();
	}
	}



	ostream & operator << (ostream & s, const PriCopyJobQueue & v)
   	 // outputs using keyword operation
	{
	v.output(s);
	return s;
	}


