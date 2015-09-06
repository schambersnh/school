

/* Stephen Chambers, Program 10
* this class defines a candidate in an election.  Each candidate
 * has a number on the ballot, a name, and a number of votes.
 * The relational operators are based on the candidate number.
 * Candidates may be input (names only) output (name, number, an
 * number of votes) and voted for (add vote to candidate tally).
 *
 * on input, the candidate name is expected to take one full
 * input line.
*/

#include <iostream>
#include <string>
#include "Candidate.h"
#include <iostream>
#include <string>
using namespace std;



        Candidate::Candidate(int num)
            // constructs candidate with specified number, empty
            // name, and 0 votes
	{
		myNumber = num;
		myName = "";
		myTally = 0;
	}

        void  Candidate::addVote()
            // adds vote to candidate tally
	{
		myTally++;
	}

        void  Candidate::input(istream & s)
            // inputs candidate name.  Name is expected to be on
            // a single line and may contain spaces
	{
		getline(s, myName);
	}

        void  Candidate::output(ostream & s) const
            // outputs candidate in form
            //   number  votes  name
	{
		s << myNumber << " -- " << myTally << " -- " << myName;
	}

	// relational operators based on candidate number 
        bool  Candidate::operator >= (const Candidate & v) const
	{
		return myNumber >= v.myNumber;
	}
        bool  Candidate::operator > (const Candidate & v) const
	{
		return myNumber > v.myNumber;
	}
        bool  Candidate::operator < (const Candidate & v) const
	{
		return myNumber < v.myNumber;
	}
        bool  Candidate::operator <= (const Candidate & v) const
	{
		return myNumber <= v.myNumber;
	}
            
	// equality operators based on candidate number 
        bool  Candidate::operator == (const Candidate & v) const
	{
		return myNumber == v.myNumber;
	}
        bool  Candidate::operator != (const Candidate & v) const
	{
		return myNumber != v.myNumber;
	}
            

	istream & operator >> (istream & s, Candidate & v)
    	// inputs with keyword operation
	{
		v.input(s);
		return s;
	}

	ostream & operator << (ostream & s, const Candidate & v)
   	 // outputs with keyword operation
	{
		v.output(s);
		return s;
	}


