#ifndef COPYJOB_H
#define COPYJOB_H

/* this class defines a copy job.  Each copy job is given an
 * id on construction, copy jobs contain department number and
 * number of pages as well as the id.  They may be input and
 * output.  Input only inputs department number and number of
 * pages, while output also prints out id.
*/

#include <iostream>
using namespace std;

class CopyJob {

    public:
        CopyJob();
            // constructs copy job with 0's for all members

        CopyJob(int id);
            // constructs copy job with given id

        void input(istream &s);
            // inputs from stream s in order
            //    dept numCopies

        void output(ostream &s) const;
            // outputs to stream s in order
            //    id dept numCopies
            

    private:

        int myId;
        int myDept;
        int myNumCopies;

};

ostream & operator << (ostream &s, const CopyJob & v);
    // outputs using keyword operation

#endif
