/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.gridtli.TLI;

import hyness.stl.AlwaysNode;
import hyness.stl.ConjunctionNode;
import hyness.stl.DisjunctionNode;
import hyness.stl.LinearPredicateLeaf;
import hyness.stl.RelOperation;
import hyness.stl.TreeNode;
import hyness.stl.grammar.flat.STLflat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.cidarlab.gridtli.DOM.Grid;
import org.cidarlab.gridtli.DOM.Signal;
import org.cidarlab.gridtli.DOM.SubGrid;

/**
 *
 * @author prash
 */
public class TemporalLogicInference {
    
    public static List<Set<Signal>> cluster(Grid grid, double threshold){
        List<Set<Signal>> clusters = new ArrayList<Set<Signal>>();
        double xstart = grid.getSubGridMinX();
        //double xend = grid.getSubGridMaxX() + grid.getXIncrement();
        double xend = grid.getSubGridMaxX();
        
        double ystart = grid.getSubGridMinY();
        //double yend = grid.getSubGridMaxY() + grid.getYIncrement();
        double yend = grid.getSubGridMaxY();
        
        boolean started = true;
        boolean up = true;
        List<Set<Integer>> clusterList = new ArrayList<Set<Integer>>();
        List<Set<Integer>> finalClusterList = new ArrayList<Set<Integer>>();
        
        Set<Integer> cluster = new HashSet<Integer>();
        for(double i=xstart;i<= xend; i+= grid.getXIncrement()){
            
            started = false;
            up = false;
            clusterList = new ArrayList<Set<Integer>>();
            cluster = new HashSet<Integer>();
            Set<Signal> signals = new HashSet<Signal>(grid.getSignals());
            int differenceCount = 0;
            for(double j=ystart; j<= yend; j+= grid.getYIncrement()){
                //System.out.println(i+ ","+j);
                if(grid.isSpecificSubGridCovered(i, j)){
                    if(!started){
                        started = true;
                    }
                    if(!up){
                        up = true;
                        //System.out.println("Another cluster started.");
                        if( (differenceCount * grid.getYIncrement()) > threshold){
                            //System.out.println("Difference greater than threshold");
                            if(!cluster.isEmpty()){
                                //System.out.println("Cluster is not empty :: " + cluster);
                                clusterList.add(cluster);
                            }
                            cluster = new HashSet<Integer>();
                        }
                    } 
                    Set<Signal> tempSignalList = new HashSet<Signal>();
                    for (Signal signal : signals) {
                        if (signal.coversSubGrid(i, j)) {
                            cluster.add(signal.getIndex());
                            //signals.remove(signal);
                        } else{
                            tempSignalList.add(signal);
                        }
                    }
                    signals = new HashSet<Signal>(tempSignalList);
                } else {
                    if(up){
                        //System.out.println("End of cluster");
                        differenceCount = 0;
                        up = false;
                    }
                    differenceCount++;
                }
            }
            if(!started){
                //System.out.println("Empty");
            } else{
                if (!cluster.isEmpty()) {
                    clusterList.add(cluster);
                }
                //System.out.println("Cluster List :: ");
                //System.out.println(clusterList);
            }
            //System.out.println("\n\n");
            if(clusterList.size() > finalClusterList.size()){
                finalClusterList = new ArrayList<Set<Integer>>(clusterList);
            }
        }
        
        System.out.println("Final Cluster :: \n"+finalClusterList);
        Set<Signal> signals = new HashSet<Signal>(grid.getSignals());
        for(Set<Integer> singleCluster: finalClusterList){
            Set<Signal> signalCluster = new HashSet<Signal>();
            Set<Signal> otherSignals = new HashSet<Signal>();
            for(Signal signal:signals){
                if(singleCluster.contains(signal.getIndex())){
                    signalCluster.add(signal);
                } else{
                    otherSignals.add(signal);
                }
            }
            clusters.add(signalCluster);
            signals = new HashSet<Signal>(otherSignals);
        }
        
        return clusters;
    }
    
    public static Set<SubGrid> getAllCoveredSubGrids(Set<Signal> signals){
        Set<SubGrid> subgrids = new HashSet<SubGrid>();
        
        for(Signal signal:signals){
            subgrids.addAll(signal.getSubGridCovered());
        }
        
        return subgrids;
    }
    
