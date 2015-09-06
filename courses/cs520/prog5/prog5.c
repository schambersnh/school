/*Stephen Chambers
4/17/12
Program 5
This program implements a simple threads package for the Linux Intel IA-32 architecture
*/
static int initDone = 0;
struct TCB
{
  int esp;
  int edi;
  int esi;
  int ebx;
  struct TCB * next;
  int * stackPointer;
}
struct TCB * head;
struct TCB * end;

long thread_create(void (*func)(void*), void* info);
static long thread_start(void (*func)(void*), void* info);
void thread_yield(void); 
static void special_yield(void);
static void doInit();

int main()
{
  return 0;  
}

//What does this mean?

/*When the main thread exits, then the process exits and all created threads are therefore terminated. When a created thread returns from its given function, then the thread is terminated, it is removed from the list of active threads, and any associated memory should be freed. Normally the next thread in the list of active threads should now execute. If there are no other active threads, then an error message should be printed to stderr and the program should be halted (call exit(-1)).*/ 
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

  int garbage = 10;
  int * threadStack = malloc(16384);
  //will info and func be 32 bits? is this how I physically place things in the stack?
  (threadStack + 4095) = info;
  (threadStack + 4094) = func;
  (threadStack + 4093) = garbage;
  //put thread_start here, how?
  (threadStack + 4092) = thread_start(func, info);
  (threadStack + 4091) = garbage;

   threadStack += 16384;

   //traverse through the chain so I can add the thread to the end of the ready queue
   struct TCB * p = head;
   while(p -> next != NULL)
   {
     p = p-> next;
   }
   //now in last element of ready queue, malloc a TCB for current thread and chain it to the end of the ready queue
   //set end equal to the last element
  struct TCB * threadTCB = malloc(sizeof(TCB)); // probably have to do more then just create one, how do I know what the esp and registers are?
  threadTCB -> stackPointer = threadStack;
  p -> next = threadTCB;
  end = threadTCB;
}

static long thread_start(void (*func)(void*), void* info)
{
  func(info);
  special_yield();  //will never return, don't save current threads TCB (only access next not cur)
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
    struct TCB * p = head;
    head = end;
    head -> next = p;
    struct TCB * traverse = head;
    while(traverse -> next != end)
    {
        traverse = traverse -> next;
    }  
    traverse -> next = NULL;
    end = traverse;

    struct TCB * cur = p;
    struct TCB * next = head;
    asm_yield(cur, next);
    //save current thread's state(register contents) in it's TCB and restore the next thread's state from it's TCB
    //asm-yield()?
  }
  
  
  
}

/*Do everything thread_yield does except call asm_special_yield() instead of asm_yield()*/
static void special_yield(void)
{

}

static void doInit()
{
  //do other stuff?

  //notes say create empty ready queue
  head = malloc(sizeof(TCB));
  end = head;
}
