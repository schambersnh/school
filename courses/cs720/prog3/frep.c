/*
frep.c
This file searches for substrings in lines read from a set of 
files (or stdin) according to a criteria specified through the use of 
command-line switches and parameters, and will then write output as specified 
through the use of command line switches. The 'matching' portion is done by
interpreter.c
Author: Stephen Chambers
Date: 10/6/14
*/
#define _POSIX_C_SOURCE 200809L
#define _ISOC99_SOURCE
#define _XOPEN_SOURCE 700
#define __EXTENSIONS__

#include "machine.h"
#include "frep.h"
#include "interpreter.h"
#include "cactus_stack.h"
#include <pthread.h>


/* This function was modified from an in-class example.
Scans the string pointed to by optarg and tries to convert it to a number.
 *  * Returns 0 if successful (and stores the number in result),
 *   *    -1 on any error (prints an error message and leaves result unchanged)
 *    */
int
scan_switch_number(int switch_char, int *result)
{
    int temp, retval;
    char *ptr;

    errno = 0;
    temp = strtol(optarg, &ptr, 10);
    if (errno != 0  ||  ptr == optarg  ||
       strspn(ptr, WHITE_SPACE) != strlen(ptr)) 
    {
        fprintf(stderr,"Illegal numeric value \"%s\" for switch -%c\n",
                optarg, switch_char);
        retval = -1;
    } 
    else if (switch_char == 'l' && temp > 1023)
    {
        fprintf(stderr, "Illegal value \"%s\" for switch -%c, must be no more "
			"than 1023.\n", optarg, switch_char);
        retval = -1;
    }
    else if(temp <= 0 && switch_char != 'd' && switch_char != 't')
    {
        fprintf(stderr, "Value \"%s\" must be positive for switch -%c.\n", 
            optarg, switch_char);
        retval = -1;
    }
	else if((switch_char == 'd' || switch_char == 't') && temp < 0)
	{
        fprintf(stderr, "Value \"%s\" must be zero or greater for switch -%c.\n", 
            optarg, switch_char);
		retval = -1;
	}
    else 
    {
        *result = temp;
        retval = 0;
    }
    return retval;
}


/* Initialize the firmware used to match regular expressions. Exits the
program on initialization error. If successful, returns a pointer to 
the initialized firmware */
struct machine *
init_firmware(char * regex, int flags)
{
    struct machine * firmware;
    if(recompiler(regex, flags, &firmware) == - 1)
    { 
        fprintf(stderr, "Error in initializing firmware\n");
        exit(EXIT_FAILURE);
    }
    
    return firmware;
}

/* Iterates through input lines (whether they be from stdin or a file) and
calls matching functions to determine if those input lines match.
*/
void
find_matches(FILE * fp, struct machine* firmware, char * fname, int * cur_list, 
int * next_list, Frep_Info * counters)
{   
    char * input_line;
    unsigned int reverse, summary;
    int line_number;
    int match_count;
    int summary_count;
	int len;
    
    /* initialize variables */
    input_line = (char *)calloc(line_length+1, sizeof(char));
    if (input_line == NULL)
    {
        fprintf(stderr, "Error in allocating memory\n");
        exit(EXIT_FAILURE);
    }
    line_number = 0;
    summary_count = 0;
    reverse = 0;
    summary = 0;
	match_count = 0;
    
    /* check to see if REVERSE or SUMMARY was specified */
    if ((flags & REVERSE) == REVERSE)
        reverse = 1;
    if ((flags & SUMMARY) == SUMMARY)
        summary = 1;
    
    /* loop through file pointer giving each line to a function that 
    will check if regex matches */
    while (fgets(input_line, line_length+1, fp) != NULL)
    {
        line_number++;
	        
        /* trim off the newline if its there */
		pthread_mutex_lock(&mu);
		len = strlen(input_line);
        if(input_line[len - 1] == '\n')
		{
			len -= 1;
            input_line[len] = '\0';
		}
		pthread_mutex_unlock(&mu);
		        
        if (match_regex(input_line, firmware, cur_list, next_list, counters))
        {
            /* print the matching line to stdout if reverse 
                and summary are off */
            if (reverse == 0 && summary == 0)
			{
                print_match(fp, input_line, line_number, fname);
				match_count++;
			}
            else if(reverse == 0 && summary)
			{
                summary_count++;
				match_count++;
			}
        }
        else
        {
            /* print the matching line to stdout if reverse is on 
                and summary is off */
            if (reverse && summary == 0)
			{
                print_match(fp, input_line, line_number, fname);
				match_count++;
			}
            else if(reverse && summary)
			{
				summary_count++;
                match_count++;
			}
        }
		
		/* If match_count is reached, stop processing */
		if (match_count == max_lines && max_lines > 0)
			break;
    }
	/* print the summary */
    if (summary)
        print_summary(fp, fname, summary_count);
	
	counters -> lines_read += line_number;
	counters -> lines_matched += match_count;
	counters -> bytes_read += ftell(fp);
    free(input_line);
}

