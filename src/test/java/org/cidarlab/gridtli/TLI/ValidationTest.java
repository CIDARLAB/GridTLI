/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.gridtli.TLI;

import org.cidarlab.gridtli.tli.TemporalLogicInference;
import org.cidarlab.gridtli.tli.Utilities;
import org.cidarlab.gridtli.tli.Validation;
import hyness.stl.AlwaysNode;
import hyness.stl.ConjunctionNode;
import hyness.stl.DisjunctionNode;
import hyness.stl.LinearPredicateLeaf;
import hyness.stl.RelOperation;
import hyness.stl.TreeNode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.cidarlab.gridtli.dom.Grid;
import org.cidarlab.gridtli.dom.Point;
import org.cidarlab.gridtli.dom.Signal;
import org.cidarlab.gridtli.adaptors.JavaPlotAdaptor;
import org.cidarlab.gridtli.dom.TLIException;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author prash
 */
public class ValidationTest {

    @Test
    public void testRobustness(){
        String columnData = Utilities.getSampleFilepath() + "demo" + Utilities.getSeparater() + "column_data.csv";
        List<Signal> signals = Utilities.getiBioSimSignals(columnData);
        double xthreshold = 10;
        double ythreshold = 10;
        double clusterThreshold = 10;
        
        
        Grid grid = new Grid(signals, xthreshold, ythreshold);
        
        TreeNode stl =  TemporalLogicInference.getSTL(grid, clusterThreshold);
        
        System.out.println(stl);
        System.out.println("\n\n\n");
        int count =0;
        for(Signal s: signals){
            System.out.println("Signal " + count++);
            System.out.println("Final Robustness Value:: " + Validation.getRobustness(stl, s));
        }
        String demoFilepath = Utilities.getSampleFilepath() + "demo" + Utilities.getSeparater();
        //JavaPlotAdaptor.plotToFile(JavaPlotAdaptor.visualizeSubGrid(grid.getSubGrid().keySet()), demoFilepath + "subgrid_Column.png");
        //JavaPlotAdaptor.plotToFile(JavaPlotAdaptor.plotGridwithoutCover(grid), demoFilepath + "gridnoCover_Column.png");
        //JavaPlotAdaptor.plotToFile(JavaPlotAdaptor.plotGrid(grid), demoFilepath + "grid_Column.png");
//        List<TreeNode> nodes = Validation.getDisjunctionLeaves(stl);
//        System.out.println("Number of Conjunction Nodes" + nodes.size());
//        System.out.println(nodes);
        
    }
    
