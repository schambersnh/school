/*
Stephen Chambers
2/24/12
This lab counts and categorizes the different types of constants in the constant pool
*/
#include <stdio.h>

/*The following are the different types of constant pool entries and their corresponding tags
CONSTANT_Class   		7
CONSTANT_Fieldref 		9
CONSTANT_Methodref 		10
CONSTANT_InterfaceMethodref 	11
CONSTANT_String 		8
CONSTANT_Integer 		3
CONSTANT_Float 			4
CONSTANT_Long 			5
CONSTANT_Double 		6
CONSTANT_NameAndType 		12
CONSTANT_Utf8 			1
*/
//For loop Variables
int i = 0;
int j = 0;
int loopCount = 0;
/////////////////////

//Variables used for counting constant pool entries
int classCount = 0;
int fieldCount = 0;
int methodCount = 0;
int interfaceCount = 0;
int stringCount = 0;
int integerCount = 0;
int floatCount = 0;
int longCount = 0;
int doubleCount = 0;
int nameCount = 0;
int utfCount = 0;
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
  int x1, x2, x3, x4, readX;
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
  for(i = 0; i < loopCount; i++)
  { 
    readX = fgetc(f1);
    if(readX == 0x01)
    {
      utfCount++;
      length = 0;
      readX = fgetc(f1);
      length = readX + fgetc(f1);
      for(j = 0; j < length; j++)
      {
        readX = fgetc(f1);
      }
    }
    else if(readX == 0x03)
    {
      integerCount++;
      for(j = 0; j < integerBytes; j++)
      {
        readX = fgetc(f1);
      }
    }
    else if(readX == 0x04)
    {
      floatCount++;
      for(j = 0; j < floatBytes; j++)
      {
        readX = fgetc(f1);
      }
    }
    else if(readX == 0x05)
    {
      longCount++;
      for(j = 0; j < longBytes; j++)
      {
        readX = fgetc(f1);
      }
    }
    else if(readX == 0x06)
    {
      doubleCount++;
      for(j = 0; j < doubleBytes; j++)
      {
        readX = fgetc(f1);
      }
    }
    else if(readX == 0x07)
    {
      classCount++;
      for(j = 0; j < classBytes; j++)
      {
        readX = fgetc(f1);
      }
    }
    else if(readX == 0x08)
    {
      stringCount++;
      for(j = 0; j < stringBytes; j++)
      {
        readX = fgetc(f1);
      }
    }
    else if(readX == 0x09)
    {
      fieldCount++;
      for(j = 0; j < fieldBytes; j++)
      {
        readX = fgetc(f1);
      }
    }
    else if(readX == 0x0A)
    {
      methodCount++;
      for(j = 0; j < methodBytes; j++)
      {
        readX = fgetc(f1);
      }
    }
    else if(readX == 0x0B)
    {
      interfaceCount++;
      for(j = 0; j < interfaceBytes; j++)
      {       
        readX = fgetc(f1);
      }
    }
    else if(readX == 0x0C)
    {
      nameCount++;
      for(j = 0; j <  nameBytes; j++)
      {
        readX = fgetc(f1);
      }
    }

  }
  printf("CONSTANT_Class: %d\n", classCount);
  printf("CONSTANT_Methodref: %d\n", methodCount);
  printf("CONSTANT_InterfaceMethodref: %d\n", interfaceCount);
  printf("CONSTANT_String: %d\n", stringCount);
  printf("CONSTANT_Integer: %d\n", integerCount);
  printf("CONSTANT_Float: %d\n", floatCount);
  printf("CONSTANT_Long: %d\n", longCount);
  printf("CONSTANT_Double: %d\n", doubleCount);
  printf("CONSTANT_NameAndType: %d\n", nameCount);
  printf("CONSTANT_Utf8: %d\n", utfCount);
  return 0;
}
