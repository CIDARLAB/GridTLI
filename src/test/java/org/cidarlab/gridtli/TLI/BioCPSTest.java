/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.gridtli.TLI;

import org.cidarlab.gridtli.tli.TemporalLogicInference;
import org.cidarlab.gridtli.tli.Utilities;
import hyness.stl.TreeNode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.cidarlab.gridtli.dom.Grid;
import org.cidarlab.gridtli.dom.Signal;
import org.cidarlab.gridtli.adaptors.JavaPlotAdaptor;
import org.junit.Test;

/**
 *
 * @author prash
 */
public class BioCPSTest {

    public static Map<String, Map<String, List<Signal>>> binDependantSignals(Map<String, Map<String, Map<String, Map<String, List<Signal>>>>> map, boolean collapsedModule) {
        Map<String, Map<String, List<Signal>>> moduleSignals = new HashMap();
        if (collapsedModule) {
            for (String module : map.keySet()) {
                for (String sm : map.get(module).keySet()) {
                    for (String plasmid : map.get(module).get(sm).keySet()) {
                        if (!moduleSignals.containsKey(module)) {
                            moduleSignals.put(module, new HashMap());
                        }
                        if (!moduleSignals.get(module).containsKey("input")) {
                            moduleSignals.get(module).put("input", new ArrayList<Signal>());
                        }
                        if (!moduleSignals.get(module).containsKey("output")) {
                            moduleSignals.get(module).put("output", new ArrayList<Signal>());
                        }
                        moduleSignals.get(module).get("input").addAll(map.get(module).get(sm).get(plasmid).get("input"));
                        moduleSignals.get(module).get("output").addAll(map.get(module).get(sm).get(plasmid).get("output"));
                    }
                }
            }
        } else {
            for (String module : map.keySet()) {
                for (String sm : map.get(module).keySet()) {
                    String mName = module + ":" + sm;
                    if (!moduleSignals.containsKey(mName)) {
                        moduleSignals.put(mName, new HashMap());
                    }
                    for (String plasmid : map.get(module).get(sm).keySet()) {
                        if (!moduleSignals.get(mName).containsKey("input")) {
                            moduleSignals.get(mName).put("input", new ArrayList<Signal>());
                        }
                        if (!moduleSignals.get(mName).containsKey("output")) {
                            moduleSignals.get(mName).put("output", new ArrayList<Signal>());
                        }
                        moduleSignals.get(mName).get("input").addAll(map.get(module).get(sm).get(plasmid).get("input"));
                        moduleSignals.get(mName).get("output").addAll(map.get(module).get(sm).get(plasmid).get("output"));
                    }
                }
            }
        }
        return moduleSignals;
    }

    private double _threshold = 5;
    private double _xthreshold = 10;
    private double _ythreshold = 5;
    
    //@Test
    public void testiBioSimGetSTLfromModulesHigh() {

        double threshold = 5;
        double xthreshold = 10;
        double ythreshold = 5;

        for (int k = 0; k < 7; k++) {
            switch (k) {
                case 0:
                    threshold = 1;
                    xthreshold = 1;
                    ythreshold = 1;
                    break;
                case 1:
                    threshold = 1;
                    xthreshold = 5;
                    ythreshold = 1;
                    break;
                case 2:
                    threshold = 4;
                    xthreshold = 5;
                    ythreshold = 5;
                    break;
                case 3:
                    threshold = 5;
                    xthreshold = 5;
                    ythreshold = 5;
                    break;
                case 4:
                    threshold = 1;
                    xthreshold = 10;
                    ythreshold = 1;
                    break;
                case 5:
                    threshold = 4;
                    xthreshold = 10;
                    ythreshold = 5;
                    break;
                case 6:
                    threshold = 5;
                    xthreshold = 10;
                    ythreshold = 5;
                    break;
            }

            for (int i = 1; i <= 3; i++) {
                for (int j = 1; j <= 2; j++) {

                    String filename = i + "-" + j + "-data";
                    String filepath = Utilities.getSampleFilepath() + "ibiosim" + Utilities.getSeparater()+ "newData" + Utilities.getSeparater() + "modulesHighInput" + Utilities.getSeparater() + filename + ".csv";
                    List<Signal> signals = Utilities.getiBioSimSignals(filepath);
                    //double threshold = 10000;

                    Grid grid = new Grid(signals, xthreshold, ythreshold);
                    //TemporalLogicInference.getSTL(grid);

                    String resultFilepath = Utilities.getSampleFilepath() + "ibiosim" + Utilities.getSeparater()+ "newData" + Utilities.getSeparater() + "modulesHighInput" + Utilities.getSeparater() + "TLI" + Utilities.getSeparater()  + ((int)xthreshold) + "_" + ((int)ythreshold) + "_" + ((int)threshold) +  Utilities.getSeparater();
                    Utilities.makeDirectory(resultFilepath);
                    String resultFilename = resultFilepath + filename + "_" + xthreshold + "_" + ythreshold + "_" + threshold + ".txt";
                    Utilities.writeToFile(resultFilename, TemporalLogicInference.getSTL(grid, threshold).toString());

                }
            }

        }

    }
    
