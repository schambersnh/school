#ifndef CANDIDATE_H
#define CANDIDATE_H

/* this class defines a candidate in an election.  Each candidate
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
using namespace std;

class Candidate {

    public:

        Candidate(int num);
            // constructs candidate with specified number, empty
            // name, and 0 votes

        void addVote();
            // adds vote to candidate tally

        void input(istream & s);
            // inputs candidate name.  Name is expected to be on
            // a single line and may contain spaces

        void output(ostream & s) const;
            // outputs candidate in form
            //   number  votes  name

        bool operator >= (const Candidate & v) const;
        bool operator > (const Candidate & v) const;
        bool operator < (const Candidate & v) const;
        bool operator <= (const Candidate & v) const;
            // relational operators based on candidate number 

        bool operator == (const Candidate & v) const;
        bool operator != (const Candidate & v) const;
            // equality operators based on candidate number 

    private:

        int myNumber;
        string myName;
        int myTally;

};

istream & operator >> (istream & s, Candidate & v);
    // inputs with keyword operation

ostream & operator << (ostream & s, const Candidate & v);
    // outputs with keyword operation

#endif
