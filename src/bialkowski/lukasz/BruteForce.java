package bialkowski.lukasz;

import bialkowski.lukasz.globals.StaticVariables;
import bialkowski.lukasz.services.FileReaderService;

import java.util.HashSet;
import java.util.List;

public class BruteForce {

    private Graph graph;

    public BruteForce() {
//        Serwis do czytania z pliku
        FileReaderService fileReaderService = new FileReaderService();
//        Odczytaj plik i zparsuj na inty
        List<int[]> dataMatrix = fileReaderService.readAndParse();
//        Stworz graf z wczytanych danych
        graph = new Graph(dataMatrix);

        bruteForce();
    }

    public int bruteForce(){
        int vertices = this.graph.getMyNumVertices();
        int colors = StaticVariables.COLOURS_COUNT;
        int possibilities = (int) Math.pow(colors, vertices);
        int[] chromosome = new int[vertices];
        int temp;

        for (int i = 0; i < possibilities; i++) {

            int x = i;
            for (int j = vertices-1; j >= 0; j--) {
                temp = x % colors;
                x = x - temp;
                x /= colors;
                chromosome[j] = temp;
            }

            if(qualityFunction(chromosome) == 0){
                System.out.println("Ilosc iteracji:" + i);
                return i;
            }

            printArray(chromosome);
        }
        return 0;
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
