import java.util.LinkedList;

public class Node {
    Node parent;
    Point point;
    LinkedList<Node> children;
    double gScore;
    double hScore;
    double fScore;

    public Node(Node parent, Point point, LinkedList<Node> children, double gScore, double hScore) {
        this.parent = parent;
        this.point = point;
        this.children = children;
        this.gScore = gScore;
        this.hScore = hScore;
        this.fScore = gScore + hScore;
    }

    public double getfScore() {
        return fScore;
    }
}
