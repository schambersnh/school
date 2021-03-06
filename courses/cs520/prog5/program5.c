/*Stephen Chambers
4/17/12
Program 5
This program implements a simple threads package for the Linux Intel IA-32 architecture
*/
#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <string.h>
#include <unistd.h>
#include <pthread.h>
#include <ctype.h>
static int initDone = 0;
struct TCB
{
  int esp;
  int edi;
  int esi;
  int ebx;
  struct TCB * next;
  int * stackPointer;
};
//Regular ready queue
struct TCB * head;
struct TCB * end;

//Suspend Queue
struct TCB * susHead;
struct TCB * susEnd;

long thread_create(void (*func)(void*), void* info);
static long thread_start(void (*func)(void*), void* info);
void thread_yield(void); 
static void special_yield(void);
long thread_suspend(long);
long thread_kill(long);
long thread_self();
void asm_yield(struct TCB *, struct TCB *);
void asm_special_yield(struct TCB*);
static void doInit();
static long mainID;

//Create a thread, malloc its stack, set the stack up to look like it yielded
long thread_create(void (*func)(void*), void* info)
{
  if(!initDone)
  {
    doInit();
    initDone = 1;
  }
  //malloc a stack
  //NOTE: when stack is created, the malloc return value is at the low end of the stack.
  //thread wants the stack to grow from high to low.
  //NOTE: Stack must look like it yielded

  int garbage = 0xFFFFFFFF;
  int * threadStack = malloc(16384);
  *(threadStack + 4095) = (int)info;
  *(threadStack + 4094) = (int)func;
  *(threadStack + 4093) = garbage;
  *(threadStack + 4092) = (int)thread_start;
  *(threadStack + 4091) = garbage;

   //traverse through the chain so I can add the thread to the end of the ready queue
   struct TCB * p = head;
   while(p -> next != NULL)
   {
     p = p-> next;
   }
   //now in last element of ready queue, malloc a TCB for current thread and chain it to the end of the ready queue
   //set end equal to the last element
  struct TCB * threadTCB = malloc(sizeof(struct TCB));
  threadTCB -> stackPointer = threadStack;
  threadTCB -> esp = (int)(threadTCB -> stackPointer+ 4091);
  p -> next = threadTCB;
  end = threadTCB;
  return (long)threadTCB;
}

static long thread_start(void (*func)(void*), void* info)
{
  func(info);
  special_yield();  //will never return, don't save current threads TCB (only access next not cur)
  return 1;
}

/*This function moves the current running thread to the end of the ready queue. The new thread at the
head of the queue is given the cpu*/
void thread_yield(void)
{
  if(!initDone)
  {
    doInit();
    initDone = 1;
  }
  // if ready queue is empty, return (how do I check if the ready queue is empty?) head -> next == NULL? end == head?
  if(head -> next == NULL)
  {
    //I want the thread to keep running and simply not yield
    return;
  }
  else
  {
    //rotate ready queue. first element needs to be at the end and last element needs to be at the beginning
    struct TCB * endingVal = head;
    struct TCB * p = head -> next;
    struct TCB * traverse = head;
    while(traverse -> next != NULL)
    {
        traverse = traverse -> next;
    }  
    traverse -> next = endingVal;
    traverse = traverse -> next;
    traverse -> next = NULL;
    end = traverse;
    head = p;

    struct TCB * cur = endingVal;
    struct TCB * next = head;
    //save current thread's state(register contents) in it's TCB and restore the next thread's state from it's TCB
    //asm-yield()?
    asm_yield(cur, next); 
  } 
}

