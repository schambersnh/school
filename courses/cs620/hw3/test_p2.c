#include <stdio.h>
#include <unistd.h>

int main()
{
  int status;
  fork();
  wait(&status);
  printf("a) process id = %d\n", getpid());
  fork();
  wait(&status);
  printf("b) process id = %d\n", getpid());
  fork();
  wait(&status);
  printf("c) process id = %d\n", getpid());
  return 0;
}
