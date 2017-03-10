/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.gridtli.TLI;

import hyness.stl.TreeNode;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import org.cidarlab.gridtli.DOM.Grid;
import org.cidarlab.gridtli.DOM.Point;
import org.cidarlab.gridtli.DOM.Signal;
import org.cidarlab.gridtli.Visualize.JavaPlotAdaptor;
import org.junit.Test;

/**
 *
 * @author prash
 */
public class PaperTest {

    String fuelControlDatafilepath = Utilities.getResourcesFilepath() + "fuelcontrol" + Utilities.getSeparater();

    String posegofile = fuelControlDatafilepath + "EGO-pos-data.csv";
    String negegofile = fuelControlDatafilepath + "EGO-neg-data.csv";
    String posmapfile = fuelControlDatafilepath + "MAP-pos-data.csv";
    String negmapfile = fuelControlDatafilepath + "MAP-neg-data.csv";
    int samplesize = 3;
    int kSize = 10;
    int iterations = 20;
    String delimiter = ",";

    String biofilesuffix = "GeoMeanMEPTR";
    String biosignalsfilepath = Utilities.getResourcesFilepath() + "biosignals" + Utilities.getSeparater();
    int biosamples = 20;
    List<Double> time = new ArrayList<Double>();
    List<Double> lasAHLconc = new ArrayList<Double>();

    
    
    //@Test
    public void generateFuelControlTest_KFold() {
        String testdatafilepath = fuelControlDatafilepath + Mode.KFold.toString();
        if (!Utilities.validFilepath(testdatafilepath)) {
            Utilities.makeDirectory(testdatafilepath);
        }
        List<Signal> posego = Utilities.getRowSignals(posegofile,false);
        List<Signal> negego = Utilities.getRowSignals(negegofile,false);
        List<Signal> posmap = Utilities.getRowSignals(posmapfile,false);
        List<Signal> negmap = Utilities.getRowSignals(negmapfile,false);

        //System.out.println(posego.size());
        //System.out.println(negego.size());
        //System.out.println(posmap.size());
        //System.out.println(negmap.size());
        List<Set<Integer>> kfoldpos = generateKSets(posego.size()); 
        List<Set<Integer>> kfoldneg = generateKSets(posego.size()); 
        for (int i = 0; i < kSize; i++) {
            int trainSize = posego.size() - kfoldpos.get(i).size();
            String trainSizeFilepath = testdatafilepath + Utilities.getSeparater() + trainSize;
            if (!Utilities.validFilepath(trainSizeFilepath)) {
                Utilities.makeDirectory(trainSizeFilepath);
            }
            String subsetfilepath = trainSizeFilepath + Utilities.getSeparater() + i;
            if (!Utilities.validFilepath(subsetfilepath)) {
                Utilities.makeDirectory(subsetfilepath);
            }
            
            Set<Integer> unusedpos = new HashSet<Integer>();
            unusedpos.addAll(kfoldpos.get(i));
            Set<Integer> unusedneg = new HashSet<Integer>();
            unusedneg.addAll(kfoldneg.get(i));
            
            List<Signal> usedposego = new ArrayList<Signal>();
            List<Signal> unusedposego = new ArrayList<Signal>();
            List<Signal> usedposmap = new ArrayList<Signal>();
            List<Signal> unusedposmap = new ArrayList<Signal>();

            List<Signal> usednegego = new ArrayList<Signal>();
            List<Signal> unusednegego = new ArrayList<Signal>();
            List<Signal> usednegmap = new ArrayList<Signal>();
            List<Signal> unusednegmap = new ArrayList<Signal>();

            List<Integer> usedposlist = new ArrayList<Integer>();
            List<Integer> usedneglist = new ArrayList<Integer>();
            List<Integer> unusedposlist = new ArrayList<Integer>();
            List<Integer> unusedneglist = new ArrayList<Integer>();

            for (int k = 0; k < posego.size(); k++) {
                if (unusedpos.contains(k)) {
                    unusedposego.add(posego.get(k));
                    unusedposmap.add(posmap.get(k));
                    unusedposlist.add(k);
                } 
                else {
                    usedposego.add(posego.get(k));
                    usedposmap.add(posmap.get(k));
                    usedposlist.add(k);
                }
                if (unusedneg.contains(k)) {
                    unusednegego.add(negego.get(k));
                    unusednegmap.add(negmap.get(k));
                    unusedneglist.add(k);
                } 
                else {
                    usednegego.add(negego.get(k));
                    usednegmap.add(negmap.get(k));
                    usedneglist.add(k);
                }
            }
            String usedposegoFilepath = subsetfilepath + Utilities.getSeparater() + "used-pos-EGO.csv";
            String unusedposegoFilepath = subsetfilepath + Utilities.getSeparater() + "unused-pos-EGO.csv";

            String usednegegoFilepath = subsetfilepath + Utilities.getSeparater() + "used-neg-EGO.csv";
            String unusednegegoFilepath = subsetfilepath + Utilities.getSeparater() + "unused-neg-EGO.csv";

            String usedposmapFilepath = subsetfilepath + Utilities.getSeparater() + "used-pos-MAP.csv";
            String unusedposmapFilepath = subsetfilepath + Utilities.getSeparater() + "unused-pos-MAP.csv";

            String usednegmapFilepath = subsetfilepath + Utilities.getSeparater() + "used-neg-MAP.csv";
            String unusednegmapFilepath = subsetfilepath + Utilities.getSeparater() + "unused-neg-MAP.csv";

            writeSignalsToFile(usedposegoFilepath, usedposego);
            writeSignalsToFile(unusedposegoFilepath, unusedposego);
            writeSignalsToFile(usedposmapFilepath, usedposmap);
            writeSignalsToFile(unusedposmapFilepath, unusedposmap);
            writeSignalsToFile(usednegegoFilepath, usednegego);
            writeSignalsToFile(unusednegegoFilepath, unusednegego);
            writeSignalsToFile(usednegmapFilepath, usednegmap);
            writeSignalsToFile(unusednegmapFilepath, unusednegmap);

            String posusedfilepath = subsetfilepath + Utilities.getSeparater() + "pos-used.txt";
            String negusedfilepath = subsetfilepath + Utilities.getSeparater() + "neg-used.txt";

            String posunusedfilepath = subsetfilepath + Utilities.getSeparater() + "pos-unused.txt";
            String negunusedfilepath = subsetfilepath + Utilities.getSeparater() + "neg-unused.txt";

            Utilities.writeToFile(posusedfilepath, usedposlist.toString());
            Utilities.writeToFile(negusedfilepath, usedneglist.toString());
            Utilities.writeToFile(posunusedfilepath, unusedposlist.toString());
            Utilities.writeToFile(negunusedfilepath, unusedneglist.toString());

           
        }

    }

