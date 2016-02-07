/**
 * \file sort_skele.c
 *
 * Sorts integers from a file using various algorithms.
 *
 * \author eaburns
 * \date 28-07-2010
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include <sys/time.h>

/* The various sorting algorithm names */
static const char *counting_sort_str = "counting";
static const char *radix_sort_str = "radix";
static const char *quick_sort_str = "quick";
static const char *insertion_sort_str = "insertion";
static const char *system_quick_sort_str = "system_quick";

/* Number of bits in an unsigned long. */
static const unsigned long ulong_bits = 8 * sizeof(unsigned long);

/* Function prototypes. */
unsigned int quicksort(unsigned long[], unsigned long, unsigned long);
unsigned long partition(unsigned long[], unsigned long, unsigned long);

/************************************************************
 * Functions that you must implement
 ************************************************************/

/*
 * Radix sort
 * Return 0 on success and 1 on failure.
*/
static unsigned int do_radix_sort(unsigned long ary[],
				  unsigned long n, unsigned long nbits)
{
  unsigned long key_len = 2;
  unsigned long * counts;
  unsigned long * positions;
  unsigned long * new_arr;
  unsigned long i = 0;
  unsigned long j = 0;
  unsigned long d = 0;
  unsigned long temp = 0;
  unsigned long counter = 0;
  unsigned long index = 0;
  
  /* initialize counts */
  counts = calloc(2, sizeof(unsigned long));
  if(counts == NULL){
      fprintf(stderr, "Calloc failed.");
      return 1;
  }

  /* initialize positions */
  positions = calloc(key_len, sizeof(unsigned long));
  if(positions == NULL){
      fprintf(stderr, "Calloc failed.");
      return 1;
  }

  /* initialize new_arr */
  new_arr = calloc(n, sizeof(unsigned long));
  if(new_arr == NULL){
      fprintf(stderr, "Calloc failed.");
      return 1;
  }

  for(d = 0; d < nbits; d++){
    /* fill in counts */
    for(i = 0; i < n; i++){
      temp = ary[i] >> d;
      index = temp & 1;
      counts[index]++;
    }
    /* fill in positions */
    for(j = 0; j < key_len; j++){
      positions[j] = counter;
      counter += counts[j];
    }
    /* sort */
    for(i = 0; i < n; i++){
      temp = ary[i] >> d;
      index = temp & 1;
      new_arr[positions[index]] = ary[i];
      positions[index]++;
    }

    /* copy the array */
    for(i = 0; i < n; i++){
      ary[i] = new_arr[i];
    }

    /* zero the count array */
    memset(counts, 0, sizeof(unsigned long) * key_len);
    counter = 0;
  }
  free(counts);
  free(positions);
  free(new_arr);
  return 0;   
}



/*
 * Counting sort
 * Return 0 on success and 1 on failure.
 */
static unsigned int do_counting_sort(unsigned long ary[],
				     unsigned long n, unsigned long nbits)
{
    unsigned long i = 0;
    unsigned long j = 0;
    unsigned long index = 0;
    unsigned long bucket_len = 0;
    unsigned long * buckets;

    if (nbits > 20)
        exit(EXIT_FAILURE);

    bucket_len = 2 << nbits;
    buckets = calloc(bucket_len, sizeof(unsigned long));
    if(buckets == NULL){
        fprintf(stderr, "Calloc failed");
        return 1;
    }
    
    /* fill the buckets with the correct counts */
    for(i = 0; i < n; i++){
      buckets[ary[i]]++;
    }

    /* output the sorted array */
    for(i = 0; i < bucket_len; i++){
       for(j = 0; j < buckets[i]; j++){
           ary[index] = i;
           index++;    
       } 
    }
    free(buckets);
    return 0;
}


/*
 * Quicksort
 * Return 0 on success and 1 on failure.
 */
static unsigned int do_quicksort(unsigned long ary[],
				  unsigned long n, unsigned long nbits)
{
   return quicksort(ary, 0, n - 1);
}

unsigned int quicksort(unsigned long ary[], unsigned long start, unsigned long end){
    unsigned long p = 0;
    if(start < end){
      p = partition(ary, start, end);
      if(p > 0)
        quicksort(ary, start, p - 1);
      quicksort(ary, p + 1, end);
    }
    return 0;
}

