#ifndef WORKER_H
#define WORKER_H

/* this class defines a worker for an orchard.  Workers have
 * names and an amount of apples that they have "picked" in one of
 * two categories, those picked from the trees and those that
 * were "drops".  The workers are paid based on the amount of
 * apples in each category.  Workers accumulate the amount they
 * have picked, and are paid based on the totals.
*/

#include <iostream>
#include <string>
using namespace std;

#include "DryMeasure.h"

class Worker {
    public:
        Worker();
            // constructs worker with nothing picked and an empty
            // string for a name

        Worker(string nam);
            // constructs worker with specified name and nothing
            // picked

        void update(char category, const DryMeasure &amount);
            // adds amount picked of specified category to worker
            // assumes category is correct

        void output(ostream &s) const;
            // outputs worker information -- name and amount of
            // each category picked

        float getPay(float rateTree, float rateDrops) const;
            // calculates returns amount of pay worker should get
            // amount per bushel for picked from tree and for
            // drops is passed in and used in the calculation.

        bool operator == (const Worker &v) const;
        bool operator != (const Worker &v) const;
            // equality operators, based on name

        // constants

            enum{treeKind = 't'};
            enum{dropKind = 'd'};

    private:
        string name;
        DryMeasure tree;
        DryMeasure drops;
};

ostream & operator << (ostream & s, const Worker & v);
    // outputs using named operation

#endif
