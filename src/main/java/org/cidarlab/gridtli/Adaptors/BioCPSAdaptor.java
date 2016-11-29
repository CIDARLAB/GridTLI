/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.gridtli.Adaptors;

import hyness.stl.AlwaysNode;
import hyness.stl.ConjunctionNode;
import hyness.stl.LinearPredicateLeaf;
import hyness.stl.RelOperation;
import hyness.stl.TreeNode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.cidarlab.gridtli.DOM.Grid;
import org.cidarlab.gridtli.DOM.Signal;
import org.cidarlab.gridtli.TLI.TemporalLogicInference;
import static org.cidarlab.gridtli.TLI.TemporalLogicInference.cluster;
import org.cidarlab.gridtli.TLI.Utilities;

/**
 *
 * @author prash
 */
public class BioCPSAdaptor {
    
    private static String generateGurobiHeader() {
        String header = "# Constraints from TLI job 7711075\n"
                + "from gurobipy import GRB, LinExpr\n"
                + "## HELPER FUNCTION\n"
                + "def check_timepoints(timepoints, T_whole, constrSet):\n"
                + "    check_T = True\n"
                + "    for t in timepoints:\n"
                + "        if t not in T_whole:\n"
                + "            print \"Error: T_whole must contain\",t,\"for\",constrSet,\"constraints.\"\n"
                + "            check_T = False\n"
                + "    return check_T\n";
        return header;
    }
    
    private static String generateGurobiBlock(List<TreeNode> alwaysNodes, String module, String xSignal, String signals){
        String gurobi = "";
        String tab = "    ";
        String timeIntervals = "";
        
        for(int i=0;i< alwaysNodes.size()-1; i++){
            AlwaysNode anode = (AlwaysNode) alwaysNodes.get(i);
            timeIntervals += anode.low + ",";
            timeIntervals += anode.high + ",";
            gurobi += "# " + anode.toString() + " &&\n";
        }
        
        AlwaysNode lastNode = (AlwaysNode) alwaysNodes.get(alwaysNodes.size()-1);
        timeIntervals += lastNode.low + ",";
        timeIntervals += lastNode.high;
        gurobi += "# " + lastNode.toString() + "\n";
        
        
        gurobi += ("def " + module + "(m,r," + signals + ",i,j,T_whole):\n");
        gurobi += tab + "# check that T_whole contains the required interval";
        gurobi += (tab + "check_T([" + timeIntervals  + "], T_whole, \"" + module + "\")\n");
        gurobi += tab + "if not check_T:\n";
        gurobi += tab + tab + "return check_T\n";
        gurobi += tab + "# add constraints\n";
        for(TreeNode node:alwaysNodes){
            AlwaysNode anode = (AlwaysNode) node;
            gurobi += tab + "T_constr = T_whole[T_whole.index(" + anode.low + "):T_whole.index(" + anode.high + ")+1]\n";
            gurobi += tab + "for t in T_constr:\n";
            gurobi += tab + tab + "m.addConstr(" + xSignal + "[(i,j,t)] ";
            LinearPredicateLeaf lpLeaf = (LinearPredicateLeaf) anode.child;
            String sign = "";
            if(lpLeaf.rop.equals(RelOperation.GE) || lpLeaf.rop.equals(RelOperation.GT)){
                gurobi += ">= ";
                sign = "+ ";
            } else {
                gurobi += "<= ";
                sign = "- ";
            }
            gurobi += lpLeaf.threshold + " " + sign + "r)\n";
            
        }
        
        gurobi += tab + "m.update()\n";
        gurobi += tab + "return check_T\n";
        return gurobi;
    }
    
    private static String generateGurobiPickConstraint(List<String> moduleStrings, String signals){
        String gurobi = "";
        String tab = "    ";
        
        String arguments = "(m, r, " + signals + ", i, j, T_whole)";
        
        gurobi += "## PICK CONSTRAINT SET FOR NODE\n";
        gurobi += "def pick_set(pick_path, m, r, " + signals + ", i, j, T_whole):\n";
        gurobi += tab + "check_T = True\n";
        for(int i=0; i<moduleStrings.size(); i++){
            if(i==0){
                gurobi += tab + "if pick_path == " + i + ":\n";
            } else {
                gurobi += tab + "elif pick_path == " + i + ":\n";
            }
            gurobi += tab + tab + "check_T = " + moduleStrings.get(i) + arguments + "\n";
        }
        gurobi += tab + "else:\n";
        gurobi += tab + tab + "print \"There is no pick_path\",pick_path\n";
        gurobi += tab + tab + "exit();\n";
        gurobi += tab + "if not check_T:\n";
        gurobi += tab + tab + "print \"Fix T_whole before proceeding.\"\n";
        gurobi += tab + tab + "exit();\n";
        
        return gurobi;
    }
    
    public static String generateGurobiConstraints(List<String> files, double xThreshold, double yThreshold, double clusterThreshold){
        String gurobi = "";
        String temp = "    ";
        List<Set<Signal>> clusters = new ArrayList<Set<Signal>>();
        gurobi += generateGurobiHeader();
        List<List<TreeNode>> clusterSTL_List = new ArrayList<List<TreeNode>>();
        List<String> xSignals = new ArrayList<String>();
        List<String> moduleStrings = new ArrayList<String>();
        
        Map<String,List<String>> moduleStringMap = new HashMap<String,List<String>>();
        Map<String,List<List<TreeNode>>> clusterSTLMap = new HashMap<String,List<List<TreeNode>>>();
        
        List<String> allModuleStrings = new ArrayList<String>();
        
        String signalString = "";
        int count =1;
        for(String file:files){
            List<Signal> signals = Utilities.getSignalsBioCPS(file);
            Grid grid = new Grid(signals, xThreshold, yThreshold);
            clusters = TemporalLogicInference.cluster(grid, clusterThreshold);
            String module = "module_" + count;
            String xSignal = "x_" + count;
            xSignals.add(xSignal);
            int mCount =0;
            moduleStrings = new ArrayList<>();
            clusterSTL_List = new ArrayList<>();
            for(Set<Signal> cluster:clusters){
                moduleStrings.add(module + "_" + mCount);
                clusterSTL_List.add(TemporalLogicInference.getClusterSTL(grid.getXSignal(), cluster, grid.getXIncrement(), grid.getYIncrement(), grid.getXLowerLimit(), grid.getXUpperLimit(), grid.getYLowerLimit(), grid.getYUpperLimit(), clusterThreshold));
                mCount++;
            }
            allModuleStrings.addAll(moduleStrings);
            moduleStringMap.put(xSignal, moduleStrings);
            clusterSTLMap.put(xSignal, clusterSTL_List);
            count++;
        }
        
        for(int i=0;i<(xSignals.size()-1);i++){
            signalString += xSignals.get(i) + ", ";
        }
        signalString += xSignals.get(xSignals.size()-1);
        
        
        
        gurobi += "## Constraints\n";
        for(String xSignal:xSignals){
            for(int i=0;i<clusterSTLMap.get(xSignal).size();i++){
                gurobi += generateGurobiBlock(clusterSTLMap.get(xSignal).get(i),moduleStringMap.get(xSignal).get(i),xSignal,signalString);
            }
            
        }
        gurobi += generateGurobiPickConstraint(allModuleStrings,signalString);
        return gurobi;
    }
    
}
