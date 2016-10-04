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
public class SignalTest {
    
    public SignalTest() {
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
    public void newSignalTest(){
        Point p1 = new Point(1,2);
        Point p2 = new Point(1,5);
        Point p3 = new Point(2,6);
        Point p4 = new Point(5,3);
        Point p5 = new Point(3,1);
        Point p6 = new Point(4,1);
        List<Point> points = new ArrayList<Point>();
        
        
        points.add(p1);
        points.add(p2);
        points.add(p3);
        points.add(p4);
        points.add(p5);
        points.add(p6);
        
        System.out.println("Before swap");
        System.out.println(points);
        
        Signal signal = new Signal(points);
        System.out.println("After Swap");
        System.out.println(signal.getPoints());
        
    }
    
    
    
    @Test
    public void getGridPointsTest(){
        Point p1 = new Point(1,2);
        Point p2 = new Point(1,5);
        Point p3 = new Point(2,6);
        Point p4 = new Point(5,3);
        Point p5 = new Point(3,1);
        Point p6 = new Point(4,1);
        List<Point> points = new ArrayList<Point>();
        
        
        points.add(p1);
        points.add(p2);
        points.add(p3);
        points.add(p4);
        points.add(p5);
        points.add(p6);
        
        
        Signal signal = new Signal(points);
        System.out.println(signal.getPoints());
        System.out.println("\n");
        System.out.println(signal.getGridPoints(2, 0.5));
        
    }
    
    
}
