/*
Stephen Chambers
2/24/12
This lab counts the number of opcodes in a java class file
*/
#include <stdio.h>
#include </usr/java/jdk/include/classfile_constants.h>

//For loop Variables
int i = 0;
int j = 0;
int k = 0;
int l = 0;
int loopCount = 0;
/////////////////////

//File Pointer
FILE * f1;

//Forward Declarations
void decodeConstantPool(int[], int, int);
void skipInterfaces();
void skipFields();
void findOpcodes(int[], int[]);
void initializeOpcodeNames(char *[JVM_OPC_MAX]);

int main(int argc, char * argv[])
{
  // Verify the File
  if(argc != 2)
  {
    fprintf(stderr, "Invalid number of arguments. Exiting program\n");
    return 1;
  }
  f1 = fopen(argv[1], "r");
  if(!f1)
  {
    fprintf(stderr, "File could not be opened successfully. Exiting program.");
    return 1;
  }

  int readX = 0;
  if((fgetc(f1) != 0xCA) || (fgetc(f1) != 0xFE) || (fgetc(f1) != 0xBA) || (fgetc(f1) != 0xBE))
  {
    fprintf(stderr, "File not a class file. Exiting program\n");
    return 1;
  }
  //Skip minor_version and major_version
  for(j = 0; j < 4; j++)
  {
    fgetc(f1);
  }

  readX = fgetc(f1);
  loopCount = fgetc(f1);
  loopCount |= (readX << 8);

  loopCount -= 1;
  int constArray[loopCount + 1];
  int opcodeArray[JVM_OPC_MAX] = {0};

  for(i = 1; i < loopCount + 1; i++)
  {
    constArray[i] = 0;
  }

  decodeConstantPool(constArray, readX, loopCount);
  skipInterfaces(readX);
  skipFields();
  findOpcodes(constArray, opcodeArray);
  int garbageFlag = 0;

  if(opcodeArray[0] == 0)
  {
    garbageFlag = 1;
  }

  char * opcodeNames[JVM_OPC_MAX];
  initializeOpcodeNames(opcodeNames);
  
  if(garbageFlag)
  {
    opcodeArray[0] = 0;
  }
  for(i = 0; i < JVM_OPC_MAX; i++)
  {
	if(opcodeArray[i] != 0)
	{
    printf("%s: %d\n", opcodeNames[i], opcodeArray[i]);
	}
  }

  return 0;
}

/*The following code decodes the constant pool. The amount to decode is based on the type of 
constant pool entry. The entries are stored in an array for later access.*/
void decodeConstantPool(int constArray[], int readX, int loopCount)
{
  int classBytes = 2;
  int fieldBytes = 4;
  int methodBytes = 4;
  int interfaceBytes = 4;
  int stringBytes = 2;
  int integerBytes = 4;
  int floatBytes = 4;
  int longBytes = 8;
  int doubleBytes = 8;
  int nameBytes = 4;
  int length = 0;
  int count = 1;

  for(i = 0; i < loopCount; i++)
  { 
    readX = fgetc(f1);
    //printf("readx: %02x\n", readX);
    if(readX == 0x01)
    {
      length = 0;
      readX = fgetc(f1);
      length = fgetc(f1);
	  length |= (readX << 8);

      for(j = 0; j < length; j++)
      {
        constArray[count] += fgetc(f1);
      }
     count++;
    }
    else if(readX == 0x03)
    {
      for(j = 0; j < integerBytes; j++)
      {
        constArray[count] += fgetc(f1);
      }
     count++;
    }
    else if(readX == 0x04)
    {
      for(j = 0; j < floatBytes; j++)
      {
        constArray[count] += fgetc(f1);
      }
    }
    else if(readX == 0x05)
    {
      loopCount--;
      for(j = 0; j < longBytes; j++)
      {
        constArray[count] += fgetc(f1);
      }
     count += 2;
    }
    else if(readX == 0x06)
    {
      loopCount--;
      for(j = 0; j < doubleBytes; j++)
      {
        constArray[count] += fgetc(f1);
      }
     count += 2;
    }
    else if(readX == 0x07)
    {
      for(j = 0; j < classBytes; j++)
      {
        constArray[count] += fgetc(f1);
      }
     count++;
    }
    else if(readX == 0x08)
    {
      for(j = 0; j < stringBytes; j++)
      {
        constArray[count] += fgetc(f1);
      }
     count++;
    }
    else if(readX == 0x09)
    {
      for(j = 0; j < fieldBytes; j++)
      {
        constArray[count] += fgetc(f1);
      }
     count++;
    }
    else if(readX == 0x0A)
    {
      for(j = 0; j < methodBytes; j++)
      {
        constArray[count] = fgetc(f1);
      }
     count++;
    }
    else if(readX == 0x0B)
    {
      for(j = 0; j < interfaceBytes; j++)
      {     
        constArray[count] += fgetc(f1);
      }
     count++;
    }
    else if(readX == 0x0C)
    {
      for(j = 0; j <  nameBytes; j++)
      {
        constArray[count] += fgetc(f1);
      }
     count++;
    } 
    else
    {
      printf("loop count error\n");
    }
  }

}

