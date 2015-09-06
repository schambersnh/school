README

Data structures / Search Algorithm:

Holes:
The holes of the program are made as a doubly linked list. To find the best fitting hole, the search algorithm starts at the midPoint of the 
list. If the process being allocated is greater than the midPoint's size, the list is traversed right until the best fitting hole is found. The
same principle works for the opposite direction. This algorithm relies on the list being sorted based on size. Therefore, whenever a process is allocated it shrinks the hole it is fitting into. That hole is then removed from the linked list and readded in the correct sorted position. Whenever the number of holes is even, the midPoint is moved to the next element in the list; ensuring it always stays in the middle of the chain.This algorithm's search efficiency is n/2, a marked improvement over the brute force search of n.

Processes:
The processes are made with a singly linked list, and are added one after another and deleted when necessary.

Compaction Policy:
This program compacts memory by shifting all processes to the lower region of memory; one after another. It then creates a large hole at the
high region of memory; after the last process.

Compiling the executables:
make bestfit: makes the bestfit executable
make bfcompact: makes the bfcompact executable

Execution of the program:
./bestfit < inputfile
./bfcompact < inputfile