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
    
    public Grid(){
        signals = new ArrayList<Signal>();
        
    }
    
    
    
    
    
}