/* The following code skips the interface portion of the class file. The interfaces are not 
important in this program, and they are not stored.*/
void skipInterfaces()
{
  unsigned int readX = 0;
  unsigned int interfaceCount = 0;
  for(i = 0; i < 6; i++)
  {
    fgetc(f1);
  }
  readX = fgetc(f1);
  interfaceCount = fgetc(f1);
  interfaceCount |= (readX << 8);

  for(i = 0; i < (interfaceCount * 2); i++)
  {
    fgetc(f1);
  }
}
/*The following code skips the fields in the class file. Each field has attributes of varying 
lengths which must be accounted for. Neither the field or attributes are important in this 
program, so they are not stored.*/
void skipFields()
{
  unsigned int fieldCount = 0;
  unsigned int attributesCount = 0;
  unsigned int attributesLength = 0;
  unsigned int readX = 0;
  unsigned int readX2 = 0;
  unsigned int readX3 = 0;

  readX = fgetc(f1);
  fieldCount = fgetc(f1);
  fieldCount |= (readX << 8);

  for(i = 0; i < fieldCount; i++)
  {
    for(j = 0; j < 6; j++)
    {
      fgetc(f1);
    }
    readX = fgetc(f1);
	attributesCount = fgetc(f1);
	attributesCount |= (readX << 8);

    for(k = 0; k < attributesCount; k++)
    {
      readX = fgetc(f1) + fgetc(f1);

	  readX = fgetc(f1);
	  readX2 = fgetc(f1);
	  readX3 = fgetc(f1);
	  attributesLength = fgetc(f1);
	  attributesLength |= (readX3 << 8);
	  attributesLength |= (readX2 << 16);
	  attributesLength |= (readX << 24);

      for(l = 0; l < attributesLength; l++)
      {
        fgetc(f1);
      }
    }
    
  }
}

