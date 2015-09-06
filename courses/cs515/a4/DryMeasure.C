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
#include "DryMeasure.h"
using namespace std;


DryMeasure::DryMeasure()
            //constructs a dry measure value of 0 B 0 P
		{
		bushels = 0; 
		pecks = 0;
		}
		

DryMeasure::DryMeasure(int b, int p)
            // constructs a dry measure value of b B p P
		{
		bushels = b; 
		pecks = p;
		}
		

void DryMeasure::input(istream &s)
            // inputs dry measure value in form shown above.  It is
            // assumed that the value is in the correct form.
		{
			char dummy;
			s >> bushels >> dummy >> pecks >>  dummy;
		}
		
		

	void DryMeasure::output(ostream &s) const
            // outputs dry measure value in form shown above
		{
			s << "B " << bushels << " P " << pecks;
		}
		
        DryMeasure& DryMeasure::operator +=(const DryMeasure &amount)
            //adds the amount to the object, modifying the object
		{
			bushels += amount.bushels;
			pecks += amount.pecks;
			if (pecks > 3)
			{
				bushels += pecks / 4;
				pecks = pecks % 4;	
			}
		}
        DryMeasure DryMeasure::operator +(const DryMeasure &amount) const
            //returns new dry measure object formed by adding the
            //amount to the object
		{
			DryMeasure temp;
			temp.bushels += bushels + amount.bushels;
			temp.pecks += pecks + amount.pecks;
			return temp;
		}

        DryMeasure DryMeasure::operator *(int number) const
            //returns a dry measure value formed by multiplying
            //the object by the integer number.
		{
			DryMeasure temp;
			temp.bushels = bushels * number;
			temp.pecks = pecks * number;
			return temp;

		}

        DryMeasure& DryMeasure::operator -=(const DryMeasure &amount)
            // subtracts the amount from the object, modifying
            // the object.  It is assumed that the object will
            // not become negative
		{
			bushels -= amount.bushels;
			pecks -= amount.pecks;
		}

        DryMeasure DryMeasure::operator -(const DryMeasure &amount) const
            // returns a dry measure value formed by subtracting
            // the amount passed from the object.It is assumed
            // that the object will not become negative
		{
			DryMeasure temp;
			temp.bushels = bushels - amount.bushels;
			temp.pecks = pecks - amount.pecks;
			return temp;
		}

        float DryMeasure::asBushels() const
            // returns the dry measure value as a real number of
            // bushels - e.g. 1 B 3 P asBushels would return the
            // real 1.75
		{
			float b = bushels;
			if(pecks == 1)
			{
				b += 0.25;
			}
			else if(pecks == 2)
			{
				b += 0.50;
			}
			else if(pecks == 3)
			{
				b+= 0.75;
			}
			return b;
		}

        bool DryMeasure::operator <(const DryMeasure &amount) const
		{
		DryMeasure temp = (*this);
		if (temp.asBushels() < amount.asBushels())
		{
			return true;
		}
		else
		{
			return false;
		}
		}
        bool DryMeasure::operator <=(const DryMeasure &amount) const
		{
		DryMeasure temp = (*this);
		if (temp.asBushels() < amount.asBushels())
		{
			return true;
		}
		else
		{
			return false;
		}
		}
        bool DryMeasure::operator >=(const DryMeasure &amount) const
		{
		DryMeasure temp = (*this);
		if (temp.asBushels() <= amount.asBushels())
		{
			return true;
		}
		else
		{
			return false;
		}
		}
        bool DryMeasure::operator >(const DryMeasure &amount) const
		{
		DryMeasure temp = (*this);
		if (temp.asBushels() > amount.asBushels())
		{
			return true;
		}
		else
		{
			return false;
		}
		}
            // relational operators - compare dry measure amounts

        bool DryMeasure::operator ==(const DryMeasure &amount) const
		{
		DryMeasure temp = (*this);
		if (temp.asBushels() == amount.asBushels())
		{
			return true;
		}
		else
		{
			return false;
		}
		}
        bool DryMeasure::operator !=(const DryMeasure &amount) const
		{
		DryMeasure temp = (*this);
		if (temp.asBushels() != amount.asBushels())
		{
			return true;
		}
		else
		{
			return false;
		}
		}
            // equality operators - compare dry measure amounts

    
istream& operator >>(istream& i, DryMeasure & d)
{	
	d.input(i);
	return i;
}
    // inputs using keyword operation

ostream& operator <<(ostream& o, const DryMeasure & d)
{
	d.output(o);
	return o;
}
    // outputs using keyword operation

DryMeasure operator *(int v1, const DryMeasure &v2)
{

}
    // returns the value formed by multiplying the dry
    // measure object by the integer


