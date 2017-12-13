/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.gridtli.webapp;

import hyness.stl.TreeNode;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.cidarlab.gridtli.dom.Grid;
import org.cidarlab.gridtli.dom.Signal;
import org.cidarlab.gridtli.dom.TLIException;
import org.cidarlab.gridtli.tli.TemporalLogicInference;
import org.cidarlab.gridtli.tli.Utilities;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.ErrorMvcAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author prash
 */

@EnableAutoConfiguration(exclude=ErrorMvcAutoConfiguration.class)
@Controller
public class MainController {
    
    
    @RequestMapping(value="/getSTL", method=RequestMethod.POST)
    public void getSTL(HttpServletRequest request, HttpServletResponse response) throws TLIException{
        String val = "";
        TreeNode stl = null;
        
        try {
            JSONArray arr = new JSONArray(request.getParameter("signals"));
            for(int i=0;i<arr.length();i++){
                System.out.println(arr.get(i));
            }
            List<Signal> signals = Utilities.getWebappSignals(arr);
            double xt = Double.valueOf(request.getParameter("xt"));
            double tt = Double.valueOf(request.getParameter("tt"));
            double ct = Double.valueOf(request.getParameter("ct"));
            Grid grid = new Grid(signals, tt, xt);
            stl = TemporalLogicInference.getSTL(grid, ct);
            
        } catch (JSONException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
                
        try {
            PrintWriter writer = response.getWriter();
            writer.print(stl);
            writer.flush();
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
