/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.gridtli.DOM;

import java.util.ArrayList;
import java.util.List;
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
    private List<SubGrid> subGrid;
    
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
    
    public Grid(List<Signal> _signals){
        this.signals = _signals;
        this.xIncrement = 1.0;
        this.yIncrement = 1.0;
        
        this.xUpperLimit = this.getxMax() + this.xIncrement;  //Maybe 2*increment?    
        this.xLowerLimit = this.getxMin() - this.xIncrement;
        
        this.yUpperLimit = this.getyMax() + this.yIncrement;
        this.yLowerLimit = this.getyMin() - this.yIncrement;
        
        createSubGrid();
    }
    
    public Grid(List<Signal> _signals, double _xIncrement, double _yIncrement){
        this.signals = _signals;
        this.xIncrement = _xIncrement;
        this.yIncrement = _yIncrement;
        
        this.xUpperLimit = this.getxMax() + this.xIncrement;  //Maybe 2*increment?
        this.xLowerLimit = this.getxMin() - this.xIncrement;
        
        this.yUpperLimit = this.getyMax() + this.yIncrement;
        this.yLowerLimit = this.getyMin() - this.yIncrement;
        
        createSubGrid();
    }
    
    public Grid(List<Signal> _signals, double _xIncrement, double _yIncrement, double _xUpperLimit, double _xLowerLimit, double _yUpperLimit, double _yLowerLimit){
        this.signals = _signals;
        this.xIncrement = _xIncrement;
        this.yIncrement = _yIncrement;
        
        this.xUpperLimit = _xUpperLimit;
        this.xLowerLimit = _xLowerLimit;
        
        this.yUpperLimit = _yUpperLimit;
        this.yLowerLimit = _yLowerLimit;;
        
        createSubGrid();
    }
    
    
    /*public Grid(Grid copy){
        this.signals = new ArrayList<Signal>(copy.signals);
        this.xIncrement = copy.xIncrement;
        this.xUpperLimit = copy.xUpperLimit;
        this.xLowerLimit = copy.xLowerLimit;
        this.yIncrement = copy.yIncrement;
        this.yUpperLimit = copy.yUpperLimit;
        this.yLowerLimit = copy.yLowerLimit;
    }*/
    
    public void createSubGrid(){
        this.subGrid = new ArrayList<SubGrid>();
        
        if(this.centered){
            
        } else {
            double xStart = 0;
            double yStart = 0;
            if (this.xUpperLimit < 0 || this.xLowerLimit > 0) {
                xStart = this.xLowerLimit;
            }
            if (this.yUpperLimit < 0 || this.yLowerLimit > 0) {
                yStart = this.yLowerLimit;
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
            for (double i = xPOSstart; i <= this.xUpperLimit; i += this.xIncrement) {
                for (double j = yPOSstart; i <= this.yUpperLimit; i += this.yIncrement) {
                    xPOSyPOS.add(new SubGrid(i, j));
                }
            }
            //Second quadrant xPOS yNEG
            List<SubGrid> xPOSyNEG = new ArrayList<SubGrid>();
            if (xStart > 0) {
                xPOSstart = xStart;
            }
            if(this.yUpperLimit <  0){
                for(double i = xPOSstart; i <= this.xUpperLimit; i+= this.xIncrement){
                    for(double j=this.yLowerLimit; j<= this.yUpperLimit ; j+= this.yIncrement){
                        xPOSyNEG.add(new SubGrid(i,j));
                    }
                }
            } else {
                if (xStart > 0) {
                    xPOSstart = xStart;
                }
                List<SubGrid> temp = new ArrayList<SubGrid>();
                for(double i = xPOSstart; i <= this.xUpperLimit; i+= this.xIncrement){
                    for(double j =0; j>= this.yLowerLimit;j -= this.yIncrement){
                        temp.add(new SubGrid(i,j));
                    }
                }
                for(int i= temp.size()-1; i>= 0 ; i--){
                    xPOSyNEG.add(temp.get(i));
                }
            }
            
            //Third quadrant xNEG yNEG 
            List<SubGrid> xNEGyNEG = new ArrayList<SubGrid>();

            //Fourth quadrant xNEG yPOS
            List<SubGrid> xNEGyPOS = new ArrayList<SubGrid>();
        }
        
        
        
        
    }
    
    public double getxMax(){
        double max = Double.MIN_VALUE;
        for(Signal signal:this.signals){
            double signalXmax = signal.getxMax();
            if(max > signalXmax){
                max = signalXmax;
            }
        }
        return max;
    }
    
    public double getxMin(){
        double min = Double.MAX_VALUE;
        for(Signal signal:this.signals){
            double signalXmin = signal.getxMin();
            if(min > signalXmin){
                min = signalXmin;
            }
        }
        return min;
    }
    
    public double getyMax(){
        double max = Double.MIN_VALUE;
        for(Signal signal:this.signals){
            double signalYmax = signal.getyMax();
            if(max > signalYmax){
                max = signalYmax;
            }
        }
        return max;
    }
    
    public double getyMin(){
        double min = Double.MAX_VALUE;
        for(Signal signal:this.signals){
            double signalYmin = signal.getyMin();
            if(min > signalYmin){
                min = signalYmin;
            }
        }
        return min;
    }
    
    public boolean inGrid(double xOr, double xInc, double yOr, double yInc, Point p1, Point p2){
        
        //Edge case
        if((p1.getX() < xOr) && (p2.getX() < xOr)){
            return false;
        }
        if((p1.getX() > (xOr + xInc)) && (p2.getX() > (xOr + xInc))){
            return false;
        }
        if((p1.getY() < yOr) && (p2.getY() < yOr)){
            return false;
        }
        if((p1.getY() > (yOr + yInc)) && (p2.getY() > (yOr + yInc))){
            return false;
        }
        
        //Case 0 p1 == p2
        if(p1.equals(p2)){
            if(p1.getX() >= xOr &&  p1.getX() <= (xOr + xInc)){
                return ( (p1.getY() >= yOr) && (p1.getY() <= (yOr + yInc)) );
            }
            return false;
        }
        
        //Case 1 x = x1;
        if(p1.getX() == p2.getX()){
            if(p1.getX() >= xOr &&  p1.getX() <= (xOr + xInc)){
                return true;
            }
            return false;
        }
        
        //Case 2 y = y1;
        if(p1.getY() == p2.getY()){
            if(p1.getY() >= yOr &&  p1.getY() <= (yOr + yInc)){
                return true;
            }
            return false;
        }
        
        //Case 3 y = mx +c
        //Case 3a xOr
        double yXor = (((p2.getY() - p1.getY())/(p2.getX() - p1.getX())) * (xOr - p1.getX())) + p1.getY();
        if((yXor >= yOr) && (yXor <= (yOr + yInc))){
            return true;
        }
        //Case 3b xOr+xInc
        double yXorInc = (((p2.getY() - p1.getY())/(p2.getX() - p1.getX())) * ((xOr+xInc) - p1.getX())) + p1.getY();
        if((yXorInc >= yOr) && (yXorInc <= (yOr + yInc))){
            return true;
        }
        //Case 3c yOr
        double xYor = (((p2.getX() - p1.getX())/(p2.getY() - p1.getY())) * (yOr - p1.getY())) + p1.getX();
        if((xYor >= xOr) && (xYor <= (xOr + xInc))){
            return true;
        }
        //Case 3d yOr+yInc
        double xYorInc = (((p2.getX() - p1.getX())/(p2.getY() - p1.getY())) * ((yOr+yInc) - p1.getY())) + p1.getX();
        if((xYorInc >= xOr) && (xYorInc <= (xOr + xInc))){
            return true;
        }
        return false;
    }
    
}
