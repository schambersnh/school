#ifndef BITSET_H
#define BITSET_H

/* this class provides a bitset implementation of a set of
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
using namespace std;

class Bitset {

    public:

        static void calcBitsPerLong();
            // calculates number of bits per long in architecture
            // runing in.  sets bitsPerLong class variable
            // to a non-zero value.  MUST BE CALLED PRIOR TO
            // DEFINING/ALLOCATING FIRST BITSET OBJECT

        Bitset(int maxElem = 32);
            // constructs a bitset the minimum size with longs to
            // hold the specified maximum number of elements.
            // range of values is 1 through maxElem.

        ~Bitset();
            // destructor

        Bitset(const Bitset & v);
            // copy

        Bitset & operator = (const Bitset & v);
            // assignment

        void include(int v);
            // adds v to set.  Prints message if out of range of
            // 1 through number of elements and ignores the
            // operation.

        void exclude(int v);
            // removes v from set.  Prints message if out of
            // range and ignores operation.

        bool empty() const;
            // returns true if empty;

        int count() const;
            // returns count of values currently contained in set

        bool isMember(int v) const;
            // returns true if v is member of set, and false
            // otherwise.  returns false if v out of range, does
            // not print error message

        int * members() const;
            // returns dynamically allocated array (address of
            // first element of the array) of current members of
            // set -- deletion of the array becomes
            // responsibility of caller.

        void output(ostream & s) const;
            // outputs in textual form

        void read(ifstream & f);
            // inputs set in binary form -- environment specific
            // inputs in form for write below

        void write(ofstream & f) const;
            // outputs set in binary form -- enviroment specific
            // output order
            // maxElements arrElements store[0] store[1] ...
            // note: if bits per long differs in writing and
            // reading environments, read/write will not work
            // correctly

    private:

        long maxElements; // maximum number of elements that can
                          // be stored in bitset.  Values start at
                          // 1 and range through maxElements

        long arrElements; // number of array elements needed

        static int bitsPerLong; // number of bits per long --
                         // environment specific, calculated on
                         // first construction.
                         // Note: must be defined as a scoped
                         // global in class code file.                         
                         // Note: C++ will initialize to 0 on
                         // program startup.

        long * store; // dynamically allocated array of longs.
                      // Each bit represents a single value.
                      // minimum length needed for architecture


};

ostream & operator << (ostream & s, const Bitset &v);
    // outputs using named operation

#endif
