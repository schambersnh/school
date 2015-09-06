

/* this class provides a course data base.  Courses may be
 * looked up by their course reference number, and they may be
 * output in original insertion order.
*/

#include <iostream>
using namespace std;

#include "Course.h"
#include "CourseDB.h"

class StudentDB;


        CourseDB::CourseDB(int size)
            // constructs empty database with specified number of
            // elements in array
	{
		int i;
		expected = size;
		table = new Elem*[expected];
		for(i = 0; i < expected; i ++)
		{
			table[i] = 0;
		}
		head = 0;
		tail = 0; 
	}

        CourseDB::~CourseDB()
            // destructor
	{
		int i;
		for(i = 0; i < expected; i++)
		{
			delete table[i];
		}
		Elem * p = head;
		while(head != 0)
		{
		p = head;
		head = head -> l_next;
		delete p;
		}
	}

        void CourseDB::insert(const Course & v) throw(Duplicate)
            // inserts course into database
	{
		int index = v.hash(expected);
		Elem * p = table[index];

		while(p && p -> info != v)
		{
			p = p -> ht_next;
		}
		if(p != 0)
		{
			throw Duplicate();
		}
		else
		{
			//create element
			p = new Elem;
			p-> info = v;
			//set hash table chain
			p -> ht_next = table[index];
			table[index] = p;

			//set insertion chain
			p -> l_next = 0; 
			if(head == 0)
			{
				head = p;
			}
			else
			{
				tail -> l_next = p;
			}
			tail = p;
		}

	}

        const Course * CourseDB::find(const Course & v) const
            // returns pointer to course in database or null if
            // not found
	{
		
		int index = v.hash(expected);
		Elem * p = table[index];

		while(p && p -> info != v)
		{
			p = p -> ht_next;
		}
		if(p != 0)
		{
			return &p-> info;
		}
		else
		{
			return NULL;
		}
		
	}

        void CourseDB::printRosters(ostream & s, const StudentDB & data) const
            // outputs rosters for all courses in database
	{
		Elem *p = head;
		while(p)
		{
			p -> info.printRoster(s, data);
			cout << endl;
			p = p -> l_next;
		}
	}
