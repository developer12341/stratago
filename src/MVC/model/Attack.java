package MVC.model;

/**
 * this class encapsulate the data about an attack on a piece.
 */
public class Attack {
    private Move move;
    private Piece attackingPiece;
    private Piece defendingPiece;

    /**
     * @param move the movement of the piece
     * @param attackingPiece the piece that was at the start of the move
     * @param defendingPiece the piece that was at the end of the move
     */
    public Attack(Move move, Piece attackingPiece, Piece defendingPiece) {
        this.move = move;
        this.attackingPiece = attackingPiece;
        this.defendingPiece = defendingPiece;
    }

    public Attack(Point p1, Point p2, Piece attackingPiece, Piece defendingPiece) {
        this(Move.create(p1,p2), attackingPiece, defendingPiece);
    }

    public Piece getDefendingPiece() {
        return defendingPiece;
    }

    public Move getMove() {
        return move;
    }

    public Piece getAttackingPiece() {
        return attackingPiece;
    }
}
