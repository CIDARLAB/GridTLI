/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.gridtli.tli;

import hyness.stl.AlwaysNode;
import hyness.stl.ConjunctionNode;
import hyness.stl.DisjunctionNode;
import hyness.stl.LinearPredicateLeaf;
import hyness.stl.RelOperation;
import hyness.stl.TreeNode;
import hyness.stl.grammar.sharp.STLSharp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.cidarlab.gridtli.dom.Grid;
import org.cidarlab.gridtli.dom.Signal;
import org.cidarlab.gridtli.dom.SubGrid;

/**
 *
 * @author prash
 */
public class TemporalLogicInference {
    
    public static List<Set<Integer>> getClusterIndices(Grid grid, double threshold){
        
        double xstart = grid.getSubGridMinX();
        //double xend = grid.getSubGridMaxX() + grid.getXIncrement();
        double xend = grid.getSubGridMaxX();
        
        double ystart = grid.getSubGridMinY();
        //double yend = grid.getSubGridMaxY() + grid.getYIncrement();
        double yend = grid.getSubGridMaxY();
        
        boolean first = true;
        boolean started = true;
        boolean up = true;
        List<Set<Integer>> clusterList = new ArrayList<Set<Integer>>();
        List<Set<Integer>> previousClusterList = new ArrayList<Set<Integer>>();
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
            //System.out.println("CHANGE STARTS HERE");
            if(first && started){
                previousClusterList = new ArrayList<Set<Integer>>(clusterList);
                first = false;
            } else {
                //System.out.println("Previous List :: " + previousClusterList);
                //System.out.println("Current List :: " + clusterList);
                finalClusterList = updateClusterList(previousClusterList,clusterList);
                previousClusterList = new ArrayList<Set<Integer>>(finalClusterList);
                //System.out.println("Final Cluster List :: " + finalClusterList);
                //System.out.println("=================================================\n");
                
            }
            
        }
        return finalClusterList;
    }
    
    public static Set<Signal> getSignalsFromClusterIndices(Grid grid, Set<Integer> cluster){
   
        Set<Signal> signals = new HashSet<Signal>(grid.getSignals());
        Set<Signal> signalCluster = new HashSet<Signal>();
        for (Signal signal : signals) {
            if (cluster.contains(signal.getIndex())) {
                signalCluster.add(signal);
            } 
        }
        return signalCluster;
    }
    
    public static List<Set<Signal>> cluster(Grid grid, double threshold){
        
        List<Set<Signal>> clusters = new ArrayList<Set<Signal>>();
        
        
        //System.out.println("Final Cluster :: \n"+finalClusterList);
        Set<Signal> signals = new HashSet<Signal>(grid.getSignals());
        for(Set<Integer> singleCluster: getClusterIndices(grid,threshold)){
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
    
    public static List<Set<Integer>> updateClusterList(List<Set<Integer>> current, List<Set<Integer>> newList){
        List<Set<Integer>> finalList = new ArrayList<Set<Integer>>();
        finalList.addAll(current);
        
        boolean retain = finalList.retainAll(newList);
        if (!retain) {
            return current;
        } else {
            for(Set<Integer> s1: current){
                for(Set<Integer> s2: newList){
                    Set<Integer> temp = new HashSet<Integer>(s1);
                    temp.retainAll(s2);
                    
                    if(!finalList.contains(temp)){
                        if(!temp.isEmpty()){
                            finalList.add(temp);
                        }
                    }
                }
            }
        
            List<Set<Integer>> currentTemp = new ArrayList<Set<Integer>>();
            List<Set<Integer>> newListTemp = new ArrayList<Set<Integer>>();
            
            for(Set<Integer> s1:current){
                Set<Integer> sTemp = new HashSet<Integer>();
                sTemp.addAll(s1);
                for(Set<Integer> s2: finalList){
                    sTemp.removeAll(s2);
                }
                currentTemp.add(sTemp);
            }
            
            for(Set<Integer> s1:newList){
                Set<Integer> sTemp = new HashSet<Integer>();
                sTemp.addAll(s1);
                for(Set<Integer> s2: finalList){
                    sTemp.removeAll(s2);
                }
                newListTemp.add(sTemp);
            }
            
            for(Set<Integer> s:currentTemp){
                if(!s.isEmpty()){
                    finalList.add(s);
                }
            }
            for(Set<Integer> s:newListTemp){
                if(!s.isEmpty()){
                    finalList.add(s);
                }
            }
            
        }
        
        
        return finalList;
    }
    
    public static Set<SubGrid> getAllCoveredSubGrids(Set<Signal> signals){
        Set<SubGrid> subgrids = new HashSet<SubGrid>();
        
        for(Signal signal:signals){
            subgrids.addAll(signal.getSubGridCovered());
        }
        
        return subgrids;
    }
    
    public static double getSmallestSubGrid(Set<SubGrid> covered, double x){
        double ymin = Double.MAX_VALUE;
        for(SubGrid s:covered){
            if(s.getXOrigin() == x){
                if(s.getYOrigin() < ymin){
                    ymin = s.getYOrigin();
                }
            }
        }
        return ymin;
    }
        
    public static double getLargestSubGrid(Set<SubGrid> covered, double x){
        double ymax = Double.MIN_VALUE;
        for(SubGrid s:covered){
            if(s.getXOrigin() == x){
                if(s.getYOrigin() > ymax){
                    ymax = s.getYOrigin();
                }
            }
        }
        return ymax;
    }
    
    public static ClusterMaxMin getMaxMinClusterSubGrid(Set<SubGrid> covered, double x){
        ClusterMaxMin cmm = new ClusterMaxMin();
        for(SubGrid s:covered){
            if(s.getXOrigin() == x){
                if(s.getYOrigin() > cmm.ymax){
                    cmm.ymax = s.getYOrigin();
                }
                if(s.getYOrigin() < cmm.ymin){
                    cmm.ymin = s.getYOrigin();
                }
                cmm.xcount++;
            }
        }
        return cmm;
    } 
    
    private static class ClusterMaxMin{
        double ymin = Double.MAX_VALUE;
        double ymax = Double.MIN_VALUE;
        int xcount = 0;
    }
    
    public static List<TreeNode> getClusterSTL(String ysignal, Set<Signal> signals, double xInc, double yInc, double xMax){
        List<TreeNode> bottom = new ArrayList<TreeNode>();
        List<TreeNode> top = new ArrayList<TreeNode>();
        SubGrid smallest = new SubGrid(Double.MAX_VALUE,Double.MAX_VALUE); 
        for(Signal signal:signals){
            if(signal.getStartingGrid().smallerThan(smallest)){
                smallest = signal.getStartingGrid();
            }
        }
        Set<SubGrid> covered = getAllCoveredSubGrids(signals);
        //Bottom
        double x = smallest.getXOrigin();
        double xstartTop = x;
        double xstartBottom = x;
        double topThreshold = 0;
        double bottomThreshold = 0;
        
        boolean started = true;
        do{
            ClusterMaxMin cmm = getMaxMinClusterSubGrid(covered,x);
            if(cmm.xcount == 0){
                break;
            }
            if(started){
                topThreshold = cmm.ymax + yInc;
                bottomThreshold = cmm.ymin;
                started = false;
            } else {
                if((cmm.ymax + yInc) != topThreshold){
                    LinearPredicateLeaf lpl = new LinearPredicateLeaf(RelOperation.LE,ysignal,topThreshold);
                    AlwaysNode always = new AlwaysNode(lpl,xstartTop,x);
                    top.add(always);
                    xstartTop = x;
                    topThreshold = (cmm.ymax + yInc);
                }
                if(cmm.ymin != bottomThreshold){
                    LinearPredicateLeaf lpl = new LinearPredicateLeaf(RelOperation.GE,ysignal,bottomThreshold);
                    AlwaysNode always = new AlwaysNode(lpl,xstartBottom,x);
                    bottom.add(always);
                    xstartBottom = x;
                    bottomThreshold = cmm.ymin;
                }
            }
            x = x + xInc;
        }while(x < xMax);
        
        LinearPredicateLeaf finalToplpl = new LinearPredicateLeaf(RelOperation.LE,ysignal,topThreshold);
        AlwaysNode finalTopAlways = new AlwaysNode(finalToplpl,xstartTop,x);
        top.add(finalTopAlways);
        LinearPredicateLeaf finalBottomlpl = new LinearPredicateLeaf(RelOperation.GE,ysignal,bottomThreshold);
        AlwaysNode finalBottomAlways = new AlwaysNode(finalBottomlpl,xstartBottom,x);
        bottom.add(finalBottomAlways);
        
        List<TreeNode> allnodes = new ArrayList<TreeNode>();
        allnodes.addAll(top);
        allnodes.addAll(bottom);
        return allnodes;
    }
    
    public static List<TreeNode> getClusterSTLFast(String xsignal, Set<Signal> signals, double xinc, double yinc, double xmin, double xmax, double ymin, double ymax, double threshold ){
        
        //System.out.println("Signals :: " + signals);
        System.out.println("Start Cluster STL ===================================");
        SubGrid smallest = new SubGrid(Double.MAX_VALUE,Double.MAX_VALUE); 
        for(Signal signal:signals){
            if(signal.getStartingGrid().smallerThan(smallest)){
                smallest = signal.getStartingGrid();
            }
        }
        System.out.println("Smallest SubGrid (start point):: " + smallest);
        Set<SubGrid> covered = getAllCoveredSubGrids(signals);
        boolean topend = false;
        boolean bottomend = false;
        
        double x = smallest.getXOrigin();
        double y = smallest.getYOrigin();
        double xstart = x;
        boolean moveright = false;
        
        //<editor-fold desc="bottom">
        //System.out.println("BOTTOM");
        List<TreeNode> bottom = new ArrayList<TreeNode>();
        while(true){
            if(covered.contains(new SubGrid(x, (y -yinc) )) ){
                y = y - yinc;
            } else {
                if(x == 0 && y == 0){
                    System.out.println("abbe this was expected.");
                }
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
                        } else {
                            moveright = false;
                            LinearPredicateLeaf lp = new LinearPredicateLeaf(RelOperation.GE,xsignal,y);
                            AlwaysNode always = new AlwaysNode(lp,xstart,x + xinc);
                            bottom.add(always);
                            //System.out.println(always.toString());
                            
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
                                        //System.out.println("THE END!!");
                                        break;
                                    } else {
                                        x = x + xinc;
                                        y = y + diff;
                                    }
                                }
                                else{
                                    x = x + xinc;
                                    y = y - (diff);
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
        //System.out.println("TOP");
        List<TreeNode> top = new ArrayList<TreeNode>();
        while(true){
            if(covered.contains(new SubGrid(x, (y + yinc) )) ){
                y = y + yinc;
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
                                //System.out.println(always.toString());
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
                            //System.out.println(always.toString());
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
                                        //System.out.println("THE END!!");
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
        List<TreeNode> allLinearPredicates = new ArrayList<TreeNode>();
        allLinearPredicates.addAll(top);
        allLinearPredicates.addAll(bottom);
        return allLinearPredicates;
    }
    
    public static List<TreeNode> getSTLClusters(Grid grid, double threshold){
        List<Set<Signal>> clusters = cluster(grid,threshold);
        List<TreeNode> disjunctionClusters = new ArrayList<TreeNode>();
        for(Set<Signal> cluster:clusters){
            disjunctionClusters.add(reduceToSingleConjunction(getClusterSTLFast(grid.getYSignal(),cluster,grid.getXIncrement(),grid.getYIncrement(),grid.getXLowerLimit(),grid.getXUpperLimit(),grid.getYLowerLimit(),grid.getYUpperLimit(),threshold)));
        }
        return disjunctionClusters;
    }
    
    public static TreeNode getSTL(Grid grid, double threshold){
        List<Set<Signal>> clusters = cluster(grid,threshold);
        List<TreeNode> disjunctionClusters = new ArrayList<TreeNode>();
        int count =0;
        for(Set<Signal> cluster:clusters){
            
            //TreeNode conjunctionNode = reduceToSingleConjunction(getClusterSTLFast(grid.getYSignal(),cluster,grid.getXIncrement(),grid.getYIncrement(),grid.getXLowerLimit(),grid.getXUpperLimit(),grid.getYLowerLimit(),grid.getYUpperLimit(),threshold));
            TreeNode conjunctionNode = reduceToSingleConjunction(getClusterSTL(grid.getYSignal(),cluster,grid.getXIncrement(),grid.getYIncrement(),grid.getxMax()));
            //System.out.println("Cluster " + count++);
            //System.out.println(conjunctionNode);
            disjunctionClusters.add(conjunctionNode);
        }
        return reduceToSingleDisjunction(disjunctionClusters);
    }
    
    public static STLSharp getLongSTL(Grid grid) {
        
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
        STLSharp stl = new STLSharp(entireConjunction);
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
    
    public static TreeNode reduceToSingleDisjunction(List<TreeNode> listTreeNode) {

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
    
    public static TreeNode reduceToSingleConjunction(List<TreeNode> listTreeNode) {

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
    
    public static TreeNode getNDimSTL(Map<String, List<Signal>> signal, double perc){
        
        Map<String, Double> signalT = new HashMap<String, Double>();
        Map<String, Double> temporalT = new HashMap<String, Double>();
        for(String dim:signal.keySet()){
            List<Signal> dimSig = signal.get(dim);
            double sigMax = Double.MIN_VALUE;
            double sigMin = Double.MAX_VALUE;
            double temMax = Double.MIN_VALUE;
            double temMin = Double.MAX_VALUE;
            for(Signal s:dimSig){
                if(s.getxMax() > temMax){
                    temMax = s.getxMax();
                }
                if(s.getxMin() < temMin){
                    temMin = s.getxMin();
                }
                if(s.getyMax() > sigMax){
                    sigMax = s.getyMax();
                }
                if(s.getyMin() < sigMin){
                    sigMin = s.getyMin();
                }
            }
            
            double sigT = (sigMax - sigMin) * perc;
            if(((sigMax - sigMin) == 0) || (sigT ==0)){
                sigT = 1;
            }
            
            double temT = (temMax - temMin) * perc;
            if((temMax - temMin) == 0){
                temT = 1;
            }
            
            signalT.put(dim, sigT);
            temporalT.put(dim, temT);
        }
        return getNDimSTL(signal, signalT, temporalT, signalT);
    }
    
    public static TreeNode getNDimSTL(Map<String, List<Signal>> signal, Map<String, Double> signalT, Map<String, Double> temporalT, Map<String, Double> clusterT){
        
        List<List<Set<Integer>>> nclusters = new ArrayList<List<Set<Integer>>>();
        Map<String, Grid> gridMap = new HashMap<String, Grid>();
        for(String module:signal.keySet()){
            Grid g = new Grid(signal.get(module),signalT.get(module),temporalT.get(module));
            nclusters.add(getClusterIndices(g, signalT.get(module)));
            gridMap.put(module, g);
        }
        
        List<Set<Integer>> finalCluster = getNDimclusterList(nclusters);
        List<TreeNode> clusterSTLCollection = new ArrayList<TreeNode>();
        for(Set<Integer> cluster: finalCluster){
            List<TreeNode> stlList = new ArrayList<TreeNode>();
            for(String module:gridMap.keySet()){
                Set<Signal> signalCluster = getSignalsFromClusterIndices(gridMap.get(module),cluster);
                stlList.add(reduceToSingleConjunction(getClusterSTL(module,signalCluster, gridMap.get(module).getXIncrement(),gridMap.get(module).getYIncrement(),gridMap.get(module).getxMax())));
            }
            clusterSTLCollection.add(reduceToSingleConjunction(stlList));
        }
        return reduceToSingleDisjunction(clusterSTLCollection);
    }
    
    public static List<Set<Integer>> getNDimclusterList(List<List<Set<Integer>>> nclusters){
        
        if(nclusters.size() == 1){
            return nclusters.get(0);
        }
        
        List<List<Set<Integer>>> finalcluster = new ArrayList<List<Set<Integer>>>();
        finalcluster.addAll(nclusters);
        while(finalcluster.size() > 1){
            List<List<Set<Integer>>> tempList = new ArrayList<List<Set<Integer>>>();
            tempList.addAll(finalcluster);
            finalcluster = new ArrayList<List<Set<Integer>>>();
            for(int i =0; i < tempList.size()-1; i+= 2){
                finalcluster.add(updateClusterList(tempList.get(i),tempList.get(i+1)));
            }
            if((tempList.size() % 2) == 1){
                finalcluster.add(tempList.get(tempList.size()-1));
            }
        }
        
        return finalcluster.get(0);
    }
    
}
