/* frep.h
Header file for frep.c.
Author: Stephen Chambers
Date: 10/6/14
*/
#ifndef FREP_H_
#define FREP_H_

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <errno.h>
#include <ctype.h>
#include <sys/stat.h>
#include <dirent.h>
#include <pthread.h>
#include "cactus_stack.h"
#include "frep_common.h"

#define OPTIONS ":hisDrl:m:caft:qd:p"
#define WHITE_SPACE " \n\r\t\v\f"

char * substring(char*, int, int);
void * process_remote(void*);
int parse_remote_option(char*, char**, char**, char**);

#endif