    //@Test
    public void generateFuelControlTest_RRS() {
        String testdatafilepath = fuelControlDatafilepath + Mode.RRS.toString();
        if (!Utilities.validFilepath(testdatafilepath)) {
            Utilities.makeDirectory(testdatafilepath);
        }
        List<Signal> posego = Utilities.getRowSignals(posegofile,false);
        List<Signal> negego = Utilities.getRowSignals(negegofile,false);
        List<Signal> posmap = Utilities.getRowSignals(posmapfile,false);
        List<Signal> negmap = Utilities.getRowSignals(negmapfile,false);

        //System.out.println(posego.size());
        //System.out.println(negego.size());
        //System.out.println(posmap.size());
        //System.out.println(negmap.size());
        for (int i = 0; i < samplesize; i++) {
            String subsetfilepath = testdatafilepath + Utilities.getSeparater() + getSubsetNumber(i, posego.size());
            if (!Utilities.validFilepath(subsetfilepath)) {
                Utilities.makeDirectory(subsetfilepath);
            }
            for (int j = 0; j < iterations; j++) {
                String iterationsFilepath = subsetfilepath + Utilities.getSeparater() + j;
                if (!Utilities.validFilepath(iterationsFilepath)) {
                    Utilities.makeDirectory(iterationsFilepath);
                }
                Set<Integer> usedpos = new HashSet<Integer>();
                while (usedpos.size() < getSubsetNumber(i, posego.size())) {
                    int num = getRandom(0, posego.size() - 1);
                    if (!usedpos.contains(num)) {
                        usedpos.add(num);
                    }
                }
                Set<Integer> usedneg = new HashSet<Integer>();
                while (usedneg.size() < getSubsetNumber(i, posego.size())) {
                    int num = getRandom(0, negego.size() - 1);
                    if (!usedneg.contains(num)) {
                        usedneg.add(num);
                    }
                }
                List<Signal> usedposego = new ArrayList<Signal>();
                List<Signal> unusedposego = new ArrayList<Signal>();
                List<Signal> usedposmap = new ArrayList<Signal>();
                List<Signal> unusedposmap = new ArrayList<Signal>();

                List<Signal> usednegego = new ArrayList<Signal>();
                List<Signal> unusednegego = new ArrayList<Signal>();
                List<Signal> usednegmap = new ArrayList<Signal>();
                List<Signal> unusednegmap = new ArrayList<Signal>();

                List<Integer> usedposlist = new ArrayList<Integer>();
                List<Integer> usedneglist = new ArrayList<Integer>();
                List<Integer> unusedposlist = new ArrayList<Integer>();
                List<Integer> unusedneglist = new ArrayList<Integer>();

                for (int k = 0; k < posego.size(); k++) {
                    if (usedpos.contains(k)) {
                        usedposego.add(posego.get(k));
                        usedposmap.add(posmap.get(k));
                        usedposlist.add(k);
                    } else {
                        unusedposego.add(posego.get(k));
                        unusedposmap.add(posmap.get(k));
                        unusedposlist.add(k);
                    }
                    if (usedneg.contains(k)) {
                        usednegego.add(negego.get(k));
                        usednegmap.add(negmap.get(k));
                        usedneglist.add(k);
                    } else {
                        unusednegego.add(negego.get(k));
                        unusednegmap.add(negmap.get(k));
                        unusedneglist.add(k);
                    }
                }
                String usedposegoFilepath = iterationsFilepath + Utilities.getSeparater() + "used-pos-EGO.csv";
                String unusedposegoFilepath = iterationsFilepath + Utilities.getSeparater() + "unused-pos-EGO.csv";

                String usednegegoFilepath = iterationsFilepath + Utilities.getSeparater() + "used-neg-EGO.csv";
                String unusednegegoFilepath = iterationsFilepath + Utilities.getSeparater() + "unused-neg-EGO.csv";

                String usedposmapFilepath = iterationsFilepath + Utilities.getSeparater() + "used-pos-MAP.csv";
                String unusedposmapFilepath = iterationsFilepath + Utilities.getSeparater() + "unused-pos-MAP.csv";

                String usednegmapFilepath = iterationsFilepath + Utilities.getSeparater() + "used-neg-MAP.csv";
                String unusednegmapFilepath = iterationsFilepath + Utilities.getSeparater() + "unused-neg-MAP.csv";

                writeSignalsToFile(usedposegoFilepath, usedposego);
                writeSignalsToFile(unusedposegoFilepath, unusedposego);
                writeSignalsToFile(usedposmapFilepath, usedposmap);
                writeSignalsToFile(unusedposmapFilepath, unusedposmap);
                writeSignalsToFile(usednegegoFilepath, usednegego);
                writeSignalsToFile(unusednegegoFilepath, unusednegego);
                writeSignalsToFile(usednegmapFilepath, usednegmap);
                writeSignalsToFile(unusednegmapFilepath, unusednegmap);

                String posusedfilepath = iterationsFilepath + Utilities.getSeparater() + "pos-used.txt";
                String negusedfilepath = iterationsFilepath + Utilities.getSeparater() + "neg-used.txt";

                String posunusedfilepath = iterationsFilepath + Utilities.getSeparater() + "pos-unused.txt";
                String negunusedfilepath = iterationsFilepath + Utilities.getSeparater() + "neg-unused.txt";

                Utilities.writeToFile(posusedfilepath, usedposlist.toString());
                Utilities.writeToFile(negusedfilepath, usedneglist.toString());
                Utilities.writeToFile(posunusedfilepath, unusedposlist.toString());
                Utilities.writeToFile(negunusedfilepath, unusedneglist.toString());

            }
        }

    }

