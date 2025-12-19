import java.util.*;

public class Node {
    private final int id;
    private final String title;
    private final Map<Node, Integer> links;

    public Node(int id, String title) {
        this.id = id;
        this.title = title;
        this.links = new HashMap<>();
    }

    public void link(Node node, int dist) {
        links.put(node, dist);
    }

    public Map<Node, Integer> getLinks() {
        return new HashMap<>(links);
    }

    public int getId() { return id; }
    public String getTitle() { return title; }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Node)) return false;
        return id == ((Node)obj).id;
    }

    public int hashCode() {
        return id;
    }

    public String toString() {
        return title;
    }
}