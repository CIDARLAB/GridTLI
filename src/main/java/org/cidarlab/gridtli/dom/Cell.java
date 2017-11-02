/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.gridtli.dom;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author prash
 */
public class Cell {

    @Getter
    @Setter
    private double xOrigin;

    @Getter
    @Setter
    private double yOrigin;

    
    @Getter
    @Setter
    private double xInc;
    
    @Getter
    @Setter
    private double yInc;
    
    //@Getter
    //@Setter
    //private boolean covered;

    public Cell(double _xOrigin, double _yOrigin) {
        this.xOrigin = _xOrigin;
        this.yOrigin = _yOrigin;
    }
    
    public Cell(double _xOrigin, double _yOrigin, double _xInc, double _yInc){
        this.xOrigin = _xOrigin;
        this.yOrigin = _yOrigin;
        this.xInc = _xInc;
        this.yInc = _yInc;
    }

    public boolean smallerThan(Cell cell){
        
        if(this.xOrigin < cell.xOrigin){
            return true;
        }
        if(this.xOrigin == cell.xOrigin){
            if(this.yOrigin < cell.yOrigin){
                return true;
            }
        }
        
        return false;
    }
    
    @Override
    public String toString(){
        String str = "";
        str += "(" + this.xOrigin + "," + this.yOrigin +  ")";
        //str += this.xOrigin + "," + this.yOrigin + ":" + this.covered;
        return str;
    }
    
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Cell)) {
            return false;
        }
        Cell clone = (Cell) o;
        if (this.xOrigin == clone.xOrigin) {
            if (this.yOrigin == clone.yOrigin) {
                return true;
            }
        }

        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + (int) (Double.doubleToLongBits(this.xOrigin) ^ (Double.doubleToLongBits(this.xOrigin) >>> 32));
        hash = 97 * hash + (int) (Double.doubleToLongBits(this.yOrigin) ^ (Double.doubleToLongBits(this.yOrigin) >>> 32));
        return hash;
    }

}
