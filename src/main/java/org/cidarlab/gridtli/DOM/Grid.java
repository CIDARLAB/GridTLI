/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.gridtli.DOM;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author prash
 */
public class Grid {

    @Getter
    @Setter //Change this. This will trigger a ripple effect.
    private List<Signal> signals;

    @Getter
    private Map<SubGrid,Boolean> subGrid;

    @Getter
    @Setter //Change this. This will trigger a ripple effect.
    private double xIncrement;

    @Getter
    @Setter //Change this. This will trigger a ripple effect.
    private double yIncrement;

    @Getter
    private double xUpperLimit;

    @Getter
    private double xLowerLimit;

    @Getter
    private double yUpperLimit;

    @Getter
    private double yLowerLimit;

    @Getter
    private boolean centered;

    @Getter
    private boolean xCentered;

    @Getter
    private boolean yCentered;
    
    @Getter
    private double xStart;
    
    @Getter
    private double yStart;
    
    @Getter
    private String xSignal;
    
    @Getter
    private String ySignal;

    public Grid(List<Signal> _signals) {
        this.signals = _signals;
        
        this.xSignal = signals.get(0).getPoints().get(0).getXSignal();
        this.ySignal = signals.get(0).getPoints().get(0).getYSignal();
        
        this.xIncrement = 1.0;
        this.yIncrement = 1.0;

        this.xUpperLimit = this.getxMax() + this.xIncrement;  //Maybe 2*increment?    
        this.xLowerLimit = this.getxMin() - this.xIncrement;

        this.yUpperLimit = this.getyMax() + this.yIncrement;
        this.yLowerLimit = this.getyMin() - this.yIncrement;

        this.centered = false;
        
        assignSignalIndex();
        
        createSubGrid();
        setSubGridCovers();
    }

    public Grid(List<Signal> _signals, double _xIncrement, double _yIncrement) {
        this.signals = _signals;
        this.xIncrement = _xIncrement;
        this.yIncrement = _yIncrement;
        
        this.xSignal = signals.get(0).getPoints().get(0).getXSignal();
        this.ySignal = signals.get(0).getPoints().get(0).getYSignal();
        
        this.xUpperLimit = this.getxMax() + this.xIncrement;  //Maybe 2*increment?
        this.xLowerLimit = this.getxMin(); // - this.xIncrement;

        this.yUpperLimit = this.getyMax() + this.yIncrement;
        this.yLowerLimit = this.getyMin() - this.yIncrement;

        this.centered = false;
        
        assignSignalIndex();
        
        //System.out.println("Start Creating Sub Grid");
        createSubGrid();
        
        //System.out.println("Sub Grid Creation complete. Now Setting Sub Grid Covers");
        
        setSubGridCovers();
        //System.out.println("Sub Grid Covers set");
    }

    public Grid(List<Signal> _signals, double _xIncrement, double _yIncrement, double _xUpperLimit, double _xLowerLimit, double _yUpperLimit, double _yLowerLimit) {
        this.signals = _signals;
        this.xIncrement = _xIncrement;
        this.yIncrement = _yIncrement;

        this.xSignal = signals.get(0).getPoints().get(0).getXSignal();
        this.ySignal = signals.get(0).getPoints().get(0).getYSignal();
        
        this.xUpperLimit = _xUpperLimit;
        this.xLowerLimit = _xLowerLimit;

        this.yUpperLimit = _yUpperLimit;
        this.yLowerLimit = _yLowerLimit;;

        this.centered = false;

        assignSignalIndex();
        
        createSubGrid();
        setSubGridCovers();
    }

    public static Set<SubGrid> createQuadTreeSubGrid(double xmin, double ymin, double xInc, double yInc, double xthreshold, double ythreshold){
        Set<SubGrid> set = new HashSet<SubGrid>();
        set.add(new SubGrid(xmin,ymin,xInc,yInc));
        double yhalf = yInc/2;
        if( yhalf >= ythreshold){
            set.add(new SubGrid(xmin,ymin,xInc,yhalf));
            set.add(new SubGrid(xmin,ymin + yhalf,xInc,yhalf));
        } 
        Set<SubGrid> xdiv = new HashSet<SubGrid>();
        
        for(SubGrid sgrid:set){
            xdiv.addAll(divideX(sgrid.getXOrigin(),sgrid.getYOrigin(),sgrid.getXInc(),sgrid.getYInc(),xthreshold,ythreshold));
        }
        set.addAll(xdiv);
        return set;
    }
    
