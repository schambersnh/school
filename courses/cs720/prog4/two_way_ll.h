#ifndef TWO_WAY_LL
#define TWO_WAY_LL

#include <stdlib.h>
#include <stdio.h>
#include <string.h>

/* Elem keeping track of an absolute path of a directory */
typedef struct Elem{
	char * path;
	int depth;
	struct Elem * prev;
	struct Elem * next;
}Elem;

Elem * head;
Elem * tail;

void add_elem(Elem *);
void remove_elem(Elem *);
Elem * find(char *);
void print_list();
void clear_list();
void print_loop(char*, int);
Elem * newElem(char*, int);

#endif