    //@Test
    public void generateBioSignalsTest() {
        Map<String, Map<Double, List<Signal>>> signals = readBioSignals(biosignalsfilepath);
        String separateSignalsFilepath = biosignalsfilepath + "separatedSignals";
        if (!Utilities.validFilepath(separateSignalsFilepath)) {
            Utilities.makeDirectory(separateSignalsFilepath);
        }
        for (String plasmid : signals.keySet()) {
            String plasmidDir = separateSignalsFilepath + Utilities.getSeparater() + plasmid;
            if (!Utilities.validFilepath(plasmidDir)) {
                Utilities.makeDirectory(plasmidDir);
            }
            //Set 1
            String set1 = plasmidDir + Utilities.getSeparater() + "1" + Utilities.getSeparater();
            if (!Utilities.validFilepath(set1)) {
                Utilities.makeDirectory(set1);
            }
            List<Signal> allSignals = new ArrayList<Signal>();
            for (Double ahl : signals.get(plasmid).keySet()) {
                allSignals.addAll(signals.get(plasmid).get(ahl));
            }
            String allSignalsFilepath = set1 + "allSignals.csv";
            writeSignalsToFileWithHeader(allSignalsFilepath, allSignals, time);
            String testdataFilepath = set1 + "testData" + Utilities.getSeparater();
            if (!Utilities.validFilepath(testdataFilepath)) {
                Utilities.makeDirectory(testdataFilepath);
            }
            for (int i = 0; i < samplesize; i++) {
                String subsetfilepath = testdataFilepath + getSubsetNumber(i, allSignals.size()) + Utilities.getSeparater();
                if (!Utilities.validFilepath(subsetfilepath)) {
                    Utilities.makeDirectory(subsetfilepath);
                }
                for (int j = 0; j < iterations; j++) {
                    String iterationfilepath = subsetfilepath + j + Utilities.getSeparater();
                    if (!Utilities.validFilepath(iterationfilepath)) {
                        Utilities.makeDirectory(iterationfilepath);
                    }
                    Set<Integer> usedIndex = new HashSet<Integer>();
                    while (usedIndex.size() < getSubsetNumber(i, allSignals.size())) {
                        int num = getRandom(0, allSignals.size() - 1);
                        if (!usedIndex.contains(num)) {
                            usedIndex.add(num);
                        }
                    }
                    List<Integer> usedList = new ArrayList<Integer>();
                    List<Integer> unusedList = new ArrayList<Integer>();
                    List<Signal> used = new ArrayList<Signal>();
                    List<Signal> unused = new ArrayList<Signal>();
                    for (int k = 0; k < allSignals.size(); k++) {
                        if (usedIndex.contains(k)) {
                            used.add(allSignals.get(k));
                            usedList.add(k);
                        } else {
                            unused.add(allSignals.get(k));
                            unusedList.add(k);
                        }
                    }
                    String usedFilepath = iterationfilepath + "used-plasmid.csv";
                    String unusedFilepath = iterationfilepath + "unused-plasmid.csv";
                    String usedIndexFilepath = iterationfilepath + "used.txt";
                    String unusedIndexFilepath = iterationfilepath + "unused.txt";

                    writeSignalsToFileWithHeader(usedFilepath, used, time);
                    writeSignalsToFileWithHeader(unusedFilepath, unused, time);

                    Utilities.writeToFile(usedIndexFilepath, usedList.toString());
                    Utilities.writeToFile(unusedIndexFilepath, unusedList.toString());

                }

            }

            //Set 2
            String set2 = plasmidDir + Utilities.getSeparater() + "2" + Utilities.getSeparater();
            if (!Utilities.validFilepath(set2)) {
                Utilities.makeDirectory(set2);
            }

        }
    }

    private Map<String, Map<Double, List<Signal>>> readBioSignals(String rootfilepath) {
        Map<String, Map<Double, List<Signal>>> signals = new HashMap<String, Map<Double, List<Signal>>>();

        lasAHLconc = new ArrayList<Double>();
        time = new ArrayList<Double>();
        for (int i = 0; i < biosamples; i++) {
            String csvfile = rootfilepath + biofilesuffix + (i + 1) + ".csv";
            List<String[]> lines = Utilities.getCSVFileContentAsList(csvfile);

            if (i == 0) {
                //Extract lasAHL concentration
                for (int j = 1; j < lines.get(0).length; j++) {
                    if (!lasAHLconc.contains(Double.valueOf(lines.get(0)[j]))) {
                        lasAHLconc.add(Double.valueOf(lines.get(0)[j]));
                    }
                }
                //Extract Time points
                for (int j = 1; j < lines.get(1).length; j++) {
                    if (time.contains(Double.valueOf(lines.get(1)[j]))) {
                        break;
                    }
                    time.add(Double.valueOf(lines.get(1)[j]));
                }

            }

            //Extract Signal values
            for (int j = 2; j < lines.size(); j++) {
                String pieces[] = lines.get(j);
                String namepieces[] = pieces[0].split("=");
                String plasmidName = namepieces[1].trim();
                if (!signals.containsKey(plasmidName)) {
                    Map<Double, List<Signal>> ahlmap = new HashMap<Double, List<Signal>>();
                    for (Double d : lasAHLconc) {
                        ahlmap.put(d, new ArrayList<Signal>());
                    }
                    signals.put(plasmidName, ahlmap);
                }
                for (int k = 0; k < lasAHLconc.size(); k++) {
                    List<Point> points = new ArrayList<Point>();
                    for (int l = 0; l < time.size(); l++) {
                        if (pieces[l + (k * time.size()) + 1].equals("nan") || pieces[l + (k * time.size()) + 1].equals("None")) {
                            //System.out.println(csvfile);
                            //System.out.println(k + "," + l);
                            continue;
                        }
                        points.add(new Point(time.get(l), "t", Double.valueOf(pieces[l + (k * time.size()) + 1]), "x"));
                    }
                    signals.get(plasmidName).get(lasAHLconc.get(k)).add(new Signal(points));
                }
            }
        }
        return signals;
    }
    
