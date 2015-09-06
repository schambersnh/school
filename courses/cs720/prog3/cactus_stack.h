/*
cactus_stack.h
Header file for a reference counting cactus stack.
cactus_stack.h
Author: Stephen Chambers
Date: 11/7/14
*/
#ifndef TWO_WAY_LL
#define TWO_WAY_LL

#include <stdlib.h>
#include <stdio.h>
#include <string.h>

/* Elem keeping track of an absolute path of a directory */
typedef struct Elem{
	char * path;
	int depth;
	int ref_count;
	struct Elem * predecessor;
}Elem;

Elem * root;

void add_elem(Elem *, Elem *);
void remove_elem(Elem *);
Elem * find(Elem *, char*);
void print_list();
void clear_list();
void print_loop(Elem*,char*, int);
Elem * newElem(char*, int);

#endif