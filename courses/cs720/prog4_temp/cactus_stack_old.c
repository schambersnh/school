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
	
	/* initialize ref_count to 1 */
	dir_elem -> ref_count = 1;
	
	/* a new elem does not have dependencies */
	dir_elem -> depend_head = NULL;
	dir_elem -> predecessor = NULL;
	dir_elem -> next = NULL;
	dir_elem -> prev = NULL;
	
	return dir_elem;
}
/* Add element to the end of the list */
void 
add_elem(Elem * predecessor, Elem * new_elem)
{		
	/* no need to do anything futher, this branch is empty */
	if(predecessor == NULL)
	{
		root = new_elem;
		fprintf(stderr, "Root is %s\n", new_elem -> path);
		return;
	}
	Elem * p = predecessor -> depend_head;
	Elem * prev = predecessor -> depend_head;
	if (p == NULL) /* first dependency for this node */
	{
		predecessor -> depend_head = new_elem;
		new_elem -> prev = predecessor;
		new_elem -> predecessor = predecessor;
		predecessor -> ref_count += 1;
		return;
	}
	while (p) /* add to end */
	{
		prev = p;
		p = p -> next;
	}
	new_elem -> predecessor = predecessor;
	new_elem -> prev = prev;
	prev -> next = new_elem;
	predecessor -> ref_count += 1;
}

/* removes an elem */
void 
remove_elem(Elem * elem)
{
	fprintf(stderr, "Removing %s from stack\n", elem -> path);
	Elem * temp;
	/* decrement the reference count, as we are finished with the node */
	elem -> ref_count -= 1;
	
	/* still have references to this node, don't delete it yet */
	if(elem -> ref_count > 0)
	{
		fprintf(stderr, "%s ref_count was %d but is now %d, not removing\n", elem -> path, elem -> ref_count +1, elem -> ref_count);
		return;
	}
	if(elem == root) /* nothing else in the list but elem */
	{
		fprintf(stderr, "Removing the root, path %s depth %d\n", elem -> path, elem -> depth);
		elem -> depend_head = NULL;
		elem -> next = NULL;
		elem -> prev = NULL;
		free(elem -> path);
		free(elem);
		root = NULL;
		return;
	}
	/* reference count is 0, remove the node from the list */
	fprintf("elem : %")
	if(elem == (elem -> predecessor -> depend_head))
	{
		if(elem -> next == NULL) /* only dependency */
		{
			fprintf(stderr, "There is are no more dependencies for node %s, removing %s\n", elem -> predecessor -> path, elem -> path);
			elem -> predecessor -> depend_head = NULL;
		}
		else
		{
			fprintf(stderr, "Removing depend head from %s to %s\n", elem -> predecessor -> depend_head -> path, elem -> next -> path);
			elem -> predecessor -> depend_head = elem -> next;
			elem -> next -> prev = elem -> prev;
		}
	}
    else if(elem -> next == NULL) //end of a dependency
		elem -> prev -> next = NULL;
	else /* remove from middle of dependencies */
	{
		Elem * temp = elem -> prev;
		temp -> next = elem -> next;
		elem -> next -> prev = temp;
	}
	/* free and recursively remove all dependency list heads */
	elem -> prev = NULL;
	elem -> next = NULL;
	temp = elem -> predecessor;
	free(elem -> path);
	free(elem);
	remove_elem(temp);
}

Elem * 
find(Elem * cur_node, char * path)
{
	Elem * p = cur_node;
	while(p)
	{
		if (strcmp(path, p -> path) == 0)
			return p;
		p = p -> predecessor;
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

/* testing main */
/*int 
main()
{
	char * pa = "A";
	char * pb = "B";
	char * pc = "C";
	char * pd = "D";
	char * pe = "E";
	char * pf = "F";
	
	Elem * a = newElem(pa, 1);
	Elem * b = newElem(pb, 2);
	Elem * c = newElem(pc, 2);
	Elem * d = newElem(pd, 3);
	Elem * e = newElem(pe, 3);
	Elem * f = newElem(pf, 4);
	
	fprintf(stderr, "A: %p\n", a);
	fprintf(stderr, "B: %p\n", b);
	fprintf(stderr, "C: %p\n", c);
	fprintf(stderr, "D: %p\n", d);
	fprintf(stderr, "E: %p\n", e);
	fprintf(stderr, "F: %p\n", f);
	
	add_elem(NULL, a);
	add_elem(a, b);
	add_elem(a, c);
	add_elem(b, d);
	add_elem(b, e);
	add_elem(c, f);
	
	Elem * p = find(f, "B");
	fprintf(stderr, "Found element at %p\n", p);
	
	print_loop(f, "B", 5);
	
	
	remove_elem(a);
	remove_elem(c);
	
	Elem * p;
	p = a;
	printf("\nnode path: %s\n", p -> path);
	printf("node depth: %d\n", p -> depth);
	printf("node reference count: %d\n", p -> ref_count);
	printf("node depencies: ");
	Elem * dep = p -> depend_head;
	while(dep)
	{
		printf("%s -> ", dep -> path);
		dep = dep -> next;
	}
	//printf("\nnode predecessor: %s\n", p -> predecessor -> path);
	p = b;
	printf("\nnode path: %s\n", p -> path);
	printf("node depth: %d\n", p -> depth);
	printf("node reference count: %d\n", p -> ref_count);
	printf("node depencies: ");
	dep = p -> depend_head;
	while(dep)
	{
		printf("%s -> ", dep -> path);
		dep = dep -> next;
	}
	printf("\nnode predecessor: %s\n", p -> predecessor -> path);
	
	p = c;
	printf("\nnode path: %s\n", p -> path);
	printf("node depth: %d\n", p -> depth);
	printf("node reference count: %d\n", p -> ref_count);
	printf("node depencies: ");
	dep = p -> depend_head;
	while(dep)
	{
		printf("%s -> ", dep -> path);
		dep = dep -> next;
	}
	printf("\nnode predecessor: %s\n", p -> predecessor -> path);
	
	p = d;
	printf("\nnode path: %s\n", p -> path);
	printf("node depth: %d\n", p -> depth);
	printf("node reference count: %d\n", p -> ref_count);
	printf("node depencies: ");
	dep = p -> depend_head;
	while(dep)
	{
		printf("%s -> ", dep -> path);
		dep = dep -> next;
	}
	printf("\nnode predecessor: %s\n", p -> predecessor -> path);
	
	p = e;
	printf("\nnode path: %s\n", p -> path);
	printf("node depth: %d\n", p -> depth);
	printf("node reference count: %d\n", p -> ref_count);
	printf("node depencies: ");
	dep = p -> depend_head;
	while(dep)
	{
		printf("%s -> ", dep -> path);
		dep = dep -> next;
	}
	printf("\nnode predecessor: %s\n", p -> predecessor -> path);
	
	p = f;
	printf("\nnode path: %s\n", p -> path);
	printf("node depth: %d\n", p -> depth);
	printf("node reference count: %d\n", p -> ref_count);
	printf("node depencies: ");
	dep = p -> depend_head;
	while(dep)
	{
		printf("%s -> ", dep -> path);
		dep = dep -> next;
	}	
	printf("\nnode predecessor: %s\n", p -> predecessor -> path);	
}*/