    private static Set<SubGrid> divideX(double xmin, double ymin, double xInc, double yInc, double xthreshold, double ythreshold){
        Set<SubGrid> set = new HashSet<SubGrid>();
        double xhalf = xInc/2;
        double yhalf = yInc/2;
        if(xhalf >= xthreshold){
            set.add(new SubGrid(xmin,ymin,xhalf,yInc));
            set.add(new SubGrid(xmin+xhalf,ymin,xhalf,yInc));
        }
        Set<SubGrid> ydiv = new HashSet<SubGrid>();
        if(yhalf >= ythreshold){
            for(SubGrid sgrid:set){
                ydiv.addAll(createQuadTreeSubGrid(sgrid.getXOrigin(),sgrid.getYOrigin(),sgrid.getXInc(),sgrid.getYInc(),xthreshold,ythreshold));
            }
            set.addAll(ydiv);
        }
        return set;
    }
    
    private void createSubGrid() {
        this.subGrid = new HashMap<SubGrid,Boolean>();

        if (this.centered) {
            
            this.xCentered = true;
            this.yCentered = true;
            
            this.xStart = 0.0;
            this.yStart = 0.0;
            
            
        } else {
            double xStart = 0;
            double yStart = 0;
            this.xCentered = true;
            this.yCentered = true;
            
//            if (this.xUpperLimit < 0 || this.xLowerLimit > 0) {
//                xStart = this.xLowerLimit;
//                this.xCentered = false;
//                this.xStart = xStart;
//            } //Can't happen.
            
            if (this.yUpperLimit < 0 || this.yLowerLimit > 0) {
                yStart = this.yLowerLimit;
                this.yCentered = false;
                this.yStart = yStart;
            }

            double xPOSstart = 0;
            double yPOSstart = 0;

            //First quadrant xPOS yPOS
            List<SubGrid> xPOSyPOS = new ArrayList<SubGrid>();
            if (xStart > 0) {
                xPOSstart = xStart;
            }
            if (yStart > 0) {
                yPOSstart = yStart;
            }
            for (double i = xPOSstart; i < this.xUpperLimit; i += this.xIncrement) {
                for (double j = yPOSstart; j < this.yUpperLimit; j += this.yIncrement) {
                    xPOSyPOS.add(new SubGrid(i, j));
                    this.subGrid.put(new SubGrid(i, j), false);
                }
            }
            //Second quadrant xPOS yNEG
            List<SubGrid> xPOSyNEG = new ArrayList<SubGrid>();
            if (xStart > 0) {
                xPOSstart = xStart;
            }
            if (this.yUpperLimit < 0) {
                for (double i = xPOSstart; i < this.xUpperLimit; i += this.xIncrement) {
                    for (double j = this.yLowerLimit; j < this.yUpperLimit; j += this.yIncrement) {
                        xPOSyNEG.add(new SubGrid(i, j));
                        this.subGrid.put(new SubGrid(i, j), false);
                    }
                }
            } else {
                //List<SubGrid> temp = new ArrayList<SubGrid>();
                List<Double> temp = new ArrayList<Double>();
                for (double j = 0; j > this.yLowerLimit; j -= this.yIncrement) {
                    temp.add(j);
                }

                for (double i = xPOSstart; i < this.xUpperLimit; i += this.xIncrement) {
                    for (int j = temp.size() - 1; j >= 0; j--) {
                        xPOSyNEG.add(new SubGrid(i, temp.get(j)));
                        this.subGrid.put(new SubGrid(i, temp.get(j)), false);
                    }
                }
            }

            //<editor-fold desc="When Time is negative. Not needed" defaultstate="collapsed"> 
//            //Third quadrant xNEG yNEG 
//            List<SubGrid> xNEGyNEG = new ArrayList<SubGrid>();
//            if (this.xUpperLimit < 0) {
//                if (this.yUpperLimit < 0) {
//                    for (double i = this.xLowerLimit; i <= this.xUpperLimit; i += this.xIncrement) {
//                        for (double j = this.yLowerLimit; j <= this.yUpperLimit; j += this.yIncrement) {
//                            xNEGyNEG.add(new SubGrid(i, j));
//                            this.subGrid.put(new SubGrid(i, j), false);
//                        }
//                    }
//                } else {
//                    List<Double> temp = new ArrayList<Double>();
//                    for (double j = 0; j >= this.yLowerLimit; j -= this.yIncrement) {
//                        temp.add(j);
//                    }
//                    for (double i = this.xLowerLimit; i <= this.xUpperLimit; i += this.xIncrement) {
//                        for (int j = temp.size() - 1; j >= 0; j--) {
//                            xNEGyNEG.add(new SubGrid(i, temp.get(j)));
//                            this.subGrid.put(new SubGrid(i, temp.get(j)), false);
//                            
//                        }
//                    }
//                }
//            } else {
//                List<Double> tempx = new ArrayList<Double>();
//                for (double i = 0; i >= this.xLowerLimit; i -= this.xIncrement) {
//                    tempx.add(i);
//                }
//                if (this.yUpperLimit < 0) {
//                    for (int i = tempx.size() - 1; i >= 0; i--) {
//                        for (double j = this.yLowerLimit; j <= this.yUpperLimit; j += this.yIncrement) {
//                            xNEGyNEG.add(new SubGrid(tempx.get(i), j));
//                            this.subGrid.put(new SubGrid(tempx.get(i), j), false);
//                            
//                        }
//                    }
//                } else {
//                    List<Double> tempy = new ArrayList<Double>();
//                    for (double j = 0; j >= this.yLowerLimit; j -= this.yIncrement) {
//                        tempy.add(j);
//                    }
//                    for (int i = tempx.size() - 1; i >= 0; i--) {
//                        for (int j = tempy.size() - 1; j >= 0; j--) {
//                            xNEGyNEG.add(new SubGrid(tempx.get(i), tempy.get(j)));
//                            this.subGrid.put(new SubGrid(tempx.get(i), tempy.get(j)), false);
//                            
//                        }
//                    }
//                }
//            }
//
//            //Fourth quadrant xNEG yPOS
//            List<SubGrid> xNEGyPOS = new ArrayList<SubGrid>();
//            if (yStart > 0) {
//                yPOSstart = yStart;
//            }
//            if (this.xUpperLimit < 0) {
//                for(double i=this.xLowerLimit; i <= this.xUpperLimit; i+= this.xIncrement){
//                    for(double j = yPOSstart; j <= this.yUpperLimit; j += this.yIncrement){
//                        xNEGyPOS.add(new SubGrid(i,j));
//                        this.subGrid.put(new SubGrid(i,j), false);
//                            
//                    }
//                }
//            } else {
//                List<Double> tempx = new ArrayList<Double>();
//                for(double i=0; i >= this.xLowerLimit; i -= this.xIncrement){
//                    tempx.add(i);
//                }
//                for(int i = tempx.size()-1; i >=0; i--){
//                    for(double j = yPOSstart; j <= this.yUpperLimit; j += this.yIncrement){
//                        xNEGyPOS.add(new SubGrid(tempx.get(i),j));
//                        this.subGrid.put(new SubGrid(tempx.get(i),j), false);
//                            
//                    }
//                }
//            }
            //</editor-fold>
            
            
        }//End of Else condition of ifCentered
        

    }
    
