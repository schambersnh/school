/*
two_way_ll.c
Implementation of a Two Way Linked List.
two_way_ll.c
Author: Stephen Chambers
Date: 10/19/14
*/
#define _POSIX_C_SOURCE 200809L
#define _ISOC99_SOURCE
#define _XOPEN_SOURCE 700
#define __EXTENSIONS__

#include "two_way_ll.h"
/* adds an elem to the list
0: added successfully
-1: ERROR directory was in the list */

/* Creates a new elem, mallocing space for it and its path */
Elem * 
newElem(char * path, int depth)
{
	/* create element to add */
	Elem * dir_elem = malloc(sizeof(Elem));
	if (dir_elem == NULL)
	{
		fprintf(stderr, "Could not allocate memory for path element\n");
		exit(EXIT_FAILURE);
	}
	
	/* allocate space to path */
	dir_elem -> path = malloc((strlen(path) + 1) * sizeof(char));
	if (dir_elem -> path == NULL)
	{
		fprintf(stderr, "Error in allocationg memory for directory path");
	}
	
	/* copy new memory into the path */
	strcpy(dir_elem -> path, path);
	dir_elem -> depth = depth;
	
	return dir_elem;
}
/* Add element to the end of the list */
void 
add_elem(Elem * elem)
{		
	/* add to the head */
	Elem * p = head;
	Elem * prev;
	if (p == NULL)
	{
		head = elem;
		tail = elem;
		head -> next = NULL;
		tail -> next = NULL;
		head -> prev = NULL;
		tail -> prev = NULL;
	}
	else
	{
		/* add to end of the list */
		prev = head;
		while (p)
		{
			prev = p;
			p = p -> next;
		}
		elem -> prev = prev;
		elem -> next = NULL;
		prev -> next = elem;
		tail = elem;
	}
}

/* removes an elem */
void 
remove_elem(Elem * elem)
{
	/* remove from the head */
	if(elem == head)
	{
		/* more than one thing in the list */
		if(elem -> next)
		{
			head = elem -> next;
			head -> prev = NULL;
		}
		else
			head = NULL;

	}
	else if(elem -> next == NULL) //tail
	{
		tail = elem -> prev;
		elem -> prev -> next = NULL;
	}
	else /* remove from middle */
	{
		Elem * temp = elem -> prev;
		temp -> next = elem -> next;
		elem -> next -> prev = temp;
	}
	elem -> prev = NULL;
	elem -> next = NULL;
	free(elem -> path);
	free(elem);
}

/* free memory in list */
void 
clear_list()
{
	Elem * p = head;
	while(p)
	{
		Elem * temp = p;
		p = p -> next;
		free(temp -> path);
		free(temp);
	}
	head = NULL;
}

/* finds path in linked list. Returns pointer if found, NULL if not found */
Elem * 
find(char * path)
{
	Elem * p = head;
	while(p)
	{
		if (strcmp(path, p -> path) == 0)
			return p;
		p = p -> next;
	}
	return NULL;
}

/* prints the list */
void 
print_list()
{
	Elem * p = head;
	printf("*********** PRINTING **************\n");
	while (p)
	{
		fprintf(stderr, "Elem -> %s\n", p -> path);
		p = p -> next;
	}
}

/* prints the loop ending with elem containing relpath parameter */
void 
print_loop(char * path, int cur_depth)
{
	Elem * p = tail;
	/* print initial cause of path */
	fprintf(stderr, "%*d,%s\n", cur_depth * 3 + 1, cur_depth, path);
	while (p)
	{
		if (p -> next != NULL && (strcmp(p -> next -> path, path)) == 0)
			break;
		fprintf(stderr, "%*d, %s\n", cur_depth * 3 + 1, p -> depth, p -> path);
		p = p -> prev;
	}
}