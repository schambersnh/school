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

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <errno.h>
#include <ctype.h>
#include "machine.h"
#include "frep.h"
#include "interpreter.h"

#define OPTIONS ":hisDrl:m:c"
#define WHITE_SPACE " \n\r\t\v\f"


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
        fprintf(stderr, "Line length must be no more than 1023.\n");
        retval = -1;
    }
    else if(temp <= 0)
    {
        fprintf(stderr, "Value must be positive for switch -%c.\n", 
            switch_char);
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
                print_match(fp, input_line, line_number, fname);
            else if(!reverse && summary)
                summary_count++;
        }
        else
        {
            /* print the matching line to stdout if reverse is on 
                and summary is off */
            if (reverse && summary == 0)
                print_match(fp, input_line, line_number, fname);
            else if(reverse && summary)
                summary_count++;
        }
        /* stop processing if the maximum lines to read has been reached */
        if (max_lines != 0 && line_number >= max_lines)
            break;
    }
    if (summary)
        print_summary(fp, fname, summary_count);
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
            printf("%s: %d\n", realpath, summary_count);
            free(realpath);
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
    memptr = calloc(1, path_max);
    if (memptr == NULL)
    {
        fprintf(stderr, "Error in allocating memory: %s\n", strerror(errno));
        exit(EXIT_FAILURE);
    }
    else 
    {
        realptr = realpath(filename, memptr);
        if(realptr == NULL)
            fprintf(stderr, "Error in obtaining absolute path of file %s: %s\n", 
        filename, strerror(errno));
    }
    return realptr;
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
    FILE * fp;
    int err_flag;
    int * cur_list;
    int * next_list;
    int err_ret_val;
    
    /* initialize all the options to their default values */
    flags = 0;
    line_length = 255;
    max_lines = 0;
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
                printf("-m102: \tset maximum input lines to 102 (defaul "
                        "255)\n");
                printf("Usage: frep [OPTIONS] pattern_string "
                    "[list of input file names]\nTry frep -h for more information.\n");
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
        err_flag = 1; 
       
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
        find_matches(stdin, firmware, "", cur_list, next_list);
    
    /*loop through remaining command line parameters*/
    for( ;  optind < argc;  optind++ ) 
    {
        /* read from stdin */
        if ((strcmp(argv[optind] ,"-")) == 0)
        {
            find_matches(stdin, firmware, "", cur_list, next_list);
            /* make sure stdin doesn't grab the EOF from before */
            rewind(stdin);
        }
        else
        {               
            if((flags & DEBUG) == DEBUG)
                printf("Opening file: %s", argv[optind]);
            fp = fopen(argv[optind], "r");
            if(fp == NULL)
                perror(argv[optind]);
            else
            {
                find_matches(fp, firmware, argv[optind], cur_list, next_list);
                fclose(fp);
            }
        }        
    }
    
    /* free all used memory */
    free(cur_list);
    free(next_list);
    free_machine(firmware);
    
    return EXIT_SUCCESS;
} 
