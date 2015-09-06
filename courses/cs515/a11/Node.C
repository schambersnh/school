/*
Stephen Chambers Program 11
Node class is designed to hold an arbitrary integer value identifying the node
in addition to holding an array of Node pointers to rooms the Node
can acess to the north, south, east, and west.
*/
#include <iostream>
#include <fstream>
#include "Node.h"
using namespace std;

        Node::Node(int id)
            // constructs a Node with given id, initializes paths array
	{
		myId = id;
		visited = false;
		paths = new Node*[4];
		for(int i = 0; i < 4; i++)
		{
			paths[i] = 0;
		}
	}

        Node::~Node()
            // destructor
	{
		
		destructCode();
		
	}

        Node::Node(const Node & v)
            // copy
	{
		copyCode(v);	
	}

        Node & Node::operator = (const Node & v)
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

        int Node::getId()
            // returns Node Id
	{
		return myId;
	}


        void Node::setPaths(Node * north, Node * east, Node * south, Node * west)
            // Sets pointers determing where the node can travel
	    //in order north east south west 
	{
		paths[0] = north;
		paths[1] = east;
		paths[2] = south;
		paths[3] = west;
	}
	

		Node ** Node::getPaths()
		//returns an array of paths that the node can travel to
		//in order north east south west
		{
			return paths;
		}
	
		bool Node::getVisited()
		//returns whether or not the Node has been visited
		{
			return visited;
		}
	
		void Node::setVisited(bool b)
		//sets visited value
		{
			visited = b;
		}

		void Node::setDirection(int d)
		//sets direction value
		{
			direction = d;
		}

		int Node::getDirection()
		//returns what direction the Node traveled in
		{
			return direction;
		}

        void Node::output(ostream & s) const
            // outputs in form id north east south west
	{
		s << myId << "\t";
		for(int i = 0; i < 4; i++)
		{
			if(paths[i] == 0)
			{
				s << "--\t";
								
				
			}
			else
			{
				s << paths[i] -> getId() << "\t";		
			}
		}
	}
		void Node::destructCode()
		//common destruct code
		{
			delete []paths;
		}
	
		void Node::copyCode(const Node & v)
		//common copy code
		{
			myId = v.myId;
			paths = v.paths;
		}

		ostream & operator << (ostream & s, const Node &v)
    	// outputs using named operation
		{
			v.output(s);
			return s;
		}