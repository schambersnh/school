#ifndef NODESTACK_H
#define NODESTACK_H

/* this class defines a stack of characters.  Values
    may be pushed onto the stack and popped off.
    The top element may be accessed, and it may
    be determined if the stack is empty.  Stacks
    are constructed empty. */

#include <iostream>
using namespace std;

class NodeStack {
    
    public:
        // exceptions
            class Overflow{};
            class Underflow{};

        NodeStack();
            // constructs empty stack with a default
            // number of elements available

        NodeStack(const CharStack &v);
            // copy constructor

        ~NodeStack();
            // destructor

        NodeStack& operator =(const CharStack &v);
            // assignment operator

        void push(Node *) throw(Overflow);
            // pushes value onto top of stack
            // throws exception if full

        void pop() throw(Underflow);
            // pops top value off stack
            // throws exception if empty

        char top() const throw(Underflow);
            // returns top value on stack
            // throws exception if empty

        bool empty() const;
            // returns true if stack is empty

        void output(ostream &s) const;
            // outputs elements of stack

    private:
        struct Elem {
            Node * info;
            Elem *next;
        };

        Elem *head; 
        Elem *tail; 

        void copyCode(const CharStack & v);
            // common code for copy constructor and assignment

        void destructCode();
            // common code for destructor and assignment
};

ostream & operator << (ostream & s, const CharStack & v);
    // outputs using keyword operation

#endif
