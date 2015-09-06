
/* 
this class defines a queue of characters.  It has the normal
 * operations (enqueue, dequeue, empty, front) and the characters
 * in the queue can be printed non-destructively.
*/

#include <iostream>
#include "CharQueue.h"
using namespace std;

         
            

        CharQueue::CharQueue()
            // constructs empty queue
	{
		head = 0;
		tail = 0;
	}

        CharQueue::CharQueue(const CharQueue &v)
            // copy constructor
	{
		copyCode(v);
	}

        CharQueue::~CharQueue()
            // destructor
	{
		destructCode();
	}

        CharQueue & CharQueue::operator = (const CharQueue &v)
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

		void CharQueue::enqueue(char v) throw(Overflow)
            // inserts character onto queue -- throws exception if
            // queue if full
	{

		Elem * p;
		try
		{
		p = new Elem;
		}
		catch (bad_alloc)
		{
			throw Overflow();
		}
		p -> info = v;
		p -> next = 0;
		if(head == 0)
		{
			head = p;
		}
		else
		{
			tail -> next = p;
		}
		tail = p;
		count ++;
	}

        void CharQueue::dequeue() throw(Underflow)
            // removes front character -- throws exception if queue
            // is empty
	{
		if((*this).empty())
		{
			throw Underflow();
		}
		else
		{
			Elem * p = head;
			head = head -> next;
			delete p;
			count --;
		}
		

	}

        char CharQueue::front() const throw(Underflow)
            // returns character at front of queue -- throws
            // exception if queue is empty
	{
		if((*this).empty())
		{
			throw Underflow();
		}
		else
		{
			return head -> info;
		}
	}

        bool CharQueue::empty() const
            // predicate -- returns true if empty
	{
		return head == 0;
	}

        int CharQueue::length() const
            // returns number of values stored in queue
	{
		return count;
	}

        void CharQueue::output(ostream &s) const
            // non-destructively outputs characters on queue
	{
		Elem * p = head;
		while(p!= 0)
		{
			s << p->info;
			p = p -> next;
		}
	}

        void CharQueue::copyCode(const CharQueue &v)
            // copies queue, without deleting original
            // common code copy constructor and assignment op
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
	p1 = v.head -> next;
	prev = head;
	p2 = head -> next;
	while(p1)
	{
	p2 = new Elem;
	p2 -> info = p1 -> info;
	p1 = p1 -> next;
	prev -> next = p2;
	prev = p2;
	p2 = p2 -> next;
	}
	p2 = 0;
	}
	
	}

	

        void CharQueue::destructCode()
            // deletes all elements of queue
            // common code destructor and assignment op
	{
	while(!(*this).empty())
		{
			(*this).dequeue();
		}
	}



	ostream & operator << (ostream &s, const CharQueue &v)
    	// non-destuctively outputs contents of queue
	{
		v.output(s);
		return s;
	}


