/*
Stephen Chambers
Brian Johnson 
Program 11
December 7, 2011
This program has an adventurer teleported to a maze at any specific chamber.
The chambers are Nodes on a graph, and each node knows what chambers it can 
travel to in the northerly, southerly, easterly, or westerly direction. The 
program can print the original maze by a dump switch, -d, given on the command 
line. Regardless of whether the dump switch was given, the program opens up 
the input file via the command line, constructs a graph, sets all the paths
the current chambers can take, then recursively searches through the graph for
a path out of the maze. The path is being kept in a stack, so paths already 
visited and wrong paths can be simply popped off.
*/
#include <iostream>
#include <fstream>
#include "Node.h"
#include "Graph.h"
#include "NodeStack.h"
#include <string.h>
#include <stdlib.h>

void createGraph(int, Graph &, ifstream &);
void pathFound(int, Graph &);

int main(int argc, const char * argv[])
{
	int numChambers = 0;
	int teleportId = 0;
	bool printMaze = false;
	ifstream f1;

	/*The following code tests the command line arguments. If an invalid ammount is supplied,
	the program aborts. If the dump switch is there, it tells the program later on to print
	out the maze. If the graph file cannot be opened, the program is exited.*/

	//////////////////////////////////////////////////////////////////////////////////////
	if(argc < 2 || argc > 3)
	{
		cout << "Invalid amount of command line arguments, exiting program" << endl;
		exit(1);
	}
	if(strcmp(argv[1], "-d") == 0)
	{
		f1.open(argv[2]);
		printMaze = true;
	}
	else
	{
		f1.open(argv[1]);
	}
	if(!f1)
	{
		cout << "Graph file failed to open, exiting program " << endl;
		exit(1);
	}
	/////////////////////////////////////////////////////////////////////////////////////////
	f1 >> numChambers;
	Graph g(numChambers);
	createGraph(numChambers, g, f1);
	
	if(printMaze)
		cout << g << endl;
	
	f1 >> teleportId;
	pathFound(teleportId, g);
	return 0;
}

/*This method takes in the number of chambers in the graph, a file to read in the information
needed by the graph, and a graph reference created in the main program. The method reads in 
an id from the file, creates a temporary node, and inserts that node into the graph. Once the graph
has chambers with 0 pointer paths, an id is read in from the file, representing the id of a node
already in the graph. The graph returns a pointer to that node, and ids of the nodes that the aforementioned
node can travel to are inputed. The graph then uses that node pointer to set the paths of that node to the
nodes in the graph with the corresponding ids just inputed from the file.*/
void createGraph(int numChambers, Graph & g, ifstream & f1)
{
	int id = 0;
	int north = 0;
	int east = 0;
	int south = 0;
	int west = 0;
	for(int i = 0; i < numChambers; i++)
	{
		//read in an id, create a temporary node, insert that node into the graph
		f1 >> id;
		Node * temp = new Node(id);
		g.insert(temp, i + 1);	
	}
	for(int j = 0; j < numChambers; j++)
	{
		//get a pointer pointing to a node in the graph with inputed id
		f1 >> id;
		Node * myNode = g.getNode(id);
		//input chambers
		f1 >> north >> east >> south >> west;
		
		//get pointers to chambers with inputed id in the graph
		Node * northNode = g.getNode(north);
		Node * eastNode = g.getNode(east);
		Node * southNode = g.getNode(south);
		Node * westNode = g.getNode(west);
		
		//set paths of the node
		myNode -> setPaths(northNode, eastNode, southNode, westNode);
	}
}
/*This method simply calls findPath(int, NodeStack&) in graph, and an appropriate message is
printed based on whether or not a path was found. If a path was found, call recursive output 
function of stack so path is displayed in correct order (<< is overloaded for recursive output)*/
void pathFound(int teleportId, Graph & g)
{
	NodeStack s;
	if(!g.findPath(teleportId, s))
	{
		cout << "You sir, are dead. (No path from teleported position)" << endl;
	}
	else
	{
		cout << s;
	}
}
