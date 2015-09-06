# Makefile
# makefile for frep
# Author: Stephen Chambers
# Date: 10/6/14
all: clean frep
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

frep: recompiler.o frep.c interpreter.c cactus_stack.c
	$(CC) $(CCFLAGS) recompiler.o frep.c interpreter.c cactus_stack.c -o frep
clean :
	rm -rf frep interpreter.o cactus_stack.o frep.o
