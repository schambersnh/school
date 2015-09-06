
/* this class defines a stack of environments 
    the normal operations of push, pop, empty and top are
    available.   Maximum size allowed for stack is specified on
    construction.
*/

#include "Environment.h"
#include "EnvStack.h"


        EnvStack::EnvStack(int n)
            // constructs empty stack with maximum of n items
            // if no argument is supplied then the default value
            // will be used
	{
		topIndex = -1;
		maxElem = n;
		if(n <= 20)
		{
			store = new Environment[n];
		}
		else
		{
			throw Overflow();
		}
			
	}

        EnvStack::~EnvStack()
            // destructor
	{
		delete []store;
	}

        void EnvStack::push(const Environment &v) throw(Overflow)
            // makes v new top element, pushing existing elements
            // down -- throws exception if no room left on stack
	{
		if(topIndex < maxElem - 1)
		{
			topIndex++;
			store[topIndex] = v;
		}
	}

        void EnvStack::pop() throw(Underflow)
            // removes top element.  Throws exception if stack
            // was empty 
	{
		if((*this).empty())
		{
			throw Underflow();
		}
		else
		{
			topIndex --;
		}
	}

        Environment & EnvStack::top() throw(Underflow)
            // returns reference to top element of stack.  Throws
            // exception if stack was empty
	{
		if((*this).empty())
		{
			throw Underflow();
		}
		else
		{
			return store[topIndex];
		}
	}

        bool EnvStack::empty() const
            // returns true if stack is empty
	{
		return topIndex == -1;
	}

        EnvStack::EnvStack(const EnvStack &v)
            // copy constructor - not implemented
	{
		
		int i;
		for(i = 0; i < topIndex; i++)
		{
			store[i] = v.store[i];
		}
		
	}
        EnvStack& EnvStack::operator = (const EnvStack &v)
            // assignment operator - not implemented
	{
			if(this != &v)
		{
			delete []store;
			int i;
			for(i = 0; i < topIndex; i++)
			{
				store[i] = v.store[i];
			}
		
			return (*this);
		}
		else
		{
			return (*this);
		}
	}

 


