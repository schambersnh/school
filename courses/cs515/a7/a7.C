/*
Stephen Chambers
Brian Johnson
Program 7
10/26/11
This program simulates allocation, deallocation, and use of local stack based variables. The program
itself is a command interpreter where the user can define, set, and use variables, as well as entering
and exiting functions. The user can also print whatever the current environment is and the variables 
defined in the aforementioned environment at any time. 
*/
#include <iostream>
#include "Variable.h"
#include "Environment.h"
#include "EnvStack.h"
#include <string>

void Interpret(string, EnvStack &);
void define(string, EnvStack&);
void set(string, int, EnvStack&);
void use(string, EnvStack&);
void print(EnvStack&);
void enter(string, EnvStack&);
void exit(EnvStack&);

int main()
{
	EnvStack s(20);
	s.push(Environment("main"));
	string command;
	cout << "?> ";
	cin >> command;
	cout << command;
	while(!s.empty())
	{
		Interpret(command, s);
		if(!s.empty())
		{
		cout << "\n?> ";
		cin >> command;
		cout << command;
		}
	}
	cout << "exiting initial environment main" << endl;
	return 0;

}
/*This is the command interpreter. It takes in a string to determine which method to call
and a stack to pass along to that method*/
void Interpret(string s, EnvStack & stack)
{
	string name;
	int value;
	if (s == "define")
	{
		cin >> name;
		cout << " " << name << endl;
		define(name, stack);
	}
	else if(s == "set")
	{
		cin >> name >> value;
		cout << " " << name << " " << value << endl;
		set(name, value, stack);
	}
	else if(s == "use")
	{
		cin >> name;
		cout << " " << name << endl;
		use(name, stack);
	}
	else if(s == "print")
	{
		cout << endl;
		print(stack);
	}
	else if(s == "enter")
	{
		cin >> name;
		cout << " " << name << endl;
		enter(name, stack);
	}
	else if(s == "exit")
	{
		cout << endl;
		exit(stack);
	}
	else
	{
		//flush away bad command line
		cout << "";
	}
	name = "";
}
/*Adds a variable with specified name to the current local environment. An error message
is printed and no new definition is made if a variable with that name already exists in the current
local environment. Variables are initialized to 0*/
void define(string varName, EnvStack& s)
{
	if(s.top().findVariable(varName))
	{
		cout << "Environment::newVariable: variable already defined" << endl;
	}
	else
	{
		s.top().newVariable(varName); 
		s.top().setVariable(varName, 0);
	}
}
/*Sets variable with specified name to integer value given in the current local 
environment. An error message is printed if no variable with that name exists in the
current local environment*/
void set(string varName, int value, EnvStack& s)
{
	if(!s.top().findVariable(varName))
	{
		cout << "Envrionment::setVariable: variable not defined" << endl;
	}
	else
	{
		s.top().setVariable(varName, value);
	}
}
/*outputs the current value of the variable with the given name in the current local environment. 
An error message is pritned if no variable with that name exists in the current loval environment.*/
void use(string varName, EnvStack& s)
{
	if(!s.top().findVariable(varName))
	{
		cout << "Envrionment::getVariable: variable not defined" << endl;
	}
	else
	{
		cout << "the value of " << varName << " is "<< s.top().getVariable(varName) << endl;
	}
}
/*Prints name of current environement and then all the variable value pairs in the current environment*/
void print(EnvStack& s)
{
	cout << "current environment " << s.top().getEnvName() << ":  " << s.top() << endl;
}
/*Simulates entering a new local environment such as would occur when a function is called. The name
entered with the command simulates the name of the function called. Prints a message indicating that
a new environment is being started, including function name. THe curent local enrionment is saved on 
the stack and the new local environment is started*/
void enter(string funcName, EnvStack& s)
{
	s.push(Environment(funcName));
	cout << "entering " << funcName << endl;
}
/*Simulates leaving a local environment such as would occur when returning from a function. Prints a
message indicating the exit from an envrironment, including the name of the exiting function. when an 
eit has been made from the "main" environment, a special message is printed and the program terminates.
The current local environment is thrown away, and the previous environment saved is restored.
*/
void exit(EnvStack& s)
{
	if(s.top().getEnvName() == "main")
	{
		s.pop();
	}
	else
	{
	cout << "exiting " << s.top().getEnvName() << endl;
	s.pop();
	}
}

