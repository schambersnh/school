/* frepd.c
The server daemon for the frep program.
Stephen Chambers
Date: 12/15/14
*/

#include "tcpblockio.h"
#include "frep_common.h"
#include "frepd.h"
#include "send_recv.h"
#include "machine.h"

void * server_agent(void * params)
{
	int client_fd = ((Agent_Params *)params) -> client_fd;
	int n;
	unsigned int len;
	enum message_types type;
	char regex[BUFSIZE];
	char remote_name[BUFSIZE];
	int options[5] = {0};
	
	/* interpreter variables */
    struct machine * firmware;
    int * cur_list;
    int * next_list;
	
	/* buffer for stat */
	struct stat statbuf;
	
	char * cwd;
	char * full_path;
	char * realpath;
	Dir_Info * dir_info;
	int detached_value;
	Elem * first_elem;
	Elem * cur_elem;
		
	/* define pieces of the options array */
	int flags;
	int max_depth;
	int line_length;
	int max_lines;
	int thread_limit;	
		
	/* thread variables */
	int errcode;
	pthread_t t_id;
	pthread_attr_t thr_attr;	
	
	/* initialize the mutex */
    if (pthread_mutex_init(&mu, NULL) != 0)
    {
      fprintf(stderr, "can't init mutex");
	  exit(EXIT_FAILURE);
    }	
			
	/* first message should be options */
	len = sizeof(options);
	if((n = our_recv_message(client_fd, &type, &len, options)) < 0)
	{
		fprintf(stderr, "Could not get options from client.\n");
		pthread_exit(NULL);
	}
	if(type != OPTIONS_TABLE)
	{
		fprintf(stderr, "Error, first message must be options table\n");
		pthread_exit(NULL);
	}
	
	len = BUFSIZE;
	/* second message should be regex */
	if((n = our_recv_message(client_fd, &type, &len, regex)) < 0)
	{
		fprintf(stderr, "Could not get regex from client.\n");
		pthread_exit(NULL);
	}
	if(type != REGULAR_EXPR)
	{
		fprintf(stderr, "Error, second message must be regex\n");
		pthread_exit(NULL);
	}
	regex[len] = '\0'; /* make regex into a string */
	
	/* third message should be remote file name */
	len = BUFSIZE;
	if((n = our_recv_message(client_fd, &type, &len, remote_name)) < 0)
	{
		fprintf(stderr, "Could not get remote name from client.\n");
		pthread_exit(NULL);
	}
	if(type != REMOTE_FILE_NAME)
	{
		fprintf(stderr, "Error, second message must be regex\n");
		pthread_exit(NULL);
	}
	remote_name[len] = '\0'; /* make remote name into a string */
	
	/* convert options to host order*/	
	flags = ntohl(options[0]);
	max_depth = ntohl(options[1]);
	line_length = ntohl(options[2]);
	max_lines = ntohl(options[3]);
	thread_limit = ntohl(options[4]);
	
	pthread_mutex_lock(&mu);
	
	fprintf(stderr, "Agent starting: agent_id %d thread_id %lu\n", 
		client_fd, (unsigned long)pthread_self());
	
			
	fprintf(stderr, "flags: %d\n", flags);
	fprintf(stderr, "max_depth: %d\n", max_depth);
	fprintf(stderr, "line_length: %d\n", line_length);
	fprintf(stderr, "max_lines: %d\n", max_lines);
	fprintf(stderr, "thread_limit: %d\n", thread_limit);	
	fprintf(stderr, "regex: %s\n", regex);
	fprintf(stderr, "remote_name: %s\n", remote_name);
	
	pthread_mutex_unlock(&mu);
	
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
	detached_value = PTHREAD_CREATE_DETACHED;
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
		
	/* get the current working directory */
	cwd = get_cwd();
		
	pthread_mutex_lock(&mu);
	
	/* add element to cactus stack */
	first_elem = newElem(cwd, 0);
	add_elem(root, first_elem);
	
	pthread_mutex_unlock(&mu);
	    
	if (stat(remote_name, &statbuf) < 0)
		perror(remote_name);
	else
	{
		full_path = get_full_path(cwd, remote_name);
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
				
				/* add element to cactus stack and increase thread limit */
				cur_elem = newElem(realpath, 1);
				add_elem(first_elem, cur_elem);

				dir_info -> cur_elem = cur_elem;
				
				dir_info -> options[0] = flags;
				dir_info -> options[1] = max_depth;
				dir_info -> options[2] = line_length;
				dir_info -> options[3] = max_lines;
				dir_info -> options[4] = thread_limit;
				
				dir_info -> fd = client_fd;
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
			process_file(firmware, remote_name, cur_list, next_list, 1, 
				&(*(dir_info -> master_counters)), options);
	}
	/* remove the root */
    pthread_mutex_lock(&mu);
	
    /* free all used memory */
    free(cur_list);
    free(next_list);
	remove_elem(first_elem);
	if(root == NULL)
	{
		/* send over the counters */
		if((flags & PRINT_COUNTERS) == PRINT_COUNTERS)
			send_counters(client_fd, &(*(dir_info -> master_counters)));
		
	}
	(*(dir_info -> thread_count))--;
	
    pthread_mutex_unlock(&mu);
	pthread_exit(EXIT_SUCCESS);	
}

