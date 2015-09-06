#ifndef PRICOPYJOBQUEUE_H
#define PRICOPYJOBQUEUE_H

/* this class defines a priority queue holding copy jobs.
 * Priorities are integers, the larger the integer the higher the
 * priority.  The oldest highest priority job is always at the
 * head of the queue.
*/

#include <iostream>
using namespace std;
#include "CopyJob.h"

class PriCopyJobQueue {

    public:
        PriCopyJobQueue();
            // constructs empty queue

        ~PriCopyJobQueue();
            // destructor

        PriCopyJobQueue(const PriCopyJobQueue &v);
            // copy constructor

        PriCopyJobQueue & operator = (const PriCopyJobQueue &v);
            // assignment operator

        void enqueue(int priority, const CopyJob &v);
            // inserts job onto queue according to priority

        bool dequeue();
            // removes job at head of queue
            // success/fail -- returns true if non-empty, false
            // if empty

        bool front(CopyJob &v) const;
            // passes back job at head of queue
            // success/fail -- returns true if non-empty, false
            // if empty

        bool empty() const;
            // returns true if empty queue, false otherwise

        void output(ostream &s) const;
            // outputs queue to specified stream

    private:

        struct Elem {
            int priority;
            CopyJob info;
            Elem * next;
        };

        Elem * head;

        // note: implemented as ordered list based on priority,
        // non-ascending order, stable insertion (equal item
        // placed towards tail)

        void copyCode(const PriCopyJobQueue & v);
            // copys v into object invoked on

        void destructCode();
            // deletes chain of object invoked on

};

ostream & operator << (ostream & s, const PriCopyJobQueue & v);
    // outputs using keyword operation

#endif
