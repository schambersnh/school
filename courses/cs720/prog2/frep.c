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
    else if(temp <= 0 && switch_char != 'd')
    {
        fprintf(stderr, "Value \"%s\" must be positive for switch -%c.\n", 
            optarg, switch_char);
        retval = -1;
    }
	else if(switch_char == 'd' && temp < 0)
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
int * next_list)
{   
    char * input_line;
    unsigned int reverse, summary;
    int line_number;
    int match_count;
    int summary_count;
    
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
        if(input_line[strlen(input_line) - 1] == '\n')
            input_line[strlen(input_line) - 1] = '\0';
		        
        if (match_regex(input_line, firmware, cur_list, next_list))
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
	
	counters.lines_read += line_number;
	counters.lines_matched += match_count;
	counters.bytes_read += ftell(fp);
    free(input_line);
}

/* Prints a successful match. */
void 
print_match(FILE * fp, char * input_line, int line_number, char * fname)
{
    char * realpath;
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
}

/* Prints a successful match if SUMMARY flag is enabled */
void 
print_summary(FILE * fp, char * fname, int summary_count)
{
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
		if ((flags & QUIET) != QUIET)
		{
	        fprintf(stderr, "Error in allocating memory: %s\n", strerror(errno));
	        exit(EXIT_FAILURE);
		}
		else
			counters.quiet_errors++;
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
			else
				counters.quiet_errors++;
						
		}
    }
    return realptr;
}

