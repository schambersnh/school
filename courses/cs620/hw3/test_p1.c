#include <stdio.h>
#include <unistd.h>

int main()
{
  fork();
  printf("a) process id = %d\n", getpid());
  fork();
  printf("b) process id = %d\n", getpid());
  fork();
  printf("c) process id = %d\n", getpid());
  return 0;
}
