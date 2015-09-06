/*
Stephen Chambers
Professor Johnson
9/29/11 
This program accepts grades of apples and amounts of apples from the user, outputs the cost per apple purchase, and outputs the 
total amount of grade a and b apples purchased.
*/
#include <iostream>
#include "DryMeasure.h"
using namespace std;

void proc(char, const DryMeasure &,  DryMeasure &,  DryMeasure &);
int main()
{
	char grade;
	DryMeasure totala;
	DryMeasure totalb;
	DryMeasure d;
	cout << "\nEnter purchases, end input with ctl D\n";
	cin >> grade >> d;

	while (cin)
	{
		proc(grade, d, totala, totalb);
		cin >> grade >> d;
	}
	cout << "\n\nSales totals:";
	cout << "\n\t" << "grade a --- " << totala;
	cout << "\n\t" << "grade b --- " << totalb;
	return 0;
}
	/*This procedure takes in a grade and a DryMeasure object, determines the cost, prints the cost, and 
	updates the total amount variables in main*/
	void proc(char grade, const DryMeasure & d, DryMeasure & totalAGrade, DryMeasure & totalBGrade)
	{
		float c;
		float diff;
		float finalCost;
		if(grade == 'a')
		{
			if(d.asBushels() >= 5.50) //discount
			{
				c = d.asBushels() * 8.50;
				diff = c * 0.10;
				finalCost =  c - diff;
				cout << "\ncost ---> " << finalCost;
				totalAGrade += d;
			}
			else //normal purchase
			{
				finalCost = d.asBushels() * 8.50;
				cout << "\ncost ---> " << finalCost;
				totalAGrade += d;
			}

		}
		else if(grade == 'b')
		{
			if(d.asBushels() >= 10.0) //discount
			{
				c = d.asBushels() * 5.25;
				diff = c * 0.10;
				finalCost = c - diff;
				cout << "\ncost ---> " << finalCost;
				totalBGrade += d;
			}
			else //normal purchase
			{
				finalCost = d.asBushels() * 5.25;
				cout << "\ncost ---> " << finalCost;
				totalBGrade += d;
			}
		}
	}
	

