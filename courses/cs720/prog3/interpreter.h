/* interpreter.h
Header file for interpreter.c.
Author: Stephen Chambers
Date: 10/6/14
*/

#ifndef INTERPRETER_H_
#define INTERPRETER_H_

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <errno.h>
#include <ctype.h>
#include "frep.h"

int in_set(unsigned char*, char, int);
void swap_arrays(int**, int**);
int add_action(int*, int);
int match_regex(char *, struct machine*, int*, int*, Frep_Info *);
int interpret(int, int, char, struct machine*, int*, int*);

#endif