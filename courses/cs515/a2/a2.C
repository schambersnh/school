/*
Stephen Chambers
Mr. Johnson
9/22/11
This program is designed to take in multiple Point paths and echo print the point and the type of paths, as well as printing out the total number of paths, total length of paths, and the average path length at the end of the program.
*/
#include <iostream>
#include "Point.h"
using namespace std;

void calculate(const Point &, float &);
int main()
{

	Point p;
	float distance = 0;
	int numPaths = 0;
	p.input(cin);
	while (cin)
	{
		numPaths++;
		calculate(p, distance);
		p.input(cin);
	}
	
	cout << "\nnumber of paths is : " << numPaths << "\n";
	cout << "total lengths of paths is: " << distance << "\n";

	cout << "Average path length is " << distance / numPaths;
return 0;
}

void calculate(const Point & start, float & d)
{

Point end;
int numVias;
end.input(cin);
cin >> numVias;

if(start.equal(end) && numVias == 0)
{
	cout << "\nSingle Point: " << "Start: ";
	start.output(cout);
	cout << " End: ";
	end.output(cout);
	cout << " " << numVias << " via points";
	d += start.distance(end);
	cout << "\n path length: " << start.distance(end) << "\n";
}

else if(start.equal(end) && numVias > 0)
{	float totDistance = 0;
	cout << "\nCircular Path: " << "Start: ";
	start.output(cout);
	cout << " End: ";
	end.output(cout);
	cout << " " << numVias << " via points";
	cout << "\n";
	Point p = start;
	
for(int i = 0; i < numVias; i++)
{
	Point via;
	via.input(cin);
	via.output(cout);
	float distance = p.distance(via);
	d += distance;
	totDistance += distance;
	p = via;
}
d += p.distance(start);
totDistance += p.distance(start);
cout << "\n path length: " << totDistance << "\n";
}
else if(start.equal(end) == false)
{
	cout << "\nOpen Path: " << "Start: ";
	start.output(cout);
	cout << " End: ";
	end.output(cout);
	cout << " " << numVias << " via points";
	d += start.distance(end);
	

for(int i = 0; i < numVias; i++)
{
	Point via;
	via.input(cin);
	via.output(cout);
}
cout << "\n path length: " << start.distance(end) << "\n";

}

}
