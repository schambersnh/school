#ifndef COURSEDB_H
#define COURSEDB_H

/* this class provides a course data base.  Courses may be
 * looked up by their course reference number, and they may be
 * output in original insertion order.
*/

#include <iostream>
using namespace std;

#include "Course.h"

class StudentDB;

class CourseDB {

    public:
        // exceptions
            class Duplicate{};

        CourseDB(int size = 1000);
            // constructs empty database with specified number of
            // elements in array

        ~CourseDB();
            // destructor

        void insert(const Course & v) throw(Duplicate);
            // inserts course into database

        const Course * find(const Course & v) const;
            // returns pointer to course in database or null if
            // not found

        void printRosters(ostream & s, const StudentDB &) const;
            // outputs rosters for all courses in database


    private:
        
        // A single structure is created that is inserted onto
        // both the hash table chain and the insertion order
        // list.  There are two next fields in the element, one
        // for the hash table chain, and one for the list.

        struct Elem {
            Course info;
            Elem * ht_next;
            Elem * l_next;
        };

        int expected;  // expected number of entries in hash table

        Elem ** table; // dynamically allocated array of element
                       // pointers for chaining implementation
                       // hash table

        Elem * head;   // head and tail pointers for insertion
        Elem * tail;   // ordered list to maintain original
                       // insertion order for final output

        CourseDB(const CourseDB &);
        CourseDB & operator = (const CourseDB &);
            // copy constructor, assignment operator -- not
            // implemented

};

#endif
