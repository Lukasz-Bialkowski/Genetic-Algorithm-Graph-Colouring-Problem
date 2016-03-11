package bialkowski.lukasz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Graph {

    private HashMap<Integer, HashSet<Edge>> myAdjList;
    private int myNumVertices;
    private int myNumEdges;

    public Graph() {
        myAdjList = new HashMap<Integer, HashSet<Edge>>();
        myNumVertices = myNumEdges = 0;
    }

    public void addVertex(Integer id) {
        HashSet<Edge> edges = this.myAdjList.get(id);
        if (edges == null) {
            edges = new HashSet<Edge>();
            this.myAdjList.put(id, edges);
            myNumVertices += 1;
        }
    }

    public boolean hasEdge(Integer from, Integer to, Integer weight) {

        if (this.myAdjList.get(from)== null)
            return false;
        return myAdjList.get(from).contains(new Edge(from,to,weight));
    }

    public void addEdge(Integer from, Integer to, Integer weight) {

        if(from==to)
            return;
        if (hasEdge(from, to, weight))
            return;
        myNumEdges += 1;
        myAdjList.get(from).add(new Edge(from,to,weight));
    }

    public Graph(List<int[]> dataMatrix) {
        this();
        buildGraph(dataMatrix);
    }

    public void buildGraph(List<int[]> matrix) {
        for (int[] edge : matrix) {
            addVertex(edge[0]);
            addVertex(edge[1]);
            addEdge(edge[0],edge[1],edge[2]);
        }
    }

    @Override
    public String toString() {
        for(Integer id : this.getMyAdjList().keySet()){
            System.out.print("Identyfikator hashmapy: " + id+ " " );
            for(Edge edge: this.getMyAdjList().get(id) ) {
                System.out.print(edge.getDestination() + "|");
            }
            System.out.println();
        }

        return "Graph{" +
                "myAdjList=" + myAdjList +
                '}';
    }

    public HashMap<Integer, HashSet<Edge>> getMyAdjList() {
        return myAdjList;
    }

    public void setMyAdjList(HashMap<Integer, HashSet<Edge>> myAdjList) {
        this.myAdjList = myAdjList;
    }

    public int getMyNumVertices() {
        return myNumVertices;
    }

    public void setMyNumVertices(int myNumVertices) {
        this.myNumVertices = myNumVertices;
    }

    public int getMyNumEdges() {
        return myNumEdges;
    }

    public void setMyNumEdges(int myNumEdges) {
        this.myNumEdges = myNumEdges;
    }
}
