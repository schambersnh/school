/*
Stephen Chambers
Elizabeth Varki
CS620
This program emulates a contiguous memory allocator.
*/

#include<stdio.h>
#include<stdlib.h>
#include<ctype.h>
#include"bfcompact.h"

Hole * hHead;
Process * pHead;
Hole * midPoint; //for search efficiency

int numHoles = 1;
int numProcesses = 0;
int totalBytes = 0;
int allocatedBytes = 0;

/*
Intialize memory to a specified amount of bytes. 
Create one hole with a size of 'bytes'
*/
void initializeMemory(int bytes)
{
  totalBytes = bytes;
  midPoint = malloc(sizeof(Hole));
  if(midPoint == NULL)
  {
		fprintf(stderr, "Error in allocation of memory.");
		exit(-1);
  }
  midPoint -> startLocation = 0;
  midPoint -> size = bytes;
  midPoint -> prev = NULL;
  midPoint -> next = NULL;
  
  //assign the head pointer;
  hHead = midPoint;
}
/* Allocate this many bytes to a process with pid. Return 0 if external fragementation occured,
-1 if insufficient memory or process is already defined, or 1 if successful*/
int allocate(int bytes, int pid)
{ 
  if(bytes > (totalBytes - allocatedBytes))
	{
	  fprintf(stderr, "Error: Insufficient Memory, cannot allocate space for process %d\n", pid);
	  return -1;
	}
	if(isProcessDefined(pid))
	{
		 fprintf(stderr, "Error: Process %d already defined\n", pid);
		return -1;
	}
	
  //Find the best fitting hole
  Hole * bestFit;
  if(bytes > midPoint -> size)
    bestFit = traverseRight(bytes, pid);
  else 
    bestFit = traverseLeft(bytes, pid);
	
	if(bestFit == NULL)
	{
		fprintf(stderr, "Error: External Fragmentation occured\n");
		compactMemory(pid);
		//reallocate memory
		allocate(bytes, pid);
		return 0;
	}
	

	//external fragmentation occurred and process has a hole to fit into, add to process chain
	addProcess(bestFit, bytes, pid);
	//reorganized the holes so that they are sorted in ascending order, may need to move bestFit in the chain
	sortHoles(bestFit, bytes);
	return 0;
}

/*
Determines if a process id with a given pid exists. 1 if it does, 0 if it doesn't
*/
int isProcessDefined(int pid)
{
	Process * p = pHead;
	while(p)
	{
		 if(p -> pid == pid)
			 return 1;
		 p = p -> next;
	}
	return 0;
}
/*
After the allocation of a process, a hole must be shrunken down by that processes size. With the new size, it then
needs to be resorted into the list for search algorithm efficiency.
*/
void sortHoles(Hole * bestFit, int bytes)
{
   int holeRemaining = bestFit -> size - bytes;
   if(bestFit == hHead) //remove bestFit from the list if it equals the head, but not if the head is the only element
   {
     if(hHead -> next != NULL)
		 {
			 hHead = bestFit -> next;
			 hHead -> prev = NULL;
		 }
   }
   else if(bestFit -> next == NULL) //remove bestfit from the list if it equals the tail
   {
     bestFit -> prev -> next = NULL;
   }
   else //remove bestFit from the list if it is in the middle
   {
     if(midPoint == bestFit)
     {
      if(numHoles % 2 == 0)
	     midPoint = midPoint -> prev;
	   else
	     midPoint = midPoint -> next;
     }
     Hole * temp = bestFit -> prev;
	   temp -> next = bestFit -> next;
	   bestFit -> next -> prev = temp;
   }
   if(bestFit -> size == bytes) //get rid of bestFit doesn't need to be in the list
   {
     numHoles--;
     free(bestFit);
   }
   else
   {
		//update size, find where new hole should go and put it there
		 if(holeRemaining < (hHead -> size)) //needs to be the new head!
		 {
		   if(hHead -> next == NULL)
			 {
			   bestFit -> startLocation = bestFit -> startLocation + bytes;
			   bestFit -> size = holeRemaining;
			   hHead = bestFit;
			 } 
			 else
			 {
			   bestFit -> startLocation = bestFit -> startLocation + bytes;
			   bestFit -> size = holeRemaining;
			   bestFit -> next = hHead;
			   hHead -> prev = bestFit;
			   hHead = bestFit;
			   bestFit -> prev = NULL;
			 }
		 }
		 else //traverse until the right spot is found
		 {
			 Hole * hole = hHead;
			 bestFit -> startLocation = bestFit -> startLocation + bytes;
			 bestFit -> size = holeRemaining;
			 
			 Hole * prev;
			 while(hole && holeRemaining > (hole -> size)) //traverse, should be inserted before returned hole pointer
			 {
			   prev = hole;
				 hole = hole -> next;
			 }
			 if(hole == NULL) //should be the last element in the list
			 {
				prev -> next = bestFit;
				bestFit -> prev = prev;
			 }
			 else
			 {
				 bestFit -> next = hole;
				 bestFit -> prev = hole -> prev;
				 hole -> prev -> next = bestFit;
				 hole -> prev = bestFit;
			 }
		 }
   }
}
/*
Add a process with a given size and pid to the end of the process chain.
*/
void addProcess(Hole * bestFit, int bytes, int pid)
{
	 //Add to process list
   if(pHead == NULL) //add to head
   {
		 pHead = malloc(sizeof(Hole));
		 if(pHead == NULL)
		   fprintf(stderr, "Error in allocation of memory.\n");
		 pHead -> startLocation = bestFit -> startLocation;
		 pHead -> size = bytes;
		 pHead -> pid = pid;
		 pHead -> next = NULL;
   }   
   else
   {
     //traverse and link to the end
		Process * previous = pHead;
		while(previous -> next)
			 previous = previous -> next;
		 //allocate the process
		 Process * processPointer = malloc(sizeof(Process));
		 if(processPointer == NULL)
		   fprintf(stderr, "Error in allocation of memory.\n");
		 processPointer -> startLocation = bestFit -> startLocation;
		 processPointer -> size = bytes;
		 processPointer -> pid = pid;
		 //link the process to the end of the chain
		 previous -> next = processPointer;
		 processPointer -> next = NULL;
   }
   allocatedBytes += bytes;
   numProcesses++;
}

