#ifndef NODE_H
#define NODE_H

/*
Stephen Chambers Program 11
This class defines a chamber in a maze, or in computing terms,
a node in a graph. The chamber, or node, holds an array
of pointers to other chambers that they can travel to in a northerly,
southerly, easterly, or westerly direction. Each node has an arbitrary
integer for identifying the chamber and it keeps track of whether or 
not it has been visited to ensure that circular paths do not occur in the 
graph. Additionally, a node keeps track of what direction it travels in so
the main program knows what direction to go within the maze. 
*/

#include <iostream>
#include <fstream>
using namespace std;

class Node {

    public:
        Node(int);
            // constructs a Node with given id

        ~Node();
            // destructor

        Node(const Node &);
            // copy

        Node & operator = (const Node &);
            // assignment

        int getId();
            // returns Node Id


        void setPaths(Node *, Node *, Node *, Node *);
            // Sets pointers determing where the node can travel
			//in order north east south west 
	
		Node ** getPaths();
		//returns an array of paths that the node can travel to
		//in order north east south west

		bool getVisited();
		//returns whether or not the Node has been visited

		void setVisited(bool);
		//sets visited value

		void setDirection(int);
		//sets direction value

		int getDirection();
		//returns what direction the Node traveled in

        void output(ostream & s) const;
            // outputs in form id north east south west

		void destructCode();
		//common destruct code

		void copyCode(const Node & v);
		//common copy code


    private:

		int myId;
		bool visited;
		int direction;
		Node ** paths;
	
};

ostream & operator << (ostream & s, const Node &v);
    // outputs using named operation

#endif
