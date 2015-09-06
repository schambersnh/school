# Makefile
# makefile for frep
# Author: Stephen Chambers
# Date: 10/6/14
all: clean frep
CC = gcc
CCFLAGS = -O -Wall
UNAME_S := $(shell uname -s)
ifeq ($(UNAME_S), SunOS)
	CCFLAGS += -g
else
	CCFLAGS += -Wextra -g
endif
frep: recompiler.o frep.o interpreter.o two_way_ll.o
	$(CC) $(CCFLAGS) recompiler.o frep.o interpreter.o two_way_ll.o -o frep
frep.o: frep.c
	$(CC) $(CCFLAGS) frep.c -c
interpreter.o: interpreter.c
	$(CC) $(CCFLAGS) interpreter.c -c
two_way_ll.o: two_way_ll.c
	$(CC) $(CCFLAGS) two_way_ll.c -c
clean :
	rm -rf frep.o interpreter.o two_way_ll.o frep
