/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.gridtli.TLI;

import hyness.stl.TreeNode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.cidarlab.gridtli.Adaptors.BioCPSAdaptor;
import org.cidarlab.gridtli.DOM.Grid;
import org.cidarlab.gridtli.DOM.Signal;
import org.cidarlab.gridtli.Visualize.JavaPlotAdaptor;
import org.junit.Test;

/**
 *
 * @author prash
 */
public class bioCPSTest {

    private double _threshold = 5;
    private double _xthreshold = 10;
    private double _ythreshold = 5;

    //@Test
    public void testiBioSimGetSTLfromModules() {

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
                    String filepath = Utilities.getResourcesFilepath() + "ibiosim" + Utilities.getSeparater() + "modules" + Utilities.getSeparater() + filename + ".csv";
                    List<Signal> signals = Utilities.getSignalsIBioSim(filepath);
                    //double threshold = 10000;

                    Grid grid = new Grid(signals, xthreshold, ythreshold);
                    //TemporalLogicInference.getSTL(grid);

                    String resultFilepath = Utilities.getResourcesFilepath() + "ibiosim" + Utilities.getSeparater() + "modules" + Utilities.getSeparater() + "TLI" + Utilities.getSeparater()  + ((int)xthreshold) + "_" + ((int)ythreshold) + "_" + ((int)threshold) +  Utilities.getSeparater() + filename + "_" + xthreshold + "_" + ythreshold + "_" + threshold + ".txt";
                    Utilities.writeToFile(resultFilepath, TemporalLogicInference.getSTL(grid, threshold).toString());

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
                    String filepath = Utilities.getResourcesFilepath() + "ibiosim" + Utilities.getSeparater() + "cascades" + Utilities.getSeparater() + filename + ".csv";
                    List<Signal> signals = Utilities.getSignalsIBioSim(filepath);
                    //double threshold = 10000;
                    Grid grid = new Grid(signals, xthreshold, ythreshold);

                    //TemporalLogicInference.getSTL(grid);
                    String resultFilepath = Utilities.getResourcesFilepath() + "ibiosim" + Utilities.getSeparater() + "cascades" + Utilities.getSeparater() + "TLI" + Utilities.getSeparater() + ((int)xthreshold) + "_" + ((int)ythreshold) + "_" + ((int)threshold) + Utilities.getSeparater() + filename + "_" + xthreshold + "_" + ythreshold + "_" + threshold + ".txt";
                    Utilities.writeToFile(resultFilepath, TemporalLogicInference.getSTL(grid, threshold).toString());
                }
            }
        }

    }

    //@Test
    public void testGetSTLfromModules() {

        for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= 2; j++) {
                String filename = i + "-" + j + "-data";
                String filepath = Utilities.getResourcesFilepath() + "bioCPS" + Utilities.getSeparater() + "modules" + Utilities.getSeparater() + filename + ".csv";
                List<Signal> signals = Utilities.getSignalsBioCPS(filepath,false);
                //double threshold = 10000;
                Grid grid = new Grid(signals, _xthreshold, _ythreshold);

                //TemporalLogicInference.getSTL(grid);
                String resultFilepath = Utilities.getResourcesFilepath() + "bioCPS" + Utilities.getSeparater() + "modules" + Utilities.getSeparater() + "TLI" + Utilities.getSeparater() + filename + "_" + _xthreshold + "_" + _ythreshold + "_" + _threshold + ".txt";
                Utilities.writeToFile(resultFilepath, TemporalLogicInference.getSTL(grid, _threshold).toString());
            }
        }
    }

    //@Test
    public void testGetSTLfromCascades() {

        for (int i = 1; i <= 6; i++) {
            for (int j = 1; j <= 3; j++) {
                String filename = i + "-" + j + "-data";
                String filepath = Utilities.getResourcesFilepath() + "bioCPS" + Utilities.getSeparater() + "cascades" + Utilities.getSeparater() + filename + ".csv";
                List<Signal> signals = Utilities.getSignalsBioCPS(filepath,false);
                //double threshold = 10000;
                Grid grid = new Grid(signals, _xthreshold, _ythreshold);

                //TemporalLogicInference.getSTL(grid);
                String resultFilepath = Utilities.getResourcesFilepath() + "bioCPS" + Utilities.getSeparater() + "cascades" + Utilities.getSeparater() + "TLI" + Utilities.getSeparater() + filename + "_" + _xthreshold + "_" + _ythreshold + "_" + _threshold + ".txt";
                Utilities.writeToFile(resultFilepath, TemporalLogicInference.getSTL(grid, _threshold).toString());
            }
        }

    }

    //@Test
    public void testGetGurobi() {
        String module1 = Utilities.getResourcesFilepath() + "bioCPS" + Utilities.getSeparater() + "modules" + Utilities.getSeparater() + "1-1-data.csv";
        String module2 = Utilities.getResourcesFilepath() + "bioCPS" + Utilities.getSeparater() + "modules" + Utilities.getSeparater() + "1-2-data.csv";

        List<String> moduleFiles = new ArrayList<String>();
        moduleFiles.add(module1);
        moduleFiles.add(module2);

        double xThreshold = 1;
        double yThreshold = 10000;
        double clusterThreshold = 10000;
        System.out.println(BioCPSAdaptor.generateGurobiConstraints(moduleFiles, xThreshold, yThreshold, clusterThreshold));

    }

    //@Test
    public void testModule11Data() {
        String filepath = Utilities.getResourcesFilepath() + "bioCPS" + Utilities.getSeparater() + "modules" + Utilities.getSeparater() + "1-1-data.csv";
        List<Signal> signals = Utilities.getSignalsBioCPS(filepath,false);
        Grid grid = new Grid(signals, 1, 10000);
        //TemporalLogicInference.getLongSTL(grid);
        JavaPlotAdaptor.plotToFile(JavaPlotAdaptor.visualizeSubGrid(grid.getSubGrid().keySet()), Utilities.getResourcesTempFilepath() + "subgrid.png");
        JavaPlotAdaptor.plotToFile(JavaPlotAdaptor.plotGridwithoutCover(grid), Utilities.getResourcesTempFilepath() + "gridnoCover.png");
        JavaPlotAdaptor.plotToFile(JavaPlotAdaptor.plotGrid(grid), Utilities.getResourcesTempFilepath() + "grid.png");
        //JavaPlotAdaptor.plotToFile(JavaPlotAdaptor.plotGridData1_1(grid), Utilities.getResourcesTempFilepath() + "cluster.png");
        JavaPlotAdaptor.plotToFile(JavaPlotAdaptor.plotGridData1_1alt(grid), Utilities.getResourcesTempFilepath() + "clusters.png");
        JavaPlotAdaptor.plotToFile(JavaPlotAdaptor.plotGridData1_1alt_1(grid), Utilities.getResourcesTempFilepath() + "cluster1_HIGHLIGHT.png");

        JavaPlotAdaptor.plotToFile(JavaPlotAdaptor.plotSignals(grid), Utilities.getResourcesTempFilepath() + "signals.png");

        List<Signal> cluster = new ArrayList<Signal>();
        for (int i = 0; i < 10; i++) {
            cluster.add(signals.get(i));
        }
//        Grid gridcluster = new Grid(cluster,1,10000);
//        JavaPlotAdaptor.plotToFile(JavaPlotAdaptor.plotGrid(gridcluster), Utilities.getResourcesTempFilepath() + "gridcluster.png");
//        System.out.println("\n\n\n\n\n\n");
//        for(TreeNode node: TemporalLogicInference.getClusterSTL(grid.getXSignal(), new HashSet<Signal>(cluster), grid.getXIncrement(), grid.getYIncrement(), grid.getXLowerLimit(), grid.getXUpperLimit(), grid.getYLowerLimit(), grid.getYUpperLimit(), 10000)){
//            System.out.println(node.toString());
//        }

        System.out.println(TemporalLogicInference.getSTL(grid, 10000));

    }

    //@Test
    public void testClustering() {
        String filepath = Utilities.getResourcesFilepath() + "bioCPS" + Utilities.getSeparater() + "modules" + Utilities.getSeparater() + "1-1-data.csv";
        List<Signal> signals = Utilities.getSignalsBioCPS(filepath,false);
        double threshold = 10000;
        Grid grid = new Grid(signals, 1, threshold);

        //TemporalLogicInference.getSTL(grid);
        List<Set<Signal>> cluster = TemporalLogicInference.cluster(grid, threshold);
        TemporalLogicInference.getClusterSTL(grid.getXSignal(), cluster.get(0), grid.getXIncrement(), grid.getYIncrement(), grid.getXLowerLimit(), grid.getXUpperLimit(), grid.getYLowerLimit(), grid.getYUpperLimit(), threshold);
    }

    //@Test
    public void testGetSTL() {
        String filepath = Utilities.getResourcesFilepath() + "bioCPS" + Utilities.getSeparater() + "modules" + Utilities.getSeparater() + "1-1-data.csv";
        List<Signal> signals = Utilities.getSignalsBioCPS(filepath,false);
        double threshold = 10000;
        Grid grid = new Grid(signals, 1, threshold);
        System.out.println(TemporalLogicInference.getSTL(grid, threshold).toString());
    }

    //@Test
    public void testFadingDrop() {
        String filepathAHL = Utilities.getResourcesFilepath() + "bioCPS" + Utilities.getSeparater() + "fadingDrop" + Utilities.getSeparater() + "AHL-data.csv";
        String filepathAHL_neighbour = Utilities.getResourcesFilepath() + "bioCPS" + Utilities.getSeparater() + "fadingDrop" + Utilities.getSeparater() + "AHL_neighborAvg-data.csv";
        String filepathGFP = Utilities.getResourcesFilepath() + "bioCPS" + Utilities.getSeparater() + "fadingDrop" + Utilities.getSeparater() + "GFP-data.csv";
        getFormulaAndPlots("AHL", filepathAHL, 5, 0.01);
        //getFormulaAndPlots("AHL_nAVG",filepathAHL_neighbour,1,0.01);
        //getFormulaAndPlots("GFP",filepathGFP,1,0.01);

    }

    public static void getFormulaAndPlots(String filename, String filepath, double xThreshHold, double yThreshHold) {

        List<Signal> signals = Utilities.getSignalsBioCPS(filepath,false);
        Grid grid = new Grid(signals, xThreshHold, yThreshHold);
        //TemporalLogicInference.getSTL(grid);
        //JavaPlotAdaptor.plotToFile(JavaPlotAdaptor.visualizeSubGrid(grid.getSubGrid()), Utilities.getResourcesTempFilepath() + "subgrid" + filename +  ".png");
        //JavaPlotAdaptor.plotToFile(JavaPlotAdaptor.plotGridwithoutCover(grid), Utilities.getResourcesTempFilepath() + "gridnoCover" + filename + ".png");
        JavaPlotAdaptor.plotToFile(JavaPlotAdaptor.plotGrid(grid), Utilities.getResourcesTempFilepath() + "grid" + filename + ".png");

    }

}
