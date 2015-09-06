/* send_recv.c */

#define _POSIX_C_SOURCE 200809L
#define _ISOC99_SOURCE
#define _XOPEN_SOURCE 700
#define __EXTENSIONS__

#include "send_recv.h"
#include "messages.h"

/* writes a fixed-size header followed by variable length data
 *
 * fills in header with values from type, length and info
 * writes length bytes of variable length data from user_data
 *
 * returns 0 if all ok, else -1 on any error
 */
int
our_send_message(int fd, enum message_types type, unsigned int length, void *user_data)
{
	struct our_header header;

	header.message_type = htonl(type);
	header.var_length = htonl(length);

	/* write fixed-size header */
	if (writeblock(fd, &header, sizeof(header)) != sizeof(header)) {
		return -1;
	}

	if (length > 0 ) {
		if (writeblock(fd, user_data, length) != (signed)length) {
			return -1;
		}
	}
	return 0;
}	/* our_send_message */

/* reads a fixed-size header followed by variable length data
 *
 * fills in type, length and info with values from the header
 * reads up to *length bytes of variable length data into user_data
 *
 * returns number of bytes of user_data read, or -1 on error
 */
int
our_recv_message(int fd, enum message_types *type, unsigned int *length, void *user_data)
{
	struct our_header	header;
	unsigned int		user_data_length;

	user_data_length = *length;
	if (readblock(fd, &header, sizeof(header)) != sizeof(header)) {
		return -1;
	}

	*type = ntohl(header.message_type);
	*length = ntohl(header.var_length);

	if (*length > user_data_length) {
		fprintf(stderr, "message length %u longer than available "
				"length %u\n", *length, user_data_length);
		return -1;
	}

	if (*length > 0)
		return readblock(fd, user_data, *length);

	return 0;
}	/* our_recv_message */

/* vi: set autoindent tabstop=8 shiftwidth=8 : */
