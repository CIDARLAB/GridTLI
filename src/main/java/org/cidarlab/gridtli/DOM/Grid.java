/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.gridtli.DOM;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
    private Set<SubGrid> subGrid;

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

    public Grid(List<Signal> _signals) {
        this.signals = _signals;
        this.xIncrement = 1.0;
        this.yIncrement = 1.0;

        this.xUpperLimit = this.getxMax() + this.xIncrement;  //Maybe 2*increment?    
        this.xLowerLimit = this.getxMin() - this.xIncrement;

        this.yUpperLimit = this.getyMax() + this.yIncrement;
        this.yLowerLimit = this.getyMin() - this.yIncrement;

        this.centered = false;

        createSubGrid();
    }

    public Grid(List<Signal> _signals, double _xIncrement, double _yIncrement) {
        this.signals = _signals;
        this.xIncrement = _xIncrement;
        this.yIncrement = _yIncrement;

        this.xUpperLimit = this.getxMax() + this.xIncrement;  //Maybe 2*increment?
        this.xLowerLimit = this.getxMin() - this.xIncrement;

        this.yUpperLimit = this.getyMax() + this.yIncrement;
        this.yLowerLimit = this.getyMin() - this.yIncrement;

        this.centered = false;

        createSubGrid();
    }

    public Grid(List<Signal> _signals, double _xIncrement, double _yIncrement, double _xUpperLimit, double _xLowerLimit, double _yUpperLimit, double _yLowerLimit) {
        this.signals = _signals;
        this.xIncrement = _xIncrement;
        this.yIncrement = _yIncrement;

        this.xUpperLimit = _xUpperLimit;
        this.xLowerLimit = _xLowerLimit;

        this.yUpperLimit = _yUpperLimit;
        this.yLowerLimit = _yLowerLimit;;

        this.centered = false;

        createSubGrid();
    }

    private void createSubGrid() {
        this.subGrid = new HashSet<SubGrid>();

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
            if (this.xUpperLimit < 0 || this.xLowerLimit > 0) {
                xStart = this.xLowerLimit;
                this.xCentered = false;
                this.xStart = xStart;
            }
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
            for (double i = xPOSstart; i <= this.xUpperLimit; i += this.xIncrement) {
                for (double j = yPOSstart; j <= this.yUpperLimit; j += this.yIncrement) {
                    xPOSyPOS.add(new SubGrid(i, j));
                }
            }
            //Second quadrant xPOS yNEG
            List<SubGrid> xPOSyNEG = new ArrayList<SubGrid>();
            if (xStart > 0) {
                xPOSstart = xStart;
            }
            if (this.yUpperLimit < 0) {
                for (double i = xPOSstart; i <= this.xUpperLimit; i += this.xIncrement) {
                    for (double j = this.yLowerLimit; j <= this.yUpperLimit; j += this.yIncrement) {
                        xPOSyNEG.add(new SubGrid(i, j));
                    }
                }
            } else {
                //List<SubGrid> temp = new ArrayList<SubGrid>();
                List<Double> temp = new ArrayList<Double>();
                for (double j = 0; j >= this.yLowerLimit; j -= this.yIncrement) {
                    temp.add(j);
                }

                for (double i = xPOSstart; i <= this.xUpperLimit; i += this.xIncrement) {
                    for (int j = temp.size() - 1; j >= 0; j--) {
                        xPOSyNEG.add(new SubGrid(i, temp.get(j)));
                    }
                }
            }

            //Third quadrant xNEG yNEG 
            List<SubGrid> xNEGyNEG = new ArrayList<SubGrid>();
            if (this.xUpperLimit < 0) {
                if (this.yUpperLimit < 0) {
                    for (double i = this.xLowerLimit; i <= this.xUpperLimit; i += this.xIncrement) {
                        for (double j = this.yLowerLimit; j <= this.yUpperLimit; j += this.yIncrement) {
                            xNEGyNEG.add(new SubGrid(i, j));
                        }
                    }
                } else {
                    List<Double> temp = new ArrayList<Double>();
                    for (double j = 0; j >= this.yLowerLimit; j -= this.yIncrement) {
                        temp.add(j);
                    }
                    for (double i = this.xLowerLimit; i <= this.xUpperLimit; i += this.xIncrement) {
                        for (int j = temp.size() - 1; j >= 0; j--) {
                            xNEGyNEG.add(new SubGrid(i, temp.get(j)));
                        }
                    }
                }
            } else {
                List<Double> tempx = new ArrayList<Double>();
                for (double i = 0; i >= this.xLowerLimit; i -= this.xIncrement) {
                    tempx.add(i);
                }
                if (this.yUpperLimit < 0) {
                    for (int i = tempx.size() - 1; i >= 0; i--) {
                        for (double j = this.yLowerLimit; j <= this.yUpperLimit; j += this.yIncrement) {
                            xNEGyNEG.add(new SubGrid(tempx.get(i), j));
                        }
                    }
                } else {
                    List<Double> tempy = new ArrayList<Double>();
                    for (double j = 0; j >= this.yLowerLimit; j -= this.yIncrement) {
                        tempy.add(j);
                    }
                    for (int i = tempx.size() - 1; i >= 0; i--) {
                        for (int j = tempy.size() - 1; j >= 0; j--) {
                            xNEGyNEG.add(new SubGrid(tempx.get(i), tempy.get(j)));
                        }
                    }
                }
            }

            //Fourth quadrant xNEG yPOS
            List<SubGrid> xNEGyPOS = new ArrayList<SubGrid>();
            if (yStart > 0) {
                yPOSstart = yStart;
            }
            if (this.xUpperLimit < 0) {
                for(double i=this.xLowerLimit; i <= this.xUpperLimit; i+= this.xIncrement){
                    for(double j = yPOSstart; j <= this.yUpperLimit; j += this.yIncrement){
                        xNEGyPOS.add(new SubGrid(i,j));
                    }
                }
            } else {
                List<Double> tempx = new ArrayList<Double>();
                for(double i=0; i >= this.xLowerLimit; i -= this.xIncrement){
                    tempx.add(i);
                }
                for(int i = tempx.size()-1; i >=0; i--){
                    for(double j = yPOSstart; j <= this.yUpperLimit; j += this.yIncrement){
                        xNEGyPOS.add(new SubGrid(tempx.get(i),j));
                    }
                }
            }
            //This is where you add everything to the hashset. 
            this.subGrid.addAll(xPOSyPOS);
            this.subGrid.addAll(xPOSyNEG);
            this.subGrid.addAll(xNEGyNEG);
            this.subGrid.addAll(xNEGyPOS);
            
            
            
        }//End of Else condition of ifCentered
        

    }

    public double getxMax() {
        double max = this.signals.get(0).getxMax();
        for (Signal signal : this.signals) {
            double signalXmax = signal.getxMax();
            System.out.println("Max of x signal :: " + signalXmax);
            if (signalXmax > max) {
                max = signalXmax;
            }
        }
        System.out.println("Max X :: " + max);
        return max;
    }

    public double getxMin() {
        double min = this.signals.get(0).getxMin();
        for (Signal signal : this.signals) {
            double signalXmin = signal.getxMin();
            System.out.println("Min of x signal :: " + signalXmin);
            if (signalXmin < min) {
                min = signalXmin;
            }
        }
        System.out.println("Min X :: " + min);
        return min;
    }

    public double getyMax() {
        double max = this.signals.get(0).getyMax();
        for (Signal signal : this.signals) {
            double signalYmax = signal.getyMax();
            System.out.println("Max of y signal :: " + signalYmax);
            if (signalYmax > max) {
                max = signalYmax;
            }
        }
        System.out.println("Max Y :: " + max);
        return max;
    }

    public double getyMin() {
        double min = this.signals.get(0).getyMin();
        for (Signal signal : this.signals) {
            double signalYmin = signal.getyMin();
            System.out.println("Min of y signal :: " + signalYmin);
            if (signalYmin < min) {
                min = signalYmin;
            }
        }
        System.out.println("Min Y :: " + min);
        return min;
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

        //Case 0 p1 == p2
        if (p1.equals(p2)) {
            if (p1.getX() >= xOr && p1.getX() <= (xOr + xInc)) {
                return ((p1.getY() >= yOr) && (p1.getY() <= (yOr + yInc)));
            }
            return false;
        }

        //Case 1 x = x1;
        if (p1.getX() == p2.getX()) {
            if (p1.getX() >= xOr && p1.getX() <= (xOr + xInc)) {
                return true;
            }
            return false;
        }

        //Case 2 y = y1;
        if (p1.getY() == p2.getY()) {
            if (p1.getY() >= yOr && p1.getY() <= (yOr + yInc)) {
                return true;
            }
            return false;
        }

        //Case 3 y = mx +c
        //Case 3a xOr
        double yXor = (((p2.getY() - p1.getY()) / (p2.getX() - p1.getX())) * (xOr - p1.getX())) + p1.getY();
        if ((yXor >= yOr) && (yXor <= (yOr + yInc))) {
            return true;
        }
        //Case 3b xOr+xInc
        double yXorInc = (((p2.getY() - p1.getY()) / (p2.getX() - p1.getX())) * ((xOr + xInc) - p1.getX())) + p1.getY();
        if ((yXorInc >= yOr) && (yXorInc <= (yOr + yInc))) {
            return true;
        }
        //Case 3c yOr
        double xYor = (((p2.getX() - p1.getX()) / (p2.getY() - p1.getY())) * (yOr - p1.getY())) + p1.getX();
        if ((xYor >= xOr) && (xYor <= (xOr + xInc))) {
            return true;
        }
        //Case 3d yOr+yInc
        double xYorInc = (((p2.getX() - p1.getX()) / (p2.getY() - p1.getY())) * ((yOr + yInc) - p1.getY())) + p1.getX();
        if ((xYorInc >= xOr) && (xYorInc <= (xOr + xInc))) {
            return true;
        }
        return false;
    }

}
