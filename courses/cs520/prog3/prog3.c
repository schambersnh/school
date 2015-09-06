/*
Stephen Chambers
2/24/12
This lab counts the number of opcodes in a java class file
*/
#include <stdio.h>
#include </usr/java/jdk/include/classfile_constants.h>
#include<inttypes.h>
#include <stdlib.h>
typedef struct
  {
    long long container;
    char * type;
    int count;
    int isChecked;
  } entry;

//For loop Variables
int i = 0;
int j = 0;
int k = 0;
int l = 0;
int c = 0;
int loopCount = 0;
/////////////////////

//File Pointer
FILE * f1;

//Opcode Array Counter
int opcodeArray[JVM_OPC_MAX];

//Constant reference counter
entry constRefArray[10000000];

//local Array
int localArray[256]; //0xFF + 1

//offset for local array
int offset = 100;

//offset for bytecode
int byteOffset = 0;

//Forward Declarations
void decodeConstantPool(entry[], int);
void skipInterfaces();
void skipFields();
void findOpcodes(entry[]);
void initializeOpcodeNames(char *[JVM_OPC_MAX]);
int checkOpcodeReferences(int, unsigned char[], entry[]);
void addReference(entry);
int checkReference(entry);
int incrementReference(entry);
void printReferences();
long long toLittleEndian(long long, int);


int main(int argc, char * argv[])
{
  // Verify the File
  if(argc < 2)
  {
    fprintf(stderr, "Invalid number of arguments. Exiting program\n");
    return 1;
  }
  for(c = 1; c < argc; c++)
  {
  byteOffset = 0;
  f1 = fopen(argv[c], "r");
  if(!f1)
  {
    fprintf(stderr, "File could not be opened successfully. Exiting program.");
    return 1;
  }

  int readX = 0;
  if((fgetc(f1) != 0xCA) || (fgetc(f1) != 0xFE) || 
  (fgetc(f1) != 0xBA) || (fgetc(f1) != 0xBE))
  {
    fprintf(stderr, "File not a class file. Exiting program\n");
    return 1;
  }
  //Skip minor_version and major_version
  for(j = 0; j < 4; j++)
  {
    fgetc(f1);
  }
  byteOffset += 4;
  byteOffset += 4;
  
  readX = fgetc(f1);
  loopCount = fgetc(f1);
  loopCount |= (readX << 8);
  loopCount -= 1;
  byteOffset += 2;  

  entry constArray[loopCount + 1];

  for(i = 1; i < loopCount + 1; i++)
  {
    constArray[i].container = 0;
    constArray[i].count = 0;
  }
  
  decodeConstantPool(constArray, loopCount);
  skipInterfaces(readX);
  skipFields();
  findOpcodes(constArray);

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
  if(c == argc - 1)
  {
    printf("\n********Opcodes********\n");
    for(i = 0; i < JVM_OPC_MAX; i++)
    {
	  if(opcodeArray[i] != 0)
	  {
      	    printf("%s: %d\n", opcodeNames[i], opcodeArray[i]);
  	  }
    }
    printReferences();
    printf("\n********Locals********\n");
    for(i = 0 + offset; i < 256 + offset; i++)
    {
      if(localArray[i - offset] > 0)
      {
        printf("local slot %d was used %d times.\n", i - (2*offset),
        localArray[i - offset]++);
      }
    }
  }
  }
  return 0;
}