    public static TreeNode getClusterSTL(String xsignal, Set<Signal> signals, double xinc, double yinc, double xmin, double xmax, double ymin, double ymax, double threshold ){
        SubGrid smallest = new SubGrid(Double.MAX_VALUE,Double.MAX_VALUE); 
        for(Signal signal:signals){
            if(signal.getStartingGrid().smallerThan(smallest)){
                smallest = signal.getStartingGrid();
            }
        }
        Set<SubGrid> covered = getAllCoveredSubGrids(signals);
        boolean topend = false;
        boolean bottomend = false;
        
        double x = smallest.getXOrigin();
        double y = smallest.getYOrigin();
        double xstart = x;
        boolean moveright = false;
        
        //<editor-fold desc="bottom">
        System.out.println("BOTTOM");
        List<TreeNode> bottom = new ArrayList<TreeNode>();
        while(true){
            //System.out.println(x + "," + y);
            if(covered.contains(new SubGrid(x, (y -yinc) )) ){
                y = y - yinc;
                continue;
            } else {
                int downcount = 0;
                double diff =0;
                for (int i = 1; (i * yinc) <= (y - (threshold + yinc)); i++) {
                    if (covered.contains(new SubGrid(x, (y - (i * yinc))))) {
                        diff = i* yinc;
                        downcount++;
                    }
                }
                if(downcount == 0){
                    //lowest point. Move right
                    //System.out.println("Lowest Point. Move Right.");
                    if(moveright){
                        if(covered.contains( new SubGrid( (x + xinc), y) )){
                            x = x + xinc;
                            continue;
                        } else {
                            moveright = false;
                            LinearPredicateLeaf lp = new LinearPredicateLeaf(RelOperation.GE,xsignal,y);
                            AlwaysNode always = new AlwaysNode(lp,xstart,x + xinc);
                            bottom.add(always);
                            System.out.println(always.toString());
                            
                            if(x + xinc > xmax){
                                break;
                            } else {
                                int changecount = 0;
                                diff = 0;
                                for(int i=1; (i*yinc) <= (y-ymin); i++){
                                    if(covered.contains(new SubGrid(x + xinc, y - (i*yinc) ) )){
                                        changecount++;
                                        diff = (i*yinc);
                                    }
                                }
                                if(changecount ==0){
                                    diff =0;
                                    for(int i=1; (i*yinc) <= (ymax - y); i++ ){
                                        if(covered.contains(new SubGrid(x + xinc, y + (i*yinc)))){
                                            changecount++;
                                            diff = (i*yinc);
                                            break;
                                        }
                                    }
                                    if(changecount ==0){
                                        System.out.println("THE END!!");
                                        break;
                                    } else {
                                        x = x + xinc;
                                        y = y + diff;
                                        continue;
                                    }
                                }
                                else{
                                    x = x + xinc;
                                    y = y - (diff);
                                    continue;
                                }
                            }
                        }
                        
                        
                    } else {
                        moveright = true;
                        xstart = x;
                    }
                } else {
                    y = y - (diff);
                    continue;
                }
            }
        }
        //</editor-fold>
        
        
        x = smallest.getXOrigin();
        y = smallest.getYOrigin();
        xstart = x;
        moveright = false;
        
        //<editor-fold desc="top">
        System.out.println("TOP");
        List<TreeNode> top = new ArrayList<TreeNode>();
        while(true){
            if(covered.contains(new SubGrid(x, (y + yinc) )) ){
                y = y + yinc;
                continue;
            } else {
                int upcount = 0;
                double diff =0;
                for (int i = 1; (i * yinc) <= (y - (threshold + yinc)); i++) {
                    if (covered.contains(new SubGrid(x, (y + (i * yinc))))) {
                        diff = i* yinc;
                        upcount++;
                    }
                }
                if(upcount == 0){
                    //topmost point. Move right
                    //System.out.println("Topmost Point. Move Right.");
                    if(moveright){
                        if(covered.contains( new SubGrid( (x + xinc), y) )){
                            if(covered.contains(new SubGrid(x+xinc, y+yinc))){
                                moveright = false;
                                LinearPredicateLeaf lp = new LinearPredicateLeaf(RelOperation.LE, xsignal, y + yinc);
                                AlwaysNode always = new AlwaysNode(lp, xstart, x + xinc);
                                top.add(always);
                                System.out.println(always.toString());
                                x = x + xinc;
                                y = y + yinc;
                                continue;
                            } else {
                                int moverightupcount = 0;
                                for (int i = 1; (i * yinc) <= (y - (threshold + yinc)); i++) {
                                    if (covered.contains(new SubGrid(x + xinc, (y + (i * yinc))))) {
                                        diff = i * yinc;
                                        moverightupcount++;
                                    }
                                }
                                
                                
                                if (moverightupcount == 0) {
                                    x = x + xinc;
                                    continue;
                                } else {
                                    x = x + xinc;
                                    y = y + diff;
                                    continue;
                                }
                                    
                            }
                            
                        } else {
                            moveright = false;
                            LinearPredicateLeaf lp = new LinearPredicateLeaf(RelOperation.LE,xsignal,y + yinc);
                            AlwaysNode always = new AlwaysNode(lp,xstart,x + xinc);
                            top.add(always);
                            System.out.println(always.toString());
                            if(x + xinc > xmax){
                                break;
                            } else {
                                int changecount = 0;
                                diff = 0;
                                for(int i=1; (i*yinc) <= (ymax - y); i++){
                                    if(covered.contains(new SubGrid(x + xinc, y + (i*yinc) ) )){
                                        changecount++;
                                        diff = (i*yinc);
                                    }
                                }
                                if(changecount ==0){
                                    diff =0;
                                    for(int i=1; (i*yinc) <= (y-ymin); i++ ){
                                        if(covered.contains(new SubGrid(x + xinc, y - (i*yinc)))){
                                            changecount++;
                                            diff = (i*yinc);
                                            break;
                                        }
                                    }
                                    if(changecount ==0){
                                        System.out.println("THE END!!");
                                        break;
                                    } else {
                                        x = x + xinc;
                                        y = y - diff;
                                        continue;
                                    }
                                }
                                else{
                                    x = x + xinc;
                                    y = y + (diff);
                                    continue;
                                }
                            }
                        }
                        
                        
                    } else {
                        moveright = true;
                        xstart = x;
                    }
                } else {
                    y = y + (diff);
                    continue;
                }
                
                
                
            }
        }
        //</editor-fold>
        return new ConjunctionNode(reduceToSingleConjunction(top),reduceToSingleConjunction(bottom));
    }
    
