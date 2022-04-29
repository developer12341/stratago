package MVC.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

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

    void printBoards() {
        System.out.println(Arrays.deepToString(b.getPieces("red")));
        System.out.println(Arrays.deepToString(b.getPieces("blue")));
    }


    @Test
    void updateMoves() {

    }

    void initPossibleMoves() {
        b.setPieces("red", new Piece[][]{
                {BOMB, null, null, null, null, null, null, null, null, null},
                {Piece.MINER, null, null, null, null, null, null, null, null, null},
                {Piece.FLAG, null, null, null, null, null, null, null, null, null},
                {BOMB, null, null, null, null, null, null, null, null, null},
                {BOMB, null, null, null, null, null, null, null, null, null},
                {BOMB, null, null, null, null, null, null, null, null, null},
                {BOMB, null, null, null, null, null, null, null, null, null},
                {BOMB, null, null, null, null, null, null, null, null, null},
                {BOMB, null, null, null, null, null, null, null, null, null},
                {BOMB, null, null, null, null, null, null, null, null, null}
        });
        b.setPieces("blue", new Piece[][]{
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, Piece.MINER, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
        });
        b.getMoves("red").clear();
        b.getMoves("blue").clear();
    }

    @Test
    void switchPieces() {

    }

    @Test
    void moveTo() {
        System.out.println(b.moveTo(Point.create(2, 1), Point.create(2, 2)));
        System.out.println(b.getPieces("blue")[2][2]);
        System.out.println(b.getPieces("blue")[2][1]);
        System.out.println(b.getMoves("red").toString());
        System.out.println(b.getMoves("blue").toString());
    }

    @Test
    void getColor() {
        System.out.println(Arrays.deepToString(b.getPieces("blue")));
        System.out.println(b.getPieces("blue")[2][1]);
        System.out.println(b.getColor(Point.create(2, 1)));
        assertEquals(b.getColor(Point.create(2, 1)), "blue");
    }

    @Test
    void getPieces() {
    }

    @Test
    void isPossibleMove() {
    }

    @Test
    void getMoves() {
    }

    @Test
    void testGetMoves() {
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
        System.out.println(Arrays.deepToString(b.getPieces("red")));
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

        b = new Board();
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
        b.moveTo(Point.create(3,2),Point.create(4,2));
        System.out.println(b);
        System.out.println(b.getMoves("red"));
        System.out.println(b.getMoves("blue"));

    }
}