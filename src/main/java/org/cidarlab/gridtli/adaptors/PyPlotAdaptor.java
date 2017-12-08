/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.gridtli.adaptors;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.cidarlab.gridtli.dom.Cell;
import org.cidarlab.gridtli.dom.Grid;
import org.cidarlab.gridtli.dom.Signal;

/**
 *
 * @author prash
 */
public class PyPlotAdaptor {
    
    private static List<String> generateCellScript(Cell cell, int index, double xinc, double yinc, char color){
        List<String> lines = new ArrayList<String>();
        
        String cx = "cx" + index + "=[";
        String cy = "cy" + index + "=[";
        
        BigDecimal xorg = BigDecimal.valueOf(cell.getXOrigin());
        BigDecimal yorg = BigDecimal.valueOf(cell.getYOrigin());
        
        cx += cell.getXOrigin() + ",";
        cy += cell.getYOrigin() + ",";
        
        cx += (xorg.add(BigDecimal.valueOf(xinc)).toPlainString()) + ",";
        cy += cell.getYOrigin() + ",";
        
        cx += (xorg.add(BigDecimal.valueOf(xinc))).toPlainString() + ",";
        cy += (yorg.add(BigDecimal.valueOf(yinc))).toPlainString() + ",";
        
        cx += cell.getXOrigin();
        cy += (yorg.add(BigDecimal.valueOf(yinc))).toPlainString();
        
        
        cx += "]";
        cy += "]";
        
        lines.add(cx);
        lines.add(cy);
        lines.add("plt.fill_between(cx" + index +  ",cy" + index + ",facecolor='"+color+"',alpha=0.4)");
        lines.add("\n");
        return lines;
    }
    
    private static List<String> generateSignalScript(List<Signal> signals){
        List<String> lines = new ArrayList<String>();
        for(int i=0;i<signals.size();i++){
            Signal s = signals.get(i);
            String sx = "sx" + i + " = [";
            String sy = "sy" + i + " = [";
            sx += s.getPoints().get(0).getX();
            sy += s.getPoints().get(0).getY();
            
            for(int j=1;j<s.getPoints().size();j++){
                sx += ("," + s.getPoints().get(j).getX());
                sy += ("," + s.getPoints().get(j).getY());
            }
            sx += "]";
            sy += "]";
            lines.add(sx);
            lines.add(sy);
            lines.add("plt.plot(sx" + i + ",sy" + i + ",color='r',linestyle='solid')" );
            lines.add("\n");
            
        }
        
        return lines;
    }
    
    
    public static List<String> generateSignalPlotScript(List<Signal> signals){
        
        List<String> lines = new ArrayList<String>();
        
        lines.add("import matplotlib.pyplot as plt");
        lines.add("import matplotlib.patches as patches\n");
        
        lines.add("fig = plt.figure()\n");
        
        //Get Signal Name
        String signalname = "signal";
        if(signals.get(0).getPoints().get(0).getYSignal() != null){
            if(!signals.get(0).getPoints().get(0).getYSignal().isEmpty()){
                signalname = signals.get(0).getPoints().get(0).getYSignal();
            }
        }
        lines.addAll(generateSignalScript(signals));
        
        
        lines.add("plt.xlabel(\"time\")");
        lines.add("plt.ylabel(\""+signalname+"\")");
        lines.add("fig.savefig('graph.png', dpi=300)");
        
        
        return lines;
    }
    
    
    public static List<String> generatePlotScript(Grid grid){
        List<String> lines = new ArrayList<String>();
        
        //Headers
        lines.add("import matplotlib.pyplot as plt");
        lines.add("import matplotlib.patches as patches\n");
        
        lines.add("fig = plt.figure()\n");
        
        //Get Signal Name
        String signalname = "signal";
        if(grid.getSignals().get(0).getPoints().get(0).getYSignal() != null){
            if(!grid.getSignals().get(0).getPoints().get(0).getYSignal().isEmpty()){
                signalname = grid.getSignals().get(0).getPoints().get(0).getYSignal();
            }
        }
        
        //Convert Cells to points to plot.
        for(int i=0;i<grid.getCell().keySet().size();i++){
            List<Cell> cells = new ArrayList<Cell>(grid.getCell().keySet());
            if(grid.getCell().get(cells.get(i))){
                lines.addAll(generateCellScript(cells.get(i),i,grid.getXIncrement(),grid.getYIncrement(),'b'));
            } else {
                lines.addAll(generateCellScript(cells.get(i),i,grid.getXIncrement(),grid.getYIncrement(),'w'));
            }
        }
        
        //Convert Signals to points to plot.
        lines.addAll(generateSignalScript(grid.getSignals()));
        
        
        lines.add("plt.xlabel(\"time\")");
        lines.add("plt.ylabel(\""+signalname+"\")");
        lines.add("fig.savefig('graph.png', dpi=300)");
        
        
        return lines;
        
    }
    
}
