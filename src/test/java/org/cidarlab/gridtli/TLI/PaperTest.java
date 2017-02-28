/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.gridtli.TLI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import org.cidarlab.gridtli.DOM.Point;
import org.cidarlab.gridtli.DOM.Signal;
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
    int iterations = 20;
    
    //@Test
    public void generateFuelControlTest(){
        String testdatafilepath = fuelControlDatafilepath + "testData";
        if(!Utilities.validFilepath(testdatafilepath)){
            Utilities.makeDirectory(testdatafilepath);
        }
        List<Signal> posego = Utilities.getSignalsBioCPS(posegofile,false);
        List<Signal> negego = Utilities.getSignalsBioCPS(negegofile,false);
        List<Signal> posmap = Utilities.getSignalsBioCPS(posmapfile,false);
        List<Signal> negmap = Utilities.getSignalsBioCPS(negmapfile,false);
        
        //System.out.println(posego.size());
        //System.out.println(negego.size());
        //System.out.println(posmap.size());
        //System.out.println(negmap.size());
        
        for(int i=0;i<samplesize;i++){
            String subsetfilepath = testdatafilepath + Utilities.getSeparater() + getSubsetNumber(i,posego.size());
            if(!Utilities.validFilepath(subsetfilepath)){
                Utilities.makeDirectory(subsetfilepath);
            }
            for(int j=0;j<iterations;j++){
                String iterationsFilepath = subsetfilepath + Utilities.getSeparater() + j;
                if(!Utilities.validFilepath(iterationsFilepath)){
                    Utilities.makeDirectory(iterationsFilepath);
                }
                Set<Integer> usedpos = new HashSet<Integer>();
                while(usedpos.size() < getSubsetNumber(i,posego.size())){
                    int num = getRandom(0,posego.size()-1);
                    if(!usedpos.contains(num)){
                        usedpos.add(num);
                    }
                }
                Set<Integer> usedneg = new HashSet<Integer>();
                while(usedneg.size() < getSubsetNumber(i,posego.size())){
                    int num = getRandom(0,negego.size()-1);
                    if(!usedneg.contains(num)){
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
                
                for(int k=0;k<posego.size();k++){
                    if(usedpos.contains(k)){
                        usedposego.add(posego.get(k));
                        usedposmap.add(posmap.get(k));
                        usedposlist.add(k);
                    } else {
                        unusedposego.add(posego.get(k));
                        unusedposmap.add(posmap.get(k));
                        unusedposlist.add(k);
                    }
                    if(usedneg.contains(k)){
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
                
                writeSignalsToFile(usedposegoFilepath,usedposego);
                writeSignalsToFile(unusedposegoFilepath,unusedposego);
                writeSignalsToFile(usedposmapFilepath,usedposmap);
                writeSignalsToFile(unusedposmapFilepath,unusedposmap);
                writeSignalsToFile(usednegegoFilepath,usednegego);
                writeSignalsToFile(unusednegegoFilepath,unusednegego);
                writeSignalsToFile(usednegmapFilepath,usednegmap);
                writeSignalsToFile(unusednegmapFilepath,unusednegmap);
               
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
    
    private void writeSignalsToFile(String filepath, List<Signal> signals){
        List<String> lines = new ArrayList<String>();
        for(Signal signal:signals){
            String line = "";
            for(int i=0;i<signal.getPoints().size()-1;i++){
                line += signal.getPoints().get(i).getY() + ",";
            }
            line += signal.getPoints().get(signal.getPoints().size()-1).getY();
            lines.add(line);
        }
        Utilities.writeToFile(filepath, lines);
    } 
    
    private void writeSignalsToFileWithHeader(String filepath, List<Signal> signals, List<Double> header){
        List<String> lines = new ArrayList<String>();
        String head = "";
        for(int i=0;i<header.size()-1;i++){
            head += header.get(i) + ",";
        }
        head += header.get(header.size()-1);
        lines.add(head);
        for(Signal signal:signals){
            String line = "";
            if (signal.getPoints().size() == header.size()) {
                for (int i = 0; i < signal.getPoints().size() - 1; i++) {
                    line += signal.getPoints().get(i).getY() + ",";
                }
                line += signal.getPoints().get(signal.getPoints().size()-1).getY();
            } else {
                int signalCounter = 0;
                for(int i=0; i<header.size();i++){
                    double x = signal.getPoints().get(signalCounter).getX();
                    double y = signal.getPoints().get(signalCounter).getY();
                    if(header.get(i).equals(x)){
                        line += y + ",";
                        signalCounter++;
                    } else {
                        line += ",";
                    }
                }
                line = line.substring(0, line.length()-1);
            }
            lines.add(line);
        }
        Utilities.writeToFile(filepath, lines);
    
    }
    
    private int getRandom(int min, int max){
        Random random = new Random();
        return (random.nextInt(max - min + 1) + min);
    }
    
    private int getSubsetNumber(int i, int max){
        switch(i){
            case 0: return ((max / 100) * 10);
            case 1: return ((max / 100) * 25);
            case 2: return ((max / 100) * 50);
            //case 3: return 300;
        }
        return 0;
    }
    
    String biofilesuffix = "GeoMeanMEPTR";
    String biosignalsfilepath = Utilities.getResourcesFilepath() + "biosignals" + Utilities.getSeparater();
    int biosamples = 20;
    List<Double> time = new ArrayList<Double>();
    List<Double> lasAHLconc = new ArrayList<Double>();
    
    @Test
    public void generateBioSignalsTest(){
        Map<String,Map<Double,List<Signal>>> signals = readBioSignals(biosignalsfilepath);
        String separateSignalsFilepath = biosignalsfilepath + "separatedSignals";
        if(!Utilities.validFilepath(separateSignalsFilepath)){
            Utilities.makeDirectory(separateSignalsFilepath);
        }
        for(String plasmid : signals.keySet()){
            String plasmidDir = separateSignalsFilepath + Utilities.getSeparater() + plasmid;
            if(!Utilities.validFilepath(plasmidDir)){
                Utilities.makeDirectory(plasmidDir);
            }
            //Set 1
            String set1 = plasmidDir + Utilities.getSeparater() + "1" + Utilities.getSeparater();
            if(!Utilities.validFilepath(set1)){
                Utilities.makeDirectory(set1);
            }
            List<Signal> allSignals = new ArrayList<Signal>();
            for(Double ahl : signals.get(plasmid).keySet()){
                allSignals.addAll(signals.get(plasmid).get(ahl));
            }
            String allSignalsFilepath = set1 + "allSignals.csv";
            writeSignalsToFileWithHeader(allSignalsFilepath,allSignals,time);
            String testdataFilepath = set1 + "testData" + Utilities.getSeparater();
            if(!Utilities.validFilepath(testdataFilepath)){
                Utilities.makeDirectory(testdataFilepath);
            }
            for(int i=0;i<samplesize;i++){
                String subsetfilepath = testdataFilepath + getSubsetNumber(i, allSignals.size()) + Utilities.getSeparater();
                if(!Utilities.validFilepath(subsetfilepath)){
                    Utilities.makeDirectory(subsetfilepath);
                }
                for(int j=0;j < iterations;j++){
                    String iterationfilepath = subsetfilepath + j + Utilities.getSeparater();
                    if(!Utilities.validFilepath(iterationfilepath)){
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
                    for(int k=0;k<allSignals.size();k++){
                        if(usedIndex.contains(k)){
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
                    
                    writeSignalsToFileWithHeader(usedFilepath,used,time);
                    writeSignalsToFileWithHeader(unusedFilepath,unused,time);
                    
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
    
    
    private Map<String,Map<Double,List<Signal>>> readBioSignals(String rootfilepath){
        Map<String,Map<Double,List<Signal>>> signals = new HashMap<String,Map<Double,List<Signal>>>();
        
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
                for (int j=1; j< lines.get(1).length; j++){
                    if(time.contains(Double.valueOf(lines.get(1)[j]))){
                       break; 
                    }
                    time.add(Double.valueOf(lines.get(1)[j]));
                }
            
            }
            
            
            //Extract Signal values
            for (int j=2; j < lines.size(); j++) {
                String pieces[] = lines.get(j);
                String namepieces[] = pieces[0].split("=");
                String plasmidName = namepieces[1].trim();
                if(!signals.containsKey(plasmidName)){
                    Map<Double,List<Signal>> ahlmap = new HashMap<Double,List<Signal>>();
                    for(Double d:lasAHLconc){
                        ahlmap.put(d, new ArrayList<Signal>());
                    }
                    signals.put(plasmidName, ahlmap);
                }
                for(int k=0;k<lasAHLconc.size();k++){
                    List<Point> points = new ArrayList<Point>();
                    for(int l=0;l<time.size(); l++){
                        if(pieces[l+(k*time.size())+1].equals("nan") || pieces[l+(k*time.size())+1].equals("None")){
                            //System.out.println(csvfile);
                            //System.out.println(k + "," + l);
                            continue;
                        }
                        points.add(new Point(time.get(l),"t",Double.valueOf(pieces[l+(k*time.size())+1]),"x"));
                    }
                    signals.get(plasmidName).get(lasAHLconc.get(k)).add(new Signal(points));
                }
            }
        }
        return signals;
    }
    
    
    
}