    //@Test
    public void testiBioSimGetSTLfromModulesLow() {

        double threshold = 5;
        double xthreshold = 10;
        double ythreshold = 5;

        for (int k = 0; k < 7; k++) {
            switch (k) {
                case 0:
                    threshold = 1;
                    xthreshold = 1;
                    ythreshold = 1;
                    break;
                case 1:
                    threshold = 1;
                    xthreshold = 5;
                    ythreshold = 1;
                    break;
                case 2:
                    threshold = 4;
                    xthreshold = 5;
                    ythreshold = 5;
                    break;
                case 3:
                    threshold = 5;
                    xthreshold = 5;
                    ythreshold = 5;
                    break;
                case 4:
                    threshold = 1;
                    xthreshold = 10;
                    ythreshold = 1;
                    break;
                case 5:
                    threshold = 4;
                    xthreshold = 10;
                    ythreshold = 5;
                    break;
                case 6:
                    threshold = 5;
                    xthreshold = 10;
                    ythreshold = 5;
                    break;
            }

            for (int i = 1; i <= 3; i++) {
                for (int j = 1; j <= 2; j++) {

                    String filename = i + "-" + j + "-data";
                    String filepath = Utilities.getSampleFilepath() + "ibiosim" + Utilities.getSeparater()+ "newData" + Utilities.getSeparater() + "modulesLowInput" + Utilities.getSeparater() + filename + ".csv";
                    List<Signal> signals = Utilities.getiBioSimSignals(filepath);
                    //double threshold = 10000;

                    Grid grid = new Grid(signals, xthreshold, ythreshold);
                    //TemporalLogicInference.getSTL(grid);

                    String resultFilepath = Utilities.getSampleFilepath() + "ibiosim" + Utilities.getSeparater()+ "newData" + Utilities.getSeparater() + "modulesLowInput" + Utilities.getSeparater() + "TLI" + Utilities.getSeparater()  + ((int)xthreshold) + "_" + ((int)ythreshold) + "_" + ((int)threshold) +  Utilities.getSeparater();
                    Utilities.makeDirectory(resultFilepath);
                    String resultFilename = resultFilepath + filename + "_" + xthreshold + "_" + ythreshold + "_" + threshold + ".txt";
                    Utilities.writeToFile(resultFilename, TemporalLogicInference.getSTL(grid, threshold).toString());
                    
                }
            }

        }
    }
    