/* Prints a successful match. */
void 
print_match(FILE * fp, char * input_line, int line_number, char * fname)
{
    char * realpath;
    pthread_mutex_lock(&mu);
    if (fp == stdin)
        printf("%d: %s\n", line_number, input_line);
    else
    {
        realpath = get_real_path(fname);
		if(realpath == NULL)
			return;
        printf("%s,%d: %s\n", realpath, line_number, input_line);
		free(realpath);
    }
    pthread_mutex_unlock(&mu);
}

/* Prints a successful match if SUMMARY flag is enabled */
void 
print_summary(FILE * fp, char * fname, int summary_count)
{
    pthread_mutex_lock(&mu);	
    if (summary_count != 0)
    {
        char * realpath;
        if (fp == stdin)
            printf("%d\n", summary_count);
        else
        {
            realpath = get_real_path(fname);
			if(realpath == NULL)
				return;
            printf("%s: %d\n", realpath, summary_count);
        }
    }
    pthread_mutex_unlock(&mu);
}

/* update the master counter list from the thread local counters */
void
update_counters(Frep_Info * counters)
{
	master_counters.soft_links_ignored += counters -> soft_links_ignored;
	master_counters.dir_opened += counters -> dir_opened;
	master_counters.dir_loops += counters -> dir_loops;
	master_counters.dir_depth_pruned += counters -> dir_depth_pruned;
	if(master_counters.max_dir_depth < counters -> max_dir_depth)
		master_counters.max_dir_depth = counters -> max_dir_depth;
	master_counters.hidden_names += counters -> hidden_names;
	master_counters.quiet_errors += counters -> quiet_errors;
	master_counters.files_read += counters -> files_read;
	master_counters.lines_read += counters -> lines_read;
	master_counters.lines_matched += counters -> lines_matched;
	master_counters.bytes_read += counters -> bytes_read;
	master_counters.bytes_inspected += counters -> bytes_inspected;
	master_counters.threads_created += counters -> threads_created;
	master_counters.threads_pruned += counters -> threads_pruned;
}

/* This was modified from an in class example.
Gets the real path of a file. If any memory related errors occur, the program
will exit with failure status. If the function is successful, it returns a
string containting the real path of the file passed into the function. */
char *
get_real_path(char * filename)
{
    int path_max;
    char * memptr;
    char * realptr; 

    #if defined(PATH_MAX) && PATH_MAX != -1
            path_max = PATH_MAX;
    #else
            errno = 0;
            if ((path_max = pathconf(filename, _PC_PATH_MAX)) == -1){
                    if (errno != 0)
                            perror(filename);
                    /* error or unlimited, pick a big number */
                    path_max = 8192;
            }
    #endif
    memptr = calloc(path_max + 1, sizeof(char));
    if (memptr == NULL)
    {
        fprintf(stderr, "Error in allocating memory: %s\n", strerror(errno));
        exit(EXIT_FAILURE);
		realptr = NULL;
    }
    else 
    {
        realptr = realpath(filename, memptr);
        if(realptr == NULL)
		{
			if((flags & QUIET) != QUIET)
            	fprintf(stderr, "realpath error %s: %s\n", 
        			filename, strerror(errno));						
		}
    }
    return realptr;
}

