/* this class defines a point in two dimensional space.
    values are input and output in the form
        (x,y)
    where x and y are reals and represent the usual x,y
    rectangular coordinates of the point. */

#include <math.h>
#include "Point.h"

//-----------------------------------------------------
/* constructors */

Point::Point()
{
    x = 0;
    y = 0;
}

Point::Point(float xval, float yval)
{
    x = xval;
    y = yval;
}

//-----------------------------------------------------
/* this procedure reads in a point in the form (x,y).
    it assumes that the data is in the proper syntax */

void Point::input(istream &s)
{
    char dummy;
    s >> dummy >> x >> dummy >> y >> dummy;
}

//-----------------------------------------------------
/* this procedure prints out a point in the form (x,y)*/

void Point::output(ostream &s) const
{
    s << '(' << x << ',' << y << ')';
}

//-----------------------------------------------------
/* this predicate returns true of the two points are
    equal */

bool Point::equal(const Point &p) const
{
    return x == p.x && y == p.y;
}

//-----------------------------------------------------
/* this function returns the distance between the two
    points */

float Point::distance(const Point &p) const
{
    float dx,dy;
    dx = x - p.x;
    dy = y - p.y;
    return sqrt(dx*dx + dy*dy);
}