/*The following code decodes the constant pool. The amount 
to decode is based on the type of constant pool entry. 
The entries are stored in an array for later access.*/
void decodeConstantPool(entry constArray[], int loopCount)
{
  int length = 0;
  int count = 1;

  unsigned int readX = 0;

  for(i = 0; i < loopCount; i++)
  { 
    readX = fgetc(f1);
    byteOffset++;
    if(readX == 0x01)
    {
      length = 0;
      length = (fgetc(f1) << 8) | fgetc(f1);
      byteOffset += 2;
      for(j = 0; j < length; j++)
      {
        constArray[count].container += fgetc(f1);
        byteOffset++;
      }
     count++;
    }
    else if(readX == 0x03)
    {
        constArray[count].container |= (fgetc(f1) << 24) | 
        (fgetc(f1) << 16) | 
        (fgetc(f1) << 8) | fgetc(f1);
        byteOffset += 4;
        constArray[count].type = "Integer";
        count++;
    }
    else if(readX == 0x04)
    {
      constArray[count].container |= (fgetc(f1) << 24) |
      (fgetc(f1) << 16) | 
      (fgetc(f1) << 8) | fgetc(f1);
      byteOffset+= 4;
      constArray[count].type = "Float";
      count++;
    }
    else if(readX == 0x05)
    {
      loopCount--;
      constArray[count].container = fgetc(f1);
      constArray[count].container <<= 8;
      constArray[count].container |= fgetc(f1);
      constArray[count].container <<= 8;
      constArray[count].container |= fgetc(f1);
      constArray[count].container <<= 8;
      constArray[count].container |= fgetc(f1);
      constArray[count].container <<= 8;
      constArray[count].container |= fgetc(f1);
      constArray[count].container <<= 8;
      constArray[count].container |= fgetc(f1);
      constArray[count].container <<= 8;
      constArray[count].container |= fgetc(f1);
      constArray[count].container <<= 8;
      constArray[count].container |= fgetc(f1);

      constArray[count].type = "Long";
      count += 2;
      byteOffset += 8;
    }
    else if(readX == 0x06)
    {
      loopCount--;
      
      constArray[count].container = fgetc(f1);
      constArray[count].container <<= 8;
      constArray[count].container |= fgetc(f1);
      constArray[count].container <<= 8;
      constArray[count].container |= fgetc(f1);
      constArray[count].container <<= 8;
      constArray[count].container |= fgetc(f1);
      constArray[count].container <<= 8;
      constArray[count].container |= fgetc(f1);
      constArray[count].container <<= 8;
      constArray[count].container |= fgetc(f1);
      constArray[count].container <<= 8;
      constArray[count].container |= fgetc(f1);
      constArray[count].container <<= 8;
      constArray[count].container |= fgetc(f1);

      constArray[count].type = "Double";
      count += 2;
      byteOffset += 8;
    }
    else if(readX == 0x07)
    {
      constArray[count].container = (fgetc(f1) << 8) | 
      fgetc(f1);   
      count++;
      byteOffset += 2;
    }
    else if(readX == 0x08)
    {
      constArray[count].container = (fgetc(f1) << 8) | 
      fgetc(f1);
      count++;
      byteOffset += 2;
    }
    else if(readX == 0x09)
    {
      constArray[count].container |= (fgetc(f1) << 24) | 
      (fgetc(f1) << 16) | 
      (fgetc(f1) << 8) | fgetc(f1);
      count++;
      byteOffset += 4;
    }
    else if(readX == 0x0A)
    {
      constArray[count].container |= (fgetc(f1) << 24) | 
      (fgetc(f1) << 16) | 
      (fgetc(f1) << 8) | fgetc(f1);
      count++;
      byteOffset += 4;
    }
    else if(readX == 0x0B)
    {   
      constArray[count].container |= (fgetc(f1) << 24) | 
      (fgetc(f1) << 16) | 
      (fgetc(f1) << 8) | fgetc(f1);
      count++;
      byteOffset += 4;
    }
    else if(readX == 0x0C)
    {
      constArray[count].container |= (fgetc(f1) << 24) | 
      (fgetc(f1) << 16) | 
      (fgetc(f1) << 8) | fgetc(f1);
       count++;
      byteOffset += 4;
    } 
    else
    {
      printf("loop count error\n");
    }
  }
}

/* The following code skips the interface portion of the class file. 
The interfaces are not important in this program, 
and they are not stored.*/
void skipInterfaces()
{
  unsigned int readX = 0;
  unsigned int interfaceCount = 0;
  for(i = 0; i < 6; i++)
  {
    fgetc(f1);
    byteOffset++;
  }
  readX = fgetc(f1);
  interfaceCount = fgetc(f1);
  interfaceCount |= (readX << 8);
  byteOffset += 2;

  for(i = 0; i < (interfaceCount * 2); i++)
  {
    fgetc(f1);
    byteOffset++;
  }
}
/*The following code skips the fields in the class file. Each field has 
attributes of varying 
lengths which must be accounted for.
 Neither the field or attributes are important in this 
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
  byteOffset += 2;

  for(i = 0; i < fieldCount; i++)
  {
    for(j = 0; j < 6; j++)
    {
      fgetc(f1);
      byteOffset++;
    }
    readX = fgetc(f1);
    attributesCount = fgetc(f1);
    attributesCount |= (readX << 8);
    byteOffset += 2;

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
          byteOffset += 6;

      for(l = 0; l < attributesLength; l++)
      {
        fgetc(f1);
        byteOffset++;
      }
    }
    
  }
}

/* The following code skips up to the opcode portion of the
 method. They are found in a method attribute. The name of that attribute
 is an index into the constant pool, where the entry will 
 be the ASCII value for "Code". The number of times each opcode 
 appears is stored within an array
 using the opcode as an index into that array.*/