/*  Process a directory from the command line
	regular file: process normally
	directory: recurse into this function again, being careful of loops
*/
void *
process_dir(void * params)
{
	/* directory variables */
	struct dirent * entry;
	struct dirent * buffer;
	char * d_name;
	char * full_dir_entry_path;
	char * realpath;
	long name_max;
	Elem * new_elem;
	Dir_Info * dir_info;
	Frep_Info thr_local_counters;
	int detached_value;
	DIR * dp;
	DIR * temp_dp;
	int cur_depth;
			
	/* stat variables */
	struct stat statbuf;
	struct stat linkstatbuf;
	
	/* regex variables */
	int * cur_list;
	int * next_list;
	
	/* thread variables */
	int errcode;
	pthread_t t_id;
	pthread_attr_t thr_attr;
	
	/* counters */
	int i;	
	
	/* intialize variables */
	i = 0;
	detached_value = PTHREAD_CREATE_DETACHED;
	cur_list = NULL;
	next_list = NULL;
		
	/* set the thread to the detached state */
	if((errcode = pthread_attr_init(&thr_attr)) != 0)
	{
		fprintf(stderr, "Error creating thread_attr in main: %s\n", 
			strerror(errcode));
		exit(EXIT_FAILURE);
	}
	
	if((errcode = pthread_attr_setdetachstate(&thr_attr, detached_value)) != 0)
	{
		fprintf(stderr, "Error setting detached in main: %s\n", 
			strerror(errcode));
		exit(EXIT_FAILURE);
	}
	
    pthread_mutex_lock(&mu);
		
	/* extract variables from params */	
	cur_depth = ((Dir_Info *)params) -> cur_elem -> depth;
		
	if ((dp = opendir(((Dir_Info *)params) -> full_dir_path)) == NULL)
	{
		log_error(((Dir_Info *)params) -> full_dir_path, &thr_local_counters);
		remove_elem(((Dir_Info *)params) -> cur_elem);
	    pthread_mutex_unlock(&mu);
		return NULL;
	}
    pthread_mutex_unlock(&mu);
	
    /* initialize firmware variables */
    cur_list = (int *)calloc(((Dir_Info *)params) -> firmware -> action_limit, sizeof(int));
    if (cur_list == NULL)
    {
        fprintf(stderr, "Error in allocating memory\n");
        exit(EXIT_FAILURE);
    }
    
    next_list = (int *)calloc(((Dir_Info *)params) -> firmware -> action_limit, sizeof(int));
    if (next_list == NULL)
    {
        fprintf(stderr, "Error in allocating memory\n");
        exit(EXIT_FAILURE);
    }
	
	/* initialize struct */
	thr_local_counters.soft_links_ignored = 0;
	thr_local_counters.dir_opened = 0;
	thr_local_counters.dir_loops = 0;
	thr_local_counters.dir_depth_pruned = 0;
	thr_local_counters.max_dir_depth = 0;
	thr_local_counters.hidden_names = 0;
	thr_local_counters.quiet_errors = 0;
	thr_local_counters.files_read = 0;
	thr_local_counters.lines_read = 0;
	thr_local_counters.lines_matched = 0;
	thr_local_counters.bytes_read = 0;
	thr_local_counters.bytes_inspected = 0;
	thr_local_counters.threads_created = 0;
	thr_local_counters.threads_pruned = 0;
	
			
	name_max = pathconf(((Dir_Info *)params) -> full_dir_path, _PC_NAME_MAX);
	if (name_max <= 0)
		name_max = 4096;	
		
	/* set up the buffer holding directory contents */
	if ((buffer = malloc(sizeof(struct dirent) + name_max + 1)) == NULL)
	{
		fprintf(stderr, "No space for dirent buffer\n");
		exit(EXIT_FAILURE);
	}
		
	thr_local_counters.dir_opened++;
	if (cur_depth > thr_local_counters.max_dir_depth)
		thr_local_counters.max_dir_depth = cur_depth;
	
	/* loop through directory contents */
	for (i = 1; ; i++)
	{
		/* read directory contents into buffer */
		if ((errno = readdir_r(dp, buffer, &entry)) != 0)
		{
			pthread_mutex_lock(&mu);
			log_error(((Dir_Info *)params) -> full_dir_path, &thr_local_counters);
			//free(((Dir_Info *)params) -> full_dir_path);
			remove_elem(((Dir_Info *)params) -> cur_elem);
			pthread_mutex_unlock(&mu);
			return NULL;
		}
		
		
		/* hit EOF, break out */
		if (entry == NULL) 
			break;
		
		/* grab the full path of the directory ENTRY name */
		d_name = entry -> d_name;
		full_dir_entry_path = get_full_path(((Dir_Info *)params) -> full_dir_path, d_name);
		
		/* grab stats of directory entry */
		if (lstat(full_dir_entry_path, &statbuf) < 0)
			log_error(full_dir_entry_path, &thr_local_counters);
		else
		{
			/* determine if file can be processed */
			if (check_file(d_name))
			{	
				/* increment hidden counter */
				if (d_name[0] == '.')
					thr_local_counters.hidden_names++;
				if (S_ISREG(statbuf.st_mode))  /* regular file */
					process_file(((Dir_Info *)params) -> firmware, full_dir_entry_path, cur_list, 
						next_list, 0, &thr_local_counters);
				else if (S_ISDIR(statbuf.st_mode)) 
				{		
					realpath = get_real_path(full_dir_entry_path);
					if(realpath == NULL)
					{
						if((flags & QUIET) == QUIET)
							thr_local_counters.quiet_errors++;
						//free(realpath);						
						continue;					
					}
					if((temp_dp = opendir(realpath)) == NULL)
					{
						log_error(full_dir_entry_path, &thr_local_counters);
						closedir(temp_dp);
						//free(realpath);
						continue;
					}
					closedir(temp_dp);
					/* if at acceptable depth and no loops, recurse */
					if(cur_depth != max_depth)
					{
						/* loop detected, return back up and don't change to directory */
						if (find(((Dir_Info *)params) -> cur_elem, realpath) != NULL)
							process_loop(&thr_local_counters, ((Dir_Info *)params) -> cur_elem,
									full_dir_entry_path, realpath, cur_depth);
						else
						{							
						    pthread_mutex_lock(&mu);
							
							if ((flags & DEBUG) == DEBUG)
								fprintf(stderr, "Entering directory %s\n", 
									realpath);
							
							/* initialize dir_info */
							dir_info = malloc(sizeof(Dir_Info));
							if(dir_info == NULL)
							{
						        fprintf(stderr, "Error in allocating memory\n");
						        exit(EXIT_FAILURE);
							}
							
							/* no loop, free to spawn thread or recurse */
							dir_info -> full_dir_path = malloc(strlen(full_dir_entry_path) + 1);
							if(dir_info -> full_dir_path == NULL)
							{
								fprintf(stderr, "Insufficient memory, thread params\n");
								exit(EXIT_FAILURE);
							}
							dir_info -> cur_elem = malloc(sizeof(Elem));
							if(dir_info -> cur_elem == NULL)
							{
						        fprintf(stderr, "Error in allocating memory\n");
						        exit(EXIT_FAILURE);
							}
							dir_info -> firmware = malloc(sizeof(struct machine));
							if(dir_info -> firmware == NULL)
							{
						        fprintf(stderr, "Error in allocating memory\n");
						        exit(EXIT_FAILURE);
							}
							
														
							strcpy(dir_info -> full_dir_path, full_dir_entry_path);
							dir_info -> firmware = ((Dir_Info *)params) -> firmware;
							new_elem = newElem(realpath, cur_depth + 1);
							add_elem(((Dir_Info *)params) -> cur_elem, new_elem);	
							dir_info -> cur_elem = new_elem;
														
							thread_count++;
														
							if (thread_count <= thread_limit && thread_count != 0)
							{
								if ((errcode = pthread_create(&t_id, &thr_attr, 
										process_dir, (void*)dir_info)) != 0)
								{
									fprintf(stderr, "Error creating thread: %s\n", 
										strerror(errcode));
									pthread_mutex_unlock(&mu);
									process_dir(dir_info);
								}
								thr_local_counters.threads_created++;
								if(master_counters.max_threads < thread_count)
									master_counters.max_threads = thread_count;										
								pthread_mutex_unlock(&mu);
							}
							else
							{
								thr_local_counters.threads_pruned++;
								if((flags & QUIET) != QUIET)
									fprintf(stderr, "Thread limit of %d reached\n", 
										thread_limit);
								else
									master_counters.quiet_errors++;
								/* recurse */
								pthread_mutex_unlock(&mu);
								process_dir(dir_info);								
							}									
						}
						//free(realpath);
					}
					else
					{
						thr_local_counters.dir_depth_pruned++;
						if((flags & QUIET) != QUIET)
							fprintf(stderr, "Max_depth of %d "
								"exceeded by %s\n", max_depth, full_dir_entry_path);
						else
							thr_local_counters.quiet_errors++;
					}
				} /* end directory */
				else if (S_ISLNK(statbuf.st_mode)) 
				{		
					/* make sure user did not specify to not follow symbolic links */
					if ((flags & NO_FOLLOW) != NO_FOLLOW)
					{
						if (stat(full_dir_entry_path, &linkstatbuf) < 0)
							log_error(full_dir_entry_path, &thr_local_counters);
						else
						{
							if(S_ISREG(linkstatbuf.st_mode))
								process_file(((Dir_Info *)params) -> firmware, full_dir_entry_path, 
									cur_list, next_list, 0, &thr_local_counters);
							else
							{					
								/* get the realpath of the full path */
								realpath = get_real_path(full_dir_entry_path);
								if(realpath == NULL)
								{
									if((flags & QUIET) == QUIET)
										thr_local_counters.quiet_errors++;
									continue;
								}
								
								/* check to see if we can descend into a directory */
								if(cur_depth != max_depth)
								{
									/* loop detected, return back up and don't change to directory */
									if (find(((Dir_Info *)params) -> cur_elem, realpath) != NULL)
										process_loop(&thr_local_counters, ((Dir_Info *)params) -> cur_elem,
											full_dir_entry_path, realpath, cur_depth);
									else
									{
										/* no loop detected, increase the depth and 
											spawn thread or recurse if 
												cur_depth != max_depth */
										if ((flags & DEBUG) == DEBUG)
											fprintf(stderr, "Entering directory %s\n", 
												realpath);
																				
									    pthread_mutex_lock(&mu);
										
										/* initialize dir_info */
										dir_info = malloc(sizeof(Dir_Info));
										if(dir_info == NULL)
										{
									        fprintf(stderr, "Error in allocating memory\n");
									        exit(EXIT_FAILURE);
										}
							
										/* no loop, free to spawn thread or recurse */
										dir_info -> full_dir_path = malloc(strlen(realpath) + 1);
										if(dir_info -> full_dir_path == NULL)
										{
											fprintf(stderr, "Insufficient memory, thread params\n");
											exit(EXIT_FAILURE);
										}
										dir_info -> cur_elem = malloc(sizeof(Elem));
										if(dir_info -> cur_elem == NULL)
										{
									        fprintf(stderr, "Error in allocating memory\n");
									        exit(EXIT_FAILURE);
										}
										dir_info -> firmware = malloc(sizeof(struct machine));
										if(dir_info -> firmware == NULL)
										{
									        fprintf(stderr, "Error in allocating memory\n");
									        exit(EXIT_FAILURE);
										}
										
										strcpy(dir_info -> full_dir_path, realpath);
										dir_info -> firmware = ((Dir_Info *)params) -> firmware;
										new_elem = newElem(realpath, cur_depth + 1);
										add_elem(((Dir_Info *)params) -> cur_elem, new_elem);	
										dir_info -> cur_elem = new_elem;
																				
										thread_count++;
										if (thread_count <= thread_limit && thread_count != 0)
										{
											if ((errcode = pthread_create(&t_id, &thr_attr, 
												process_dir, (void*)dir_info)) != 0)
											{
												fprintf(stderr, "Error creating thread: %s\n", 
													strerror(errcode));
												pthread_mutex_unlock(&mu);
												process_dir(dir_info);
											}
											thr_local_counters.threads_created++;
											if(master_counters.max_threads < thread_count)
												master_counters.max_threads = thread_count;										
											pthread_mutex_unlock(&mu);
										}
										else
										{
											thr_local_counters.threads_pruned++;
											if((flags & QUIET) != QUIET)
												fprintf(stderr, "Thread limit of %d reached\n", thread_limit);
											else
												master_counters.quiet_errors++;
											/* recurse */
										    pthread_mutex_unlock(&mu);
											process_dir(dir_info);
										}												
									}
								}
								else
								{
									thr_local_counters.dir_depth_pruned++;
									if((flags & QUIET) != QUIET)
										fprintf(stderr, "Max_depth of %d "
											"exceeded by %s\n", max_depth, full_dir_entry_path);
									else
										thr_local_counters.quiet_errors++;
								}
								//free(realpath);
							}		
						}	
					} 
					else
					{
						thr_local_counters.soft_links_ignored++;
						/* print fullpath of symlink found if quiet is off */
						if ((flags & QUIET) != QUIET)
							fprintf(stderr, "symlink not followed: %s\n", 
							full_dir_entry_path);
						else
							thr_local_counters.quiet_errors++;
					}
				} /* end symlink */		
			} /* end if hidden */   
		}
		//free(full_dir_entry_path);
	} /* end for loop */
	if (errno)
		perror(((Dir_Info *)params) -> full_dir_path);	
	
    pthread_mutex_lock(&mu);
	
	update_counters(&thr_local_counters);
	
	/* free variables */
	free(cur_list);
	//free(buffer);
	free(next_list);
	closedir(dp);
		
	remove_elem(((Dir_Info *)params) -> cur_elem);
	if(root == NULL)
	{
		if((flags & PRINT_COUNTERS) == PRINT_COUNTERS && 
				master_counters.files_read != 0)
			print_counters();
	}
	thread_count--;
    pthread_mutex_unlock(&mu);
	
   	return 0;
}

