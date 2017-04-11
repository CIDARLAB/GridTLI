/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.gridtli.TLI;

import com.panayotis.gnuplot.JavaPlot;
import com.panayotis.gnuplot.dataset.PointDataSet;
import com.panayotis.gnuplot.plot.DataSetPlot;
import com.panayotis.gnuplot.style.NamedPlotColor;
import com.panayotis.gnuplot.style.PlotStyle;
import com.panayotis.gnuplot.style.Style;
import java.util.ArrayList;
import java.util.List;
import org.cidarlab.gridtli.DOM.Grid;
import org.cidarlab.gridtli.DOM.Point;
import org.cidarlab.gridtli.DOM.Signal;
import org.cidarlab.gridtli.DOM.SubGrid;
import org.cidarlab.gridtli.Visualize.JavaPlotAdaptor;
import static org.cidarlab.gridtli.Visualize.JavaPlotAdaptor.getSignalJPlotPoints;
import static org.cidarlab.gridtli.Visualize.JavaPlotAdaptor.getSubGridJPlotPoints;

/**
 *
 * @author prash
 */
public class CompositionTest {
    
    public static void createFolder(String filepath){
        if(!Utilities.isDirectory(filepath)){
            Utilities.makeDirectory(filepath);
        }
    }
    
    public static void main(String[] args) {
        CompositionTest ct = new CompositionTest();
        
        String plotresults = Utilities.getResourcesFilepath() + "ibiosim" + Utilities.getSeparater()+ "newData" + Utilities.getSeparater() + "plots" + Utilities.getSeparater();
        createFolder(plotresults);
        String modulescascadesFP = plotresults + "moduleCascades" + Utilities.getSeparater();
        String modulesFP = plotresults + "modules" + Utilities.getSeparater();
        createFolder(modulescascadesFP);
        createFolder(modulesFP);
        
        double threshold = 5;
        double xthreshold = 10;
        double ythreshold = 5;

        for (int k = 0; k < 7; k++) {
            switch (k) {
                case 0:
                    threshold = 1;
                    xthreshold = 1;
                    ythreshold = 1;
                    break;
                case 1:
                    threshold = 1;
                    xthreshold = 5;
                    ythreshold = 1;
                    break;
                case 2:
                    threshold = 4;
                    xthreshold = 5;
                    ythreshold = 5;
                    break;
                case 3:
                    threshold = 5;
                    xthreshold = 5;
                    ythreshold = 5;
                    break;
                case 4:
                    threshold = 1;
                    xthreshold = 10;
                    ythreshold = 1;
                    break;
                case 5:
                    threshold = 4;
                    xthreshold = 10;
                    ythreshold = 5;
                    break;
                case 6:
                    threshold = 5;
                    xthreshold = 10;
                    ythreshold = 5;
                    break;
            }

            for (int i = 1; i <= 3; i++) {
                for (int j = 1; j <= 2; j++) {

                    String file1 = i + "-" + j + "-data";
                    String filepath1 = Utilities.getResourcesFilepath() + "ibiosim" + Utilities.getSeparater()+ "newData" + Utilities.getSeparater() + "modulesLowInput" + Utilities.getSeparater() + file1 + ".csv";
                    List<Signal> m1 = Utilities.getiBioSimSignals(filepath1);
                    Grid g1 = new Grid(m1,xthreshold,ythreshold);
                    String mPlotFP = modulesFP + file1 + ".png";
                    JavaPlotAdaptor.plotToFile(JavaPlotAdaptor.plotGrid(g1), mPlotFP);
                    for (int m = 1; m <= 3; m++) {
                        for (int n = 1; n <= 2; n++) {
                            String file2 = m + "-" + n + "-data";
                            if(i == m && j == n){
                                continue;
                            }
                            //modulescascadesFP
                            String filepath2 = Utilities.getResourcesFilepath() + "ibiosim" + Utilities.getSeparater()+ "newData" + Utilities.getSeparater() + "modulesLowInput" + Utilities.getSeparater() + file2 + ".csv";
                            List<Signal> m2 = Utilities.getiBioSimSignals(filepath2);
                            String cPlotFP = modulescascadesFP + file1 + "TO" + file2 + ".png";
                            JavaPlotAdaptor.plotToFile(composedModulesGrid(m1,m2,xthreshold,ythreshold), cPlotFP);
                        }
                    }
                    
                    
                }
            }
        }
        
    }
    
    public static JavaPlot composedModulesGrid(List<Signal> m1, List<Signal> m2, double time, double signal){
        
        double tmax = 0;
        for(Signal s:m1){
            if(tmax < s.getPoints().get(s.getPoints().size()-1).getX()){
                tmax = s.getPoints().get(s.getPoints().size()-1).getX();
            }
        }
        for(Signal s:m2){
            for(Point p:s.getPoints()){
                double tValue = p.getX();
                p.setX(tValue + tmax);
            }
        }
        List<Signal> cascade = new ArrayList<Signal>();
        cascade.addAll(m1);
        cascade.addAll(m2);
        Grid g = new Grid(cascade,time,signal);
        return plotGrid(g,tmax);
    }
    
    
    public static JavaPlot plotGrid(Grid grid, double nextTime){
        JavaPlot plot = new JavaPlot();
        PlotStyle ps = new PlotStyle();
        ps.setStyle(Style.DOTS);
        ps.setLineType(NamedPlotColor.BLACK);
        PointDataSet pdsgrid = new PointDataSet(getSubGridJPlotPoints(grid.getSubGrid().keySet()));
        DataSetPlot dspgrid = new DataSetPlot(pdsgrid);
        dspgrid.setPlotStyle(ps);
        plot.addPlot(dspgrid);
        
        for(Signal signal:grid.getSignals()){
            PlotStyle sps = new PlotStyle();
            sps.setStyle(Style.LINES);
            sps.setLineType(NamedPlotColor.BLACK);
            PointDataSet psd = new PointDataSet(getSignalJPlotPoints(signal));
            DataSetPlot dsp = new DataSetPlot(psd);
            dsp.setPlotStyle(sps);
            plot.addPlot(dsp);
        }
        int count =1;
        for(SubGrid subgrid: grid.getSubGrid().keySet()){
            if(grid.isSpecificSubGridCovered(subgrid)){
                
                //System.out.println("Covered: " +  subgrid.getXOrigin()+","+subgrid.getYOrigin());
                String obj = "object " + count;
                String rect = "";
                if(subgrid.getXOrigin() < nextTime){
                    rect = "rect from " + subgrid.getXOrigin()+","+subgrid.getYOrigin() + " to " + (subgrid.getXOrigin() + grid.getXIncrement()) + "," + (subgrid.getYOrigin()+grid.getYIncrement()) + " fc rgb \"yellow\"";
                } 
                else{
                    rect = "rect from " + subgrid.getXOrigin()+","+subgrid.getYOrigin() + " to " + (subgrid.getXOrigin() + grid.getXIncrement()) + "," + (subgrid.getYOrigin()+grid.getYIncrement()) + " fc rgb \"cyan\"";
                } 
                plot.set(obj, rect);
                count++;
            }
        }
        plot.set("style fill", "transparent solid 0.5");
        plot.getAxis("x").setLabel("x");
        plot.getAxis("y").setLabel("y");
        plot.setTitle("Grid");
        plot.set("xzeroaxis", "");
        plot.set("yzeroaxis", "");
        plot.set("key", "off");
        return plot;
    }
    
    
}