    public void assignSignalIndex(){
        for(int i=0;i<this.signals.size();i++){
            this.signals.get(i).setIndex(i);
        }
    }
    
    
    public boolean isSpecificSubGridCovered(double x, double y){
        return isSpecificSubGridCovered(new SubGrid(x,y));
    }
    
    public boolean isSpecificSubGridCovered(SubGrid _sgrid){
        if(this.subGrid.containsKey(_sgrid)){
            return this.subGrid.get(_sgrid);
        }
        return false;
    }
    
    public SubGrid getSpecificSubGrid(double x, double y){
        return getSpecificSubGrid(new SubGrid(x,y));
    }
    
    public SubGrid getSpecificSubGrid(SubGrid _sgrid){
        for(SubGrid sgrid : this.subGrid.keySet()){
            if(sgrid.equals(_sgrid)){
                return sgrid;
            }
        }
        return null;
    }
    
    private void setSubGridCovers(){
        //System.out.println("In Set Sub Grid Covers");
        //System.out.println("subgrid size :: " + this.subGrid.size());
        //System.out.println("Number of Signals :: " + this.signals.size());
        for(SubGrid subgrid: this.subGrid.keySet()){
            for(Signal signal:this.signals){
                List<Point> possiblePoints = signal.getGridPoints(subgrid.getXOrigin(), this.xIncrement);
                for(int i=0;i< possiblePoints.size()-1; i++){
                    if(inGrid(subgrid.getXOrigin(), this.xIncrement , subgrid.getYOrigin(), this.yIncrement, possiblePoints.get(i), possiblePoints.get(i+1))){
                        //subgrid.setCovered(true);
                        signal.addSubGrid(subgrid);
                        this.subGrid.put(subgrid, true);
                        
                        if(signal.getSubGridCovered().size() == 1){
                            signal.setStartingGrid(subgrid);
                        } else {
                            if(subgrid.smallerThan(signal.getStartingGrid())){
                                signal.setStartingGrid(subgrid);
                            }
                        }
                    }
                }
            }
        }
    }
    
