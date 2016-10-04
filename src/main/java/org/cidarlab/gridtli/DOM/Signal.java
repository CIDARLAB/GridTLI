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
public class Signal {

    @Getter
    private final List<Point> points;

    @Getter
    @Setter
    private double xLimit;

    @Getter
    @Setter
    private double yLimit;

    public Signal(List<Point> _points) {
        points = new ArrayList<Point>(_points);
        if(!isSorted()){
            sortPoints(); 
        }
    }
    
    public double getxMax(){
        return this.points.get(this.points.size()-1).getX();
    }
    
    public double getxMin(){
        return this.points.get(0).getX();
    }
    
    public double getyMax(){
        double max = this.points.get(0).getY();
        
        for(int i=0;i<this.points.size();i++){
            if(this.points.get(i).getY() > max){
                max = this.points.get(i).getY();
            }
        }
        
        return max;
    }
    
    public double getyMin(){
        double min = this.points.get(0).getY();
        
        for(int i=0;i<this.points.size();i++){
            if(this.points.get(i).getY() < min){
                min = this.points.get(i).getY();
            }
        }
        
        return min;
    }
    
    public List<Point> getGridPoints(double xOr, double xInc){
        List<Point> gpoints = new ArrayList<Point>();
        
        Point prev = new Point(this.points.get(0));
        
        boolean started = false;
        boolean ended = false;
        
        for(Point point:this.points){
            if(!started){
                if(point.getX() >= xOr){
                    if (point.getX() <= (xOr + xInc)) {
                        started = true;
                        if (!prev.equals(point)) {
                            gpoints.add(prev);
                        }
                        gpoints.add(point);
                    }
                    else{
                        break;
                    }
                    
                } else {
                    prev = point;
                }
                
            } else{
                gpoints.add(point);
                if(point.getX() >= (xOr + xInc)){
                    break;
                }
            }
        }
        
        return gpoints;
    }
    
    
    private boolean isSorted(){
        for(int i=0;i<this.points.size()-1;i++){
            if(this.points.get(i).getX() > this.points.get(i+1).getX()){
                return false;
            }
        }
        return true;
    }
    
    private void sortPoints() {
        quickSortPoints(this.points, 0, this.points.size()-1);
    }

    private void quickSortPoints(List<Point> _points, int low, int high) {
        if (low < high) {
            int p = partition(_points, low, high);
            quickSortPoints(_points, low, p-1);
            quickSortPoints(_points, p, high);
        }
    }
    
    private int partition(List<Point> _points, int low, int high) {
        double pivot = _points.get(high).getX();
        int p = low;
        for (int i = low; i < high; i++) {
            if (_points.get(i).getX() <= pivot) {
                swapPoints(_points.get(i),_points.get(p));
                p++;
            }
        }
        swapPoints(_points.get(p),_points.get(high));
        return p;
    }

    private void swapPoints(Point p1, Point p2) {
        Point temp = new Point(p1);
        p1.setX(p2.getX());
        p1.setY(p2.getY());
        p1.setXSignal(p2.getXSignal());
        p1.setYSignal(p2.getYSignal());
    
        p2.setX(temp.getX());
        p2.setY(temp.getY());
        p2.setXSignal(temp.getXSignal());
        p2.setYSignal(temp.getYSignal());
    
    }
    
}
