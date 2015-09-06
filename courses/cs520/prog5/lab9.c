/*Stephen Chambers
4/6/12
lab 9
*/
#include <stdio.h>
#include <stdlib.h>
int frameCount()
{
  int count = 0;
  int * ebp = (int*)getFP();
  while((*ebp) != 0)
  {
    count ++;
    ebp = (int*) *ebp;
  }
  return count - 1;
}

