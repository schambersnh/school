/*Stephen Chambers
Program 4
4/something/12
*/
#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <string.h>
#include <unistd.h>
#include <pthread.h>
#include <ctype.h>
//This struct holds a word for easy lookup and how many times that word has appeared

#define MAX_HASH 100000
#define MAX_THREADS 2
#define MAX_WORD_LENGTH 51
#define MAX_FILES 100

pthread_mutex_t mu;             
pthread_cond_t cv; 

int threadCount = 0;
int n = 0;

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

typedef struct
{
  int threadID;
  int filePosition;
} threadInfo;

struct Elem * finalTable[MAX_HASH];
struct Elem ** arrTable[100];
struct Elem ** threadArrTable[MAX_THREADS];


resultElem results[100000];

//Pointer to "copy" argv array
char ** copyArgV;

//sizes of files
int fileSize[105]; // maximum of 100 files, +5 to be on the safe side


unsigned long hash(char[]);
void createThreadHash(int, int, char[], int);
void createHash(int, int, char[], int);
void checkRef(int);
void checkThreadRef(int);
void createResults();
void sortResults(int);
void printResults();
void * work(void *);


int main(int argc, char * argv[])
{
  copyArgV = argv;
  int i = 0;
  int c = 0;
  int fd = 0;
  int smallCount = 100000;
  int shortFile = 1;

  if(argc < 2)
  {
    fprintf(stderr, "Invlaid number of Arguments. Exiting Program");
    return 1;
  }
  if (pthread_mutex_init(&mu, NULL) != 0)
  {
    fprintf(stderr, "can't init mutex");
  }

  if (pthread_cond_init(&cv, NULL) != 0)
  {
    fprintf(stderr, "can't init condition variable");
  }
  //finds the smallest file, saves it's index
  for(c = 1; c < argc; c++)
  {
    fd = open(argv[c], O_RDONLY);
    if(fd < 0)
    {
      fprintf(stderr, "Could not open file! Invalid file name. Exiting program\n");
      exit(-1);
    }
    else
    {
      int bufferSize = (int)lseek(fd, 0, SEEK_END);
      if(bufferSize == -1)
      {
        fprintf(stderr, "lseek failed. exiting program\n");
        exit(-1);
      }
      fileSize[c] = bufferSize;
      if(bufferSize < smallCount && bufferSize != 0)
      {
        shortFile = c;
        smallCount = bufferSize;
      }
     } 
  }
  //switch shortest file with first file in argument array
  char * temp = argv[shortFile];
  argv[shortFile] = argv[1];
  argv[1] = temp;
                                              
  int numFile = 1;                                                  
  for(numFile = 1; numFile < argc; numFile++)                       
  {             
    int fd = open(argv[numFile], O_RDONLY); //check if file could be opened
    if(fd < 0)
    {
      fprintf(stderr, "Could not open file. Invalid File name. Exiting program\n");
      exit(-1);
    }
    else
    {
      int bufferSize = (int)lseek((int)fd, 0, SEEK_END);
      if(bufferSize == -1)
      {
      fprintf(stderr, "lseek failed. exiting program\n");
        exit(-1);
      }
      if(lseek(fd, 0 , SEEK_SET) == -1)
      {
      fprintf(stderr, "lseek failed. exiting program\n");
        exit(-1);
      }
      if(bufferSize == 0)
      {
        fprintf(stderr, "Empty file, skipping file. The rest of the files will still be processed.\n");
      }
      else
      {
        //FOR LOAD IMBALANCE. Process shorter files serially, larger threads with multiple threads per file
        if(bufferSize >= 500000)
        {
          //multi threaded
          threadCount = 0; 
          pthread_t pt[MAX_THREADS];
          for(i = 0; i < MAX_THREADS; i++)
          {
            threadInfo  * myThread = malloc(sizeof(threadInfo));
            if(myThread == NULL)
            { 
              fprintf(stderr, "malloc failed, exiting program\n");
              exit(-1);
            }
            myThread -> threadID = i;
            myThread -> filePosition = numFile;

            pthread_create(&pt[i], NULL, work, (void*)myThread);
          }
          int w = 0;
          for(w = 0; w < MAX_THREADS; w++)
          {
            if(pthread_join(pt[w], NULL) != 0)
            {
              fprintf(stderr, "Could not join threads!\n");
            }
          }
         checkThreadRef(numFile);
        }
        else
        {
          //serial
          char buffer[bufferSize];
          read(fd, buffer, bufferSize);                            
          createHash(0, bufferSize, buffer, numFile);     
        }
      }
    }
  }                                                                 
  checkRef(argc);
  createResults();
  int numResults = 0;
  i = 0;
  while(results[i].count != 0)
  {
    i++;
  }
  numResults = i;
  sortResults(numResults);
  printResults();
  return 0;
}

