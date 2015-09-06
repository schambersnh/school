import static org.junit.Assert.*;

import java.awt.Point;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;


public class NodeTest {
	/*private Node n1;
	private Node n2;
	private Point p1;
	private Point p2;
	private Point p3;
	private ArrayList<Point> blockedCells;
	
	@Before 
	public void setup(){
		p1 = new Point(1, 0);
		p2 = new Point(4, 1);
		p3 = new Point(9, 26);
		blockedCells = new ArrayList<Point>();
		blockedCells.add(p2);
	}
	
	@Test
	public void equals() {
		Point curLocation1 = new Point(7,7);
		Point curLocation2 = new Point(7,7);
		
		ArrayList<Point> dirtyCells1 = new ArrayList<Point>();
		dirtyCells1.add(p1);
		dirtyCells1.add(p2);
		dirtyCells1.add(p3);
		
		ArrayList<Point> dirtyCells2 = new ArrayList<Point>();
		dirtyCells2.add(p1);
		dirtyCells2.add(p2);
		dirtyCells2.add(p3);
		
		State s1 = new State("", blockedCells, dirtyCells1, curLocation1, new Point(10,10), false);
		State s2 = new State("", blockedCells, dirtyCells2, curLocation2, new Point(10,10), false);
		
		n1 = new Node(0, s1);
		n2 = new Node(0, s2);
		
		assertTrue(n1.equals(n2));
	}
	
	@Test
	public void equalsDifferentOrder() {
		Point curLocation1 = new Point(7,7);
		Point curLocation2 = new Point(7,7);
		
		ArrayList<Point> dirtyCells1 = new ArrayList<Point>();
		dirtyCells1.add(p1);
		dirtyCells1.add(p2);
		dirtyCells1.add(p3);
		
		ArrayList<Point> dirtyCells2 = new ArrayList<Point>();
		dirtyCells2.add(p2);
		dirtyCells2.add(p3);
		dirtyCells2.add(p1);
		
		State s1 = new State("", blockedCells, dirtyCells1, curLocation1, new Point(10,10), false);
		State s2 = new State("", blockedCells, dirtyCells2, curLocation2, new Point(10,10), false);
		
		n1 = new Node(0, s1);
		n2 = new Node(0, s2);
		
		assertTrue(n1.equals(n2));
	}
	
	@Test
	public void equalsChangeBlockedCells() {
		Point curLocation1 = new Point(7,7);
		Point curLocation2 = new Point(7,7);
		
		ArrayList<Point> dirtyCells1 = new ArrayList<Point>();
		dirtyCells1.add(p1);
		dirtyCells1.add(p2);
		dirtyCells1.add(p3);
		
		ArrayList<Point> dirtyCells2 = new ArrayList<Point>();
		dirtyCells2.add(p2);
		dirtyCells2.add(p3);
		dirtyCells2.add(p1);
		
		blockedCells.add(p2);
		
		State s1 = new State("", blockedCells, dirtyCells1, curLocation1, new Point(10,10), false);
		State s2 = new State("", blockedCells, dirtyCells2, curLocation2, new Point(10,10), false);
		
		n1 = new Node(0, s1);
		n2 = new Node(0, s2);
		
		assertTrue(n1.equals(n2));
	}

	
	@Test
	public void notEqualsLocation() {
		Point curLocation1 = new Point(7,7);
		Point curLocation2 = new Point(7,6);
		
		ArrayList<Point> dirtyCells1 = new ArrayList<Point>();
		dirtyCells1.add(p1);
		dirtyCells1.add(p2);
		dirtyCells1.add(p3);
		
		ArrayList<Point> dirtyCells2 = new ArrayList<Point>();
		dirtyCells2.add(p2);
		dirtyCells2.add(p3);
		dirtyCells2.add(p1);
		
		State s1 = new State("", blockedCells, dirtyCells1, curLocation1, new Point(10,10), false);
		State s2 = new State("", blockedCells, dirtyCells2, curLocation2, new Point(10,10), false);
		
		n1 = new Node(0, s1);
		n2 = new Node(0, s2);
		
		assertFalse(n1.equals(n2));
	}
	
	@Test
	public void notEqualsDirtyCellsSize() {
		Point curLocation1 = new Point(7,7);
		Point curLocation2 = new Point(7,7);
		
		ArrayList<Point> dirtyCells1 = new ArrayList<Point>();
		dirtyCells1.add(p1);
		dirtyCells1.add(p2);
		dirtyCells1.add(p3);
		
		ArrayList<Point> dirtyCells2 = new ArrayList<Point>();
		dirtyCells2.add(p2);
		dirtyCells2.add(p3);
		
		State s1 = new State("", blockedCells, dirtyCells1, curLocation1, new Point(10,10), false);
		State s2 = new State("", blockedCells, dirtyCells2, curLocation2, new Point(10,10), false);
		
		n1 = new Node(0, s1);
		n2 = new Node(0, s2);
		
		assertFalse(n1.equals(n2));
	}
	
	@Test
	public void notEqualsDirtyCellsValue() {
		Point curLocation1 = new Point(7,7);
		Point curLocation2 = new Point(7,7);
		
		ArrayList<Point> dirtyCells1 = new ArrayList<Point>();
		dirtyCells1.add(p2);
		
		ArrayList<Point> dirtyCells2 = new ArrayList<Point>();
		dirtyCells2.add(p1);
		
		State s1 = new State("", blockedCells, dirtyCells1, curLocation1, new Point(10,10), false);
		State s2 = new State("", blockedCells, dirtyCells2, curLocation2, new Point(10,10), false);
		
		n1 = new Node(0, s1);
		n2 = new Node(0, s2);
		
		assertFalse(n1.equals(n2));
	}

	
	@Test
	public void notEqualsLocationDirtyCells() {
		Point curLocation1 = new Point(7,7);
		Point curLocation2 = new Point(7,6);
		
		ArrayList<Point> dirtyCells1 = new ArrayList<Point>();
		dirtyCells1.add(p2);
		dirtyCells1.add(p3);
		
		ArrayList<Point> dirtyCells2 = new ArrayList<Point>();
		dirtyCells2.add(p3);
		
		State s1 = new State("", blockedCells, dirtyCells1, curLocation1, new Point(10,10), false);
		State s2 = new State("", blockedCells, dirtyCells2, curLocation2, new Point(10,10), false);
		
		n1 = new Node(0, s1);
		n2 = new Node(0, s2);
		
		assertFalse(n1.equals(n2));
	}
	
	@Test
	public void hashCodeTest() {
		Point curLocation1 = new Point(7,7);
		Point curLocation2 = new Point(7,7);
		
		ArrayList<Point> dirtyCells1 = new ArrayList<Point>();
		dirtyCells1.add(p1);
		dirtyCells1.add(p2);
		dirtyCells1.add(p3);
		
		ArrayList<Point> dirtyCells2 = new ArrayList<Point>();
		dirtyCells2.add(p2);
		dirtyCells2.add(p3);
		dirtyCells2.add(p1);
		
		State s1 = new State("", blockedCells, dirtyCells1, curLocation1, new Point(10,10), false);
		State s2 = new State("", blockedCells, dirtyCells2, curLocation2, new Point(10,10), false);
		
		n1 = new Node(0, s1);
		n2 = new Node(0, s2);
		
		assertEquals(n1.hashCode(), n2.hashCode());
	}

*/

	
	/*
	Location     Dirty Cells
	  same          same
	  different     different
	  same          different
	  different same
	*/

}
