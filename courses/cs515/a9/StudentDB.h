#ifndef STUDENTDB_H
#define STUDENTDB_H

/* this class provides a student data base.  Students may be
 * looked up by their id number, and they may be output in
 * original insertion order.
*/

#include <iostream>
using namespace std;

class Student;
class CourseDB;

class StudentDB {

    public:
        
        // exceptions
            
            class Duplicate{};

        StudentDB(int size = 1000);
            // constructs empty database with specified number of
            // elements in array

        ~StudentDB();
            // destructor

        void insert(const Student & v) throw(Duplicate);
            // inserts student into database.  Throws exception
            // if student already in database.

        const Student * find(const Student & v) const;
            // returns pointer to student in database or null if
            // not found

        void printSchedules(ostream & s, const CourseDB &) const;
            // prints schedules of all students in database

    private:
        
        // same structure used for hash table chains and input
        // order list.  students are dynamically allocated and
        // same student object pointed to by element in the table
        // and in the list (see diagram on assignment sheet) so
        // each student is shared by the two structures.

        struct Elem {
            Student * infoPtr;
            Elem * next;
        };

        int expected;  // expected number of entries in hash table

        Elem ** table; // dynamically allocated array of element
                       // pointers for chaining implementation
                       // hash table

        Elem * head;   // head and tail pointers for insertion
        Elem * tail;   // ordered list to maintain original
                       // insertion order for final output

        StudentDB(const StudentDB &);
        StudentDB & operator = (const StudentDB &);
            // copy constructor, assignment operator -- not
            // implemented
};

#endif