/*
Remove a process with a given pid from the process chain
*/
void removeProcess(pid)
{
  int processBytes = 0;
	if(pHead -> pid == pid)
	{
	  processBytes = pHead -> size;
	  Process * temp = pHead;
		pHead = pHead -> next;
		free(temp);
	}
	else
	{
		Process * prev = pHead;
		while(prev -> next -> pid != pid)
			prev = prev -> next;
			
		processBytes = prev -> next -> size;
		Process * temp = prev -> next;
		prev -> next = prev -> next -> next;
		free(temp);
	}
	numProcesses--;
	allocatedBytes -= processBytes;
}

/*
Traverse right in the hole chain until best fit is found.
*/
Hole * traverseRight(int bytes, int pid)
{
  Hole * holePointer = midPoint;
  while(holePointer -> next && (bytes >= (holePointer -> size)))
    holePointer = holePointer -> next;
  if(bytes > holePointer -> size) //External Fragmentation
	  return NULL;
  else
    return holePointer;
}

/*
Traverse left in the hole chain until best fit is found.
*/
Hole * traverseLeft(int bytes, int pid)
{
  Hole * holePointer = midPoint;
  while(holePointer -> prev && (bytes <= (holePointer -> prev -> size)))
	{
		holePointer = holePointer -> prev;
	}
  return holePointer;
}

/*
Deallocate a process with a given pid. Must combine holes if needed, 
or add a new hole where the process was originally.
*/
int deallocate(int pid)
{ //deallocate memory allocated to this process
  // return 1 if successful, -1 otherwise with an error message
  if(addHole(pid) == -1) //process does not exist in memory
    return -1;
  removeProcess(pid);
	return 0;
}

