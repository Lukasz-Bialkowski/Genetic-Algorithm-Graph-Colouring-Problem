package bialkowski.lukasz;

import bialkowski.lukasz.algorithms.GeneticAlgorithm;
import bialkowski.lukasz.algorithms.IAlgorithm;

public class Main {

    public static void main(String args[]) {
        IAlgorithm algorithm;

        // ########################## Test podstawowego algorytmu ##################################################

//        algorithm = new BruteForce();
//        int iterationsBF = algorithm.algorithm();
//
//        algorithm = new HillClimbing();
//        int iterationsHC = algorithm.algorithm();
//
//        GeneticAlgorithm gc = new GeneticAlgorithm();
//        int iterationsBGCP = gc.accumulateAlgorithm();

//        System.out.println("Brute force: " + iterationsBF);
//        System.out.println("Hill climbing: " + iterationsHC);
//        System.out.println("Genetic algorithm: " + iterationsBGCP + "\t pokolen");


        // ########################## Test modyfikacji ##################################################

        GeneticAlgorithm gc = new GeneticAlgorithm();
        gc.accumulateAlgorithm();

        // ########################## Test czasu trwania ##################################################

//        algorithm = new BruteForce();
//        double t1 = getDurration(algorithm);
//
//        algorithm = new HillClimbing();
//        double t2 = getDurration(algorithm);
//
//        algorithm = new GeneticAlgorithm();
//        double t3 = getDurration(algorithm);
//
//        System.out.println("Brute force: " + t1);
//        System.out.println("Hill climbing: " + t2);
//        System.out.println("Genetic algorithm: " + t3);
    }

    public static double getDurration(IAlgorithm algorithm) {
        long tStart = System.currentTimeMillis();
        algorithm.algorithm();
        long tEnd = System.currentTimeMillis();
        long tDelta = tEnd - tStart;
        return tDelta / 1000.0;
    }

}
