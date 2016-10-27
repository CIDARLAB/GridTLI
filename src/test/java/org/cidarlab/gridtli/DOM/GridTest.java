/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.gridtli.DOM;

import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author prash
 */
public class GridTest {
    
    public GridTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void testWithinGrid(){
        Point p1 = new Point(1.2,1.1);
        Point p2 = new Point(1.4,1.9);
        Point p3 = new Point(1,1);
        Point p4 = new Point(1,1);
        Point p5 = new Point(0.3,0.4);
        Point p6 = new Point(4,5);
        Point p7 = new Point(3,6);
        
        Point p8 = new Point(3,3);
        Point p9 = new Point(4,4);
        
        List<Point> points1 = new ArrayList<Point>();
        points1.add(p1);
        points1.add(p2);
        points1.add(p3);
        points1.add(p4);
        points1.add(p5);
        points1.add(p6);
        points1.add(p7);
        points1.add(p8);
        points1.add(p9);
        
        Signal s1 = new Signal(points1);
        List<Signal> signals1 = new ArrayList<Signal>();
        signals1.add(s1);
        Grid g1 = new Grid(signals1);
        //These should be true
        assertTrue(g1.inGrid(1, 1, 1, 1, p1, p2));
        assertTrue(g1.inGrid(1, 1, 1, 1, p3, p4));
        assertTrue(g1.inGrid(1, 1, 1, 1, p1, p3));
        assertTrue(g1.inGrid(1, 1, 1, 1, p1, p5));
        assertTrue(g1.inGrid(1, 1, 1, 1, p5, p6));
        
        //This should be false
        assertFalse(g1.inGrid(2, 1, 2, 1, p1, p2));
        assertFalse(g1.inGrid(1, 1, 1, 1, p6, p7));
        assertFalse(g1.inGrid(1, 1, 1, 1, p8, p9));
        
    }
    
    @Test
    public void testGetSubGrid(){
        Point p1 = new Point(1.2,1.1);
        Point p2 = new Point(1.4,1.9);
        Point p3 = new Point(1,1);
        Point p4 = new Point(1,1);
        Point p5 = new Point(0.3,0.4);
        Point p6 = new Point(4,5);
        Point p7 = new Point(3,6);
        
        Point p8 = new Point(3,3);
        Point p9 = new Point(4,4);
        
        List<Point> points1 = new ArrayList<Point>();
        points1.add(p1);
        points1.add(p2);
        points1.add(p3);
        points1.add(p4);
        points1.add(p5);
        points1.add(p6);
        points1.add(p7);
        points1.add(p8);
        points1.add(p9);
        
        Signal s1 = new Signal(points1);
        List<Signal> signals1 = new ArrayList<Signal>();
        signals1.add(s1);
        Grid g1 = new Grid(signals1);
        System.out.println("Sub Grids :: " + g1.getSubGrid().keySet());
        
        SubGrid sg = new SubGrid(0,2);
        assertTrue(g1.getSubGrid().containsKey(sg));
        assertFalse(g1.isSpecificSubGridCovered(new SubGrid(0,2)));
        assertTrue(g1.isSpecificSubGridCovered(new SubGrid(0,0)));
        
    }
    
}