    @Test
    public void testFuelControl(){
        testFuelControl(Mode.KFold,1);
    }
    
    
    public void testFuelControl(Mode mode, int run) {
        System.out.println("Fuel Control Data Set!");
        String root = fuelControlDatafilepath + mode.toString() + Utilities.getSeparater();
        //String root = fuelControlDatafilepath + "KFold" + Utilities.getSeparater();
        String headerLine = 
                    "sizeFolder" +delimiter + //sizeFolder
                    "iterFolder" +delimiter+ //iterFolder
                    "trainSize" +delimiter+ //trainSize (used in PosNeg TLI)
                    "mcrTrain" +delimiter+ //training MCR
                    "fprTrain" +delimiter+ //training FPR
                    "fnrTrain" +delimiter+ //training FNR
                    "testSize" +delimiter+ //testSize (used in PosNeg TLI)
                    "mcrTest" +delimiter+ //testing MCR
                    "fprTest" +delimiter+ //testing FPR
                    "fnrTest" +delimiter+ //testing FNR
                    "runtime" +delimiter+ //Runtime
                    "t_t" +delimiter+ //xthreshold
                    "x_t" +delimiter+ //ythreshold
                    "c_t"  //cthreshold
                    ;
        
        for(int t=0;t<6;t++){
            for(int x=0;x<3;x++){
                for(int c=0;c<4;c++){
                    double xthreshold = getFuelt(t); //t
                    double ythreshold = getFuelx(x); //x
                    double cthreshold = getFuelc(c); //c

                    String xplotdot = "x" + xthreshold;
                    String xplot = xplotdot.replaceAll("\\.", "_");
                    String yplotdot = "y" + ythreshold;
                    String yplot = yplotdot.replaceAll("\\.", "_");
                    String cplotdot = "c" + cthreshold;
                    String cplot = cplotdot.replaceAll("\\.", "_");

                    String plotsuffix = xplot + "-" + yplot + "-" + cplot;
                    String resultRun = fuelControlDatafilepath + (mode.toString() + "_Run" + run) + Utilities.getSeparater();
                    if (!Utilities.isDirectory(resultRun)) {
                        Utilities.makeDirectory(resultRun);
                    }
                    String resultRoot = resultRun + plotsuffix;
                    if (!Utilities.isDirectory(resultRoot)) {
                        Utilities.makeDirectory(resultRoot);
                    }
                    List<String> fileLines = new ArrayList<String>();
                    fileLines.add(headerLine);
                    walkFuelControl(root, root, xthreshold, ythreshold, cthreshold, fileLines);

                    String resultsFilepath = resultRoot + Utilities.getSeparater() + "result.csv";
                    Utilities.writeToFile(resultsFilepath, fileLines);
                }
            }
        }
        
        
        
    }

    //@Test
    public void testTransformFuel(){
        String path = Utilities.getResourcesFilepath() + "fuelcontrol" + Utilities.getSeparater() + Mode.KFold.toString() + Utilities.getSeparater();
        String header = "";
        BigDecimal b = new BigDecimal(0.3);
        b = b.setScale(1, RoundingMode.UP);
        
        BigDecimal v = new BigDecimal(0);
        v = v.setScale(1, RoundingMode.UP);
        for(int i=0;i<199;i++){
            header += v.toString() + delimiter;
            v = v.add(b);
            v = v.setScale(1, RoundingMode.UP);
        }
        header += v.toString();
        walkTransformFuel(path,header);
    }
    
    //@Test
    public void testBioSignals() {
        String root = biosignalsfilepath + "separatedSignals" + Utilities.getSeparater();
        System.out.println("Biosignals test");
        String headerLine = "plasmid" +delimiter+ 
                "iterFolder" +delimiter+ 
                "trainSize" +delimiter+ 
                "mcrTrain" +delimiter+ 
                "fnrTrain" +delimiter+ 
                "testSize" +delimiter+ 
                "mcrTest" +delimiter+ 
                "fnrTest" +delimiter+ 
                "runtime" +delimiter+ 
                "t_t" +delimiter+ 
                "x_t" +delimiter+ 
                "c_t";
        
        
        for(int i=0;i<6;i++){
            for(int j=0;j<3;j++){
                for (int k = 0; k < 4; k++) {
                    double xthreshold = getBiot(i);
                    double ythreshold = getBiox(j);
                    double cthreshold = getBioc(k);
                    List<String> filelines = new ArrayList<String>();
                    filelines.add(headerLine);
                    walkBioSignals(root, root, xthreshold, ythreshold, cthreshold, filelines);

                    String xplotdot = "x" + xthreshold;
                    String xplot = xplotdot.replaceAll("\\.", "_");
                    String yplotdot = "y" + ythreshold;
                    String yplot = yplotdot.replaceAll("\\.", "_");
                    String cplotdot = "c" + cthreshold;
                    String cplot = cplotdot.replaceAll("\\.", "_");

                    String plotsuffix = xplot + "-" + yplot + "-" + cplot;
                    String resultRoot = biosignalsfilepath + plotsuffix;
                    if (!Utilities.isDirectory(resultRoot)) {
                        Utilities.makeDirectory(resultRoot);
                    }
                    String resultFilepath = resultRoot + Utilities.getSeparater() + "result.csv";
                    Utilities.writeToFile(resultFilepath, filelines);
                }
            }
        }
    }
    
    //@Test
    public void testSpecificBiosignal(){
        System.out.println("Specific Test");
        String filepath = Utilities.getResourcesFilepath() + "biosignals/separatedSignals/pL2f1439/1/testData/10/11/";
        String usedSignalsFilepath = filepath + "used-plasmid.csv";
        String unusedSignalsFilepath = filepath + "unused-plasmid.csv";
        String plot = filepath + "grid.png";
        String cluster1 = filepath + "cluster1.png";
        String cluster2 = filepath + "cluster2.png";
        String cluster3 = filepath + "cluster3.png";
        List<Signal> used = Utilities.getRowSignals(usedSignalsFilepath, true);
        List<Signal> unused = Utilities.getRowSignals(unusedSignalsFilepath, true);
        
        double xthreshold = 0.5;
        double ythreshold = 10;
        double cthreshold = 0.5;
        
        Grid grid = new Grid(used,xthreshold,ythreshold);
        TreeNode stl = TemporalLogicInference.getSTL(grid, cthreshold);
        List<Set<Signal>> clusters = new ArrayList<Set<Signal>>();
        clusters = TemporalLogicInference.cluster(grid, cthreshold);
        JavaPlotAdaptor.plotToFile(JavaPlotAdaptor.plotGrid(grid,clusters), plot);
        JavaPlotAdaptor.plotToFile(JavaPlotAdaptor.plotSpecificCluster(grid,clusters.get(0)), cluster1);
        JavaPlotAdaptor.plotToFile(JavaPlotAdaptor.plotSpecificCluster(grid,clusters.get(1)), cluster2);
        JavaPlotAdaptor.plotToFile(JavaPlotAdaptor.plotSpecificCluster(grid,clusters.get(2)), cluster3);
        
        System.out.println("STL Formula");
        System.out.println(stl);
        
        for (Signal s : used) {
            double r = Validation.getRobustness(stl, s);
            if (r < 0) {
                System.out.println("SIGNAL INDEX :: " + s.getIndex());
                System.out.println(s.getPoints());
                System.out.println(r);
            }

        }
    }
    
