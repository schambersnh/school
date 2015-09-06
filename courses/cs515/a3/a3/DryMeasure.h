#ifndef DRYMEASURE_H
#define DRYMEASURE_H

/* This class defines a type which provides dry measure values.  
    These values are input and output in the form
       n B m P
    where "n" and "m" are non-negative integers and 'B' and 'P'
    are the actual characters, which stand for Bushels and Pecks.

    It is assumed that no operation will produce a negative dry
    measurement value.

    The number of pecks will never be output exceeding 3.

    It is assumed that input values are correct.
*/

#include <iostream>
using namespace std;

class DryMeasure {
    public:
        DryMeasure();
            //constructs a dry measure value of 0 B 0 P

        DryMeasure(int b, int p);
            // constructs a dry measure value of b B p P

        void input(istream &s);
            // inputs dry measure value in form shown above.  It is
            // assumed that the value is in the correct form.

        void output(ostream &s) const;
            // outputs dry measure value in form shown above

        DryMeasure& operator +=(const DryMeasure &amount);
            //adds the amount to the object, modifying the object

        DryMeasure operator +(const DryMeasure &amount) const;
            //returns new dry measure object formed by adding the
            //amount to the object.

        DryMeasure operator *(int number) const;
            //returns a dry measure value formed by multiplying
            //the object by the integer number.

        DryMeasure& operator -=(const DryMeasure &amount);
            // subtracts the amount from the object, modifying
            // the object.  It is assumed that the object will
            // not become negative

        DryMeasure operator -(const DryMeasure &amount) const;
            // returns a dry measure value formed by subtracting
            // the amount passed from the object.It is assumed
            // that the object will not become negative

        float asBushels() const;
            // returns the dry measure value as a real number of
            // bushels - e.g. 1 B 3 P asBushels would return the
            // real 1.75

        bool operator <(const DryMeasure &amount) const;
        bool operator <=(const DryMeasure &amount) const;
        bool operator >=(const DryMeasure &amount) const;
        bool operator >(const DryMeasure &amount) const;
            // relational operators - compare dry measure amounts

        bool operator ==(const DryMeasure &amount) const;
        bool operator !=(const DryMeasure &amount) const;
            // equality operators - compare dry measure amounts

    private:
        int bushels;   //number of bushels
        int pecks;     //number of pecks (4 pecks per bushel)
};
    
istream& operator >>(istream&, DryMeasure &);
    // inputs using keyword operation

ostream& operator <<(ostream&, const DryMeasure &);
    // outputs using keyword operation

DryMeasure operator *(int v1, const DryMeasure &v2);
    // returns the value formed by multiplying the dry
    // measure object by the integer

#endif
