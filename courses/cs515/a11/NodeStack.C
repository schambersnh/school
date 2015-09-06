/* 
	Stephen Chambers Program 11
	This class defines a stack of Nodes.  Values
    may be pushed onto the stack and popped off.
    The top element may be accessed, and it may
    be determined if the stack is empty.  Stacks
    are constructed empty. */

#include <iostream>
#include "NodeStack.h"
using namespace std;


        NodeStack::NodeStack()
            // constructs empty stack 
	{
		head = 0;
	}

        NodeStack::NodeStack(const NodeStack &v)
            // copy constructor
	{
		copyCode(v);
	}

        NodeStack::~NodeStack()
            // destructor
	{
		destructCode();
	}

        NodeStack& NodeStack::operator =(const NodeStack &v)
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

		void NodeStack::push(Node * value) throw(Overflow) 
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

        void NodeStack::pop() throw(Underflow)
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

        Node * NodeStack::top() const throw(Underflow)
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

        bool NodeStack::empty() const
        // returns true if stack is empty
	{
		return head == 0;
	}

        void NodeStack::output(ostream &s) const
            // outputs elements of stack using recursion so that the
			//path is output in the correct order
	{
		output(head, s);
	}

        void NodeStack::copyCode(const NodeStack & v)
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

        void NodeStack::destructCode()
            // common code for destructor and assignment
	{
		while(!(*this).empty())
		{
			(*this).pop();
		}
	}
		void NodeStack::output(const Elem * p, ostream & s)
			//recursively outputs stack so stack is output in reverse order,
			//essential for making sure path is output correctly
		  {
			//base case
			if(p == 0)
			{
				return;
			}
			output(p -> next, s);
			string direction = "";
			
			if (p -> info -> getId() == 0)
			{
				s << "outside" << endl;
			}
			else
			{

			//convert direction to a 'string' direction
			if(p -> info -> getDirection() == 0)
			{
				direction = "north";
			}
			else if(p -> info -> getDirection() == 1)
			{
				direction = "east";
			}
			else if(p -> info -> getDirection() == 2)
			{
				direction = "south";
			}
			else if(p -> info -> getDirection() == 3)
			{
				direction = "west";
			}
			///////////////////////////////////////////

			s << "in " << p -> info -> getId() << " go " << direction << endl;
			
			}
		  }


	ostream & operator << (ostream & s, const NodeStack & v)
    	// outputs using keyword operation
	{
		v.output(s);
		return s;
	}


