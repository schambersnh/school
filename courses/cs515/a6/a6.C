/*Stephen Chambers
Brian Johnson
Program 6
10/26/11
This program takes uses two classes, CharQueue and CharStack.
These are a queue and a stack respectively implemented via
a dynamically allocated chain of elements. The main program
inputs characters until a new line is found, then checks if
the input was a palindrome or not. When input is passed to
the isPalindrome function, it is without spaces and all the 
characters are lower case. The program is to print a * before
echoed input if input is not a palindrome and a --> if it is.
*/
#include "CharQueue.h"
#include "CharStack.h"
#include <ctype.h>

void fill(char, CharQueue&, CharStack&);
bool isPalindrome(CharQueue&, CharStack&, CharQueue&);
bool isAllSpace(CharQueue&);



int main()
{
	char ch;
	CharQueue q;
	CharStack s;
	CharQueue input;

	cin.get(ch);
	while(cin)
	{
		if(ch == ' ') 
		{
			input.enqueue(' ');
			cin.get(ch);
		}
		else if(ch == '\n') // end of input
		{
			if(isPalindrome(q, s, input))
			{
				cout << "--> " << input;
				while(!input.empty()) //empty input
				{
					input.dequeue();
				}
			}
			else
			{
				cout << "*   " << input;
				while(!input.empty()) //empty input
				{
					input.dequeue();
				}
			}
			cout << "\n";
			cin.get(ch);
		}
		else
		{
			input.enqueue(ch);
			fill(tolower(ch), q, s); //fill up queue and stack
			cin.get(ch);
		}

	}

}
/*This method simply fills the current queue and stack with 
the lower case character from the input*/

void fill(char c, CharQueue & q, CharStack & s)
{
	q.enqueue(c);
	s.push(c);
}

/* isPalindrome is a function that takes in the current queue, stack, and input 
that the program is working with. Since a stack is a backwards version of a queue,
and a palindrome is a word with the same spelling backwards and forwards, isPalindrome
empties out both the queue and stack as long as their front and top respectively are
equal. If they are empty at the end, the input is a palindrome. If it is not a palindrome,
empty out the queue and stack to prepare for next input.
*/

bool isPalindrome(CharQueue& q, CharStack & s, CharQueue & input)
{
		
		if(q.empty() && s.empty()) //if no input
		{
			return false;
		}
		
		else if(isAllSpace(input)) //if input is only spaces
		{
			//empty out the spaces
			while(!s.empty()) 
			{
				s.pop();
			}
			while(!q.empty())
			{
				q.dequeue();
			}
			return false;
		}
		else
		{
			//remove elements from queue and stack to determine if input is a palindrome
			char top, front;
			front = q.front();
			top = s.top();
			q.dequeue();
			s.pop();
			while(top == front && (!s.empty() && !q.empty()))
			{
			front = q.front();
			top = s.top();
			q.dequeue();
			s.pop();

			}

			if(s.empty() && q.empty()) //Palindrome!
			{
				return true;
			}
			else
			{
				//not a palindrome, empty out queue and stack
				while(!s.empty())
				{
					s.pop();
				}
				while(!q.empty())
				{
					q.dequeue();
				}
			
			return false;
			}
			cout << "\n";
		  }
}
bool isAllSpace(CharQueue& input)
{
	char top = input.front();
	while(!input.empty())
	{
		if(isspace(top))
		{
			input.dequeue();
			if(!input.empty())
			top = input.front();
		}
		else
		{
			break;
		}
	}
	if(input.empty())
	{
		return true;
	}
}