    public static TreeNode getSTL(Grid grid, double threshold){
        List<Set<Signal>> clusters = cluster(grid,threshold);
        List<TreeNode> disjunctionClusters = new ArrayList<TreeNode>();
        for(Set<Signal> cluster:clusters){
            disjunctionClusters.add(getClusterSTL(grid.getXSignal(),cluster,grid.getXIncrement(),grid.getYIncrement(),grid.getXLowerLimit(),grid.getXUpperLimit(),grid.getYLowerLimit(),grid.getYUpperLimit(),threshold));
        }
        return reduceToSingleDisjunction(disjunctionClusters);
    }
    
    public static STLflat getLongSTL(Grid grid) {
        
        System.out.println(grid.getSubGrid());

        double xstart = grid.getSubGridMinX();
        //double xend = grid.getSubGridMaxX() + grid.getXIncrement();
        double xend = grid.getSubGridMaxX();
        
        double ystart = grid.getSubGridMinY();
        //double yend = grid.getSubGridMaxY() + grid.getYIncrement();
        double yend = grid.getSubGridMaxY();
        
        boolean started = false;
        boolean ended = false;
        boolean up = false;
        List<TreeNode> outerNodes = new ArrayList<TreeNode>();
        for (double i = xstart; i <= xend; i += grid.getXIncrement()) {
            started = false;
            ended = false;
            List<LinearPredicateLeaf> predicates = new ArrayList<LinearPredicateLeaf>();
            for (double j = ystart; j <= yend; j += grid.getYIncrement()) {
                SubGrid sgrid = new SubGrid(i, j);
                if (grid.getSubGrid().containsKey(sgrid)) {
                    if (!started) {
                        if (grid.isSpecificSubGridCovered(sgrid)){
                            started = true;
                            up = true;
                            predicates.add(new LinearPredicateLeaf(RelOperation.GE, grid.getYSignal(), j));
                        }
                    }

                    if (started) {
                        if (up) {

                            if (!grid.isSpecificSubGridCovered(sgrid)) {
                                predicates.add(new LinearPredicateLeaf(RelOperation.LE, grid.getYSignal(), j));
                                up = false;
                            }
                        } else {
                            if (grid.isSpecificSubGridCovered(sgrid)) {
                                predicates.add(new LinearPredicateLeaf(RelOperation.GE, grid.getYSignal(), j));
                                up = true;
                            }
                        }
                    }
                }
            }
            List<ConjunctionNode> innerConjunctions = new ArrayList<ConjunctionNode>();
            for (int k = 0; k < predicates.size() - 1; k += 2) {
                if (predicates.size() > 1) {

                    AlwaysNode first = new AlwaysNode(predicates.get(k), i, i + grid.getXIncrement());
                    AlwaysNode second = new AlwaysNode(predicates.get(k+1), i, i + grid.getXIncrement());
                    innerConjunctions.add(new ConjunctionNode(first, second));

                }
            }
            List<TreeNode> listTreeNode = new ArrayList<TreeNode>();
            listTreeNode.addAll(innerConjunctions);
            if ((predicates.size() % 2) == 1) {
                listTreeNode.add(predicates.get(predicates.size() - 1));
            }
            if(listTreeNode.size() > 0){
                outerNodes.add(reduceToSingleDisjunction(listTreeNode));
            }
            
        }
        TreeNode entireConjunction = reduceToSingleConjunction(outerNodes);
        STLflat stl = new STLflat(entireConjunction);
        System.out.println(stl.module.toString());
        return stl;
    }
    
