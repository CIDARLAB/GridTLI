/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.gridtli.TLI;

import org.cidarlab.gridtli.tli.TemporalLogicInference;
import org.cidarlab.gridtli.tli.Utilities;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.cidarlab.gridtli.dom.Grid;
import org.cidarlab.gridtli.dom.Point;
import org.cidarlab.gridtli.dom.Signal;
import org.cidarlab.gridtli.visualize.JavaPlotAdaptor;
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
    public void testColumnDemo(){
        System.out.println("COLUMN TYPE DATA");
        String columnData = Utilities.getResourcesFilepath() + "demo" + Utilities.getSeparater() + "column_data.csv";
        List<Signal> signals = Utilities.getColumnSignals(columnData,false);
        double xthreshold = 10;
        double ythreshold = 10;
        double clusterThreshold = 10;
        
        Grid grid = new Grid(signals, xthreshold, ythreshold);
        System.out.println("STL ::\n" + TemporalLogicInference.getSTL(grid, clusterThreshold).toString());
               
        String demoFilepath = Utilities.getResourcesFilepath() + "demo" + Utilities.getSeparater();
        JavaPlotAdaptor.plotToFile(JavaPlotAdaptor.visualizeSubGrid(grid.getSubGrid().keySet()), demoFilepath + "subgrid_Column.png");
        JavaPlotAdaptor.plotToFile(JavaPlotAdaptor.plotGridwithoutCover(grid), demoFilepath + "gridnoCover_Column.png");
        JavaPlotAdaptor.plotToFile(JavaPlotAdaptor.plotGrid(grid), demoFilepath + "grid_Column.png");
        
    }
    
    @Test
    public void testRowDemo(){
        System.out.println("ROW TYPE DATA");
        String rowData = Utilities.getResourcesFilepath() + "demo" + Utilities.getSeparater() + "row_data.csv";
        List<Signal> signals = Utilities.getRowSignals(rowData,false);
        double xthreshold = 1;
        double ythreshold = 1;
        double clusterThreshold = 1;
        
        Grid grid = new Grid(signals, xthreshold, ythreshold);
        System.out.println("STL ::\n" + TemporalLogicInference.getSTL(grid, clusterThreshold).toString());
        
        String demoFilepath = Utilities.getResourcesFilepath() + "demo" + Utilities.getSeparater();
        JavaPlotAdaptor.plotToFile(JavaPlotAdaptor.visualizeSubGrid(grid.getSubGrid().keySet()), demoFilepath + "subgrid_Row.png");
        JavaPlotAdaptor.plotToFile(JavaPlotAdaptor.plotGridwithoutCover(grid), demoFilepath + "gridnoCover_Row.png");
        JavaPlotAdaptor.plotToFile(JavaPlotAdaptor.plotGrid(grid), demoFilepath + "grid_Row.png");
        
    }
    
    
    //@Test
    public void testTLI(){
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
        TemporalLogicInference.getLongSTL(grid);
        JavaPlotAdaptor.plotToFile(JavaPlotAdaptor.visualizeSubGrid(grid.getSubGrid().keySet()), Utilities.getResourcesTempFilepath() + "subgrid.png");
        JavaPlotAdaptor.plotToFile(JavaPlotAdaptor.plotGridwithoutCover(grid), Utilities.getResourcesTempFilepath() + "gridnoCover.png");
        JavaPlotAdaptor.plotToFile(JavaPlotAdaptor.plotGrid(grid), Utilities.getResourcesTempFilepath() + "grid.png");
        
    }
    
    //@Test
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
        
        Grid grid = new Grid(signals,5,5);
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
        
        JavaPlotAdaptor.plotToFile(JavaPlotAdaptor.visualizeSubGrid(grid.getSubGrid().keySet()), Utilities.getResourcesTempFilepath() + "subgrid.png");
        JavaPlotAdaptor.plotToFile(JavaPlotAdaptor.plotGridwithoutCover(grid), Utilities.getResourcesTempFilepath() + "gridnoCover.png");
        JavaPlotAdaptor.plotToFile(JavaPlotAdaptor.plotGrid(grid), Utilities.getResourcesTempFilepath() + "grid.png");
        
    }
    
}
