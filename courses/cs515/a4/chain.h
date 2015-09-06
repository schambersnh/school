#ifndef CHAIN_H
#define CHAIN_H

/* this structure defines an element in a chain of apple pickers.
 * Each element contains a worker object and a link to the next
 * element in the chain.
*/

#include "Worker.h"

struct Elem {
    Worker info;
    Elem * next;
};

#endif
