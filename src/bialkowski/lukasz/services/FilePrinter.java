package bialkowski.lukasz.services;

import bialkowski.lukasz.globals.StaticVariables;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FilePrinter {

    PrintWriter writer;


    public FilePrinter() {
        try {
            writer = new PrintWriter(new FileWriter(StaticVariables.CHARTS_FILE,false));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void savePopulationScores (double avg, double max, double min){
        writer.println(avg+","+max+","+min);
    }

    public void closeStream(){
        writer.close();
    };
}
