package bialkowski.lukasz;

import bialkowski.lukasz.globals.StaticVariables;
import bialkowski.lukasz.model.MaxMinAvgDTO;
import bialkowski.lukasz.services.FilePrinter;
import bialkowski.lukasz.services.FileReaderService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class GeneticAlgorithm implements IAlgorithm{

    private Graph graph;
    private boolean solutionFound = false;
    private int[] pocketChromosome = null;
    private double bestScoreSoFar=-1;
    private int colors = -1;
    private FilePrinter printer = new FilePrinter();

    public GeneticAlgorithm() {
        FileReaderService fileReaderService = new FileReaderService();
        List<int[]> dataMatrix = fileReaderService.readAndParse();
        graph = new Graph(dataMatrix);
    }

    @Override
    public int algorithm() {
        int generationsCounter = 1;
        List<int[]> population = initializePopulation(graph.getMyNumVertices(), StaticVariables.COLOURS_COUNT, StaticVariables.POPULATION_SIZE);
        printPopulation(population);
        holePopulationQuality(population);

        while (!solutionFound && generationsCounter < StaticVariables.GENERATIONS_NUMBER) {

            crossover(population);
            System.out.println("Po crossover");
            printPopulation(population);

            mutatePopulation(population);
            System.out.println("Po mutacji");
            printPopulation(population);

            population = selection(population);
            System.out.println("Po selekcji");
            printPopulation(population);

            holePopulationQuality(population);
            generationsCounter++;
        }

//        printSummary();
        printer.closeStream();

        return generationsCounter;
    }

    private void printSummary() {
        System.out.print("Najlepszy chromosom: \t");
        printArray(pocketChromosome);
        System.out.println("Best score: \t" + bestScoreSoFar);
        System.out.println("Liczba kolorow: \t" + colors);
    }

    private void testujAlgorytm() {
        /*TESTOWANIE MUTACJI
        List<int[]> trysome = new ArrayList<>();
        trysome.add(new int[]{1,2,3,4,5,6,7});
        trysome.add(new int[]{1,2,3,4,5,6,7});
        trysome.add(new int[]{1,2,3,4,5,6,7});
        trysome.add(new int[]{1,2,3,4,5,6,7});
        trysome.add(new int[]{1,2,3,4,5,6,7});
        trysome.add(new int[]{1,2,3,4,5,6,7});
        System.out.println("Przed mutacja");
        this.printPopulation(trysome);
        this.mutatePopulation(trysome);
        System.out.println("Po mutacji");
        this.printPopulation(trysome);*/


        /*TESTOWANIE SELEKCJI
        List<int[]> selection1 = selection(population);
        holePopulationQuality(selection1);
        List<int[]> selection2 = selection(selection1);
        holePopulationQuality(selection2);
        List<int[]> selection3 = selection(selection2);
        holePopulationQuality(selection3);*/

        /* TESTOWANIE KRZYZOWANIA
        List<int[]> trysome = new ArrayList<>();
        trysome.add(new int[]{1,1,1,1,1,1,1});
        trysome.add(new int[]{2,2,2,2,2,2,2});
        trysome.add(new int[]{3,3,3,3,3,3,3});
        trysome.add(new int[]{4,4,4,4,4,4,4});
        trysome.add(new int[]{5,5,5,5,5,5,5});
        trysome.add(new int[]{6,6,6,6,6,6,6});
        System.out.println("Przed crossover");
        for (int i = 0; i < trysome.size(); i++) {
            int[] ints = trysome.get(i);
            System.out.print("Chromosome "+i+": ");
            for (int j = 0; j < ints.length; j++) {
                System.out.print(ints[j]+ " ");
            }
            System.out.println();
        }
        crossover(trysome);
        System.out.println("Po crossover");
        for (int i = 0; i < trysome.size(); i++) {
            int[] ints = trysome.get(i);
            System.out.print("Chromosome "+i+": ");
            for (int j = 0; j < ints.length; j++) {
                System.out.print(ints[j]+ " ");
            }
            System.out.println();
        }*/
    }

    private List<int[]> selection(List<int[]> currentPopulation) {
        List<int[]> newPopulation = new ArrayList<>();
        int newPopulationSize = 0;
        int currentPopulationSize = currentPopulation.size();
        HashSet<Integer> competitors;
        List<int[]> competitorsChromosomes;

        while (newPopulationSize < StaticVariables.POPULATION_SIZE) {
            competitors = new HashSet<>();
            competitorsChromosomes = new ArrayList<>();
            for (int i = 0; i < StaticVariables.SELECTION_COMPETITION_SIZE; i++) {

                while (!competitors.add((int) (Math.random() * currentPopulationSize))) {
                }
            }

            for (Integer i : competitors) {
                competitorsChromosomes.add(currentPopulation.get(i));
            }

            int[] competitionWinner = getCompetitionWinner(competitorsChromosomes);
            int[] array = new int[competitionWinner.length];
            System.arraycopy( competitionWinner, 0, array, 0, competitionWinner.length );

            newPopulation.add(array);
            newPopulationSize++;
        }
        return newPopulation;
    }

    private int[] getCompetitionWinner(List<int[]> competitorsChromosomes) {
        int bestIndex = -1;
        double bestQuality = -1;

        if(competitorsChromosomes.size()>0){
            bestQuality = qualityFunction(competitorsChromosomes.get(0));
            bestIndex = 0;
        }

        for (int i = 1; i<competitorsChromosomes.size(); i++) {
            double quality = qualityFunction(competitorsChromosomes.get(i));
            if (quality < bestQuality) {
                bestQuality = quality;
                bestIndex = i;
            }
        }

        return competitorsChromosomes.get(bestIndex);
    }

    private void mutatePopulation(List<int[]> population) {
        int geneToMutate = -1;
        int temp = -1;
        for (int i = 0; i < population.size(); i++) {
            int[] chromosome = population.get(i);

            for (int j = 0; j < chromosome.length; j++) {
                if (Math.random() < StaticVariables.MUTATION_POSSIBILITY) {
                    if(geneToMutate!=-1){
                        temp = chromosome[j];
                        chromosome[j] = chromosome[geneToMutate];
                        chromosome[geneToMutate] = temp;
                        temp = -1;
                        geneToMutate = -1;
                    } else{
                        geneToMutate = j;
                    }
                }

            }
        }
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

    private void crossover(List<int[]> population){
        boolean isOdd = population.size()%2==0;
        double probabilityArray[] = isOdd?new double[population.size()/2]:new double[(population.size()-1)/2];
        probabilityArray = generateProbabilityArray(probabilityArray);
        int lastChromosomeIndex = population.size()-1;
        List<int[]> newChromosomes = new ArrayList<>();
        List<int[]> returnedChromosomes;

        for (int i = 0; i < probabilityArray.length; i++) {
            if(probabilityArray[i] <= StaticVariables.CROSSOVER_POSSIBILITY ){
                returnedChromosomes = cossoverTwoChromosomes(population.get(i), population.get(population.size() - 1 - i));
                for (int j = 0; j < returnedChromosomes.size(); j++) {
                    newChromosomes.add(returnedChromosomes.get(j));
                }
            }
        }
        population.addAll(newChromosomes);
    }

    private List<int[]> cossoverTwoChromosomes(int[] firstChromosome, int[] secondChromosome) {
        int maxCrossingIndex = (int) (Math.random()*firstChromosome.length);
        int tempValue;
        int chromosomeLength = firstChromosome.length;

        /**
         * */
        printArray(firstChromosome);
        System.out.print(  " mutuje z ");
        printArray(secondChromosome);
        /**
         * */

        int[] newChromosome1 = new int[chromosomeLength];
        System.arraycopy( firstChromosome, 0, newChromosome1, 0, firstChromosome.length );
        int[] newChromosome2 = new int[chromosomeLength];
        System.arraycopy( secondChromosome, 0, newChromosome2, 0, secondChromosome.length );

        for (int i = 0; i < maxCrossingIndex; i++) {
            tempValue = newChromosome1[i];
            newChromosome1[i] = newChromosome2[i];
            newChromosome2[i] = tempValue;
        }
        List<int[]> newChromosomes = new ArrayList<>();
        newChromosomes.add(newChromosome1);
        newChromosomes.add(newChromosome2);

        return newChromosomes;
    }

    private void printArray(int[] arr) {

        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }

    private double[] generateProbabilityArray(double[] arr){
        for (int i = 0; i < arr.length; i++) {
            arr[i]=Math.random();
        }
        return arr;
    }

    public double qualityFunction(int[] chromosome){

        double result = 0;
        double weightDiff = 0;
        int collisionCount = 0;
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
        this.colors = (maxColor - minColor + 1);

//        double finalScore = (collisionCount+1)*colors;
        double finalScore = colors*colors*((int)(weightDiff));
        if(weightDiff == 0){
            this.bestScoreSoFar = weightDiff;
            this.solutionFound = true;
            this.pocketChromosome = chromosome;
        }
//        return weightDiff;
        return finalScore;
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
            System.out.println();
        }
    }

    private void holePopulationQuality(List<int[]> population){

        double quality = 0;
        double bestPopulationScore = -1;
        double worstPopulationScore = -1;

        int[] bestChromosomeInPopulation = null;
        int[] worstChromosomeInPopulation = null;

        if (population.size() > 0) {
            double initial = qualityFunction(population.get(0));
            bestPopulationScore = initial;
            worstPopulationScore = initial;
            bestChromosomeInPopulation = population.get(0);
            worstChromosomeInPopulation = population.get(0);
        }

        for (int i = 0; i < population.size(); i++) {
            int[] chromosome = population.get(i);
            double result = qualityFunction(chromosome);

            if (result < bestPopulationScore) {
                bestPopulationScore = result;
                bestChromosomeInPopulation = chromosome;
            }
            if (result > worstPopulationScore) {
                worstPopulationScore = result;
                worstChromosomeInPopulation = chromosome;
            }
            quality += result;
        }

        if (this.bestScoreSoFar == -1) {
            this.bestScoreSoFar = worstPopulationScore;
        }

        if(bestPopulationScore < this.bestScoreSoFar){
            this.bestScoreSoFar = bestPopulationScore;
            this.pocketChromosome = bestChromosomeInPopulation;
        }

        System.out.println("Wartosc sumaryczna bledow w calej populacji: " + quality);
        System.out.println("Najlepsza wartosc funkcji: " + bestPopulationScore);
        System.out.print("Wartosci chromosomu najlepszego: ");
        printArray(bestChromosomeInPopulation);

        System.out.println("Najgorszy wartosc funkcji: " + worstPopulationScore);
        System.out.print("Wartosci chromosomu najgorszego: ");
        printArray(worstChromosomeInPopulation);

        double averagePopulationScore = quality / StaticVariables.POPULATION_SIZE;
        System.out.println("Srednia wartosc bledu w populacji: "+ averagePopulationScore);

        printer.savePopulationScores(averagePopulationScore, bestPopulationScore, worstPopulationScore);
    }

    private MaxMinAvgDTO accumulatePopulationScores(List<int[]> population){

        double quality = 0;
        double bestPopulationScore = -1;
        double worstPopulationScore = -1;

        int[] bestChromosomeInPopulation = null;
        int[] worstChromosomeInPopulation = null;

        if (population.size() > 0) {
            double initial = qualityFunction(population.get(0));
            bestPopulationScore = initial;
            worstPopulationScore = initial;
            bestChromosomeInPopulation = population.get(0);
            worstChromosomeInPopulation = population.get(0);
        }

        for (int i = 0; i < population.size(); i++) {
            int[] chromosome = population.get(i);
            double result = qualityFunction(chromosome);

            if (result < bestPopulationScore) {
                bestPopulationScore = result;
                bestChromosomeInPopulation = chromosome;
            }
            if (result > worstPopulationScore) {
                worstPopulationScore = result;
                worstChromosomeInPopulation = chromosome;
            }
            quality += result;
        }

        if (this.bestScoreSoFar == -1) {
            this.bestScoreSoFar = worstPopulationScore;
        }

        if(bestPopulationScore < this.bestScoreSoFar){
            this.bestScoreSoFar = bestPopulationScore;
            this.pocketChromosome = bestChromosomeInPopulation;
        }

        double averagePopulationScore = quality / StaticVariables.POPULATION_SIZE;
        return new MaxMinAvgDTO(worstPopulationScore, bestPopulationScore, averagePopulationScore);
    }

    public void accumulateAlgorithm() {
        int maxLength = 0;
        ArrayList<ArrayList<MaxMinAvgDTO>> chartsList = new ArrayList<>();
        ArrayList<MaxMinAvgDTO> oneCycle;

        for (int i = 0; i < 10; i++) {
            oneCycle = new ArrayList<>();
            int generationsCounter = 1;
            List<int[]> population = initializePopulation(graph.getMyNumVertices(), StaticVariables.COLOURS_COUNT, StaticVariables.POPULATION_SIZE);
            printPopulation(population);
            oneCycle.add(accumulatePopulationScores(population));

            while (!solutionFound && generationsCounter < StaticVariables.GENERATIONS_NUMBER) {

                crossover(population);
                System.out.println("Po crossover");
                printPopulation(population);

                mutatePopulation(population);
                System.out.println("Po mutacji");
                printPopulation(population);

                population = selection(population);
                System.out.println("Po selekcji");
                printPopulation(population);

                oneCycle.add(accumulatePopulationScores(population));
                generationsCounter++;
            }
            if (maxLength < generationsCounter) {
                maxLength = generationsCounter;
            }
            chartsList.add(oneCycle);
        }

        int i =0;
        double sumaMax=0;
        double sumaMin=0;
        double sumaAvg=0;
        int specificGenerationCounter=0;

        while (i<maxLength) {
            for (ArrayList<MaxMinAvgDTO> chart : chartsList) {
                if (i < chart.size()) {
                    MaxMinAvgDTO dto = chart.get(i);
                    specificGenerationCounter++;
                    sumaMax += dto.getMax();
                    sumaMin += dto.getMin();
                    sumaAvg += dto.getAvg();
                }
            }

            printer.savePopulationScores(sumaAvg/specificGenerationCounter,sumaMax/specificGenerationCounter,sumaMin/specificGenerationCounter);
            sumaMax = sumaMin = sumaAvg = 0;
            i++;
        }

        System.out.println(chartsList.size());
        System.out.println(maxLength);
        //printSummary();
        printer.closeStream();

        //return generationsCounter;
    }

}