    public double getxMax() {
        double max = this.signals.get(0).getxMax();
        for (Signal signal : this.signals) {
            double signalXmax = signal.getxMax();
            if (signalXmax > max) {
                max = signalXmax;
            }
        }
        return max;
    }

    public double getxMin() {
        double min = this.signals.get(0).getxMin();
        for (Signal signal : this.signals) {
            double signalXmin = signal.getxMin();
            if (signalXmin < min) {
                min = signalXmin;
            }
        }
        return min;
    }

    public double getyMax() {
        double max = this.signals.get(0).getyMax();
        for (Signal signal : this.signals) {
            double signalYmax = signal.getyMax();
            if (signalYmax > max) {
                max = signalYmax;
            }
        }
        return max;
    }

    public double getyMin() {
        double min = this.signals.get(0).getyMin();
        for (Signal signal : this.signals) {
            double signalYmin = signal.getyMin();
            if (signalYmin < min) {
                min = signalYmin;
            }
        }
        return min;
    }
    
    public double getSubGridMinX(){
        double min = this.xUpperLimit;
        for(SubGrid sgrid: this.subGrid.keySet()){
            if(sgrid.getXOrigin() <= min){
                min = sgrid.getXOrigin();
            }
        }
        return min;
    }
    
    public double getSubGridMaxX(){
        double max = this.xLowerLimit;
        for(SubGrid sgrid: this.subGrid.keySet()){
            if(sgrid.getXOrigin() >= max){
                max = sgrid.getXOrigin();
            }
        }
        return max;
    }    
    
    public double getSubGridMinY(){
        double min = this.yUpperLimit;
        for(SubGrid sgrid: this.subGrid.keySet()){
            if(sgrid.getYOrigin() <= min){
                min = sgrid.getYOrigin();
            }
        }
        return min;
    }
    
    public double getSubGridMaxY(){
        double max = this.yLowerLimit;
        for(SubGrid sgrid: this.subGrid.keySet()){
            if(sgrid.getYOrigin() >= max){
                max = sgrid.getYOrigin();
            }
        }
        return max;
    }
    
    public boolean inGrid(double xOr, double xInc, double yOr, double yInc, Point p1, Point p2) {

        //Edge case
        if ((p1.getX() < xOr) && (p2.getX() < xOr)) {
            return false;
        }
        if ((p1.getX() > (xOr + xInc)) && (p2.getX() > (xOr + xInc))) {
            return false;
        }
        if ((p1.getY() < yOr) && (p2.getY() < yOr)) {
            return false;
        }
        if ((p1.getY() > (yOr + yInc)) && (p2.getY() > (yOr + yInc))) {
            return false;
        }

//        //Case 1 x = x1;
//        if (p1.getX() == p2.getX()) {
//            if (p1.getX() >= xOr && p1.getX() < (xOr + xInc)) {
//                return true;
//            }
//            return false;
//        }

        //Case 2 y = y1;
        if (p1.getY() == p2.getY()) {
            if (p1.getY() >= yOr && p1.getY() < (yOr + yInc)) {
                return true;
            }
            return false;
        }

        //Case 3 y = mx +c
        //Case 3a xOr
        double yXor = (((p2.getY() - p1.getY()) / (p2.getX() - p1.getX())) * (xOr - p1.getX())) + p1.getY();
        if ((yXor >= yOr) && (yXor < (yOr + yInc))) {
            return true;
        }
        //Case 3b xOr+xInc
        double yXorInc = (((p2.getY() - p1.getY()) / (p2.getX() - p1.getX())) * ((xOr + xInc) - p1.getX())) + p1.getY();
        if ((yXorInc >= yOr) && (yXorInc < (yOr + yInc))) {
            return true;
        }
        //Case 3c yOr
        double xYor = (((p2.getX() - p1.getX()) / (p2.getY() - p1.getY())) * (yOr - p1.getY())) + p1.getX();
        if ((xYor >= xOr) && (xYor < (xOr + xInc))) {
            return true;
        }
        //Case 3d yOr+yInc
        double xYorInc = (((p2.getX() - p1.getX()) / (p2.getY() - p1.getY())) * ((yOr + yInc) - p1.getY())) + p1.getX();
        if ((xYorInc >= xOr) && (xYorInc < (xOr + xInc))) {
            return true;
        }
        return false;
    }

}
