/*
This program is designed to encapsulate a basic shell. The user will be able to 
input commands, similar to typing them into a terminal. It can be exited with the "quit" command.

Main Functionalities:
1. get a command from the user in an infinite loop
2. parse out empty space and null terminate the string
3. execute the command (fork, execvp, wait)

Does not support:
1. Pipes (|)
2. Redirection (< or >)
4. Internal Commands (cd)*/
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <sys/types.h>
#include <unistd.h>
#include <sys/wait.h>

size_t MAX_CHAR_SIZE = 50;
size_t MAX_ARG_SIZE = 50;

void format(char ** line, char ***args)
{
  int i = 0;
  int tempIndex = 0;
  int argumentIndex = 0;
  char * temp = malloc(MAX_CHAR_SIZE * sizeof(char));
  while((*line)[i] != '\0') //Loop until end of the string
  {
    if(!isspace((*line)[i])) //get argument
    {
      temp[tempIndex] = (*line)[i];
      tempIndex++;
      i++;
    }
	else
	{
      strcpy((*args)[argumentIndex], temp);//add argument to args array to be passed into execvp, keep looping for more arguments
      
  	  argumentIndex++;
      tempIndex = 0;
      memset(temp,'\0',MAX_CHAR_SIZE);
	  while(isspace((*line)[i]))
	    i++;
	}
  }
  //Got to the \0, add the last element
  strcpy((*args)[argumentIndex], temp);
  
  //add the null at the end of command so execvp knows when arguments end
  (*args)[++argumentIndex] = NULL;
}

/*
This method executes basic linux commands with the execvp function. 
*/
void  execute(char **args)
{
  pid_t  pid;
  int    status;

  //create child
  if ((pid = fork()) < 0) 
  { 
    perror("Error in forking");
    exit(1);
  }
  if (pid == 0) //the child!
  {
  	//wipe out memory and execute program
	//Argument 1: Command
	//Argument 2: A string array containing that command and that commands "command line stack"
    if (execvp(args[0], args) < 0)
	{
	  perror("Error executing program");
	  exit(1);
    }
  }
  else //the parent!
	  wait(&status); //wait around for the child to finish
}

/*
The main program sits in a loop doing three things.
1. Fetches the command
2. Formats the command and arguments for execution
3. Executes the command

If quit is entered, the shell exits.*/
int main()
{
  while(1)
  {
    char * line = malloc(MAX_CHAR_SIZE * sizeof(char));
    char ** arguments = malloc(MAX_ARG_SIZE * sizeof(char*));
 
    int i = 0;
    for(i = 0; i < MAX_ARG_SIZE; i++)
    {
      arguments[i] = malloc(MAX_CHAR_SIZE * sizeof(char));
    }
    printf("myshell> "); //print prompt
	
	//get user entered command
    int index = 0;
	char c = getchar();
	while(c != '\n')
	{
	  line[index] = c;
	  c = getchar();
	  index++;
	}
	//if the user entered a newline, print that newline and go back to the top of the loop to get the next command
	if(index == 0) 
	  continue;
	line[index] = '\0';
	
	//clear args before formatting
    format(&line, &arguments);
    if(strcmp(line, "quit") == 0)
      exit(0);
    else
      execute(arguments);
  }
  return 0;
}