void
process_loop(Frep_Info * counters, Elem * cur_elem, char * full_path, 
	 char * realpath, int cur_depth)
{
	counters -> dir_loops += 1;	
	if((flags & QUIET) != QUIET)
	{
	    pthread_mutex_lock(&mu);
		
		fprintf(stderr, "Loop detected trying to follow %s at "
			"depth %d\n", full_path, cur_depth + 1);
		print_loop(cur_elem, realpath, cur_depth + 1);
		
	    pthread_mutex_unlock(&mu);
	}
	else
		counters -> quiet_errors += 1;
}


/* Processes a file, opening it and sending it to the interpreter 
0: Success
-1: Error
*/

void 
process_file(struct machine * firmware, char * full_path, int * cur_list, 
	int * next_list, int cmdline, Frep_Info * counters)
{
	FILE * fp;
    fp = fopen(full_path, "r");
    if(fp == NULL)
	{
		if((flags & QUIET) != QUIET || cmdline == 1)
			perror(full_path);
		else
			counters -> quiet_errors += 1;
	}
    else
    {
        find_matches(fp, firmware, full_path, cur_list, next_list, counters);
        fclose(fp);
		counters -> files_read += 1;
    }
}

/* logs an error to the console if quiet is off */
void 
log_error(char * full_path, Frep_Info * counters)
{
	if((flags & QUIET) != QUIET)
		perror(full_path);
	else
		counters -> quiet_errors += 1;
}

