#ifndef CHARQUEUE_H
#define CHARQUEUE_H

/* this class defines a queue of characters.  It has the normal
 * operations (enqueue, dequeue, empty, front) and the characters
 * in the queue can be printed non-destructively.
*/

#include <iostream>
using namespace std;

class CharQueue {

    public:
        class Underflow{};
        class Overflow{};
            // exception classes

        CharQueue();
            // constructs empty queue

        CharQueue(const CharQueue &v);
            // copy constructor

        ~CharQueue();
            // destructor

        CharQueue & operator = (const CharQueue &v);
            // assignment operator

        void enqueue(char v) throw(Overflow);
            // inserts character onto queue -- throws exception if
            // queue if full

        void dequeue() throw(Underflow);
            // removes front character -- throws exception if queue
            // is empty

        char front() const throw(Underflow);
            // returns character at front of queue -- throws
            // exception if queue is empty

        bool empty() const;
            // predicate -- returns true if empty

        int length() const;
            // returns number of values stored in queue

        void output(ostream &s) const;
            // non-destructively outputs characters on queue

    private:
        struct Elem {
            char info;
            Elem * next;
        };

        Elem *head;
        Elem *tail;

        int count;  // number of characters currently in queue

        void copyCode(const CharQueue &v);
            // copies queue, without deleting original
            // common code copy constructor and assignment op

        void destructCode();
            // deletes all elements of queue
            // common code destructor and assignment op

};

ostream & operator << (ostream &s, const CharQueue &v);
    // non-destuctively outputs contents of queue

#endif