/* The following code skips up to the opcode portion of the method. They are found in a method 
attribute. The name of that attribute is an index into the constant pool, where the entry will 
be the ASCII value for "Code". The number of times each opcode appears is stored within an array
using the opcode as an index into that array.*/
void findOpcodes(int constArray[], int opcodeArray[])
{
  
  unsigned int methodCount = 0;
  unsigned int methodAttributesCount = 0;
  unsigned int name = 0;
  unsigned int codeLength = 0;
  unsigned int exceptionLength = 0;
  unsigned char opcode_length[JVM_OPC_MAX + 1] = JVM_OPCODE_LENGTH_INITIALIZER;
  unsigned int readX = 0;
  unsigned int readX2 = 0;
  unsigned int readX3 = 0;
  unsigned int attributesCount = 0;
  unsigned int attributesLength = 0;

  readX = fgetc(f1);
  methodCount = fgetc(f1);
  methodCount |= (readX << 8);
  for(i = 0; i < methodCount; i++)
  {
	for(j = 0; j < 6; j++)
	  {
	    readX = fgetc(f1);
	  }
	  readX = fgetc(f1);
	  methodAttributesCount = fgetc(f1);
	  methodAttributesCount |= (readX << 8);

	  for(k = 0; k < methodAttributesCount; k++)
	  {
	        readX = fgetc(f1);
		name = fgetc(f1);
		name |= (readX << 8);
	    if(constArray[name] == 0x17b)
	    {
	      for(j = 0; j < 8; j++)
	      {
		    readX = fgetc(f1);
	      }
	      readX = fgetc(f1);
	      readX2 = fgetc(f1);
	      readX3 = fgetc(f1);
	      codeLength = fgetc(f1);
	      codeLength |= (readX3 << 8);
     	      codeLength |= (readX2 << 16);
	      codeLength |= (readX << 24);

	      for(l = 0; l < codeLength; l++)
	      {
		readX = fgetc(f1);
		opcodeArray[readX] += 1;
                for(j = 0; j < opcode_length[readX] - 1; j++)
                {
                  fgetc(f1);
                  codeLength--;
                }
	      }   
	     readX = fgetc(f1);
	     exceptionLength = fgetc(f1);
	     exceptionLength |= (readX << 8);
	     for(l = 0; l < (exceptionLength * 8); l++)
	     {
	       readX = fgetc(f1);
	     }
             readX = fgetc(f1);
	     attributesCount = fgetc(f1);
	     attributesCount |= (readX << 8);

             for(l = 0; l < attributesCount; l++)
             {
		name = 0;
                name = fgetc(f1) + fgetc(f1);
	        readX = fgetc(f1);
		readX2 = fgetc(f1);
		readX3 = fgetc(f1);
		attributesLength = fgetc(f1);
		attributesLength |= (readX3 << 8);
		attributesLength |= (readX2 << 16);
		attributesLength |= (readX << 24);
               for(j = 0; j < attributesLength; j++)
               {
                 fgetc(f1);
               }
             }
            
	    }
	    else
	    {
	      attributesCount = 0;
	      readX = fgetc(f1);
	      readX2 = fgetc(f1);
	      readX3 = fgetc(f1);
	      attributesCount = fgetc(f1);
	      attributesCount |= (readX3 << 8);
	      attributesCount |= (readX2 << 16);
	      attributesCount |= (readX << 24);
	      for(l = 0; l < attributesCount; l++)
	      {
		readX = fgetc(f1);
	      }
	    }
	  }
  }
}

