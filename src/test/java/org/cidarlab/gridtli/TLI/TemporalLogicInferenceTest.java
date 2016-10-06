/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.gridtli.TLI;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.cidarlab.gridtli.DOM.Grid;
import org.cidarlab.gridtli.DOM.Point;
import org.cidarlab.gridtli.DOM.Signal;
import org.cidarlab.gridtli.Visualize.JavaPlotAdaptor;
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
public class TemporalLogicInferenceTest {
    
    public TemporalLogicInferenceTest() {
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
    public void testSignals1(){
        
        List<Point> s1points = new ArrayList<Point>();
        s1points.add(new Point(0.0,0.7));
        s1points.add(new Point(2.5,6.2));
        s1points.add(new Point(6.3,6.1));
        s1points.add(new Point(8.3,0.0));
        
        List<Point> s2points = new ArrayList<Point>();
        s2points.add(new Point(0.0,0.7));
        s2points.add(new Point(2.4,5.2));
        s2points.add(new Point(5.6,5.0));
        s2points.add(new Point(8.2,0.8));
        
        List<Point> s3points = new ArrayList<Point>();
        s3points.add(new Point(0.0,0.4));
        s3points.add(new Point(2.0,3.3));
        s3points.add(new Point(5.8,3.3));
        s3points.add(new Point(8.0,0.4));
        
        List<Point> s4points = new ArrayList<Point>();
        s4points.add(new Point(0.0,0.0));
        s4points.add(new Point(2.1,2.6));
        s4points.add(new Point(5.2,2.7));
        s4points.add(new Point(7.2,0.5));
        
        List<Signal> signals = new ArrayList<Signal>();
        signals.add(new Signal(s1points));
        signals.add(new Signal(s2points));
        signals.add(new Signal(s3points));
        signals.add(new Signal(s4points));
        
        Grid grid = new Grid(signals,0.5,0.5);
        System.out.println("x Lower limit :" + grid.getXLowerLimit());
        System.out.println("x Upper limit :" + grid.getXUpperLimit());
        System.out.println("y Lower limit :" + grid.getYLowerLimit());
        System.out.println("y Upper limit :" + grid.getYUpperLimit());
        
        System.out.println("");
        System.out.println("x centered :" + grid.isXCentered());
        System.out.println("x Start :" + grid.getXStart());
        System.out.println("y centered :" + grid.isYCentered());
        System.out.println("y Start :" + grid.getYStart());
        
        System.out.println("");
        System.out.println("\nSubGrid :: \n" + grid.getSubGrid());
        
        JavaPlotAdaptor.plotToFile(JavaPlotAdaptor.visualizeSubGrid(grid.getSubGrid()), Utilities.getResourcesTempFilepath() + "testgrid.png");
        
    }
    
}
