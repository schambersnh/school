#
# Makefile for Posix Threads pi program
#

CC = gcc

CFLAGS = -O -pthread

LDFLAGS = -O -lpthread

pi: pi.o timer.o
	$(CC) $(CFLAGS) pi.o timer.o $(LDFLAGS) -o pi

pi.o: pi.c
	$(CC) $(CFLAGS) -c pi.c

timer.o: timer.c
	$(CC) $(CFLAGS) -c timer.c

clean:
	rm -f pi *.o

