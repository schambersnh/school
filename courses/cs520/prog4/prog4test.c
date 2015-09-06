/*Stephen Chambers
Program 4
4/something/12
*/
#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <string.h>
//This struct holds a word for easy lookup and how many times that word has appeared

#define MAX_HASH 100000
struct Elem
{
  char word[51];
  int count;
  struct Elem * next;
};

typedef struct
{
  char word[51];
  int count;
} resultElem;

//Two hash tables are going to be used interchangably throughout the program
struct Elem * hashTable[MAX_HASH];
struct Elem ** arrTable[100];
struct Elem * globTable[MAX_HASH];

//For Loop Control Variables
int c = 0;
int j = 0;

//Character Buffer
int bufferSize = 0;

//Shortest file index
int shortFile = 1;

//Max word length
#define MAX_WORD_LENGTH 51

//Results
resultElem results[1000000];

void checkRef(int, int, int, char[], int);
int hash(char[]);
void dumpTable(int);
void createHash(int, int, int, char[], int);
void createResults(int);
void sortResults(int);
void printResults();

int main(int argc, char * argv[])
{
  int i = 0;
  int fd = 0;
  int smallCount = 100000;

  if(argc < 2)
  {
    fprintf(stderr, "Invlaid number of Arguments. Exiting Program");
    return 1;
  }
  //finds the smallest file, saves it's index
  for(c = 1; c < argc; c++)
  {
    fd = open(argv[c], O_RDONLY);
    bufferSize = (int)lseek(fd, 0, SEEK_END);
    if(bufferSize < smallCount)
    {
      shortFile = c;
      smallCount = bufferSize;
    } 
  }
   //For multi-threaded portion, divide count into equal portions among threads.
  //Use locks to make sure the threads aren't overwriting any data(they'll be working on the same hash table) 

  //switch shortest file with first file in argument array
  char * temp = argv[shortFile];
  argv[shortFile] = argv[1];
  argv[1] = temp;

  //create hash table for every file entry
  int numFile = 1;
  for(numFile = 1; numFile < argc; numFile++)
  {
    fd = open(argv[1], O_RDONLY);
    if (fd < 0)
    {
      fprintf(stderr, "Could not open file. moving on to next file");
      numFile++;
    }
    else
    {  
      int bufferSize = (int)lseek(fd, 0, SEEK_END);
      char buffer[bufferSize];
      read(fd, buffer, bufferSize);
      createHash(0, bufferSize, fd, buffer, numFile);
      dumpTable(numFile);
    }
  }
  
  for(c = 1; c < argc; c++)
  {
    checkRef(arr[c]);
  }
  
  /*int numResults = 0;
  //createResults(argc - 1);
  createResults(1);
  i = 0;
  while(results[i].count != 0)
  {
    i++;
  }
  numResults = i;
  sortResults(numResults);
  printResults();
  //dumpTable(argc - 1);*/

}

/*This function is only used for the FIRST file processed. The first file processed is the shortest 
file in the list of command arguments for efficiency purposes. A portion of the file to be read is 
also given. 

SERIAL: From beginning of file to the end of the file
PARALLEL: Split up file into portions and have each thread work on a certain portion

The function finds a word and determines if it is 6 letters long. If it is, and nothing 
is in the table at that specific hash index, a new Elem is created using that word, and 
it is added to the table. If there is already a value at that index, and the 'word' value does
not equal the word just found, the new Elem is added at the end of the chain. If the value of
the 'word' field DOES equal the word just found, the count is incremented by 1.
*/
void createHash(int beginning, int end, int fd, char buffer[], int filePosition)
{
  struct Elem * origTable[MAX_HASH];
  char myWord[MAX_WORD_LENGTH] = "";
  int i = beginning;
  int k = 0;
  int wordCount = 0;
  while(i < end) //maybe + 1
  {
    j = 0;
    while((buffer[k] >= 'a' && buffer[k] <= 'z') || 
	(buffer[k] >= 'A' && buffer[k] <= 'Z'))
    {
      myWord[j] = buffer[k];
      j++;
      i++;
      k++;
    }
    int l = 0;
    for(l = 0; l < j; l++)
    {
      myWord[l] = tolower(myWord[l]);
    }
    if(strcmp(myWord, "though") == 0)
    {
      wordCount++;
    }
    int hashVal = hash(myWord);
    struct Elem * p = origTable[hashVal];
    if(j >= 6 && j < 51)
    {
      if(p == NULL)
      {
        //add to the table
        p = malloc(sizeof(struct Elem));
        strcpy(p -> word, myWord);
        p -> count = 1;
        origTable[hashVal] = p;
      }
      else
      {
        if(strcmp(p -> word, myWord) == 0)
        {
          p -> count += 1;
        }
        else
        {
	  //COLLISION CASE
          while(p -> next != NULL)
          {
            if(strcmp(p -> word, myWord) == 0)
            {
              //printf("incrementing count\n");
              p -> count += 1;
              break;
            } 
            else
            {
              p = p -> next;
            }
          }
            if(p -> next == NULL)
            {
              if(strcmp(p -> word, myWord) == 0)
              {
                p -> count += 1;
              }
              else
              {
                //add to the END OF THE CHAIN
                struct Elem * myP = malloc(sizeof(struct Elem)); 
                strcpy(myP -> word, myWord);
                myP -> count = 1;
                p -> next = myP;
              }
            }
          }
        }
      }
    int h = 0;/////////
    for(h = 0; h < j; h++)
    {
      myWord[h] = '\0';
    }
    i++;
    k++;
  }
  printf("wordCount: %d\n", wordCount);
  arrTable[filePosition] = origTable;
}
//This function is used in a loop after the shortest file has been processed.

