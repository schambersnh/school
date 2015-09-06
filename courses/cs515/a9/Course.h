#ifndef COURSE_H
#define COURSE_H

/* this class provides a course.  Courses may be input and
 * output, and their name and number of credits may be extracted.
 * course comparisons are done based on their reference number.
*/

#include <iostream>
#include <string>
using namespace std;

class StudentDB;

class Course {

    public:
        Course();
            // empty name, 0 section, 0 crn, 0 credits, 0 seats,
            // 0 students

        Course(int myCrn);
            // empty name, 0 section, specified crn, 0 credits,
            // 0 seats, 0 students

        Course(const Course & v);
            // copy constructor

        ~Course();
            // destructor

        Course & operator = (const Course & v);
            // assignment operator

        void input(istream & s);
            // inputs course in form
            //  name section crn credits numseats numstudents id id ...

        int getCredits() const;
            // returns number of credits for course

        const string & getName() const;
            // returns name of course

        int getSection() const;
            // returns section of course

        int hash(int size) const;
            // returns index into hash table of specified size

        void printRoster(ostream & s, const StudentDB &) const;
            // prints roster for course

        bool operator == (const Course & v) const;
        bool operator != (const Course & v) const;
            // equality ops based on course reference number

    private:
        string name;
        int section;
        int crn;
        int credits;
        int seats;
        int numStudents;

        struct Elem {
            string id;
            Elem * next;
        };

        Elem * head;
        Elem * tail;
            // input order chain
        
        void copyCode(const Course & v);
            // common code copy constructor and assignment op
        void destructCode();
            // common code destructor and assignment op
};

istream & operator >> (istream &s, Course & v);
    // input using named operation

#endif