    //@Test
    public void testConsolidateFuelResults(){
        String path = Utilities.getResourcesFilepath() + "fuelcontrol" + Utilities.getSeparater() + "run1" + Utilities.getSeparater();
        List<String> finalLines = new ArrayList<String>();
        List<String> lines = new ArrayList<String>();
        String headerLine = 
                    "sizeFolder" +delimiter + //sizeFolder
                    "iterFolder" +delimiter+ //iterFolder
                    "trainSize" +delimiter+ //trainSize (used in PosNeg TLI)
                    "mcrTrain" +delimiter+ //training MCR
                    "fprTrain" +delimiter+ //training FPR
                    "fnrTrain" +delimiter+ //training FNR
                    "testSize" +delimiter+ //testSize (used in PosNeg TLI)
                    "mcrTest" +delimiter+ //testing MCR
                    "fprTest" +delimiter+ //testing FPR
                    "fnrTest" +delimiter+ //testing FNR
                    "runtime" +delimiter+ //Runtime
                    "t_t" +delimiter+ //xthreshold
                    "x_t" +delimiter+ //ythreshold
                    "c_t"  //cthreshold
                    ;
        finalLines.add(headerLine);
        walkResults(path,lines);
        finalLines.addAll(lines);
        
        String filepath = path + "consolidated.csv";
        Utilities.writeToFile(filepath, finalLines);
    }
    
    //@Test
    public void testConsolidateBioResults(){
        String path = Utilities.getResourcesFilepath() + "bioSignals" + Utilities.getSeparater()+ "separatedSignals" + Utilities.getSeparater() + "run1" + Utilities.getSeparater();
        List<String> finalLines = new ArrayList<String>();
        List<String> lines = new ArrayList<String>();
        String headerLine = "plasmid" +delimiter+ 
                "iterFolder" +delimiter+ 
                "trainSize" +delimiter+ 
                "mcrTrain" +delimiter+ 
                "fnrTrain" +delimiter+ 
                "testSize" +delimiter+ 
                "mcrTest" +delimiter+ 
                "fnrTest" +delimiter+ 
                "runtime" +delimiter+ 
                "t_t" +delimiter+ 
                "x_t" +delimiter+ 
                "c_t";
        finalLines.add(headerLine);
        walkResults(path,lines);
        finalLines.addAll(lines);
        
        String filepath = path + "consolidated.csv";
        Utilities.writeToFile(filepath, finalLines);
    }
    
    //<editor-fold desc="Folder walk functions" defaultstate="collapsed">
    private void walkResults(String path, List<String> allLines){
        
        File root = new File(path);
        File[] list = root.listFiles();

        if (list == null) {
            return;
        }
        
        for (File f : list) {
            if (f.isDirectory()) {
                walkResults(f.getAbsolutePath(),allLines);
            } // Reached all Files
            else {
                if (f.getName().endsWith(".csv")) {
                    List<String> filelines = Utilities.getFileContentAsStringList(f.getAbsolutePath());
                    for(int i=1;i<filelines.size();i++){
                        allLines.add(filelines.get(i));
                    }
                }
            }
        }
    }
    