    //@Test
    public void testiBioSimGetSTLfromCascades() {

        double threshold = 5;
        double xthreshold = 10;
        double ythreshold = 5;

        for (int k = 0; k < 7; k++) {
            switch (k) {
                case 0:
                    threshold = 1;
                    xthreshold = 1;
                    ythreshold = 1;
                    break;
                case 1:
                    threshold = 1;
                    xthreshold = 5;
                    ythreshold = 1;
                    break;
                case 2:
                    threshold = 4;
                    xthreshold = 5;
                    ythreshold = 5;
                    break;
                case 3:
                    threshold = 5;
                    xthreshold = 5;
                    ythreshold = 5;
                    break;
                case 4:
                    threshold = 1;
                    xthreshold = 10;
                    ythreshold = 1;
                    break;
                case 5:
                    threshold = 4;
                    xthreshold = 10;
                    ythreshold = 5;
                    break;
                case 6:
                    threshold = 5;
                    xthreshold = 10;
                    ythreshold = 5;
                    break;
            }
            for (int i = 1; i <= 6; i++) {
                for (int j = 1; j <= 3; j++) {
                    String filename = i + "-" + j + "-data";
                    String filepath = Utilities.getSampleFilepath() + "ibiosim" + Utilities.getSeparater()+ "newData" + Utilities.getSeparater() + "cascades" + Utilities.getSeparater() + filename + ".csv";
                    List<Signal> signals = Utilities.getiBioSimSignals(filepath);;
                    //double threshold = 10000;
                    Grid grid = new Grid(signals, xthreshold, ythreshold);

                    //TemporalLogicInference.getSTL(grid);
                    String resultFilepath = Utilities.getSampleFilepath() + "ibiosim" + Utilities.getSeparater()+ "newData" + Utilities.getSeparater() + "cascades" + Utilities.getSeparater() + "TLI" + Utilities.getSeparater() + ((int)xthreshold) + "_" + ((int)ythreshold) + "_" + ((int)threshold) + Utilities.getSeparater();
                    
                    Utilities.makeDirectory(resultFilepath);
                    String fileResultName = resultFilepath + filename + "_" + xthreshold + "_" + ythreshold + "_" + threshold + ".txt";
                    Utilities.writeToFile(fileResultName, TemporalLogicInference.getSTL(grid, threshold).toString());
                }
            }
        }

    }
    