void findOpcodes(entry constArray[])
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
  byteOffset += 2;
  for(i = 0; i < methodCount; i++)
  {
	for(j = 0; j < 6; j++)
	  {
	    readX = fgetc(f1);
            byteOffset++;
	  }
	  readX = fgetc(f1);
	  methodAttributesCount = fgetc(f1);
	  methodAttributesCount |= (readX << 8);
          byteOffset += 2;

	  for(k = 0; k < methodAttributesCount; k++)
	  {
	        readX = fgetc(f1);
		name = fgetc(f1);
		name |= (readX << 8);
                byteOffset += 2;
	    if(constArray[name].container == 0x17b)
	    {
	      for(j = 0; j < 8; j++)
	      {
		    readX = fgetc(f1);
                    byteOffset++;
	      }
	      readX = fgetc(f1);
	      readX2 = fgetc(f1);
	      readX3 = fgetc(f1);
	      codeLength = fgetc(f1);
	      codeLength |= (readX3 << 8);
     	      codeLength |= (readX2 << 16);
	      codeLength |= (readX << 24);
              byteOffset += 4;

	      for(l = 0; l < codeLength; l++)
	      {
		readX = fgetc(f1);
                byteOffset++;
		opcodeArray[readX] += 1;
                int subtract = checkOpcodeReferences(readX, opcode_length, constArray);
                codeLength -= subtract;
                byteOffset += subtract;
	      }   
	     readX = fgetc(f1);
	     exceptionLength = fgetc(f1);
	     exceptionLength |= (readX << 8);
             byteOffset += 2;
	     for(l = 0; l < (exceptionLength * 8); l++)
	     {
	       readX = fgetc(f1);
               byteOffset++;
	     }
             readX = fgetc(f1);
	     attributesCount = fgetc(f1);
	     attributesCount |= (readX << 8);
             byteOffset+= 2;

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
                byteOffset += 6;
               for(j = 0; j < attributesLength; j++)
               {
                 fgetc(f1);
                 byteOffset++;
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
              byteOffset += 4;
	      for(l = 0; l < attributesCount; l++)
	      {
		readX = fgetc(f1);
                byteOffset++;
	      }
	    }
	  }
  }
}
/*The following code takes in an opcode. If the opcode has 
to deal with accessing constants or locals, appropriate actions
 are taken. Regardless, the length of the opcode - 1 is returned
for the findOpcodes function's use.
*/
int checkOpcodeReferences(int opcode, unsigned char opcode_length[], entry constArray[])
{ 
  unsigned int index = 0;
  if(opcode == 3)
  {
    entry temp;
    temp.container = 0;
    temp.type = "Integer";
    temp.count = 0;
    temp.isChecked = 1;
    if(!checkReference(temp))
    {
      addReference(temp);
    }
    if(!incrementReference(temp))
    {
      fprintf(stderr, "Error incrementing constant reference\n");
    }
    return opcode_length[opcode] - 1;
  }
  else if(opcode == 4)
  {
    entry temp;
    temp.container = 1;
    temp.type = "Integer";
    temp.count = 0;
    temp.isChecked = 1;
    if(!checkReference(temp))
    {
      addReference(temp);
    }
    if(!incrementReference(temp))
    {
      fprintf(stderr, "Error incrementing constant reference\n");
    }
    return opcode_length[opcode] - 1;
  }
  else if(opcode == 5)
  {
    entry temp;
    temp.container = 2;
    temp.type = "Integer";
    temp.count = 0;
    temp.isChecked = 1;
    if(!checkReference(temp))
    {
      addReference(temp);
    }
    if(!incrementReference(temp))
    {
      fprintf(stderr, "Error incrementing constant reference\n");
    }
    return opcode_length[opcode] - 1;
  }
  else if(opcode == 6)
  {
    entry temp;
    temp.container = 3;
    temp.type = "Integer";
    temp.count = 0;
    temp.isChecked = 1;
    if(!checkReference(temp))
    {
      addReference(temp);
    }
    if(!incrementReference(temp))
    {
      fprintf(stderr, "Error incrementing constant reference\n");
    }
    return opcode_length[opcode] - 1;
  }
  else if(opcode == 7)
  {
    entry temp;
    temp.container = 4;
    temp.type = "Integer";
    temp.count = 0;
    temp.isChecked = 1;
    if(!checkReference(temp))
    {
      addReference(temp);
    }
    if(!incrementReference(temp))
    {
      fprintf(stderr, "Error incrementing constant reference\n");
    }
    return opcode_length[opcode] - 1;
  }
  else if(opcode == 8)
  {
    entry temp;
    temp.container = 5;
    temp.type = "Integer";
    temp.count = 0;
    temp.isChecked = 1;
    if(!checkReference(temp))
    {
      addReference(temp);
    }
    if(!incrementReference(temp))
    {
      fprintf(stderr, "Error incrementing constant reference\n");
    }
    return opcode_length[opcode] - 1;
  }
  else if(opcode == 9)
  {
    entry temp;
    temp.container = 0;
    temp.type = "Long";
    temp.count = 0;
    temp.isChecked = 1;
    if(!checkReference(temp))
    {
      addReference(temp);
    }
    if(!incrementReference(temp))
    {
      fprintf(stderr, "Error incrementing constant reference\n");
    }
    return opcode_length[opcode] - 1;
  }
  else if(opcode == 0x0a)
  {
    entry temp;
    temp.container = 1;
    temp.type = "Long";
    temp.count = 0;
    temp.isChecked = 1;
    if(!checkReference(temp))
    {
      addReference(temp);
    }
    if(!incrementReference(temp))
    {
      fprintf(stderr, "Error incrementing constant reference\n");
    }
    return opcode_length[opcode] - 1;
  }
  else if(opcode == 0x0b)
  {
    entry temp;
    temp.container = 0;
    temp.type = "Float";
    temp.count = 0;
    temp.isChecked = 1;
    if(!checkReference(temp))
    {
      addReference(temp);
    }
    if(!incrementReference(temp))
    {
      fprintf(stderr, "Error incrementing constant reference\n");
    }
    return opcode_length[opcode] - 1;
  }
  else if(opcode == 0x0c)
  {
    entry temp;
    temp.container = 1;
    temp.type = "Float";
    temp.count = 0;
    temp.isChecked = 1;
    if(!checkReference(temp))
    {
      addReference(temp);
    }
    if(!incrementReference(temp))
    {
      fprintf(stderr, "Error incrementing constant reference\n");
    }
    return opcode_length[opcode] - 1;
  }
  else if(opcode == 0x0d)
  {
    entry temp;
    temp.container = 2;
    temp.type = "Float";
    temp.count = 0;
    temp.isChecked = 1;
    if(!checkReference(temp))
    {
      addReference(temp);
    }
    if(!incrementReference(temp))
    {
      fprintf(stderr, "Error incrementing constant reference\n");
    }
    return opcode_length[opcode] - 1;
  }
  else if(opcode == 0x0e)
  {
    entry temp;
    temp.container = 0;
    temp.type = "Double";
    temp.count = 0;
    temp.isChecked = 1;
    if(!checkReference(temp))
    {
      addReference(temp);
    }
    if(!incrementReference(temp))
    {
      fprintf(stderr, "Error incrementing constant reference\n");
    }
    return opcode_length[opcode] - 1;
  }
  else if(opcode == 0x0f)
  {
    entry temp;
    temp.container = 1;
    temp.type = "Double";
    temp.count = 0;
    temp.isChecked = 1;
    if(!checkReference(temp))
    {
      addReference(temp);
    }
    if(!incrementReference(temp))
    {
      fprintf(stderr, "Error incrementing constant reference\n");
    }
    return opcode_length[opcode] - 1;
  }
  else if(opcode == 0x10)
  {
    entry temp;
    temp.container = fgetc(f1);
    //printf("opcode: %x\nbyte after opcode: %x\n", opcode, fgetc(f1));
    temp.type = "Integer";
    temp.count = 0;
    temp.isChecked = 1;
    if(!checkReference(temp))
    {
      addReference(temp);
    }
    if(!incrementReference(temp))
    {
      fprintf(stderr, "Error incrementing constant reference\n");
    }
    return opcode_length[opcode] - 1;
  }
  else if(opcode == 0x11)
  {
    entry temp;
    temp.container = (fgetc(f1) << 8) | fgetc(f1);
    temp.type = "Float";
    temp.count = 0;
    temp.isChecked = 1;
    if(!checkReference(temp))
    {
      addReference(temp);
    }
    if(!incrementReference(temp))
    {
      fprintf(stderr, "Error incrementing constant reference\n");
    }
    return opcode_length[opcode] - 1;
  }
  else if(opcode == 0x12) //ldc
  {
    index =  fgetc(f1);
    entry temp;
    temp.container = constArray[index].container;
    temp.type = constArray[index].type;
    temp.count = constArray[index].count;
    temp.isChecked = 1;
    if(!checkReference(temp))
    {
      addReference(temp);
    }
    if(!incrementReference(temp))
    {
      fprintf(stderr, "Error incrementing constant reference\n");
    }
    return opcode_length[opcode] - 1;
  }
  else if(opcode == 0x13)//ldc_w
  {
    index = (fgetc(f1) << 8) | fgetc(f1);
    entry temp;
    temp.container = constArray[index].container;
    temp.type = constArray[index].type;
    temp.count = constArray[index].count;
    temp.isChecked = 1;
    if(!checkReference(temp))
    {
      addReference(temp);
    }
    if(!incrementReference(temp))
    {
      fprintf(stderr, "Error incrementing constant reference\n");
    }
    return opcode_length[opcode] - 1;
  }
  else if(opcode == 0x14) //ldc2_w
  {
    index = (fgetc(f1) << 8) | fgetc(f1);
    entry temp;
    temp.container = constArray[index].container;
    temp.type = constArray[index].type;
    temp.count = constArray[index].count;
    temp.isChecked = 1;
    if(!checkReference(temp))
    {
      addReference(temp);
    }
    if(!incrementReference(temp))
    {
      fprintf(stderr, "Error incrementing constant reference\n");
    }
    return opcode_length[opcode] - 1;
  }
  else if(opcode == 0x15) //iload
  {
    signed int i = 0;
    i = fgetc(f1);
    localArray[i + offset]++;
    return opcode_length[opcode] - 1;
  }
  else if(opcode == 0x16) //lload
  {
    signed int i = 0;
    i = fgetc(f1);
    localArray[i + offset]++;
    localArray[i + offset + 1]++;
    return opcode_length[opcode] - 1;
  }
  else if(opcode == 0x17) //fload
  {
    signed int i = 0;
    i = fgetc(f1);
    localArray[i + offset]++;
    return opcode_length[opcode] - 1;
  }
  else if(opcode == 0x18) //dload
  {
    signed int i = 0;
    i = fgetc(f1);
    localArray[i + offset]++;
    localArray[i + offset + 1]++;
    return opcode_length[opcode] - 1;
  }
  else if(opcode == 0x19) //aload
  {
    signed int i = 0;
    i = fgetc(f1);
    localArray[i + offset]++;
    return opcode_length[opcode] - 1;
  }
  else if(opcode == 0x1a) //iload_0
  {
    localArray[0 + offset]++;
    return opcode_length[opcode] - 1;
  }
  else if(opcode == 0x1b) //iload_1
  {
    localArray[1 + offset]++;
    return opcode_length[opcode] - 1;
  }
  else if(opcode == 0x1c) //iload_2
  {
    printf("opcode: %02x\n", opcode);
    localArray[2 + offset]++;
    return opcode_length[opcode] - 1;
  }
  else if(opcode == 0x1d) //iload_3
  {
    localArray[3 + offset]++;
    return opcode_length[opcode] - 1;
  }
  else if(opcode == 0x1e) //lload_0
  {
    localArray[0 + offset]++;
    localArray[1 + offset]++;
    return opcode_length[opcode] - 1;
  }
  else if(opcode == 0x1f) //lload_1
  {
    localArray[1 + offset]++;
    localArray[2 + offset]++;
    return opcode_length[opcode] - 1;
  }
  else if(opcode == 0x20) //lload_2
  {
    localArray[2 + offset]++;
    localArray[3 + offset]++;
    return opcode_length[opcode] - 1;
  }
  else if(opcode == 0x21) //lload_3
  {
    localArray[3 + offset]++;
    localArray[4 + offset]++;
    return opcode_length[opcode] - 1;
  }
  else if(opcode == 0x22) //fload_0
  {
    localArray[0 + offset]++;
    return opcode_length[opcode] - 1;
  }
  else if(opcode == 0x23) //fload_1
  {
    localArray[1 + offset]++;
    return opcode_length[opcode] - 1;
  }
  else if(opcode == 0x24) //fload_2
  {
    localArray[2 + offset]++;
    return opcode_length[opcode] - 1;
  }
  else if(opcode == 0x25) //fload_3
  {
    localArray[3 + offset]++;
    return opcode_length[opcode] - 1;
  }
  else if(opcode == 0x26) //dload_0
  {
    localArray[0 + offset]++;
    localArray[1 + offset]++;
    return opcode_length[opcode] - 1;
  }
  else if(opcode == 0x27) //dload_1
  {
    localArray[1 + offset]++;
    localArray[2 + offset]++;
    return opcode_length[opcode] - 1;
  }
  else if(opcode == 0x28) //dload_2
  {
    localArray[2 + offset]++;
    localArray[3 + offset]++;
    return opcode_length[opcode] - 1;
  }
  else if(opcode == 0x29) //dload_3
  {
    localArray[3 + offset]++;
    localArray[4 + offset]++;
    return opcode_length[opcode] - 1;
  }
  else if(opcode == 0x2a) //aload_0
  {
    localArray[0 + offset]++;
    return opcode_length[opcode] - 1;
  }
  else if(opcode == 0x2b) //aload_1
  {
    localArray[1 + offset]++;
    return opcode_length[opcode] - 1;
  }
  else if(opcode == 0x2c) //aload_2
  {
    localArray[2 + offset]++;
    return opcode_length[opcode] - 1;
  }
  else if(opcode == 0x2d) //aload_3
  {
    localArray[3 + offset]++;
    return opcode_length[opcode] - 1;
  }
 else if(opcode == 0x36) //istore
  {
    signed int i = 0;
    i = fgetc(f1);
    localArray[i + offset]++;
    return opcode_length[opcode] - 1;
  }
  else if(opcode == 0x37) //lstore
  {
    signed int i = 0;
    i = fgetc(f1);
    localArray[i + offset]++;
    localArray[i + offset + 1]++;
    return opcode_length[opcode] - 1;
  }
  else if(opcode == 0x38) //fstore
  {
    signed int i = 0;
    i = fgetc(f1);
    localArray[i + offset]++;
    return opcode_length[opcode] - 1;
  }
  else if(opcode == 0x39) //dstore
  {
    signed int i = 0;
    i = fgetc(f1);
    localArray[i + offset]++;
    localArray[i + offset + 1]++;
    return opcode_length[opcode] - 1;
  }
  else if(opcode == 0x3a) //astore
  {
    signed int i = 0;
    i = fgetc(f1);
    localArray[i + offset]++;
    return opcode_length[opcode] - 1;
  }
  else if(opcode == 0x3b) //istore_0
  {
    localArray[0 + offset]++;
    return opcode_length[opcode] - 1;
  }
  else if(opcode == 0x3c) //istore_1
  {
    localArray[1 + offset]++;
    return opcode_length[opcode] - 1;
  }
  else if(opcode == 0x3d) //istore_2
  {
    localArray[2 + offset]++;
    return opcode_length[opcode] - 1;
  }
  else if(opcode == 0x3e) //istore_3
  {
    localArray[3 + offset]++;
    return opcode_length[opcode] - 1;
  }
  else if(opcode == 0x3f) //lstore_0
  {
    localArray[0 + offset]++;
    localArray[1 + offset]++;
    return opcode_length[opcode] - 1;
  }
  else if(opcode == 0x40) //lstore_1
  {
    localArray[1 + offset]++;
    localArray[2 + offset]++;
    return opcode_length[opcode] - 1;
  }
  else if(opcode == 0x41) //lstore_2
  {
    localArray[2 + offset]++;
    localArray[3 + offset]++;
    return opcode_length[opcode] - 1;
  }
  else if(opcode == 0x42) //lstore_3
  {
    localArray[3 + offset]++;
    localArray[4 + offset]++;
    return opcode_length[opcode] - 1;
  }
  else if(opcode == 0x43) //fstore_0
  {
    localArray[0 + offset]++;
    return opcode_length[opcode] - 1;
  }
  else if(opcode == 0x44) //fstore_1
  {
    localArray[1 + offset]++;
    return opcode_length[opcode] - 1;
  }
  else if(opcode == 0x45) //fstore_2
  {
    localArray[2 + offset]++;
    return opcode_length[opcode] - 1;
  }
  else if(opcode == 0x46) //fstore_3
  {
    localArray[3 + offset]++;
    return opcode_length[opcode] - 1;
  }
  else if(opcode == 0x47) //dstore_0
  {
    localArray[0 + offset]++;
    localArray[1 + offset]++;
    return opcode_length[opcode] - 1;
  }
  else if(opcode == 0x48) //dstore_1
  {
    localArray[1 + offset]++;
    localArray[2 + offset]++;
    return opcode_length[opcode] - 1;
  }
  else if(opcode == 0x49) //dstore_2
  {
    localArray[2 + offset]++;
    localArray[3 + offset]++;
    return opcode_length[opcode] - 1;
  }
  else if(opcode == 0x4a) //dstore_3
  {
    localArray[3 + offset]++;
    localArray[4 + offset]++;
    return opcode_length[opcode] - 1;
  }
  else if(opcode == 0x4b) //astore_0
  {
    localArray[0 + offset]++;
    return opcode_length[opcode] - 1;
  }
  else if(opcode == 0x4c) //astore_1
  {
    localArray[1 + offset]++;
    return opcode_length[opcode] - 1;
  }
  else if(opcode == 0x4d) //astore_2
  {
    localArray[2 + offset]++;
    return opcode_length[opcode] - 1;
  }
  else if(opcode == 0x4e) //astore_3
  {
    localArray[3 + offset]++;
    return opcode_length[opcode] - 1;
  }
  else if(opcode == 0xaa) //TABLE SWITCH SPECIAL CASE
  {  
    int numBytes = 0;
    //pad bytes
    while(byteOffset % 4 != 0)
    {
      fgetc(f1);
      byteOffset++;
      numBytes++;
    }
    for(j = 0; j < 4; j++)
    {
      fgetc(f1);
      byteOffset++;
      numBytes++;
    }
    signed int low = (fgetc(f1) << 24) | (fgetc(f1) << 16) | 
    (fgetc(f1) << 8) << fgetc(f1);
    signed int high = (fgetc(f1) << 24) | (fgetc(f1) << 16) | 
    (fgetc(f1) << 8) << fgetc(f1);
    byteOffset += 8;
    numBytes += 8;
    for(j = 0; j < (((high - low) + 1) * 4); j++)
    {
      fgetc(f1);
      byteOffset++;
      numBytes++;
    }
    return numBytes;
  }
  else if(opcode == 0xab) //LOOKUP SWITCH SPECIAL CASE
  {
    int numBytes = 0;
    //pad bytes
    while((byteOffset % 4) != 0)
     {
       fgetc(f1);
       byteOffset++;
       numBytes++;
     }
     for(j = 0; j < 4; j++)
     {
       fgetc(f1);
       byteOffset++;
       numBytes++;
     }
 
    signed int n = (fgetc(f1) << 24) | (fgetc(f1) << 16) | 
    (fgetc(f1) << 8) << fgetc(f1);
    byteOffset += 4;
    numBytes += 4;
    for(j = 0; j < (n * 8); j++)
    {
      fgetc(f1);
      byteOffset++;
      numBytes++;
    }
    return numBytes;
  }
  else
  {
    for(j = 0; j < opcode_length[opcode] - 1; j++)
    {
      fgetc(f1);
    }
    return opcode_length[opcode] - 1;
  } 
}
/*This method adds an entry to the constant reference array*/
void addReference(entry a)
{
  int i;
  while(constRefArray[i].isChecked != 0)
  {
    i++;
  }
  constRefArray[i].container = a.container;
  constRefArray[i].type = a.type;
  constRefArray[i].count = a.count;
  constRefArray[i].isChecked = 1;
}
//This method checks to see if an entry is within the constant reference array
int checkReference(entry a)
{
   int flag = 0;
   int i = 0;
  while(constRefArray[i].isChecked > 0)
  {
    if(constRefArray[i].container == a.container && 
      constRefArray[i].type == a.type && 
      constRefArray[i].isChecked == a.isChecked)
    {
      flag = 1;
      return flag;
    }
    i++;
  }
 return flag; 
}
/*This method checks to see if an entry is within the constant
 reference array. If it is, its count is incremented. If it is not, 
an error is printed to the screen.*/
int incrementReference(entry a)
{
  int flag = 0;
  int i = 0; 
 
  while(constRefArray[i].isChecked > 0)
  {
    if(constRefArray[i].container == a.container && 
      constRefArray[i].type == a.type && 
     constRefArray[i].isChecked == a.isChecked)
    {
      constRefArray[i].count += 1;
      flag = 1;
      return flag;
    }
    else
    {
      
    }
    i++;
  }
return flag;
}
/*This code takes the constant reference array and sorts it according 
to type. Then all arrays are sorted in ascending order and printed out to 
the screen with the number of times they were
referenced by opcodes*/
void printReferences(int finalFlag)
{
	entry intArray[1000] = {};
	entry doubleArray[1000] = {};
	entry floatArray[1000] = {};
	entry longArray[1000] = {};

    i = 0;
    int i2 = 0;
    //printf("\n********Integer Constants********\n");
    while(constRefArray[i].isChecked == 1)
    {
      if(constRefArray[i].type == (char*)"Integer")
      {
        intArray[i2].container = constRefArray[i].container;
        intArray[i2].type = constRefArray[i].type;
        intArray[i2].count = constRefArray[i].count;
        intArray[i2].isChecked = constRefArray[i].isChecked;
        i2++;
      }
      i++;
    } 
    i = 0; 
    i2 = 0; 
    //printf("\n********Long Constants********\n");
     while(constRefArray[i].isChecked == 1)
    {
      if(constRefArray[i].type == (char*)"Long")
      {
        longArray[i2].container = constRefArray[i].container;
        longArray[i2].type = constRefArray[i].type;
        longArray[i2].count = constRefArray[i].count;
        longArray[i2].isChecked = constRefArray[i].isChecked;
        i2++;
      }
      i++;
    }
     i = 0;
     i2 = 0;
     //printf("\n********Float Constants********\n");
     while(constRefArray[i].isChecked == 1)
    {
      if(constRefArray[i].type == (char*)"Float")
      {
        floatArray[i2].container = constRefArray[i].container;
        floatArray[i2].type = constRefArray[i].type;
        floatArray[i2].count = constRefArray[i].count;
        floatArray[i2].isChecked = constRefArray[i].isChecked;
        i2++;
      }
      i++;
    }
     i = 0;
     i2 = 0;
     //printf("\n********Double Constants********\n");
     while(constRefArray[i].isChecked == 1)
    {
      if(constRefArray[i].type == (char*)"Double")
      {
        doubleArray[i2].container = constRefArray[i].container;
        doubleArray[i2].type = constRefArray[i].type;
        doubleArray[i2].count = constRefArray[i].count;
        doubleArray[i2].isChecked = constRefArray[i].isChecked;
        i2++;
      }
      i++;
    }
    //SORT ARRAYS
    //Arrays are sorted using the bubble sort method taken from CS515 notes.
   int length = 0;
   j = 0;
   int flag = 1;    
   entry temp;
   i = 1;
   while(intArray[length].isChecked == 1)
   {
     length++;
   }          
   for(i = 1; i <= length && flag; i++)
   {
     flag = 0;
     for (j=0; j < length; j++)
     {
       if (intArray[j + 1].container < intArray[j].container)      
       { 
         temp = intArray[j];            
         intArray[j].container = intArray[j+1].container;
         intArray[j].type = intArray[j+1].type;
         intArray[j].count = intArray[j+1].count;
         intArray[j].isChecked = intArray[j+1].isChecked;

         intArray[j+1].container = temp.container;
         intArray[j+1].type = temp.type;
         intArray[j+1].count = temp.count;
         intArray[j+1].isChecked = temp.isChecked;

         flag = 1;               
       }
     }
   }

   
   length = 0;
   i = 1;
   while(doubleArray[length].isChecked == 1)
   {
     length++;
   }
   j = 1; 
   flag = 1;               
   for(i = 1; i <= length && flag; i++)
   {
     flag = 0;
     for (j=0; j < length; j++)
     {
       if (doubleArray[j + 1].container < doubleArray[j].container)      
       { 
         temp = doubleArray[j];            
         doubleArray[j].container = doubleArray[j+1].container;
         doubleArray[j].type = doubleArray[j+1].type;
         doubleArray[j].count = doubleArray[j+1].count;
         doubleArray[j].isChecked = doubleArray[j+1].isChecked;

         doubleArray[j+1].container = temp.container;
         doubleArray[j+1].type = temp.type;
         doubleArray[j+1].count = temp.count;
         doubleArray[j+1].isChecked = temp.isChecked;

         flag = 1;                            
       }
     }
   }  
   length = 0;
   i = 1;
   while(floatArray[length].isChecked == 1)
   {
     length++;
   }
   j = 1;
   flag = 1;              
   for(i = 1; i <= length && flag; i++)
   {
     flag = 0;
     for (j=0; j < length; j++)
     {
       if (floatArray[j + 1].container < floatArray[j].container)      
       { 
         temp = floatArray[j];            
         floatArray[j].container = floatArray[j+1].container;
         floatArray[j].type = floatArray[j+1].type;
         floatArray[j].count = floatArray[j+1].count;
         floatArray[j].isChecked = floatArray[j+1].isChecked;

         floatArray[j+1].container = temp.container;
         floatArray[j+1].type = temp.type;
         floatArray[j+1].count = temp.count;
         floatArray[j+1].isChecked = temp.isChecked;

         flag = 1;               
       }
     }
   }
   length = 0;
   i = 1;
   while(longArray[length].isChecked == 1)
   {
     length++;
   }   
   j = 1; 
   flag = 1;
   for(i = 1; i <= length && flag; i++)
   {
     flag = 0;
     for (j=0; j < length; j++)
     {
       if (longArray[j + 1].container < longArray[j].container)      
       { 
         temp = longArray[j];            
         longArray[j].container = longArray[j+1].container;
         longArray[j].type = longArray[j+1].type;
         longArray[j].count = longArray[j+1].count;
         longArray[j].isChecked = longArray[j+1].isChecked;

         longArray[j+1].container = temp.container;
         longArray[j+1].type = temp.type;
         longArray[j+1].count = temp.count;
         longArray[j+1].isChecked = temp.isChecked;

         flag = 1;             
       }
     }
   }
    printf("\n********Integer Constants********\n");
    for(i = 0; i < 1000; i++)
    {   
        if(intArray[i].isChecked == 1)
        {
          //printf("%llx(%lld in decimal) was used %d times\n", toLittleEndian(intArray[i].container, 0), 
          //intArray[i].container, intArray[i].count);
          printf("%lld: %d\n", intArray[i].container, intArray[i].count);
        }
    } 
    printf("\n********Long Constants********\n");
     for(i = 0; i < 1000; i++)
     {   
        if(longArray[i].isChecked == 1)
        {
          //printf("%llx(%lld in decimal) was used %d times\n", toLittleEndian(longArray[i].container, 1), 
          //longArray[i].container, 1, longArray[i].count);
          printf("%lld: %d\n", longArray[i].container, longArray[i].count);
        }
     }
     printf("\n********Float Constants********\n");
     for(i = 0; i < 1000; i++)
     {   
        if(floatArray[i].isChecked == 1)
        {
          //printf("%llx(%e in decimal) was used %d times\n", toLittleEndian(floatArray[i].container, 0), 
	  //floatArray[i].container, floatArray[i].count);
          printf("%e: %d\n", *(float*)&floatArray[i].container, floatArray[i].count);
        }
     }
     printf("\n********Double Constants********\n");
    for(i = 0; i < 1000; i++)
    {   
        if(doubleArray[i].isChecked == 1)
        {
          //Must check if the 32 bit container is equal to the 64 bit contaienr.
          if((doubleArray[i].container & 0x0000FFFF) == doubleArray[i].container)
          {
            //printf("%llx(%e in decimal) was used %d times\n", toLittleEndian(doubleArray[i].container, 1), 
             //(double)doubleArray[i].container, doubleArray[i].count);
             printf("%e: %d\n", (double)doubleArray[i].container, doubleArray[i].count);
          }
          else
          {
             //printf("%llx(%e in decimal) was used %d times\n", toLittleEndian(doubleArray[i].container, 1), 
             //doubleArray[i].container, doubleArray[i].count);
             printf("%e: %d\n", *(double*)&doubleArray[i].container, doubleArray[i].count);
          }
        }
    }
    
}
long long toLittleEndian(long long value, int flag)
{
  long long littleEndian = 0;
  if(flag)
  {
    littleEndian =  ((0x00000000000000FF) & (value >> 56)) | 
     ((0x000000000000FF00) & (value >> 40)) | 
    ((0x0000000000FF0000) & (value >> 24)) | 
     ((0x00000000FF000000) & (value >> 8)) | 
    ((0x000000FF00000000) & (value << 8)) |
     ((0x0000FF0000000000) & (value << 24)) | 
    ((0x00FF000000000000) & (value << 40)) | 
     ((0xFF00000000000000) & (value << 56));
    return littleEndian;
  }
  else
  {
    littleEndian = ((0x000000000000FF) & (value >> 24)) | 
     ((0x0000000000FF00) & (value >> 8)) | 
    ((0x0000000000FF0000) & (value << 8)) | 
     ((0x00000000FF000000) & (value << 24));
    return littleEndian;
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
