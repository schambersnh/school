

/* this class defines a variable which is paired with a value.
    variables values may be obtained and may be set.  When
    created, a variable is initialized to 0.
*/

#include <iostream>
#include <string>
#include "Variable.h"
using namespace std;

        Variable::Variable()
            // constructs variable with empty name and 0 value
	{
		self_name = "";
		self_value = 0;
	}

        Variable::Variable(const string & nam)
            // constructs variable with given name and 0 value
	{
		self_name = nam;
		self_value = 0;
	}

        void Variable::setValue(int value)
            // sets value in variable
	{
		self_value = value;
	}

        int Variable::getValue() const
            // gets value in variable
	{
		return self_value;
	}

        void Variable::output(ostream &s) const
            // prints variable and value as pair
	{
		s << "{" << self_name << ", " << self_value << "}";
	}
	// equality operators based on name
        bool Variable::operator == (const Variable &v) const
	{
		if(self_name == v.self_name)
		{
			return true;
		}
	}
        bool Variable::operator != (const Variable &v) const
	{
		if(self_name != v.self_name)
		{
			return true;
		}
	}
            
	// relational operators - based on name
        bool Variable::operator < (const Variable &v) const
	{
		if(self_name < v.self_name)
		{
			return true;
		}
	}
        bool Variable::operator <= (const Variable &v) const
	{
		if(self_name <= v.self_name)
		{
			return true;
		}
	}
        bool Variable::operator >= (const Variable &v) const
	{
		if(self_name >= v.self_name)
		{
			return true;
		}
	}
        bool Variable::operator > (const Variable &v) const
	{
		if(self_name > v.self_name)
		{
			return true;
		}
	}
            
	ostream& operator << (ostream &s, const Variable &v)
   	 // outputs using keyword operation
	{
		v.output(s);
		return s;
	}