    private void walkFuelControl(String path, String resultsRoot, double xthreshold, double ythreshold, double cthreshold, List<String> fileLines) {
        File root = new File(path);
        File[] list = root.listFiles();

        if (list == null) {
            return;
        }
        List<Signal> training_PosMAP = new ArrayList<Signal>();
        List<Signal> training_NegMAP = new ArrayList<Signal>();
        List<Signal> training_PosEGO = new ArrayList<Signal>();
        List<Signal> training_NegEGO = new ArrayList<Signal>();

        List<Signal> testing_PosMAP = new ArrayList<Signal>();
        List<Signal> testing_NegMAP = new ArrayList<Signal>();
        List<Signal> testing_PosEGO = new ArrayList<Signal>();
        List<Signal> testing_NegEGO = new ArrayList<Signal>();
        int sampleSize = 0;
        int iteration = 0;
        boolean analysis = false;
        for (File f : list) {

            if (f.isDirectory()) {
                walkFuelControl(f.getAbsolutePath(), resultsRoot, xthreshold, ythreshold, cthreshold, fileLines);
            } // Reached all Files
            else {
                analysis = true;
                String pieces[] = filepathPieces(f.getAbsolutePath(), resultsRoot);

                sampleSize = Integer.valueOf(pieces[pieces.length - 3].trim());
                

                iteration = Integer.valueOf(pieces[pieces.length - 2].trim());

                if (f.getName().equals("used-pos-EGO.csv")) {
                    training_PosEGO = Utilities.getRowSignals(f.getAbsolutePath(),true);
                } else if (f.getName().equals("unused-pos-EGO.csv")) {
                    testing_PosEGO = Utilities.getRowSignals(f.getAbsolutePath(),true);
                } else if (f.getName().equals("used-neg-EGO.csv")) {
                    training_NegEGO = Utilities.getRowSignals(f.getAbsolutePath(),true);
                } else if (f.getName().equals("unused-neg-EGO.csv")) {
                    testing_NegEGO = Utilities.getRowSignals(f.getAbsolutePath(),true);
                } else if (f.getName().equals("used-pos-MAP.csv")) {
                    training_PosMAP = Utilities.getRowSignals(f.getAbsolutePath(),true);
                } else if (f.getName().equals("unused-pos-MAP.csv")) {
                    testing_PosMAP = Utilities.getRowSignals(f.getAbsolutePath(),true);
                } else if (f.getName().equals("used-neg-MAP.csv")) {
                    training_NegMAP = Utilities.getRowSignals(f.getAbsolutePath(),true);
                } else if (f.getName().equals("unused-neg-MAP.csv")) {
                    testing_NegMAP = Utilities.getRowSignals(f.getAbsolutePath(),true);
                }

            }
        }
        if (analysis) {
            //System.out.println("path : " + path);
            
            String xplotdot = "x" + xthreshold;
            String xplot = xplotdot.replaceAll("\\.", "_");
            String yplotdot = "y" + ythreshold;
            String yplot = yplotdot.replaceAll("\\.", "_");
            String cplotdot = "c" + cthreshold;
            String cplot = cplotdot.replaceAll("\\.", "_");
            
            long tstart = System.nanoTime();
            Grid gridEgo = new Grid(training_PosEGO, xthreshold, ythreshold);
            Grid gridMap = new Grid(training_PosMAP, xthreshold, ythreshold);
            TreeNode stlEgo = TemporalLogicInference.getSTL(gridEgo, cthreshold);
            TreeNode stlMap = TemporalLogicInference.getSTL(gridMap, cthreshold);
            long tend = System.nanoTime();
            double runtime = getTimeElapsed(tstart,tend);
                        
            int count=0;

            List<Integer> pos_training_Ego_robust = new ArrayList<Integer>();
            List<Integer> pos_training_Map_robust = new ArrayList<Integer>();
            
            List<Integer> pos_testing_Ego_robust = new ArrayList<Integer>();
            List<Integer> pos_testing_Map_robust = new ArrayList<Integer>();
            
            List<Integer> pos_training_Ego_fail = new ArrayList<Integer>();
            List<Integer> pos_training_Map_fail = new ArrayList<Integer>();
            
            List<Integer> pos_testing_Ego_fail = new ArrayList<Integer>();
            List<Integer> pos_testing_Map_fail = new ArrayList<Integer>();
            
            List<Integer> neg_training_Ego_robust = new ArrayList<Integer>();
            List<Integer> neg_training_Map_robust = new ArrayList<Integer>();
            
            List<Integer> neg_testing_Ego_robust = new ArrayList<Integer>();
            List<Integer> neg_testing_Map_robust = new ArrayList<Integer>();
            
            List<Integer> neg_training_Ego_fail = new ArrayList<Integer>();
            List<Integer> neg_training_Map_fail = new ArrayList<Integer>();
            
            List<Integer> neg_testing_Ego_fail = new ArrayList<Integer>();
            List<Integer> neg_testing_Map_fail = new ArrayList<Integer>();
            
            double training_Pos_EGO_total = 0;
            double testing_Pos_EGO_total = 0;
            double training_Pos_MAP_total = 0;
            double testing_Pos_MAP_total = 0;
            
            double training_Neg_EGO_total = 0;
            double testing_Neg_EGO_total = 0;
            double training_Neg_MAP_total = 0;
            double testing_Neg_MAP_total = 0;
            
            int totalSize = training_PosEGO.size() + testing_PosEGO.size();
            
            
            //Positive Training
            count = 0;
            for (Signal s : training_PosEGO) {
                double r = Validation.getRobustness(stlEgo, s);
                if (r < 0) {
                    pos_training_Ego_fail.add(count);
                } else {
                    pos_training_Ego_robust.add(count);
                }
                training_Pos_EGO_total += r;
                count++;
            }
            
            count = 0;
            for (Signal s : training_PosMAP) {
                double r = Validation.getRobustness(stlMap, s);
                if (r < 0) {
                    pos_training_Map_fail.add(count);
                } else {
                    pos_training_Map_robust.add(count);
                }
                training_Pos_MAP_total += r;
                count++;
            }
            
            //Positive Testing
            count = 0;
            for (Signal s : testing_PosEGO) {
                double r = Validation.getRobustness(stlEgo, s);
                if (r < 0) {
                    pos_testing_Ego_fail.add(count);
                } else {
                    pos_testing_Ego_robust.add(count);
                }
                testing_Pos_EGO_total += r;
                count++;
            }
            
            count = 0;
            for (Signal s : testing_PosMAP) {
                double r = Validation.getRobustness(stlMap, s);
                if (r < 0) {
                    pos_testing_Map_fail.add(count);
                } else {
                    pos_testing_Map_robust.add(count);
                }
                testing_Pos_MAP_total += r;
                count++;
            }

            //Negative Used
            count = 0;
            for (Signal s : training_NegEGO) {
                double r = Validation.getRobustness(stlEgo, s);
                if (r >= 0) {
                    neg_training_Ego_robust.add(count);
                } else {
                    neg_training_Ego_fail.add(count);
                }
                training_Neg_EGO_total += r;
                count++;
            }
            
            count = 0;
            for (Signal s : training_NegMAP) {
                double r = Validation.getRobustness(stlMap, s);
                if (r >= 0) {
                    neg_training_Map_robust.add(count);
                } else {
                    neg_training_Map_fail.add(count);
                }
                training_Neg_MAP_total += r;
                count++;
            }
            
            //Negative Unused
            count = 0;
            for (Signal s : testing_NegEGO) {
                double r = Validation.getRobustness(stlEgo, s);
                if (r >= 0) {
                    neg_testing_Ego_robust.add(count);
                } else {
                    neg_testing_Ego_fail.add(count);
                }
                testing_Neg_EGO_total += r;
                count++;
            }
            
            count = 0;
            for (Signal s : testing_NegMAP) {
                double r = Validation.getRobustness(stlMap, s);
                if (r >= 0) {
                    neg_testing_Map_robust.add(count);
                } else {
                    neg_testing_Map_fail.add(count);
                }
                testing_Neg_MAP_total += r;
                count++;
            }
            
            // training data
            List<Integer> pos_training_robust = new ArrayList<Integer>();
            pos_training_robust.addAll(pos_training_Map_robust);
            pos_training_robust.retainAll(pos_training_Ego_robust);
            
            List<Integer> pos_training_fail = new ArrayList<Integer>();
            pos_training_fail.addAll(pos_training_Map_fail);
            pos_training_fail.addAll(pos_training_Map_robust);
            pos_training_fail.removeAll(pos_training_robust);
            
            List<Integer> neg_training_robust = new ArrayList<Integer>();
            neg_training_robust.addAll(neg_training_Map_robust);
            neg_training_robust.retainAll(neg_training_Ego_robust);
            
            List<Integer> neg_training_fail = new ArrayList<Integer>();
            neg_training_fail.addAll(neg_training_Map_fail);
            neg_training_fail.addAll(neg_training_Map_robust);
            neg_training_fail.removeAll(neg_training_robust);
            
            // testing data
            List<Integer> pos_testing_robust = new ArrayList<Integer>();
            pos_testing_robust.addAll(pos_testing_Map_robust);
            pos_testing_robust.retainAll(pos_testing_Ego_robust);
            
            List<Integer> pos_testing_fail = new ArrayList<Integer>();
            pos_testing_fail.addAll(pos_testing_Map_fail);
            pos_testing_fail.addAll(pos_testing_Map_robust);
            pos_testing_fail.removeAll(pos_testing_robust);
            
            List<Integer> neg_testing_robust = new ArrayList<Integer>();
            neg_testing_robust.addAll(neg_testing_Map_robust);
            neg_testing_robust.retainAll(neg_testing_Ego_robust);
            
            List<Integer> neg_testing_fail = new ArrayList<Integer>();
            neg_testing_fail.addAll(neg_testing_Map_fail);
            neg_testing_fail.addAll(neg_testing_Map_robust);
            neg_testing_fail.removeAll(neg_testing_robust);
            
            double fpr_training = ((double)neg_training_robust.size()) / ((double)(neg_training_robust.size() + pos_training_robust.size()));
            double fpr_testing = ((double)neg_testing_robust.size()) / ((double)(neg_testing_robust.size() + pos_testing_robust.size()));
            
            double fnr_training = ((double)pos_training_fail.size()) / ((double)(neg_training_fail.size() + pos_training_fail.size()));
            double fnr_testing = ((double)pos_testing_fail.size()) / ((double)(neg_testing_fail.size() + pos_testing_fail.size()));
            
            double mcr_training = ((double)neg_training_robust.size() + pos_training_fail.size()) / ((double)(neg_training_robust.size() + pos_training_robust.size() + neg_training_fail.size() + pos_training_fail.size()));
            double mcr_testing = ((double)neg_testing_robust.size() + pos_testing_fail.size()) / ((double)(neg_testing_robust.size() + pos_testing_robust.size() + neg_testing_fail.size() + pos_testing_fail.size()));
            
            int trainingSize = (neg_training_robust.size() + pos_training_robust.size() + neg_training_fail.size() + pos_training_fail.size());
            int testingSize = (neg_testing_robust.size() + pos_testing_robust.size() + neg_testing_fail.size() + pos_testing_fail.size());
            String line = 
                    sampleSize +delimiter + //sizeFolder
                    iteration +delimiter+ //iterFolder
                    trainingSize +delimiter+ //trainSize (used in PosNeg TLI)
                    mcr_training +delimiter+ //training MCR
                    fpr_training +delimiter+ //training FPR
                    fnr_training +delimiter+ //training FNR
                    testingSize +delimiter+ //testSize (used in PosNeg TLI)
                    mcr_testing +delimiter+ //testing MCR
                    fpr_testing +delimiter+ //testing FPR
                    fnr_testing +delimiter+ //testing FNR
                    runtime +delimiter+ //Runtime
                    xthreshold +delimiter+ //xthreshold
                    ythreshold +delimiter+ //ythreshold
                    cthreshold  //cthreshold
                    ;
            fileLines.add(line);
        }

    }
    
