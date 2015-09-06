
/*Stephen Chambers
2/9/12
This program is designed to add floats using integer representation. 
*/ 
 #include <stdio.h>
  #include <stdlib.h>
  void srand(unsigned int seed);
  int rand(void);

  int main(void)
  {
    float f1, f2;

    //This code is for testing specific cases 	
    
    f1 = -1.0;
    f2 = 3.0;
    unsigned int result;
    float fresult;
    float totalCount = 0.0;
    float successCount = 0.0;

    result = FloatAdd(*(int*)&f1, *(int*)&f2);
    fresult = f1 + f2;
   // printf("Result in hex should be: %x\n", *(int*)&fresult);

   
    if(*(float*)&result != fresult)
    {
      printf("Failure.\nfresult: %x\nresult: %x\n", *(int*)&fresult, result);
    }
    else
    {
      printf("fresult: %x\nresult: %x\n", *(int*)&fresult, result);
      printf("Success!\n");
    }
    
     
   /*
	// The following code presents random cases using all exponents
	unsigned int exponent = 0x00000000;
        unsigned int exponent2 = 0x00000000;
	unsigned int sign = 0x00000000;
	int k;
	int randSig = 0;
	int randSig2 = 0;
	unsigned int result;
        float fresult;
        float totalCount = 0.0;
        float successCount = 0.0;
        
      
	
	for(k = 0; k < 10000; k++)
	{
	totalCount += 1.0;  
        f1 = 0x00000000;
        f2 = 0x00000000;

        f1 = rand();
        f2 = rand();

	result = FloatAdd(f1, f2);
	fresult = *(float*)&f1 + *(float*)&f2;
   
        if(result != (*(int*)&fresult))
	{
          printf("\n//////////////////////////////////\n");
	  printf("f1: %08X\nf2: %08X\n", *(int*)&f1, *(int*)&f2);
	  printf("fresult: %08X\nresult: %08X\n", *(int*)&fresult, result);
          printf("//////////////////////////////////\n\n");
	}
	else
	{   
            successCount++;
	    // printf("Success!\n");
        }
       
	  
      }
     printf("Success Rate of program: %f\n", successCount / totalCount);
	*/
  }                              
 int FloatAdd( int o1, int o2) 
{
  //1. Decode Inputs
  unsigned int op1, op2, sign1, exp1, sig1, sign2, exp2, sig2;
  unsigned int deltaExp, sigFinal, guardbits, stickybit, isNormalized;
  stickybit = 0;
  
  op1 = o1;
  op2 = o2; 

  sign1 = (op1 >> 31) & 0x00000001;
  exp1 = (op1 >> 23) & 0x000000FF;
  sig1 = op1 & 0x007FFFFF;

  sign2 = (op2 >> 31) & 0x00000001;
  exp2 = (op2 >> 23) & 0x000000FF;
  sig2 = op2 & 0x007FFFFF;

  
  //add "secret" 1 bit
  unsigned int significand1, significand2;
  if(exp1 >= 0x00000001)
  {
    significand1 = sig1 | 0x00800000;
  }
  else
  {
    significand1 = sig1;
  }
  if(exp2 >= 0x00000001)
  {
    significand2 = sig2 | 0x00800000;
  }
  else
  {
    significand2 = sig2;
  }
  if(exp1 == 0x00)
  {
    exp1 = 0x01;
  }
  else if(exp2 == 0x00)
  {
    exp2 = 0x01;
  }
  if(exp1 == 0xFF)
  {
    return 0x7FC00000 | sig1;
  }
  else if(exp2 == 0xFF)
  {
    return 0x7FC00000 | sig2;
  } 
  //2. Compute difference in the exp.
  if(exp2 > exp1)
  {
    deltaExp = exp2 - exp1; 
  }
  else
  {
    deltaExp = exp1 - exp2;
  }
  //3. Shift smaller numbers significand that distance to the right.
   
  if(exp2 < exp1)
  {
    if(deltaExp > 25)
    {
      unsigned int deltaResult = 0;
      deltaResult = deltaResult | (sign1 << 31) | (exp1 << 23) | (sig1);
      return deltaResult;
    }
    
    guardbits = (significand2 >> (deltaExp-2)) & 0x00000003;
    int i = 0;
    if(deltaExp >=2)
    {
	    for(i = 0; i < deltaExp - 2; i++)
	    {
	      if((significand2 & 0x00000001) == 1)
	      {
			stickybit = 1;
			significand2 >>= 1;
	      }
	      else
	      {
			significand2 >>= 1;
	      }
	    }
	    significand2 >>= 2;
     }
     else if(deltaExp == 1)
     {
       if((significand2 & 0x00000001) == 1)
       {
         guardbits = 0x00000002;
         significand2 >>= 1;
       }
       else
       {
         significand2 >>= 1;
       }
     }
  }
  else
  {
     if(deltaExp > 25)
    {
      unsigned int deltaResult2 = 0;
      deltaResult2 = deltaResult2 | (sign2 << 31) | (exp2 << 23) | (sig2);
      return deltaResult2;
    }
    guardbits = ((significand1 >> (deltaExp-2)) & 0x00000003);
    int j = 0;
    
    if(deltaExp >= 2)
    {
	    for(j = 0; j < deltaExp - 2; j++)
	    {
	      if((significand1 & 0x00000001) == 1)
	      {
			stickybit = 1;
			significand1 >>= 1;
	      }
	      else
	      {
			significand1 >>= 1;
	      }
	    }
	    
	    significand1 >>= 2;
    }
       else if(deltaExp == 1)
     {
       if((significand2 & 0x00000001) == 1)
       {
         guardbits = 0x00000002;
         significand2 >>= 1;
       }
       else
       {
         significand2 >>= 1;
       }
     }
  }
  //4. Add significands
   if((sign1 == 0x00000001 && sign2 == 0x00000001) || 
	   (sign1 == 0x00000000 && sign2 == 0x00000000))
   {
	 sigFinal = significand1 + significand2; 
   }
   else
   {
     if(significand1 > significand2)
     {
      sigFinal = significand1 - significand2;
     }
     else
     {
       sigFinal = significand2 - significand1;
     }
   }


  //5. Normalize if necessary, check for Overflow/Underflow
  unsigned int count = 0;
  int isNormalizedLeft = 0;

  if(sigFinal == 0x00000000)
  {
    if(exp1 != 0xFF && exp2 != 0xFF)
    {
    return 0.0;
    }
    else
    {
      if(exp1 == 0xFF && exp2 == 0xFF)
      {
        if(sign1 == 0 && sign2 == 0)
        {
          return 0x7F800000;
        }
        else if(sign1 == 1 && sign2 == 1)
        {
          return 0xFF800000;
        }
        else
        {
          //NOT A NUMBER
          return 0xFFC00000;
        }
      }
    }
  }
  //First case. 0 to the left of the radex point, must shift over to the left until a one appears
  //decrement the exponent by how many times the significand was shifted left.
  unsigned int tempSig = sigFinal >> 24;
  unsigned int tempCount = 0;
 
  if((tempSig & 0x000000FF) == 0)
  {
          isNormalized = 1;
	  if ((sigFinal & 0x00800000) == 0)
	  {   
		  //printf("First Case.\n");
		  while((sigFinal & 0x00800000) == 0) 
		  {
		    sigFinal <<= 1;
		    count ++;
		  }
		  if (exp2 > exp1)
		  {
		    isNormalizedLeft = 1;
		    exp2 -= count;
	 	  }
		  else
		  {
		    exp1 -= count;
		  }
	  }
  }
  else
  {
	  
	  //Second case. More then two bits to the left of the radex point. Must shift right until only a 1 is in the 24th 
	  //bit position and everything before it are 0's. 
	                                  
	    while((tempSig & 0x000000FF) != 0)
	    {
              isNormalized = 1;
	      tempSig >>= 1;
	      tempCount++;
	     // printf("tempCount: %d", tempCount);
	    }
           if(deltaExp == 0)
           {
             guardbits = (sigFinal & 0x00000001) << 1;
           }
           sigFinal >>= tempCount;
	  if(exp2 > exp1)
	  {
	    exp2 += tempCount;   
	  } 
	  else
	  {
	    exp1 += tempCount;
	  }
  }
  //Check for overflow and underflow
  if (exp2 > exp1)
  {
    if(exp2 == 0x00000000)
    {
      //underflow
      if(sigFinal == 0x00000000)
      {
        //number is 0.0.
        unsigned int returnval = 0x00000000;
        returnval = returnval | (sign1 << 24) | (exp1 << 23) | (sig1);
      }
      else
      {
        //Number is denormalized. 
      }
    }
    else if(exp2 == 0x000000FF)
    {
      //infinity
      if(sign1 == 0x00000001 && exp1 == 0x000000FF && significand1 == 0x00000000)
      {
        //infinity + (-infinity)
        return 0xFFC00000; //NaN
      }
      else
      {
        //infinity + x
        return 0x7F800000;
      }
    }
  }
  else
  {
    if(exp1 == 0x00000000)
    {
      //underflow
      if(sigFinal == 0x00000000)
      {
        //number is 0.0.
        unsigned int returnval = 0x00000000;
        returnval = returnval | (sign2 << 24) | (exp2 << 23) | (sig2);
      }
      else
      {
        //Number is denormalized. 
      }
    }
    else if(exp1 == 0x000000FF)
    {
      //infinity
      if(sign2 == 0x00000001 && exp2 == 0x000000FF && significand2 == 0x00000000)
      {
        //infinity + (-infinity)
        return 0xFFC00000; //NaN
      }
      else
      {
        //infinity + x
        return 0x7F800000;
      }
    }
  }
  
  //6. Round if necessary
  if(sign1 == sign2)
  {
    if(guardbits == 0x00000003) //11
    {
      sigFinal += 1;
    }
    else if(guardbits == 0x00000002) //10
    {
      if(stickybit == 0x00000001)
      {
        sigFinal += 1;
      }
      else
      {
        if((sigFinal & 0x00000001) == 1)
        {
          sigFinal += 1;
        }
      }
    }
  }
  else
  {
    if(guardbits == 0x00000003) // 11
    {
      sigFinal += 1;
    }
  }


  ///////////////////May have to normalize after rounding, repeating code//////////////////
   count = 0;

  if(sigFinal == 0x00000000)
  {
    if(exp1 != 0xFF && exp2 != 0xFF)
    {
    return 0.0;
    }
    else
    {
      if(exp1 == 0xFF && exp2 == 0xFF)
      {
        if(sign1 == 0 && sign2 == 0)
        {
          return 0x7F800000;
        }
        else if(sign1 == 1 && sign2 == 1)
        {
          return 0xFF800000;
        }
        else
        {
          //NOT A NUMBER
          return 0xFFC00000;
        }
      }
    }
  }
  //First case. 0 to the left of the radex point, must shift over to the left until a one appears
  //decrement the exponent by how many times the significand was shifted left.
   tempSig = sigFinal >> 24;
   tempCount = 0;
 
  if(isNormalized == 0)
  {
  if((tempSig & 0x000000FF) == 0)
  {
	  if ((sigFinal & 0x00800000) == 0)
	  {   
		  //printf("First Case.\n");
		  while((sigFinal & 0x00800000) == 0) 
		  {
		    sigFinal <<= 1;
		    count ++;
		  }
		  if (exp2 > exp1)
		  {
		    isNormalizedLeft = 1;
		    exp2 -= count;
	 	  }
		  else
		  {
		    exp1 -= count;
		  }
	  }
     } 
 }
  else
  {
	  
	  //Second case. More then two bits to the left of the radex point. Must shift right until only a 1 is in the 24th 
	  //bit position and everything before it are 0's. 
	          
            if(isNormalized == 0)
            {                        
		    while((tempSig & 0x000000FF) != 0)
		    {
		      tempSig >>= 1;
		      tempCount++;
		    }
		   if(deltaExp == 0)
		   {
		     guardbits = (sigFinal & 0x00000001) << 1;
		   }
		   sigFinal >>= tempCount;
		  if(exp2 > exp1)

		  {
		    exp2 += tempCount;   
		  } 
		  else
		  {
		    exp1 += tempCount;
		  }
           }
  }
  //Check for overflow and underflow
  if (exp2 > exp1)
  {
    if(exp2 == 0x00000000)
    {
      //underflow
      if(sigFinal == 0x00000000)
      {
        //number is 0.0.
        unsigned int returnval = 0x00000000;
        returnval = returnval | (sign1 << 24) | (exp1 << 23) | (sig1);
      }
      else
      {
        //Number is denormalized. 
      }
    }
    else if(exp2 == 0x000000FF)
    {
      //infinity
      if(sign1 == 0x00000001 && exp1 == 0x000000FF && significand1 == 0x00000000)
      {
        //infinity + (-infinity)
        return 0xFFC00000; //NaN
      }
      else
      {
        //infinity + x
        return 0x7F800000;
      }
    }
  }
  else
  {
    if(exp1 == 0x00000000)
    {
      //underflow
      if(sigFinal == 0x00000000)
      {
        //number is 0.0.
        unsigned int returnval = 0x00000000;
        returnval = returnval | (sign2 << 24) | (exp2 << 23) | (sig2);
      }
      else
      {
        //Number is denormalized. 
      }
    }
    else if(exp1 == 0x000000FF)
    {
      //infinity
      if(sign2 == 0x00000001 && exp2 == 0x000000FF && significand2 == 0x00000000)
      {
        //infinity + (-infinity)
        return 0xFFC00000; //NaN
      }
      else
      {
        //infinity + x
        return 0x7F800000;
      }
    }
  }
  
  //6. Round if necessary
  if(sign1 == sign2)
  {
    if(guardbits == 0x00000003) //11
    {
      sigFinal += 1;
    }
    else if(guardbits == 0x00000002) //10
    {
      if(stickybit == 0x00000001)
      {
        sigFinal += 1;
      }
      else
      {
        if((sigFinal & 0x00000001) == 1)
        {
          sigFinal += 1;
        }
      }
    }
  }
  else
  {
    if(guardbits == 0x00000003) // 11
    {
      sigFinal += 1;
    }
  }


  //7. Encode Result
 
  unsigned int result = 0x00000000;
  if(sign1 == 0 && sign2 == 0)
  {
	  if(exp2 > exp1)
	  {
		result |= (exp2 << 23);  
		result |= (sigFinal & 0x07FFFFF);        
	  }
	  else
	  {
		result |= (exp1 << 23);
		result |= (sigFinal & 0x07FFFFF); 
	  }
  }
  else if(sign1 == 1 && sign2 == 1)
  {
	  if(exp2 > exp1)
	  {
		result |= 0x80000000;
		result |= (exp2 << 23);  
		result |= (sigFinal & 0x07FFFFF);        
	  }
	  else
	  {
		result |= 0x80000000;
		result |= (exp1 << 23);
		result |= (sigFinal & 0x07FFFFF); 
	  }
  }
  else
  {
	  //SIGNS ARE DIFFERENT
   if(exp2 > exp1)
  {
    if(significand1 > significand2)
    {
     result |= (sign1 << 31);
	 if(isNormalizedLeft)
	 {
     result |= (exp2 << 23);
	 }
	 else
	 {
       result |= (exp1 << 23);
	 }
     result |= (sigFinal & 0x07FFFFF);        
    }
    else
    {
     result |= (sign2 << 31);
      if(isNormalizedLeft)
	 {
     result |= (exp2 << 23);
	 }
	 else
	 {
       result |= (exp1 << 23);
	 }  
     result |= (sigFinal & 0x07FFFFF);
    }
  }
  else if(exp1 > exp2)
  {
    if(significand1 > significand2)
    {
     result |= (sign1 << 31);
      if(isNormalizedLeft)
	 {
     result |= (exp2 << 23);
	 }
	 else
	 {
       result |= (exp1 << 23);
	 } 
     result |= (sigFinal & 0x07FFFFF);        
    }
    else
    {
     result |= (sign2 << 31);
      if(isNormalizedLeft)
	 {
     result |= (exp2 << 23);
	 }
	 else
	 {
       result |= (exp1 << 23);
	 } 
     result |= (sigFinal & 0x07FFFFF);
    }
  }
  else
  {
    if(significand1 > significand2)
    {
     result |= (sign1 << 31);
      if(isNormalizedLeft)
	 {
     result |= (exp2 << 23);
	 }
	 else
	 {
       result |= (exp1 << 23);
	 } 
     result |= (sigFinal & 0x07FFFFF);        
    }
    else
    {
     result |= (sign2 << 31);
      if(isNormalizedLeft)
	 {
     result |= (exp2 << 23);
	 }
	 else
	 {
       result |= (exp1 << 23);
	 } 
     result |= (sigFinal & 0x07FFFFF);
    }
  }
   
  }
  
  return result;
}

