/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.gridtli.TLI;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.cidarlab.gridtli.DOM.Point;
import org.cidarlab.gridtli.DOM.Signal;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author prash
 */
public class Utilities {
    
    public static void printDebugStatement(String message){
        System.out.println("#########################################");
        System.out.println("######################" + message);
        System.out.println("#########################################");
    }
    
    //<editor-fold desc="OS Check">
    public static boolean isSolaris() {
        String os = System.getProperty("os.name");
        return isSolaris(os);
    }

    public static boolean isSolaris(String os) {
        if (os.toLowerCase().indexOf("sunos") >= 0) {
            return true;
        }
        return false;
    }

    public static boolean isWindows() {
        String os = System.getProperty("os.name");
        return isWindows(os);
    }

    public static boolean isWindows(String os) {
        if (os.toLowerCase().indexOf("win") >= 0) {
            return true;
        }
        return false;
    }

    public static boolean isLinux() {
        String os = System.getProperty("os.name");
        return isLinux(os);
    }

    public static boolean isLinux(String os) {
        if ((os.toLowerCase().indexOf("nix") >= 0) || (os.indexOf("nux") >= 0) || (os.indexOf("aix") > 0)) {
            return true;
        }
        return false;
    }
    
    public static boolean isMac() {
        String os = System.getProperty("os.name");
        return isMac(os);
    }

    public static boolean isMac(String os) {
        if (os.toLowerCase().indexOf("mac") >= 0) {
            return true;
        }
        return false;
    }
    //</editor-fold>  
    
    //<editor-fold desc="File and Directory checks">
    public static boolean makeDirectory(String filepath){
        File file = new File(filepath);
        return file.mkdir();
    }
    
    public static boolean validFilepath(String filepath){
        File file = new File(filepath);
        return file.exists();
    }
    
