package MVC.Stratagies.BoardComputerView;

import MVC.model.Board;
import MVC.model.Piece;
import MVC.model.Point;

import java.util.Arrays;

import static MVC.model.Piece.BOMB;
import static MVC.model.Piece.FLAG;

public class SpeculationBoard {
    private final Board mainBoard;
    private PossiblePiece[][] otherPieces;
    private int[] invisiblePieceCount;
    private int[] visiblePieceCount;

    private String color;

    public SpeculationBoard(Board board, String color) {
        this.mainBoard = board;
        this.color = color;

        invisiblePieceCount = new int[]{1, 8, 5, 4, 4, 4, 3, 2, 1, 1, 6, 1};
        visiblePieceCount = new int[12];

        otherPieces = new PossiblePiece[Board.size][Board.size];
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
        if (mainBoard.isFree(p2) || color.equals(mainBoard.getColor(p2))) {
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
                otherPieces[p2.getRow()][p2.getCol()].setProbability(FLAG, 0);
                updateProbability(p1, p2);
            }
        }
    }

    private void updateProbability(Point p1, Point p2) {
        //todo:need to update the piece's probabilities to reflect his movement.


    }


    private int updateSums(Piece piece, int[] pieceCount, int pieceSum) {
        if (pieceCount[piece.PieceNumber - 1] == 0)
            throw new IllegalStateException("there are no " + piece + " available.");
        pieceCount[piece.PieceNumber - 1]--;
        pieceSum--;
        return pieceSum;
    }

    public Board getBoard() {
        Board newBoard = mainBoard.clone(color);
        int[] invisiblePieceCountCopy = invisiblePieceCount.clone();
        int invisiblePieceSum = Arrays.stream(invisiblePieceCountCopy).sum();

        //find out where the computer thinks the flag is
        int flagRow = -1, flagCol = -1;
        float maxProbability = 0;
        for (int row = otherPieces.length - 1; row >= 0; row--) {
            for (int col = 0; col < otherPieces[row].length; col++) {
                if (otherPieces[row][col] != null && otherPieces[row][col].getProbability(FLAG) > maxProbability) {
                    maxProbability = otherPieces[row][col].getProbability(FLAG);
                    flagCol = col;
                    flagRow = row;
                }
            }
        }
        if (flagCol == -1)
            throw new IllegalStateException("the flag wasn't found, we need more data");
        newBoard.setPiece(flagRow, flagCol, FLAG, Board.getOppositeColor(color));
        invisiblePieceSum = updateSums(FLAG, invisiblePieceCountCopy, invisiblePieceSum);

        //the flag is most likely surrounded with bombs
        if (newBoard.isValid(flagRow - 1, flagCol)) {
            newBoard.setPiece(flagRow - 1, flagCol, BOMB, Board.getOppositeColor(color));
            invisiblePieceSum = updateSums(BOMB, invisiblePieceCountCopy, invisiblePieceSum);
        }
        if (newBoard.isValid(flagRow + 1, flagCol)) {
            newBoard.setPiece(flagRow + 1, flagCol, BOMB, Board.getOppositeColor(color));
            invisiblePieceSum = updateSums(BOMB, invisiblePieceCountCopy, invisiblePieceSum);
        }
        if (newBoard.isValid(flagRow, flagCol - 1)) {
            newBoard.setPiece(flagRow, flagCol - 1, BOMB, Board.getOppositeColor(color));
            invisiblePieceSum = updateSums(BOMB, invisiblePieceCountCopy, invisiblePieceSum);
        }
        if (newBoard.isValid(flagRow, flagCol + 1)) {
            newBoard.setPiece(flagRow, flagCol + 1, BOMB, Board.getOppositeColor(color));
            invisiblePieceSum = updateSums(BOMB, invisiblePieceCountCopy, invisiblePieceSum);
        }


        do {

            int lowestFrequencyPiece = -1;
            int minFrequency = Integer.MAX_VALUE;
            for (int pieceNum = 0; pieceNum < invisiblePieceCountCopy.length; pieceNum++) {
                if (invisiblePieceCountCopy[pieceNum] != 0 && invisiblePieceCountCopy[pieceNum] < minFrequency) {
                    minFrequency = invisiblePieceCountCopy[pieceNum];
                    lowestFrequencyPiece = pieceNum;
                }
            }
            if (lowestFrequencyPiece == -1) {
                break;
            }
            Piece pieceToSearch = Piece.fromInteger(lowestFrequencyPiece + 1);
            int Row = -1, Col = -1;
            maxProbability = 0;
            for (int row = 0; row < otherPieces.length; row++) {
                for (int col = 0; col < otherPieces[row].length; col++) {
                    if (newBoard.isFree(row, col) && otherPieces[row][col] != null && otherPieces[row][col].getProbability(pieceToSearch) > maxProbability) {
                        maxProbability = otherPieces[row][col].getProbability(pieceToSearch);
                        Col = col;
                        Row = row;
                    }
                }
            }
            if (Col == -1) {
                for (int row = 0; row < otherPieces.length; row++) {
                    for (int col = 0; col < otherPieces[row].length; col++) {
                        if(newBoard.isFree(row,col) && otherPieces[row][col] == null){
                            newBoard.setPiece(row,col,pieceToSearch, Board.getOppositeColor(color));
                            invisiblePieceSum = updateSums(pieceToSearch, invisiblePieceCountCopy, invisiblePieceSum);
                        }
                    }
                }
            } else {
                newBoard.setPiece(Row, Col, pieceToSearch, Board.getOppositeColor(color));
                invisiblePieceSum = updateSums(pieceToSearch, invisiblePieceCountCopy, invisiblePieceSum);
            }
        } while (invisiblePieceSum > 0);


//
//        for (int row = 0; row < otherPieces.length; row++) {
//            for (int col = 0; col < otherPieces[row].length; col++) {
//                if (otherPieces[row][col] != null && mainBoard.isFree(row, col)) {
//                    newBoard.setPiece(row,
//                            col,
//                            otherPieces[row][col].getExpectedPiece(invisiblePieceCountCopy, invisiblePieceSum),
//                            newBoard.getOppositeColor(color));
//                    invisiblePieceCountCopy[newBoard.getPiece(Point.create(row, col)).PieceNumber - 1]--;
//                    invisiblePieceSum--;
//                }
//            }
//        }
        newBoard.initPossibleMoves();
        newBoard.setGameOverFlag("red", true);
        newBoard.setGameOverFlag("blue", true);

        return newBoard;
    }
}
