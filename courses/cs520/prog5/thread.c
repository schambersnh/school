/*Stephen Chambers
5/12/12
Program 6
This program implements a threads package for the Linux Intel IA-32 architecture
*/
#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <string.h>
#include <unistd.h>
#include <pthread.h>
#include <ctype.h>
#include <signal.h>
#include <sys/time.h>
#include "thread.h"
static int initDone = 0;
//Regular ready queue

struct TCB * head;
struct TCB * end;

//start of mutex chain
struct thread_mutex_t * mutexHead;
//global flag for blocking 'critical sections' of code
int critFlag = 0;

static long thread_start(void (*func)(void*), void* info);
static void special_yield(void);
void asm_yield(struct TCB *, struct TCB *);
void asm_special_yield(struct TCB *);
static void doInit();
static long mainID;
static void sigHandler(int);

//Create a thread, malloc its stack, set the stack up to look like it yielded
long thread_create(void (*func)(void*), void* info)
{
  critFlag = 1;
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
  critFlag = 0;
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
  critFlag = 1;
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
    critFlag = 0;
    asm_yield(cur, next); 
  } 
 
}

/*Do everything thread_yield does except call asm_special_yield() instead of asm_yield()*/
static void special_yield(void)
{
  critFlag = 1;
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
    critFlag = 0;
    asm_special_yield(next); 
  } 
}
//simulate the main thread running and save its address for later
static void doInit()
{
  critFlag = 1;
 //set up signal handlers for premptive scheduling
  struct sigaction sigInfo;
  sigInfo.sa_handler = sigHandler;
  sigemptyset(&sigInfo.sa_mask);
  sigInfo.sa_flags = SA_NODEFER;

  sigaction(SIGVTALRM, &sigInfo, NULL);

  struct itimerval timerInfo;
  timerInfo.it_interval.tv_sec = 0;
  timerInfo.it_interval.tv_usec = 10000;
  timerInfo.it_value.tv_sec = 0;
  timerInfo.it_value.tv_usec = 10000;


  setitimer(ITIMER_VIRTUAL, &timerInfo, NULL);

  
  head = malloc(sizeof(struct TCB));
  end = head;
  mainID = (long)head;
  critFlag = 0;
}
void sigHandler(int signal)
{
  //yield if flag is NOT set
  if(critFlag == 1)
  {
    return;
  }
  else
  {
    thread_yield();
  }
}

int thread_mutex_init(struct thread_mutex_t * newMutex)
{
  /*
  if(mutexHead == NULL)
  {
    mutexHead = newMutex;
    return 1;
  }
  struct thread_mutex_t * p = mutexHead;
  while(p -> mutex_next != NULL)
  {
    p = p -> mutex_next;
  }
  p -> mutex_next = newMutex;
  return 1;
  */
}
int thread_mutex_lock(struct thread_mutex_t * mutexLocked)
{
  /*
  //when lock is called, currently running thread is trying to lock. Use head.
  if(mutexLocked -> thread_head == NULL)
  {
    //I am free to place myself in the wait queue, the lock is open
    mutexLocked -> thread_head = head;
    mutexLocked -> thread_head -> next = NULL;
    return 1;
  }
  else if((long)(mutexLocked -> thread_head) == (long)head)
  {
    //I'm trying to suspend myself. error
    return 0;
  }
  else
  {
    //have to block thread, check if user was stupid and thread is already in queue
    struct TCB * p = mutexLocked -> thread_head;
    while(p -> next != NULL)
    {
      if((long)p == (long)head)
      {
        //tried to lock a thread that was already waiting 
        return 0;
      }
      else
      {
        p = p -> next;
      }
    }
    p -> next = head;
    head = head -> next;
  }*/
}
int thread_mutex_unlock(struct thread_mutex_t * mutexUnlocked)
{
  /*if(mutexUnlocked -> thread_head == NULL)
  {
    //no thread holds the lock
    return 0;
  }
  else
  {
    //skip over it in mutex queue
    mutexUnlocked -> thread_head = mutexUnlocked -> thread_head -> next;

    //add to ready queue
    struct TCB * myP = mutexUnlocked -> thread_head;
    struct TCB * topHead = head;
    while(topHead -> next != NULL)
    {
      topHead = topHead -> next;
    }
    topHead -> next = myP;
  }*/
}
