/*Stephen Chambers
Brian Johnson
Program 9
11/17/11
This program has two interacting databases, one based on courses, and one on students.
Each time a student is inputed, it's inputed with a varied amount of crns, which are
stored on a chain. These are used as the keys to access the courseDB's hash table. THe 
courses are inputed with a varied amount of id's, which are used as the keys to access
the studentDB's hash table. The main program checks to see if file input worked,
loads the databases, then prints the schedules of the students and the rosters of the
courses.
*/

#include "Student.h"
#include "StudentDB.h"
#include "Course.h"
#include "CourseDB.h"
#include <string.h>
#include <iostream>
#include <fstream>
#include <stdlib.h>

int main(int argc, const char*argv[])
{
	
	StudentDB studentData;
	CourseDB courseData;
	Student stemp;
	Course ctemp;

	//open file input streams
	ifstream f1(argv[1]);
	ifstream f2(argv[2]);
	
	//if either stream failed, abort the program
	if(!f1 || !f2)
	{
		exit(1);
	}
	//load student database
	f1 >> stemp;
	while(f1)
	{
		studentData.insert(stemp);
		f1 >> stemp;
	}
	//load course database
	f2 >> ctemp;
	while (f2)
	{
		courseData.insert(ctemp);
		f2 >> ctemp;
	}
	//print schedules
	studentData.printSchedules(cout, courseData);
	//print rosters
	courseData.printRosters(cout, studentData);
	return 0;
}
