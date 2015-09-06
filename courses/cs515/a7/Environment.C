
/* this class defines an environment of variables and their
    values.  Each variable is paired with a value.  Variables are
    initialized to 0 when placed in the environment.  Individual
    variables may be defined, set to a value, and their value
    obtained.  All the variables and values in the environment
    may be printed.
*/

#include <iostream>
#include <string>
using namespace std;
#include "Variable.h"
#include "Environment.h"


   
        Environment::Environment()
            // constructs empty environment with empty name
	{
		Variable bigValue = Variable("zzzzzzzzzzzzzz");
		head = new Elem;
		head -> back = 0;
		head -> next = new Elem;
		head -> next -> info = bigValue;
		head -> next -> back = head;
		head -> next -> next = 0;
	}
	

        Environment::Environment(const string & name)
            // constructs empty environment with specified name
	{
		envName = name;
		Variable bigValue = Variable("zzzzzzzzzzzzzz");
		head = new Elem;
		head -> back = 0;
		head -> next = new Elem;
		head -> next -> info = bigValue;
		head -> next -> back = head;
		head -> next -> next = 0;
	}

        Environment::Environment(const Environment &v)
            // copy constructor
	{
		copyCode(v);
	}

        Environment::~Environment()
            // destructor
	{
		destructCode();
	}

        Environment& Environment::operator = (const Environment &v)
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

        void Environment::newVariable(const string & var)
            // adds new variable to environment initializing it
            // to 0.  Prints error if already in environment.
	{
		Variable newVar = Variable(var);
		Elem * ptr = new Elem;
		ptr -> info = newVar;

		Elem * p = head -> next;
		
		while(p -> next != 0 && p -> info <= var)
		{
			p = p -> next;
		}
		if(p -> info > var)
		{
			ptr -> next = p;
			ptr -> back = p -> back;
			p -> back -> next = ptr;
			p -> back = ptr;	
		}
	}

        void Environment::setVariable(const string & var, int val)
            // sets variable value to value specified.  Prints
            // error if not in environment.
	{
		Elem * p = head;
		Variable v = Variable(var);
		while(p!= 0 && p -> info != v)
		{
			p = p -> next;
		}
		if(p != 0)
		{
			p -> info.setValue(val);
		}
		else
		{
			cout << "\nVariable not found in environment";
		}
	}

        void Environment::output(ostream & s) const
            // outputs environment name and variable/value pairs 
            // in environment
	{
		Elem * p = head -> next;	
		while(p -> next != 0)
		{
			cout << p -> info << " ";
			p = p -> next;
		}
	}

        const string &  Environment::getEnvName() const
            // returns the environment name
	{
		return envName;
	}
            
        int Environment::getVariable(const string & var) const
            // returns variable contents.  Prints error if not
            // in environment and returns 0.
	{
		Elem * p = head;
		Variable v = Variable(var);
		while(p != 0 && p -> info != v)
		{
			p = p -> next;
		}
		if(p != 0)
		{
			return p -> info.getValue();
		}
		else
		{
			cout << "\nVariable not found in environment";
		}
	}

        bool Environment::findVariable(const string & var) const
            // returns true if variable has been defined within
            // environment and false otherwise
	{
		Elem * p = head;
		Variable v = Variable(var);
		while(p != 0 && p -> info != v)
		{
			p = p -> next;
		}
		if(p != 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

        void Environment::copyCode(const Environment & v)
            // common code for copy constructor and assignment op
	{
	envName = v.envName;
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

        void Environment::destructCode()
            // common code for destructor and assignment op
	{
		Elem * p = head;
		while(head != 0)
		{
		p = head;
		head = head -> next;
		delete p;
		}
	}

	ostream& operator << (ostream &s, const Environment &v)
    	// outputs using keyword operation
	{
		v.output(s);
		return s;
	}


