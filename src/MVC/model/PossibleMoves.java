package MVC.model;

import MVC.model.Iterators.PossibleMoveIterator;

import java.util.*;

/**
 * this class encapsulate all the moves a player can make.
 * it is an iterable and can be iterated over in the following format
 * for(Move move: possiblemoves){
 * ...
 * }
 */
public class PossibleMoves implements Iterable<Move>, Cloneable {
    public HashMap<Point, List<Point>> Moves;// {Point -> [Point1, Point2], Point3 -> [Point4...}

    public PossibleMoves() {
        Moves = new HashMap<>();
    }

    /**
     * @param p1 the starting point
     * @param p2 the ending point
     */
    public void addMove(Point p1, Point p2) {
        if (contains(p1, p2))
            throw new IllegalArgumentException("the point was already added");
        if (!Moves.containsKey(p1)) {
            Moves.put(p1, new ArrayList<>());
        }
        if (!Moves.get(p1).contains(p2))
            Moves.get(p1).add(p2);
    }

    public List<Point> getMoves(Point p1) {
        return Moves.get(p1);
    }

    public boolean contains(Point p1, Point p2) {
        return contains(p1) && Moves.get(p1).contains(p2);
    }

    public boolean contains(Point p) {
        return Moves.containsKey(p);
    }

    public void clear(Point p) {
        if (!Moves.containsKey(p))
            return;
        Moves.remove(p);
    }

    public void clear() {
        Moves.clear();
    }

    @Override
    public String toString() {
        StringBuilder outMsg = new StringBuilder("\nPossible Moves{\n");
        outMsg.append('\t');
        Iterator<Map.Entry<Point, List<Point>>> iter = Moves.entrySet().iterator();

        Map.Entry<Point, List<Point>> e = iter.next();
        outMsg.append(e.getKey().toString()).append(" -> [");

        Iterator<Point> iterator = e.getValue().iterator();
        outMsg.append(iterator.next());
        while (iterator.hasNext()) {
            Point p = iterator.next();
            outMsg.append(", ").append(p);
        }
        outMsg.append("]");
        while (iter.hasNext()) {
            outMsg.append(",\n");
            outMsg.append('\t');

            e = iter.next();
            outMsg.append(e.getKey().toString()).append(" -> [");

            iterator = e.getValue().iterator();
            outMsg.append(iterator.next());
            while (iterator.hasNext()) {
                Point p = iterator.next();
                outMsg.append(", ").append(p);
            }
            outMsg.append("]");
        }
        outMsg.append("}");
        return String.valueOf(outMsg);
    }

    public boolean isEmpty() {
        return Moves.isEmpty();
    }

    /**
     *
     * @return an iterator of this possible move set.
     */
    @Override
    public Iterator<Move> iterator() {
//        this iterator is not influenced by and changes to the data structure in the future to prevent an error
//        accruing
        HashMap<Point, List<Point>> moves = new HashMap<>();
        //cloning Moves and the lists inside Moves.
        for (Map.Entry<Point, List<Point>> entry : Moves.entrySet()) {
            var n = new ArrayList<>(entry.getValue());
            moves.put(entry.getKey(), n);
        }
        return new PossibleMoveIterator(moves);
    }

    public int size() {
        return Moves.values().stream().mapToInt(List::size).sum();
    }

    @Override
    public PossibleMoves clone() {
        try {
            PossibleMoves clone = (PossibleMoves) super.clone();
            clone.Moves = new HashMap<>();
            //cloning Moves and the lists inside Moves.
            for (Map.Entry<Point, List<Point>> entry : Moves.entrySet()) {
                var n = new ArrayList<>(entry.getValue());
                clone.Moves.put(entry.getKey(), n);
            }
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    /**
     * @return a random move
     */
    public Move chooseRandomMove() {
        int random = Board.RANDOM_GENERATOR.nextInt(size());
        for (Move move : this) {
            if (random == 0)
                return move;
            random--;
        }
        return null;
    }
}
