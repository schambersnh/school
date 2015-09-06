#
# Makefile for thread tests
#

CC = gcc
CFLAGS = -g
EXES = test1 test2 test3 test4a test4b test5 testkill1 testkill2 testkill3 \
     testsusp1 testsusp2 testsusp3

all: $(EXES)

test1: test1.o thread.o thr_asm.o
	$(CC) $(CFLAGS) test1.o thread.o thr_asm.o -o test1

test2: test2.o thread.o thr_asm.o
	$(CC) $(CFLAGS) test2.o thread.o thr_asm.o -o test2

test3: test3.o thread.o thr_asm.o
	$(CC) $(CFLAGS) test3.o thread.o thr_asm.o -o test3

test4a: test4a.o thread.o thr_asm.o
	$(CC) $(CFLAGS) test4a.o thread.o thr_asm.o -o test4a

test4b: test4b.o thread.o thr_asm.o
	$(CC) $(CFLAGS) test4b.o thread.o thr_asm.o -o test4b

test5: test5.o thread.o thr_asm.o
	$(CC) $(CFLAGS) test5.o thread.o thr_asm.o -o test5

testkill1: testkill1.o thread.o thr_asm.o
	$(CC) $(CFLAGS) testkill1.o thread.o thr_asm.o -o testkill1

testkill2: testkill2.o thread.o thr_asm.o
	$(CC) $(CFLAGS) testkill2.o thread.o thr_asm.o -o testkill2

testkill3: testkill3.o thread.o thr_asm.o
	$(CC) $(CFLAGS) testkill3.o thread.o thr_asm.o -o testkill3

testsusp1: testsusp1.o thread.o thr_asm.o
	$(CC) $(CFLAGS) testsusp1.o thread.o thr_asm.o -o testsusp1

testsusp2: testsusp2.o thread.o thr_asm.o
	$(CC) $(CFLAGS) testsusp2.o thread.o thr_asm.o -o testsusp2

testsusp3: testsusp3.o thread.o thr_asm.o
	$(CC) $(CFLAGS) testsusp3.o thread.o thr_asm.o -o testsusp3

thread.o: thread.c
	$(CC) $(CFLAGS) -c thread.c

thr_asm.o: thr_asm.s
	$(CC) $(CFLAGS) -c thr_asm.s

clean:
	-rm -f *.o $(EXES)

