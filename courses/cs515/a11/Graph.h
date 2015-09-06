#ifndef GRAPH_H
#define GRAPH_H

/* 
Stephen Chambers Program 11
This class defines a maze which holds chambers. A graph holds
the number of chambers in the maze and an array of pointers to
chambers representing the maze. The graph is able to 
return a pointer to a node in the maze given its id. It is also able to find 
a path from the chamber that the adventurer was teleported to the outside world.
*/

#include <iostream>
#include "Node.h"
#include "NodeStack.h"
using namespace std;

class Graph {

    public:

        Graph(int);
            // constructs a graph with the number of chambers

        ~Graph();
            // destructor

        Graph(const Graph &);
            // copy

        Graph & operator = (const Graph &);
            // assignment

		Node * getNode(int);
		//returns pointer to node in maze with current id

		bool findPath(int, NodeStack & s);
			//finds path between node teleported to and the outside world
		//recurses through graph, keeping track of nodes that were visted
		//and direction that the adventurer traveled in. Nodes already visited
		//or paths resulting in not being able to get out of the maze can 
		//simply be popped off the stack.

        void output(ostream & s) const;
            // outputs in form id north east south west

		void insert(Node*, int);
		//inserts node into graph, chamber into maze

		void destructCode();
		//common destruct code

		void copyCode(const Graph & v);
		//common copy code
	
		private:
			int numChambers;
			Node ** chambers;
};

		ostream & operator << (ostream & s, const Graph &v);
	    // outputs using named operation

#endif
