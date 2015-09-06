
/*
 * Stephen Chambers, Program 10
 * this class provides a bitset implementation of a set of
 * integers.  The size is set on construction, which will
 * generate an array of the appropriate number of longs for the
 * architecture being compiled on.  The default constructor will
 * provide a small set which will hold 32 values.
 *
 * in order to define and/or dynamically allocate bitset objects,
 * the class function calcBitsPerLong MUST be called (prior to
 * creating an object).
 *
 * values in the set are in the range 1 through maxElem
 *
 * values can be included and excluded from the set, and the set
 * can be tested to determine if a value is a member or not.
 * Construction creates an empty set.  The count operation
 * returns a count of the number of elements in the set.  The
 * members operation returns a dynamically allocated array of
 * the members of the set -- the array becomes the property of
 * the caller, whose responsibility it is to deallocate the
 * array.  The set may be output in text form or in binary form,
 * sets may be input in binary form.  Caution binary input and
 * output is implementation/environment dependent.
*/

#include <iostream>
#include <fstream>
#include "Bitset.h"
using namespace std;

	int Bitset::bitsPerLong;

        void Bitset::calcBitsPerLong()
            // calculates number of bits per long in architecture
            // runing in.  sets bitsPerLong class variable
            // to a non-zero value.  MUST BE CALLED PRIOR TO
            // DEFINING/ALLOCATING FIRST BITSET OBJECT
	{
		 long s = 1234;
		 int count = 1;
		 while (s != 0)
		 {
			 s  = s << 1;
			 count++;

		 }
		 bitsPerLong = count;
	}

       Bitset:: Bitset(int maxElem)
            // constructs a bitset the minimum size with longs to
            // hold the specified maximum number of elements.
            // range of values is 1 through maxElem.
	{
		maxElements = maxElem;
		arrElements = maxElements/bitsPerLong;
		store = new long[arrElements];
		
	}

        Bitset::~Bitset()
            // destructor
	{
		delete []store;
	}

        Bitset::Bitset(const Bitset & v)
            // copy
	{

	}

        Bitset & Bitset::operator = (const Bitset & v)
            // assignment
	{

	}

        void Bitset::include(int v)
            // adds v to set.  Prints message if out of range of
            // 1 through number of elements and ignores the
            // operation.
	{
		
	}

        void Bitset::exclude(int v)
            // removes v from set.  Prints message if out of
            // range and ignores operation.
	{

	}

        bool Bitset::empty() const
            // returns true if empty.
	{

	}

        int Bitset::count() const
            // returns count of values currently contained in set
	{
		
		int count = 0;
		for(int i = 1; i <= maxElements; i++)
		{
			if(isMember(i))
			{
				count++;
			}
		}
		return count;
	}

        bool Bitset::isMember(int v) const
            // returns true if v is member of set, and false
            // otherwise.  returns false if v out of range, does
            // not print error message
	{
		v--;
		long s = store[v/bitsPerLong];
		v = v % bitsPerLong;
		return s & (1<<v);
		
	}

        int * Bitset::members() const
            // returns dynamically allocated array (address of
            // first element of the array) of current members of
            // set -- deletion of the array becomes
            // responsibility of caller.
	{
		int count = (*this).count();
		int * memberArray = new int[count];
		int arrCount = 0;
		for(int i = 1; i <= maxElements; i++)
		{
			if(isMember(i))
			{
				memberArray[arrCount] = i;
				arrCount ++;
			}
		}
		return &(memberArray[0]);
	}

        void Bitset::output(ostream & s) const
            // outputs in textual form
	{

	}

        void Bitset::read(ifstream & f)
            // inputs set in binary form -- environment specific
            // inputs in form for write below
	{
		f.read((char*)&maxElements, sizeof(long));
		f.read((char*)&arrElements, sizeof(long));
		
		for(int i = 0; i < arrElements; i++)
		{
			f.read((char*)&store[i], sizeof(long));
		}
	}

        void Bitset::write(ofstream & f) const
            // outputs set in binary form -- enviroment specific
            // output order
            // maxElements arrElements store[0] store[1] ...
            // note: if bits per long differs in writing and
            // reading environments, read/write will not work
            // correctly
	{
		int i = 0;
		f.write((char*)&maxElements, bitsPerLong);
		f.write((char*)&arrElements, bitsPerLong);
		
		for(i = 0; i < arrElements; i++)
		{
			f.write((char*)&store[i], bitsPerLong);
		}
	}

	ostream & operator << (ostream & s, const Bitset &v)
    	// outputs using named operation
	{
		v.output(s);
		return s;
	}


