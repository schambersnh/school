/*
cactus_stack.c
Implementation of a reference counting cactus stack.
cactus_stack.c
Author: Stephen Chambers
Date: 11/7/14
*/
#define _POSIX_C_SOURCE 200809L
#define _ISOC99_SOURCE
#define _XOPEN_SOURCE 700
#define __EXTENSIONS__

#include "cactus_stack.h"
#include <pthread.h>
#include "frep.h"
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
	
	/* copy new memory into the path */
	if(path != NULL)
	{
		/* allocate space to path */
		dir_elem -> path = malloc((strlen(path) + 1) * sizeof(char));
		if (dir_elem -> path == NULL)
		{
			fprintf(stderr, "Error in allocationg memory for directory path");
		}
		strcpy(dir_elem -> path, path);
	}
	dir_elem -> depth = depth;
	
	/* initialize ref_count to 1 */
	dir_elem -> ref_count = 1;
	
	/* a new elem does not have a predecessor by default */
	dir_elem -> predecessor = NULL;
	
	return dir_elem;
}
/* Add element to the end of the list */
void 
add_elem(Elem * predecessor, Elem * new_elem)
{		
	if(predecessor != NULL)
	{
		new_elem -> predecessor = predecessor;
		predecessor -> ref_count += 1;
	}
}

/* removes an elem */
void 
remove_elem(Elem * elem)
{
	/* decrement the reference count, as we are finished with the node */
	elem -> ref_count -= 1;
		
	/* still have references to this node, don't delete it yet */
	if(elem -> ref_count > 0)
		return;
	
	remove_elem(elem -> predecessor);
}

Elem * 
find(Elem * cur_node, char * path)
{
	Elem * p = cur_node;
	
    if (pthread_mutex_lock(&mu) != 0)
	{
      	 fprintf(stderr, "error in mutex_lock in slave\n");
		 exit(EXIT_FAILURE);
	}
	
	while(p)
	{
		if (p -> path && strcmp(path, p -> path) == 0)
		{
		    if (pthread_mutex_unlock(&mu) != 0)
			{
		      	 fprintf(stderr, "error in mutex_lock in slave\n");
				 exit(EXIT_FAILURE);
			}
			return p;
		}
		p = p -> predecessor;
	}
	
    if (pthread_mutex_unlock(&mu) != 0)
	{
      	 fprintf(stderr, "error in mutex_lock in slave\n");
		 exit(EXIT_FAILURE);
	}
	
	return NULL;
}

void 
print_loop(Elem * cur_node, char * path, int cur_depth)
{
	Elem * p = cur_node;
	/* print initial cause of path */
	fprintf(stderr, "%*d, %s\n", cur_depth * 3 + 1, cur_depth, path);
	while (p && strcmp(p -> path, path) != 0)
	{
		fprintf(stderr, "%*d, %s\n", cur_depth * 3 + 1, p -> depth, p -> path);
		p = p -> predecessor;
	}
	fprintf(stderr, "%*d, %s\n", cur_depth * 3 + 1, p -> depth, p -> path);
}