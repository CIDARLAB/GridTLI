/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.gridtli.TLI;

import org.cidarlab.gridtli.tli.TemporalLogicInference;
import org.cidarlab.gridtli.tli.Utilities;
import org.cidarlab.gridtli.tli.Validation;
import hyness.stl.ConjunctionNode;
import hyness.stl.DisjunctionNode;
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
import org.cidarlab.gridtli.dom.Grid;
import org.cidarlab.gridtli.dom.Point;
import org.cidarlab.gridtli.dom.Signal;
import org.cidarlab.gridtli.adaptors.JavaPlotAdaptor;
import org.cidarlab.gridtli.adaptors.PyPlotAdaptor;
import org.cidarlab.gridtli.dom.TLIException;
import org.junit.Test;

/**
 *
 * @author prash
 */
public class ManuscriptTest {

    String fuelControlDatafilepath = Utilities.getSampleFilepath() + "fuelcontrol" + Utilities.getSeparater();

    String posegofile = fuelControlDatafilepath + "EGO-pos-data.csv";
    String negegofile = fuelControlDatafilepath + "EGO-neg-data.csv";
    String posmapfile = fuelControlDatafilepath + "MAP-pos-data.csv";
    String negmapfile = fuelControlDatafilepath + "MAP-neg-data.csv";
    int samplesize = 3;
    int kSize = 10;
    int iterations = 20;
    String delimiter = ",";

    String biofilesuffix = "GeoMeanMEPTR";
    String biosignalsfilepath = Utilities.getSampleFilepath() + "biosignals" + Utilities.getSeparater();
    int biosamples = 20;
    List<Double> time = new ArrayList<Double>();
    List<Double> lasAHLconc = new ArrayList<Double>();

    
    