unsigned long partition(unsigned long ary[], unsigned long start, unsigned long end){
   unsigned long pivot = 0;
   unsigned long i = 0;
   unsigned long j = 0;
   unsigned long temp = 0;
   unsigned long temp2 = 0;
 
   pivot = ary[end];
   i = start;
   for(j = start; j < end; j++){
       if(ary[j] <= pivot){
           /* swap ary[i] and ary[j] */
           temp = ary[i];
           ary[i] = ary[j];
           ary[j] = temp;
           i++;
       }
   }
   /* swap ary[i] with ary[end] */
   temp2 = ary[i];
   ary[i] = ary[end];
   ary[end] = temp2;
   return i;
}

/************************************************************
 *
 * You probably don't need to modify anything beyond here.
 *
 ************************************************************/


/************************************************************
 * Example: Using the standard library's qsort() routine.
 ************************************************************/

/* The comparison function for qsort(). */
static int compare(const void *_a, const void *_b)
{
    unsigned long *a = (unsigned long *) _a;
    unsigned long *b = (unsigned long *) _b;

    return *a - *b;
}


/*
 * Uses the standard library quicksort function.
 * Return 0 on success and 1 on failure.
 */
static unsigned int do_system_quicksort(unsigned long ary[],
					unsigned long n,
					unsigned long nbits)
{
    /* This is the system's quick sort function. */
    qsort(ary, n, sizeof(*ary), compare);

    return 0;
}


/************************************************************
 * Example: insertion sort
 ************************************************************/

/*
 * Insertion sort.
 * Return 0 on success and 1 on failure.
 */
static unsigned int do_insertion_sort(unsigned long ary[], unsigned long n,
				      unsigned long nbits)
{
    unsigned long j;

    for (j = 1; j < n; j += 1) {
	unsigned long key = ary[j];
	unsigned long i = j;
	while (i > 0 && ary[i-1] > key) {
	    ary[i] = ary[i-1];
	    i -= 1;
	}
	ary[i] = key;
    }

    return 0;
}

/*
 * Read the header from the input file and returts the number of
 * values in the input using the 'nvalues' argument and the number of
 * bits for each number using the 'nbits' argument.
 *
 * Returns 0 on success and 1 on failure.
 */
static unsigned int read_file_header(FILE * infile, unsigned long *nvalues,
				     unsigned long *nbits)
{
    int ret;
    unsigned long _nvalues, _nbits;

    ret = fscanf(infile, " %lu %lu", &_nvalues, &_nbits);
    if (ret == EOF) {
	fprintf(stderr, "Unexpected end of file\n");
	return 1;
    }
    if (ret != 2) {
	fprintf(stderr, "Malformed file header\n");
	return 1;
    }

    if (_nbits > ulong_bits) {
	fprintf(stderr, "%lu bits input values are too big\n", _nbits);
	fprintf(stderr, "Word size seems to be %lu bits\n", ulong_bits);
	return 1;
    }

    if (nvalues)
	*nvalues = _nvalues;
    if (nbits)
	*nbits = _nbits;

    return 0;
}

/*
 * Reads the next number from the input file into the 'num' argument.
 * If the end of the file is reached then 'num' is left as is and
 * 'eof' is set to 1, otherwise 'eof' is set to zero.
 *
 * Returns 0 on success and 1 on failure.
 */
static unsigned int read_next_number(FILE * infile, unsigned long *num,
				     unsigned int *eof)
{
    int ret;
    unsigned long _num;

    ret = fscanf(infile, " %lu", &_num);
    if (ret == EOF) {
	*eof = 1;
	return 0;
    }
    if (ret != 1) {
	perror("fscanf failed");
	return 1;
    }

    *num = _num;
    *eof = 0;

    return 0;
}

/*
 * Reads 'n' numbers from the given input file into the provided
 * array.
 *
 * Returns 0 on success and 1 on failure.  The state of 'ary' on
 * failure is unspecified.
 */
static unsigned int read_into_array(FILE * infile, unsigned long n,
				    unsigned long ary[])
{
    unsigned long i;

    for (i = 0; i < n; i += 1) {
	unsigned int err, eof;
	err = read_next_number(infile, &ary[i], &eof);
	if (err)
	    return 1;
	if (eof) {
	    fprintf(stderr, "Unexpected EOF when reading %lu values", n);
	    return 1;
	}
    }

    return 0;
}

/* Writes the given number to the output file. */
static void output_number(FILE * outfile, unsigned long num)
{
    fprintf(outfile, "%lu\n", num);
}

