package MVC.model;

public class Attack {
    private Move move;
    private Piece attackingPiece;

    public Attack(Move move, Piece attackingPiece) {
        this.move = move;
        this.attackingPiece = attackingPiece;
    }

    public Attack(Point p1, Point p2, Piece attackingPiece) {
        this(Move.create(p1,p2), attackingPiece);
    }

    public Move getMove() {
        return move;
    }

    public Piece getAttackingPiece() {
        return attackingPiece;
    }
}
