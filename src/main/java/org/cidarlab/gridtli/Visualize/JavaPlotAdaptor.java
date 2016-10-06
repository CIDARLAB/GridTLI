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
import org.cidarlab.gridtli.DOM.SubGrid;

/**
 *
 * @author prash
 */
public class JavaPlotAdaptor {
 
    
    public static List<Point> getSubGridJPlotPoints(Set<SubGrid> subgrids){
        List<Point> points = new ArrayList<Point>();
        for(SubGrid subgrid:subgrids){
            points.add(new Point(subgrid.getXOrigin(),subgrid.getYOrigin()));
        }
        return points;
    }
    
    public static JavaPlot visualizeSubGrid(Set<SubGrid> subgrids){
        JavaPlot plot = new JavaPlot();
        PlotStyle ps = new PlotStyle();
        ps.setStyle(Style.DOTS);
        PointDataSet pds = new PointDataSet(getSubGridJPlotPoints(subgrids));
        DataSetPlot dsp = new DataSetPlot(pds);
        plot.addPlot(dsp);
        
        plot.getAxis("x").setLabel("x");
        plot.getAxis("y").setLabel("y");
        plot.setTitle("SubGrid");
        plot.set("xzeroaxis", "");
        plot.set("yzeroaxis", "");
        
        System.out.println(plot.getCommands());
        
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
