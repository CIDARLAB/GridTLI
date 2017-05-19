/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.gridtli.Visualize.Webapp;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author prash
 */

@Controller
public class MainController {
    
    
    @RequestMapping(value="/getSTL", method=RequestMethod.POST)
    public void getSTL(HttpServletRequest request, HttpServletResponse response){
        String val = "";
        System.out.println("I called you!");
        
        try {
            JSONArray arr = new JSONArray(request.getParameter("signals"));
            for(int i=0;i<arr.length();i++){
                System.out.println(arr.get(i));
            }
        } catch (JSONException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
                
                
        try {
            PrintWriter writer = response.getWriter();
            writer.print("Yayy");
            writer.flush();
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
