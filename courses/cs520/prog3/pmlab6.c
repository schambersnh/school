/*
Stephen Chambers
2/24/12
This lab counts the number of opcodes in a java class file
*/
#include <stdio.h>
#include </usr/java/jdk/include/classfile_constants.h>

/*The following creates an array of opcode names indexed by opcode numerical values
*/

        char * opcodeNames[] = {
		        " nop ",
			" aconst_null ",
			" iconst_m1 ",
			" iconst_0 ",
			" iconst_1 ",
			" iconst_2 ",
			" iconst_3 ",
			" iconst_4 ",
			" iconst_5 ",
			" lconst_0 ",
			" lconst_1 ",
			" fconst_0 ",
			" fconst_1 ",
			" fconst_2 ",
			" dconst_0 ",
			" dconst_1 ",
			" bipush ",
			" sipush ",
			" ldc ",
			" ldc_w ",
			" ldc2_w ",
			" iload ",
			" lload ",
			" fload ",
			" dload ",
			" aload ",
			" iload_0 ",
			" iload_1 ",
			" iload_2 ",
			" iload_3 ",
			" lload_0 ",
			" lload_1 ",
			" lload_2 ",
			" lload_3 ",
			" fload_0 ",
			" fload_1 ",
			" fload_2 ",
			" fload_3 ",
			" dload_0 ",
			" dload_1 ",
			" dload_2 ",
			" dload_3 ",
			" aload_0 ",
			" aload_1 ",
			" aload_2 ",
			" aload_3 ",
			" iaload ",
			" laload ",
			" faload ",
			" daload ",
			" aaload ",
			" baload ",
			" caload ",
			" saload ",
			" istore ",
			" lstore ",
			" fstore ",
			" dstore ",
			" astore ",
			" istore_0 ",
			" istore_1 ",
			" istore_2 ",
			" istore_3 ",
			" lstore_0 ",
			" lstore_1 ",
			" lstore_2 ",
			" lstore_3 ",
			" fstore_0 ",
			" fstore_1 ",
			" fstore_2 ",
			" fstore_3 ",
			" dstore_0 ",
			" dstore_1 ",
			" dstore_2 ",
			" dstore_3 ",
			" astore_0 ",
			" astore_1 ",
			" astore_2 ",
			" astore_3 ",
			" iastore ",
			" lastore ",
			" fastore ",
			" dastore ",
			" aastore ",
			" bastore ",
			" castore ",
			" sastore ",
			" pop ",
			" pop2 ",
			" dup ",
			" dup_x1 ",
			" dup_x2 ",
			" dup2 ",
			" dup2_x1 ",
			" dup2_x2 ",
			" swap ",
			" iadd ",
			" ladd ",
			" fadd ",
			" dadd ",
			" isub ",
			" lsub ",
			" fsub ",
			" dsub ",
			" imul ",
			" lmul ",
			" fmul ",
			" dmul ",
			" idiv ",
			" ldiv ",
			" fdiv ",
			" ddiv ",
			" irem ",
			" lrem ",
			" frem ",
			" drem ",
			" ineg ",
			" lneg ",
			" fneg ",
			" dneg ",
			" ishl ",
			" lshl ",
			" ishr ",
			" lshr ",
			" iushr ",
			" lushr ",
			" iand ",
			" land ",
			" ior ",
			" lor ",
			" ixor ",
			" lxor ",
			" iinc ",
			" i2l ",
			" i2f ",
			" i2d ",
			" l2i ",
			" l2f ",
			" l2d ",
			" f2i ",
			" f2l ",
			" f2d ",
			" d2i ",
			" d2l ",
			" d2f ",
			" i2b ",
			" i2c ",
			" i2s ",
			" lcmp ",
			" fcmpl ",
			" fcmpg ",
			" dcmpl ",
			" dcmpg ",
			" ifeq ",
			" ifne ",
			" iflt ",
			" ifge ",
			" ifgt ",
			" ifle ",
			" if_icmpeq ",
			" if_icmpne ",
			" if_icmplt ",
			" if_icmpge ",
			" if_icmpgt ",
			" if_icmple ",
			" if_acmpeq ",
			" if_acmpne ",
			" goto ",
			" jsr ",
			" ret ",
			" tableswitch ",
			" lookupswitch ",
			" ireturn ",
			" lreturn ",
			" freturn ",
			" dreturn ",
			" areturn ",
			" return ",
			" getstatic ",
			" putstatic ",
			" getfield ",
			" putfield ",
			" invokevirtual ",
			" invokespecial ",
			" invokestatic ",
			" invokeinterface ",
			" xxxunusedxxx ",
			" new ",
			" newarray ",
			" anewarray ",
			" arraylength ",
			" athrow ",
			" checkcast ",
			" instanceof ",
			" monitorenter ",
			" monitorexit ",
			" wide ",
			" multianewarray ",
			" ifnull ",
			" ifnonnull ",
			" goto_w ",
			" jsr_w "};

//For loop Variables
int i = 0;
int j = 0;
int k = 0;
int l = 0;
int loopCount = 0;
/////////////////////

//Variables used for counting constant pool entries
int count = 1;
///////////////////////////////////////////////////

