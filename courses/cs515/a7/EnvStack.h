#ifndef ENVSTACK_H
#define ENVSTACK_H

/* this class defines a stack of environments 
    the normal operations of push, pop, empty and top are
    available.   Maximum size allowed for stack is specified on
    construction.
*/

#include "Environment.h"

class EnvStack {

    public:
        // exceptions

            class Overflow{};
            class Underflow{};

        EnvStack(int n = 10);
            // constructs empty stack with maximum of n items
            // if no argument is supplied then the default value
            // will be used

        ~EnvStack();
            // destructor

        void push(const Environment &v) throw(Overflow);
            // makes v new top element, pushing existing elements
            // down -- throws exception if no room left on stack

        void pop() throw(Underflow);
            // removes top element.  Throws exception if stack
            // was empty 

        Environment & top() throw(Underflow);
            // returns reference to top element of stack.  Throws
            // exception if stack was empty

        bool empty() const;
            // returns true if stack is empty

    private:
        EnvStack(const EnvStack &v);
            // copy constructor - not implemented
        EnvStack& operator = (const EnvStack &v);
            // assignment operator - not implemented

        int maxElem;
        Environment * store;  // dynamically allocated array
        int topIndex;
};

#endif