/* Process a directory from the command line
	regular file: process normally
	directory: recurse into this function again, being careful of loops
*/
int 
process_dir(char * cwd, char * dir_name, struct machine * firmware, int cur_depth)
{
	/* directory variables */
	DIR * dp;
	struct dirent * entry;
	struct dirent * buffer;
	char * d_name;
	char * full_dir_name;
	char * realpath;
	long name_max;
	Elem * dir_elem;
	
	/* stat variables */
	struct stat statbuf;
	struct stat linkstatbuf;
	
	/* regex variables */
	int * cur_list;
	int * next_list;
	
	/* counters */
	int i;	
	
	/* grab the realpath and fullpath from the dir_name passed in */
	realpath = get_real_path(dir_name);
	full_dir_name = get_full_path(cwd, dir_name);
	
	/* open the directory, return if failure */
	if ((dp = opendir(full_dir_name)) == NULL)
	{
		log_error(full_dir_name);
		free(realpath);
		free(full_dir_name);
		return cur_depth;
	}
		
	if ((flags & DEBUG) == DEBUG)
		fprintf(stderr, "Entering directory %s\n", full_dir_name);
	
	/* realpath didnt exist */
	if(realpath == NULL)
	{
		free(full_dir_name);
		return cur_depth;
	}
	
	/* change to the directory */
	/*if (chdir(realpath) < 0)
	{
		log_error(full_dir_name);
		free(full_dir_name);
		free(realpath);
		return cur_depth;
	}*/
	
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
	
	/* successfully changed directories, increase current depth */
	counters.dir_opened++;
	cur_depth++;
	if (cur_depth > counters.max_dir_depth)
		counters.max_dir_depth = cur_depth;
	
	/* add element to list */
	dir_elem = newElem(realpath, cur_depth);
	add_elem(dir_elem);	
	
	name_max = pathconf(full_dir_name, _PC_NAME_MAX);
	if (name_max <= 0)
		name_max = 4096;	
		
	/* set up the buffer holding directory contents */
	if ((buffer = malloc(sizeof(struct dirent) + name_max + 1)) == NULL)
	{
		fprintf(stderr, "No space for dirent buffer\n");
		exit(EXIT_FAILURE);
	}
	
	/* free the fullpath and realpath that was allocated */
	free(full_dir_name);
	free(realpath);	
	
	/* loop through directory contents */
	for (i = 1; ; i++)
	{
		/* grab the full path again for possible readdir error */
		full_dir_name = get_full_path(cwd, dir_name);
		
		/* read directory contents into buffer */
		if ((errno = readdir_r(dp, buffer, &entry)) != 0)
			log_error(full_dir_name);
		
		/* hit EOF, break out */
		if (entry == NULL) 
		{
			free(full_dir_name);
			break;
		}
		
		/* free the full path if no errrors occured */
		free(full_dir_name);
		
		/* grab the full path of the directory ENTRY name */
		d_name = entry -> d_name;
		full_dir_name = get_full_path(cwd, d_name);
		
		if ((flags & DEBUG) == DEBUG)
			fprintf(stderr, "d_name: %s\n", d_name);
		
		/* grab stats of directory entry */
		if (lstat(d_name, &statbuf) < 0)
			log_error(full_dir_name);
		else
		{
			/* determine if file can be processed */
			if (check_file(d_name))
			{	
				/* increment hidden counter */
				if (d_name[0] == '.')
					counters.hidden_names++;
				if (S_ISREG(statbuf.st_mode))  /* regular file */
					process_file(firmware, full_dir_name, cur_list, next_list, 0);
				else if (S_ISDIR(statbuf.st_mode)) 
				{							
					/* if at acceptable depth and no loops, recurse */
					if(cur_depth != max_depth)
					{
						realpath = get_real_path(d_name);
						/* loop detected, return back up and don't change to directory */
						if (find(realpath) != NULL)
						{
							counters.dir_loops++;
							if((flags & QUIET) != QUIET)
							{
								fprintf(stderr, "Loop detected trying to follow %s at "
									"depth %d\n", full_dir_name, cur_depth + 1);
								print_loop(realpath, cur_depth + 1);
							}
							else
								counters.quiet_errors++;
						}
						else
						{
							/* no loop, free to recurse */
							cur_depth = process_dir(full_dir_name, d_name, firmware, cur_depth);
							cur_depth--;
						}
						free(realpath);
					}
					else
					{
						counters.dir_depth_pruned++;
						if((flags & QUIET) != QUIET)
							fprintf(stderr, "Max_depth of %d "
								"exceeded by %s\n", max_depth, full_dir_name);
						else
							counters.quiet_errors++;
					}
				} /* end directory */
				else if (S_ISLNK(statbuf.st_mode)) 
				{		
					/* make sure user did not specify to not follow symbolic links */
					if ((flags & NO_FOLLOW) != NO_FOLLOW)
					{
						if (stat(d_name, &linkstatbuf) < 0)
							log_error(full_dir_name);
						else
						{
							if(S_ISREG(linkstatbuf.st_mode))
								process_file(firmware, full_dir_name, cur_list, next_list, 0);
							else
							{					
								/* get the realpath of the full path */
								realpath = get_real_path(full_dir_name);
								if(realpath == NULL)
									return cur_depth;
								/* check to see if we can descend into a directory */
								if(cur_depth != max_depth)
								{
									/* loop detected, return back up and don't change to directory */
									if (find(realpath) != NULL)
									{
										counters.dir_loops++;
										if((flags & QUIET) != QUIET)
										{
											fprintf(stderr, "Loop detected trying to follow %s at "
												"depth %d\n", full_dir_name, cur_depth + 1);
											print_loop(realpath, cur_depth + 1);
										}
										else
											counters.quiet_errors++;
									}
									else
									{
										/* no loop detected, increase the depth and 
											recurse if cur_depth != max_depth */
										cur_depth = process_dir(full_dir_name, d_name, firmware, 
											cur_depth);
										cur_depth--;
									}
								}
								else
								{
									counters.dir_depth_pruned++;
									if((flags & QUIET) != QUIET)
										fprintf(stderr, "Max_depth of %d "
											"exceeded by %s\n", max_depth, full_dir_name);
									else
										counters.quiet_errors++;
								}
								free(realpath);
							}		
						}	
					} 
					else
					{
						counters.soft_links_ignored++;
						/* print fullpath of symlink found if quiet is off */
						if ((flags & QUIET) != QUIET)
							fprintf(stderr, "symlink not followed: %s\n", 
							full_dir_name);
						else
							counters.quiet_errors++;
					}
				} /* end symlink */		
			} /* end if hidden */   
		}
		free(full_dir_name);
	} /* end for loop */
	if (errno)
	{
		full_dir_name = get_full_path(cwd, dir_name);
		perror(full_dir_name);
		free(full_dir_name);
	}
	/* free the directory entry bufffer */
	free(buffer);
	
	/*if((flags & DEBUG) == DEBUG)
	{
		char * cwd = get_cwd();
		fprintf(stderr, "Moving up from current directory %s\n", cwd);
		free(cwd);
	}*/
	
	/* change directory to the one above the current one using history list */
	/*if (chdir(tail -> prev -> path) < 0)
	{
		perror("chdir to parent directory");
		exit(EXIT_FAILURE);
	}*/
	remove_elem(tail);
	
	/*if((flags & DEBUG) == DEBUG)
	{
		char * cwd = get_cwd();
		fprintf(stderr, "Current directory is now %s\n", cwd);
		free(cwd);
	}*/
	/* free variables */
	free(cur_list);
	free(next_list);
	closedir(dp);
	return cur_depth;
}

