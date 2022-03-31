package MVC.model;

import java.util.*;

public class PossibleMoves implements Iterable<Move>{
    public final Map<Point, List<Point>> possibleMoves;

    public PossibleMoves() {
        possibleMoves = new HashMap<>();
    }

    public void addMove(Point p1, Point p2){
        if(contains(p1,p2))
            throw new IllegalArgumentException("the point was already added");
        if(!possibleMoves.containsKey(p1))
            possibleMoves.put(p1, new ArrayList<>());
        if(!possibleMoves.get(p1).contains(p2))
            possibleMoves.get(p1).add(p2);
    }

    public Map<Point, List<Point>> getMoves() {
        return possibleMoves;
    }

    public List<Point> getMoves(Point p1){
        return possibleMoves.get(p1);
    }

    public boolean contains(Point p1, Point p2){
        return contains(p1) && possibleMoves.get(p1).contains(p2);
    }

    public boolean contains(Point p){
        return possibleMoves.containsKey(p);
    }

    public void clear(Point p){
        possibleMoves.remove(p);
    }

    public void clear(){
        possibleMoves.clear();
    }

    @Override
    public String toString() {
        return "PossibleMoves{" +
                "possibleMoves=" + possibleMoves +
                '}';
    }

    public boolean isEmpty() {
        return possibleMoves.isEmpty();
    }

    @Override
    public Iterator<Move> iterator() {
        return new PossibleMoveIterator(possibleMoves);
    }

    public int size() {
        return possibleMoves.values().stream().mapToInt(List::size).sum();
    }
}
