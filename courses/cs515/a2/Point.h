#ifndef POINT_H
#define POINT_H

#include <iostream>
using namespace std;

/* this class defines a point in two dimensional space.
    values are input and output in the form
        (x,y)
    where x and y are reals and represent the usual x,y
    rectangular coordinates of the point. */

class Point {
    public:
        Point();
            // Point initialized to (0,0)

        Point(float x,float y);
            // point initialized to rectangular coordinates
            // (x,y)

        void input(istream &s);
            // inputs into obj point from specified input 
            // stream.  input expected to be in form (x,y)

        void output(ostream &s) const;
            // outputs obj point in form (x,y) to specified
            // output stream

        bool equal(const Point &) const;
            // returns true if obj and argument are at same
            // spot

        float distance(const Point &) const;
            // returns distance between obj and argument

    private:
        float x;
        float y;
};

#endif