/* checks to see if a file can be processed, taking into account a variety of
	factors (the -a flag for example).
	0: cannot process
	1: can process
*/
int 
check_file(char * d_name)
{
	int retval;
	pthread_mutex_lock(&mu);
	retval = 0;
	
	/* if '-a' is set, is okay to process if file is not '.' or '..'*/
	if((flags & ALL) == ALL)
	{
		if(strcmp(d_name, "..") != 0 && strcmp(d_name, ".") != 0)
			retval = 1;
		else
			retval = 0;
		pthread_mutex_unlock(&mu);
		return retval;	
	}
	else
	{
		if(d_name[0] != '.')
			retval = 1;
		else
			retval = 0;
		pthread_mutex_unlock(&mu);
		return retval;
	}
}

char *
get_full_path(char * cwd, char * d_name)
{
	char * full_dir_name;
	/* the full path of an (absolute) name starting with a '/' is  just
		the name itself */
	if (d_name[0] == '/')
	{
		full_dir_name = malloc(strlen(d_name) + 1);
		strcpy(full_dir_name, d_name);
		return full_dir_name;
	}
		
	full_dir_name = malloc((strlen(cwd) + strlen(d_name) + 
		strlen("/") + 1) * sizeof(char));
	if(full_dir_name == NULL)
	{
		fprintf(stderr, "Error in malloc: Insufficient memory\n");
		exit(EXIT_FAILURE);
	}
	strcpy(full_dir_name, cwd);
	full_dir_name = strcat(full_dir_name, "/");
	full_dir_name = strcat(full_dir_name, d_name);
	return full_dir_name;
}