/*
Add a hole where the rocess that deleted used to be. Combine holes if necessary.
*/
int addHole(int pid)
{
  //Find the process in the chain
  Process * p = pHead;
  while(p && p -> pid != pid)
  {
    p = p -> next;
  }
  if(p == NULL)
  {
    fprintf(stderr, "Error: Process does not exist in memory\n");
	return -1;
  }
  Hole * h = hHead;
	Hole * newHole;
  int combinedHoles = 0;
  while(h)
  {
    if((h -> startLocation + h -> size) == p -> startLocation) //if a hole ends when a process begins
		{
      h -> size = h -> size + p -> size;
			newHole = h;
			combinedHoles = 1;
		}
		if((p -> startLocation + p -> size) == h -> startLocation) //if a process ends where a hole begins
		{
			h -> startLocation = h -> startLocation - p -> size;
			h -> size = h -> size + p -> size;
			newHole = h;
			combinedHoles = 1;
		}
		h = h -> next;
  }
	if(combinedHoles) //if holes were combined, need to remove this hole from the list to resort
	{
		if(newHole == hHead) //remove h from the list if it equals the head, but not if the head is the only element
		 {
			 hHead = newHole -> next;
		 }
		 else if(newHole -> next == NULL) //remove h from the list if it equals the tail
		 {
			 newHole -> prev -> next = NULL;
		 }
		 else //remove h from the list if it is in the middle
		 {
			 Hole * temp = newHole -> prev;
			 temp -> next = newHole -> next;
			 newHole -> next -> prev = temp;
		 }
		 newHole -> prev = NULL;
		 newHole -> next = NULL;
	}
  else
  {
	  //Create a new hole where the process is
	  newHole = malloc(sizeof(Hole));
		if(newHole == NULL)
		  fprintf(stderr, "Error in allocaton of memory.\n");
	  newHole -> startLocation = p -> startLocation;
	  newHole -> size = p -> size;
		numHoles++;
	}
	  
	  //Add the new hole in the correct location in the hole chain
	  Hole * temp = hHead;
		if(hHead == NULL)
		{
		  newHole -> next = hHead;
			hHead = newHole;
			newHole -> prev = NULL;
		}
	  else if(newHole -> size < temp -> size) // needs to be the new head, but there is a process in the list
	  {
			newHole -> next = temp;
			temp -> prev = newHole;
			newHole -> prev = NULL;
			hHead = newHole;
	  }
	  else
	  {
			Hole * prev;
			while(temp && ((newHole -> size) > (temp -> size)))
			{
				prev = temp;
				temp = temp -> next;
			}
			if(temp == NULL) //insert it at the end
			{
				temp = newHole;
				temp -> prev = prev;
				temp -> next = NULL;
				prev -> next = temp;
			}
			else //add the newHole before temp
			{
				temp -> prev -> next = newHole;
				newHole -> prev = temp -> prev;
				temp -> prev = newHole;
				newHole -> next = temp;
			}
	  }		
  if(numHoles % 2 == 0 && midPoint -> next != NULL)
    midPoint = midPoint -> next;
		
  return 0;
}

/*
Prints out the state of memory.
*/
void printMemory()
{
  //Print memory state
  printf("Memory size = %d bytes, allocated bytes = %d, free = %d\n", 
  totalBytes, allocatedBytes, totalBytes - allocatedBytes);
  printf("There are currently %d Holes and %d processes\n", numHoles, numProcesses);

  //Print the holes
  Hole * holePointer = hHead;
  int i = 1;
	printf("Hole list:\n");
  while(holePointer != NULL)
  {
		printf("hole %d: start location = %d, size = %d\n", i, 
		holePointer -> startLocation, holePointer -> size);
		holePointer = holePointer -> next;
		i++;
  }
  
  //Print the processes
  Process * processPointer = pHead;
  i = 1;
	printf("Process list:\n");
  while(processPointer != NULL)
  {
		printf("process %d: pid = %d start location = %d, size = %d\n", i, 
		processPointer -> pid, processPointer -> startLocation, processPointer -> size);
		processPointer = processPointer -> next;
		i++;
  }
}

/*
Compacts memory by moving all of the processes to the front and leaving a large hole
at the high end of memory.
*/
void compactMemory(int pid)
{
  fprintf(stderr, "Compacting memory...\n");
  //Remove all holes
  Hole * h = hHead;
  while(h)
  {
    Hole * temp = h;
	  h = h -> next;
		free(temp);
  }
	int startLoc = 0;
	Process * p = pHead;
	while(p)
	{
	  p -> startLocation = startLoc;
		startLoc = p-> size;
		p = p -> next;
	}
	hHead = malloc(sizeof(Hole));
	hHead -> startLocation = allocatedBytes;
	hHead -> size = totalBytes - allocatedBytes;
	hHead -> prev = NULL;
	hHead -> next = NULL;
	midPoint = hHead;
	numHoles = 1;
	fprintf(stderr, "Memory successfully compacted. Now adding process %d.\n", pid);
}

/*
The main program. Gets input for contiguous memory scheduler.
*/
int main()
{     
  int initBytes = 0;
  int numBytes = 0;
  int pid = 0;
  
  //Initialize memory and get rid of newline at the end
  scanf("%d", &initBytes);
  printf("%d\n", initBytes);
  
  initializeMemory(initBytes);
  
  //Grab the command and call the appropiate method
  char c = getchar();
  while(c != EOF)
  {
    if(!isspace(c))
      printf("%c\n", c);
    if(c == 'A') //allocate
    {
      scanf("%d", &numBytes);
      scanf("%d", &pid);    
			printf("%d %d\n", numBytes, pid);
			allocate(numBytes, pid);
    }
    else if(c == 'D') //deallocate
    {
      scanf("%d", &pid);
			printf("%d\n", pid);
  	  deallocate(pid);
    }
    else if(c == 'P') //print memory
      printMemory();
		else if(isspace(c))
			; // do nothing but don't hit the else
		else
			fprintf(stderr, "Invalid command, must be 'A' 'D' or 'P'");
		c = getchar();
  }
	return 0;
}