    //@Test
    public void testSampleRobustness() throws TLIException{
        
        AlwaysNode a11g = new AlwaysNode(new LinearPredicateLeaf(RelOperation.GE,"x",0),0,3);
        AlwaysNode a11l = new AlwaysNode(new LinearPredicateLeaf(RelOperation.LE,"x",3),0,3);
        
        AlwaysNode a12g = new AlwaysNode(new LinearPredicateLeaf(RelOperation.GE,"x",2),3,7);
        AlwaysNode a12l = new AlwaysNode(new LinearPredicateLeaf(RelOperation.LE,"x",6),3,7);
        
        AlwaysNode a13g = new AlwaysNode(new LinearPredicateLeaf(RelOperation.GE,"x",4),7,18);
        AlwaysNode a13l = new AlwaysNode(new LinearPredicateLeaf(RelOperation.LE,"x",8),7,18);
        
        AlwaysNode a14g = new AlwaysNode(new LinearPredicateLeaf(RelOperation.GE,"x",2),18,21);
        AlwaysNode a14l = new AlwaysNode(new LinearPredicateLeaf(RelOperation.LE,"x",6),18,21);
    
        AlwaysNode a15g = new AlwaysNode(new LinearPredicateLeaf(RelOperation.GE,"x",0),21,25);
        AlwaysNode a15l = new AlwaysNode(new LinearPredicateLeaf(RelOperation.LE,"x",3),21,25);
    
        
        //Conjunction 2
        AlwaysNode a21g = new AlwaysNode(new LinearPredicateLeaf(RelOperation.GE,"x",0),0,3);
        AlwaysNode a21l = new AlwaysNode(new LinearPredicateLeaf(RelOperation.LE,"x",8),0,3);
        
        AlwaysNode a22g = new AlwaysNode(new LinearPredicateLeaf(RelOperation.GE,"x",7),3,6);
        AlwaysNode a22l = new AlwaysNode(new LinearPredicateLeaf(RelOperation.LE,"x",15),3,6);
        
        AlwaysNode a23g = new AlwaysNode(new LinearPredicateLeaf(RelOperation.GE,"x",13),6,18);
        AlwaysNode a23l = new AlwaysNode(new LinearPredicateLeaf(RelOperation.LE,"x",17),6,18);
        
        AlwaysNode a24g = new AlwaysNode(new LinearPredicateLeaf(RelOperation.GE,"x",7),18,22);
        AlwaysNode a24l = new AlwaysNode(new LinearPredicateLeaf(RelOperation.LE,"x",15),18,22);
    
        AlwaysNode a25g = new AlwaysNode(new LinearPredicateLeaf(RelOperation.GE,"x",0),22,25);
        AlwaysNode a25l = new AlwaysNode(new LinearPredicateLeaf(RelOperation.LE,"x",8),22,25);
    
        List<TreeNode> cn1 = new ArrayList<TreeNode>();
        List<TreeNode> cn2 = new ArrayList<TreeNode>();
        
        cn1.add(a11l);
        cn1.add(a11g);
        cn1.add(a12l);
        cn1.add(a12g);
        cn1.add(a13l);
        cn1.add(a13g);
        cn1.add(a14l);
        cn1.add(a14g);
        cn1.add(a15l);
        cn1.add(a15g);
        
        cn2.add(a21l);
        cn2.add(a21g);
        cn2.add(a22l);
        cn2.add(a22g);
        cn2.add(a23l);
        cn2.add(a23g);
        cn2.add(a24l);
        cn2.add(a24g);
        cn2.add(a25l);
        cn2.add(a25g);
        
        
        DisjunctionNode stl = new DisjunctionNode(TemporalLogicInference.reduceToSingleConjunction(cn1),TemporalLogicInference.reduceToSingleConjunction(cn2));
        
        Point p0 = new Point(0,"t",0,"x");
        Point p1 = new Point(2,"t",1,"x");
        Point p2 = new Point(4,"t",2,"x");
        Point p3 = new Point(6,"t",5,"x");
        Point p4 = new Point(10,"t",7,"x");
        Point p5 = new Point(16,"t",6,"x");
        Point p6 = new Point(19,"t",5,"x");
        Point p7 = new Point(22,"t",2,"x");
        Point p8 = new Point(24,"t",0,"x");
        
        List<Point> points = new ArrayList<Point>();
        points.add(p0);
        points.add(p1);
        points.add(p2);
        points.add(p3);
        points.add(p4);
        points.add(p5);
        points.add(p6);
        points.add(p7);
        points.add(p8);
        Signal s = new Signal(points);
        double r  = Validation.getRobustness(stl,s);
        System.out.println("Robustness Final value :: " + r);
    }
    