/*Do everything thread_yield does except call asm_special_yield() instead of asm_yield()*/
static void special_yield(void)
{
  if(!initDone)
  {
    doInit();
    initDone = 1;
  }
  // if ready queue is empty, return (how do I check if the ready queue is empty?) head -> next == NULL? end == head?
  if(head -> next == NULL)
  {
    //I want the thread to keep running and simply not yield
    return;
  }
  else
  {
    //rotate ready queue. first element needs to be at the end and last element needs to be at the beginning
    struct TCB * p = head;
    head = head -> next;
    free(p);
    struct TCB * next = head;
    //save current thread's state(register contents) in it's TCB and restore the next thread's state from it's TCB
    if(head -> next == NULL)
    {
      //printf("Process needs to be halted! No more active threads\n");
    } 
    asm_special_yield(next); 
  } 
}
/*This method moves a TCB out of the ready queue and puts it in the suspend chain*/
long thread_suspend(long threadID)
{
  if(!initDone)
  {
    doInit();
    initDone = 1;
  }
  struct TCB * prev = NULL;
  struct TCB * temp = head;
  
  if(head -> next == NULL && (long)head == threadID)
  {
    fprintf(stderr, "Trying to suspend only active thread! HALTING PROGRAM\n");  
    exit(-1);
  }
  while(temp)
  {
    if((long)head == threadID)
    {
      //SPECIAL CASE, ACTIVE THREAD IS SUSPENDING
      if(susHead == NULL)
      {
        susHead = head;
	head = head -> next;
        susHead -> next = NULL;
        asm_yield(susHead, head); 
        return 1;
      }
      else
      {
        struct TCB * p = susHead;
        while(p -> next)
        {
          p = p -> next;
        }
        p -> next = head;
        struct TCB * temp2 = head;
        head = head -> next;
        p -> next -> next = NULL;
        asm_yield(temp2, head);
        return 1;
      }
    }
    else
    {
      if((long)temp == threadID)
      {
        if(susHead == NULL)
        {
          prev -> next = temp -> next;
          susHead = temp;
          temp -> next = NULL;
          return 1;
        }
        else
        {
           //add to end of sus chain
           struct TCB * p = susHead;
           while(p -> next)
           {
             p = p -> next;
           }
           prev -> next = temp -> next;
           p -> next = temp;
           temp -> next = NULL;
           return 1;
        }
      }
      prev = temp;
      temp = temp -> next;
    }
  } // end of while
  
  struct TCB * myP = susHead;
  while(myP)
  {
    if((long)myP == threadID)
    {
      return 1;
    }
    myP = myP -> next;
  }
//couldnt find either of them
  return 0;
}
//Finds a thread in the suspended list and puts it at the end of the ready queue
long thread_continue(long threadID)
{
  if(!initDone)
  {
    doInit();
    initDone = 1;
  }
  struct TCB * prev = NULL;
  struct TCB * temp = susHead;
  while(temp)
  {
    if((long)susHead == threadID)
    {
      struct TCB * p = head;
      while(p -> next)
      {
        p = p -> next;
      }
      p -> next = susHead;
      p -> next -> next = NULL;
      susHead = susHead -> next;
      return 1; 
    }
    else
    {
      if((long)temp == threadID)
      {
        struct TCB * p = head;
        while(p -> next)
        {
          p = p -> next;
        }
        p -> next = temp;
        p -> next -> next = NULL;
        prev -> next = temp -> next;
        return 1;
      }
    }
    prev = temp;
    temp = temp -> next;
  }
  //didn't find it
  return 0;
}
//returns the current id
long thread_self()
{
  if(!initDone)
  {
    doInit();
    initDone = 1;
  }
	return (long)head;
}
//finds the thread in either the ready queue or the suspended queue and free its memory
long thread_kill(long threadID)
{
  if(!initDone)
  {
    doInit();
    initDone = 1;
  }
  struct TCB * prev = NULL;
  struct TCB * temp = head;
  
  if(mainID == threadID)
  {
    return 0;
  }
  if(head -> next == NULL && (long)head == threadID)
  {
    fprintf(stderr, "Trying to kill only active thread! HALTING PROGRAM\n");
    exit(-1);
  }
  while(temp)
  {
    if((long)head == threadID)
    {
      struct TCB * temp2 = head;
      head = head -> next;
      asm_yield(temp2, head);
      free(temp2);
      return 1;
    }
    else
    {
      if((long)temp == threadID)
      {
        prev -> next = temp -> next;
        free(temp);
        return 1;
      }
    }
    prev = temp;
    temp = temp -> next;
  }
  //didn't find it in ready queue, look through suspend queue
  struct TCB * susPrev = NULL;
  struct TCB * susTemp = susHead;
  while(susTemp)
  {
    if((long)susHead == threadID)
    {
      struct TCB * save = head;
      susHead = susHead -> next;
      free(save);
      return 1;
    }
    else
    {
      if((long)susTemp == threadID)
      {
        susPrev -> next = susTemp -> next;
        free(susTemp);
        return 1;
      }
    }
    susPrev = susTemp;
    susTemp = susTemp -> next;
  }
  //couldn't find it in either queue
  return 0;
}
//simulate the main thread running and save its address for later
static void doInit()
{
  head = malloc(sizeof(struct TCB));
  end = head;
  mainID = (long)head;
}