    private void walkBioSignals(String path, String resultsRoot, double xthreshold, double ythreshold, double cthreshold, List<String> filelines) {
        File root = new File(path);
        File[] list = root.listFiles();

        if (list == null) {
            return;
        }
        
        boolean analysis = false;
        List<Signal> training = new ArrayList<Signal>();
        List<Signal> testing = new ArrayList<Signal>();
        String plasmidName = "";
        int sampleSize = 0;
        int iteration = 0;
        for (File f : list) {
            if (f.isDirectory()) {
                walkBioSignals(f.getAbsolutePath(), resultsRoot, xthreshold, ythreshold, cthreshold, filelines);
            } // Reached all Files
            else {
                String pathPieces[] = filepathPieces(f.getAbsolutePath(),resultsRoot);
                if (f.getName().equals("used-plasmid.csv")) {
                    plasmidName = pathPieces[0].trim();
                    
                    sampleSize = Integer.valueOf(pathPieces[pathPieces.length - 3].trim());
                    
                    iteration = Integer.valueOf(pathPieces[pathPieces.length - 2].trim());
                    
                    training = Utilities.getRowSignals(f.getAbsolutePath(), true);
                    analysis = true;
                } else if(f.getName().equals("unused-plasmid.csv")){
                    testing = Utilities.getRowSignals(f.getAbsolutePath(), true);
                }
            }
        }
        if(analysis){
            
            String xplotdot = "x" + xthreshold;
            String xplot = xplotdot.replaceAll("\\.", "_");
            String yplotdot = "y" + ythreshold;
            String yplot = yplotdot.replaceAll("\\.", "_");
            String cplotdot = "c" + cthreshold;
            String cplot = cplotdot.replaceAll("\\.", "_");
           
            String plotsuffix = xplot + "-" + yplot + "-" + cplot;
            String plotpathRoot = path + Utilities.getSeparater() + plotsuffix;
            if(!Utilities.isDirectory(plotpathRoot)){
                Utilities.makeDirectory(plotpathRoot);
            }
            
            long tstart = System.nanoTime();
            Grid g = new Grid(training,xthreshold,ythreshold);
            TreeNode stl = TemporalLogicInference.getSTL(g, cthreshold);
            long tend = System.nanoTime();
            double timeElapsed = getTimeElapsed(tstart,tend);
            

            int count=0;
            
            List<Integer> training_robust = new ArrayList<Integer>();
            List<Integer> training_fail = new ArrayList<Integer>();
            List<Integer> testing_robust = new ArrayList<Integer>();
            List<Integer> testing_fail = new ArrayList<Integer>();
            
            count =0;
            for(Signal s:training){
                double r = Validation.getRobustness(stl, s);
                if(r < 0){
                    training_fail.add(count);
                } else {
                    training_robust.add(count);
                }
                count++;
            }
            count =0;
            for(Signal s:testing){
                double r = Validation.getRobustness(stl, s);
                if(r < 0){
                    testing_fail.add(count);
                } else {
                    testing_robust.add(count);
                }
                count++;
            }

            double fnrTrain = ((double)training_fail.size()) / ((double) training.size());
            double mcrTrain = fnrTrain;
            
            double fnrTest = ((double)testing_fail.size()) / ((double) testing.size());
            double mcrTest = fnrTest;
            
            

            String line = plasmidName +delimiter+ 
                    iteration +delimiter+ 
                    training.size() +delimiter+ 
                    mcrTrain +delimiter+
                    fnrTrain +delimiter+
                    testing.size() +delimiter+ 
                    mcrTest +delimiter+
                    fnrTest +delimiter+
                    timeElapsed +delimiter+ 
                    xthreshold +delimiter+ 
                    ythreshold +delimiter+ 
                    cthreshold;
            
            filelines.add(line);
        }
    }
    