/* prints a table of all performance counters */
void
print_counters()
{
	printf("%40s\t%d\n", "Total soft links ignored due to -f", 
		master_counters.soft_links_ignored);
	printf("%40s\t%d\n", "Total directories opened successfully", 
		master_counters.dir_opened);
	printf("%40s\t%d\n", "Total directory loops avoided", 
		master_counters.dir_loops);
	printf("%40s\t%d\n", "Total directory descents pruned by -d", 
		master_counters.dir_depth_pruned);
	printf("%40s\t%d\n", "Maximum directory descent depth", 
		master_counters.max_dir_depth);
	printf("%40s\t%d\n", "Total dot names not ignored due to -a", 
		master_counters.hidden_names);
	printf("%40s\t%d\n", "Total errors not printed due to -q", 
		master_counters.quiet_errors);
	printf("%40s\t%d\n", "Total files read", 
		master_counters.files_read);
	printf("%40s\t%d\n", "Total lines read", 
		master_counters.lines_read);
	printf("%40s\t%d\n", "Total lines matched", 
		master_counters.lines_matched);
	printf("%40s\t%ld\n", "Total bytes read", 
		master_counters.bytes_read);
	printf("%40s\t%ld\n", "Total bytes inspected", 
		master_counters.bytes_inspected);
	printf("%40s\t%d\n", "Total descent threads created", 
		master_counters.threads_created);
	printf("%40s\t%d\n", "Total descent threads pruned by -t", 
		master_counters.threads_pruned);
	printf("%40s\t%d\n", "Maximum simultaneously active descent threads", 
		master_counters.max_threads);
}

char *
get_cwd()
{
	char * cwdptr;
	char * memptr;
	int path_max;
	#if defined(PATH_MAX) && PATH_MAX != -1
	        path_max = PATH_MAX;
	#else
	        errno = 0;
	        if ((path_max = pathconf(".", _PC_PATH_MAX)) == -1){
	                if (errno != 0)
	                        perror("Could not get path_max of cwd");
	                /* error or unlimited, pick a big number */
	                path_max = 8192;
	        }
	#endif
	memptr = calloc(path_max + 1, sizeof(char));
	if(memptr == NULL)
	{
		fprintf(stderr, "Could not allocate memory for cwd: %s\n", 
			strerror(errno));
		exit(EXIT_FAILURE);
	}
	cwdptr = getcwd(memptr, path_max);
	return cwdptr;
}

