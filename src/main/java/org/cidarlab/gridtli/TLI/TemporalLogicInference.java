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
import java.util.List;
import org.cidarlab.gridtli.DOM.Grid;
import org.cidarlab.gridtli.DOM.Signal;
import org.cidarlab.gridtli.DOM.SubGrid;

/**
 *
 * @author prash
 */
public class TemporalLogicInference {
    
    public static void main(String[] args) {
        
    }
    
    
    public static List<List<Signal>> cluster(Grid grid){
        List<List<Signal>> cluster = new ArrayList<List<Signal>>();
        
        
        
        return cluster;
    }
    
    
    public static STLflat getSTL(Grid grid) {
        
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
        //System.out.println(stl.toString());
        return stl;
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
    
}
