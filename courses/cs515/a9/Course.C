
/* this class provides a course.  Courses may be input and
 * output, and their name and number of credits may be extracted.
 * course comparisons are done based on their reference number.
*/

#include <iostream>
#include <string>
#include "Course.h"
#include "Student.h"
#include "StudentDB.h"
using namespace std;

//class StudentDB;

        Course::Course()
            // empty name, 0 section, 0 crn, 0 credits, 0 seats,
            // 0 students
	{
		name = "";
		section = 0;
		crn = 0;
		credits = 0;
		seats = 0;
		numStudents = 0;
		head = 0;		
		tail = 0;	
	}

        Course::Course(int myCrn)
            // empty name, 0 section, specified crn, 0 credits,
            // 0 seats, 0 students
	{	
		name = "";
		section = 0;
		crn = myCrn;
		credits = 0;
		seats = 0;
		numStudents = 0;
		head = 0;
		tail = 0;	

	}

        Course::Course(const Course & v)
            // copy constructor
	{
		copyCode(v);
	}

        Course::~Course()
            // destructor
	{
		destructCode();
	}

        Course & Course::operator = (const Course & v)
            // assignment operator
	{
	
		if(this != &v)
		{
			(*this).destructCode();
			copyCode(v);
			return (*this);
		}
		else
		{
			return (*this);
		}
	}

        void Course::input(istream & s)
            // inputs course in form
            //  name section crn credits numseats numstudents id id ...
	{
		int i;
		s >> name >> section >> crn >> credits >> seats >> numStudents;
		if(s)
		{
		destructCode();
		for(i = 0; i < numStudents; i++)
		{
			Elem * p = new Elem;
			s >> p -> id;
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

        int Course::getCredits() const
            // returns number of credits for course
	{
		return credits;
	}

        const string & Course::getName() const
            // returns name of course
	{
		return name;
	}

        int Course::getSection() const
            // returns section of course
	{
		return section;
	}

        int Course::hash(int size) const
            // returns index into hash table of specified size
	{
		
		return crn % size;
		
	}

        void Course::printRoster(ostream & s, const StudentDB & data) const
            // prints roster for course
	{
		
		Elem * p = head;
		const Student * ptr;
		s << "*********************************";
		s << "\n" << name << "-" << section << " -- " << crn << " (" << numStudents 
		<< " / " << seats << ") {" << credits << " credits}" << endl;
		while(p)
		{
			ptr = data.find(p -> id);
			if (ptr == 0)
			{
				s << "error retrieving course information" << endl;
			}		
			else
			{
				s << ptr -> getName() << " [" << p -> id << "]";
			}
			p = p -> next;	
		}
			s << "\n*********************************" << endl;
	}
	// equality ops based on course reference number
        bool Course::operator == (const Course & v) const
	{
		return crn == v.crn;
	}
        bool Course::operator != (const Course & v) const
            
	{
		return crn != v.crn;
	}

	
   	 void Course::copyCode(const Course & v)
            // common code copy constructor and assignment op
	{
	name = v.name;
	section = v.section;
	crn = v.crn;
	credits = v.credits;
	seats = v.seats;
	numStudents = v.numStudents;
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
	head -> id = v.head -> id;
	p1 = v.head -> next;
	prev = head;
	while(p1)
	{
	p2 = new Elem;
	p2 -> id = p1 -> id;
	p1 = p1 -> next;
	prev -> next = p2;
	prev = p2;
	}
	prev -> next = 0;
	}
	}
	
        void Course::destructCode()
            // common code destructor and assignment op
	{
		Elem * p = head;
		while(head != 0)
		{
		p = head;
		head = head -> next;
		delete p;
		}
	}

	istream & operator >> (istream &s, Course & v)
    	// input using named operation
	{
		v.input(s);
		return s;
	}


