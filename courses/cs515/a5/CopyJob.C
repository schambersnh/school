
/* this class defines a copy job.  Each copy job is given an
 * id on construction, copy jobs contain department number and
 * number of pages as well as the id.  They may be input and
 * output.  Input only inputs department number and number of
 * pages, while output also prints out id.
*/

#include <iostream>
#include "CopyJob.h"
using namespace std;



    
        CopyJob::CopyJob()
            // constructs copy job with 0's for all members
	{
		myId = 0;
		myDept = 0;
		myNumCopies = 0;
	}

        CopyJob::CopyJob(int id)
            // constructs copy job with given id
	{
		myId = id;
		myDept = 0;
		myNumCopies = 0;
	}

        void CopyJob::input(istream &s)
            // inputs from stream s in order
            //    dept numCopies
	{
		s >> myDept >> myNumCopies;
	}

        void CopyJob::output(ostream &s) const
            // outputs to stream s in order
            //    id dept numCopies
         {
			 s << " " << myId << " " << myDept << " " << myNumCopies;
		}   

	ostream & operator << (ostream &s, const CopyJob & v)
    	// outputs using keyword operation
	{
		v.output(s);
		return s;
	}


