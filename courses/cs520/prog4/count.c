// This program is designed to illustrate the standard
// I/O functions open, read and close. It simply counts
// the number of characters in an input file, given by
// its only command-line argument.

#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>

#define BUFLEN 100

int main(int argc, char *argv[])
{
  if (argc < 2)
  {
    fprintf(stderr, "there should be exactly one argument!\n");
    exit(-1);
  }
  int c = 1;
  for(c = 1; c < argc; c++)
  {
  int fd = open(argv[c], O_RDONLY);

  // open returns -1 if open of file failed
  if (fd == -1)
  {
    fprintf(stderr, "can't open %s for reading!\n", argv[c]);
    exit(-1);
  }

  int totalCount = 0;
  int lastRead;
  char buf[BUFLEN];

  // read will return 0 on EOF or the number of characters read
  while ((lastRead = read(fd, buf, BUFLEN)))
  {
    // read could also return -1 if there is an error (not EOF)
    if (lastRead == -1)
    {
      fprintf(stderr, "error reading file %s!\n", argv[c]);
      exit(-1);
    }
    totalCount += lastRead;
  }

  close(fd);

  printf("%d characters in file %s\n", totalCount, argv[c]);
  }
  return 0;

}

