# Makefile
# makefile for frep
# Author: Stephen Chambers
# Date: 10/6/14
all: clean frep frepd
CC = gcc
CCFLAGS = -O -Wall
UNAME_S := $(shell uname -s)
ifeq ($(UNAME_S), SunOS)
	CCFLAGS += -g -lpthread
endif
ifeq ($(UNAME_S), Linux)
	CCFLAGS += -Wextra -g -pthread -lpthread
endif
ifeq ($(UNAME_S), Darwin)
	CCFLAGS += -Wextra -g -lpthread
endif

frep: recompiler.o frep.c interpreter.c cactus_stack.c frep_common.c no_sigpipe.c tcpblockio.c send_recv.c
	$(CC) $(CCFLAGS) recompiler.o frep.c interpreter.c send_recv.c cactus_stack.c no_sigpipe.c tcpblockio.c frep_common.c -o frep
frepd: recompiler.o frepd.c interpreter.c cactus_stack.c send_recv.c frep_common.c no_sigpipe.c tcpblockio.c
	$(CC) $(CCFLAGS) recompiler.o frepd.c interpreter.c send_recv.c cactus_stack.c tcpblockio.c no_sigpipe.c frep_common.c -o frepd
clean :
	rm -rf frep frepd interpreter.o cactus_stack.o frep.o
