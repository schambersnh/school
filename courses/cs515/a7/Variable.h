#ifndef VARIABLE_H
#define VARIABLE_H

/* this class defines a variable which is paired with a value.
    variables values may be obtained and may be set.  When
    created, a variable is initialized to 0.
*/

#include <iostream>
#include <string>
using namespace std;

class Variable {


    public:

        Variable();
            // constructs variable with empty name and 0 value

        Variable(const string & nam);
            // constructs variable with given name and 0 value

        void setValue(int value);
            // sets value in variable

        int getValue() const;
            // gets value in variable

        void output(ostream &s) const;
            // prints variable and value as pair

        bool operator == (const Variable &v) const;
        bool operator != (const Variable &v) const;
            // equality operators based on name

        bool operator < (const Variable &v) const;
        bool operator <= (const Variable &v) const;
        bool operator >= (const Variable &v) const;
        bool operator > (const Variable &v) const;
            // relational operators - based on name

    private:

        string self_name;
        int self_value;
};

ostream& operator << (ostream &s, const Variable &v);
    // outputs using keyword operation
#endif
