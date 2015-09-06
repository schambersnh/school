#ifndef FREP_COMMON_H_
#define FREP_COMMON_H_

/* Stephen Chambers
 frep_common.h */

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

/* Struct to keep track of counters */
typedef struct Frep_Info{
	int soft_links_ignored;
	int dir_opened;
	int dir_loops;
	int dir_depth_pruned;
	int max_dir_depth;
	int hidden_names;
	int quiet_errors;
	int files_read;
	int lines_read;
	int lines_matched;
	int bytes_read;
	int bytes_inspected;
	int threads_created;
	int threads_pruned;
	int max_threads;
}Frep_Info;

typedef struct Dir_Info{
	char * full_dir_path;
	Elem * cur_elem;
	struct machine * firmware;
	int * thread_count;
	Frep_Info * master_counters;
	int options[5];
	Elem * root;
	int fd;
}Dir_Info;

typedef struct IP_Thread_Info{
	char * regex;
	char * node;
	char * port;
	char * remote_name;
	Elem * cur_elem;
	Elem * root;
	Frep_Info * master_counters;
	int options[5];
}IP_Thread_Info;

/* thread mutex and condition variables */
pthread_mutex_t mu;

void * process_dir(void*);
struct machine* init_firmware(char*, int);
void find_matches(FILE*, struct machine*, char*, int*, int*, Frep_Info *, int[]);
char* get_real_path(char*, int);
void print_match(FILE*, char*, int, char*, int);
void print_summary(FILE*, char*, int, int);
char* get_full_path(char*, char*);
int check_file(char*, int);
void log_error(char*, Frep_Info *, int);
void process_file(struct machine*, char*, int*, int*, int, Frep_Info *, int[]);
char* get_cwd();
void process_loop(Frep_Info*, Elem*, char*, char*, int, int);
int scan_switch_number(int, int*);
void update_counters(Frep_Info*, Frep_Info*);
void print_counters(Frep_Info*);
void send_counters(int, Frep_Info*);

#endif
