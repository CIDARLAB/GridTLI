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
    @Setter
    private List<Signal> signals;
    
    @Getter
    @Setter
    private double xIncrement;
    
    @Getter
    @Setter
    private double yIncrement;
    
    @Getter
    @Setter
    private double xLimit;
    
    @Getter
    @Setter
    private double yLimit;
    
    public Grid(List<Signal> _signals){
        signals = _signals;
    }
    
    public Grid(Grid copy){
        this.signals = new ArrayList<Signal>(copy.signals);
        this.xIncrement = copy.xIncrement;
        this.xLimit = copy.xLimit;
        this.yIncrement = copy.yIncrement;
        this.yLimit = copy.yLimit;
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
                if (p1.getY() >= yOr && p1.getY() <= (yOr + yInc)) {
                    return true;
                }
                return false;
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
