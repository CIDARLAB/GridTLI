/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.gridtli.adaptors.cli;

/**
 *
 * @author prash
 */
public class Main {
    public static void main(String[] args) {
        if(args.length == 1){
            if(args[0].equals("-help")){
                
            } else {
                System.out.println("Invalid option. Try \"-help\" for options and usage");
            }
        } else {
            
        }
    }
}
