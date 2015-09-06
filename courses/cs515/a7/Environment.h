#ifndef ENVIRONMENT_H
#define ENVIRONMENT_H

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

class Environment {
    
    public:
        Environment();
            // constructs empty environment with empty name

        Environment(const string & name);
            // constructs empty environment with specified name

        Environment(const Environment &v);
            // copy constructor

        ~Environment();
            // destructor

        Environment& operator = (const Environment &v);
            // assignment operator

        void newVariable(const string & var);
            // adds new variable to environment initializing it
            // to 0.  Prints error if already in environment.

        void setVariable(const string & var, int val);
            // sets variable value to value specified.  Prints
            // error if not in environment.

        void output(ostream & s) const;
            // outputs environment name and variable/value pairs 
            // in environment

        const string &  getEnvName() const;
            // returns the environment name
            
        int getVariable(const string & var) const;
            // returns variable contents.  Prints error if not
            // in environment and returns 0.

        bool findVariable(const string & var) const;
            // returns true if variable has been defined within
            // environment and false otherwise
        

    private:

        struct Elem {
            Variable info;
            Elem *next;
            Elem *back;
        };

        string envName; // environment name, set on explicit
                        // construction

        Elem *head;     // head of chain of variables in environment
                        // chain is kept sorted by variable name
                        // chain is doubly linked with head and
                        // tail sentinels

        void copyCode(const Environment & v);
            // common code for copy constructor and assignment op

        void destructCode();
            // common code for destructor and assignment op
};

ostream& operator << (ostream &s, const Environment &v);
    // outputs using keyword operation

#endif
