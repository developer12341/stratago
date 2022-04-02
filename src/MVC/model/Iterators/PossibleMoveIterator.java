package MVC.model.Iterators;

import MVC.model.Move;
import MVC.model.Point;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PossibleMoveIterator implements Iterator<Move> {
    private Iterator<Map.Entry<Point, List<Point>>> mapHead;
    private Map.Entry<Point, List<Point>> currentEntry;
    private Iterator<Point> listIterator;

    public PossibleMoveIterator(Map<Point, List<Point>> moves) {
        mapHead = moves.entrySet().iterator();
        listIterator = null;
        currentEntry = null;
    }

    @Override
    public boolean hasNext() {
        return mapHead.hasNext() || listIterator != null && listIterator.hasNext();
    }

    @Override
    public Move next() {
        if(currentEntry == null)
            currentEntry = mapHead.next();
        if(listIterator == null)
            listIterator = currentEntry.getValue().iterator();
        if(!listIterator.hasNext()){
            currentEntry = mapHead.next();
            listIterator = currentEntry.getValue().iterator();
        }
        return Move.create(currentEntry.getKey(), listIterator.next());

    }
}
