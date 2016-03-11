package bialkowski.lukasz;

public class Edge  {
    private final int source;
    private final int destination;
    private final int weight;

    public Edge(Integer source, Integer destination, Integer weight) {
        this.source = source;
        this.destination = destination;
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Edge{" +
                "source=" + source +
                ", destination=" + destination +
                ", weight=" + weight +
                '}';
    }

    public int getSource() {
        return source;
    }

    public int getDestination() {
        return destination;
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Edge edge = (Edge) o;

        if (source != edge.source) return false;
        if (destination != edge.destination) return false;
        return weight == edge.weight;

    }

    @Override
    public int hashCode() {
        int result = source;
        result = 31 * result + destination;
        result = 31 * result + weight;
        return result;
    }
}