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
#include "messages.h"
#include "tcpblockio.h"
#include "send_recv.h"
#include <pthread.h>

char *
substring(char *string, int position, int length) 
{
   char *pointer;
   int c;
   int index = 0;
 
   pointer = malloc(length - position + 1);
 
   if (pointer == NULL)
   {
      printf("Unable to allocate memory.\n");
      exit(EXIT_FAILURE);
   }
 
   for (c = position ; c < length ; c++) 
   {
      pointer[index] = string[c];      
      index++;   
   }
 
   pointer[index] = '\0';
 
   return pointer;
}

void *
process_remote(void * params)
{		
	/* fd for server connection */
	int fd;
	
	/* remote option information */
	char * node;
	char * port;
	char * remote_name;
	char message[BUFSIZE];
	
	/* master counters */
	Frep_Info * master_counters;
	
	/* regex to be used for matching */
	char * regex;
	
	/* options to send to server */
	int options[5];
	
	/* type of the message */
	enum message_types type;
	
	/* number of bytes read */
	int n;
	
	unsigned int len;
	int flags;
	
	Elem * new_elem;
	
	struct sockaddr_in *iptr;
	char text_buf[TEXT_SIZE];
	
	
	/* storage information about connection */
	struct sockaddr server;
	struct sockaddr client;
	
	/* storage for counters */
	Frep_Info * counters;
	
	pthread_mutex_lock(&mu);
				
	/* initialize variables */
	flags = ((IP_Thread_Info *)params) -> options[0];
	regex = ((IP_Thread_Info *)params) -> regex;
	node = ((IP_Thread_Info *)params) -> node;
	port = ((IP_Thread_Info *)params) -> port;
	remote_name = ((IP_Thread_Info *)params) -> remote_name;
	options[0] = htonl(((IP_Thread_Info *)params) -> options[0]);
	options[1] = htonl(((IP_Thread_Info *)params) -> options[1]);
	options[2] = htonl(((IP_Thread_Info *)params) -> options[2]);
	options[3] = htonl(((IP_Thread_Info *)params) -> options[3]);
	options[4] = htonl(((IP_Thread_Info *)params) -> options[4]);
	master_counters = (&(*((IP_Thread_Info *)params) -> master_counters));
	new_elem = ((IP_Thread_Info *)params) -> cur_elem;
			
	//fprintf(stderr, "Opening client to %s on port %s\n", node, port);
	fd = openclient(port, node, &server, &client);
	if(fd == -1)
	{
		fprintf(stderr, "Server %s refused connection on port %s\n", node, port);
		pthread_exit(NULL);
	}
	
	iptr = (struct sockaddr_in *)&server;
	if(inet_ntop(iptr -> sin_family, &iptr -> sin_addr, 
		text_buf, TEXT_SIZE) == NULL)
	{
		perror("inet_ntop client");
		pthread_exit(NULL);
	}
	//fprintf(stderr, "Connected to server at IP address %s port %d\n", 
	//	text_buf, ntohs(iptr -> sin_port));
	
	
	pthread_mutex_unlock(&mu);
	
	/* send over the options to the server */
	if(our_send_message(fd, OPTIONS_TABLE, sizeof(options), options) != 0)
	{
		perror("Could not send options");
		pthread_exit(NULL);
	}
	
	/* send over the regex to the server */
	if(our_send_message(fd, REGULAR_EXPR, strlen(regex) + 1, regex) != 0)
	{
		perror("Could not send regex");
		pthread_exit(NULL);
	}
	
	/* send over the remote_name to the server */
	if(our_send_message(fd, REMOTE_FILE_NAME, strlen(remote_name) + 1, 
		remote_name) != 0)
	{
		perror("Could not send remote file name");
		pthread_exit(NULL);
	}
	
	/* loop on recv */
	len = BUFSIZE;
	while((n = our_recv_message(fd, &type, &len, message)))
	{
		//fprintf(stderr, "Message: %s", message);
		if(type == COUNTERS_TABLE)
		{
			counters = (Frep_Info *)message;
			counters -> soft_links_ignored = ntohl(counters -> soft_links_ignored);
			counters -> dir_opened = ntohl(counters -> dir_opened);
			counters -> dir_loops = ntohl(counters -> dir_loops);
			counters -> dir_depth_pruned = ntohl(counters -> dir_depth_pruned);
			counters -> max_dir_depth = ntohl(counters -> max_dir_depth);
			counters -> hidden_names = ntohl(counters -> hidden_names);
			counters -> quiet_errors = ntohl(counters -> quiet_errors);
			counters -> files_read = ntohl(counters -> files_read);
			counters -> lines_read = ntohl(counters -> lines_read);
			counters -> lines_matched = ntohl(counters -> lines_matched);
			counters -> bytes_read = ntohl(counters -> bytes_read);
			counters -> bytes_inspected = ntohl(counters -> bytes_inspected);
			counters -> threads_created = ntohl(counters -> threads_created);
			counters -> threads_pruned = ntohl(counters -> threads_pruned);
			counters -> max_threads = ntohl(counters -> max_threads);
		
			/* update the master counters */
			pthread_mutex_lock(&mu);
			update_counters(counters, 
				(&(*((IP_Thread_Info *)params) -> master_counters)));
			remove_elem(new_elem);
			if(((IP_Thread_Info *)params) -> root -> ref_count == 0)
			{
				if((flags & PRINT_COUNTERS) == PRINT_COUNTERS && 
						(*master_counters).files_read != 0)
					print_counters((&(*((IP_Thread_Info *)params) -> master_counters)));
			}
			pthread_mutex_unlock(&mu);
			break;
		}
		else if(type == OUTPUT_LINE)
		{
			message[len] = '\0';
			/* print the message to stdout */
			pthread_mutex_lock(&mu);
			printf("%s:%d/%s", text_buf, ntohs(iptr -> sin_port), message);
			pthread_mutex_unlock(&mu);
		}
		else if(type == ERROR_LINE)
		{
			message[len] = '\0';
			/* print the message to stdout */
			pthread_mutex_lock(&mu);
			fprintf(stderr, "%s:%d/%s", text_buf, ntohs(iptr -> sin_port), message);
			pthread_mutex_unlock(&mu);
		}
		len = BUFSIZE;
	}
	if (n < 0)
	{
		fprintf(stderr, "Could not get message from client.\n");
		pthread_exit(NULL);	
	}
			
	return 0;
}

