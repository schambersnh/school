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
#include "frep_common.h"
#include "interpreter.h"
#include "cactus_stack.h"
#include "messages.h"
#include "send_recv.h"
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
int * next_list, Frep_Info * counters, int options[], int fd)
{   
    char * input_line;
    unsigned int reverse, summary;
    int line_number;
    int match_count;
    int summary_count;
	int len;
	
	/* initialize options */
	int flags = options[0];
	int line_length = options[2];
	int max_lines = options[3];
	
    /* initialize variables */
    input_line = malloc(line_length+1);
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
    if ((flags & REVERSE_SELECTION) == REVERSE_SELECTION)
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
		        
        if (match_regex(input_line, firmware, cur_list, next_list, counters, options))
        {
            /* print the matching line to stdout if reverse 
                and summary are off */
            if (reverse == 0 && summary == 0)
			{
                print_match(fp, input_line, line_number, fname, flags, fd);
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
                print_match(fp, input_line, line_number, fname, flags, fd);
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
        print_summary(fp, fname, summary_count, flags, fd);
	
	counters -> lines_read += line_number;
	counters -> lines_matched += match_count;
	counters -> bytes_read += ftell(fp);
    free(input_line);
}

/* Prints a successful match. */
void 
print_match(FILE * fp, char * input_line, int line_number, 
	char * fname, int flags, int fd)
{
	char output[BUFSIZE];
    char * realpath;
    pthread_mutex_lock(&mu);
    if (fp == stdin)
        sprintf(output, "%d: %s\n", line_number, input_line);
    else
    {
        realpath = get_real_path(fname, flags);
		if(realpath == NULL)
			return;
        sprintf(output, "%s,%d: %s\n", realpath, line_number, input_line);
		free(realpath);
    }
	if(fd == 1)
		printf("%s", output);
	else
	{
		/* send to client */
		if(our_send_message(fd, OUTPUT_LINE, strlen(output) + 1, output) != 0)
		{
			perror("Could not send output");
			exit(EXIT_FAILURE);
		}
	}
    pthread_mutex_unlock(&mu);
}

/* Prints a successful match if SUMMARY flag is enabled */
void 
print_summary(FILE * fp, char * fname, int summary_count, int flags, int fd)
{
	char output[BUFSIZE];
    pthread_mutex_lock(&mu);	
    if (summary_count != 0)
    {
        char * realpath;
        if (fp == stdin)
            sprintf(output, "%d\n", summary_count);
        else
        {
            realpath = get_real_path(fname, flags);
			if(realpath == NULL)
				return;
            sprintf(output, "%s: %d\n", realpath, summary_count);
        }
    }
	if(fd == 1)
		printf("%s", output);
	else
	{
		/* send to client */
		if(our_send_message(fd, OUTPUT_LINE, strlen(output), output) != 0)
		{
			perror("Could not send output");
			exit(EXIT_FAILURE);
		}
	}
    pthread_mutex_unlock(&mu);
}

/* This was modified from an in class example.
Gets the real path of a file. If any memory related errors occur, the program
will exit with failure status. If the function is successful, it returns a
string containting the real path of the file passed into the function. */
char *
get_real_path(char * filename, int flags)
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
	char output[BUFSIZE];
			
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
	int * thread_count;
	
	/* counters */
	int i;	
	Frep_Info * master_counters;
	
	/* fd for writing */
	int fd;
	
	/* options */
	int flags = ((Dir_Info *)params) -> options[0];
	int max_depth = ((Dir_Info *)params) -> options[1];
	int line_length = ((Dir_Info *)params) -> options[2];
	int max_lines = ((Dir_Info *)params) -> options[3];
	int thread_limit = ((Dir_Info *)params) -> options[4];
	
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
	thread_count = (&(*((Dir_Info *)params) -> thread_count));
	master_counters = (&(*((Dir_Info *)params) -> master_counters));
	fd = ((Dir_Info *)params) -> fd;
		
	if ((dp = opendir(((Dir_Info *)params) -> full_dir_path)) == NULL)
	{
		log_system_error(((Dir_Info *)params) -> full_dir_path, &thr_local_counters, flags, fd);
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
	thr_local_counters.max_threads = 0;
			
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
			log_system_error(((Dir_Info *)params) -> full_dir_path, &thr_local_counters, flags, fd);
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
			log_system_error(full_dir_entry_path, &thr_local_counters, flags, fd);
		else
		{
			/* determine if file can be processed */
			if (check_file(d_name, flags))
			{	
				/* increment hidden counter */
				if (d_name[0] == '.')
					thr_local_counters.hidden_names++;
				if (S_ISREG(statbuf.st_mode))  /* regular file */
					process_file(((Dir_Info *)params) -> firmware, full_dir_entry_path, cur_list, 
						next_list, 0, &thr_local_counters, ((Dir_Info *)params) -> options, fd);
				else if (S_ISDIR(statbuf.st_mode)) 
				{		
					realpath = get_real_path(full_dir_entry_path, flags);
					if(realpath == NULL)
					{
						if((flags & QUIET) == QUIET)
							thr_local_counters.quiet_errors++;
						//free(realpath);						
						continue;					
					}
					if((temp_dp = opendir(realpath)) == NULL)
					{
						log_system_error(full_dir_entry_path, &thr_local_counters, flags, fd);
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
									full_dir_entry_path, realpath, cur_depth, flags, fd);
						else
						{							
						    pthread_mutex_lock(&mu);
							
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
							
							dir_info -> options[0] = flags;
							dir_info -> options[1] = max_depth;
							dir_info -> options[2] = line_length;
							dir_info -> options[3] = max_lines;
							dir_info -> options[4] = thread_limit;
														
							(*thread_count)++;
							dir_info -> thread_count = (&(*thread_count));
							dir_info -> master_counters = (&(*master_counters));
							dir_info -> root = ((Dir_Info *)params) -> root;
							dir_info -> fd = fd;
							dir_info -> agent_id = ((Dir_Info *)params) -> agent_id;
							dir_info -> thread_id = ((Dir_Info *)params) -> thread_id;
														
							if ((*thread_count) <= thread_limit && 
									(*thread_count) != 0)
							{
								if ((errcode = pthread_create(&t_id, &thr_attr, 
										process_dir, (void*)dir_info)) != 0)
								{
									sprintf(output, "Error creating thread: %s\n", 
										strerror(errcode));
									log_error(output, &thr_local_counters, flags, fd);
									pthread_mutex_unlock(&mu);
									process_dir(dir_info);
								}
								thr_local_counters.threads_created++;
								if(thr_local_counters.max_threads < (*thread_count))
									thr_local_counters.max_threads = (*thread_count);										
								pthread_mutex_unlock(&mu);
							}
							else
							{
								thr_local_counters.threads_pruned++;
								if((flags & QUIET) != QUIET)
								{
									sprintf(output, "Thread limit of %d reached\n", 
										thread_limit);
									log_error(output, &thr_local_counters, flags, fd);
								}
								else
									thr_local_counters.quiet_errors++;
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
						{
							sprintf(output, "Max_depth of %d "
								"exceeded by %s\n", max_depth, full_dir_entry_path);
							log_error(output, &thr_local_counters, flags, fd);
						}
						else
							thr_local_counters.quiet_errors++;
					}
				} /* end directory */
				else if (S_ISLNK(statbuf.st_mode)) 
				{		
					/* make sure user did not specify to not follow symbolic links */
					if ((flags & DONT_FOLLOW_LINKS) != DONT_FOLLOW_LINKS)
					{
						if (stat(full_dir_entry_path, &linkstatbuf) < 0)
							log_system_error(full_dir_entry_path, &thr_local_counters, flags, fd);
						else
						{
							if(S_ISREG(linkstatbuf.st_mode))
								process_file(((Dir_Info *)params) -> firmware, full_dir_entry_path, 
									cur_list, next_list, 0, &thr_local_counters, ((Dir_Info *)params) -> options, fd);
							else
							{	
								/* temporarily make sure directory can be opened */				
								if((temp_dp = opendir(full_dir_entry_path)) == NULL)
								{
									log_system_error(full_dir_entry_path, &thr_local_counters, flags, fd);
									closedir(temp_dp);
									//free(realpath);
									continue;
								}
								closedir(temp_dp);
								
								/* get the realpath of the full path */
								realpath = get_real_path(full_dir_entry_path, flags);
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
											full_dir_entry_path, realpath, cur_depth, flags, fd);
									else
									{
										/* no loop detected, increase the depth and 
											spawn thread or recurse if 
												cur_depth != max_depth */																														
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
										
										dir_info -> options[0] = flags;
										dir_info -> options[1] = max_depth;
										dir_info -> options[2] = line_length;
										dir_info -> options[3] = max_lines;
										dir_info -> options[4] = thread_limit;
										dir_info -> fd = fd;
																				
										(*thread_count)++;
										dir_info -> thread_count = (&(*thread_count));
										dir_info -> master_counters = (&(*master_counters));
										dir_info -> root = ((Dir_Info *)params) -> root;
										dir_info -> agent_id = ((Dir_Info *)params) -> agent_id;
										dir_info -> thread_id = ((Dir_Info *)params) -> thread_id;
										
										if ((*thread_count) <= thread_limit && 
												(*thread_count) != 0)
										{
											if ((errcode = pthread_create(&t_id, &thr_attr, 
												process_dir, (void*)dir_info)) != 0)
											{
												sprintf(output, "Error creating thread: %s\n", 
													strerror(errcode));
												log_error(output, &thr_local_counters, flags, fd);
												pthread_mutex_unlock(&mu);
												process_dir(dir_info);
											}
											thr_local_counters.threads_created++;
											if(thr_local_counters.max_threads < (*thread_count))
												thr_local_counters.max_threads = (*thread_count);										
											pthread_mutex_unlock(&mu);
										}
										else
										{
											thr_local_counters.threads_pruned++;
											if((flags & QUIET) != QUIET)
											{
												sprintf(output, "Thread limit of %d reached\n", thread_limit);
												log_error(output, &thr_local_counters, flags, fd);
											}
											else
												thr_local_counters.quiet_errors++;
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
									{
										sprintf(output, "Max_depth of %d "
											"exceeded by %s\n", max_depth, full_dir_entry_path);
										log_error(output, &thr_local_counters, flags, fd);
									}
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
						{
							sprintf(output, "symlink not followed: %s\n", 
							full_dir_entry_path);
							log_error(output, &thr_local_counters, flags, fd);
						}
						else
							thr_local_counters.quiet_errors++;
					}
				} /* end symlink */		
			} /* end if hidden */   
		}
		//free(full_dir_entry_path);
	} /* end for loop */
	if (errno)
	{
		log_system_error(((Dir_Info *)params) -> full_dir_path, 
			(&(*((Dir_Info *)params) -> master_counters)), flags, fd);
	}
	
    pthread_mutex_lock(&mu);
	
	update_counters(&thr_local_counters, (&(*((Dir_Info *)params) -> master_counters)));
	
	/* free variables */
	free(cur_list);
	//free(buffer);
	free(next_list);
	closedir(dp);
		
	remove_elem(((Dir_Info *)params) -> cur_elem);
	if(((Dir_Info *)params) -> root -> ref_count == 0)
	{
		if((flags & PRINT_COUNTERS) == PRINT_COUNTERS)
		{
			if(fd == 1)
			{
				if((*master_counters).files_read != 0)	
					print_counters((&(*((Dir_Info *)params) -> master_counters)));
			} 
		}
		if(fd != 1)
		{
			send_counters(fd, (&(*((Dir_Info *)params) -> master_counters)));
			fprintf(stderr, "Agent exiting: agent_id %d thread_id %lu\n", 
				((Dir_Info *)params) -> agent_id, ((Dir_Info *)params) -> thread_id);
			free_machine(((Dir_Info *)params) -> firmware);
			if(close(((Dir_Info *)params) -> agent_id) < 0)
				perror("frepd agent close");
		}
	}
	(*thread_count)--;
    pthread_mutex_unlock(&mu);
	
   	return 0;
}

void
process_loop(Frep_Info * counters, Elem * cur_elem, char * full_path, 
	 char * realpath, int cur_depth, int flags, int fd)
{
	char output[BUFSIZE];
	
	counters -> dir_loops += 1;	
	if((flags & QUIET) != QUIET)
	{
	    pthread_mutex_lock(&mu);
		
		sprintf(output, "Loop detected trying to follow %s at "
			"depth %d\n", full_path, cur_depth + 1);
		if(fd == 1)
			fprintf(stderr, "%s", output);
		else
		{
			if(our_send_message(fd, ERROR_LINE, strlen(output)+1, output) != 0)
			{
				perror("Could not send error message loop");
				pthread_exit(NULL);
			}
		}
		print_loop(cur_elem, realpath, cur_depth + 1, fd);
		
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
	int * next_list, int cmdline, Frep_Info * counters, int options[], int fd)
{
	FILE * fp;
	int flags = options[0];
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
        find_matches(fp, firmware, full_path, cur_list, next_list, 
			counters, options, fd);
        fclose(fp);
		counters -> files_read += 1;
    }
}

/* logs an error to the console if quiet is off
   NOTE: Set fd to 1 on a non-remote connection */
void 
log_system_error(char * full_path, Frep_Info * counters, int flags, int fd)
{
	char output[BUFSIZE];
	if((flags & QUIET) != QUIET)
	{
		sprintf(output, "%s: %s\n", full_path, strerror(errno));
		if(fd != 1)
			send_error(output, fd);	
		else
			perror(full_path);
	}
	else
		counters -> quiet_errors += 1;
}

/* logs an error to the console based on frep options if quiet is off
   NOTE: Set fd to 1 on a non-remote connection */
void 
log_error(char output[], Frep_Info * counters, int flags, int fd)
{
	if((flags & QUIET) != QUIET)
	{
		if(fd != 1)
			send_error(output, fd);	
		else
			fprintf(stderr, "%s", output);
	}
	else
		counters -> quiet_errors += 1;
}

/* checks to see if a file can be processed, taking into account a variety of
	factors (the -a flag for example).
	0: cannot process
	1: can process
*/
int 
check_file(char * d_name, int flags)
{
	int retval;
	pthread_mutex_lock(&mu);
	retval = 0;
	
	/* if '-a' is set, is okay to process if file is not '.' or '..'*/
	if((flags & DO_DOTS) == DO_DOTS)
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

/* update the master counter list from the thread local counters */
void
update_counters(Frep_Info * counters, Frep_Info * master_counters)
{
	master_counters -> soft_links_ignored += counters -> soft_links_ignored;
	master_counters -> dir_opened += counters -> dir_opened;
	master_counters -> dir_loops += counters -> dir_loops;
	master_counters -> dir_depth_pruned += counters -> dir_depth_pruned;
	if(master_counters -> max_dir_depth < counters -> max_dir_depth)
		master_counters -> max_dir_depth = counters -> max_dir_depth;
	master_counters -> hidden_names += counters -> hidden_names;
	master_counters -> quiet_errors += counters -> quiet_errors;
	master_counters -> files_read += counters -> files_read;
	master_counters -> lines_read += counters -> lines_read;
	master_counters -> lines_matched += counters -> lines_matched;
	master_counters -> bytes_read += counters -> bytes_read;
	master_counters -> bytes_inspected += counters -> bytes_inspected;
	master_counters -> threads_created += counters -> threads_created;
	master_counters -> threads_pruned += counters -> threads_pruned;
	if(master_counters -> max_threads < counters -> max_threads)
		master_counters -> max_threads = counters -> max_threads;
	
}

void send_counters(int fd, Frep_Info * counters)
{
	/* convert counters to network order */
	counters -> soft_links_ignored = htonl(counters -> soft_links_ignored);
	counters -> dir_opened = htonl(counters -> dir_opened);
	counters -> dir_loops = htonl(counters -> dir_loops);
	counters -> dir_depth_pruned = htonl(counters -> dir_depth_pruned);
	counters -> max_dir_depth = htonl(counters -> max_dir_depth);
	counters -> hidden_names = htonl(counters -> hidden_names);
	counters -> quiet_errors = htonl(counters -> quiet_errors);
	counters -> files_read = htonl(counters -> files_read);
	counters -> lines_read = htonl(counters -> lines_read);
	counters -> lines_matched = htonl(counters -> lines_matched);
	counters -> bytes_read = htonl(counters -> bytes_read);
	counters -> bytes_inspected = htonl(counters -> bytes_inspected);
	counters -> threads_created = htonl(counters -> threads_created);
	counters -> threads_pruned = htonl(counters -> threads_pruned);
	counters -> max_threads = htonl(counters -> max_threads);
	if(our_send_message(fd, COUNTERS_TABLE, sizeof(*counters), counters) != 0)
	{
		perror("Could not send counters");
		pthread_exit(NULL);
	}
}


/* prints a table of all performance counters */
void
print_counters(Frep_Info * master_counters)
{
	printf("%40s\t%d\n", "Total soft links ignored due to -f", 
		master_counters -> soft_links_ignored);
	printf("%40s\t%d\n", "Total directories opened successfully", 
		master_counters -> dir_opened);
	printf("%40s\t%d\n", "Total directory loops avoided", 
		master_counters -> dir_loops);
	printf("%40s\t%d\n", "Total directory descents pruned by -d", 
		master_counters -> dir_depth_pruned);
	printf("%40s\t%d\n", "Maximum directory descent depth", 
		master_counters -> max_dir_depth);
	printf("%40s\t%d\n", "Total dot names not ignored due to -a", 
		master_counters -> hidden_names);
	printf("%40s\t%d\n", "Total errors not printed due to -q", 
		master_counters -> quiet_errors);
	printf("%40s\t%d\n", "Total files read", 
		master_counters -> files_read);
	printf("%40s\t%d\n", "Total lines read", 
		master_counters -> lines_read);
	printf("%40s\t%d\n", "Total lines matched", 
		master_counters -> lines_matched);
	printf("%40s\t%d\n", "Total bytes read", 
		master_counters -> bytes_read);
	printf("%40s\t%d\n", "Total bytes inspected", 
		master_counters -> bytes_inspected);
	printf("%40s\t%d\n", "Total descent threads created", 
		master_counters -> threads_created);
	printf("%40s\t%d\n", "Total descent threads pruned by -t", 
		master_counters -> threads_pruned);
	printf("%40s\t%d\n", "Maximum simultaneously active descent threads", 
		master_counters -> max_threads);
}

void send_error(char output[], int fd)
{
	if(our_send_message(fd, ERROR_LINE, strlen(output)+1, 
		output) != 0)
	{
		perror("Could not send error message");
		pthread_exit(NULL);
	}
}


