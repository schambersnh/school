/*Stephen Chambers
This lab inputs from stdin, discards any bytes that are not
0xEF, and writes any bytes that are to stdout
lab1 1/27/12
*/
#include <stdio.h>
int main()
{
	int c;

	c = getchar();
	while(c != EOF)
	{
		if (c == 0xEF)
		{
			putchar(0xEF);
		}
		c = getchar();	
	}
}

