// interface for CS520 threads library
//
// Note: thread_mutex_t and thread_cond_t definitions are incomplete!
//

long thread_create(void (*)(void*), void*);
void thread_yield(void);
struct TCB
{
  int esp;
  int edi;
  int esi;
  int ebx;
  struct TCB * next;
  struct thread_mutex_t * mutex;
  int * stackPointer;
};

typedef struct TCB TCB;

struct thread_mutex_t
{ 
  struct TCB * owner;
  struct TCB * wait_head;
};

typedef struct thread_mutex_t thread_mutex_t;

int thread_mutex_init(struct thread_mutex_t *);
int thread_mutex_lock(struct thread_mutex_t *);
int thread_mutex_unlock(struct thread_mutex_t *);

struct  thread_cond_t
{ 
  struct TCB * wait_head;
};
typedef struct thread_cond_t thread_cond_t;
int thread_cond_init(struct thread_cond_t *);
int thread_cond_wait(struct thread_cond_t *, struct thread_mutex_t *);
int thread_cond_signal(struct thread_cond_t *);

