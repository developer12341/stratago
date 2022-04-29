package MVC.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static MVC.model.Piece.*;
import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    Board b;

    public static void main(String[] args) {
        int[][] board = {
                {6,2,9,'B',4,4,2,2,4,4},
                {8,10,2,8,'b',5,2,5,2,3},
                {'b',3,1,7,6,'b',3,6,5,2},
                {'F','b',7,3,7,6,'b',3,2,5}
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
    private static void printBoard(Piece[][] b){
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

    @Test
    void testOptimizedFunctions() {
        setUpKnownBoard();
        List<Move> moves = b.getMoves("red");
        int[] optimizedMoves = b.getOptimizedMoves("red");
        System.out.println(Arrays.toString(optimizedMoves));
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
        assertEquals(3, b.getMoves(Point.create(6,0)).size());
        assertEquals(b.getMoves("red").size(), b.amountOfMoves("red"));
        System.out.println(b.getMoves("blue"));
        System.out.println(b.getMoves("blue").size());
        assertEquals(b.getMoves("blue").size(), b.amountOfMoves("blue"));



    }

    @Test
    void amountOfMoves() {
//        b.setPieces("red", new Piece[][]{
//                SERGEANT(4) SCOUT(2) MINER(3) BOMB(11) SERGEANT(4) MINER(3) MINER(3) BOMB(11) FLAG(12) BOMB(
//                BOMB(11) CAPTAIN(6) SPY(1) MAJOR(7) LIEUTENANT(5) SCOUT(2) CAPTAIN(6) LIEUTENANT(5) BOMB(11) SERGEANT(
//                MINER(3) SCOUT(2) COLONEL(8) MAJOR(7) BOMB(11) LIEUTENANT(5) MARSHAL(10) MAJOR(7) LIEUTENANT(5) COLONEL(
//                GENERAL(9) CAPTAIN(6) SCOUT(2) SERGEANT(4) SCOUT(2) SCOUT(2) SCOUT(2) MINER(3) SERGEANT(4) null
//                null null null null null null null null null null
//                null null null null null null null null null null
//                SCOUT(2) MINER(3) LIEUTENANT(5) MAJOR(7) BOMB(11) CAPTAIN(6) MINER(3) MARSHAL(10) MAJOR(7) SCOUT(
//                GENERAL(9) SCOUT(2) MAJOR(7) COLONEL(8) BOMB(11) COLONEL(8) LIEUTENANT(5) CAPTAIN(6) null SERGEANT(
//                SCOUT(2) SCOUT(2) MINER(3) SPY(1) null MINER(3) CAPTAIN(6) LIEUTENANT(5) LIEUTENANT(5) BOMB(
//                CAPTAIN(6) SERGEANT(4) MINER(3) SCOUT(2) SCOUT(2) BOMB(11) null BOMB(11) BOMB(11) FLAG(
//
    }



    @Test
    void testGetMoves() {
        assertNotNull(b.getMoves("red"));
        System.out.println(b.getMoves("red"));

        setUpKnownBoard();
        b.moveTo(Point.create(3,1),Point.create(4,1));
        System.out.println(b.getMoves("red"));
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
    void testClone() {
        Board b1 = b.clone("red");
        assertNotNull(b1);
        b.setPiece(5, 5, Piece.PLACEHOLDER, "red");
        assertNull(b1.getPiece(Point.create(5, 5)));
        System.out.println(Arrays.deepToString(b1.getPieces("red")));
        System.out.println(Arrays.deepToString(b1.getPieces("blue")));

        System.out.println();
        System.out.println(b1.getMoves("blue"));
        System.out.println(b1.getMoves("red"));
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
        b.moveTo(Point.create(3,2),Point.create(4,2));
        System.out.println(b);
        System.out.println(b.getMoves("red"));
        System.out.println(b.getMoves("blue"));

    }

    @Test
    void testAmountOfMoves() {
        setUpKnownBoard();
        System.out.println(b.getMoves("red"));
        assertEquals(b.getMoves("red").size(), b.amountOfMoves("red"));
        assertEquals(b.getMoves("blue").size(), b.amountOfMoves("blue"));
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
    void testClone1() {
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