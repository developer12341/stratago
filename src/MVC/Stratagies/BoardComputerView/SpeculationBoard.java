package MVC.Stratagies.BoardComputerView;

import MVC.model.Attack;
import MVC.model.Board;
import MVC.model.Piece;
import MVC.model.Point;

import java.util.Arrays;

import static MVC.model.Piece.BOMB;
import static MVC.model.Piece.FLAG;

/**
 * this is the board from the prospective of the computer player.
 * each player can't know which piece is where, so it basically guesses.
 */
public class SpeculationBoard {
    /**
     * this is the main board, though the computer never get access to the player's pieces
     */
    private final Board mainBoard;
    private final int[] invisiblePieceCount;
    private final String color;
    private final PossiblePiece[][] otherPieces;


    public SpeculationBoard(Board board, String color) {
        this.mainBoard = board;
        this.color = color;

        invisiblePieceCount = new int[]{1, 8, 5, 4, 4, 4, 3, 2, 1, 1, 6, 1};
        //in here we calculate the probability of a piece appearing in a place starting board
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

        for (int row = 0; row < amountOfAppearances.length; row++) {
            for (int col = 0; col < amountOfAppearances[row].length; col++) {
                otherPieces[otherPieces.length - amountOfAppearances.length + row][col] = new PossiblePiece();
                for (int pieceNumber = 0; pieceNumber < amountOfAppearances[row][col].length; pieceNumber++) {
                    otherPieces[otherPieces.length - amountOfAppearances.length + row][col].updateProbability(pieceNumber,
                            amountOfAppearances[row][col][pieceNumber],
                            defaultBoards.length);
                }
            }
        }


    }

    public PossiblePiece[][] getOtherPieces() {
        return otherPieces;
    }

    /**
     * when the computer moves it receives more information, this function update this information
     * @param attack the attack the computer made.
     */
    public void thisPlayerMove(Attack attack) {
        //if there is no extra information return
        if (attack.getAttackingPiece() == null) {
            return;
        }
        Point p2 = attack.getMove().getP2();
        //if the piece at p2 is known then return.
        if (otherPieces[p2.getRow()][p2.getCol()].getPiece() != null)
            return;

        //update the piece.
        otherPieces[p2.getRow()][p2.getCol()].setPiece(attack.getDefendingPiece());
        invisiblePieceCount[attack.getDefendingPiece().PieceNumber - 1]--;

    }


    /**
     * when the player move the computer is getting more information
     * @param p1 the point the player moved from
     * @param p2 the point the player moved to
     * @param attackingPiece the attacking piece.
     */
    public void otherPlayerMove(Point p1, Point p2, Piece attackingPiece) {
        if (otherPieces[p1.getRow()][p1.getCol()] == null) {
            throw new IllegalArgumentException("there is no piece in " + p1);
        }
        otherPieces[p2.getRow()][p2.getCol()] = otherPieces[p1.getRow()][p1.getCol()];
        otherPieces[p1.getRow()][p1.getCol()] = null;
        if (mainBoard.isFree(p2) && color.equals(mainBoard.getColor(p2))) {
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
            }
        }
    }

    /**
     * this function receive the objects that contain metadata about the pieces and updates them
     * @param piece the piece that is updated
     * @param pieceCount the count of every piece that should be on the board but isn't
     * @param pieceSum the count of pieces missing from the board
     * @return the updated sum of pieceCount
     */
    private int updateSums(Piece piece, int[] pieceCount, int pieceSum) {
        if (pieceCount[piece.PieceNumber - 1] == 0)
            throw new IllegalStateException("there are no " + piece + " available.");
        pieceCount[piece.PieceNumber - 1]--;
        pieceSum--;
        return pieceSum;
    }

    /**
     * when an AI or human tries to choose a move it first needs to estimate what pieces are where
     * this class stores the probability of every piece to be a certain piece.
     * this function collapses the probability to a board
     * @return the board the AI thinks it is playing in.
     */
    public Board getBoard() {
        //clone the mainBoard.
        Board newBoard = mainBoard.clone(color);

        //clone the metadata
        int[] invisiblePieceCountCopy = invisiblePieceCount.clone();
        int invisiblePieceSum = Arrays.stream(invisiblePieceCountCopy).sum();


        //first we need to find the most important piece, the flag.
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

        //set the flag and update the metadata
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

        //if the user showed his pieces the board stored it and this is the time to pull this data.
        for (int row = 0; row < otherPieces.length; row++) {
            for (int col = 0; col < otherPieces[row].length; col++) {
                if (otherPieces[row][col] != null && otherPieces[row][col].getPiece() != null) {
                    newBoard.setPiece(row, col, otherPieces[row][col].getPiece(), Board.getOppositeColor(color));
                    invisiblePieceSum = updateSums(otherPieces[row][col].getPiece(), invisiblePieceCountCopy, invisiblePieceSum);
                }
            }
        }

        do {
            //find the piece that has the least frequency
            //e.g. SPY, MARSHAL...
            int lowestFrequencyPiece = -1;
            int minFrequency = Integer.MAX_VALUE;
            for (int pieceNum = 0; pieceNum < invisiblePieceCountCopy.length; pieceNum++) {
                if (invisiblePieceCountCopy[pieceNum] != 0 && invisiblePieceCountCopy[pieceNum] < minFrequency) {
                    minFrequency = invisiblePieceCountCopy[pieceNum];
                    lowestFrequencyPiece = pieceNum;
                }
            }
            //if there is no such one then break from the loop
            if (lowestFrequencyPiece == -1) {
                break;
            }
            Piece pieceToSearch = Piece.fromInteger(lowestFrequencyPiece + 1);

            //find the location that is most likely to be this piece.
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
                //if the piece doesn't have any location that has any chance of it being there then piece a
                //random location and put it there.
                boolean isDone = false;
                for (int row = 0; row < otherPieces.length && !isDone; row++) {
                    for (int col = 0; col < otherPieces[row].length && !isDone; col++) {
                        if (newBoard.isFree(row, col) && otherPieces[row][col] == null) {
                            newBoard.setPiece(row, col, pieceToSearch, Board.getOppositeColor(color));
                            invisiblePieceSum = updateSums(pieceToSearch, invisiblePieceCountCopy, invisiblePieceSum);
                            isDone = true;
                        }
                    }
                }
            } else {
                //if the piece does have a place to be then put it in the board and update the metadata.
                newBoard.setPiece(Row, Col, pieceToSearch, Board.getOppositeColor(color));
                invisiblePieceSum = updateSums(pieceToSearch, invisiblePieceCountCopy, invisiblePieceSum);
            }
        } while (invisiblePieceSum > 0);


//        // this bunch of code represent a different way to think about creating a board.
//        // if in the first part we made the board by checking each piece and then finding the most probable
//        // location for it in here we go throw each piece and find the expected value for it.
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

        //init board variables
        newBoard.initPossibleMoves();
        newBoard.setGameOverFlag("red", true);
        newBoard.setGameOverFlag("blue", true);
        System.out.println(newBoard);

        return newBoard;
    }

}
