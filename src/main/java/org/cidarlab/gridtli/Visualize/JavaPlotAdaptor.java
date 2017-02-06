/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.gridtli.Visualize;
import com.panayotis.gnuplot.JavaPlot;
import com.panayotis.gnuplot.dataset.Point;
import com.panayotis.gnuplot.dataset.PointDataSet;
import com.panayotis.gnuplot.plot.DataSetPlot;
import com.panayotis.gnuplot.style.NamedPlotColor;
import com.panayotis.gnuplot.style.PlotColor;
import com.panayotis.gnuplot.style.PlotStyle;
import com.panayotis.gnuplot.style.Style;
import com.panayotis.gnuplot.terminal.ImageTerminal;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import javax.imageio.ImageIO;
import org.cidarlab.gridtli.DOM.Grid;
import org.cidarlab.gridtli.DOM.Signal;
import org.cidarlab.gridtli.DOM.SubGrid;

/**
 *
 * @author prash
 */
public class JavaPlotAdaptor {
 
    
    public static List<Point> getSignalJPlotPoints(Signal signal){
        List<Point> points = new ArrayList<Point>();
        for(org.cidarlab.gridtli.DOM.Point point:signal.getPoints()){
            points.add(new Point(point.getX(),point.getY()));
        }
        return points;
    }
    
