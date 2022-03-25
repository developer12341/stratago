package MVC.model;

import MVC.model.PointClasses.Point;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    Board b;
    @BeforeEach
    void setUp() {
        b = new Board();
        initPossibleMoves();
    }

    void printBoards() {
        System.out.println(Arrays.deepToString(b.getPieces("red")));
        System.out.println(Arrays.deepToString(b.getPieces("blue")));
    }


    @Test
    void updateMoves() {

    }

    void initPossibleMoves() {
        b.setRedPieces(new Piece[][]{
                {Piece.BOMB, null, null, null, null, null, null, null, null, null},
                {Piece.BOMB, null, null, null, null, null, null, null, null, null},
                {Piece.MINER, null, null, null, null, null, null, null, null, null},
                {Piece.BOMB, null, null, null, null, null, null, null, null, null},
                {Piece.BOMB, null, null, null, null, null, null, null, null, null},
                {Piece.BOMB, null, null, null, null, null, null, null, null, null},
                {Piece.BOMB, null, null, null, null, null, null, null, null, null},
                {Piece.BOMB, null, null, null, null, null, null, null, null, null},
                {Piece.BOMB, null, null, null, null, null, null, null, null, null},
                {Piece.BOMB, null, null, null, null, null, null, null, null, null}
            });
        b.setBluePieces(new Piece[][]{
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
        b.initPossibleMoves();
    }

    @Test
    void switchPieces() {

    }

    @Test
    void moveTo() {
        System.out.println(b.moveTo(Point.create(2,1),Point.create(2,2)));
        System.out.println(b.getPieces("blue")[2][2]);
        System.out.println(b.getPieces("blue")[2][1]);
        System.out.println(b.getMoves("red").toString());
        System.out.println(b.getMoves("blue").toString());
    }

    @Test
    void getColor() {
        System.out.println(Arrays.deepToString(b.getPieces("blue")));
        System.out.println(b.getPieces("blue")[2][1]);
        System.out.println(b.getColor(Point.create(2,1)));
        assertEquals(b.getColor(Point.create(2,1)),"blue");
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
        assertFalse(b.doesHaveMoves(Point.create(0,0)));
        assertTrue(b.doesHaveMoves(Point.create(2,1)));
        b.moveTo(Point.create(2,1),Point.create(2,2));
        b.updateMoves(Point.create(2,2));
        b.updateMoves(Point.create(2,1));
        assertTrue(b.doesHaveMoves(Point.create(2,2)));
        assertFalse(b.doesHaveMoves(Point.create(2,1)));
    }
}