int 
parse_remote_option(char * remote_option, char ** node, char ** port_string, 
	char ** remote_name)
{
	int index = 0;
	int colon_index = 0;
	int slash_index = 0;
	
	while(remote_option[index] != '\0')
	{
		if(remote_option[index] == ':')
			colon_index = index;
		else if(remote_option[index] == '/')
		{
			slash_index = index;
			break;
		}
		index++;
	}
	/* no slash or colon, must be a malformatted string */
	if(slash_index == 0)
	{
		fprintf(stderr, "There must be a / after the port and before the file\n");
		return -1;
	}
	
	/* split the string into node, port, and remote_name */
	*node = substring(remote_option, 0, colon_index);
	*port_string = substring(remote_option, colon_index + 1, slash_index);
	*remote_name =  substring(remote_option, slash_index + 1, strlen(remote_option));
	
	if(strlen(*node) == 0)
	{
		fprintf(stderr, "Node is missing, must be a DNS name or IPv4 address\n");
		return -1;
	}
	else if(strlen(*port_string) == 0)
	{
		fprintf(stderr, "Port is missing from remote option string\n");
		return -1;
	}
	else if(strlen(*remote_name) == 0)
	{
		fprintf(stderr, "No file specified in remote option string\n");
		return -1;
	}
	return 0;
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
	IP_Thread_Info * ip_info;
	int detached_value;
	Elem * first_elem;
	Elem * cur_elem;
	Elem * new_elem;
	char * node;
	char * port;
	char * remote_name;
	
	/* options */
	int options[5] = {0};
	int flags;
	int max_depth;
	int line_length;
	int max_lines;
	int thread_limit;
		
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
	
	dir_info -> thread_count = malloc(sizeof(int));
	dir_info -> master_counters = malloc(sizeof(Frep_Info));
	if(dir_info -> thread_count == NULL || dir_info -> master_counters == NULL)
    {
      fprintf(stderr, "Could not allocate memory\n");
	  exit(EXIT_FAILURE);
    }
	(*(dir_info -> thread_count)) = 0;
	
	/* initialize struct */
	(*(dir_info -> master_counters)).soft_links_ignored = 0;
	(*(dir_info -> master_counters)).dir_opened = 0;
	(*(dir_info -> master_counters)).dir_loops = 0;
	(*(dir_info -> master_counters)).dir_depth_pruned = 0;
	(*(dir_info -> master_counters)).max_dir_depth = 0;
	(*(dir_info -> master_counters)).hidden_names = 0;
	(*(dir_info -> master_counters)).quiet_errors = 0;
	(*(dir_info -> master_counters)).files_read = 0;
	(*(dir_info -> master_counters)).lines_read = 0;
	(*(dir_info -> master_counters)).lines_matched = 0;
	(*(dir_info -> master_counters)).bytes_read = 0;
	(*(dir_info -> master_counters)).bytes_inspected = 0;
	(*(dir_info -> master_counters)).threads_created = 0;
	(*(dir_info -> master_counters)).threads_pruned = 0;
	(*(dir_info -> master_counters)).max_threads = 0;
	

	/* initialize the mutex */
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
                flags |= IGNORE_CASE;
                break;
        case 's':       /* turn summary on */
                flags |= SUMMARY;
                break;
        case 'r':       /* turn reverse on */
                flags |= REVERSE_SELECTION;
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
                flags |= LIST_CODE;
                break;
        case 'a':       /* process ALL files (including hidden ones)  */
                flags |= DO_DOTS;
                break;
        case 'f':       /* turn on do not follow symlinks  */
                flags |= DONT_FOLLOW_LINKS;
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
	
	/* initialize options */
	options[0] = flags;
	options[1] = max_depth;
	options[2] = line_length;
	options[3] = max_lines;
	options[4] = thread_limit;
    
    /* files not provided, read from stdin */
    if(no_files_present)
	{
        find_matches(stdin, firmware, "", cur_list, next_list, 
			&(*(dir_info -> master_counters)), options, 1);
		(*(dir_info -> master_counters)).files_read++;
	}
		
	/* get the current working directory */
	cwd = get_cwd();
	
	/* add element to cactus stack */
	first_elem = newElem(cwd, 0);
	    
    /*loop through remaining command line parameters*/
    for( ;  optind < argc;  optind++ ) 
    {			
        if ((strcmp(argv[optind] ,"-")) == 0)
        {
            find_matches(stdin, firmware, "", cur_list, next_list, 
				&(*(dir_info -> master_counters)), options, 1);
            /* make sure stdin doesn't grab the EOF from before */
            rewind(stdin);
			(*(dir_info -> master_counters)).files_read++;
        }
		else
		{
			/* check if the command line references a remote node */
			if(strpbrk(argv[optind], ":") != NULL)
			{			
				pthread_mutex_lock(&mu);
				
				ip_info = malloc(sizeof(IP_Thread_Info));
				if(ip_info == NULL)
				{
					fprintf(stderr, "remote option malloc\n");
					exit(EXIT_FAILURE);
				}
				/* prepare options array */
				ip_info -> options[0] = options[0];
				ip_info -> options[1] = options[1];
				ip_info -> options[2] = options[2];
				ip_info -> options[3] = options[3];
				ip_info -> options[4] = options[4];
				
				/* parse the remote option string */
				if((parse_remote_option(argv[optind], &node, &port, &remote_name)) == -1)
				{
					fprintf(stderr, "Remote string syntax: node:port/file\n");
					pthread_mutex_unlock(&mu);
					continue;	
				}					
				/* add dummy elem to stack representaing the servert */
				new_elem = newElem(node, 0);
				add_elem(first_elem, new_elem);
				ip_info -> cur_elem = new_elem;
				ip_info -> root = first_elem;
								
				/* prepare information and launch the 'IP Thread' */
				ip_info -> regex = malloc(strlen(regex) + 1);
				ip_info -> node = malloc(strlen(node) + 1);
				ip_info -> port = malloc(strlen(port) + 1);
				ip_info -> remote_name = malloc(strlen(remote_name) + 1);
					
				if(ip_info -> regex == NULL || ip_info -> node == NULL || 
						ip_info -> remote_name == NULL || ip_info -> port == NULL)
				{
					fprintf(stderr, "Error allocationg memory\n");
					exit(EXIT_FAILURE);
				}
				strcpy(ip_info -> regex, regex);
				strcpy(ip_info -> node, node);
				strcpy(ip_info -> port, port);
				strcpy(ip_info -> remote_name, remote_name);
				
				/* give the ip thread the master counters */
				ip_info -> master_counters = (&(*(dir_info -> master_counters)));
				
				if ((errcode = pthread_create(&t_id, &thr_attr, 
						process_remote, (void*)ip_info)) != 0)
				{
					fprintf(stderr, "Error creating thread to process remote node: %s\n",
					strerror(errcode));
				}
				pthread_mutex_unlock(&mu);
				continue;	
			}
			/* cmdline parameter is normal, grab the stat information of the file */
			if (stat(argv[optind], &statbuf) < 0)
				perror(argv[optind]);
			else
			{
				full_path = get_full_path(cwd, argv[optind]);
				realpath = get_real_path(full_path, flags);
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
						dir_info -> fd = 1;
						
						dir_info -> options[0] = flags;
						dir_info -> options[1] = max_depth;
						dir_info -> options[2] = line_length;
						dir_info -> options[3] = max_lines;
						dir_info -> options[4] = thread_limit;
						
						dir_info -> root = first_elem;
						
						/* create the thread */
						(*(dir_info -> thread_count)) += 1;						
						
						if ((*(dir_info -> thread_count)) <= thread_limit && 
								(*(dir_info -> thread_count)) != 0)
						{
							if ((errcode = pthread_create(&t_id, &thr_attr, 
								process_dir, (void*)dir_info)) != 0)
							{
								fprintf(stderr, "Error creating thread in main: %s\n",
								strerror(errcode));
								pthread_mutex_unlock(&mu);
								process_dir(dir_info);
							}
							(*(dir_info -> master_counters)).threads_created++;
							if((*(dir_info -> master_counters)).max_threads < (*(dir_info -> thread_count)))
								(*(dir_info -> master_counters)).max_threads = (*(dir_info -> thread_count));										
						    pthread_mutex_unlock(&mu);
						}
						else
						{
							(*(dir_info -> master_counters)).threads_pruned++;
							fprintf(stderr, "Thread limit of %d reached\n", 
									thread_limit);
						    pthread_mutex_unlock(&mu);
							/* recurse */
							process_dir(dir_info);
						}						
					}
					else
					{
						(*(dir_info -> master_counters)).dir_depth_pruned++;
						fprintf(stderr, "Max_depth of %d "
							"exceeded by %s\n", max_depth, full_path);
					}
				}
		        else
					process_file(firmware, argv[optind], cur_list, next_list, 1, 
						&(*(dir_info -> master_counters)), options, 1);
			}
		}
    }
	/* remove the root */
    pthread_mutex_lock(&mu);
	
    /* free all used memory */
    free(cur_list);
    free(next_list);
	remove_elem(first_elem);
	if(first_elem -> ref_count == 0)
	{
		if((flags & PRINT_COUNTERS) == PRINT_COUNTERS && 
				(*(dir_info -> master_counters)).files_read != 0)
			print_counters(&(*(dir_info -> master_counters)));
		
	}
	(*(dir_info -> thread_count))--;
	
    pthread_mutex_unlock(&mu);
	pthread_exit(EXIT_SUCCESS);
	return 0;
} 