int 
main(int argc, char * argv[])
{
	/* port and interface to listen on &*/
	char * port;
	char * interface;
	
	struct sockaddr server;
	struct sockaddr_in *iptr;
	char text_buf[TEXT_SIZE];
	
	/* error code for the thread */
	int errorcode;
	
	/* Agent variables; */
	Agent_Params * params;
	socklen_t len;
	pthread_t agent_id;
	pthread_attr_t thr_attr;
	int detached_value;
	
	/* the fd to listen on */
	int listen_fd;
		
	port = NULL;
	interface = NULL;
	/* check the command line arguments */
	if(argc > 3)
	{
		fprintf(stderr, "Usage: frepd [port] [interface]");
		exit(EXIT_FAILURE);
	}
	else if(argc == 2)
	{
		/* only the port was specified */
		port = argv[1];
	}
	else if(argc == 3)
	{
		/* port and interface were specified */
		port = argv[1];
		interface = argv[2];
	}
	
	detached_value = PTHREAD_CREATE_DETACHED;
	
	/* set the agent thread to the detached state */
	if((errorcode = pthread_attr_init(&thr_attr)) != 0)
	{
		fprintf(stderr, "Error creating thread_attr in main: %s\n", 
			strerror(errorcode));
		exit(EXIT_FAILURE);
	}
	
	if((errorcode = pthread_attr_setdetachstate(&thr_attr, detached_value)) != 0)
	{
		fprintf(stderr, "Error setting detached in main: %s\n", 
			strerror(errorcode));
		exit(EXIT_FAILURE);
	}
	
	/* add a dummy root to the stack */
	root = newElem(NULL, 0);
	root -> ref_count = 0;
	add_elem(NULL, root);
	
	
	/* open a connection to listen on */
	listen_fd = openlistener(port, interface, &server);
	if(listen_fd == 0)
		exit(EXIT_FAILURE);
	
	/* established as a server, enter an infinite loop */
	while(1)
	{
		iptr = (struct sockaddr_in *)&server;
		if(inet_ntop(iptr -> sin_family, &iptr->sin_addr, 
			text_buf, TEXT_SIZE) == NULL)
		{
			perror("inet_ntop server");
			break;
		}
		fprintf(stderr, "\nfrepd listening at IP address %s port %d\n\n", 
			text_buf, ntohs(iptr -> sin_port));
			
		params = malloc(sizeof(Agent_Params));
		if(params == NULL)
		{
			fprintf(stderr, "Could not allocate memory on server\n");
			break;
		}
		len = sizeof(params -> client);
		if((params -> client_fd = accept(listen_fd, &params -> client, &len)) < 0)
		{
			perror("frepd accept");
			free(params);
			break;
		}
		iptr = (struct sockaddr_in *)&params -> client;
		if(inet_ntop(iptr -> sin_family, &iptr -> sin_addr, 
			text_buf, TEXT_SIZE) == NULL)
		{
			perror("inet_ntop client");
			free(params);
			break;
		}
		fprintf(stderr, "Connected to client at IP address %s port %d\n", 
			text_buf, ntohs(iptr -> sin_port));
			
		/* launch the agent thread */
		errorcode = pthread_create(&agent_id, &thr_attr, server_agent, 
			(void*)params);
		if(errorcode != 0)
		{
			fprintf(stderr, "Could not create server agent: %s\n", 
				strerror(errorcode));
			free(params);
			break;
		}
	}
	/* shut down the server */
	if(close(listen_fd) < 0)
		perror("frepd close");
	pthread_exit(NULL);
}