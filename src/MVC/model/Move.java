package MVC.model;

import MVC.model.caches.MoveCache;

public class Move {
    private static final MoveCache cache = new MoveCache();

    private Point p1;
    private Point p2;

    public static Move create(Point p1, Point p2){
        if(cache.contains(p1, p2))
            return cache.get(p1,p2);
        Move obj= new Move(p1, p2);
        cache.put(obj);
        return obj;
    }


    public static Move create(int x1, int y1, int x2, int y2){
        return create(Point.create(x1, y1), Point.create(x2,y2));
    }



    private Move(Point p1, Point p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    public Point getP1() {
        return p1;
    }

    public Point getP2() {
        return p2;
    }

    @Override
    public String toString() {
        return "Move{" +
                "p1=" + p1 +
                ", p2=" + p2 +
                '}';
    }
}