    //@Test
    public void setTest(){
        List<Set<Integer>> l1 = new ArrayList<Set<Integer>>();
        List<Set<Integer>> l2 = new ArrayList<Set<Integer>>();
        List<Set<Integer>> l3 = new ArrayList<Set<Integer>>();
        List<Set<Integer>> l4 = new ArrayList<Set<Integer>>();
        List<Set<Integer>> l5 = new ArrayList<Set<Integer>>();
        
        Set<Integer> l1s1 = new HashSet<Integer>();
        Set<Integer> l1s2 = new HashSet<Integer>();
        Set<Integer> l1s3 = new HashSet<Integer>();
        
        Set<Integer> l2s1 = new HashSet<Integer>();
        Set<Integer> l2s2 = new HashSet<Integer>();
        Set<Integer> l2s3 = new HashSet<Integer>();
        
        Set<Integer> l3s1 = new HashSet<Integer>();
        Set<Integer> l3s2 = new HashSet<Integer>();
        Set<Integer> l3s3 = new HashSet<Integer>();
        
        Set<Integer> l4s1 = new HashSet<Integer>();
        Set<Integer> l4s2 = new HashSet<Integer>();
        
        Set<Integer> l5s1 = new HashSet<Integer>();
        
        
        //List 1
        l1s1.add(3);
        l1s1.add(4);
        l1s1.add(5);
        
        l1s2.add(6);
        l1s2.add(1);
        l1s2.add(2);
        
        l1s3.add(8);
        l1s3.add(9);
        l1s3.add(7);
        
        //List 2
        l2s1.add(1);
        l2s1.add(2);
        l2s1.add(6);
        
        l2s2.add(3);
        l2s2.add(4);
        l2s2.add(5);
        
        l2s3.add(7);
        l2s3.add(8);
        l2s3.add(9);
        
        //List 3
        l3s1.add(3);
        l3s1.add(4);
        l3s1.add(5);
        
        l3s2.add(6);
        l3s2.add(1);
        
        
        l3s3.add(8);
        l3s3.add(9);
        l3s3.add(7);
        l3s3.add(2);
        
        //List 4
        l4s1.add(3);
        l4s1.add(4);
        l4s1.add(5);
        l4s1.add(8);
        l4s1.add(9);
        l4s1.add(7);
        
        l4s2.add(6);
        l4s2.add(1);
        l4s2.add(2);
        
        //List 5
        l5s1.add(3);
        l5s1.add(4);
        l5s1.add(5);
        l5s1.add(8);
        l5s1.add(9);
        l5s1.add(7);
        l5s1.add(6);
        l5s1.add(1);
        //l5s1.add(2);
        
        
        l1.add(l1s1);
        l1.add(l1s2);
        l1.add(l1s3);
        
        l2.add(l2s1);
        l2.add(l2s2);
        l2.add(l2s3);
        
        l3.add(l3s1);
        l3.add(l3s2);
        l3.add(l3s3);
        
        l4.add(l4s1);
        l4.add(l4s2);
        
        l5.add(l5s1);
        
        
        System.out.println("L1::" + l1);
        System.out.println("L2::" + l2);
        System.out.println("L3::" + l3);
        System.out.println("L4::" + l4);
        System.out.println("L5::" + l5);
        
        List<Set<Integer>> temp1 = new ArrayList<Set<Integer>>(l1);
        System.out.println("Compare l1 & l2");
        boolean res1 = temp1.retainAll(l2);
        System.out.println(res1);
        System.out.println(temp1);
        
        List<Set<Integer>> temp2 = new ArrayList<Set<Integer>>(l1);
        System.out.println("Compare l1 & l3");
        boolean res2 = temp2.retainAll(l3);
        System.out.println(res2);
        System.out.println(temp2);
        
        List<Set<Integer>> temp3 = new ArrayList<Set<Integer>>(l1);
        System.out.println("Compare l1 & l5");
        boolean res3 = temp3.retainAll(l5);
        System.out.println(res3);
        System.out.println(temp3);
        
        List<Set<Integer>> temp4 = new ArrayList<Set<Integer>>(l5);
        System.out.println("Compare l5 & l1");
        boolean res4 = temp4.retainAll(l1);
        System.out.println(res4);
        System.out.println(temp4);
        
        List<Set<Integer>> up1 = TemporalLogicInference.updateClusterList(l1,l2);
        List<Set<Integer>> up2 = TemporalLogicInference.updateClusterList(l1,l3);
        List<Set<Integer>> up3 = TemporalLogicInference.updateClusterList(l1,l4);
        List<Set<Integer>> up4 = TemporalLogicInference.updateClusterList(l1,l5);
        List<Set<Integer>> up5 = TemporalLogicInference.updateClusterList(l2,l3);
        List<Set<Integer>> up6 = TemporalLogicInference.updateClusterList(l5,l1);
        
        System.out.println("L1 + L2");
        System.out.println(l1);
        System.out.println(l2);
        System.out.println(up1);
        System.out.println("=================================");
        
        System.out.println("L1 + L3");
        System.out.println(l1);
        System.out.println(l3);
        System.out.println(up2);
        System.out.println("=================================");
        
        System.out.println("L1 + L4");
        System.out.println(l1);
        System.out.println(l4);
        System.out.println(up3);
        System.out.println("=================================");
        
        System.out.println("L1 + L5");
        System.out.println(l1);
        System.out.println(l5);
        System.out.println(up4);
        System.out.println("=================================");
        
        System.out.println("L2 + L3");
        System.out.println(l2);
        System.out.println(l3);
        System.out.println(up5);
        System.out.println("=================================");
        
        System.out.println("L5 + L1");
        System.out.println(l5);
        System.out.println(l1);
        System.out.println(up6);
        System.out.println("=================================");
        
    }
    
}
