/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.gridtli.adaptors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.cidarlab.gridtli.dom.Signal;

/**
 *
 * @author prash
 */
public class BioCPSAdaptor {
        
    public static Map<String, Map<String,List<Signal>>> binDependantSignals(Map<String,Map<String,Map<String,Map<String,List<Signal>>>>> map, boolean collapsedModule){
        
        Map<String, Map<String,List<Signal>>> moduleSignals = new HashMap();
        if(collapsedModule){ 
           for (String module : map.keySet()) {
                //System.out.println("Module name :: " + module);
                for (String sm : map.get(module).keySet()) {
                    for(String plasmid: map.get(module).get(sm).keySet()){
                        //System.out.println(sm + "::" + plasmid);
                        if(!moduleSignals.containsKey(module)){
                            moduleSignals.put(module, new HashMap());
                        }
                        if(!moduleSignals.get(module).containsKey("input")){
                            moduleSignals.get(module).put("input", new ArrayList<Signal>());
                        }
                        if(!moduleSignals.get(module).containsKey("output")){
                            moduleSignals.get(module).put("output", new ArrayList<Signal>());
                        }
                        moduleSignals.get(module).get("input").addAll( map.get(module).get(sm).get(plasmid).get("input") );
                        moduleSignals.get(module).get("output").addAll( map.get(module).get(sm).get(plasmid).get("output") );
                    }
                }
            }
        } else {
            for (String module : map.keySet()) {
                //System.out.println("Module name :: " + module);
                for (String sm : map.get(module).keySet()) {
                    String mName = module + ":" + sm;
                    //System.out.println(mName);
                    if(!moduleSignals.containsKey(mName)) {
                        moduleSignals.put(mName, new HashMap());
                    }
                    for(String plasmid: map.get(module).get(sm).keySet()){
                        
                        if(!moduleSignals.get(mName).containsKey("input")){
                            moduleSignals.get(mName).put("input", new ArrayList<Signal>());
                        }
                        if(!moduleSignals.get(mName).containsKey("output")){
                            moduleSignals.get(mName).put("output", new ArrayList<Signal>());
                        }
                        moduleSignals.get(mName).get("input").addAll( map.get(module).get(sm).get(plasmid).get("input") );
                        moduleSignals.get(mName).get("output").addAll( map.get(module).get(sm).get(plasmid).get("output") );
                    }
                }
            }
        }
        return moduleSignals;
        
    }

    

}
