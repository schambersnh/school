#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
/*
Utilities class for final
1. Single precision float[32-bits] to integer[32-bit] converter
2. ASCIII to integer converter //////DONE///////
3. isBigEndian((possible int parameter))
4. Program 5 Thread Create (psuedo-code)

//ASSEMBLY (utilities.s)
5. Write add 11 in assembly, takes an int, adds 11 to it, and returns the result ///DONE/////
6. Write threadYield /////DONE///////
*/

#include "utilities.h"
int main()
{
  ////////////////////ADD 11///////////////////////////
  int x = 5;
  x = add11(x);
  printf("TESTING ADD11: %d\n", x);

  char * ascString = "150";
  int ascInt = asciiConverter(ascString);
  printf("TESTING ASCIICONVERTER: %d\n", ascInt);

  if(isBigEndian())
  {
    printf("Machine is big endian\n");
  }
  else
  {
    printf("Machine is little endian\n");
  }

  float myF = -10.5;
  int myI = floatToInt(myF);
  printf("%f as int is %d\n", myF, myI);

  int intTest = 50;
  float floatTest = intToFloat(intTest);
  printf("%d as float is %f\n", intTest, floatTest);

}
int floatToInt(float f)
{
  //printf("sign: %d\nexp: %d\nsig: %0x\n", sign, realExp, sig);
  unsigned int sign = *(int*)&f & 0x80000000;
  unsigned int exp = *(int*)&f & 0x7F800000;
  unsigned int sig = *(int*)&f & 0x007FFFFF;

  printf("f: %0x\n", f);
  //insert hidden one bit
  sig = sig | 0x00800000;
  exp >>= 23;
  int realExp = exp - 127;

  float value = pow(2, realExp);
  int i = 0;
  float sum = 0.0; 
  int count = 0;
  int set = 23;
  unsigned int and = 0x01;
  for(i = 0; i < 23; i++)
  {
    if((sig & (and << set)) != 0)
    {
      sum += pow(2, count);
    }
    count--;
    set--;
  }
  if(sign == 0x80000000)
  {
    return -1 * value * sum;
  }
  else
  {
    return value * sum;
  }
}
float intToFloat(int i1)
{
  /*
  printf("\n\ni1: %08x\n", i1);
  unsigned int sign = i1 & 0x80000000;
  unsigned int exp = i1 & 0x7F800000;
  unsigned int sig = i1 & 0x007FFFFF;

  //insert hidden one bit
  sig = sig | 0x00800000;
  exp >>= 23;
  float realExp = exp - 127;

  int i = 0;
  float sum = 0.0; 
  int count = 0;
  int set = 23;
  unsigned int and = 0x01;
  for(i = 0; i < 23; i++)
  {
    if((sig & (and << set)) != 0)
    {
      sum += pow(2, count);
    }
    count--;
    set--;
  }
printf("sign: %d\nexp: %d\nsig: %0x\n", sign, realExp, sig);
  if(sign == 0x80000000)
  {
    return -1 * pow(sum, realExp);
  }
  else
  {
    return pow(sum, realExp);
  }
  */
}
int asciiConverter(char * s)
{
  int count = 0;
  int val = 0;
  while(s[count] != 0)
  {
    val = (val*10) + (s[count] - '0');
    count++;
  }
  return val;
}
int isBigEndian()
{
  int x = 1;
  
  if(*(char*)&x == 0)
  {
    return 1;
  }
  else
  {
    return 0;
  }
}
void threadCreate()
{
  /*
  if(!init)
  {
    doinit();
    init = 1;
  }
  int mallocSize = 16384;
  int * mallocStack = malloc(mallocSize);
  int changeMalloc = mallocSize / 4;
  *(mallocStack + changeMalloc) = info;
  *(mallocStack + changeMalloc - 1) = func;
  *(mallocStack + changeMalloc - 2) = garbage;
  *(mallocStack + changeMalloc - 3) = threadStart;
  *(mallocStack + changeMalloc - 4) = garbage;
  
  yo -> stack = mallocStack;
  TCB * yo = malloc(sizeof(TCB)); 
  yo -> esp = (int)(yo -> stack + changeMalloc - 4);

  TCB * p = head;
  while(p -> next != NULL)
  {
    p = p -> next;
  }
  p -> next = yo;
  return (long)yo;
  */
}
int add11(int arg)
{
  asm_add11(arg);
}
void threadYield()
{

}
