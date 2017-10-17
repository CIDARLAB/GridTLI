/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.gridtli.cli;

/**
 *
 * @author prash
 */
public class Main {
    public static void main(String[] args) {
        String tab = "\t";
        if(args.length == 1){
            if(args[0].equals("--help")){
                System.out.println("General Usage: ");
                System.out.println("java -jar <GridTLI-jar-name>.jar [--modes] [-parameters] [-options]");
                System.out.println("");
                System.out.println("Modes in GridTLI include:");
                System.out.println(tab + "--run                  Run Grid TLI.");
                System.out.println("");
                System.out.println("Parameters include:");
                System.out.println(tab + "-xt                    Signal threshold. This must be a double value.");
                System.out.println(tab + "-tt                    Time threshold. This must be a double value.");
                System.out.println(tab + "-ct                    Cluster threshold. This must be a double value.");
                System.out.println(tab + "-ct                    Cluster threshold. This must be a double value.");
                System.out.println("");
                System.out.println("Options include:");
                System.out.println(tab + "-outputDir             Output Directory. This must be a valid filepath.");
                
            } else {
                System.out.println("Invalid option. Try 'java -jar <GridTLI-jar-name>.jar --help' for options and usage");
            }
        } else {
            
        }
    }
}