    public static List<Point> getSubGridJPlotPoints(Set<SubGrid> subgrids){
        List<Point> points = new ArrayList<Point>();
        for(SubGrid subgrid:subgrids){
            points.add(new Point(subgrid.getXOrigin(),subgrid.getYOrigin()));
        }
        return points;
    }
    
    
    public static JavaPlot plotGrid(Grid grid){
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
            sps.setLineType(NamedPlotColor.RED);
            PointDataSet psd = new PointDataSet(getSignalJPlotPoints(signal));
            DataSetPlot dsp = new DataSetPlot(psd);
            dsp.setPlotStyle(sps);
            plot.addPlot(dsp);
        }
        int count =1;
        for(SubGrid subgrid: grid.getSubGrid().keySet()){
            if(!grid.isSpecificSubGridCovered(subgrid)){
                
                //System.out.println("Covered: " +  subgrid.getXOrigin()+","+subgrid.getYOrigin());
                String obj = "object " + count;
                String rect = "rect from " + subgrid.getXOrigin()+","+subgrid.getYOrigin() + " to " + (subgrid.getXOrigin() + grid.getXIncrement()) + "," + (subgrid.getYOrigin()+grid.getYIncrement()) + " fc rgb \"cyan\"";
                
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
    
    public static JavaPlot plotGridData1_1(Grid grid){
        JavaPlot plot = new JavaPlot();
        PlotStyle ps = new PlotStyle();
        ps.setStyle(Style.DOTS);
        ps.setLineType(NamedPlotColor.BLACK);
        PointDataSet pdsgrid = new PointDataSet(getSubGridJPlotPoints(grid.getSubGrid().keySet()));
        DataSetPlot dspgrid = new DataSetPlot(pdsgrid);
        dspgrid.setPlotStyle(ps);
        plot.addPlot(dspgrid);
        
        
        
        for(int i=0;i<10;i++){
            Signal signal = grid.getSignals().get(i);
            PlotStyle sps = new PlotStyle();
            sps.setStyle(Style.LINES);
            sps.setLineType(NamedPlotColor.ORANGE_RED);
            PointDataSet psd = new PointDataSet(getSignalJPlotPoints(signal));
            DataSetPlot dsp = new DataSetPlot(psd);
            dsp.setPlotStyle(sps);
            plot.addPlot(dsp);
        }
        
        for(int i=10;i<11;i++){
            Signal signal = grid.getSignals().get(i);
            PlotStyle sps = new PlotStyle();
            sps.setStyle(Style.LINES);
            sps.setLineType(NamedPlotColor.DARK_VIOLET);
            PointDataSet psd = new PointDataSet(getSignalJPlotPoints(signal));
            DataSetPlot dsp = new DataSetPlot(psd);
            dsp.setPlotStyle(sps);
            plot.addPlot(dsp);
        }
        
        for(int i=11;i<12;i++){
            Signal signal = grid.getSignals().get(i);
            PlotStyle sps = new PlotStyle();
            sps.setStyle(Style.LINES);
            sps.setLineType(NamedPlotColor.MIDNIGHT_BLUE);
            PointDataSet psd = new PointDataSet(getSignalJPlotPoints(signal));
            DataSetPlot dsp = new DataSetPlot(psd);
            dsp.setPlotStyle(sps);
            plot.addPlot(dsp);
        }
        
        int count =1;
        for(SubGrid subgrid: grid.getSubGrid().keySet()){
            if(!grid.isSpecificSubGridCovered(subgrid)){
                
                //System.out.println("Covered: " +  subgrid.getXOrigin()+","+subgrid.getYOrigin());
                String obj = "object " + count;
                String rect = "rect from " + subgrid.getXOrigin()+","+subgrid.getYOrigin() + " to " + (subgrid.getXOrigin() + grid.getXIncrement()) + "," + (subgrid.getYOrigin()+grid.getYIncrement()) + " fc rgb \"beige\"";
                
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
    
    
    
    public static JavaPlot plotGridData1_1alt_1(Grid grid){
        JavaPlot plot = new JavaPlot();
        PlotStyle ps = new PlotStyle();
        ps.setStyle(Style.DOTS);
        ps.setLineType(NamedPlotColor.BLACK);
        PointDataSet pdsgrid = new PointDataSet(getSubGridJPlotPoints(grid.getSubGrid().keySet()));
        DataSetPlot dspgrid = new DataSetPlot(pdsgrid);
        dspgrid.setPlotStyle(ps);
        plot.addPlot(dspgrid);
        
        
        
        
        
        int count =1;
        for(SubGrid subgrid: grid.getSubGrid().keySet()){
            if(!grid.isSpecificSubGridCovered(subgrid)){
                
                //System.out.println("Covered: " +  subgrid.getXOrigin()+","+subgrid.getYOrigin());
                String obj = "object " + count;
                String rect = "rect from " + subgrid.getXOrigin()+","+subgrid.getYOrigin() + " to " + (subgrid.getXOrigin() + grid.getXIncrement()) + "," + (subgrid.getYOrigin()+grid.getYIncrement()) + " fc rgb \"white\"";
                
                plot.set(obj, rect);
                count++;
            } else{
                String color = "";
                for(int i=0;i<10;i++){
                    if (grid.getSignals().get(i).coversSubGrid(subgrid)) {
                        color = "yellow";
                        String obj = "object " + count;
                        String rect = "rect from " + subgrid.getXOrigin() + "," + subgrid.getYOrigin() + " to " + (subgrid.getXOrigin() + grid.getXIncrement()) + "," + (subgrid.getYOrigin() + grid.getYIncrement()) + " fc rgb \"" + color + "\"";

                        plot.set(obj, rect);
                        count++;
                    }
                }
                
                
            }
        }
        
        for(int i=0;i<10;i++){
            Signal signal = grid.getSignals().get(i);
            PlotStyle sps = new PlotStyle();
            sps.setStyle(Style.LINES);
            sps.setLineType(NamedPlotColor.RED);
            PointDataSet psd = new PointDataSet(getSignalJPlotPoints(signal));
            DataSetPlot dsp = new DataSetPlot(psd);
            dsp.setPlotStyle(sps);
            plot.addPlot(dsp);
        }
        
        for(int i=10;i<11;i++){
            Signal signal = grid.getSignals().get(i);
            PlotStyle sps = new PlotStyle();
            sps.setStyle(Style.LINES);
            sps.setLineType(NamedPlotColor.RED);
            PointDataSet psd = new PointDataSet(getSignalJPlotPoints(signal));
            DataSetPlot dsp = new DataSetPlot(psd);
            dsp.setPlotStyle(sps);
            plot.addPlot(dsp);
        }
        
        for(int i=11;i<12;i++){
            Signal signal = grid.getSignals().get(i);
            PlotStyle sps = new PlotStyle();
            sps.setStyle(Style.LINES);
            sps.setLineType(NamedPlotColor.RED);
            PointDataSet psd = new PointDataSet(getSignalJPlotPoints(signal));
            DataSetPlot dsp = new DataSetPlot(psd);
            dsp.setPlotStyle(sps);
            plot.addPlot(dsp);
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
    
    
    public static JavaPlot plotGridData1_1alt(Grid grid){
        JavaPlot plot = new JavaPlot();
        PlotStyle ps = new PlotStyle();
        ps.setStyle(Style.DOTS);
        ps.setLineType(NamedPlotColor.BLACK);
        PointDataSet pdsgrid = new PointDataSet(getSubGridJPlotPoints(grid.getSubGrid().keySet()));
        DataSetPlot dspgrid = new DataSetPlot(pdsgrid);
        dspgrid.setPlotStyle(ps);
        plot.addPlot(dspgrid);
        
        
        
        
        
        int count =1;
        for(SubGrid subgrid: grid.getSubGrid().keySet()){
            if(!grid.isSpecificSubGridCovered(subgrid)){
                
                //System.out.println("Covered: " +  subgrid.getXOrigin()+","+subgrid.getYOrigin());
                String obj = "object " + count;
                String rect = "rect from " + subgrid.getXOrigin()+","+subgrid.getYOrigin() + " to " + (subgrid.getXOrigin() + grid.getXIncrement()) + "," + (subgrid.getYOrigin()+grid.getYIncrement()) + " fc rgb \"white\"";
                
                plot.set(obj, rect);
                count++;
            } else{
                String color = "";
                for(int i=0;i<10;i++){
                    if(grid.getSignals().get(i).coversSubGrid(subgrid)){
                        color = "yellow";
                    }
                }
                for(int i=10;i<11;i++){
                    if(grid.getSignals().get(i).coversSubGrid(subgrid)){
                        color = "green";
                    }
                }
                for(int i=11;i<12;i++){
                    if(grid.getSignals().get(i).coversSubGrid(subgrid)){
                        color = "orange";
                    }
                }
                String obj = "object " + count;
                String rect = "rect from " + subgrid.getXOrigin()+","+subgrid.getYOrigin() + " to " + (subgrid.getXOrigin() + grid.getXIncrement()) + "," + (subgrid.getYOrigin()+grid.getYIncrement()) + " fc rgb \""+color+"\"";
                
                plot.set(obj, rect);
                count++;
            }
        }
        
        for(int i=0;i<10;i++){
            Signal signal = grid.getSignals().get(i);
            PlotStyle sps = new PlotStyle();
            sps.setStyle(Style.LINES);
            sps.setLineType(NamedPlotColor.RED);
            PointDataSet psd = new PointDataSet(getSignalJPlotPoints(signal));
            DataSetPlot dsp = new DataSetPlot(psd);
            dsp.setPlotStyle(sps);
            plot.addPlot(dsp);
        }
        
        for(int i=10;i<11;i++){
            Signal signal = grid.getSignals().get(i);
            PlotStyle sps = new PlotStyle();
            sps.setStyle(Style.LINES);
            sps.setLineType(NamedPlotColor.RED);
            PointDataSet psd = new PointDataSet(getSignalJPlotPoints(signal));
            DataSetPlot dsp = new DataSetPlot(psd);
            dsp.setPlotStyle(sps);
            plot.addPlot(dsp);
        }
        
        for(int i=11;i<12;i++){
            Signal signal = grid.getSignals().get(i);
            PlotStyle sps = new PlotStyle();
            sps.setStyle(Style.LINES);
            sps.setLineType(NamedPlotColor.RED);
            PointDataSet psd = new PointDataSet(getSignalJPlotPoints(signal));
            DataSetPlot dsp = new DataSetPlot(psd);
            dsp.setPlotStyle(sps);
            plot.addPlot(dsp);
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
    
    
    public static JavaPlot plotSignals(Grid grid){
        JavaPlot plot = new JavaPlot();
        PlotStyle ps = new PlotStyle();
        ps.setStyle(Style.DOTS);
        ps.setLineType(NamedPlotColor.BLACK);
        PointDataSet pdsgrid = new PointDataSet(getSubGridJPlotPoints(grid.getSubGrid().keySet()));
        DataSetPlot dspgrid = new DataSetPlot(pdsgrid);
        dspgrid.setPlotStyle(ps);
        //plot.addPlot(dspgrid);
        
        for(Signal signal:grid.getSignals()){
            PlotStyle sps = new PlotStyle();
            sps.setStyle(Style.LINES);
            sps.setLineType(NamedPlotColor.RED);
            PointDataSet psd = new PointDataSet(getSignalJPlotPoints(signal));
            DataSetPlot dsp = new DataSetPlot(psd);
            dsp.setPlotStyle(sps);
            plot.addPlot(dsp);
        }
        /*int count =1;
        for(SubGrid subgrid: grid.getSubGrid().keySet()){
            if(!grid.isSpecificSubGridCovered(subgrid)){
                
                //System.out.println("Covered: " +  subgrid.getXOrigin()+","+subgrid.getYOrigin());
                String obj = "object " + count;
                String rect = "rect from " + subgrid.getXOrigin()+","+subgrid.getYOrigin() + " to " + (subgrid.getXOrigin() + grid.getXIncrement()) + "," + (subgrid.getYOrigin()+grid.getYIncrement()) + " fc rgb \"cyan\"";
                
                plot.set(obj, rect);
                count++;
            }
        }*/
        plot.set("style fill", "transparent solid 0.5");
        plot.getAxis("x").setLabel("x");
        plot.getAxis("y").setLabel("y");
        plot.setTitle("Grid");
        plot.set("xzeroaxis", "");
        plot.set("yzeroaxis", "");
        plot.set("key", "off");
        return plot;
    }
    
    public static JavaPlot plotGridwithoutCover(Grid grid){
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
            sps.setLineType(NamedPlotColor.RED);
            PointDataSet psd = new PointDataSet(getSignalJPlotPoints(signal));
            DataSetPlot dsp = new DataSetPlot(psd);
            dsp.setPlotStyle(sps);
            plot.addPlot(dsp);
        }
        
        plot.getAxis("x").setLabel("x");
        plot.getAxis("y").setLabel("y");
        plot.setTitle("Grid");
        plot.set("xzeroaxis", "");
        plot.set("yzeroaxis", "");
        plot.set("key", "off");
        return plot;
    }
    
    public static JavaPlot visualizeSubGrid(Set<SubGrid> subgrids){
        JavaPlot plot = new JavaPlot();
        PlotStyle ps = new PlotStyle();
        ps.setStyle(Style.DOTS);
        PointDataSet pds = new PointDataSet(getSubGridJPlotPoints(subgrids));
        DataSetPlot dsp = new DataSetPlot(pds);
        dsp.setPlotStyle(ps);
        plot.addPlot(dsp);
        
        plot.getAxis("x").setLabel("x");
        plot.getAxis("y").setLabel("y");
        plot.setTitle("SubGrid");
        plot.set("xzeroaxis", "");
        plot.set("yzeroaxis", "");
        plot.set("key", "off");
        
        return plot;
    }
    
    public static void plotToFile(JavaPlot plot, String filepath){
        
        ImageTerminal png = new ImageTerminal();
        File file = new File(filepath);
        
        try {
            file.createNewFile();
            png.processOutput(new FileInputStream(file));
        } catch (FileNotFoundException ex) {
            System.err.print("File " + filepath + " not found.\n");
            System.err.print(ex);
        } catch (IOException ex) {
            System.err.print(ex);
        }
        
        plot.setPersist(false);
        plot.setTerminal(png);
        plot.plot();
        
        try {
            ImageIO.write(png.getImage(), "png", file);
        } catch (IOException ex) {
            System.err.print(ex);
        }
    } 
   
    
}