    public static boolean isDirectory(String filepath){
        File file = new File(filepath);
        return file.isDirectory();
    }
    
    
    public static String getFilepath() {
        String _filepath = Utilities.class.getClassLoader().getResource(".").getPath();
        if (_filepath.contains("/target/")) {
            _filepath = _filepath.substring(0, _filepath.lastIndexOf("/target/"));
        } else if (_filepath.contains("/src/")) {
            _filepath = _filepath.substring(0, _filepath.lastIndexOf("/src/"));
        } else if (_filepath.contains("/build/classes/")) {
            _filepath = _filepath.substring(0, _filepath.lastIndexOf("/build/classes/"));
        }
        if (Utilities.isWindows()) {
            try {
                _filepath = URLDecoder.decode(_filepath, "utf-8");
                _filepath = new File(_filepath).getPath();
                if (_filepath.contains("\\target\\")) {
                    _filepath = _filepath.substring(0, _filepath.lastIndexOf("\\target\\"));
                } else if (_filepath.contains("\\src\\")) {
                    _filepath = _filepath.substring(0, _filepath.lastIndexOf("\\src\\"));
                } else if (_filepath.contains("\\build\\classes\\")) {
                    _filepath = _filepath.substring(0, _filepath.lastIndexOf("\\build\\classes\\"));
                }
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(Utilities.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            if (_filepath.contains("/target/")) {
                _filepath = _filepath.substring(0, _filepath.lastIndexOf("/target/"));
            } else if (_filepath.contains("/src/")) {
                _filepath = _filepath.substring(0, _filepath.lastIndexOf("/src/"));
            } else if (_filepath.contains("/build/classes/")) {
                _filepath = _filepath.substring(0, _filepath.lastIndexOf("/build/classes/"));
            }
        }
        return _filepath;
    }
    
    public static String getResourcesFilepath(){
        String filepath = getFilepath();
        if(Utilities.isWindows()){
            filepath += "\\src\\main\\resources\\";
        }
        else{
            filepath += "/src/main/resources/";
        }
        return filepath;
    }
    
    public static String getResourcesTempFilepath(){
        String filepath = getFilepath();
        if(Utilities.isWindows()){
            filepath += "\\src\\main\\resources\\temp\\";
        }
        else{
            filepath += "/src/main/resources/temp/";
        }
        return filepath;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="File content">
    
    public static String getFileContentAsString(String filepath){
        String filecontent = "";
        
        File file = new File(filepath);
        try { 
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line= "";
            while((line=reader.readLine()) != null){
                filecontent += (line+"\n");
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Utilities.class.getName()).log(Level.SEVERE, null, ex);
            Logger.getLogger(Utilities.class.getName()).log(Level.SEVERE, null, "File at " + filepath + " not found.");
        } catch (IOException ex) {
            Logger.getLogger(Utilities.class.getName()).log(Level.SEVERE, null, ex);
        }
        return filecontent;
    }
    
    public static List<String> getFileContentAsStringList(String filepath){
        List<String> filecontent = null;
        
        File file = new File(filepath);
        try { 
            BufferedReader reader = new BufferedReader(new FileReader(file));
            filecontent = new ArrayList<String>();
            String line= "";
            while((line=reader.readLine()) != null){
                filecontent.add(line);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Utilities.class.getName()).log(Level.SEVERE, null, ex);
            Logger.getLogger(Utilities.class.getName()).log(Level.SEVERE, null, "File at " + filepath + " not found.");
        } catch (IOException ex) {
            Logger.getLogger(Utilities.class.getName()).log(Level.SEVERE, null, ex);
        }
        return filecontent;
    }
    
    public static List<String[]> getCSVFileContentAsList(String filepath){
        List<String[]> listPieces = new ArrayList<String[]>();
        List<String> stringList = getFileContentAsStringList(filepath);
        for(String line:stringList){
            listPieces.add(line.split(","));
        }
        return listPieces;
    }
    
    public static Map<String,Map<String,Map<String,Map<String,List<Signal>>>>> binDependantWalk(String root){
        Map<String,Map<String,Map<String,Map<String,List<Signal>>>>> map = new HashMap<>();
        //List<Signal> signals = new ArrayList<Signal>();
        binDependantWalk(root,root,map);
        return map;
    }
    
    private static void binDependantWalk(String path, String resultsRoot, Map<String,Map<String,Map<String,Map<String,List<Signal>>>>> map){
        File root = new File( path );
        File[] list = root.listFiles();
        
        if (list == null) return ;
        
        String[] pathpieces = filepathPieces(path,resultsRoot);
        
        if(pathpieces[pathpieces.length-1].equals("input")  || pathpieces[pathpieces.length-1].equals("output")){
            
            int len = pathpieces.length;
            if(!map.containsKey(pathpieces[len-4].trim())){
                map.put(pathpieces[len-4].trim(), new HashMap());
            }
            if(!map.get(pathpieces[len-4]).containsKey(pathpieces[len-3].trim())){
                map.get(pathpieces[len-4]).put(pathpieces[len-3], new HashMap());
            }
            if(!map.get(pathpieces[len-4]).get(pathpieces[len-3]).containsKey(pathpieces[len-2])){
                map.get(pathpieces[len-4]).get(pathpieces[len-3]).put(pathpieces[len-2], new HashMap());
            }
            if(!map.get(pathpieces[len-4]).get(pathpieces[len-3]).get(pathpieces[len-2]).containsKey(pathpieces[len-1])){
                map.get(pathpieces[len-4]).get(pathpieces[len-3]).get(pathpieces[len-2]).put(pathpieces[len-1], new ArrayList<Signal>());
            }
            for(File f: list){
                //System.out.println(f.getAbsolutePath());
                map.get(pathpieces[len-4]).get(pathpieces[len-3]).get(pathpieces[len-2]).get(pathpieces[len-1]).add(readBinDependantFile(f.getAbsolutePath()));
            }
        }
        else {
            for(File f: list){
                binDependantWalk(f.getAbsolutePath(),resultsRoot,map);
            }
        }
        
    }
    
    private static String[] filepathPieces(String filepath, String rootFilepath){
        String relativeFilepath = filepath.substring(filepath.lastIndexOf(rootFilepath) + rootFilepath.length());
        return relativeFilepath.split("/");
    }
    
    public static Signal readBinDependantFile(String filepath){
        List<String[]> listPieces = new ArrayList<String[]>();
        listPieces = getCSVFileContentAsList(filepath);
        List<Point> points = new ArrayList<Point>();
        for(String[] pieces : listPieces){
            points.add(new Point(Double.valueOf(pieces[0].trim()), "t", Double.valueOf(pieces[1].trim()), "x"));
        }
        return new Signal(points);
    }
    
    public static void writeToFile(String filepath, List<String> content){
        String allcontent = "";
        for(String line:content){
            allcontent += line + "\n";
        }
        writeToFile(filepath,allcontent);
    }
    
    public static void writeToFile(String filepath, String content){
        File file = new File(filepath);
        try {
            FileWriter fr = new FileWriter(file);
            BufferedWriter br = new BufferedWriter(fr);
            br.write(content);
            br.flush();
            br.close();
        } catch (IOException ex) {
            Logger.getLogger(Utilities.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static List<Signal> getWebappSignals(JSONArray arr){
        List<Signal> signals = new ArrayList<Signal>();
        
        for(int i=0;i<arr.length();i++){
            List<Point> points = new ArrayList<Point>();
            try {
                JSONArray pointsArr = (JSONArray)arr.get(i);
                for(int j=0;j<pointsArr.length();j++){
                    JSONObject point = (JSONObject)pointsArr.get(j);
                    double time = Double.valueOf(point.get("x").toString());
                    double signal = Double.valueOf(point.get("y").toString());
                    points.add(new Point(time,"t",signal,"x"));
                }
            } catch (JSONException ex) {
                Logger.getLogger(Utilities.class.getName()).log(Level.SEVERE, null, ex);
            }
            signals.add(new Signal(points));
        }
        
        return signals;
    }
    
    public static List<Signal> getiBioSimSignals(String filepath){ //
        List<Signal> signals = new ArrayList<Signal>();
        List<String[]> csvsignals = getCSVFileContentAsList(filepath);
        List<List<Point>> pointlist = new ArrayList<List<Point>>();
        for(int i=0;i<csvsignals.get(1).length-1;i++){
            pointlist.add(new ArrayList<Point>());
        }
        
        for (int i = 1; i < csvsignals.size(); i++) {
            for (int j = 1; j < csvsignals.get(i).length; j++) {
                double x = Double.valueOf(csvsignals.get(i)[0]);
                double y = Double.valueOf(csvsignals.get(i)[j]);
                pointlist.get(j-1).add(new Point(x, "t", y, "x"));
            }
        }

        for(int i=0;i<pointlist.size();i++){
            signals.add(new Signal(pointlist.get(i)));
        }
        
        return signals;
    }
    
    public static List<Signal> getColumnSignals(String filepath, boolean columnHeader){ //
        List<Signal> signals = new ArrayList<Signal>();
        List<String[]> csvsignals = getCSVFileContentAsList(filepath);
        List<List<Point>> pointlist = new ArrayList<List<Point>>();
        
        if (!columnHeader) {
            for (int i = 0; i < csvsignals.get(1).length; i++) {
                pointlist.add(new ArrayList<Point>());
            }
            for (int i = 0; i < csvsignals.size(); i++) {
                //double x = Double.valueOf(csvsignals.get(i)[0]);
                for (int j = 0; j < csvsignals.get(i).length; j++) {
                    double y = Double.valueOf(csvsignals.get(i)[j]);
                    pointlist.get(j).add(new Point(i, "t", y, "x"));
                }
            }
        } else {
            for (int i = 0; i < csvsignals.get(1).length-1; i++) {
                pointlist.add(new ArrayList<Point>());
            }
            for (int i = 0; i < csvsignals.size(); i++) {
                for (int j = 1; j < csvsignals.get(i).length; j++) {
                    double x = Double.valueOf(csvsignals.get(0)[j]);
                    double y = Double.valueOf(csvsignals.get(i)[j]);
                    pointlist.get(j-1).add(new Point(x, "t", y, "x"));
                }
            }
        }
        
        for(int i=0;i<pointlist.size();i++){
            signals.add(new Signal(pointlist.get(i)));
        }
        
        return signals;
    }
    
    public static List<Signal> getRowSignals(String filepath, boolean header){
        List<Signal> signals = new ArrayList<Signal>();
        List<String[]> csvStrings = getCSVFileContentAsList(filepath);
        if (!header) {
            for (String[] csvString : csvStrings) {
                int count = 0;
                List<Point> points = new ArrayList<Point>();
                for (int i = 0; i < csvString.length; i++) {
                    if(csvString[i].trim().isEmpty()){
                        continue;
                    }
                    double yVal = Double.valueOf(csvString[i]);
                    points.add(new Point(count, "t", yVal, "x"));
                    count++;
                }
                signals.add(new Signal(points));
            }
        } else {
            String[] headerLine = csvStrings.get(0);
            for(int i=1;i<csvStrings.size();i++){
                String csvString[] = csvStrings.get(i);
                List<Point> points = new ArrayList<Point>();
                for (int j = 0; j < csvString.length; j++) {
                    if(csvString[j].trim().isEmpty()){
                        continue;
                    }
                    double xVal = Double.valueOf(headerLine[j]);
                    double yVal = Double.valueOf(csvString[j]);
                    points.add(new Point(xVal, "t", yVal, "x"));
                }
                signals.add(new Signal(points));
            }
        }
        
        return signals;
    }
    
    public static char getSeparater(){
        if(Utilities.isWindows()){
            return '\\';
        }
        return '/';
    }
    
    //</editor-fold>
    
}
