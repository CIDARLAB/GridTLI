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
import java.util.ArrayList;
import java.util.List;
import org.cidarlab.gridtli.DOM.Point;
import org.cidarlab.gridtli.DOM.Signal;

/**
 *
 * @author prash
 */
public class Validation {
    
    public static double getRobustness(TreeNode stl, Signal s){
        double r = 0;
        boolean first = true;
        List<TreeNode> disjunctions = Validation.getDisjunctionLeaves(stl);
        //System.out.println("Number of Disjunctions :: " + disjunctions.size());
        for(TreeNode node:disjunctions){
            //System.out.println("New Conjunction Node");
            double val = getConjunctionNodeRobustness(node,s);
            
            //System.out.println("Conjunction Node Robustness :: " + val);
            //Get max
            if(first){
                first = false;
                r = val;
            } else {
                if(val > r){
                    r = val;
                }
            }
        }
        return r;
    }
    
    private static double getConjunctionNodeRobustness(TreeNode stl, Signal s){
        double r =0;
        boolean first = true;
        List<TreeNode> alwaysNodes = new ArrayList<TreeNode>();
        alwaysNodes = getConjunctionLeaves(stl);
        for(TreeNode node:alwaysNodes){
            AlwaysNode always = (AlwaysNode)node;
            double val = getAlwaysNodeRobustness(always,s);
            //System.out.println("Always Node            :: " + always);
            //System.out.println("Always Node Robustness :: " + val);
                
            if(first){
                r = val;
                first = false;
            } else {
                if(val < r){
                    r = val;
                }
            }
        }
        
        return r;
    }
    
    private static double getAlwaysNodeRobustness(AlwaysNode stl, Signal s){
        
        //boolean condition = false;
        //if(stl.toString().equals("(G[4.5,5.0](x <= 30.0))")){
        //   condition = true;
        //}
        
        List<Point> covering = getCoveringPoints(stl,s);
        //if(condition){
        //    System.out.println("COVERING POINTS :: " + covering);
        //}
        if (covering.get(0).getX() < stl.low) {
            covering.set(0, getInterpolation(covering.get(0), covering.get(1), stl.low));
        } 
        if(covering.get(covering.size()-1).getX() > stl.high){
            covering.set(covering.size()-1, getInterpolation(covering.get(covering.size()-2),covering.get(covering.size()-1), stl.high));
        }
        double r=0;
        boolean first = true;
        for(Point p:covering){
            double val =0;
            LinearPredicateLeaf lpf = (LinearPredicateLeaf) stl.child;
            if(lpf.rop.equals(RelOperation.LE) || lpf.rop.equals(RelOperation.LT)){
                val = lpf.threshold - p.getY();
            } else if(lpf.rop.equals(RelOperation.GE) || lpf.rop.equals(RelOperation.GT)){
                val = p.getY() - lpf.threshold;
            }
            
            if(first){
                r = val;
                first = false;
            } else {
                if(val<r){
                    r = val;
                }
            }
        }
        //System.out.println(r);
        return r;
    }
    
    private static Point getInterpolation(Point p1, Point p2, double x){
        double slope = (p1.getY() - p2.getY()) / (p1.getX() - p2.getX());
        double y = ((x-p1.getX()) * slope)  + p1.getY();
        return new Point(x,y);
    }
    
    private static List<Point> getCoveringPoints(AlwaysNode stl, Signal s){
        double low = stl.low;
        double high = stl.high;
        //System.out.println("Low ::" + low);
        //System.out.println("High ::" + high);
        //System.out.println(s.getPoints());
        List<Point> points = new ArrayList<Point>();
        boolean started = false;
        Point lastP = null;
        for(Point p:s.getPoints()){
            if(started){
                if(p.getX() >= high){
                    points.add(p);
                    break;
                }
                points.add(p);
            } else {
                if(p.getX() < low){
                    lastP = p;
                } else {
                    started = true;
                    if (p.getX() > low) {
                        if (lastP != null) {
                            points.add(lastP);
                        }
                    }
                    points.add(p);
                    if(p.getX() >= high){
                        break;
                    }
                }
            }
        }
        if(!started){
            points.add(lastP);
        }
        
        return points;
    }
    
    private static List<TreeNode> getConjunctionLeaves(TreeNode stl){
        List<TreeNode> nodes = new ArrayList<TreeNode>();
        getConjunctionLeaves(stl,nodes);
        return nodes;
    }
    
    private static void getConjunctionLeaves(TreeNode stl, List<TreeNode> list){
        if(stl instanceof ConjunctionNode){
            ConjunctionNode cnode = (ConjunctionNode) stl;
            getConjunctionLeaves(cnode.left,list);
            getConjunctionLeaves(cnode.right,list);
        } else {
            list.add(stl);
        }
    }
    
    private static List<TreeNode> getDisjunctionLeaves(TreeNode stl){
        List<TreeNode> nodes = new ArrayList<TreeNode>();
        getDisjunctionLeaves(stl,nodes);
        return nodes;
    }
    
    private static void getDisjunctionLeaves(TreeNode stl, List<TreeNode> list){
        if(stl instanceof DisjunctionNode){
            DisjunctionNode cnode = (DisjunctionNode) stl;
            getDisjunctionLeaves(cnode.left,list);
            getDisjunctionLeaves(cnode.right,list);
        } else {
            list.add(stl);
        }
    }
    
}
