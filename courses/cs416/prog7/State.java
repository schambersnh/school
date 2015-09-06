import java.util.*;
public class State
{
  //----------------------------Instance Variables----------------------------
  public CardStack _draw;
  public ArrayList<CardStack> _play;
  public CardStack _discard;
  
  public State(CardStack d, ArrayList<CardStack> p, CardStack dis)
  {
    _draw = (CardStack)d.clone();
    _play = new ArrayList<CardStack>();
    for(int i = 0; i < p.size(); i++)
    {
      _play.add((CardStack)p.get(i).clone());
    }
    _discard = (CardStack)dis.clone();
    
  }
  public State(State s)
  {
    this(s._draw, s._play, s._discard);
  }
  public boolean canDraw()
  {
    if(_draw.size() == 0)
    {
      return false;
    }
    else
    {
      return true;
    }
  }
  public boolean isPlayEmpty()
  {
    boolean isPlayEmpty = true;
    for(int i = 0; i < _play.size(); i++)
    {
      if(_play.get(i).size() > 0)
      {
        return false;
      }
    }
    return isPlayEmpty;
  }
  public boolean canPlay()
  {
    boolean canPlay = false;
    Card curCard = _discard.top();
    
    for(int i = 0; i < Game.numPlayPiles; i++)
    {
      if(_play.get(i).top() != null)
      {
        Card topCard = _play.get(i).top();
        
        int topCardOrdinal = topCard.getRank().ordinal();
        int curCardOrdinal = curCard.getRank().ordinal();
        
        if(topCardOrdinal == curCardOrdinal + 1 ||
           topCardOrdinal == curCardOrdinal - 1)
        {
          canPlay = true;
          return canPlay;
        }
      }
    }
    return canPlay;
  }
  public ArrayList<Integer> getPlays()
  {
    ArrayList<Integer> plays = new ArrayList<Integer>();
    Card curCard = _discard.top();
    for(int i = 0; i < Game.numPlayPiles; i++)
    {
      if(_play.get(i).top() != null)
      {
        Card topCard = _play.get(i).top();
        
        int topCardOrdinal = topCard.getRank().ordinal();
        int curCardOrdinal = curCard.getRank().ordinal();
        
        if(topCardOrdinal == curCardOrdinal + 1 ||
           topCardOrdinal == curCardOrdinal - 1)
        {
          plays.add(i);
        }
      }  
    }
    boolean won = true;
  for(int i = 0; i < _play.size(); i++)
  {
    if(_play.get(i).size() > 0)
    {
      won = false;
    }
  }
   
    if(plays.size() == 0 && canDraw() && !won)
    {
      plays.add(-1);
    }
    return plays;
  }
  public int getResults()
  {
  int count = 0;
  int score = 0;
  for(int i = 0; i < _play.size(); i++)
  {
    count += _play.get(i).size();
  }
  score = _draw.size() - count;
  return score;
  }
  public void play(Node n)
  {
    if(n.data < _play.size() && n.data > -1)
    {
      //play from pile
      Card temp = _play.get(n.data).pop();
      if (temp!= null)
      {
      _discard.push(temp);
      }
    }
    else
    {
      //play from draw
      Card temp = _draw.pop();
      if (temp != null)
      _discard.push(temp);
    }
  }
}