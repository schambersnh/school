#define _POSIX_C_SOURCE 199309L

#include <time.h>
#include <stdlib.h>
#include <stdio.h>
#include <sys/times.h>
#include <sys/resource.h>
#include <unistd.h>

struct timespec timer_start(){
    struct timespec start_time;
    clock_gettime(CLOCK_PROCESS_CPUTIME_ID, &start_time);
    return start_time;
}

long timer_end(struct timespec start_time){
    struct timespec end_time;
    clock_gettime(CLOCK_PROCESS_CPUTIME_ID, &end_time);
    long diffInNanos = end_time.tv_nsec - start_time.tv_nsec;
    return diffInNanos;
}

int main()
{
    struct timespec vartime = timer_start();  
    int i = 0;
    int sum = 0;
    for(i = 0; i < 1000000; i++)
        sum+=i;
    long time_elapsed_nanos = timer_end(vartime);
    float time_elapsed_seconds = time_elapsed_nanos/10000000.0;
    printf("Time taken (seconds): %f\n",  time_elapsed_seconds);

}