/*The following code initializes the opcodeNames array to the nmueonic opcode values.
*/
void initializeOpcodeNames(char * opcodeNames[JVM_OPC_MAX])
{
	opcodeNames[0]=  " nop ";
	opcodeNames[1]=	" aconst_null ";
	opcodeNames[2]=	" iconst_m1 ";
	opcodeNames[3]=	" iconst_0 ";
	opcodeNames[4]=	" iconst_1 ";
	opcodeNames[5]=	" iconst_2 ";
	opcodeNames[6]=	" iconst_3 ";
	opcodeNames[7]=	" iconst_4 ";
	opcodeNames[8]=	" iconst_5 ";
	opcodeNames[9]=	" lconst_0 ";
	opcodeNames[10]=" lconst_1 ";
	opcodeNames[11]=" fconst_0 ";
	opcodeNames[12]=" fconst_1 ";
	opcodeNames[13]=" fconst_2 ";
	opcodeNames[14]=" dconst_0 ";
	opcodeNames[15]=" dconst_1 ";
	opcodeNames[16]=" bipush ";
	opcodeNames[17]=" sipush ";
	opcodeNames[18]=" ldc ";
	opcodeNames[19]=" ldc_w ";
	opcodeNames[20]=" ldc2_w ";
	opcodeNames[21]=" iload ";
	opcodeNames[22]=" lload ";
	opcodeNames[23]=" fload ";
	opcodeNames[24]=" dload ";
	opcodeNames[25]=" aload ";
	opcodeNames[26]=" iload_0 ";
	opcodeNames[27]=" iload_1 ";
	opcodeNames[28]=" iload_2 ";
	opcodeNames[29]=" iload_3 ";
	opcodeNames[30]=" lload_0 ";
	opcodeNames[31]=" lload_1 ";
	opcodeNames[32]=" lload_2 ";
	opcodeNames[33]=" lload_3 ";
	opcodeNames[34]=" fload_0 ";
	opcodeNames[35]=" fload_1 ";
	opcodeNames[36]=" fload_2 ";
	opcodeNames[37]=" fload_3 ";
	opcodeNames[38]=" dload_0 ";
	opcodeNames[39]=" dload_1 ";
	opcodeNames[40]=" dload_2 ";
	opcodeNames[41]=" dload_3 ";
	opcodeNames[42]=" aload_0 ";
	opcodeNames[43]=" aload_1 ";
	opcodeNames[44]=" aload_2 ";
	opcodeNames[45]=" aload_3 ";
	opcodeNames[46]=" iaload ";
	opcodeNames[47]=" laload ";
	opcodeNames[48]=" faload ";
	opcodeNames[49]=" daload ";
	opcodeNames[50]=" aaload ";
	opcodeNames[51]=" baload ";
	opcodeNames[52]=" caload ";
	opcodeNames[53]=" saload ";
	opcodeNames[54]=" istore ";
	opcodeNames[55]=" lstore ";
	opcodeNames[56]=" fstore ";
	opcodeNames[57]=" dstore ";
	opcodeNames[58]=" astore ";
	opcodeNames[59]=" istore_0 ";
	opcodeNames[60]=" istore_1 ";
	opcodeNames[61]=" istore_2 ";
	opcodeNames[62]=" istore_3 ";
	opcodeNames[63]=" lstore_0 ";
	opcodeNames[64]=" lstore_1 ";
	opcodeNames[65]=" lstore_2 ";
	opcodeNames[66]=" lstore_3 ";
	opcodeNames[67]=" fstore_0 ";
	opcodeNames[68]=" fstore_1 ";
	opcodeNames[69]=" fstore_2 ";
	opcodeNames[70]=" fstore_3 ";
	opcodeNames[71]=" dstore_0 ";
	opcodeNames[72]=" dstore_1 ";
	opcodeNames[73]=" dstore_2 ";
	opcodeNames[74]=" dstore_3 ";
	opcodeNames[75]=" astore_0 ";
	opcodeNames[76]=" astore_1 ";
	opcodeNames[77]=" astore_2 ";
	opcodeNames[78]=" astore_3 ";
	opcodeNames[79]=" iastore ";
	opcodeNames[80]=" lastore ";
	opcodeNames[81]=" fastore ";
	opcodeNames[82]=" dastore ";
	opcodeNames[83]=" aastore ";
	opcodeNames[84]=" bastore ";
	opcodeNames[85]=" castore ";
	opcodeNames[86]=" sastore ";
	opcodeNames[87]=" pop ";
	opcodeNames[88]=" pop2 ";
	opcodeNames[89]=" dup ";
	opcodeNames[90]=" dup_x1 ";
	opcodeNames[91]=" dup_x2 ";
	opcodeNames[92]=" dup2 ";
	opcodeNames[93]=" dup2_x1 ";
	opcodeNames[94]=" dup2_x2 ";
	opcodeNames[95]=" swap ";
	opcodeNames[96]=" iadd ";
	opcodeNames[97]=" ladd ";
	opcodeNames[98]=" fadd ";
	opcodeNames[99]=" dadd ";
	opcodeNames[100]=" isub ";
	opcodeNames[101]=" lsub ";
	opcodeNames[102]=" fsub ";
	opcodeNames[103]=" dsub ";
	opcodeNames[104]=" imul ";
	opcodeNames[105]=" lmul ";
	opcodeNames[106]=" fmul ";
	opcodeNames[107]=" dmul ";
	opcodeNames[108]=" idiv ";
	opcodeNames[109]=" ldiv ";
	opcodeNames[110]=" fdiv ";
	opcodeNames[111]=" ddiv ";
	opcodeNames[112]=" irem ";
	opcodeNames[113]=" lrem ";
	opcodeNames[114]=" frem ";
	opcodeNames[115]=" drem ";
	opcodeNames[116]=" ineg ";
	opcodeNames[117]=" lneg ";
	opcodeNames[118]=" fneg ";
	opcodeNames[119]=" dneg ";
	opcodeNames[120]=" ishl ";
	opcodeNames[121]=" lshl ";
	opcodeNames[122]=" ishr ";
	opcodeNames[123]=" lshr ";
	opcodeNames[124]=" iushr ";
	opcodeNames[125]=" lushr ";
	opcodeNames[126]=" iand ";
	opcodeNames[127]=" land ";
	opcodeNames[128]=" ior ";
	opcodeNames[129]=" lor ";
	opcodeNames[130]=" ixor ";
	opcodeNames[131]=" lxor ";
	opcodeNames[132]=" iinc ";
	opcodeNames[133]=" i2l ";
	opcodeNames[134]=" i2f ";
	opcodeNames[135]=" i2d ";
	opcodeNames[136]=" l2i ";
	opcodeNames[137]=" l2f ";
	opcodeNames[138]=" l2d ";
	opcodeNames[139]=" f2i ";
	opcodeNames[140]=" f2l ";
	opcodeNames[141]=" f2d ";
	opcodeNames[142]=" d2i ";
	opcodeNames[143]=" d2l ";
	opcodeNames[144]=" d2f ";
	opcodeNames[145]=" i2b ";
	opcodeNames[146]=" i2c ";
	opcodeNames[147]=" i2s ";
	opcodeNames[148]=" lcmp ";
	opcodeNames[149]=" fcmpl ";
	opcodeNames[150]=" fcmpg ";
	opcodeNames[151]=" dcmpl ";
	opcodeNames[152]=" dcmpg ";
	opcodeNames[153]=" ifeq ";
	opcodeNames[154]=" ifne ";
	opcodeNames[155]=" iflt ";
	opcodeNames[156]=" ifge ";
	opcodeNames[157]=" ifgt ";
	opcodeNames[158]=" ifle ";
	opcodeNames[159]=" if_icmpeq ";
	opcodeNames[160]=" if_icmpne ";
	opcodeNames[161]=" if_icmplt ";
	opcodeNames[162]=" if_icmpge ";
	opcodeNames[163]=" if_icmpgt ";
	opcodeNames[164]=" if_icmple ";
	opcodeNames[165]=" if_acmpeq ";
	opcodeNames[166]=" if_acmpne ";
	opcodeNames[167]=" goto ";
	opcodeNames[168]=" jsr ";
	opcodeNames[169]=" ret ";
	opcodeNames[170]=" tableswitch ";
	opcodeNames[171]=" lookupswitch ";
	opcodeNames[172]=" ireturn ";
	opcodeNames[173]=" lreturn ";
	opcodeNames[174]=" freturn ";
	opcodeNames[175]=" dreturn ";
	opcodeNames[176]=" areturn ";
	opcodeNames[177]=" return ";
	opcodeNames[178]=" getstatic ";
	opcodeNames[179]=" putstatic ";
	opcodeNames[180]=" getfield ";
	opcodeNames[181]=" putfield ";
	opcodeNames[182]=" invokevirtual ";
	opcodeNames[183]=" invokespecial ";
	opcodeNames[184]=" invokestatic ";
	opcodeNames[185]=" invokeinterface ";
	opcodeNames[186]=" xxxunusedxxx ";
	opcodeNames[187]=" new ";
	opcodeNames[188]=" newarray ";
	opcodeNames[189]=" anewarray ";
	opcodeNames[190]=" arraylength ";
	opcodeNames[191]=" athrow ";
	opcodeNames[192]=" checkcast ";
	opcodeNames[193]=" instanceof ";
	opcodeNames[194]=" monitorenter ";
	opcodeNames[195]=" monitorexit ";
	opcodeNames[196]=" wide ";
	opcodeNames[197]=" multianewarray ";
	opcodeNames[198]=" ifnull ";
	opcodeNames[199]=" ifnonnull ";
	opcodeNames[200]=" goto_w ";
	opcodeNames[201]=" jsr_w ";
}


