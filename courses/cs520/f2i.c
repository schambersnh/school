/*
Stephen Chambers
Float to Int converter
*/
int floatToInt(float f)
{
  unsigned int sign = *(int*)&f & 0x80000000;
  unsigned int exp = *(int*)&f & 0x7F800000;
  unsigned int sig = *(int*)&f & 0x007FFFFF;

  printf("f: %0x\n", f);
  //insert hidden one bit
  sig = sig | 0x00800000;
  exp >>= 23;
  int realExp = exp - 127;

  float value = pow(2, realExp);
  int i = 0;
  float sum = 0.0; 
  int count = 0;
  int set = 23;
  unsigned int and = 0x01;
  for(i = 0; i < 23; i++)
  {
    if((sig & (and << set)) != 0)
    {
      sum += pow(2, count);
    }
    count--;
    set--;
  }
  if(sign == 0x80000000)
  {
    return -1 * value * sum;
  }
  else
  {
    return value * sum;
  }
}