/* The main program. Parses all command line options and prints out
appropriate errors if the program is used incorrectly. */
int
main(int argc, char * argv[])
{
    int ch;
    int no_files_present;
    char * regex;
    struct machine * firmware;
    int err_flag;
    int * cur_list;
    int * next_list;
    int err_ret_val;
	struct stat statbuf;
	char * cwd;
	char * full_path;
	char * realpath;
	Dir_Info * dir_info;
	int detached_value;
	Elem * first_elem;
	Elem * cur_elem;
	
	/* thread variables */
	int errcode;
	pthread_t t_id;
	pthread_attr_t thr_attr;
	
    
    /* initialize all the options to their default values */
    flags = 0;
    line_length = 255;
    max_lines = 0;
	max_depth = -1; //allow for infinite recursion if not set
    regex = "";
    no_files_present = 0;
    err_flag = 0;
	errcode = 0;
	thread_limit = 8192; /* arbitrarily large to allow for infinite threads */
	detached_value = PTHREAD_CREATE_DETACHED;
	dir_info = malloc(sizeof(Dir_Info));
	if(dir_info == NULL)
	{
		fprintf(stderr, "Could not allocate memory for thread params\n");
		exit(EXIT_FAILURE);
	}
    if (pthread_mutex_init(&mu, NULL) != 0)
    {
      fprintf(stderr, "can't init mutex");
	  exit(EXIT_FAILURE);
    }

	
	/* set the thread to the detached state */
	if((errcode = pthread_attr_init(&thr_attr)) != 0)
	{
		fprintf(stderr, "Error creating thread_attr in main: %s\n", 
			strerror(errcode));
		exit(EXIT_FAILURE);
	}
	
	if((errcode = pthread_attr_setdetachstate(&thr_attr, detached_value)) != 0)
	{
		fprintf(stderr, "Error setting detached in main: %s\n", 
			strerror(errcode));
		exit(EXIT_FAILURE);
	}
	

    /* process all the command line switches */
    opterr = 0;     /* prevent getopt() from printing error messages */
    while ((ch = getopt(argc, argv, OPTIONS)) != -1) 
    {
        switch (ch) {
        case 'h':       /* print help info */
                printf("-h: \tprint this help information.\n");
                printf("-i: \tturn case sensitivity ON (default OFF)\n");
                printf("-s: \tturn summary ON (default OFF). A single "
                        "output line containing the count\n\tof " 
                        "selected input lines is generated.\n");
                printf("-l57: \tset line length to 57 (default 0)\n");
                printf("-m102: \tset maximum input lines to 102 (default "
                        "255).\n");
                printf("-r: \tprint lines NOT matching the pattern string.\n");
                printf("-c: \tprint out the pseudocode instructions generated "
                        "by the compiler\n");
                printf("-a: \tprocess all files, including hidden files and "
						"directories.\n");
                printf("-f: \tdo not follow symbolic links\n");
                printf("-q: \tdo not print errors\n");
                printf("-d2: \tdo not descend into more than 2 levels "
						"of directories (default infinite)\n");
                printf("-p: \tprint out a table of information "
						"gathered by frep\n");
				printf("-t5: \tlimit the program to 5 threads");
                printf("Usage: frep [OPTIONS] pattern_string "
                    "[list of input file names]\n");
                exit(EXIT_SUCCESS);
                break;  
        case 'i':       /* turn case sensitivity on */
                flags |= CASE_INSENSITIVE;
                break;
        case 's':       /* turn summary on */
                flags |= SUMMARY;
                break;
        case 'r':       /* turn reverse on */
                flags |= REVERSE;
                break;
        case 'l':       /* set line length */
                err_ret_val = scan_switch_number(ch, &line_length);
                if(err_ret_val == -1)
                    err_flag = 1;
                break;
        case 'm':       /* set maximum input lines */
                err_ret_val = scan_switch_number(ch, &max_lines);
                if(err_ret_val == -1)
                    err_flag = 1;
                break;
        case 'c':       /* set print psuedocode */
                flags |= PRINTACTIONS;
                break;
        case 'D':       /* turn on debugging  */
                flags |= DEBUG;
                break;
        case 'a':       /* process ALL files (including hidden ones)  */
                flags |= ALL;
                break;
        case 'f':       /* turn on do not follow symlinks  */
                flags |= NO_FOLLOW;
                break;
        case 'q':       /* turn on quiet mode  */
                flags |= QUIET;
                break;
        case 'd':       /* set maximum directory depth  */
		        err_ret_val = scan_switch_number(ch, &max_depth);
		        if(err_ret_val == -1)
		            err_flag = 1;
		        break;
        case 'p':       /* print out counters  */
                flags |= PRINT_COUNTERS;
                break;
        case 't':       /* print out counters  */
                err_ret_val = scan_switch_number(ch, &thread_limit);
		        if(err_ret_val == -1)
		            err_flag = 1;
                break;
        case ':':
                fprintf(stderr, "Missing parameter to switch '%c'\n",
                      optopt);
                err_flag = 1;
                break;
        case '?':
                fprintf(stderr, "Illegal switch '%c'\n", optopt);
                err_flag = 1;
                break;
            } /*switch*/
    } 
    /* Did not specify regular expression */
    if (argc < 2 || argv[optind] == NULL)
	{
        fprintf(stderr, "Error: Did not specify regular expression\n");
		err_flag = 1; 
	}
    
	/* an error occurred, print out usage and exit */
    if(err_flag)
    {
        fprintf(stderr, "Usage: frep [OPTIONS] pattern_string "
            "[list of input file names]\nTry frep -h for more information.\n");
        exit(EXIT_FAILURE);
    }
    
    /* grab regex */
    regex = argv[optind];
    optind++;
    /* no files present */
    if(optind == argc)
        no_files_present = 1;
    
    /* initialize firmware */
    firmware = init_firmware(regex, flags);
    
    /* initialize firmware variables */
    cur_list = (int *)calloc(firmware -> action_limit, sizeof(int));
    if (cur_list == NULL)
    {
        fprintf(stderr, "Error in allocating memory\n");
        exit(EXIT_FAILURE);
    }
    
    next_list = (int *)calloc(firmware -> action_limit, sizeof(int));
    if (next_list == NULL)
    {
        fprintf(stderr, "Error in allocating memory\n");
        exit(EXIT_FAILURE);
    }
    
    /* files not provided, read from stdin */
    if(no_files_present)
	{
        find_matches(stdin, firmware, "", cur_list, next_list, &master_counters);
		master_counters.files_read++;
	}
	
	/* get the current working directory */
	cwd = get_cwd();
	
	/* add element to cactus stack */
	first_elem = newElem(cwd, 0);
	add_elem(NULL, first_elem);
	    
    /*loop through remaining command line parameters*/
    for( ;  optind < argc;  optind++ ) 
    {			
        if ((strcmp(argv[optind] ,"-")) == 0)
        {
            find_matches(stdin, firmware, "", cur_list, next_list, &master_counters);
            /* make sure stdin doesn't grab the EOF from before */
            rewind(stdin);
			master_counters.files_read++;
        }
		else
		{
			full_path = get_full_path(cwd, argv[optind]);
			realpath = get_real_path(full_path);
			/* grab the stat information of the file */
			if (stat(argv[optind], &statbuf) < 0)
				perror(argv[optind]);
			else
			{
			    if(S_ISDIR(statbuf.st_mode))
				{
					if (max_depth != 0)
					{
						dir_info -> full_dir_path = malloc(strlen(full_path) + 1);
						if(dir_info -> full_dir_path == NULL)
						{
							fprintf(stderr, "Insufficient memory, thread params\n");
							exit(EXIT_FAILURE);
						}
						strcpy(dir_info -> full_dir_path, realpath);
						dir_info -> firmware = firmware;
						
					    pthread_mutex_lock(&mu);
						
						/* add element to cactus stack and increase thread limit*/
						cur_elem = newElem(realpath, 1);
						add_elem(first_elem, cur_elem);

						dir_info -> cur_elem = cur_elem;
						
						if((flags & DEBUG) == DEBUG)
							fprintf(stderr, "Spawning thread...\n");
						
						/* create the thread */
						thread_count += 1;
						
						if (thread_count <= thread_limit && thread_count != 0)
						{
							if ((errcode = pthread_create(&t_id, &thr_attr, 
								process_dir, (void*)dir_info)) != 0)
							{
								fprintf(stderr, "Error creating thread in main: %s\n",
								strerror(errcode));
								pthread_mutex_unlock(&mu);
								process_dir(dir_info);
							}
							master_counters.threads_created++;
							if(master_counters.max_threads < thread_count)
								master_counters.max_threads = thread_count;										
						    pthread_mutex_unlock(&mu);
						}
						else
						{
							master_counters.threads_pruned++;
							if((flags & QUIET) != QUIET)
								fprintf(stderr, "Thread limit of %d reached\n", 
									thread_limit);
							else
								master_counters.quiet_errors++;
						    pthread_mutex_unlock(&mu);
							/* recurse */
							process_dir(dir_info);
						}						
					}
					else
					{
						master_counters.dir_depth_pruned++;
						fprintf(stderr, "Max_depth of %d "
							"exceeded by %s\n", max_depth, full_path);
					}
				}
		        else
					process_file(firmware, argv[optind], cur_list, next_list, 1, 
						&master_counters);
			}
		}
    }
	/* remove the root */
    pthread_mutex_lock(&mu);
	
    /* free all used memory */
    free(cur_list);
    free(next_list);
	remove_elem(first_elem);
	if(root == NULL)
	{
		if((flags & PRINT_COUNTERS) == PRINT_COUNTERS && 
				master_counters.files_read != 0)
			print_counters();
		
	}
	thread_count--;
	
    pthread_mutex_unlock(&mu);
	pthread_exit(EXIT_SUCCESS);
	return 0;
} 