/* Output the given array to the output file. */
static void output_from_array(FILE * outfile, unsigned long ary[],
			      unsigned long n)
{
    unsigned long i;

    for (i = 0; i < n; i += 1)
	output_number(outfile, ary[i]);
}

/* Print the usage string */
static void usage(void)
{
    fprintf(stderr, "usage: sort <algorithm> <infile> <outfile>\n");
    fprintf(stderr,
	    "Where <algorithm> is one of: counting, radix, quick,\n"
	    "                             insertion or system_quick\n"
	    "and <infile> and/or <outfile> may be '-' to indicate that\n"
	    "the standard input and/or output stream should be used\n");
}


/*
 * Reads the file header and the values.  The return value is an array
 * of values that must be freed by the caller.
 */
static unsigned long *get_values(FILE * infile, unsigned long *n,
				 unsigned long *nbits)
{
    unsigned int err;
    unsigned long _n, _nbits;
    unsigned long *ary;

    err = read_file_header(infile, &_n, &_nbits);
    if (err)
	return NULL;

    ary = malloc(sizeof(*ary) * _n);
    if (!ary) {
	perror("Failed to allocate array");
	return NULL;
    }

    err = read_into_array(infile, _n, ary);
    if (err) {
	free(ary);
	return NULL;
    }

    if (n)
	*n = _n;
    if (nbits)
	*nbits = _nbits;

    return ary;
}

/* Gets the time of day in seconds. */
static double get_current_seconds(void)
{
    double sec, usec;
    struct timeval tv;

    if (gettimeofday(&tv, NULL) < 0) {
	perror("gettimeofday failed");
	exit(EXIT_FAILURE);
    }

    sec = tv.tv_sec;
    usec = tv.tv_usec;

    return sec + (usec / 1000000);
}

/*
 * Reads the values, begins the timer, calls the sorting algorithm,
 * stops the timer and outputs the values.  The time taken is printed
 * to standard error.
 */
static unsigned int do_sort(const char *const algorithm, FILE * infile,
			    FILE * outfile)
{
    int err = 0;
    double start, end;
    unsigned long n, nbits;
    unsigned long *ary;

    ary = get_values(infile, &n, &nbits);
    if (!ary)
	return 1;

    start = get_current_seconds();

    if (strcmp(algorithm, counting_sort_str) == 0) {
	err = do_counting_sort(ary, n, nbits);

    } else if (strcmp(algorithm, radix_sort_str) == 0) {
	err = do_radix_sort(ary, n, nbits);

    } else if (strcmp(algorithm, quick_sort_str) == 0) {
	err = do_quicksort(ary, n, nbits);

    } else if (strcmp(algorithm, insertion_sort_str) == 0) {
	err = do_insertion_sort(ary, n, nbits);

    } else if (strcmp(algorithm, system_quick_sort_str) == 0) {
	err = do_system_quicksort(ary, n, nbits);

    } else {
	fprintf(stderr, "Impossible\n");
	exit(EXIT_FAILURE);
    }

    end = get_current_seconds();

    output_from_array(outfile, ary, n);
    fprintf(stderr, "%f\n", end - start);

    free(ary);

    return err;
}


int main(int argc, char *const argv[])
{
    int ret = EXIT_SUCCESS;
    unsigned int err;
    FILE *infile = stdin, *outfile = stdout;

    if (argc < 4 || (strcmp(argv[1], counting_sort_str) != 0
		     && strcmp(argv[1], radix_sort_str) != 0
		     && strcmp(argv[1], quick_sort_str) != 0
		     && strcmp(argv[1], insertion_sort_str) != 0
		     && strcmp(argv[1], system_quick_sort_str) != 0)) {
	usage();
	return EXIT_FAILURE;
    }


    if (strcmp(argv[2], "-") != 0) {
	infile = fopen(argv[2], "r");
	if (!infile) {
	    perror("Failed to open input file for reading");
	    ret = EXIT_FAILURE;
	    goto out;
	}
    }

    if (strcmp(argv[3], "-") != 0) {
	outfile = fopen(argv[3], "w");
	if (!outfile) {
	    perror("Failed to open output file for writing");
	    ret = EXIT_FAILURE;
	    goto out;
	}
    }

    err = do_sort(argv[1], infile, outfile);
    if (err)
	ret = EXIT_FAILURE;

  out:

    if (outfile && outfile != stdout)
	fclose(outfile);
    if (infile && infile != stdin)
	fclose(infile);

    return ret;
}
