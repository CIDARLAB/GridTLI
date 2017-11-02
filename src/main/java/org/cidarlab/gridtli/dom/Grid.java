/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.gridtli.dom;

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
    private Map<Cell,Boolean> cell;

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
        setCellCovers();
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
        
        setCellCovers();
        //System.out.println("Sub Grid Covers set complete");
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
        setCellCovers();
    }

    public static Set<Cell> createQuadTreeCell(double xmin, double ymin, double xInc, double yInc, double xthreshold, double ythreshold){
        Set<Cell> set = new HashSet<Cell>();
        set.add(new Cell(xmin,ymin,xInc,yInc));
        double yhalf = yInc/2;
        if( yhalf >= ythreshold){
            set.add(new Cell(xmin,ymin,xInc,yhalf));
            set.add(new Cell(xmin,ymin + yhalf,xInc,yhalf));
        } 
        Set<Cell> xdiv = new HashSet<Cell>();
        
        for(Cell cell:set){
            xdiv.addAll(divideX(cell.getXOrigin(),cell.getYOrigin(),cell.getXInc(),cell.getYInc(),xthreshold,ythreshold));
        }
        set.addAll(xdiv);
        return set;
    }
    
    private static Set<Cell> divideX(double xmin, double ymin, double xInc, double yInc, double xthreshold, double ythreshold){
        Set<Cell> set = new HashSet<Cell>();
        double xhalf = xInc/2;
        double yhalf = yInc/2;
        if(xhalf >= xthreshold){
            set.add(new Cell(xmin,ymin,xhalf,yInc));
            set.add(new Cell(xmin+xhalf,ymin,xhalf,yInc));
        }
        Set<Cell> ydiv = new HashSet<Cell>();
        if(yhalf >= ythreshold){
            for(Cell sgrid:set){
                ydiv.addAll(createQuadTreeCell(sgrid.getXOrigin(),sgrid.getYOrigin(),sgrid.getXInc(),sgrid.getYInc(),xthreshold,ythreshold));
            }
            set.addAll(ydiv);
        }
        return set;
    }
    
    private void createSubGrid() {
        this.cell = new HashMap<Cell,Boolean>();

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
            List<Cell> xPOSyPOS = new ArrayList<Cell>();
            if (xStart > 0) {
                xPOSstart = xStart;
            }
            if (yStart > 0) {
                yPOSstart = yStart;
            }
//            System.out.println("X Start :: " + xPOSstart);
//            System.out.println("X End :: " + this.xUpperLimit);
//            System.out.println("Y Start :: " + yPOSstart);
//            System.out.println("Y End :: " + this.yUpperLimit);
//            System.out.println("X Increment :: " + this.xIncrement);
//            System.out.println("Y Increment :: " + this.yIncrement);
//            System.out.println("First quadrant");
            for (double i = xPOSstart; i < this.xUpperLimit; i += this.xIncrement) {
                for (double j = yPOSstart; j < this.yUpperLimit; j += this.yIncrement) {
//                    System.out.println("i = " + i + ", j = " + j);
                    //System.out.println("Here");
                    //xPOSyPOS.add(new Cell(i, j));
                    this.cell.put(new Cell(i, j), false);
                }
            }
//            System.out.println("Second quadrant");
            //Second quadrant xPOS yNEG
            List<Cell> xPOSyNEG = new ArrayList<Cell>();
            if (xStart > 0) {
                xPOSstart = xStart;
            }
            if (this.yUpperLimit < 0) {
                for (double i = xPOSstart; i < this.xUpperLimit; i += this.xIncrement) {
                    for (double j = this.yLowerLimit; j < this.yUpperLimit; j += this.yIncrement) {
                        //xPOSyNEG.add(new Cell(i, j));
                        this.cell.put(new Cell(i, j), false);
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
                        //xPOSyNEG.add(new Cell(i, temp.get(j)));
                        this.cell.put(new Cell(i, temp.get(j)), false);
                    }
                }
            }
            
            
        }//End of Else condition of ifCentered
        

    }
    
    public void assignSignalIndex(){
        for(int i=0;i<this.signals.size();i++){
            this.signals.get(i).setIndex(i);
        }
    }
    
    
    public boolean isSpecificCellCovered(double x, double y){
        return Grid.this.isSpecificCellCovered(new Cell(x,y));
    }
    
    public boolean isSpecificCellCovered(Cell _cell){
        if(this.cell.containsKey(_cell)){
            return this.cell.get(_cell);
        }
        return false;
    }
    
    public Cell getSpecificCell(double x, double y){
        return getSpecificCell(new Cell(x,y));
    }
    
    public Cell getSpecificCell(Cell _cell){
        for(Cell cell : this.cell.keySet()){
            if(cell.equals(_cell)){
                return cell;
            }
        }
        return null;
    }
    
    private void setCellCovers(){
        //System.out.println("In Set Sub Grid Covers");
        //System.out.println("cell size :: " + this.cell.size());
        //System.out.println("Number of Signals :: " + this.signals.size());
        for(Cell cell: this.cell.keySet()){
            for(Signal signal:this.signals){
                List<Point> possiblePoints = signal.getGridPoints(cell.getXOrigin(), this.xIncrement);
                for(int i=0;i< possiblePoints.size()-1; i++){
                    if(inGrid(cell.getXOrigin(), this.xIncrement , cell.getYOrigin(), this.yIncrement, possiblePoints.get(i), possiblePoints.get(i+1))){
                        //subgrid.setCovered(true);
                        signal.addCell(cell);
                        this.cell.put(cell, true);
                        
                        if(signal.getCellCovered().size() == 1){
                            signal.setStartingCell(cell);
                        } else {
                            if(cell.smallerThan(signal.getStartingCell())){
                                signal.setStartingCell(cell);
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
    
    public double getCellMinX(){
        double min = this.xUpperLimit;
        for(Cell cell: this.cell.keySet()){
            if(cell.getXOrigin() <= min){
                min = cell.getXOrigin();
            }
        }
        return min;
    }
    
    public double getCellMaxX(){
        double max = this.xLowerLimit;
        for(Cell cell: this.cell.keySet()){
            if(cell.getXOrigin() >= max){
                max = cell.getXOrigin();
            }
        }
        return max;
    }    
    
    public double getCellMinY(){
        double min = this.yUpperLimit;
        for(Cell cell: this.cell.keySet()){
            if(cell.getYOrigin() <= min){
                min = cell.getYOrigin();
            }
        }
        return min;
    }
    
    public double getCellMaxY(){
        double max = this.yLowerLimit;
        for(Cell cell: this.cell.keySet()){
            if(cell.getYOrigin() >= max){
                max = cell.getYOrigin();
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
