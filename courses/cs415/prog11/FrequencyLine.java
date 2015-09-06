/*
 * FrequencyLine.java
 * Object representing a line of the Frequency Table
 * 
 * Stephen Chambers
 * 11/16/10
 * */
public class FrequencyLine
{
  //-------------------------Instance Variables--------------------------------
  String word;
  int count = 1;
  /*
   * Constructor for FrequencyLine, simply updates value of instance variable
   * */
  public FrequencyLine(String w)
  {
    word = w;
  }
  public String getWord() //gets the word
  {
    return word;
  }
  public void incCount() //increase count
  {
    count++;
  }
  public String toString() //formats line to a string
  {
    return word + " " + count+"\n";
  }
}