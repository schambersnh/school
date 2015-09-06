/* frepd.h 
Header file for frep daemon
Stephen Chambers
12/15/14
*/

typedef struct Agent_Params{
	int client_fd;
	struct sockaddr client;
}Agent_Params;

void * server_agent(void*);