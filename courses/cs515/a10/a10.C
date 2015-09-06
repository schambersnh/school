/*Stephen Chambers
Brian Johnson
Program 10
12/1/11
This program accepts candidate information and "voting machine" data, 
represented by information in binary files, and tallies the votes for each 
candidate. When data has been processed, the program is to print the candidate
information in input order. A bitset is used to keep track of the ballots, and 
returns a dynamically allocated array to the main program. The main program 
then tallies up the votes, throws away the bitset, and gets a new one.

*/

#include <iostream>
#include "Bitset.h"
#include "Candidate.h"
#include <string>
#include <stdlib.h>

//method forawrd declarations
void storeCandidates(ifstream&, const char **, int&, int&, Candidate **);
void processBallots(int &, int &, int &, const char**, Candidate **); 

int main (int argc, const char * argv[])
{
	int numCandidates = 0;
	int maxnumVotes = 0;
	string garbage;
	ifstream f1(argv[1]);
	
	//check to see if the files opened
	if(!f1)
	{	
		cout << "Candidates File could not be opened " << endl;
		exit(1);
	}
	//input in the number of candidates and the maximum number of votes
	//must input the white space before first candidate
	f1 >> numCandidates >> maxnumVotes;
	getline(f1, garbage);
	//define an array of pointers to candidates
	Candidate * candArray[numCandidates];
	cout << "there are " << numCandidates << " candidates" << endl;
	cout << "each ballot may have up to " << maxnumVotes << " votes" << endl;

	//store candidates and process all the ballot files
	storeCandidates(f1, argv, numCandidates, maxnumVotes, &candArray[0]);
	processBallots(argc, numCandidates, maxnumVotes, argv, &candArray[0]);

	//output results
	cout << "\n*************FINAL RESULTS*************" << endl;
	for(int j = 0; j < numCandidates; j++)
	{
		cout << *(candArray[j]) << endl;
	}
	cout << "***************************************" << endl;
	for(int i = 0; i <= numCandidates; i++)
	{
		delete candArray[i];
	}
	
}
/*This method opens an input stream to read the candidates file from 
the command line. dynamically allocated candidates are then assigned to those 
pointers, where the number of the candidate is always one more then the index of the array.
Candidates are then echoed to the output stream.*/
void storeCandidates(ifstream& f1, const char * argv[], int & numCandidates, int & maxnumVotes, Candidate * candArray[])
{
	int num = 0;
	while(f1)
	{
		candArray[num] = new Candidate(num + 1);
		f1 >> *(candArray[num]);
		num++;
	}
	cout << "\n**********CANDIDATES**********" << endl;
	for(int c = 0; c < numCandidates; c++)
	{
		cout << *(candArray[c]) << endl;
	}
	cout << "******************************" << endl;
	f1.close();
}
/*This method opens an input stream in binary mode to read in data from binary ballot files.
The number of bits per long needed by Bitset objects is calculated, and a bitset object is created.
The method also is set up so any number of ballot files can be put into the command line. A dynamically
allocated array is then brought in from the Bitset object, containing the votes for the ballot. The candidate's 
tally is updated, and the current ballot file is cleared and closed. If the ballot has two many votes (based on
value from candidate file), then an error message is printed to the screen showing the votes, and the votes are
not counted*/
void processBallots(int & argc, int & numCandidates, int & maxnumVotes, const char * argv[], Candidate * candArray[])
{
	ifstream f2;
	Bitset::calcBitsPerLong();
	Bitset b;
	int * members;
	int ballotCount = 0;
	int invalidBallotCount = 0;

	for(int k = 2; k < argc; k++)
	{
	cout << "\nprocessing " << argv[k] << endl;
	f2.open(argv[k], ios::in | ios::binary);
	if(!f2)
	{
		cout << "Ballots File could not be opened " << endl;
		exit(1);
	}
	
	b.read(f2);
	while(f2)
	{
		members = b.members();
		int count = b.count();

		if(count <= maxnumVotes)
		{
		for(int i = 0; i < count; i++)
		{
			candArray[members[i] - 1] -> addVote();
		}
		ballotCount++;
		}
		else
		{
			cerr << "ballot has too many votes: { ";
			for(int j = 0; j < count; j++)
			{
				cerr << members[j] << " ";
			}
			cerr << "}" << endl;
			invalidBallotCount++;
		}
		delete []members;
		b.read(f2);
	}
	f2.clear();
	f2.close();
	}
	cout << "\n There were " << ballotCount << " good ballots\n\t and " << invalidBallotCount << " invalid ballots" << endl;
}
