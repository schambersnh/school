#ifndef BFCOMPACT_H_
#define BFCOMPACT_H_

//The Hole element
typedef struct Hole{
  int startLocation;
  int size;
  struct Hole * prev;
  struct Hole * next;
}Hole;

//The Process element
typedef struct Process{
  int pid;
  int size;
  int startLocation;
  struct Process * next;
}Process;

Hole * traverseRight(int, int);
Hole * traverseLeft(int, int);
void addProcess(Hole*, int, int);
void removeProcess(int);
void sortHoles(Hole*, int);
void compactMemory(int);
int addHole(int);
int isProcessDefined(int);

#endif