/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.gridtli.adaptors;
import com.panayotis.gnuplot.JavaPlot;
import com.panayotis.gnuplot.dataset.Point;
import com.panayotis.gnuplot.dataset.PointDataSet;
import com.panayotis.gnuplot.plot.DataSetPlot;
import com.panayotis.gnuplot.style.NamedPlotColor;
import com.panayotis.gnuplot.style.PlotStyle;
import com.panayotis.gnuplot.style.Style;
import com.panayotis.gnuplot.terminal.ImageTerminal;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import javax.imageio.ImageIO;
import org.cidarlab.gridtli.dom.Grid;
import org.cidarlab.gridtli.dom.Signal;
import org.cidarlab.gridtli.dom.Cell;
import org.cidarlab.gridtli.tli.TemporalLogicInference;

/**
 *
 * @author prash
 */
public class JavaPlotAdaptor {
 
    
    private static List<Point> getSignalJPlotPoints(Signal signal){
        List<Point> points = new ArrayList<Point>();
        for(org.cidarlab.gridtli.dom.Point point:signal.getPoints()){
            points.add(new Point(point.getX(),point.getY()));
        }
        return points;
    }
    
    private static List<Point> getSubGridJPlotPoints(Set<Cell> cells){
        List<Point> points = new ArrayList<Point>();
        for(Cell cell:cells){
            points.add(new Point(cell.getXOrigin(),cell.getYOrigin()));
        }
        return points;
    }
    
    private static NamedPlotColor getRandomColor(){
        Random rand = new Random();
        int i= rand.nextInt(NamedPlotColor.values().length);
        return NamedPlotColor.values()[i];
    }
    
    private static int getClusterIndex(List<Set<Signal>> clusters, Signal s){
        for(int i=0;i<clusters.size();i++){
            if(clusters.get(i).contains(s)){
                return i;
            }
        }
        return 0;
    }
    
