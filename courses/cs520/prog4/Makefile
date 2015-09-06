#
# Makefile for prog4 posix threads program
#

CC = gcc

CFLAGS = -O -pthread

LDFLAGS = -O -lpthread

prog4: prog4.o timer.o
	$(CC) $(CFLAGS) prog4.o timer.o $(LDFLAGS) -o prog4

prog4.o: prog4.c
	$(CC) $(CFLAGS) -c prog4.c

timer.o: timer.c
	$(CC) $(CFLAGS) -c timer.c

clean:
	rm -f prog4 *.o

