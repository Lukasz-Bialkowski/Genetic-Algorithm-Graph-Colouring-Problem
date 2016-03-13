package bialkowski.lukasz.services;


import bialkowski.lukasz.globals.StaticVariables;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileReaderService {

    private static final String FILE_NAME = StaticVariables.GRAPH_FILE;

    public List<int[]> readAndParse() {
        List<String[]> strings = readFiles();
        return parseMatrix(strings);
    }

    private List<int[]> parseMatrix(List<String[]> strings) {
        List<int[]> linesParsed = new ArrayList<>();
        int[] ints;
        for (String[] line : strings) {
            ints = new int[line.length - 1];
            ints[0] = Integer.parseInt(line[1].trim());
            ints[1] = Integer.parseInt(line[2].trim());
            ints[2] = Integer.parseInt(line[3].trim());
            linesParsed.add(ints);
        }
        return linesParsed;
    }

    public List<String[]> readFiles() {

        List<String[]> dataTable = new ArrayList<String[]>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new java.io.FileReader(FILE_NAME));
            String line = "";
            String parsedLine[] = null;
            while ((line = br.readLine()) != null) {
                if (line.charAt(0) == 'e') {
                    parsedLine = line.split("\\s+");
                    dataTable.add(parsedLine);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println(e);
        } catch (IOException e) {
            System.out.println(e);
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return dataTable;

    }
}
