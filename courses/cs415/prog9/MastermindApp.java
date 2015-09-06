import java.awt.Color;
import wheelsunh.users.*;
import java.util.Random;
public class MastermindApp
{
  //--------------------Instance Variables------------------------------------
  protected Rectangle outerBoard, rectHide, outerCheck[];
  protected Random randGen;
  protected Peg myPegs[], myCode[], myBoardPegs[][];
  protected CheckCode myCheckCode[];
  protected int myX, myY, randColor, curRow = 0, pegCount = 0, redColorCount = 0;
  protected int checkX = 310, checkY = 47, userX, userY, greenColorCount = 0;
  protected boolean isNear, checkable, wasIncremented, matchBoard[], matchCode[];
  protected Color colors[] = {Color.RED, Color.BLUE, Color.GREEN, Color.CYAN,
    Color.MAGENTA, Color.YELLOW};
  
  public MastermindApp(int x, int y)
  {
    userX = x;
    userY = y;
    
    //Initializes the Random Number Generator
    randGen = new Random();
    
    //Creates the Outer Board of MastermindApp
    outerBoard = new Rectangle(x-10, y-10);
    outerBoard.setSize(150, 400);
    outerBoard.setFillColor(Color.ORANGE);
    outerBoard.setFrameColor(Color.BLACK);
    
    /*
     * Initializes an array of boxes that tell the user how close their guess
     * is to the secret code
     * */
    myCheckCode = new CheckCode[12];
    for(int i = 0; i < 12; i++)
    {
      myCheckCode[i] = new CheckCode(checkX, checkY);
      checkY += 30;
    }
    
    
    //Initializes 2 Dimensional Array of Pegs for the Board
    myBoardPegs = new Peg[12][4];
    //Saves x value
    myX = x;
    myY = y;
    
    //Creates Board Pegs
    for(int i = 0; i < 12; i++)
    {
      for(int j = 0; j < 4; j++)
      {
        myBoardPegs[i][j] = new Peg(x, y, Color.GRAY, false, this);
        x += 30;   
      }  
      //Increments y and resets x
      y+=30;
      x = myX; 
    }
    //Initializes array of Pegs for the User
    myPegs = new Peg[6];
    for(int i = 0; i < 6; i++)
    {
      myPegs[i] = new Peg(x + 200, y - 360, colors[i], true, this);
      x += 30;
    }  
    
    //Generate a new Code
    genCode();
    //Hide the Code
    hideCode();
  }
  //--------------------------------------------------------------------------
  /*
   * Generates a new Code
   * */
  public void genCode()
  {
    myCode = new Peg[4];
    int x = 200;
    
    for(int i = 0; i < 4; i++)
    {
      randColor = randGen.nextInt(6);
      myCode[i] = new Peg(x, 410, colors[randColor], false, this);
      x+= 30;
    }
  }
  //---------------------------------------------------------------------------
  /*
   * Creates a rectangle to hide the secret code
   * */
  public void hideCode()
  {
    int x = 200;
    int y = 50;
    
    
    rectHide = new Rectangle(x, y + 360);
    rectHide.setSize(110, 20);
    rectHide.setColor(Color.BLACK);
  }
  public void newGame()
  {
    //Generate a new Code
    genCode();
    //Hide the Code
    hideCode();
 
    //Sets all pegs to Gray
    for(int i = 0; i < 12; i++)
    {
      for(int j = 0; j < 4; j++)
      {
        myBoardPegs[i][j].setColor(Color.GRAY);
      }    
    }
    
    //Set first row of pegs to black
    for(int i = 0; i < 4; i++)
    {
      myBoardPegs[0][i].setColor(Color.BLACK);
    }
    //Reset all pegs inside the Check Code Rectangles
    for(int i = 0; i < 12; i++)
    {
      Peg temp[] = myCheckCode[i].getCheckCode();
      for(int j = 0; j < 4; j++)
      {
        temp[j].setColor(Color.WHITE);
        temp[j].setFrameColor(Color.BLACK);
      }
      
    }
    //Reset Current Row
    curRow = 0;
  }
  //---------------------------------------------------------------------------
  /*
   * Shows Rectangle that hides the Code
   * */
  //---------------------------------------------------------------------------
  public void showCode()
  {
    rectHide.hide();
  }
  //---------------------------------------------------------------------------
  /*
   *Gets the Current Row of the Two Dimensional array for the Peg MouseReleased
   *Method
   * */
  //---------------------------------------------------------------------------
  public Peg[] getCurRow()
  {
    return myBoardPegs[curRow]; 
  }
  //---------------------------------------------------------------------------
  /*Returns the Secret Code 
   * */
  //---------------------------------------------------------------------------
  public Peg[] getCode()
  {
    return myCode;
  }
  //---------------------------------------------------------------------------
  public void incNumPegs()
  {
    Peg innerPegs[] = myCheckCode[curRow].getCheckCode();
    matchBoard = new boolean[4];
    matchCode = new boolean[4];
    pegCount++;
    if (pegCount == 4)
    {
      Peg myCode[] = this.getCode();
      for(int i = 0; i < 4; i++)
      {
        if(myBoardPegs[curRow][i].getColor() == myCode[i].getColor())
        {
          redColorCount++;
          innerPegs[i].setColor(Color.RED);
          matchBoard[i] = true;
          matchCode[i] = true;
        }
      }
      
      for(int i = 0; i < 4; i++)
      {
        if(matchBoard[i] == false)
        {
          if(matchCode[0] == false && myBoardPegs[curRow][i].getColor() ==
             myCode[i].getColor())
          {
            matchBoard[i] = true;
            matchCode[0] = true;
            redColorCount++;
            innerPegs[i].setColor(Color.RED);
          }
          else if (matchCode[1] == false && (myBoardPegs[curRow][i].getColor() ==
                  myCode[1].getColor()))
          {
            matchBoard[i] = true;
            matchCode[1] = true;
            
            greenColorCount++;
            innerPegs[i].setColor(Color.GREEN);
          }
          else if(matchCode[2] == false && (myBoardPegs[curRow][i].getColor() ==
                  myCode[2].getColor()))
          {
            matchBoard[i] = true;
            matchCode[2] = true;
            
            greenColorCount++;
            innerPegs[i].setColor(Color.GREEN);
          }
          else if(matchCode[3] == false && (myBoardPegs[curRow][i].getColor() ==
                  myCode[3].getColor()))
          {
            matchBoard[i] = true;
            matchCode[3] = true;
            
            greenColorCount++;
            innerPegs[i].setColor(Color.GREEN);
          }
          
          }
      }
      
      
      System.out.println("Red: " + redColorCount + "\tGreen: " + greenColorCount);
      
      curRow++;
      pegCount = 0;
      Peg myBoardPegs[] = this.getCurRow();
      for (int i = 0; i < 4; i++)
      {
        myBoardPegs[i].setColor(Color.BLACK);
      }
      greenColorCount = 0;
      redColorCount = 0;
      }
  }
  //----------------------------------------------------------------------------
  /**
   *
   * Initializes Game Display
   */
  
  public static void main(String [] args)
  {
    Frame f = new Frame();
    MastermindApp app = new MastermindApp(200, 50);
    
    
    NewGameButton newGame = new NewGameButton(500, 200, app);
    newGame.setText("New Game");
    
    HideButton showHide = new HideButton(500, 250, app);
    showHide.setText("Show/Hide");
    
    
    Peg myBoardPegs[] = app.getCurRow();
    for (int i = 0; i < 4; i++)
    {
      myBoardPegs[i].setColor(Color.BLACK);
    }
  }
  } //End of Class
