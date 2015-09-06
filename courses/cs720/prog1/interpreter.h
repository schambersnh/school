/* interpreter.h
Header file for interpreter.c.
Author: Stephen Chambers
Date: 10/6/14
*/

/* variable to keep track of command line options */
unsigned int flags;

/* define which bit sets which option */
#define CASE_INSENSITIVE     0x0001
#define PRINTACTIONS         0x0002
#define SUMMARY              0x0004
#define REVERSE              0x0008
#define DEBUG                0x0016

#ifndef INTERPRETER_H_
#define INTERPRETER_H_
int in_set(unsigned char*, char, int);
void swap_arrays(int**, int**);
int add_action(int*, int);
int match_regex(char *, struct machine*, int*, int*);
int interpret(int, int, char, struct machine*, int*, int*);

#endif