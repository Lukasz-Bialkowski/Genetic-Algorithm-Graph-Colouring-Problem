package bialkowski.lukasz;

import bialkowski.lukasz.globals.StaticVariables;
import bialkowski.lukasz.services.FileReaderService;

import java.util.HashSet;
import java.util.List;

public class HillClimbing implements IAlgorithm{

    private Graph graph;

    public HillClimbing() {
//        Serwis do czytania z pliku
        FileReaderService fileReaderService = new FileReaderService();
//        Odczytaj plik i zparsuj na inty
        List<int[]> dataMatrix = fileReaderService.readAndParse();
//        Stworz graf z wczytanych danych
        graph = new Graph(dataMatrix);
    }

    @Override
    public int algorithm() {
        int failHits = 0;
        int iterationCount = 0;
        double newScore = 0;
        int[] sollution = new int[this.graph.getMyNumVertices()];
        int[] newSollution = null;

        for (int j = 0; j < sollution.length; j++) {
            sollution[j] = (int) (Math.random() * StaticVariables.COLOURS_COUNT);
        }

        double oldScore=-1;

        while (failHits < 100000 && oldScore!=0){
            iterationCount ++ ;
            newSollution = new int[sollution.length];
            System.arraycopy(sollution, 0, newSollution, 0, sollution.length);
            int index = (int) (Math.random() * newSollution.length);
            newSollution[index] = (newSollution[index] + 1) % StaticVariables.COLOURS_COUNT;
            newScore = qualityFunction(newSollution);
            oldScore = qualityFunction(sollution);
            if (newScore <= oldScore) {
                oldScore = newScore;
                newScore = -1;
                sollution = newSollution;
                failHits ++;
            }
            System.out.println(oldScore);
        }

        System.out.println(iterationCount);
        System.out.println("Wynik: " + qualityFunction(sollution));
        printArray(sollution);
        return  iterationCount;
    }

    private void printArray(int[] arr) {

        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }

    public double qualityFunction(int[] chromosome){
        int collisionCount = 0;
        double weightDiff = 0;
        int minColor = -1;
        int maxColor = -1;

        if (chromosome.length > 0) {
            minColor = chromosome[0];
            maxColor = chromosome[0];
        }

        for (int i = 0; i < chromosome.length; i++) {
            int currentColor = chromosome[i];
            if (currentColor > maxColor) {
                maxColor = currentColor;
            }
            if (currentColor < minColor) {
                minColor = currentColor;
            }
            HashSet<Edge> edges = graph.getMyAdjList().get(i + 1);

            for (Edge edge : edges) {
                int siblingColor = chromosome[edge.getDestination()-1];
                int weight = edge.getWeight();

                if (isInvalid(weight, currentColor, siblingColor)) {
                    weightDiff += Math.abs(weight - Math.abs(currentColor-siblingColor));
                    collisionCount++;
                }
            }
        }
//        int colorNumber = (maxColor-minColor+1);
//        double finalScore = (collisionCount+1)*colorNumber;
        return weightDiff;
    }

    private boolean isInvalid(int weight, int v1, int v2) {
        return weight>Math.abs(v1-v2);
    }

}
