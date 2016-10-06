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
    public void getGridPointsTest(){
        
        Point p1 = new Point(1,1);
        Point p2 = new Point(1.1,1.2);
        Point p3 = new Point(1.2,1.3);
        Point p4 = new Point(1.5,0.8);
        Point p5 = new Point(2,1);
        Point p6 = new Point(2,5);
        Point p7 = new Point(2.5,5);
        Point p8 = new Point(4,5);
        Point p9 = new Point(6,8);
        
        List<Point> points = new ArrayList<Point>();
        points.add(p1);
        points.add(p2);
        points.add(p3);
        points.add(p4);
        points.add(p5);
        points.add(p6);
        points.add(p7);
        points.add(p8);
        points.add(p9);
        
        Signal signal = new Signal(points);
        
        System.out.println("Points :: ");
        System.out.println(points + "\n");
        
        System.out.println("Between x1, inc1 :: {1,2}");
        System.out.println(signal.getGridPoints(1, 1));
        
        System.out.println("Between x1, inc0.1 :: {1,1.1}");
        System.out.println(signal.getGridPoints(1, 0.1));
        
        System.out.println("Between x0, inc0.5 :: {0,0.5}");
        System.out.println(signal.getGridPoints(0, 0.5));
        
        System.out.println("Between x4.5, inc1 :: {4.5,5.5}");
        System.out.println(signal.getGridPoints(4.5, 1));
    
    }
    
}