    public static JavaPlot plotGrid(Grid grid, List<Set<Signal>> clusters){
        JavaPlot plot = new JavaPlot();
        PlotStyle ps = new PlotStyle();
        ps.setStyle(Style.DOTS);
        ps.setLineType(NamedPlotColor.BLACK);
        PointDataSet pdsgrid = new PointDataSet(getSubGridJPlotPoints(grid.getCell().keySet()));
        DataSetPlot dspgrid = new DataSetPlot(pdsgrid);
        dspgrid.setPlotStyle(ps);
        plot.addPlot(dspgrid);
        
        List<NamedPlotColor> clusterColors = new ArrayList<NamedPlotColor>();
        for (Set<Signal> cluster : clusters) {
            NamedPlotColor randColor = getRandomColor();
            while(clusterColors.contains(randColor)){
                randColor = randColor = getRandomColor();
            }
            clusterColors.add(randColor);
        }
        //clusterColors.add(NamedPlotColor.RED);
        //clusterColors.add(NamedPlotColor.BLUE);
        //clusterColors.add(NamedPlotColor.BLACK);
        
        for(Signal signal:grid.getSignals()){
            PlotStyle sps = new PlotStyle();
            sps.setStyle(Style.LINES);
            //System.out.println("Signal Index :: " + signal.getIndex() + ", Color :: " + clusterColors.get(getClusterIndex(clusters,signal)).name());
            //sps.setLineType(clusterColors.get(getClusterIndex(clusters,signal)));
            sps.setLineType(NamedPlotColor.BLACK);
            PointDataSet psd = new PointDataSet(getSignalJPlotPoints(signal));
            DataSetPlot dsp = new DataSetPlot(psd);
            dsp.setPlotStyle(sps);
            plot.addPlot(dsp);
        }
        int count =1;
        for(Cell cell: grid.getCell().keySet()){
            if(grid.isSpecificCellCovered(cell)){
                
                //System.out.println("Covered: " +  cell.getXOrigin()+","+cell.getYOrigin());
                String obj = "object " + count;
                String rect = "rect from " + cell.getXOrigin()+","+cell.getYOrigin() + " to " + (cell.getXOrigin() + grid.getXIncrement()) + "," + (cell.getYOrigin()+grid.getYIncrement()) + " fc rgb \"yellow\"";
                
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
    
    
    public static JavaPlot plotCluster(Grid grid, Set<Signal> cluster){
        JavaPlot plot = new JavaPlot();
        PlotStyle ps = new PlotStyle();
        ps.setStyle(Style.DOTS);
        ps.setLineType(NamedPlotColor.BLACK);
        PointDataSet pdsgrid = new PointDataSet(getSubGridJPlotPoints(grid.getCell().keySet()));
        DataSetPlot dspgrid = new DataSetPlot(pdsgrid);
        dspgrid.setPlotStyle(ps);
        plot.addPlot(dspgrid);
        
        Set<Cell> coveredCluster = TemporalLogicInference.getAllCoveredCells(cluster);
        
        for(Signal signal:cluster){
            PlotStyle sps = new PlotStyle();
            sps.setStyle(Style.LINES);
            //System.out.println("Signal Index :: " + signal.getIndex() + ", Color :: " + clusterColors.get(getClusterIndex(clusters,signal)).name());
            sps.setLineType(NamedPlotColor.RED);
            PointDataSet psd = new PointDataSet(getSignalJPlotPoints(signal));
            DataSetPlot dsp = new DataSetPlot(psd);
            dsp.setPlotStyle(sps);
            plot.addPlot(dsp);
        }
        int count =1;
        for(Cell cell: grid.getCell().keySet()){
            if(grid.isSpecificCellCovered(cell)){
                
                //System.out.println("Covered: " +  cell.getXOrigin()+","+cell.getYOrigin());
                String obj = "object " + count;
                if (coveredCluster.contains(cell)) {
                    String rect = "rect from " + cell.getXOrigin() + "," + cell.getYOrigin() + " to " + (cell.getXOrigin() + grid.getXIncrement()) + "," + (cell.getYOrigin() + grid.getYIncrement()) + " fc rgb \"yellow\"";
                    plot.set(obj, rect);
                    count++;

                }
    
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
    
    
    
    public static JavaPlot plotGrid(Grid grid){
        JavaPlot plot = new JavaPlot();
        PlotStyle ps = new PlotStyle();
        ps.setStyle(Style.DOTS);
        ps.setLineType(NamedPlotColor.BLACK);
        PointDataSet pdsgrid = new PointDataSet(getSubGridJPlotPoints(grid.getCell().keySet()));
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
        for(Cell cell: grid.getCell().keySet()){
            if(grid.isSpecificCellCovered(cell)){
                
                //System.out.println("Covered: " +  cell.getXOrigin()+","+cell.getYOrigin());
                String obj = "object " + count;
                String rect = "rect from " + cell.getXOrigin()+","+cell.getYOrigin() + " to " + (cell.getXOrigin() + grid.getXIncrement()) + "," + (cell.getYOrigin()+grid.getYIncrement()) + " fc rgb \"#B3B3FA\"";
                
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
    
    
    public static JavaPlot plotGrid_withTestingData(Grid grid, List<Signal> test){
        JavaPlot plot = new JavaPlot();
        PlotStyle ps = new PlotStyle();
        ps.setStyle(Style.DOTS);
        ps.setLineType(NamedPlotColor.BLACK);
        PointDataSet pdsgrid = new PointDataSet(getSubGridJPlotPoints(grid.getCell().keySet()));
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
        for(Signal signal:test){
            PlotStyle sps_test = new PlotStyle();
            sps_test.setStyle(Style.LINES);
            sps_test.setLineType(NamedPlotColor.RED);
            PointDataSet psd_test = new PointDataSet(getSignalJPlotPoints(signal));
            DataSetPlot dsp_test = new DataSetPlot(psd_test);
            dsp_test.setPlotStyle(sps_test);
            plot.addPlot(dsp_test);
        }
        int count =1;
        for(Cell cell: grid.getCell().keySet()){
            if(grid.isSpecificCellCovered(cell)){
                
                //System.out.println("Covered: " +  cell.getXOrigin()+","+cell.getYOrigin());
                String obj = "object " + count;
                String rect = "rect from " + cell.getXOrigin()+","+cell.getYOrigin() + " to " + (cell.getXOrigin() + grid.getXIncrement()) + "," + (cell.getYOrigin()+grid.getYIncrement()) + " fc rgb \"#B3B3FA\"";
                
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
    
    public static JavaPlot plotGrid_withTestingData(Grid grid, List<Signal> satisfy, List<Signal> notSatisfy){
        JavaPlot plot = new JavaPlot();
        PlotStyle ps = new PlotStyle();
        ps.setStyle(Style.DOTS);
        ps.setLineType(NamedPlotColor.BLACK);
        PointDataSet pdsgrid = new PointDataSet(getSubGridJPlotPoints(grid.getCell().keySet()));
        DataSetPlot dspgrid = new DataSetPlot(pdsgrid);
        dspgrid.setPlotStyle(ps);
        plot.addPlot(dspgrid);
        
        for(Signal signal:satisfy){
            PlotStyle sps = new PlotStyle();
            sps.setStyle(Style.LINES);
            sps.setLineType(NamedPlotColor.BLACK);
            PointDataSet psd = new PointDataSet(getSignalJPlotPoints(signal));
            DataSetPlot dsp = new DataSetPlot(psd);
            dsp.setPlotStyle(sps);
            plot.addPlot(dsp);
        }
        for(Signal signal:notSatisfy){
            PlotStyle sps_test = new PlotStyle();
            sps_test.setStyle(Style.LINES);
            sps_test.setLineType(NamedPlotColor.RED);
            sps_test.setLineWidth(3);
            PointDataSet psd_test = new PointDataSet(getSignalJPlotPoints(signal));
            DataSetPlot dsp_test = new DataSetPlot(psd_test);
            dsp_test.setPlotStyle(sps_test);
            plot.addPlot(dsp_test);
        }
        int count =1;
        for(Cell cell: grid.getCell().keySet()){
            if(grid.isSpecificCellCovered(cell)){
                
                //System.out.println("Covered: " +  cell.getXOrigin()+","+cell.getYOrigin());
                String obj = "object " + count;
                String rect = "rect from " + cell.getXOrigin()+","+cell.getYOrigin() + " to " + (cell.getXOrigin() + grid.getXIncrement()) + "," + (cell.getYOrigin()+grid.getYIncrement()) + " fc rgb \"#B3B3FA\"";
                
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
    
    public static JavaPlot plotSignals(Grid grid){
        JavaPlot plot = new JavaPlot();
        PlotStyle ps = new PlotStyle();
        ps.setStyle(Style.DOTS);
        ps.setLineType(NamedPlotColor.BLACK);
        PointDataSet pdsgrid = new PointDataSet(getSubGridJPlotPoints(grid.getCell().keySet()));
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
        PointDataSet pdsgrid = new PointDataSet(getSubGridJPlotPoints(grid.getCell().keySet()));
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
    
    public static JavaPlot visualizeCells(Set<Cell> cells){
        JavaPlot plot = new JavaPlot();
        PlotStyle ps = new PlotStyle();
        ps.setStyle(Style.DOTS);
        PointDataSet pds = new PointDataSet(getSubGridJPlotPoints(cells));
        DataSetPlot dsp = new DataSetPlot(pds);
        dsp.setPlotStyle(ps);
        plot.addPlot(dsp);
        
        plot.getAxis("x").setLabel("x");
        plot.getAxis("y").setLabel("y");
        plot.setTitle("Cells");
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
