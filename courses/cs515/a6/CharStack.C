

/* this class defines a stack of characters.  Values
    may be pushed onto the stack and popped off.
    The top element may be accessed, and it may
    be determined if the stack is empty.  Stacks
    are constructed empty. */

#include <iostream>
#include "CharStack.h"
using namespace std;


        CharStack::CharStack()
            // constructs empty stack with a default
            // number of elements available
	{
		head = 0;
	}

        CharStack::CharStack(const CharStack &v)
            // copy constructor
	{
		copyCode(v);
	}

        CharStack::~CharStack()
            // destructor
	{
		destructCode();
	}

        CharStack& CharStack::operator =(const CharStack &v)
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

		void CharStack::push(char value) throw(Overflow) 
            // pushes value onto top of stack
            // throws exception if full
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
		p -> info = value;
		p -> next = head;
		head = p;
	}

        void CharStack::pop() throw(Underflow)
            // pops top value off stack
            // throws exception if empty
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
		}
	}

        char CharStack::top() const throw(Underflow)
            // returns top value on stack
            // throws exception if empty
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

        bool CharStack::empty() const
        // returns true if stack is empty
	{
		return head == 0;
	}

        void CharStack::output(ostream &s) const
            // outputs elements of stack
	{
		Elem * p = head;
		while(p!= 0)
		{
			s << p->info;
			p = p -> next;
		}
	}

        void CharStack::copyCode(const CharStack & v)
            // common code for copy constructor and assignment
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

        void CharStack::destructCode()
            // common code for destructor and assignment
	{
		while(!(*this).empty())
		{
			(*this).pop();
		}
	}


	ostream & operator << (ostream & s, const CharStack & v)
    	// outputs using keyword operation
	{
		v.output(s);
		return s;
	}


