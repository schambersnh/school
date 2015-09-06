/* 
Stephen Chambers Program 11
This class defines a maze which holds chambers. A graph holds
the number of chambers in the maze and an array of pointers to
chambers representing the maze. The first element in the maze is the
outside world. The graph is able to return a pointer to a node in the maze given its id.
It is also able to find a path from the chamber that the adventurer was 
teleported to the outside world.
*/

#include <iostream>
#include "Graph.h"
#include "NodeStack.h"
using namespace std;


        Graph::Graph(int size)
            // initializes chambers array
	{	
		numChambers = size + 1;
		chambers = new Node*[numChambers];
		for(int i = 0; i < numChambers; i++)
		{
			chambers[i] = 0;
		}
		Node * outside = new Node(0);
		chambers[0] = outside;
		
	}

         Graph::~Graph()
            // destructor
	{
		destructCode();
	}

         Graph::Graph(const Graph & v)
            // copy
	{
		copyCode(v);
	}

        Graph &  Graph::operator = (const Graph & v)
            // assignment
	{
		if(this != &v)
		{
			(*this).destructCode();
			copyCode(v);
			return (*this);
		}
		else
		{
			return (*this);
		}
	}
		Node * Graph::getNode(int id)
			//returns pointer to node in maze with current id
		{
			for(int i = 0; i < numChambers; i++)
			{
				if(id == -1)
				{
					return 0;
				}
				if(chambers[i] -> getId() == id)
				{
					return chambers[i];
				}
			}
		}

		bool Graph::findPath(int startPosition, NodeStack & s)
		//finds path between node teleported to and the outside world
		//recurses through graph, keeping track of nodes that were visted
		//and direction that the adventurer traveled in. Nodes already visited
		//or paths resulting in not being able to get out of the maze can 
		//simply be popped off the stack.
		{
			Node * start = getNode(startPosition);
			if(start -> getVisited())
			{
				return false;
			}
			start -> setVisited(true);
			s.push(start);
			if(start -> getId() == 0)
			{
				return true;
			}
	
			if(s.empty())
			{
				return false;
			}
			
			Node ** paths = start -> getPaths();
			for(int i = 0; i < 4; i ++)
			{
				if(paths[i] != 0)
				{
					start -> setDirection(i);
					if(findPath(paths[i] -> getId(), s))
					{
						return true;
					}	
				}
					
			}
			s.pop();
			return false;
		}
	
        void  Graph::output(ostream & s) const
            // outputs in form id north east south west
		{
			cout << "id\tnorth\teast\tsouth\twest\t" << endl;
			for(int i = 0; i < numChambers; i++)
			{
				s << *(chambers[i]) << endl;
			}
		}
		void Graph::insert(Node * v, int position)
		//inserts node into graph, chamber into maze
		{
			chambers[position] = v;
		}

		void Graph::destructCode()
		//common destruct code
		{
			for(int i = 0; i < numChambers; i++)
			{
				delete chambers[i];
			}
			delete []chambers;
		}

		void  Graph::copyCode(const Graph & v)
		//common copy code
		{
			numChambers = v.numChambers;
			for(int i = 0; i < numChambers; i++)
			{
				chambers[i] = v.chambers[i];
			}
		}
	
		ostream & operator << (ostream & s, const Graph &v)
    		// outputs using named operation
		{
			v.output(s);
			return s;
		}