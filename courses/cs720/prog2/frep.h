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

#define OPTIONS ":hisDrl:m:cafqd:p"
#define WHITE_SPACE " \n\r\t\v\f"

/* variable to keep track of command line options */
unsigned int flags;

/* define which bit sets which option */
#define CASE_INSENSITIVE     0x0001
#define PRINTACTIONS         0x0002
#define SUMMARY              0x0004
#define REVERSE              0x0008
#define DEBUG                0x0010
#define ALL			 		 0x0020
#define NO_FOLLOW			 0x0040
#define QUIET				 0x0080
#define MAX_DEPTH			 0x0100
#define PRINT_COUNTERS		 0x0200	

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
	long bytes_read;
	long bytes_inspected;
}Frep_Info;

/* variable to keep track of all counters */
Frep_Info counters; 	 

/* maximum length of an input line */
int line_length;

/* maximum amount of lines allowed in an input file */
int max_lines;

/* maximum depth to recurse to */
int max_depth;

struct machine* init_firmware(char*, int);
void find_matches(FILE*, struct machine*, char*, int*, int*);
char* get_real_path(char*);
void print_match(FILE*, char*, int, char*);
void print_summary(FILE*, char*, int);
int proccess_dir(DIR*, char*, struct machine*, int);
char* get_full_path(char*);
int check_file(char*);
void log_error(char*);
void process_file(struct machine*, char*, int*, int*, int);
char * get_cwd();

#endif