    private List<Double> getMaxMin(Set<SubGrid> subgrid){
        List<Double> maxmin = new ArrayList<Double>();
        double xmin = Double.MAX_VALUE;
        double xmax = (-1) * Double.MAX_VALUE;
        double ymin = Double.MAX_VALUE;
        double ymax = (-1) * Double.MAX_VALUE;
        
        for(SubGrid sgrid:subgrid){
            if(sgrid.getXOrigin() < xmin){
                xmin = sgrid.getXOrigin();
            }
            if(sgrid.getXOrigin() > xmax){
                xmax = sgrid.getXOrigin();
            }
            if(sgrid.getYOrigin() < ymin){
                ymin = sgrid.getYOrigin();
            }
            if(sgrid.getYOrigin() > ymax){
                ymax = sgrid.getYOrigin();
            }
        }
        
        maxmin.add(xmin);
        maxmin.add(xmax);
        maxmin.add(ymin);
        maxmin.add(ymax);
        
        
        return maxmin;
    }
    
    private static TreeNode reduceToSingleDisjunction(List<TreeNode> listTreeNode) {

        if (listTreeNode.size() == 1) {
            return listTreeNode.get(0);
        }

        List<TreeNode> reducedList = new ArrayList<TreeNode>();
        reducedList.addAll(listTreeNode);
        while(reducedList.size() > 1){
            List<TreeNode> tempList = new ArrayList<TreeNode>();
            tempList.addAll(reducedList);
            reducedList = new ArrayList<TreeNode>();
            for(int i =0; i < tempList.size()-1; i+= 2){
                reducedList.add(new DisjunctionNode(tempList.get(i),tempList.get(i+1)));
            }
            if((tempList.size() % 2) == 1){
                reducedList.add(tempList.get(tempList.size()-1));
            }
        }
        

        return reducedList.get(0);
    }
    
    private static TreeNode reduceToSingleConjunction(List<TreeNode> listTreeNode) {

        if (listTreeNode.size() == 1) {
            return listTreeNode.get(0);
        }

        List<TreeNode> reducedList = new ArrayList<TreeNode>();
        reducedList.addAll(listTreeNode);
        while(reducedList.size() > 1){
            List<TreeNode> tempList = new ArrayList<TreeNode>();
            tempList.addAll(reducedList);
            reducedList = new ArrayList<TreeNode>();
            for(int i =0; i < tempList.size()-1; i+= 2){
                reducedList.add(new ConjunctionNode(tempList.get(i),tempList.get(i+1)));
            }
            if((tempList.size() % 2) == 1){
                reducedList.add(tempList.get(tempList.size()-1));
            }
        }
        

        return reducedList.get(0);
    }
    
}
