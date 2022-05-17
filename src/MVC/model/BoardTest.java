package MVC.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static MVC.model.Piece.*;
import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    Board b;

    public static void main(String[] args) {
        int[][] board = {
                {6, 2, 9, 'B', 4, 4, 2, 2, 4, 4},
                {8, 10, 2, 8, 'b', 5, 2, 5, 2, 3},
                {'b', 3, 1, 7, 6, 'b', 3, 6, 5, 2},
                {'F', 'b', 7, 3, 7, 6, 'b', 3, 2, 5}
        };
        Piece[][] b2 = new Piece[4][10];
        //copy from board to b2
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 10; j++) {
                b2[i][j] = Piece.fromInteger(board[i][j]);
            }
        }

        //print b2
        printBoard(b2);
    }

    private static void printBoard(Piece[][] b) {
        System.out.print('{');
        for (Piece[] pieces : b) {
            System.out.print('{');
            System.out.print(pieces[0].name());
            for (int col = 1; col < pieces.length; col++) {
                System.out.print(", " + pieces[col].name());
            }
            System.out.print("}, \n");
        }
        System.out.print("}");

    }

    @BeforeEach
    void setUp() {
        b = new Board();
    }


    @Test
    void updateMoves() {

    }

    @Test
    void switchPieces() {

    }

    @Test
    void moveTo() {
        assertNotNull(b.getPiece(Point.create(3, 1)));
        assertNull(b.getPiece(Point.create(4, 1)));
        System.out.println(b.moveTo(Point.create(3, 1), Point.create(4, 1)));
        assertNull(b.getPiece(Point.create(3, 1)));
        assertNotNull(b.getPiece(Point.create(4, 1)));
        System.out.println(b);
    }

    @Test
    void getColor() {
        System.out.println(b.getColor(Point.create(2, 1)));
        assertEquals(b.getColor(Point.create(2, 1)), "blue");
    }

    @Test
    void getPieces() {
    }

    void testOptimizedFunctions(int D) {
        compareMoves(b);
        if (D == 0 || b.isGameOver()) {
            return;
        }
        int[] moves = b.getOptimizedMoves(b.getPlayerTurn());
        for (int move : moves) {
            Piece p1 = b.getPiece(MoveLib.unpackRowFrom(move), MoveLib.unpackColFrom(move));
            Piece p2 = b.getPiece(MoveLib.unpackRowTo(move), MoveLib.unpackColTo(move));
            String PieceColor = b.getColor(MoveLib.unpackRowFrom(move), MoveLib.unpackColFrom(move));
            b.OptimizedMoveTo(move);

            testOptimizedFunctions(D - 1);
            b.OptimizedUndoMove(move, p1, p2, PieceColor);
        }
    }

    @Test
    void testOptimizedFunctions2() {
        testOptimizedFunctions(5);
    }

    @Test
    void testGetAmountOfMoves() {
        for (int row = 0; row < Board.size; row++) {
            for (int col = 0; col < Board.size; col++) {
                if (!b.isValid(row, col)) {
                    continue;
                }
                if (b.getPiece(row, col) != null) {
                    if (b.getMoves(Point.create(row, col)) != null) {
                        assertEquals(b.getAmountOfMoves(row, col), b.getMoves(Point.create(row, col)).size());
                    }
                }
            }
        }
    }

    @Test
    void testGetAmountOfMoves2() {
        while (!b.isGameOver()) {
            //select a random move
            List<Move> moves = b.getMoves(b.getPlayerTurn());
            Move move = moves.get((int) (Math.random() * moves.size()));
            b.moveTo(move.getP1(), move.getP2());
            testGetAmountOfMoves();
        }
        System.out.println("success");
    }


    @Test
    void testGetMoves() {
        Board d = new Board();
        d.setPieces("red", new Piece[][]{
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, FLAG, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}});

        d.setPieces("blue", new Piece[][]{
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, SCOUT, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, SERGEANT, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {CAPTAIN, null, MINER, SCOUT, null, BOMB, null, BOMB, BOMB, FLAG}});
        System.out.println(d);
        List<Point> moves = d.getMoves(Point.create(1, 8));
        System.out.println(moves);

    }


    private void compareMoves(Board b) {
        List<Move> moves = b.getMoves("red");
        int[] optimizedMoves = b.getOptimizedMoves("red");
        assertEquals(moves.size(), optimizedMoves.length);
        for (int optimizedMove : optimizedMoves) {
            assertTrue(moves.contains(MoveLib.convertToMove(optimizedMove)));
        }

        moves = b.getMoves("blue");
        optimizedMoves = b.getOptimizedMoves("blue");
        assertEquals(moves.size(), optimizedMoves.length);
        for (int optimizedMove : optimizedMoves) {
            assertTrue(moves.contains(MoveLib.convertToMove(optimizedMove)));
        }
    }


    @Test
    void isPossibleMove() {
        setUpKnownBoard();
        System.out.println(b.getMoves(Point.create(3, 1)));
        System.out.println(b.isPossibleMove(Point.create(3, 1), Point.create(4, 1)));
//        assertFalse(b.isPossibleMove(Point.create(0, 0), Point.create(0, 1)));
//        assertTrue(b.isPossibleMove(Point.create(3, 1), Point.create(4, 1)));
//        assertFalse(b.isPossibleMove(Point.create(3, 2), Point.create(4, 2)));
    }

    @Test
    void getMoves() {
        setUpKnownBoard();
        System.out.println(b);
        assertEquals(3, b.getMoves(Point.create(6, 0)).size());
        assertEquals(b.getMoves("red").size(), b.amountOfMoves("red"));
        System.out.println(b.getMoves("blue"));
        System.out.println(b.getMoves("blue").size());
        assertEquals(b.getMoves("blue").size(), b.amountOfMoves("blue"));


    }

    @Test
    void amountOfMoves() {
        b.setPieces("red", new Piece[][]{
                {SERGEANT, SCOUT, MINER, BOMB, SERGEANT, MINER, MINER, BOMB, FLAG, BOMB},
                {BOMB, CAPTAIN, SPY, MAJOR, LIEUTENANT, SCOUT, CAPTAIN, LIEUTENANT, BOMB, SERGEANT},
                {MINER, SCOUT, COLONEL, MAJOR, BOMB, LIEUTENANT, MARSHAL, MAJOR, LIEUTENANT, COLONEL},
                {GENERAL, CAPTAIN, SCOUT, SERGEANT, SCOUT, SCOUT, SCOUT, MINER, SERGEANT, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}});

        b.setPieces("blue", new Piece[][]{
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {SCOUT, MINER, LIEUTENANT, MAJOR, BOMB, CAPTAIN, MINER, MARSHAL, MAJOR, SCOUT},
                {GENERAL, SCOUT, MAJOR, COLONEL, BOMB, COLONEL, LIEUTENANT, CAPTAIN, null, SERGEANT},
                {SCOUT, SCOUT, MINER, SPY, null, MINER, CAPTAIN, LIEUTENANT, LIEUTENANT, BOMB},
                {CAPTAIN, SERGEANT, MINER, SCOUT, SCOUT, BOMB, null, BOMB, BOMB, FLAG}});


        System.out.println(b.getMoves("red"));
        System.out.println(b.getMoves("blue"));

        compareMoves(b);

    }


    private void setUpKnownBoard() {
        b.setPieces("blue", new Piece[][]{
                {SCOUT, COLONEL, LIEUTENANT, SCOUT, CAPTAIN, SCOUT, GENERAL, MINER, SCOUT, CAPTAIN},
                {MARSHAL, SCOUT, MAJOR, COLONEL, SCOUT, CAPTAIN, BOMB, LIEUTENANT, BOMB, LIEUTENANT},
                {CAPTAIN, SERGEANT, MAJOR, SPY, MAJOR, LIEUTENANT, BOMB, SERGEANT, BOMB, SERGEANT},
                {MINER, SCOUT, MINER, MINER, SERGEANT, BOMB, FLAG, BOMB, MINER, SCOUT},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
        });
        b.setPieces("red", new Piece[][]{
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {SCOUT, CAPTAIN, LIEUTENANT, SCOUT, SCOUT, CAPTAIN, SCOUT, MINER, SCOUT, CAPTAIN},
                {MINER, SCOUT, COLONEL, SPY, GENERAL, SCOUT, SCOUT, MAJOR, COLONEL, LIEUTENANT},
                {SERGEANT, SERGEANT, MAJOR, SPY, MAJOR, LIEUTENANT, BOMB, SERGEANT, LIEUTENANT, SERGEANT},
                {MINER, SCOUT, MINER, SCOUT, SERGEANT, BOMB, FLAG, BOMB, MINER, MINER},
        });
    }

    @Test
    void doesHaveMoves() {
        assertFalse(b.doesHaveMoves(Point.create(0, 0)));
        assertTrue(b.doesHaveMoves(Point.create(2, 1)));
        b.moveTo(Point.create(2, 1), Point.create(2, 2));
        assertTrue(b.doesHaveMoves(Point.create(2, 2)));
        assertFalse(b.doesHaveMoves(Point.create(2, 1)));
    }

    @Test
    void isGameOver() {
        assertFalse(b.isGameOver());


    }

    @Test
    void testIsSurrounded() {
        assertTrue(b.isSurrounded(Point.create(0, 0), b.getPieces("blue")));
        assertFalse(b.isSurrounded(Point.create(3, 1), b.getPieces("blue")));
    }


    @Test
    void testClone() {
        Board b1 = b.clone("red");
        assertNotNull(b1);
        b1.setPiece(2, 2, null, "red");
        assertNull(b1.getPiece(2, 2));
        assertNotNull(b.getPiece(2, 2));

        b1.setPiece(2, 2, PLACEHOLDER, "blue");
        assertEquals(PLACEHOLDER, b1.getPiece(2, 2));
        assertNotEquals(PLACEHOLDER, b.getPiece(2, 2));
        assertNotEquals(b.getPieces("red"), b1.getPieces("red"));
        assertNotEquals(b.getPieces("blue"), b1.getPieces("blue"));
        System.out.println(b1);
        b1.moveTo(Point.create(6, 4), Point.create(5, 4));
        assertNull(b1.getPiece(6, 4));
        assertNotEquals(b1.getPlayerTurn(), b.getPlayerTurn());
    }

    @Test
    void undoMove() {
//        initPossibleMoves();
//        System.out.println(b.getMoves("red"));
//        System.out.println(b.getMoves("blue"));
//        System.out.println(b);
//        Piece p1 = b.getPiece(Point.create(2,1));
//        Piece p2 = b.getPiece(Point.create(2,0));
//        System.out.println(b.getColor(Point.create(2,1)));
//        b.moveTo(Point.create(2,1),Point.create(2,0));
//        System.out.println(b);
//        assertTrue(b.isGameOver());
//        b.undoMove(Point.create(2,1),Point.create(2,0),p1,p2,"blue");
//        System.out.println(b);
//        System.out.println(b.getMoves("red"));
//        System.out.println(b.getMoves("blue"));

        setUpKnownBoard();
        b.moveTo(Point.create(3, 2), Point.create(4, 2));
        System.out.println(b);
        System.out.println(b.getMoves("red"));
        System.out.println(b.getMoves("blue"));

    }

    @Test
    void testAmountOfMoves(Board b) {
        for (int row = 0; row < Board.size; row++) {
            for (int col = 0; col < Board.size; col++) {
                if (!b.isValid(Point.create(row, col))) {
                    continue;
                }
                if (b.getPiece(row, col) != null) {
                    if (b.doesHaveMoves(Point.create(row, col))) {
                        assertEquals(b.getMoves(Point.create(row, col)).size(), b.getAmountOfMoves(row, col));
                    }
                }
            }
        }
        assertEquals(b.getMoves("red").size(), b.amountOfMoves("red"));
        assertEquals(b.getMoves("blue").size(), b.amountOfMoves("blue"));
    }

    void simulateGame(Board b, int Depth) {
        testAmountOfMoves(b);
        if (b.isGameOver() || Depth == 0) {
            return;
        }
        List<Move> moves = b.getMoves(b.getPlayerTurn());
        for (Move move : moves) {
            Piece p1 = b.getPiece(move.getP1());
            Piece p2 = b.getPiece(move.getP2());
            b.moveTo(move.getP1(), move.getP2());
            simulateGame(b, Depth - 1);
            b.undoMove(move, p1, p2, Board.getOppositeColor(b.getPlayerTurn()));
        }

    }

    @Test
    void testAmountOfMovesInGame() {
        simulateGame(b, 5);
    }


    @Test
    void getOppositeColor() {
    }

    @Test
    void hasMoves() {
    }

    @Test
    void setGameOverFlag() {
    }

    @Test
    void isFree() {
    }

    @Test
    void testIsFree() {
    }

    @Test
    void getPiece() {
    }

    @Test
    void setPiece() {
    }

    @Test
    void isValid() {
    }

    @Test
    void getWinner() {
    }

    @Test
    void getPlayerTurn() {
    }

    @Test
    void testToString() {
    }

    @Test
    void setPieces() {
    }
}