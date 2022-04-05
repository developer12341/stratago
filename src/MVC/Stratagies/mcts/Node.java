package MVC.Stratagies.mcts;

import MVC.model.Move;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Node {

    private Node parent;
    private Move move;
    private int visits;
    private int score;
    private final List<Node> children;

    public Node(Move move) {
        this();
        this.move = move;
    }

    public Node() {
        children = new ArrayList<>();
        parent = null;
    }

    public Node getChildWithMaxScore() {
        return Collections.max(children, Comparator.comparingInt(o -> o.score));
    }

    public Move getMove() {
        return move;
    }

    public int getVisits() {
        return visits;
    }

    public int getScore() {
        return score;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Node getChild(int index){
        return children.get(index);
    }
    public List<Node> getChildren() {
        return children;
    }

    public void addChild(Node node) {
        children.add(node);
    }

    public void incrementScore() {
        score++;
    }

    public void incrementVisits() {
        visits++;
    }

    public void setScore(int score) {
        this.score = score;

    }
}