//Varibles used for storing how long each constant pool entry is in terms of bytes
//NOTE: utf-8 special case
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
//////////////////////////////////////////////////
int * createOpcodeArray();
int main(int argc, char * argv[])
{
  if(argc != 2)
  {
    fprintf(stderr, "Invalid number of arguments. Exiting program\n");
    return 1;
  }
  FILE * f1 = fopen(argv[1], "r");
  if(!f1)
  {
    fprintf(stderr, "File could not be opened successfully. Exiting program.");
    return 1;
  }
  //Verify that the file looked at is a class file
  int x1 = 0;
  int x2 = 0; 
  int x3 = 0; 
  int x4 = 0;
  int readX = 0;
  x1 = fgetc(f1);
  x2 = fgetc(f1);
  x3 = fgetc(f1);
  x4 = fgetc(f1);

  if((x1 != 0xCA) || (x2 != 0xFE) || (x3 != 0xBA) || (x4 != 0xBE))
  {
    fprintf(stderr, "File not a class file. Exiting program\n");
    return 1;
  }
  //Skip minor_version and major_version
  for(j = 0; j < 4; j++)
  {
    readX = fgetc(f1);
  }
  readX = fgetc(f1);
  loopCount = readX + fgetc(f1);
  //loop count stored as 1 more then actually there
  loopCount -= 1;
  int constArray[loopCount + 1];
  //initialize array elements
  for(i = 1; i < loopCount + 1; i++)
  {
    constArray[i] = 0;
  }
  for(i = 0; i < loopCount; i++)
  { 
    readX = fgetc(f1);
    if(readX == 0x01)
    {
      
      length = 0;
      readX = fgetc(f1);
      length = readX + fgetc(f1);
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
      /*The constant pool entries have been processed. They are stored in an array which
    starts at an index of 1. The following code skips class information from the end of 
    the constant pool to the methods_count portion of the class file.
    */

  for(i = 0; i < 6; i++)
  {
    readX = fgetc(f1);
  }
  unsigned int interfaceCount = fgetc(f1) + fgetc(f1);
  
  for(i = 0; i < (interfaceCount * 2); i++)
  {
    readX = fgetc(f1);
  }
  unsigned int fieldCount = fgetc(f1) + fgetc(f1);
  unsigned int attributesCount = 0;
  unsigned int attributesLength = 0;
  int opcodeArray[JVM_OPC_MAX] = {0};
  
  for(i = 0; i < fieldCount; i++)
  {
    for(j = 0; j < 6; j++)
    {
      readX = fgetc(f1);
    }
    attributesCount = fgetc(f1) + fgetc(f1);
    for(k = 0; k < attributesCount; k++)
    {
      readX = fgetc(f1) + fgetc(f1);
      attributesLength = fgetc(f1) + fgetc(f1) + fgetc(f1) + fgetc(f1);
      for(l = 0; l < attributesLength; l++)
      {
        readX = fgetc(f1);
      }
    }
    
  }
  // The following code skips up to the opcodes and counts the number of times each opcode appears
  unsigned int methodCount = fgetc(f1) + fgetc(f1);
  unsigned int methodAttributesCount = 0;
  unsigned int name = 0;
  unsigned int codeLength = 0;
  unsigned int exceptionLength = 0;
  unsigned char opcode_length[JVM_OPC_MAX + 1] = JVM_OPCODE_LENGTH_INITIALIZER;
  for(i = 0; i < methodCount; i++)
  {
	for(j = 0; j < 6; j++)
	  {
	    readX = fgetc(f1);
	  }
	  methodAttributesCount = fgetc(f1) + fgetc(f1);
	  for(k = 0; k < methodAttributesCount; k++)
	  {
	    name = fgetc(f1) + fgetc(f1);
	    if(constArray[name] == 0x17b)
	    {
	      for(j = 0; j < 8; j++)
	      {
		readX = fgetc(f1);
	      }
	      codeLength = fgetc(f1) + fgetc(f1) + fgetc(f1) + fgetc(f1);
	      for(k = 0; k < codeLength; k++)
	      {
		readX = fgetc(f1);
		opcodeArray[readX] += 1;
                for(j = 0; j < opcode_length[readX] - 1; j++)
                {
                  fgetc(f1);
                  codeLength--;
                }
	      }   
	     exceptionLength = fgetc(f1) + fgetc(f1);
	     for(l = 0; l < (exceptionLength * 8); l++)
	     {
	       readX = fgetc(f1);
	     }
             attributesCount = fgetc(f1) + fgetc(f1);
             for(l = 0; l < attributesCount; l++)
             {
               name = fgetc(f1) + fgetc(f1);
               attributesLength = fgetc(f1) + fgetc(f1) + fgetc(f1) + fgetc(f1);
               for(j = 0; j < attributesLength; j++)
               {
                 fgetc(f1);
               }
             }
            
	    }
	    else
	    {
	      printf("not in a code attribute\n");
	      attributesCount = fgetc(f1) + fgetc(f1) + fgetc(f1) + fgetc(f1);
	      for(l = 0; l < attributesCount; l++)
	      {
		readX = fgetc(f1);
	      }
	    }
	  }
  }
  for(i = 0; i < JVM_OPC_MAX; i++)
  {
    //printf("%s: %d\n", opcodeNames[i], opcodeArray[i]);
	  printf("%02x\n", i);
  }

  return 0;
}


