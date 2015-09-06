/*
Stephen Chambers
Professor Johnson
10/18/11
Program Description
*/
#include <iostream>
#include "PriCopyJobQueue.h"
#include "CopyJob.h"
#include <string>
using namespace std;

enum State {idle, busy};
void Interpret(char, State&, PriCopyJobQueue&, CopyJob&);
void enterJob(int, State&, PriCopyJobQueue&, CopyJob&);
void finishJob(State&, CopyJob&, PriCopyJobQueue&);
void listQueue(PriCopyJobQueue&);
void done(CopyJob&, PriCopyJobQueue&);



int main()
{
	cout << "enter commands, end with d";
	char command;
	State curState = idle;
	PriCopyJobQueue queue;
	CopyJob curJob;

	
	cin.get(command);
	while(cin)
	{
		Interpret(command, curState, queue, curJob);
		cout << "\n";
		cin.get(command);
	}
}

void Interpret(char c, State & curState, PriCopyJobQueue & q, CopyJob & curJob)
{
	if(c == 'r')
	{
		int priority;
		cin >> priority;
		enterJob(priority, curState, q, curJob);
	}
	else if(c == 'f')
	{
		finishJob(curState, curJob, q);
	}
	else if (c == 'l')
	{	
		listQueue(q);
	}
	else if(c == 'd')
	{
		done(curJob, q);
	}
}
void enterJob(int p, State & curState, PriCopyJobQueue & q, CopyJob & curJob)
{
	static int id = 0;
	CopyJob newJob(++id);
	newJob.input(cin);
	if(curState == idle)
	{
	curJob = newJob;
	curState = busy;
	cout << "starting job [" << curJob << "]";
	}
	else if(curState == busy)
	{
	cout << "putting job [" << newJob << "] onto queue with priority (" << p << ")";
	q.enqueue(p, newJob);
	}
}
void finishJob(State & curState, CopyJob& curJob, PriCopyJobQueue & q)
{
	if(curState == idle)
	{
		cout << "not working on a job";
	}
	else if(curState == busy)
	{
		CopyJob f = CopyJob();
		cout << "finished job [" << curJob << "]";
		if(q.front(f))
		{
		cout << "\nstarting job [" << f << "]";
		curJob = f;
		q.dequeue();
		}
		else
		{
		cout << "nothing in queue, worker becoming idle";
		curState = idle;
		}
		
		
	}
}
void listQueue(PriCopyJobQueue & q)
{
	cout << q;
}
void done(CopyJob & curJob, PriCopyJobQueue & q)
{
cout << "finished job [" << curJob << "]";
cout << "\nworker is done for the day\n";
cout << q;
}
