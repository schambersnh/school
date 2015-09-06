
/* this class provides a student.  Students may be input and
 * output, and their name and id may be extracted.  comparison of
 * students is based on their id.
*/

#include <iostream>
#include <string>
#include "Student.h"
#include "Course.h"
#include "CourseDB.h"
#include <string>
using namespace std;

class CourseDB;


        Student::Student()
            // empty name, id, no courses
	{
		name = "";
		id = "";
		numCourses = 0;
		head = 0;
		tail = 0;
	}

        Student::Student(const string myId)
            // empty name, specified id, no courses	
	{
		name = "";
		id = myId;
		numCourses = 0;
		head = 0;
		tail = 0;
	}

        Student::Student(const Student & v)
            // copy constructor
	{
	name = v.name;
    id = v.id;
    numCourses = v.numCourses;
	Elem * p1 = v.head;
	Elem * p2 = head;
	Elem * prev;
	if(v.head == 0)
	{
	head = 0;
	}
	else
	{
	head = new Elem;
	head -> crn = v.head -> crn;
	p1 = v.head -> next;
	prev = head;
	while(p1)
	{
	p2 = new Elem;
	p2 -> crn = p1 -> crn;
	p1 = p1 -> next;
	prev -> next = p2;
	prev = p2;
	p2 = p2 -> next;
	}
	prev -> next = 0;
	}
	}

        Student::~Student()
            // destructor
	{
		Elem * p = head;
		while(head != 0)
		{
		p = head;
		head = head -> next;
		delete p;
		}
	}

        Student & Student::operator = (const Student & v)
            // assignment operator
	{
		//////////////destruct code///////////////
			if(this != &v)
		{
			Elem * p = head;
			while(head != 0)
			{
			p = head;
			head = head -> next;
			delete p;
		}
		//////////////////////////////////////////

		//////////////copy code///////////////////
		name = v.name;
		id = v.id;
		numCourses = v.numCourses;
		Elem * p1 = v.head;
		Elem * p2 = head;
		Elem * prev;
		if(v.head == 0)
		{
		head = 0;
		}
		else
		{
		head = new Elem;
		head -> crn = v.head -> crn;
		p1 = v.head -> next;
		prev = head;
		
		while(p1)
		{
		p2 = new Elem;
		p2 -> crn = p1 -> crn;
		p1 = p1 -> next;
		prev -> next = p2;
		prev = p2;
		p2 = p2 -> next;
		}
		prev -> next = 0;
		}
		///////////////////////////////////
			return (*this);
		}
		else
		{
			return (*this);
		}
	}

        void Student::input(istream & s)
            // inputs student in form
            //   name: id numCourses crn crn ...
	{	
		int i;
		string firstName;
		getline(s, name, ':');
		s >> id >> numCourses;
		if(s)
		{
			Elem * p = head;
			while(head != 0)
			{
			p = head;
			head = head -> next;
			delete p;
			}
		//add to chain
		for(i = 0; i < numCourses; i++)
		{
			Elem * p = new Elem;
			s >> p -> crn;
			p -> next = 0; 
			if(head == 0)
			{
				head = p;
			}
			else
			{
				tail -> next = p;
			}
			tail = p;
		}
		}
	}

        const string & Student::getName() const
            // returns student's name
	{
		return name;
	}

        const string & Student::getId() const
            // returns student's id
	{
		return id;
	}

        int Student::hash(int size) const
            // returns hash index for table of specified size
	{
		int charSum = 0;
		int i;
		for(i = 0; i < id.length(); i++)
		{
			charSum += id.at(i);
		}

		return charSum % size;
	}

        void Student::printSchedule(ostream & s, const CourseDB & data) const
            // prints student schedule
	{
		
		Elem * p = head;
		const Course * ptr;
		int numCredits = 0;
			
		s << "===================================";
		s << "\n" << name << " -- " << id << endl;
		while(p)
		{
			ptr = data.find(p -> crn);
			if (ptr == 0)
			{
				s << "error retrieving course information" << endl;
			}		
			else
			{
				numCredits += ptr -> getCredits();
				s << "\t" << ptr -> getName() << " (" << ptr -> getSection() << ") [" << p -> crn << "]" << endl;
			}
			p = p -> next;
		}
		cout << "total credits: " << numCredits << endl;
		s << "===================================" << endl;
	
	}
	// equality ops based on id
        bool Student::operator == (const Student & v) const
	{
		return id == v.id;
	}
        bool Student::operator != (const Student & v) const
	{
		return id != v.id;
	}
            
           istream & operator >> (istream &s, Student & v)
   	 // input using named operation
	{
		v.input(s);
		return s;
	}


