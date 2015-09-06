/** 
 * DisplayPanel.java: A panel for displaying the application graphics.
 *
 * Matthew Plumlee
 * modified by rdb
 * CS416 Spring 2008
 */

import javax.swing.JPanel;
import javax.swing.*;
import java.awt.*;
import java.util.*;

public class DisplayPanel extends JPanel 
{
   //------------------- instance variables ------------------------
   private BinarySearchTree _bst   = null;          // current list to be displayed
   private ArrayList<GNode> _glist = null;  // list of graphical nodes.
   
   //------------- magic constants
   private int       _deltaX = 20; // space between nodes
   private int       _deltaY = 20;
   private int       _firstX;    // x loc for first node on a row
   private int       _firstY;    // y loc for first row
   private int       _nextX;     // location for next node to be added
   private int       _nextY;
   private int       _nodeWidth = 25;  // size of node
   private int       _nodeHeight = 30;
   private int       _stepX      = _deltaX + _nodeWidth;
   private int       _stepY      = _deltaY + _nodeHeight;
   private int       _defaultW   = 700;
   private int       _defaultH   = 500;
   

   //--------------------- constructor ----------------------------
   public DisplayPanel () 
   {
      super();
      setLayout( null );
      //setSize( new Dimension( 500, 500 ));
      setPreferredSize( new Dimension( _defaultW, _defaultH ));
      //setBackground( new Color( 0, 255, 255 ));
   }
   //----------------------- setTree( DisplayList ) ----------------
   /**
    * define a new list to display
    */
   public void setTree( BinarySearchTree bst )
   {
      _bst = bst;
      update();
   }
   //----------------------  setNodeWidth( int ) -------------------
   public void setNodeWidth( int newW )
   {
      if ( newW > 0 )
      {
         _nodeWidth = newW;
         _stepX = _nodeWidth + _deltaX;
      }
      update();
   }
   //------------------------ update() -----------------------------
   /**
    * update the graphical representation of the list being shown
    */
   public void update()
   {
      if ( _bst == null )
         return;
      
      int treeH = _bst.height();
      if ( treeH == 0 )
         treeH = 7;
      //int leafCount = (int) Math.pow( 2, treeH - 1 );
      // rdb: since most trees are not terribly dense, artificially
      //      reduce space by pretending tree is 1 level shorter.
      int leafCount = (int) Math.pow( 2, treeH - 2 );  
      
      int width = leafCount * _stepX;
      int height = treeH * _stepY;
      
      setPreferredSize( new Dimension( width, height ));
            
      cleanupGNodes();
      
      _glist = new ArrayList<GNode>();
      
      int xLoc = width / 2 + _nodeWidth / 2;
      int subTreeWidth = width / 2;  // half the size of the panel
      GNode groot = prefixWalk( _bst.root(), xLoc, subTreeWidth, 0 );
      
      //this.revalidate();
      this.repaint();
   } 
   //--------------------- prefixWalk( Node, _glist ) -------------------
   private GNode prefixWalk( BinarySearchTree.Node node, 
                            int xc, int w, int depth )
   {
      if ( node == null )
         return null;
      GNode gnode = new GNode( node, _nodeWidth, _nodeHeight );
      _glist.add( gnode );
      this.add( gnode );
      gnode.setLocation( xc - _nodeWidth / 2, _firstY + depth * _stepY );
      gnode.left  = prefixWalk( node.left,  xc - w / 2, w / 2, depth + 1 );
      gnode.right = prefixWalk( node.right, xc + w / 2, w / 2, depth + 1 );
      return gnode;
   }
      
   //----------------------- setPosition ----------------------------
   /**
    * determine the location of the "next" node to be displayed
    */
   public void setPosition( GNode label )
   {
      label.setLocation( _nextX, _nextY );
      _nextX += _stepX; //_deltaX + _nodeWidth;
      if ( _nextX + _nodeWidth > _defaultW )
      {
         _nextX = _firstX;
         _nextY += _deltaY + _nodeHeight;
      }
      label.repaint();
   }
      
   //---------------------- cleanupGNodes() ----------------------------
   /**
    * When we change to a new DS, need to get all the JComponents
    * that have been added to the JPanel and remove them!
    */
   private void cleanupGNodes()
   {
      if ( _glist == null )
         return;
      Iterator<GNode> iter = _glist.iterator();
      while ( iter.hasNext() )
      {
         GNode gNode = iter.next();
         this.remove( gNode );
      }
   }
   //-------------------- paintComponent ------------------------------
   /**
    * draw the arrows between items
    */
   public void paintComponent( Graphics g )
   {
      super.paintComponent( g );
      if ( _glist != null && _glist.size() > 1 )
      {        
         Graphics2D g2 = (Graphics2D) g;
         Arrow      leftArrow = new Arrow();
         leftArrow.setColor( Color.BLACK );
         
         Arrow      rightArrow = new Arrow();
         rightArrow.setColor( Color.RED );
         
         Iterator<GNode> iter = _glist.iterator();
         
         while ( iter.hasNext() )
         {
            GNode nCur  = iter.next();       
            if ( nCur.left != null )
            {
               leftArrow.setLine( nCur.getStart(), nCur.left.getEnd() );
               leftArrow.draw( g2 );
            }
            if ( nCur.right != null )
            {
               rightArrow.setLine( nCur.getStart(), nCur.right.getEnd() );
               rightArrow.draw( g2 );
            }
        }
      }
   }
}