/* Processes a file, opening it and sending it to the interpreter */
void 
process_file(struct machine * firmware, char * full_path, int * cur_list, 
	int * next_list, int cmdline)
{
	FILE * fp;
    fp = fopen(full_path, "r");
    if(fp == NULL)
	{
		if((flags & QUIET) != QUIET || cmdline == 1)
			perror(full_path);
		else
			counters.quiet_errors++;
	}
    else
    {
        find_matches(fp, firmware, full_path, cur_list, next_list);
        fclose(fp);
		counters.files_read++;
    }
}

/* logs an error to the console if quiet is off */
void 
log_error(char * full_path)
{
	if((flags & QUIET) != QUIET)
		perror(full_path);
	else
		counters.quiet_errors++;
}

/* checks to see if a file can be processed, taking into account a variety of
	factors (the -a flag for example).
	0: cannot process
	1: can process
*/
int 
check_file(char * d_name)
{
	/* if '-a' is set, is okay to process if file is not '.' or '..'*/
	if((flags & ALL) == ALL)
		return (strcmp(d_name, "..") != 0 && strcmp(d_name, ".") != 0);	
	else
		return (d_name[0] != '.');
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
		counters.soft_links_ignored);
	printf("%40s\t%d\n", "Total directories opened successfully", 
		counters.dir_opened);
	printf("%40s\t%d\n", "Total directory loops avoided", 
		counters.dir_loops);
	printf("%40s\t%d\n", "Total directory descents pruned by -d", 
		counters.dir_depth_pruned);
	printf("%40s\t%d\n", "Maximum directory descent depth", 
		counters.max_dir_depth);
	printf("%40s\t%d\n", "Total dot names not ignored due to -a", 
		counters.hidden_names);
	printf("%40s\t%d\n", "Total errors not printed due to -q", 
		counters.quiet_errors);
	printf("%40s\t%d\n", "Total files read", 
		counters.files_read);
	printf("%40s\t%d\n", "Total lines read", 
		counters.lines_read);
	printf("%40s\t%d\n", "Total lines matched", 
		counters.lines_matched);
	printf("%40s\t%ld\n", "Total bytes read", 
		counters.bytes_read);
	printf("%40s\t%ld\n", "Total bytes inspected", 
		counters.bytes_inspected);
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
	Elem * cwd_elem;
    
    /* initialize all the options to their default values */
    flags = 0;
    line_length = 255;
    max_lines = 0;
	max_depth = -1; //allow for infinite recursion if not set
    regex = "";
    no_files_present = 0;
    err_flag = 0;

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
        find_matches(stdin, firmware, "", cur_list, next_list);
		counters.files_read++;
	}
	
	/* add the current working directory onto the history list */
	cwd = get_cwd();	
	cwd_elem = newElem(cwd, 0);
	add_elem(cwd_elem);
    
    /*loop through remaining command line parameters*/
    for( ;  optind < argc;  optind++ ) 
    {			
        if ((strcmp(argv[optind] ,"-")) == 0)
        {
            find_matches(stdin, firmware, "", cur_list, next_list);
            /* make sure stdin doesn't grab the EOF from before */
            rewind(stdin);
			counters.files_read++;
        }
		else
		{
			full_path = get_full_path(cwd, argv[optind]);
			/* grab the stat information of the file */
			if (stat(argv[optind], &statbuf) < 0)
				perror(argv[optind]);
			else
			{
			    if(S_ISDIR(statbuf.st_mode))
				{
					if (max_depth != 0)
						process_dir(cwd, argv[optind], firmware, 1);
					else
					{
						counters.dir_depth_pruned++;
						fprintf(stderr, "Max_depth of %d "
							"exceeded by %s\n", max_depth, full_path);
					}
				}
		        else
		            process_file(firmware, argv[optind], cur_list, next_list, 1);
			}
			free(full_path);
		}
    }
	if((flags & PRINT_COUNTERS) == PRINT_COUNTERS && counters.files_read != 0)
		print_counters();
    /* free all used memory */
	remove_elem(head);
	free(cwd);
    free(cur_list);
    free(next_list);
    free_machine(firmware);
    
    return EXIT_SUCCESS;
} 
