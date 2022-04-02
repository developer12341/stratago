package MVC.Stratagies.BoardComputerView;

import MVC.model.Board;
import MVC.model.Piece;
import MVC.model.Point;

import java.util.Arrays;

public class SpeculationBoard {
    private final Board board;
    private PossiblePiece[][] otherPieces;
    private int[] invisiblePieceCount;
    private int[] visiblePieceCount;

    private String color;

    public SpeculationBoard(Board board, String color) {
        this.board = board;
        this.color = color;

        invisiblePieceCount = new int[]{1, 8, 5, 4, 4, 4, 3, 2, 1, 1, 1, 6};
        visiblePieceCount = new int[12];

        otherPieces = new PossiblePiece[10][10];
        Piece[][][] defaultBoards = Board.defaultBoards;
        int[][][] amountOfAppearances = new int[defaultBoards[0].length][defaultBoards[0][0].length][12];
        for (Piece[][] defaultBoard : defaultBoards) {
            for (int row = 0; row < defaultBoard.length; row++) {
                for (int col = 0; col < defaultBoard[row].length; col++) {
                    amountOfAppearances[row][col][defaultBoard[row][col].PieceNumber - 1]++;
                }
            }
        }
        //todo: fix these loops
        for (int col = 0; col < amountOfAppearances[0].length; col++) {
            otherPieces[6][col] = new PossiblePiece();
            for (int pieceNumber = 0; pieceNumber < amountOfAppearances[0][col].length; pieceNumber++) {
                otherPieces[6][col].updateProbability(pieceNumber,
                        amountOfAppearances[0][col][pieceNumber],
                        defaultBoards.length);
            }
        }
        for (int col = 0; col < amountOfAppearances[1].length; col++) {
            otherPieces[7][col] = new PossiblePiece();
            for (int pieceNumber = 0; pieceNumber < amountOfAppearances[1][col].length; pieceNumber++) {
                otherPieces[7][col].updateProbability(pieceNumber,
                        amountOfAppearances[1][col][pieceNumber],
                        defaultBoards.length);
            }
        }
        for (int col = 0; col < amountOfAppearances[2].length; col++) {
            otherPieces[8][col] = new PossiblePiece();
            for (int pieceNumber = 0; pieceNumber < amountOfAppearances[2][col].length; pieceNumber++) {
                otherPieces[8][col].updateProbability(pieceNumber,
                        amountOfAppearances[2][col][pieceNumber],
                        defaultBoards.length);
            }
        }
        for (int col = 0; col < amountOfAppearances[3].length; col++) {
            otherPieces[9][col] = new PossiblePiece();
            for (int pieceNumber = 0; pieceNumber < amountOfAppearances[3][col].length; pieceNumber++) {
                otherPieces[9][col].updateProbability(pieceNumber,
                        amountOfAppearances[3][col][pieceNumber],
                        defaultBoards.length);
            }
        }
    }

    public PossiblePiece[][] getOtherPieces() {
        return otherPieces;
    }

    public void otherPlayerMove(Point p1, Point p2, Piece attackingPiece) {
        if (otherPieces[p1.getRow()][p1.getCol()] == null) {
            throw new IllegalArgumentException("there is no piece in " + p1);
        }
        otherPieces[p2.getRow()][p2.getCol()] = otherPieces[p1.getRow()][p1.getCol()];
        otherPieces[p1.getRow()][p1.getCol()] = null;
        if (board.isFree(p2) || color.equals(board.getColor(p2))) {
            //the piece in p1 attacked and there was a tie or the computer won.
            otherPieces[p2.getRow()][p2.getCol()] = null;
            invisiblePieceCount[attackingPiece.PieceNumber - 1]--;
        } else if (attackingPiece != null) {
            //there was an attack, and now we know the piece.
            invisiblePieceCount[attackingPiece.PieceNumber - 1]--;
        } else {
            //if the piece is a scout.
            if (p1.distance(p2) > 1)
                invisiblePieceCount[Piece.SCOUT.PieceNumber - 1]--;
            else {
                otherPieces[p2.getRow()][p2.getCol()].setProbability(Piece.BOMB, 0);
                otherPieces[p2.getRow()][p2.getCol()].setProbability(Piece.FLAG, 0);
                updateProbability(p1, p2);
            }
        }
    }

    private void updateProbability(Point p1, Point p2) {
        //todo:need to update the piece's probabilities to reflect his movement.

    }

    public Board getBoard() {
        Board newBoard = board.clone(color);
        int invisiblePieceSum = Arrays.stream(invisiblePieceCount).sum();
        for (int row = 0; row < otherPieces.length; row++) {
            for (int col = 0; col < otherPieces[row].length; col++) {
                if (otherPieces[row][col] != null)
                    newBoard.setPiece(row,
                            col,
                            otherPieces[row][col].getExpectedPiece(invisiblePieceCount, invisiblePieceSum),
                            newBoard.getOppositeColor(color));
            }
        }
        newBoard.initPossibleMoves();
        newBoard.setGameOverFlag("red", true);
        newBoard.setGameOverFlag("blue", true);

        return newBoard;
    }
}