    public void walkTransformFuel(String path, String header){
        File root = new File(path);
        File[] list = root.listFiles();

        if (list == null) {
            return;
        }
        
        for (File f : list) {
            if (f.isDirectory()) {
                walkTransformFuel(f.getAbsolutePath(), header);
            } // Reached all Files
            else {
                if (f.getName().endsWith(".csv")) {
                    List<String> filelines = new ArrayList<String>();
                    filelines.add(header);
                    filelines.addAll(Utilities.getFileContentAsStringList(f.getAbsolutePath()));
                    Utilities.writeToFile(f.getAbsolutePath(), filelines);
                    
                }
            }
        }
    }
    
    //</editor-fold>
    
    //<editor-fold desc="Helper Functions" defaultstate="collapsed">
    private double getTimeElapsed(long start, long end){
        long elapsed = end - start;
        return (elapsed/1000000000.0);
    }
    
    private List<Set<Integer>> generateKSets(int size){
        List<Set<Integer>> kfold = new ArrayList<Set<Integer>>();
        for(int i=0;i<kSize;i++){
            kfold.add(new HashSet<Integer>());
        }
        Set<Integer> assigned = new HashSet<Integer>();
        while(assigned.size() < size){
            for (int i = 0; i < kSize; i++) {
                if(assigned.size() >= size){
                    break;
                }
                int indx = getRandom(0,size-1);
                while(assigned.contains(indx)){
                    indx = getRandom(0,size-1);
                }
                assigned.add(indx);
                kfold.get(i).add(indx);
            }
        }
        return kfold;
    }
    
    private int getRandom(int min, int max) {
        Random random = new Random();
        return (random.nextInt(max - min + 1) + min);
    }

    private int getSubsetNumber(int i, int max) {
        switch (i) {
            case 0:
                return ((max / 100) * 10);
            case 1:
                return ((max / 100) * 25);
            case 2:
                return ((max / 100) * 50);
            //case 3: return 300;
        }
        return 0;
    }

    private static String[] filepathPieces(String filepath, String rootFilepath) {
        String relativeFilepath = filepath.substring(filepath.lastIndexOf(rootFilepath) + rootFilepath.length());
        return relativeFilepath.split("/");
    }
    
    
    private void writeSignalsToFile(String filepath, List<Signal> signals) {
        List<String> lines = new ArrayList<String>();
        for (Signal signal : signals) {
            String line = "";
            for (int i = 0; i < signal.getPoints().size() - 1; i++) {
                line += signal.getPoints().get(i).getY() + ",";
            }
            line += signal.getPoints().get(signal.getPoints().size() - 1).getY();
            lines.add(line);
        }
        Utilities.writeToFile(filepath, lines);
    }

    private void writeSignalsToFileWithHeader(String filepath, List<Signal> signals, List<Double> header) {
        List<String> lines = new ArrayList<String>();
        String head = "";
        for (int i = 0; i < header.size() - 1; i++) {
            head += header.get(i) + ",";
        }
        head += header.get(header.size() - 1);
        lines.add(head);
        for (Signal signal : signals) {
            String line = "";
            if (signal.getPoints().size() == header.size()) {
                for (int i = 0; i < signal.getPoints().size() - 1; i++) {
                    line += signal.getPoints().get(i).getY() + ",";
                }
                line += signal.getPoints().get(signal.getPoints().size() - 1).getY();
            } else {
                int signalCounter = 0;
                for (int i = 0; i < header.size(); i++) {
                    double x = signal.getPoints().get(signalCounter).getX();
                    double y = signal.getPoints().get(signalCounter).getY();
                    if (header.get(i).equals(x)) {
                        line += y + ",";
                        signalCounter++;
                    } else {
                        line += ",";
                    }
                }
                line = line.substring(0, line.length() - 1);
            }
            lines.add(line);
        }
        Utilities.writeToFile(filepath, lines);

    }

    public enum Mode{
        RRS,
        KFold
    }
    
    //</editor-fold>
    
    //<editor-fold desc="Threshold Values" defaultstate="collapsed">
    
    //Fuel Control Thresholds
    private double getFuelt(int t){
        switch(t){
            case 0: return 0.01;
            case 1: return 0.1;
            case 2: return 0.3;
            case 3: return 1.0;
            case 4: return 5.0;
            case 5: return 10.0;
        }
        return 0;
    }
    
    private double getFuelx(int x){
        switch(x){
            case 0: return 0.5;
            case 1: return 1.0;
            case 2: return 10.0;
        }
        return 0;
    }
    
    private double getFuelc(int c){
        switch(c){
            case 0: return 0.1;
            case 1: return 0.5;
            case 2: return 1.0;
            case 3: return 10.0;
        }
        return 0;
    }
    
    //Bio Signal Thresholds
    private double getBiot(int t) {
        switch (t) {
            case 0:
                return 0.01;
            case 1:
                return 0.1;
            case 2:
                return 0.5;
            case 3:
                return 1.0;
            case 4:
                return 5.0;
            case 5:
                return 10.0;
        }
        return 0;
    }

    private double getBiox(int x) {
        switch (x) {
            case 0:
                return 10.0;
            case 1:
                return 50.0;
            case 2:
                return 100.0;
        }
        return 0;
    }

    private double getBioc(int c) {
        switch (c) {
            case 0:
                return 5.0;
            case 1:
                return 10.0;
            case 2:
                return 50.0;
            case 3:
                return 100.0;
        }
        return 0;
    }
    //</editor-fold>
    
}
