/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.gridtli.DOM;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author prash
 */
public class SubGrid {

    @Getter
    @Setter
    private double xOrigin;

    @Getter
    @Setter
    private double yOrigin;

    @Getter
    @Setter
    private boolean covered;

    public SubGrid(double _xOrigin, double _yOrigin) {
        this.xOrigin = _xOrigin;
        this.yOrigin = _yOrigin;
        covered = false;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof SubGrid)) {
            return false;
        }
        SubGrid clone = (SubGrid) o;
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
