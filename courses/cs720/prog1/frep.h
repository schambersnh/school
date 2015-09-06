/* frep.h
Header file for frep.c.
Author: Stephen Chambers
Date: 10/6/14
*/
#ifndef FREP_H_
#define FREP_H_

/* maximum length of an input line */
int line_length;

/* maximum amount of lines allowed in an input file */
int max_lines;

struct machine* init_firmware(char*, int);
void find_matches(FILE*, struct machine*, char*, int*, int*);
char* get_real_path(char*);
void print_match(FILE*, char*, int, char*);
void print_summary(FILE*, char*, int);

#endif
