/*Stephen Chambers
Lab 11
4/20/12
This lab increments a count whenever a signal has been detected.
*/
#include <stdio.h>
#include <stdlib.h>
#include <signal.h>
#include <sys/time.h>
int count = 0;

void sigHandler(int);
int main()
{
  struct sigaction info;
  info.sa_handler = sigHandler;
  sigemptyset(&info.sa_mask);
  info.sa_flags = SA_NODEFER;

  sigaction(SIGVTALRM, &info, NULL);

  struct itimerval timerInfo;
  timerInfo.it_interval.tv_sec = 0;
  timerInfo.it_interval.tv_usec = 10000;
  timerInfo.it_value.tv_sec = 0;
  timerInfo.it_value.tv_usec = 10000;


  setitimer(ITIMER_VIRTUAL, &timerInfo, NULL);

  while(count != 100)
  {
    //do nothing
  }
  fprintf(stderr, "got to end of while loop, exiting...\n");
  exit(-1);
}
void sigHandler(int signal)
{
  count++;
}