void checkRef(int beginning, int end, int fd, char buffer[], int fileIndex)
{
	int h = 0;
	for(h = 0; h < MAX_HASH; h++)
	{
		hashTable[h] = NULL;
		struct Elem * p = globTable[h];
		struct Elem * p2 = hashTable[h];
		while(p!= NULL)
		{
			strcpy(p2 -> word, p -> word);
			p2 -> count = p-> count;
			p = p-> next;
		}
		globTable[h] = NULL;
	}
    char myWord[MAX_WORD_LENGTH] = "";
    int i = beginning;
    int k = 0;
    while(i < end) //maybe + 1
    {
      ////////////////////////////GET THE WORD////////////////////////////
      j = 0;
      while((buffer[k] >= 'a' && buffer[k] <= 'z') || 
	  (buffer[k] >= 'A' && buffer[k] <= 'Z'))
      {
        myWord[j] = buffer[k];
        j++;
        i++;
        k++;
      }
      int l = 0;
      for(l = 0; l < MAX_WORD_LENGTH; l++)
      {
        myWord[l] = tolower(myWord[l]);
      }
      /////////////////////////////////////////////////////////////////////
      int hashVal = hash(myWord);
      struct Elem * p = hashTable[hashVal];
      struct Elem * pHash = globTable[hashVal];
      if(j >= 6 && j < 51)
      {
        if(p != NULL) //if the value in HASH TABLE BEING SEARCHED exists
        {
          //if the first word in the HASH TABLE BEING SEARCHED equals the word thats being looked for
          if(strcmp(p-> word, myWord) == 0) 
          {
            //if nothing is in the HASH TABLE BEING ADDED TO
            if(pHash == NULL)
            {
              //add it
              struct Elem * p2 = malloc(sizeof(struct Elem));
	      strcpy(p2 -> word, myWord);
              p2 -> count = p-> count + 1;  
	      hashTable[hashVal] = p2;
            }
            else
            {
              //if its the first value in the HASH TABLE BEING ADDED TO
              if(strcmp(pHash -> word, myWord) == 0)
              {
                //increment count
                pHash -> count += 1;
              }
              else
              {
                //not first value, have to loop through the rest of HASH TABLE BEING ADDED TO to see if its there
	        while(pHash -> next != NULL) 
		{
                 //fprintf(stderr, "while loop 1\n");
                 //if the value im at is the word im looking for
		    if(strcmp(pHash -> word, myWord) == 0)
		    {
                    //increment count, break from loop
		    pHash -> count += 1;
                    break;
		  }
		  else
		  {
                    //didn't find the word, move on to the next element
		    pHash = pHash -> next;
		  }
		}
                 //if it wasn't found in the HASH TABLE BEING ADDED TO
                 if(pHash -> next == NULL)
		 {
                   //if the element being looked at is the word increment the count
                   if(strcmp(pHash -> word, myWord))
                   {
                     pHash -> count += 1;
                   }
                   else
                   {
                     //not found, add it to the END OF THE CHAIN
		     struct Elem * p2 = malloc(sizeof(struct Elem));
		     strcpy(p2 -> word, myWord);
		     p2 -> count = p-> count + 1;  
		     pHash -> next = p2;
                   }
	         }
              }
            }
          }
          else
          {
              //The word being looked for was not the first element in the HASH TABLE BEING SEARCHED
              while(p -> next != NULL)
              {
                //fprintf(stderr, "while loop 2\n");
                //if the element in the HASH TABLE BEING SEARCHED is the word being looked for
                if(strcmp(p -> word, myWord) == 0)
                {
                  //if nothing is in the hash table
                  if(pHash == NULL)
                  {
                    //add it, break from the loop
                    struct Elem * p2 = malloc(sizeof(struct Elem));
	            strcpy(p2 -> word, myWord);
                    p2 -> count = p-> count + 1;  
	            hashTable[hashVal] = p2;
                    break;			
                  }
                  else
                  {
                    //if the first element in the HASH TABLE BEING ADDED TO is the word being looked for
                    if(strcmp(pHash -> word, myWord) == 0)
                    {
                      //increment the count, break from the loop
                      pHash -> count += 1;
                      break;
                    }
                    else
                    {
                      //not first value, have to loop through HASH TABLE BEING ADDED TO to see if its there
                      while(pHash -> next != NULL)
                      {
                        //fprintf(stderr, "while loop 3\n");
                        //if the value im at is the word im looking for
                        if(strcmp(pHash -> word, myWord) == 0)
                        {
                          //increment count, break from the loop
                          pHash -> count += 1;
                          break;
                        }
                        else
                        {
                          //didn't find the word IN THE TABLE BEING ADDED TO, move on to the next element
                          pHash = pHash -> next;
                        }
                      }
                     //if it wasn't found
                     if(pHash -> next == NULL)
		     {
                       //if the element im at is the word im looking for
                       if(strcmp(pHash -> word, myWord) == 0)
                       {
                         //increment the count
                         pHash -> count += 1;
                       }
                       else
                       {
                         //wasn't found, add it to the END OF THE CHAIN
		         struct Elem * p2 = malloc(sizeof(struct Elem));
    		         strcpy(p2 -> word, myWord);
       		         p2 -> count = p-> count + 1;  
		         pHash -> next = p2;
                       }
	             }
                    }
                  } 
                 }
                 else
                 {
                   //didn't find the word in THE TABLE BEING SEARCHED, move on to the next element
                   p = p -> next;
                 }
                 //if it wasn't found in THE TABLE BEING SEARCHED, don't do anything.
              } 
            } 
          }
        }
      //clear the word
	  int clear = 0;
      for(clear = 0; clear < j; clear++)
      {
        myWord[clear] = '\0';
      }
      i++;
      k++;
    }
    //clear the opposite table
}
int hash(char word[])
{
  int i = 0; 
  int count = 0;
  for(i = 0; i < MAX_WORD_LENGTH; i++)
  {
    count += word[i];
  }
  return count % MAX_HASH;
}
void dumpTable(int fileCount)
{
    int i = 0;
    for(i = 0; i < MAX_HASH; i++)
    {
      struct Elem * p = arrTable[fileCount][i];
      while(p != NULL)
      {
        printf("p-> word: %s\np-> count: %d\n", p -> word, p -> count);
        p = p -> next;
      }
    }
}
void createResults(int fileCount)
{
  int i = 0;
  int j = 0;
    for(i = 0; i < MAX_HASH; i++)
    {
      struct Elem * p = arrTable[fileCount][i];
      while(p != NULL)
      {
        //printf("WORD ADDING: %s: %d\n", p-> word, p-> count);
        strcpy(results[j].word, p -> word);
        results[j].count = p -> count;
        j++;
        p = p -> next;
      }
    }
}
//THE FOLLOWING CODE WAS MODIFIED FROM CS515 NOTES ON SORTING ALGORITHMS. 
void sortResults(int length)
{
   int i = 1; 
   int j = 1;
   int flag = 1;   
   resultElem temp;           
   for(i = 1; i <= length && flag; i++)
   {
     flag = 0;
     for (j=0; j < length; j++)
     {
       if (results[j + 1].count > results[j].count)      
       { 
         strcpy(temp.word, results[j].word);
         temp.count = results[j].count; 
          
	 strcpy(results[j].word, results[j+1].word);
         results[j].count = results[j+1].count;

         strcpy(results[j+1].word, temp.word);
         results[j+1].count = temp.count;

         flag = 1;                            
       }
     }
   }  
}
void printResults()
{
  int i = 0;
  while(results[i].count)
  {
    if(i <= 19)
    {
      printf("%s: %d\n", results[i].word, results[i].count);
    }
    else
    {
      if(results[i].count == results[19].count)
      {
        printf("%s: %d\n", results[i].word, results[i].count);
      }
    } 
    i++;
  }
}

