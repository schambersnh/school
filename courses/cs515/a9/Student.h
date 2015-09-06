#ifndef STUDENT_H
#define STUDENT_H

/* this class provides a student.  Students may be input and
 * output, and their name and id may be extracted.  comparison of
 * students is based on their id.
*/

#include <iostream>
#include <string>
using namespace std;

class CourseDB;

class Student {

    public:
        Student();
            // empty name, id, no courses

        Student(const string myId);
            // empty name, specified id, no courses

        Student(const Student & v);
            // copy constructor

        ~Student();
            // destructor

        Student & operator = (const Student & v);
            // assignment operator

        void input(istream & s);
            // inputs student in form
            //   name: id numCourses crn crn ...

        const string & getName() const;
            // returns student's name

        const string & getId() const;
            // returns student's id

        int hash(int size) const;
            // returns hash index for table of specified size

        void printSchedule(ostream & s, const CourseDB &) const;
            // prints student schedule

        bool operator == (const Student & v) const;
        bool operator != (const Student & v) const;
            // equality ops based on id
            

    private:

        string name;
        string id;
        int numCourses;

        struct Elem {
            int crn; 
            Elem * next;
        };

        Elem * head;
        Elem * tail;
            // input order chain

        void copyCode(const Student & v);
            // common code, copy constructor, assignment op

        void destructCode();
            // common code, destructor, assignment op
};

istream & operator >> (istream &s, Student & v);
    // input using named operation

#endif
