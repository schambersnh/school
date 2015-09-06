/* intepreter.c
This file performs the regex matching algorithm using the recompiler.o object
file. 
Author: Stephen Chambers
Date: 10/6/14
*/

#define _POSIX_C_SOURCE 200809L
#define _ISOC99_SOURCE
#define _XOPEN_SOURCE 700
#define __EXTENSIONS__

#include "machine.h"
#include "interpreter.h"
#include "frep.h"
/* Determines whether or not the character specified by c is 
in the brset determined by the firmware.

Return Values:
    1: character was in brset
    0: character was not in brset
*/
int 
in_set(unsigned char * brset, char c, int first_printable)
{
    /* subtract the first_printable */
    int char_val = (int)c - first_printable;
    
    /* index into the brset */
    int search_index = char_val / 8;
    unsigned char search_byte = brset[search_index];
    
    /* shift bits to the one we are interested in */
    search_byte >>= char_val % 8;
    
    /* check if the bit is on, if it is, char is in set */
    return search_byte & 0x01;
}

/* Swaps two arrays */
void
swap_arrays(int ** array1, int ** array2)
{
    int * temp = *array1;
    *array1 = *array2;
    *array2 = temp;
}

/* Adds address of next instruction to end of array. Will not 
add action if it is already in the list.
Return Values:
    0: success
    -1: error (action is already in the list)
 */
int 
add_action(int * list, int action)
{
    int index;
    
    index = 0;
    while (list[index] != 0)
    {
        if(list[index] == action)
            return -1;
        else
            index++;    
    }
    list[index] = action;
    return 0;
}

/* Matches a regex against a particular input line.
Iterates character by character through the input line,
intepreting the cur_list and next_list for each of those 
characters.

Return Values:
    1: Successful match
    0: Does not match input line
 *  */
int 
match_regex(char * input_line, struct machine * firmware, int * cur_list,
 int * next_list, Frep_Info * counters)
{  
    /* boolean for start of line */
    int sol;
    
    /* placeholder for current character */
    char cur_char;
    
    /* index of current character */
    int i;   
    /* ensure arrays are zeroed out */
    if ((memset(cur_list, 0, sizeof(int) * firmware -> action_limit)) == NULL)
    {
        fprintf(stderr, "Error performing regex algorithm: memset\n");
        exit(EXIT_FAILURE);
    }
    if ((memset(next_list, 0, sizeof(int) * firmware -> action_limit)) == NULL)
    {
        fprintf(stderr, "Error performing regex algorithm: memset\n");
        exit(EXIT_FAILURE);
    }

    /* initialize algorithm values */
    sol = 1;
    i = 0;
    
    /* loop through every character in input_line */
    while(input_line[i] != '\0')
    {
		counters -> bytes_inspected += 1;
        /* case insensitive is on, force character to lower case */
        if ((flags & CASE_INSENSITIVE) == CASE_INSENSITIVE)
            cur_char = tolower(input_line[i]);
        else
            cur_char = input_line[i];
       
        /* append action 1 to the current list */
        add_action(cur_list, 1);
        
        /* Complete one cycle through current_list. If the function returns 1, 
            done was reached and this function should return immediately. */
        if ((interpret(sol, 0, cur_char, firmware, cur_list, next_list)) == 1)
            return 1;

        /* make cur_list next_list and make next_list empty */
        swap_arrays(&cur_list, &next_list);
        if ((memset(next_list, 0, 
            sizeof(int) * firmware -> action_limit)) == NULL)
        {
            fprintf(stderr, "Error performing regex algorithm: memset\n");
            exit(EXIT_FAILURE);
        }
        /* increment character */
        i++;  		      
        /* no longer start of line */
        sol = 0;
    }
	counters -> bytes_inspected += 1;
	
    /* done was not reached, go through cur_list once more with eol set */
    return interpret(0, 1, '\0', firmware, cur_list, next_list);
}

/* interprets the action_list based on the instrcutions in cur_list and 
next_list. The opcodes are treated like the following: 
0: illegal
1: if at the start of an input line then append cur_addr+1 to cur_list
2: if at the end of an input line then append cur_addr+1 to cur_list
3: return 0 as a match was successfully found
4: append cur_addr+1 and the oprand of the instruction to cur_list
5: append the oprand of the instruction to cur_list
6: if cur_char equals the oprand of the instruction, append 
    cur_addr+1 to cur_list
7: if cur_char is in brset_table at the index specified by the 
    oprand of the instruction, append cur_addr+1 to next_list
8: append cur_addr+1 to next_list
9: illegal

Return Values:
    1: Done reached
    0: Done not reached */
int 
interpret(int sol, int eol, char cur_char, struct machine * firmware, 
	int* cur_list, int* next_list)
{
    /* loop through current list and apply actions */
    int address = 0;
    while (cur_list[address] != 0)
    {
        struct action cur_action = firmware -> actions[ cur_list[address] ];
		
        /* Interpret machine instruction */
        switch (cur_action.opcode) {
        case 0:
                fprintf(stderr, "Error in interpreting algorithm.");
                exit(EXIT_FAILURE);
        case 1:       /* next_if_soln */
                if(sol)
                    add_action(cur_list, cur_list[address]+1);
                break;
        case 2:       /* next_if_eoln */
                if(eol)
                    add_action(cur_list, cur_list[address]+1);
                break;
        case 3:       /* done */
                return 1;
        case 4:       /* split */
                add_action(cur_list, cur_list[address]+1);
                add_action(cur_list, cur_action.oprand);
                break;
        case 5:       /* jump */
                add_action(cur_list, cur_action.oprand);
                break;
        case 6:       /* match */
                if(cur_char == cur_action.oprand)
                    add_action(next_list, cur_list[address]+1);
                break;  
        case 7:       /* matchset */
                if (in_set(firmware -> brset_table[cur_action.oprand], cur_char, 
                firmware -> first_printable))
                    add_action(next_list, cur_list[address]+1);
                break;
        case 8:       /* matchany */
                add_action(next_list, cur_list[address]+1);
                break;
        case 9:
                fprintf(stderr, "Error in interpreting algorithm.");
                exit(EXIT_FAILURE);
        } /*switch*/
        address ++;
    }
    /* done not reached */
        return 0;
}