    //@Test
    public void testGetSTLfromModules() {

        for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= 2; j++) {
                String filename = i + "-" + j + "-data";
                String filepath = Utilities.getSampleFilepath() + "bioCPS" + Utilities.getSeparater() + "modules" + Utilities.getSeparater() + filename + ".csv";
                List<Signal> signals = Utilities.getRowSignals(filepath,false);
                //double threshold = 10000;
                Grid grid = new Grid(signals, _xthreshold, _ythreshold);

                //TemporalLogicInference.getSTL(grid);
                String resultFilepath = Utilities.getSampleFilepath() + "bioCPS" + Utilities.getSeparater() + "modules" + Utilities.getSeparater() + "TLI" + Utilities.getSeparater() + filename + "_" + _xthreshold + "_" + _ythreshold + "_" + _threshold + ".txt";
                Utilities.writeToFile(resultFilepath, TemporalLogicInference.getSTL(grid, _threshold).toString());
            }
        }
    }

    //@Test
    public void testGetSTLfromCascades() {

        for (int i = 1; i <= 6; i++) {
            for (int j = 1; j <= 3; j++) {
                String filename = i + "-" + j + "-data";
                String filepath = Utilities.getSampleFilepath() + "bioCPS" + Utilities.getSeparater() + "cascades" + Utilities.getSeparater() + filename + ".csv";
                List<Signal> signals = Utilities.getRowSignals(filepath,false);
                //double threshold = 10000;
                Grid grid = new Grid(signals, _xthreshold, _ythreshold);

                //TemporalLogicInference.getSTL(grid);
                String resultFilepath = Utilities.getSampleFilepath() + "bioCPS" + Utilities.getSeparater() + "cascades" + Utilities.getSeparater() + "TLI" + Utilities.getSeparater() + filename + "_" + _xthreshold + "_" + _ythreshold + "_" + _threshold + ".txt";
                Utilities.writeToFile(resultFilepath, TemporalLogicInference.getSTL(grid, _threshold).toString());
            }
        }

    }


    //@Test
    public void testModule11Data() {
        String filepath = Utilities.getSampleFilepath() + "bioCPS" + Utilities.getSeparater() + "modules" + Utilities.getSeparater() + "1-1-data.csv";
        List<Signal> signals = Utilities.getRowSignals(filepath,false);
        Grid grid = new Grid(signals, 1, 10000);
        //TemporalLogicInference.getLongSTL(grid);
        JavaPlotAdaptor.plotToFile(JavaPlotAdaptor.visualizeCells(grid.getCell().keySet()), Utilities.getSampleTestFilepath() + "subgrid.png");
        JavaPlotAdaptor.plotToFile(JavaPlotAdaptor.plotGridwithoutCover(grid), Utilities.getSampleTestFilepath() + "gridnoCover.png");
        JavaPlotAdaptor.plotToFile(JavaPlotAdaptor.plotGrid(grid), Utilities.getSampleTestFilepath() + "grid.png");
        //JavaPlotAdaptor.plotToFile(JavaPlotAdaptor.plotGridData1_1(grid), Utilities.getSampleTestFilepath() + "cluster.png");
        
        JavaPlotAdaptor.plotToFile(JavaPlotAdaptor.plotSignals(grid), Utilities.getSampleTestFilepath() + "signals.png");

        List<Signal> cluster = new ArrayList<Signal>();
        for (int i = 0; i < 10; i++) {
            cluster.add(signals.get(i));
        }
//        Grid gridcluster = new Grid(cluster,1,10000);
//        JavaPlotAdaptor.plotToFile(JavaPlotAdaptor.plotGrid(gridcluster), Utilities.getSampleTestFilepath() + "gridcluster.png");
//        System.out.println("\n\n\n\n\n\n");
//        for(TreeNode node: TemporalLogicInference.getClusterSTL(grid.getXSignal(), new HashSet<Signal>(cluster), grid.getXIncrement(), grid.getYIncrement(), grid.getXLowerLimit(), grid.getXUpperLimit(), grid.getYLowerLimit(), grid.getYUpperLimit(), 10000)){
//            System.out.println(node.toString());
//        }

        System.out.println(TemporalLogicInference.getSTL(grid, 10000));

    }
    

    //@Test
    public void testFadingDrop() {
        String filepathAHL = Utilities.getSampleFilepath() + "bioCPS" + Utilities.getSeparater() + "fadingDrop" + Utilities.getSeparater() + "AHL-data.csv";
        String filepathAHL_neighbour = Utilities.getSampleFilepath() + "bioCPS" + Utilities.getSeparater() + "fadingDrop" + Utilities.getSeparater() + "AHL_neighborAvg-data.csv";
        String filepathGFP = Utilities.getSampleFilepath() + "bioCPS" + Utilities.getSeparater() + "fadingDrop" + Utilities.getSeparater() + "GFP-data.csv";
        getFormulaAndPlots("AHL", filepathAHL, 5, 0.01);
        //getFormulaAndPlots("AHL_nAVG",filepathAHL_neighbour,1,0.01);
        //getFormulaAndPlots("GFP",filepathGFP,1,0.01);

    }

    public static void getFormulaAndPlots(String filename, String filepath, double xThreshHold, double yThreshHold) {

        List<Signal> signals = Utilities.getRowSignals(filepath,false);
        Grid grid = new Grid(signals, xThreshHold, yThreshHold);
        //TemporalLogicInference.getSTL(grid);
        //JavaPlotAdaptor.plotToFile(JavaPlotAdaptor.visualizeCells(grid.getSubGrid()), Utilities.getSampleTestFilepath() + "subgrid" + filename +  ".png");
        //JavaPlotAdaptor.plotToFile(JavaPlotAdaptor.plotGridwithoutCover(grid), Utilities.getSampleTestFilepath() + "gridnoCover" + filename + ".png");
        JavaPlotAdaptor.plotToFile(JavaPlotAdaptor.plotGrid(grid), Utilities.getSampleTestFilepath() + "grid" + filename + ".png");

    }
    
    @Test
    public void testIBioSimNDim(){
        BioCPSTest test = new BioCPSTest();
        test.testIBioSimLowModules();
        test.testIBioSimHighModules();
        test.testIBioSimCascades();
    }
    
    public void testIBioSimLowModules(){
        
        for (int i = 1; i <= 3; i++) {
            Map<String, List<Signal>> nsig = new HashMap<String, List<Signal>>();
            for (int j = 1; j <= 2; j++) {
                String file1 = i + "-" + j + "-data";
                String filepath1 = Utilities.getSampleFilepath() + "ibiosim" + Utilities.getSeparater() + "newData" + Utilities.getSeparater() + "modulesLowInput" + Utilities.getSeparater() + file1 + ".csv";
                List<Signal> m1 = Utilities.getiBioSimSignals(filepath1);
                String signalVar = ioChar(j) + i;
                nsig.put(signalVar, m1);
            }
            String resultFile = Utilities.getSampleFilepath() + "ibiosim" + Utilities.getSeparater() + "newData" + Utilities.getSeparater() + "modulesLowInput" + Utilities.getSeparater() + "ndim_low" + Utilities.getSeparater() + i + "-data.txt";
            TreeNode stl = TemporalLogicInference.getNDimSTL(nsig, 0.05);
            Utilities.writeToFile(resultFile, stl.toString());
            //System.out.println("STL :: " + stl);
        }
    }
    
    public void testIBioSimHighModules(){
        
        for (int i = 1; i <= 3; i++) {
            Map<String, List<Signal>> nsig = new HashMap<String, List<Signal>>();
            for (int j = 1; j <= 2; j++) {
                String file1 = i + "-" + j + "-data";
                String filepath1 = Utilities.getSampleFilepath() + "ibiosim" + Utilities.getSeparater() + "newData" + Utilities.getSeparater() + "modulesHighInput" + Utilities.getSeparater() + file1 + ".csv";
                List<Signal> m1 = Utilities.getiBioSimSignals(filepath1);
                String signalVar = ioChar(j) + i;
                nsig.put(signalVar, m1);
            }
            String resultFile = Utilities.getSampleFilepath() + "ibiosim" + Utilities.getSeparater() + "newData" + Utilities.getSeparater() + "modulesHighInput" + Utilities.getSeparater() + "ndim_high" + Utilities.getSeparater() + i + "-data.txt";
            TreeNode stl = TemporalLogicInference.getNDimSTL(nsig, 0.05);
            Utilities.writeToFile(resultFile, stl.toString());
            //System.out.println("STL :: " + stl);
        }
    }
    
    public void testIBioSimCascades(){
        
        for (int i = 1; i <= 6; i++) {
            Map<String, List<Signal>> nsig = new HashMap<String, List<Signal>>();
            for (int j = 1; j <= 3; j++) {
                String file1 = i + "-" + j + "-data";
                String filepath1 = Utilities.getSampleFilepath() + "ibiosim" + Utilities.getSeparater() + "newData" + Utilities.getSeparater() + "cascades" + Utilities.getSeparater() + file1 + ".csv";
                List<Signal> m1 = Utilities.getiBioSimSignals(filepath1);
                String signalVar = icoChar(j) + i;
                nsig.put(signalVar, m1);
            }
            String resultFile = Utilities.getSampleFilepath() + "ibiosim" + Utilities.getSeparater() + "newData" + Utilities.getSeparater() + "cascades" + Utilities.getSeparater() + "ndim_cascades" + Utilities.getSeparater() + i + "-data.txt";
            TreeNode stl = TemporalLogicInference.getNDimSTL(nsig, 0.05);
            Utilities.writeToFile(resultFile, stl.toString());
            //System.out.println("STL :: " + stl);
        }
    }
    
    private static String ioChar(int i){
        switch(i){
            case 1: return "i";
            case 2: return "o";
        }
        return "";
    }
    
    private static String icoChar(int i){
        switch(i){
            case 1: return "i";
            case 2: return "c";
            case 3: return "o";
        }
        return "";
    }
    
    //@Test
    public void testBinDependant(){
        String filepath = Utilities.getSampleFilepath() + "bin_dependant";
        Map<String,Map<String,Map<String,Map<String,List<Signal>>>>> map = Utilities.binDependantWalk(filepath);
        Map<String, Map<String,List<Signal>>> collapsedSignals = binDependantSignals(map, true);
        Map<String, Map<String,List<Signal>>> expandedSignals = binDependantSignals(map, false);

        
        
        for(String module: expandedSignals.keySet()){
            System.out.println("Module ::" + module);
            TreeNode stl = TemporalLogicInference.getNDimSTL( expandedSignals.get(module) , 0.5);
            System.out.println("STL ::" + stl);
        }
        
        for(String module: collapsedSignals.keySet()){
            System.out.println("Module ::" + module);
            TreeNode stl = TemporalLogicInference.getNDimSTL( collapsedSignals.get(module) , 0.05);
            System.out.println("STL ::" + stl);
        }
    }
    
    @Test
    public void testBinDependantWalk(){
        String filepath = Utilities.getSampleFilepath() + "bin_dependant";
        Map<String,Map<String,Map<String,Map<String,List<Signal>>>>> map = Utilities.binDependantWalk(filepath);
        
        System.out.println(map.keySet());
        for(String key:map.keySet()){
            System.out.println(map.get(key).keySet());
            for(String innerKey:map.get(key).keySet()){
                System.out.println("One Level Deeper :: " + map.get(key).get(innerKey).keySet());
            }
        }
    }

}
