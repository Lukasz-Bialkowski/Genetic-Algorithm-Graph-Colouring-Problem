package bialkowski.lukasz;

import bialkowski.lukasz.globals.StaticVariables;
import bialkowski.lukasz.services.FileReaderService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Main {

    private Graph graph;
    private int[] bestChromosome = null;
    private double bestQualityScore = -1;
    private List<int[]> population = new ArrayList<>();

    public static void main(String args[]) {
        Main graphColoringProblem = new Main();
    }

    public Main() {

//        Serwis do czytania z pliku
        FileReaderService fileReaderService = new FileReaderService();

//        Odczytaj plik i zparsuj na inty
        List<int[]> dataMatrix = fileReaderService.readAndParse();

//        Stworz graf z wczytanych danych
        graph = new Graph(dataMatrix);
        graph.toString();

        population = initializePopulation(graph.getMyNumVertices(), StaticVariables.COLOURS_COUNT, StaticVariables.POPULATION_SIZE);
        printPopulation(population);
        holePopulationQuality(population);
    }

    private List<int[]> initializePopulation(int myNumVertices, int maxColor, int populationCount) {

        List<int[]> population = new ArrayList<int[]>();
        int[] chromosome;

//        Stworz tyle chromosomow ile wynosi licznosc populacji
        for (int i = 0; i < populationCount; i++) {
            chromosome = new int[myNumVertices];

//            Stworz tyle genow z kolorami w chromosomie ile wynosi liczba wierzcholkow
            for (int j = 0; j < myNumVertices; j++) {
                chromosome[j] = (int) (Math.random() * maxColor);
            }

//            Dodaj osobnika do populacji
            population.add(chromosome);
        }

        return population;
    }

    private double qualityFunction(int[] chromosome){
        double result = 0;
        int collisionCount = 0;

        for (int i = 0; i < chromosome.length; i++) {
            int currentColor = chromosome[i];
            HashSet<Edge> edges = graph.getMyAdjList().get(i + 1);

            for (Edge edge : edges) {
                int siblingColor = chromosome[edge.getDestination()-1];
                int weight = edge.getWeight();

                if(isInvalid(weight, currentColor, siblingColor))
                    collisionCount++;
            }
        }

        return collisionCount;
    }

    private boolean isInvalid(int weight, int v1, int v2) {
        return weight>Math.abs(v1-v2);
    }

    private void printPopulation(List<int[]> population) {
        for (int i = 0; i < population.size(); i++) {
            int[] ints = population.get(i);

            System.out.print("Osobnik ["+i+"] : ");
            for (int j = 0; j <ints.length; j++) {
                System.out.print(ints[j] + " ");
            }

            System.out.println(" = > "+ qualityFunction(ints));
        }
    }

    private void holePopulationQuality(List<int[]> population){
        double quality = 0;
        for (int[] chromosome : population) {
            double result = qualityFunction(chromosome);

            if(this.bestQualityScore == -1){
                this.bestQualityScore = result;
            }

            if(result < this.bestQualityScore){
                this.bestQualityScore = result;
                this.bestChromosome = chromosome;
            }
            quality+=result;
        }
        System.out.println("Wartosc sumaryczna bledow w calej populacji: " + quality);
        System.out.println("Najlepsza wartosc funkcji: " + this.bestQualityScore);
        for (int i : this.bestChromosome) {
            System.out.print(i+" ");
        }
        System.out.println();
    }

    public int[] getBestChromosome() {
        return bestChromosome;
    }

    public void setBestChromosome(int[] bestChromosome) {
        this.bestChromosome = bestChromosome;
    }

    public double getBestQualityScore() {
        return bestQualityScore;
    }

    public void setBestQualityScore(double bestQualityScore) {
        this.bestQualityScore = bestQualityScore;
    }
}