/*This function is only used for the FIRST file processed. The first file processed is the shortest 
file in the list of command arguments for efficiency purposes. A portion of the file to be read is 
also given. 

SERIAL: From beginning of file to the end of the file
PARALLEL: Split up file into portions and have each thread work on a certain portion

The function finds a word and determines if it is 6 letters long. If it is, and nothing 
is in the table at that specific hash index, a new Elem is created using that word, and 
it is added to the table	`. If there is already a value at that index, and the 'word' value does
not equal the word just found, the new Elem is added at the end of the chain. If the value of
the 'word' field DOES equal the word just found, the count is incremented by 1.
*/
void * work(void * threadInformation)
{
  threadInfo * myThread = (threadInfo *)threadInformation;

  int fd = open(copyArgV[myThread -> filePosition], O_RDONLY);
  if(fd < 0)
  {
    fprintf(stderr, "error in opening file, exiting program\n");
    exit(-1);
  }  
  else
  {
    int bufferSize = (int)lseek(fd, 0, SEEK_END);
    if(bufferSize == -1)
    {
      fprintf(stderr, "lseek failed. exiting program\n");
      exit(-1);
    }
    if(lseek(fd, 0 , SEEK_SET) == -1)
    {
      fprintf(stderr, "lseek failed. exiting program\n");
      exit(-1);
    }
    char buffer[bufferSize];
    read(fd, buffer, bufferSize);

    int offset = bufferSize / MAX_THREADS;

    createThreadHash(myThread -> threadID * offset, 
    (myThread -> threadID * offset) + offset, buffer, myThread -> threadID);  
  }
  return 0;
}
void createThreadHash(int beginning, int end, char buffer[], int threadID)
{
  //WILL NOT WORK FOR SMALL FILES, HAVE TO LOOK BACK ONE
  struct Elem ** origTable = malloc(sizeof(struct Elem *) * MAX_HASH);
  if(origTable == NULL)
  {
     fprintf(stderr, "malloc failed, exiting program\n");
     exit(-1);
  }
  char myWord[MAX_WORD_LENGTH] = {0};
  int i = beginning;
  int j = 0;
  int wordCount = 0;
  while(i < end) //maybe + 1
  {
    j = 0;
    while((buffer[i] >= 'a' && buffer[i] <= 'z') || 
	(buffer[i] >= 'A' && buffer[i] <= 'Z'))
    {
      if(i == (end - 1))
      {
        while((buffer[i + 1] >= 'a' && buffer[i + 1] <= 'z') || 
	(buffer[i + 1] >= 'A' && buffer[i + 1] <= 'Z'))
        {
          myWord[j] = buffer[i];
          i++;
          j++;
        }
      }
      
      myWord[j] = buffer[i];
      j++;
      i++;
    }
    myWord[j] = '\0';
    int l = 0;
    for(l = 0; l < j; l++)
    {
      myWord[l] = tolower(myWord[l]);
    }
    if(strcmp(myWord, "letter") == 0)
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
        if(p == NULL)
        {
          fprintf(stderr, "malloc failed, exiting program\n");
          exit(-1);
        }
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
                if(myP == NULL)
                {
                  fprintf(stderr, "malloc failed, exiting program\n");
                  exit(-1);
                }
                strcpy(myP -> word, myWord);
                myP -> count = 1;
                p -> next = myP;
              }
            }
          }
        }
      }
    int h = 0;
    for(h = 0; h < j; h++)
    {
      myWord[h] = '\0';
    }
    i++;
  }
  threadArrTable[threadID] = origTable; 
}
void createHash(int beginning, int end, char buffer[], int filePosition)
{
  struct Elem ** origTable = malloc(sizeof(struct Elem *) * MAX_HASH);
  if(origTable == NULL)
  {
    fprintf(stderr, "malloc failed, exiting program\n");
    exit(-1);
  }
  char myWord[MAX_WORD_LENGTH] = {0};
  int i = beginning;
  int k = 0;
  int j = 0;
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
    myWord[j] = '\0';
    int l = 0;
    for(l = 0; l < j; l++)
    {
      myWord[l] = tolower(myWord[l]);
    }
    if(strcmp(myWord, "letter") == 0)
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
        if(p == NULL)
        {
          fprintf(stderr, "malloc failed, exiting program\n");
          exit(-1);
        }
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
                if(myP == NULL)
                {
                  fprintf(stderr, "malloc failed, exiting program\n");
                  exit(-1);
                }
                strcpy(myP -> word, myWord);
                myP -> count = 1;
                p -> next = myP;
              }
            }
          }
        }
      }
    int h = 0;
    for(h = 0; h < j; h++)
    {
      myWord[h] = '\0';
    }
    i++;
    k++;
  }

  arrTable[filePosition] = origTable; 
}
//This function is used in a loop after the shortest file has been processed.
void checkRef(int numFiles)
{
  int i = 0;
  int j = 0;
  int foundWord = 0;
  int foundWordCount = 0;
  int isWordFound = 0;
  for(i = 0; i < MAX_HASH; i++)
  {
       struct Elem * p = arrTable[1][i];
       while(p != NULL)
       {
         int isEmpty = 0;
         foundWord = 0;
         foundWordCount = 0;
         char word[51] = {0};
         strcpy(word, p -> word); 
         if(strcmp(word, "letter") == 0)
         {

         }
         for(j = 2; j < numFiles; j++)
         {
           //EMPTY FILE CHECK
           if(arrTable[j] ==  NULL)
           {
             isEmpty++;
           }
           else
           {
             struct Elem * arrP = arrTable[j][i];
             while(arrP != NULL)
             {
               //if the word equals the word I'm looking for, I'm done
               isWordFound = 0;
               if(strcmp(arrP -> word, word) == 0)
               {
                 foundWord++;
                 foundWordCount += arrP -> count;
                 isWordFound = 1;
                 break;
               }
               else
               {
                 arrP = arrP -> next;
               }
             }
             //if the word wasn't found in one of the files, don't search through the rest of them
             if(isWordFound != 1)
             {
               break;
             }
            }
         } 
         if(foundWord == (numFiles - 2 - isEmpty)) // word was found in every file
         {
           struct Elem * finalP = finalTable[i];

           if(finalP == NULL)
           {
             struct Elem * finalP2 = malloc(sizeof(struct Elem));
             if(finalP2 == NULL)
             {
               fprintf(stderr, "malloc failed, exiting program\n");
               exit(-1);
             }
             strcpy(finalP2 -> word, word);
             finalP2 -> count = foundWordCount + p -> count;    
             finalTable[i] = finalP2;
           }
           else
           {
             while(finalP -> next != NULL)
             {
               finalP = finalP -> next;
             }
             struct Elem * finalP2 = malloc(sizeof(struct Elem));
             if(finalP2 == NULL)
             {
               fprintf(stderr, "malloc failed, exiting program\n");
               exit(-1);
             }
             strcpy(finalP2 -> word, word);
             finalP2 -> count = foundWordCount + p -> count;    
             finalP -> next = finalP2;
           }
         }
         p = p -> next;
       }
  }
}
void checkThreadRef(int filePosition)
{
  struct Elem ** returnTable = malloc(sizeof(struct Elem *) * MAX_HASH);
  if(returnTable == NULL)
  {
    fprintf(stderr, "malloc failed, exiting program\n");
    exit(-1);
  }
  int i = 0;
  int j = 0;
  int foundWord = 0;
  int foundWordCount = 0;
  int isWordFound = 0;
  for(i = 0; i < MAX_HASH; i++)
  {
       struct Elem * p = threadArrTable[0][i];
       while(p != NULL)
       {
         foundWord = 0;
         foundWordCount = 0;
         char word[51];
         strcpy(word, p -> word); 
         for(j = 1; j < MAX_THREADS; j++)
         {
           struct Elem * arrP = threadArrTable[j][i];
           while(arrP != NULL)
           {
             //if the word equals the word I'm looking for, I'm done
             isWordFound = 0;
             if(strcmp(arrP -> word, word) == 0)
             {
               foundWord++;
               foundWordCount += arrP -> count;
               isWordFound = 1;
               break;
             }
             else
             {
               arrP = arrP -> next;
             }
           }

         } 
           struct Elem * finalP = returnTable[i];

           if(finalP == NULL)
           {
             struct Elem * finalP2 = malloc(sizeof(struct Elem));
             if(finalP2 == NULL)
             {
               fprintf(stderr, "malloc failed, exiting program\n");
               exit(-1);
             }
             strcpy(finalP2 -> word, word);
             finalP2 -> count = foundWordCount + p -> count;    
             returnTable[i] = finalP2;
           }
           else
           {
             while(finalP -> next != NULL)
             {
               finalP = finalP -> next;
             }
             struct Elem * finalP2 = malloc(sizeof(struct Elem));
             if(finalP2 == NULL)
             {
               fprintf(stderr, "malloc failed, exiting program\n");
               exit(-1);
             }
             strcpy(finalP2 -> word, word);
             finalP2 -> count = foundWordCount + p -> count;    
             finalP -> next = finalP2;
           }
         p = p -> next;
       }
  }
  //clear threadArrTable
  i = 0;
  j = 0;
  for(i = 0; i < MAX_THREADS; i++)
  {
    for(j = 0; j < MAX_HASH; j++)
    {
        threadArrTable[i][j] = NULL;
    }
  }
  arrTable[filePosition] = returnTable;
}
//MODIFIED FROM http://www.cse.yorku.ca/~oz/hash.html
unsigned long hash(char word[])
{
  unsigned long hash = 5381;
  int c;

  while ((c = *word++))
      hash = ((hash << 5) + hash) + c; /* hash * 33 + c */
  return hash % MAX_HASH;
}
void createResults()
{
  int i = 0;
  int j = 0;
    for(i = 0; i < MAX_HASH; i++)
    {
      struct Elem * p = finalTable[i];
      while(p != NULL)
      {
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
