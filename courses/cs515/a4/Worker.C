/*
Stephen Chambers
Professor Johnson
10/6/11
* this class defines a worker for an orchard.  Workers have
 * names and an amount of apples that they have "picked" in one of
 * two categories, those picked from the trees and those that
 * were "drops".  The workers are paid based on the amount of
 * apples in each category.  Workers accumulate the amount they
 * have picked, and are paid based on the totals.
*/

#include <iostream>
#include <string>
#include "Worker.h"
#include "DryMeasure.h"
using namespace std;

        Worker::Worker()
	// constructs worker with nothing picked and an empty
            // string for a name
	{
	name = "";
	tree = DryMeasure();
	drops = DryMeasure();
	}
            

        Worker::Worker(string nam)
	// constructs worker with specified name and nothing
            // picked
	{
		name = nam;
		tree = DryMeasure();
		drops = DryMeasure();
	}
            

        void Worker::update(char category, const DryMeasure &amount)
   	 // adds amount picked of specified category to worker
            // assumes category is correct
	{
		if(category == 't')
		{
			tree += amount;
		}
		else if (category == 'd')
		{
			drops += amount;
		}
	}
         

        void Worker::output(ostream &s) const
            // outputs worker information -- name and amount of
            // each category picked
	{
		s << name << " -- " << "tree picks: " << " -- " << tree << " drops: " << "-- " << drops;
	}

        float Worker::getPay(float rateTree, float rateDrops) const
            // calculates returns amount of pay worker should get
            // amount per bushel for picked from tree and for
            // drops is passed in and used in the calculation.
	{
		
		return (rateTree * tree.asBushels()) + (rateDrops * drops.asBushels());
	}

        bool Worker::operator == (const Worker &v) const
	{
		if (name == v.name)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
        bool Worker::operator != (const Worker &v) const
            // equality operators, based on name
	{
		if(name != v.name)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	ostream & operator << (ostream & s, const Worker & v)
	// outputs using named operation
	{
	v.output(s);
	return s;
	}
    


