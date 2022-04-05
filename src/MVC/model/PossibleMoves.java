package MVC.model;

import MVC.model.Iterators.PossibleMoveIterator;

import java.util.*;

public class PossibleMoves implements Iterable<Move>, Cloneable {
    public HashMap<Point, List<Point>> Moves;
    public List<Pair<Point,List<Point>>> MovesList;
    public PossibleMoves() {
        Moves = new HashMap<>();
        MovesList = new ArrayList<>();
    }

    public void addMove(Point p1, Point p2) {
        if (contains(p1, p2))
            throw new IllegalArgumentException("the point was already added");
        if (!Moves.containsKey(p1)) {
            Moves.put(p1, new ArrayList<>());
            MovesList.add(new Pair<>(p1,Moves.get(p1)));
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
        if(!Moves.containsKey(p))
            return;
        var obj = new Pair<>(p, Moves.get(p));
        MovesList.remove(obj);
        Moves.remove(p);
    }

    public void clear() {
        Moves.clear();
        MovesList.clear();
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

    @Override
    public Iterator<Move> iterator() {
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

    public Move chooseRandomMove() {
        int random = Board.RANDOM_GENERATOR.nextInt(size());
        for (Move move : this){
            if(random == 0)
                return move;
            random--;
        }
        return null;
//        Pair<Point, List<Point>> list = MovesList.get(random);
//        random = Board.RANDOM_GENERATOR.nextInt(list.getValue().size());
//        return Move.create(list.getKey(), list.getValue().get(random));
    }
}
