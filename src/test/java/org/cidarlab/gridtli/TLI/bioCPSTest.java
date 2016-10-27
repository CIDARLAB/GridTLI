/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.gridtli.TLI;

import java.util.List;
import org.cidarlab.gridtli.DOM.Grid;
import org.cidarlab.gridtli.DOM.Signal;
import org.cidarlab.gridtli.Visualize.JavaPlotAdaptor;
import org.junit.Test;

/**
 *
 * @author prash
 */
public class bioCPSTest {
    
    //@Test
    public void testModule11Data(){
        String filepath = Utilities.getResourcesFilepath() + "bioCPS" + Utilities.getSeparater() + "modules" + Utilities.getSeparater() + "1-1-data.csv";
        List<Signal> signals = Utilities.getSignalsBioCPS(filepath);
        Grid grid = new Grid(signals,1,10000);
        TemporalLogicInference.getSTL(grid);
        JavaPlotAdaptor.plotToFile(JavaPlotAdaptor.visualizeSubGrid(grid.getSubGrid().keySet()), Utilities.getResourcesTempFilepath() + "subgrid.png");
        JavaPlotAdaptor.plotToFile(JavaPlotAdaptor.plotGridwithoutCover(grid), Utilities.getResourcesTempFilepath() + "gridnoCover.png");
        JavaPlotAdaptor.plotToFile(JavaPlotAdaptor.plotGrid(grid), Utilities.getResourcesTempFilepath() + "grid.png");
        
        
    }
    
    @Test
    public void testFadingDrop(){
        String filepathAHL = Utilities.getResourcesFilepath() + "bioCPS" + Utilities.getSeparater() + "fadingDrop" + Utilities.getSeparater() + "AHL-data.csv";
        String filepathAHL_neighbour = Utilities.getResourcesFilepath() + "bioCPS" + Utilities.getSeparater() + "fadingDrop" + Utilities.getSeparater() + "AHL_neighborAvg-data.csv";
        String filepathGFP = Utilities.getResourcesFilepath() + "bioCPS" + Utilities.getSeparater() + "fadingDrop" + Utilities.getSeparater() + "GFP-data.csv";
        getFormulaAndPlots("AHL",filepathAHL,5,0.01);
        //getFormulaAndPlots("AHL_nAVG",filepathAHL_neighbour,1,0.01);
        //getFormulaAndPlots("GFP",filepathGFP,1,0.01);
        
        
    }
    
    public static void getFormulaAndPlots(String filename,String filepath,double xThreshHold, double yThreshHold){
        
        List<Signal> signals = Utilities.getSignalsBioCPS(filepath);
        Grid grid = new Grid(signals,xThreshHold,yThreshHold);
        //TemporalLogicInference.getSTL(grid);
        //JavaPlotAdaptor.plotToFile(JavaPlotAdaptor.visualizeSubGrid(grid.getSubGrid()), Utilities.getResourcesTempFilepath() + "subgrid" + filename +  ".png");
        //JavaPlotAdaptor.plotToFile(JavaPlotAdaptor.plotGridwithoutCover(grid), Utilities.getResourcesTempFilepath() + "gridnoCover" + filename + ".png");
        JavaPlotAdaptor.plotToFile(JavaPlotAdaptor.plotGrid(grid), Utilities.getResourcesTempFilepath() + "grid" + filename +".png");
        
    }
    
}