    //@Test
    public void generateFuelControlTest_KFold() throws TLIException {
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
    public void generateFuelControlTest_RRS() throws TLIException {
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

    private void createFolder(String filepath){
        if(!Utilities.validFilepath(filepath)){
            Utilities.makeDirectory(filepath);
        }
    }
    
    //@Test
    public void generateBioSignalsTest_KFold() throws TLIException{
        
        String allSignalsFilepath = biosignalsfilepath + "allSignals" + Utilities.getSeparater();
        createFolder(allSignalsFilepath);
        String twoRBSFilepath = biosignalsfilepath + "twoRBS" + Utilities.getSeparater();
        createFolder(twoRBSFilepath);
        String randomAllSignalsFilepath = allSignalsFilepath + "random" + Utilities.getSeparater();
        createFolder(randomAllSignalsFilepath);
        String balancedAllSignalsFilepath = allSignalsFilepath + "balanced" + Utilities.getSeparater();
        createFolder(balancedAllSignalsFilepath);
        String strongFilepath = twoRBSFilepath + "strong" + Utilities.getSeparater();
        createFolder(strongFilepath);
        String weakFilepath = twoRBSFilepath + "weak" + Utilities.getSeparater();
        createFolder(weakFilepath);
        String randomStrongFilepath = strongFilepath + "random" + Utilities.getSeparater();
        createFolder(randomStrongFilepath);
        String balancedStrongFilepath = strongFilepath + "balanced" + Utilities.getSeparater();
        createFolder(balancedStrongFilepath);
        String randomWeakFilepath = weakFilepath + "random" + Utilities.getSeparater();
        createFolder(randomWeakFilepath);
        String balancedWeakFilepath = weakFilepath + "balanced" + Utilities.getSeparater();
        createFolder(balancedWeakFilepath);
        
        Map<String, Map<Double, List<Signal>>> signals = readBioSignals(biosignalsfilepath);
        Map<String, Map<Double, List<Set<Signal>>>> kFolds = new HashMap<String, Map<Double, List<Set<Signal>>>>(); 
        Map<String, Map<Double, List<Set<Integer>>>> kFoldsIndx = new HashMap<String, Map<Double, List<Set<Integer>>>>(); 
        List<Signal> allSignals = new ArrayList<Signal>();
        List<Signal> weakRBS = new ArrayList<Signal>();
        List<Signal> strongRBS = new ArrayList<Signal>();
        
        List<String> allSignalsInfo = new ArrayList<String>();
        List<String> weakRBSInfo = new ArrayList<String>();
        List<String> strongRBSInfo = new ArrayList<String>();
        
        //Get All data
        for(String plasmid:signals.keySet()){
            kFolds.put(plasmid, new HashMap<Double, List<Set<Signal>>>());
            kFoldsIndx.put(plasmid, new HashMap());
            List<Set<Signal>> signalKFold = new ArrayList<Set<Signal>>();
            for(Double ahl:signals.get(plasmid).keySet()){
                signalKFold = new ArrayList<Set<Signal>>();
                kFolds.get(plasmid).put(ahl, new ArrayList<Set<Signal>>());
                List<Set<Integer>> kfold = generateKSets(signals.get(plasmid).get(ahl).size());
                kFoldsIndx.get(plasmid).put(ahl, kfold);
                for(int i=0;i<kSize;i++){
                    signalKFold.add(new HashSet<Signal>());
                }
                int count =0;
                for(Signal s: signals.get(plasmid).get(ahl)){
                    for(int i=0;i<kSize;i++){
                        Set<Integer> k = kfold.get(i);
                        if(k.contains(count)){
                            signalKFold.get(i).add(s);
                            break;
                        }
                    }
                    allSignals.add(s);
                    String infoLineAll = count +delimiter+ plasmid +delimiter+ ahl;
                    allSignalsInfo.add(infoLineAll);
                    
                    //Strong RBS
                    if(plasmid.equals("pL2f1433") || plasmid.equals("pL2f1434") || plasmid.equals("pL2f1435") || plasmid.equals("pL2f1436")){
                        strongRBS.add(s);
                        int index = strongRBS.size()-1;
                        String infoLine = index +delimiter+ plasmid +delimiter+ ahl;
                        strongRBSInfo.add(infoLine);
                    } else {
                    //Weak RBS
                        weakRBS.add(s);
                        int index = weakRBS.size()-1;
                        String infoLine = index +delimiter+ plasmid +delimiter+ ahl;
                        weakRBSInfo.add(infoLine);
                    }
                    count++;
                }
                kFolds.get(plasmid).get(ahl).addAll(signalKFold);
            }
        }
        
        List<List<Signal>> balancedSignals = new ArrayList<List<Signal>>();
        List<List<Signal>> balancedWeak = new ArrayList<List<Signal>>();
        List<List<Signal>> balancedStrong = new ArrayList<List<Signal>>();
        List<List<String>> balancedInfo = new ArrayList();
        List<List<String>> balancedWeakInfo = new ArrayList();
        List<List<String>> balancedStrongInfo = new ArrayList();
        
        for(int i=0;i<kSize;i++){
            balancedSignals.add(new ArrayList());
            balancedWeak.add(new ArrayList());
            balancedStrong.add(new ArrayList());
            balancedInfo.add(new ArrayList());
            balancedWeakInfo.add(new ArrayList());
            balancedStrongInfo.add(new ArrayList());
        }
        
        //Balanced K-fold sampling
        for(String plasmid:signals.keySet()){
            for(Double ahl:signals.get(plasmid).keySet()){
                for(int i=0;i<kSize;i++){
                    balancedSignals.get(i).addAll(kFolds.get(plasmid).get(ahl).get(i));
                    for(Integer indx:kFoldsIndx.get(plasmid).get(ahl).get(i)){
                        balancedInfo.get(i).add(indx+delimiter+plasmid+delimiter+ahl);
                    }
                    if(plasmid.equals("pL2f1433") || plasmid.equals("pL2f1434") || plasmid.equals("pL2f1435") || plasmid.equals("pL2f1436")){
                        balancedStrong.get(i).addAll(kFolds.get(plasmid).get(ahl).get(i));
                        for (Integer indx : kFoldsIndx.get(plasmid).get(ahl).get(i)) {
                            balancedStrongInfo.get(i).add(indx+delimiter+plasmid+delimiter+ahl);
                        }
                    } else {
                        balancedWeak.get(i).addAll(kFolds.get(plasmid).get(ahl).get(i));
                        for (Integer indx : kFoldsIndx.get(plasmid).get(ahl).get(i)) {
                            balancedWeakInfo.get(i).add(indx+delimiter+plasmid+delimiter+ahl);
                        }
                    }
                }
            }
        }
        
        for(int i=0;i<kSize;i++){
            
            //balancedAllSignalsFilepath
            String allIterationFilepath = balancedAllSignalsFilepath + i + Utilities.getSeparater();
            createFolder(allIterationFilepath);
            List<Signal> training = new ArrayList<Signal>();
            List<Signal> testing = new ArrayList<Signal>();
            List<String> trainingInfo = new ArrayList<String>();
            List<String> testingInfo = new ArrayList<String>();
            
            for(int j=0;j<balancedSignals.size();j++){
                if(i==j){
                    testing.addAll(balancedSignals.get(j));
                    testingInfo.addAll(balancedInfo.get(j));
                } else {
                    training.addAll(balancedSignals.get(j));
                    trainingInfo.addAll(balancedInfo.get(j));
                }
            }
            String allUsedPlasmidFilepath = allIterationFilepath + "training.csv";
            String allUnusedPlasmidFilepath = allIterationFilepath + "testing.csv";
            String allUsedInfoFilepath = allIterationFilepath + "training-info.csv";
            String allUnusedInfoFilepath = allIterationFilepath + "testing-info.csv";
            
            writeSignalsToFileWithHeader(allUsedPlasmidFilepath,training,time);
            writeSignalsToFileWithHeader(allUnusedPlasmidFilepath,testing,time);
            Utilities.writeToFile(allUsedInfoFilepath, trainingInfo);
            Utilities.writeToFile(allUnusedInfoFilepath, testingInfo);
            
            //balancedStrongFilepath
            String strongIterationFilepath = balancedStrongFilepath + i + Utilities.getSeparater();
            createFolder(strongIterationFilepath);
            
            training = new ArrayList<Signal>();
            testing = new ArrayList<Signal>();
            trainingInfo = new ArrayList<String>();
            testingInfo = new ArrayList<String>();
            
            for(int j=0;j<balancedStrong.size();j++){
                if(i==j){
                    testing.addAll(balancedStrong.get(j));
                    testingInfo.addAll(balancedStrongInfo.get(j));
                } else {
                    training.addAll(balancedStrong.get(j));
                    trainingInfo.addAll(balancedStrongInfo.get(j));
                }
            }
            
            allUsedPlasmidFilepath = strongIterationFilepath + "training.csv";
            allUnusedPlasmidFilepath = strongIterationFilepath + "testing.csv";
            allUsedInfoFilepath = strongIterationFilepath + "training-info.csv";
            allUnusedInfoFilepath = strongIterationFilepath + "testing-info.csv";
            
            writeSignalsToFileWithHeader(allUsedPlasmidFilepath,training,time);
            writeSignalsToFileWithHeader(allUnusedPlasmidFilepath,testing,time);
            Utilities.writeToFile(allUsedInfoFilepath, trainingInfo);
            Utilities.writeToFile(allUnusedInfoFilepath, testingInfo);
            
            //balancedWeakFilepath
            String weakIterationFilepath = balancedWeakFilepath + i + Utilities.getSeparater();
            createFolder(weakIterationFilepath);
            
            training = new ArrayList<Signal>();
            testing = new ArrayList<Signal>();
            trainingInfo = new ArrayList<String>();
            testingInfo = new ArrayList<String>();
            
            for(int j=0;j<balancedWeak.size();j++){
                if(i==j){
                    testing.addAll(balancedWeak.get(j));
                    testingInfo.addAll(balancedWeakInfo.get(j));
                } else {
                    training.addAll(balancedWeak.get(j));
                    trainingInfo.addAll(balancedWeakInfo.get(j));
                }
            }
            
            allUsedPlasmidFilepath = weakIterationFilepath + "training.csv";
            allUnusedPlasmidFilepath = weakIterationFilepath + "testing.csv";
            allUsedInfoFilepath = weakIterationFilepath + "training-info.csv";
            allUnusedInfoFilepath = weakIterationFilepath + "testing-info.csv";
            
            writeSignalsToFileWithHeader(allUsedPlasmidFilepath,training,time);
            writeSignalsToFileWithHeader(allUnusedPlasmidFilepath,testing,time);
            Utilities.writeToFile(allUsedInfoFilepath, trainingInfo);
            Utilities.writeToFile(allUnusedInfoFilepath, testingInfo);
            
        }
        
        //Random k-fold sampling
        List<Set<Integer>> allK = generateKSets(allSignals.size());
        List<Set<Integer>> strongK = generateKSets(strongRBS.size());
        List<Set<Integer>> weakK = generateKSets(weakRBS.size());
        
        for(int i=0;i<kSize;i++){
            String allIterationFilepath = randomAllSignalsFilepath + i + Utilities.getSeparater();
            createFolder(allIterationFilepath);
            String strongIterationFilepath = randomStrongFilepath + i + Utilities.getSeparater();
            createFolder(strongIterationFilepath);
            String weakIterationFilepath = randomWeakFilepath + i + Utilities.getSeparater();
            createFolder(weakIterationFilepath);
            
            
            List<Signal> allUsed = new ArrayList<Signal>();
            List<Signal> allUnused = new ArrayList<Signal>();
            List<String> allUsedInfo = new ArrayList<String>();
            List<String> allUnusedInfo = new ArrayList<String>();
            
            String allUsedPlasmidFilepath = allIterationFilepath + "training.csv";
            String allUnusedPlasmidFilepath = allIterationFilepath + "testing.csv";
            String allUsedInfoFilepath = allIterationFilepath + "training-info.csv";
            String allUnusedInfoFilepath = allIterationFilepath + "testing-info.csv";
            
            for(int j=0;j<allSignals.size();j++){
                if(allK.get(i).contains(j)){
                    allUnused.add(allSignals.get(j));
                    allUnusedInfo.add(allSignalsInfo.get(j));
                } else {
                    allUsed.add(allSignals.get(j));
                    allUsedInfo.add(allSignalsInfo.get(j));
                }
            }
            
            writeSignalsToFileWithHeader(allUsedPlasmidFilepath,allUsed,time);
            writeSignalsToFileWithHeader(allUnusedPlasmidFilepath,allUnused,time);
            Utilities.writeToFile(allUsedInfoFilepath, allUsedInfo);
            Utilities.writeToFile(allUnusedInfoFilepath, allUnusedInfo);
            
            
            //Strong
            allUsed = new ArrayList<Signal>();
            allUnused = new ArrayList<Signal>();
            allUsedInfo = new ArrayList<String>();
            allUnusedInfo = new ArrayList<String>();
            for(int j=0;j<strongRBS.size();j++){
                if(strongK.get(i).contains(j)){
                    allUnused.add(strongRBS.get(j));
                    allUnusedInfo.add(strongRBSInfo.get(j));
                } else {
                    allUsed.add(strongRBS.get(j));
                    allUsedInfo.add(strongRBSInfo.get(j));
                }
            }
            
            allUsedPlasmidFilepath = strongIterationFilepath + "training.csv";
            allUnusedPlasmidFilepath = strongIterationFilepath + "testing.csv";
            allUsedInfoFilepath = strongIterationFilepath + "training-info.csv";
            allUnusedInfoFilepath = strongIterationFilepath + "testing-info.csv";
            
            writeSignalsToFileWithHeader(allUsedPlasmidFilepath,allUsed,time);
            writeSignalsToFileWithHeader(allUnusedPlasmidFilepath,allUnused,time);
            Utilities.writeToFile(allUsedInfoFilepath, allUsedInfo);
            Utilities.writeToFile(allUnusedInfoFilepath, allUnusedInfo);
            
            
            //Weak
            allUsed = new ArrayList<Signal>();
            allUnused = new ArrayList<Signal>();
            allUsedInfo = new ArrayList<String>();
            allUnusedInfo = new ArrayList<String>();
            for(int j=0;j<weakRBS.size();j++){
                if(weakK.get(i).contains(j)){
                    allUnused.add(weakRBS.get(j));
                    allUnusedInfo.add(weakRBSInfo.get(j));
                } else {
                    allUsed.add(weakRBS.get(j));
                    allUsedInfo.add(weakRBSInfo.get(j));
                }
            }
            
            allUsedPlasmidFilepath = weakIterationFilepath + "training.csv";
            allUnusedPlasmidFilepath = weakIterationFilepath + "testing.csv";
            allUsedInfoFilepath = weakIterationFilepath + "training-info.csv";
            allUnusedInfoFilepath = weakIterationFilepath + "testing-info.csv";
            
            writeSignalsToFileWithHeader(allUsedPlasmidFilepath,allUsed,time);
            writeSignalsToFileWithHeader(allUnusedPlasmidFilepath,allUnused,time);
            Utilities.writeToFile(allUsedInfoFilepath, allUsedInfo);
            Utilities.writeToFile(allUnusedInfoFilepath, allUnusedInfo);
        }
        
        
    }
    
    //@Test
    public void generateBioSignalsTest() throws TLIException {
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

    private Map<String, Map<Double, List<Signal>>> readBioSignals(String rootfilepath) throws TLIException {
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
                        if(Double.valueOf(pieces[l + (k * time.size()) + 1]) < 0){
                            points.add(new Point(time.get(l), "t", 0, "x"));
                        } else {
                            points.add(new Point(time.get(l), "t", Double.valueOf(pieces[l + (k * time.size()) + 1]), "x"));
                        }
                        
                    }
                    signals.get(plasmidName).get(lasAHLconc.get(k)).add(new Signal(points));
                }
            }
        }
        return signals;
    }
    
    //@Test
    public void testFuelControl() throws TLIException{
        int run = 666;
        testFuelControl(Mode.KFold,run);
        testConsolidateFuelResults(Mode.KFold, run);
        System.out.println("FC done");
    }
    
    private double getThresholdValue(double range, double perc, int it){
        return (it * (perc*(range)));
    }
    
    public void testFuelControl(Mode mode, int run) throws TLIException {
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
                    "tpTrain" +delimiter+ // True Positive Training
                    "fnTrain" +delimiter+ // False Negative Training
                    "fpTrain" +delimiter+ // False Positive Training 
                    "tnTrain" +delimiter+ // True Negative Training
                    "testSize" +delimiter+ //testSize (used in PosNeg TLI)
                    "mcrTest" +delimiter+ //testing MCR
                    "fprTest" +delimiter+ //testing FPR
                    "fnrTest" +delimiter+ //testing FNR
                    "tpTest" +delimiter+ // True Positive Testing
                    "fnTest" +delimiter+ // False Negative Testing
                    "fpTest" +delimiter+ // False Positive Testing 
                    "tnTest" +delimiter+ // True Negative Testing
                    "runtime" +delimiter+ //Runtime
                    "t_t" +delimiter+ //xthreshold
                    "x_t_EGO" +delimiter+ //ythreshold
                    "c_t_EGO" +delimiter+ //cthreshold
                    "x_t_MAP" +delimiter+ //ythreshold
                    "c_t_MAP" +delimiter+ //cthreshold
                    "primitiveCount" //Number of primitives
                    ;
        
        double tmax = 59.7;
        
        double xmax_MAP = 1.0058;
        double xmin_MAP = 0.2985;
        
        double xmax_EGO = 1.1095;
        double xmin_EGO = 0.0071;
        
        double x_range_MAP = xmax_MAP - xmin_MAP;
        double x_range_EGO = xmax_EGO - xmin_EGO;
        
        double inc = 0.025;
        int steps = 20;
        
        for(int i = 1; i <= steps; i ++){
            for(int j = 1; j <= steps; j ++){
                System.out.format("i = %d, j = %d%n", i, j);
                for (int k = 0; k <= steps; k++) {
                    double x_t_MAP = getThresholdValue(x_range_MAP,inc,i);
                    double x_t_EGO = getThresholdValue(x_range_EGO,inc,i);
                    double t_t = getThresholdValue(tmax,inc,j);
                    double c_t_MAP = k*x_t_MAP;//getThresholdValue(x_range_MAP,inc,k);
                    double c_t_EGO = k*x_t_EGO;//getThresholdValue(x_range_EGO,inc,k);
                    if(c_t_MAP > x_range_MAP*3/4 || c_t_EGO > x_range_EGO*3/4){
                        continue;
                    }
                    String xplotdot = "x" + x_t_EGO;
                    String xplot = xplotdot.replaceAll("\\.", "_");
                    String yplotdot = "t" + t_t;
                    String yplot = yplotdot.replaceAll("\\.", "_");
                    String cplotdot = "c" + c_t_EGO;
                    String cplot = cplotdot.replaceAll("\\.", "_");

                    String plotsuffix = yplot + "-" + xplot + "-" + cplot;
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
                    walkFuelControl(root, root, t_t, x_t_EGO, x_t_MAP, c_t_EGO, c_t_MAP, fileLines);

                    String resultsFilepath = resultRoot + Utilities.getSeparater() + "result.csv";
                    Utilities.writeToFile(resultsFilepath, fileLines);
                }
            }
        }
        
        
        
    }

    //@Test
    public void testTransformFuel(){
        String path = Utilities.getSampleFilepath() + "fuelcontrol" + Utilities.getSeparater() + Mode.KFold.toString() + Utilities.getSeparater();
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
    public void testBioSignals() throws TLIException{
        String balancedAll = biosignalsfilepath + "allSignals" + Utilities.getSeparater()+ "balanced" + Utilities.getSeparater() ;
//        String randomAll = biosignalsfilepath + "allSignals" + Utilities.getSeparater() + "random" + Utilities.getSeparater();
//        String balancedWeak = biosignalsfilepath + "twoRBS" + Utilities.getSeparater() + "weak" + Utilities.getSeparater() + "balanced" + Utilities.getSeparater();
//        String randomWeak = biosignalsfilepath + "twoRBS" + Utilities.getSeparater() + "weak" + Utilities.getSeparater() + "random" + Utilities.getSeparater();
//        String balancedStrong = biosignalsfilepath + "twoRBS" + Utilities.getSeparater() + "strong" + Utilities.getSeparater() + "balanced" + Utilities.getSeparater();
//        String randomStrong = biosignalsfilepath + "twoRBS" + Utilities.getSeparater() + "strong" + Utilities.getSeparater() + "random" + Utilities.getSeparater();
        int run = 666;
        testBioSignals(balancedAll,run,"all_balanced");
//        testBioSignals(randomAll,run,"all_random");
//        testBioSignals(balancedWeak,run,"weak_balanced");
//        testBioSignals(randomWeak,run,"weak_random");
//        testBioSignals(balancedStrong,run,"strong_balanced");
//        testBioSignals(randomStrong,run,"strong_random");
    
        testConsolidateBioResults(run);
        System.out.println("Biosignals done");
    }
    
    public void testBioSignals(String root, int run, String jobName) throws TLIException {
        System.out.println("Biosignals test " + jobName);
        String headerLine = 
                "iterFolder" +delimiter+ 
                "trainSize" +delimiter+ 
                "mcrTrain" +delimiter+ 
                "fnrTrain" +delimiter+ 
                "avgRTPtrain" +delimiter+ 
                "testSize" +delimiter+ 
                "mcrTest" +delimiter+ 
                "fnrTest" +delimiter+ 
                "avgRTPtest" +delimiter+
                "avgRFNtest" +delimiter+
                "runtime" +delimiter+ 
                "t_t" +delimiter+ 
                "x_t" +delimiter+ 
                "c_t" +delimiter+ //cthreshold
                "primitiveCount" +delimiter+ //Number of primitives
                "clusterCount"
                ;
        
        double xmax = 3687;
        double tmax = 6.9;
        
        double inc = 0.025;
        int steps = 20;
        
        //double x_lim = xmax/2;
        //double t_lim = tmax/2;
        
        for(int i = 1; i <= steps; i ++){
            for(int j = 1; j <= steps; j ++){
                System.out.format("i = %d, j = %d%n", i, j);
                for (int k = 0; k <= steps; k++) {
                    double x_t = getThresholdValue(xmax,inc,i);
                    double t_t = getThresholdValue(tmax,inc,j);
                    double c_t = k*x_t;//getThresholdValue(xmax,inc,k);
                    if(c_t > xmax*3/4){
                        continue;
                    }
                    List<String> filelines = new ArrayList<String>();
                    filelines.add(headerLine);

                    String xplotdot = "x" + x_t;
                    String xplot = xplotdot.replaceAll("\\.", "_");
                    String yplotdot = "t" + t_t;
                    String yplot = yplotdot.replaceAll("\\.", "_");
                    String cplotdot = "c" + c_t;
                    String cplot = cplotdot.replaceAll("\\.", "_");

                    String plotsuffix = yplot + "-" + xplot + "-" + cplot;
                    String runRoot = root + "run"+run + Utilities.getSeparater();
                    createFolder(runRoot);
                    String resultRoot = runRoot + plotsuffix;
                    createFolder(resultRoot);
                    
                    walkBioSignals(root, root, t_t, x_t, c_t, filelines);
                    
                    String resultFilepath = resultRoot + Utilities.getSeparater() + jobName+"_result.csv";
                    Utilities.writeToFile(resultFilepath, filelines);
                }
            }
        }
    }
    
    @Test
    public void testPyPlotSpecificBioSignal() throws TLIException{
        int i=3;
        String filepath = Utilities.getSampleFilepath() + "biosignals/allSignals/balanced/" + i + "/";
        String outputfilepath = Utilities.getSampleFilepath() + "biosignals/forPaper/pyplot/";
        createFolder(outputfilepath);
        double x_t = 1843.5;
        double t_t = 3.45;
        double c_t = 1843.5;
        String trainingFilepath = filepath + "training.csv";
        String testingFilepath = filepath + "testing.csv";

        String plot_test = outputfilepath + "grid_test" + i + ".png";
        String plot_train = outputfilepath + "grid_train" + i + ".png";
        List<Signal> training = Utilities.getRowSignals(trainingFilepath, true);
        List<Signal> testing = Utilities.getRowSignals(testingFilepath, true);
        Grid grid = new Grid(training, t_t, x_t);
        TreeNode stl = TemporalLogicInference.getSTL(grid, c_t);
        System.out.println(stl);
        List<Signal> notSatisfy = new ArrayList<Signal>();
        List<Signal> satisfy = new ArrayList<Signal>();
        for (Signal signal : testing) {
            double r = Validation.getRobustness(stl, signal);
            if (r < 0) {
                notSatisfy.add(signal);
            } else {
                satisfy.add(signal);
            }
        }
        List<String> plotlinestest = PyPlotAdaptor.generatePlotScript(grid, satisfy, notSatisfy);
        List<String> plotlinestrain = PyPlotAdaptor.generatePlotScript(grid);
        
        Utilities.writeToFile(outputfilepath + "highthreshtrain.py", plotlinestrain);
        Utilities.writeToFile(outputfilepath + "highthreshtest.py", plotlinestest);
        
        List<Signal> allsignals = new ArrayList<Signal>();
        allsignals.addAll(training);
        allsignals.addAll(testing);
        List<String> plotallsignals = PyPlotAdaptor.generateSignalPlotScript(allsignals);
        List<String> plottestsignals = PyPlotAdaptor.generateSignalPlotScript(testing);
        List<String> plottrainsignals = PyPlotAdaptor.generateSignalPlotScript(training);
        
        Utilities.writeToFile(outputfilepath + "allsignals.py", plotallsignals);
        Utilities.writeToFile(outputfilepath + "testsignals.py", plottestsignals);
        Utilities.writeToFile(outputfilepath + "trainsignals.py", plottrainsignals);
        
        
    }
    
    
    //@Test
    public void testSpecificBioSignal() throws TLIException{
        
        //for (int i = 0; i < 10; i++) {
        int i=3;
            String filepath = Utilities.getSampleFilepath() + "biosignals/allSignals/balanced/"+i+"/";
            String outputfilepath = Utilities.getSampleFilepath() + "biosignals/forPaper/highThresh/foundyou/";
            createFolder(outputfilepath);
//            double x_t = 184.35;
//            double t_t = 0.345;
//            double c_t = 0;
            double x_t = 1843.5;
            double t_t = 3.45;
            double c_t = 1843.5;
            
            String trainingFilepath = filepath + "training.csv";
            String testingFilepath = filepath + "testing.csv";

            String plot_test = outputfilepath + "grid_test"+i+".png";
            String plot_train = outputfilepath + "grid_train"+i+".png";
            List<Signal> training = Utilities.getRowSignals(trainingFilepath, true);
            List<Signal> testing = Utilities.getRowSignals(testingFilepath, true);
            Grid grid = new Grid(training, t_t, x_t);
            TreeNode stl = TemporalLogicInference.getSTL(grid, c_t);
            System.out.println(stl);
            List<Signal> notSatisfy = new ArrayList<Signal>();
            List<Signal> satisfy = new ArrayList<Signal>();
            for(Signal signal:testing){
                double r = Validation.getRobustness(stl, signal);
                if(r<0){
                    notSatisfy.add(signal);
                } else {
                    satisfy.add(signal);
                }
            }
            JavaPlotAdaptor.plotToFile(JavaPlotAdaptor.plotGrid_withTestingData(grid, satisfy, notSatisfy), plot_test);
            JavaPlotAdaptor.plotToFile(JavaPlotAdaptor.plotGrid(grid), plot_train);
        //}
        
    }
    
    //@Test
    public void testSpecificBiosignal() throws TLIException{
        System.out.println("Specific Test");
        String filepath = Utilities.getSampleFilepath() + "biosignals/separatedSignals/pL2f1439/1/testData/10/11/";
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
        JavaPlotAdaptor.plotToFile(JavaPlotAdaptor.plotCluster(grid,clusters.get(0)), cluster1);
        JavaPlotAdaptor.plotToFile(JavaPlotAdaptor.plotCluster(grid,clusters.get(1)), cluster2);
        JavaPlotAdaptor.plotToFile(JavaPlotAdaptor.plotCluster(grid,clusters.get(2)), cluster3);
        
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
    public void testConsolidateFuelResults(Mode mode, int run){
        
        String path = fuelControlDatafilepath + (mode.toString() + "_Run" + run) + Utilities.getSeparater();
        List<String> finalLines = new ArrayList<String>();
        List<String> lines = new ArrayList<String>();
        String headerLine = 
                    "sizeFolder" +delimiter + //sizeFolder
                    "iterFolder" +delimiter+ //iterFolder
                    "trainSize" +delimiter+ //trainSize (used in PosNeg TLI)
                    "mcrTrain" +delimiter+ //training MCR
                    "fprTrain" +delimiter+ //training FPR
                    "fnrTrain" +delimiter+ //training FNR
                    "tpTrain" +delimiter+ // True Positive Training
                    "fnTrain" +delimiter+ // False Negative Training
                    "fpTrain" +delimiter+ // False Positive Training 
                    "tnTrain" +delimiter+ // True Negative Training
                    "testSize" +delimiter+ //testSize (used in PosNeg TLI)
                    "mcrTest" +delimiter+ //testing MCR
                    "fprTest" +delimiter+ //testing FPR
                    "fnrTest" +delimiter+ //testing FNR
                    "tpTest" +delimiter+ // True Positive Testing
                    "fnTest" +delimiter+ // False Negative Testing
                    "fpTest" +delimiter+ // False Positive Testing 
                    "tnTest" +delimiter+ // True Negative Testing
                    "runtime" +delimiter+ //Runtime
                    "t_t" +delimiter+ //xthreshold
                    "x_t_EGO" +delimiter+ //ythreshold
                    "c_t_EGO" +delimiter+ //cthreshold
                    "x_t_MAP" +delimiter+ //ythreshold
                    "c_t_MAP" +delimiter+ //cthreshold
                    "primitiveCount" //Number of primitives
                    ;
        finalLines.add(headerLine);
        walkResults(path,lines);
        finalLines.addAll(lines);
        
        String filepath = path + "consolidated.csv";
        Utilities.writeToFile(filepath, finalLines);
    }
    
    //@Test
    public void testConsolidateBioResults(int run){
        String balancedAll = biosignalsfilepath + "allSignals" + Utilities.getSeparater()+ "balanced" + Utilities.getSeparater() + "run" + run + Utilities.getSeparater();
        String randomAll = biosignalsfilepath + "allSignals" + Utilities.getSeparater() + "random" + Utilities.getSeparater() + "run" + run + Utilities.getSeparater();
//        String balancedWeak = biosignalsfilepath + "twoRBS" + Utilities.getSeparater() + "weak" + Utilities.getSeparater() + "balanced" + Utilities.getSeparater() + "run" + run + Utilities.getSeparater();
//        String randomWeak = biosignalsfilepath + "twoRBS" + Utilities.getSeparater() + "weak" + Utilities.getSeparater() + "random" + Utilities.getSeparater() + "run" + run + Utilities.getSeparater();
//        String balancedStrong = biosignalsfilepath + "twoRBS" + Utilities.getSeparater() + "strong" + Utilities.getSeparater() + "balanced" + Utilities.getSeparater() + "run" + run + Utilities.getSeparater();
//        String randomStrong = biosignalsfilepath + "twoRBS" + Utilities.getSeparater() + "strong" + Utilities.getSeparater() + "random" + Utilities.getSeparater() + "run" + run + Utilities.getSeparater();
        testConsolidateBioResults(balancedAll);
        testConsolidateBioResults(randomAll);
//        testConsolidateBioResults(balancedWeak);
//        testConsolidateBioResults(randomWeak);
//        testConsolidateBioResults(balancedStrong);
//        testConsolidateBioResults(randomStrong);
        
    }
    
    public void testConsolidateBioResults(String path){
        List<String> finalLines = new ArrayList<String>();
        List<String> lines = new ArrayList<String>();
        String headerLine = 
                "iterFolder" +delimiter+ 
                "trainSize" +delimiter+ 
                "mcrTrain" +delimiter+ 
                "fnrTrain" +delimiter+ 
                "avgRTPtrain" +delimiter+ 
                "testSize" +delimiter+ 
                "mcrTest" +delimiter+ 
                "fnrTest" +delimiter+ 
                "avgRTPtest" +delimiter+
                "avgRFNtest" +delimiter+
                "runtime" +delimiter+ 
                "t_t" +delimiter+ 
                "x_t" +delimiter+ 
                "c_t" +delimiter+ //cthreshold
                "primitiveCount" +delimiter+ //Number of primitives
                "clusterCount"
                ;
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
    
    private void walkFuelControl(String path, String resultsRoot, double t_t, double x_t_EGO, double x_t_MAP, double c_t_EGO, double c_t_MAP, List<String> fileLines) throws TLIException {
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
                walkFuelControl(f.getAbsolutePath(), resultsRoot, t_t, x_t_EGO, x_t_MAP, c_t_EGO, c_t_MAP, fileLines);
            } // Reached all Files
            else {
                if(f.getName().contains("DS_Store")){
                    continue; //Fuck you mac.... FAACCKKKK YOU, says Rachael to the Mac.. :( 
                }
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
            
            String xplotdot = "t" + t_t;
            String xplot = xplotdot.replaceAll("\\.", "_");
            String yplotdot = "x" + x_t_EGO;
            String yplot = yplotdot.replaceAll("\\.", "_");
            String cplotdot = "c" + c_t_EGO;
            String cplot = cplotdot.replaceAll("\\.", "_");
            
            long tstart = System.nanoTime();
            Grid gridEgo = new Grid(training_PosEGO, t_t, x_t_EGO);
            Grid gridMap = new Grid(training_PosMAP, t_t, x_t_MAP);
            TreeNode stlEgo = TemporalLogicInference.getSTL(gridEgo, c_t_EGO);
            TreeNode stlMap = TemporalLogicInference.getSTL(gridMap, c_t_MAP); //Rachael really doesn't trust her mac.. or maybe she does.. she gave me the dagger eyes.. 
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
            
            Map<Integer,Double> train_Pos_EGO_r = new HashMap<Integer,Double>();
            Map<Integer,Double> train_Neg_EGO_r = new HashMap<Integer,Double>();
            Map<Integer,Double> train_Pos_MAP_r = new HashMap<Integer,Double>();
            Map<Integer,Double> train_Neg_MAP_r = new HashMap<Integer,Double>();
            
            Map<Integer,Double> test_Pos_EGO_r = new HashMap<Integer,Double>();
            Map<Integer,Double> test_Neg_EGO_r = new HashMap<Integer,Double>();
            Map<Integer,Double> test_Pos_MAP_r = new HashMap<Integer,Double>();
            Map<Integer,Double> test_Neg_MAP_r = new HashMap<Integer,Double>();
            
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
                train_Pos_EGO_r.put(count, r);
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
                train_Pos_MAP_r.put(count, r);
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
                test_Pos_EGO_r.put(count, r);
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
                test_Pos_MAP_r.put(count, r);
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
                train_Neg_EGO_r.put(count, r);
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
                train_Neg_MAP_r.put(count, r);
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
                test_Neg_EGO_r.put(count, r);
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
                test_Neg_MAP_r.put(count, r);
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
            
            //Calculate Total Positive Training Robustnes = min(EGO,MAP)
            double pos_training_r = 0.0;
            double pos_training_f = 0.0;
            
            double neg_training_r = 0.0;
            double neg_training_f = 0.0;
            
            double pos_testing_r = 0.0;
            double pos_testing_f = 0.0;
            
            double neg_testing_r = 0.0;
            double neg_testing_f = 0.0;
            
            for(Integer i:pos_training_robust){
                double val = (train_Pos_EGO_r.get(i) < train_Pos_MAP_r.get(i)) ? train_Pos_EGO_r.get(i) :train_Pos_MAP_r.get(i);
                pos_training_r += val;
            }
            
            for(Integer i: pos_training_fail){
                double val = (train_Pos_EGO_r.get(i) < train_Pos_MAP_r.get(i)) ? train_Pos_EGO_r.get(i) :train_Pos_MAP_r.get(i);
                pos_training_f += val;
            }
            
            
            for(Integer i:neg_training_robust){
                double val = (train_Neg_EGO_r.get(i) < train_Neg_MAP_r.get(i)) ? train_Neg_EGO_r.get(i) :train_Neg_MAP_r.get(i);
                neg_training_r += val;
            }
            
            for(Integer i:neg_training_fail){
                double val = (train_Neg_EGO_r.get(i) < train_Neg_MAP_r.get(i)) ? train_Neg_EGO_r.get(i) :train_Neg_MAP_r.get(i);
                neg_training_f += val;
            }
            
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
            
            
            for(Integer i:pos_testing_robust){
                double val = (test_Pos_EGO_r.get(i) < test_Pos_MAP_r.get(i)) ? test_Pos_EGO_r.get(i) :test_Pos_MAP_r.get(i);
                pos_testing_r += val;
            }
            
            for(Integer i:pos_testing_fail){
                double val = (test_Pos_EGO_r.get(i) < test_Pos_MAP_r.get(i)) ? test_Pos_EGO_r.get(i) :test_Pos_MAP_r.get(i);
                pos_testing_f += val;
            }
            
            for(Integer i:neg_testing_robust){
                double val = (test_Neg_EGO_r.get(i) < test_Neg_MAP_r.get(i)) ? test_Neg_EGO_r.get(i) :test_Neg_MAP_r.get(i);
                neg_testing_r += val;
            }
            
            for(Integer i:neg_testing_fail){
                double val = (test_Neg_EGO_r.get(i) < test_Neg_MAP_r.get(i)) ? test_Neg_EGO_r.get(i) :test_Neg_MAP_r.get(i);
                neg_testing_f += val;
            }
            
            double tp_pos_training = (pos_training_robust.size() <= 0) ? 0.0 : (pos_training_r/((double)pos_training_robust.size()));
            double fn_pos_training = (pos_training_fail.size() <= 0) ? 0.0 : (pos_training_f/((double)pos_training_fail.size()));
            double fp_neg_training = (neg_training_robust.size() <= 0) ? 0.0 : (neg_training_r/((double)neg_training_robust.size()));
            double tn_neg_training = (neg_training_fail.size() <= 0) ? 0.0 : (neg_training_f/((double)neg_training_fail.size()));
            
            double tp_pos_testing = (pos_testing_robust.size() <= 0) ? 0.0 : (pos_testing_r/((double)pos_testing_robust.size()));
            double fn_pos_testing = (pos_testing_fail.size() <= 0) ? 0.0 : (pos_testing_f/((double)pos_testing_fail.size()));
            double fp_neg_testing = (neg_testing_robust.size() <= 0) ? 0.0 : (neg_testing_r/((double)neg_testing_robust.size()));
            double tn_neg_testing = (neg_testing_fail.size() <= 0) ? 0.0 : (neg_testing_f/((double)neg_testing_fail.size()));
            
            
            double fpr_training = ((double)neg_training_robust.size()) / ((double)(neg_training_robust.size() + pos_training_robust.size()));
            double fpr_testing = ((double)neg_testing_robust.size()) / ((double)(neg_testing_robust.size() + pos_testing_robust.size()));
            
            double fnr_training = ((double)pos_training_fail.size()) / ((double)(neg_training_fail.size() + pos_training_fail.size()));
            double fnr_testing = ((double)pos_testing_fail.size()) / ((double)(neg_testing_fail.size() + pos_testing_fail.size()));
            
            double mcr_training = ((double)neg_training_robust.size() + pos_training_fail.size()) / ((double)(neg_training_robust.size() + pos_training_robust.size() + neg_training_fail.size() + pos_training_fail.size()));
            double mcr_testing = ((double)neg_testing_robust.size() + pos_testing_fail.size()) / ((double)(neg_testing_robust.size() + pos_testing_robust.size() + neg_testing_fail.size() + pos_testing_fail.size()));
            
            int trainingSize = (neg_training_robust.size() + pos_training_robust.size() + neg_training_fail.size() + pos_training_fail.size());
            int testingSize = (neg_testing_robust.size() + pos_testing_robust.size() + neg_testing_fail.size() + pos_testing_fail.size());
            int primitiveCount = (getPrimitiveCount(stlEgo) + getPrimitiveCount(stlMap));
            String line = 
                    sampleSize +delimiter + //sizeFolder
                    iteration +delimiter+ //iterFolder
                    trainingSize +delimiter+ //trainSize (used in PosNeg TLI)
                    mcr_training +delimiter+ //training MCR
                    fpr_training +delimiter+ //training FPR
                    fnr_training +delimiter+ //training FNR
                    tp_pos_training +delimiter+ // True Positive Training
                    fn_pos_training +delimiter+ // False Negative Training
                    fp_neg_training +delimiter+ // False Positive training 
                    tn_neg_training +delimiter+ // True Negative training
                    testingSize +delimiter+ //testSize (used in PosNeg TLI)
                    mcr_testing +delimiter+ //testing MCR
                    fpr_testing +delimiter+ //testing FPR
                    fnr_testing +delimiter+ //testing FNR
                    tp_pos_testing +delimiter+ // True Positive Testing
                    fn_pos_testing +delimiter+ // False Negative Testing
                    fp_neg_testing +delimiter+ // False Positive Testing 
                    tn_neg_testing +delimiter+ // True Negative Testing
                    runtime +delimiter+ //Runtime
                    t_t +delimiter+ //xthreshold
                    x_t_EGO +delimiter+ //ythreshold
                    c_t_EGO +delimiter+ //cthreshold
                    x_t_MAP +delimiter+ //ythreshold
                    c_t_MAP +delimiter+ //cthreshold
                    primitiveCount //Primitive Count
                    ;
            fileLines.add(line);
        }

    }
    
    private void walkBioSignals(String path, String resultsRoot, double xthreshold, double ythreshold, double cthreshold, List<String> filelines) throws TLIException {
        File root = new File(path);
        File[] list = root.listFiles();

        if (list == null) {
            return;
        }
        
        boolean analysis = false;
        List<Signal> training = new ArrayList<Signal>();
        List<Signal> testing = new ArrayList<Signal>();
        int sampleSize = 0;
        int iteration = 0;
        for (File f : list) {
            if (f.isDirectory()) {
                walkBioSignals(f.getAbsolutePath(), resultsRoot, xthreshold, ythreshold, cthreshold, filelines);
            } // Reached all Files
            else {
                String pathPieces[] = filepathPieces(f.getAbsolutePath(),resultsRoot);
                if (f.getName().equals("training.csv")) {
                    
                    //sampleSize = Integer.valueOf(pathPieces[pathPieces.length - 3].trim());
                    iteration = Integer.valueOf(pathPieces[pathPieces.length - 2].trim());
                    
                    training = Utilities.getRowSignals(f.getAbsolutePath(), true);
                    analysis = true;
                } else if(f.getName().equals("testing.csv")){
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
            double trainingTruePosRobustnessTot = 0.0;
            double testingFalseNegRobustnessTot = 0.0;
            double testingTruePosRobustnessTot = 0.0;
            
            for(Signal s:training){
                double r = Validation.getRobustness(stl, s);
                if(r < 0){
                    training_fail.add(count);
                } else {
                    training_robust.add(count);
                    trainingTruePosRobustnessTot+= r;
                }
                count++;
            }
            count =0;
            for(Signal s:testing){
                double r = Validation.getRobustness(stl, s);
                if(r < 0){
                    testing_fail.add(count);
                    testingFalseNegRobustnessTot+= r;
                } else {
                    testing_robust.add(count);
                    testingTruePosRobustnessTot+= r;
                }
                count++;
            }

            double fnrTrain = ((double)training_fail.size()) / ((double) training.size());
            double mcrTrain = fnrTrain;
            
            double fnrTest = ((double)testing_fail.size()) / ((double) testing.size());
            double mcrTest = fnrTest;
            
            double avgRTPtrain = training_robust.size() <= 0 ? 0 :
                    trainingTruePosRobustnessTot / ((double)training_robust.size());
            double avgRFNtest = testing_fail.size() <= 0 ? 0 :
                    testingFalseNegRobustnessTot / ((double)testing_fail.size());
            double avgRTPtest = testing_robust.size() <= 0 ? 0 :
                    testingTruePosRobustnessTot / ((double)testing_robust.size());
            
            int primitiveCount = getPrimitiveCount(stl);
            int clusterCount = getClusterCount(stl);
            
//            String headerLine = 
//                "iterFolder" +delimiter+ 
//                "trainSize" +delimiter+ 
//                "mcrTrain" +delimiter+ 
//                "fnrTrain" +delimiter+ 
//                "avgRTrain" +delimiter+ 
//                "testSize" +delimiter+ 
//                "mcrTest" +delimiter+ 
//                "fnrTest" +delimiter+ 
//                "avgRTest" +delimiter+
//                "runtime" +delimiter+ 
//                "t_t" +delimiter+ 
//                "x_t" +delimiter+ 
//                "c_t" +delimiter+ //cthreshold
//                "primitiveCount" +delimiter+ //Number of primitives
//                "clusterCount"
//                ;
            
            String line = 
                    iteration +delimiter+ 
                    training.size() +delimiter+ 
                    mcrTrain +delimiter+
                    fnrTrain +delimiter+
                    avgRTPtrain +delimiter+
                    testing.size() +delimiter+ 
                    mcrTest +delimiter+
                    fnrTest +delimiter+
                    avgRTPtest +delimiter+
                    avgRFNtest +delimiter+
                    timeElapsed +delimiter+ 
                    xthreshold +delimiter+ 
                    ythreshold +delimiter+ 
                    cthreshold +delimiter+
                    primitiveCount +delimiter+
                    clusterCount
                    ;
            
            filelines.add(line);
        }
    }
    
    
    private void walkBioSignals_old(String path, String resultsRoot, double xthreshold, double ythreshold, double cthreshold, List<String> filelines) throws TLIException {
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
    
    
    private int getClusterCount(TreeNode node){
        return getAllDisjunctionChildren(node).size();
    }
    
    private int getPrimitiveCount(TreeNode node){
        int count = 0;
        for(TreeNode dChild : getAllDisjunctionChildren(node)){
            count += getAllConjunctionChildren(dChild).size();
        }
        return count;
    }
    
    private List<TreeNode> getAllDisjunctionChildren(TreeNode node){
        List<TreeNode> children = new ArrayList<TreeNode>();
        
        if(node instanceof DisjunctionNode){
            DisjunctionNode dnode = (DisjunctionNode)node;
            children.addAll(getAllDisjunctionChildren(dnode.left));
            children.addAll(getAllDisjunctionChildren(dnode.right));
        } else {
            children.add(node);
        }
        return children;
    }
    
    private List<TreeNode> getAllConjunctionChildren(TreeNode node){
        List<TreeNode> children = new ArrayList<TreeNode>();
        
        if(node instanceof ConjunctionNode){
            ConjunctionNode cnode = (ConjunctionNode)node;
            children.addAll(getAllConjunctionChildren(cnode.left));
            children.addAll(getAllConjunctionChildren(cnode.right));
        } else {
            children.add(node);
        }
        return children;
    }
    
    
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
