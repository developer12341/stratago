package MVC.Stratagies;

import MVC.Stratagies.BoardComputerView.SpeculationBoard;
import MVC.model.*;

import java.util.List;

import static java.lang.Math.*;

public class ABOptimized implements Strategy {
    private static final int[] normalNumberOfPieces = {1, 8, 5, 4, 4, 4, 3, 2, 1, 1, 6, 1};
    private static final int MAX_VALUE = 99999;
    private static final int MIN_VALUE = -99999;


    private static final int maxDepth = 7;
    private String computerColor;
    private SpeculationBoard speculationBoard;
    private String playerColor;

    /**
     * flagRow and flagCol are the coordinates of the flag
     * they are needed for board evaluation, so I thought it's better to calculate them here
     */
    private Point flagLocation;

    public ABOptimized(SpeculationBoard board, String computerColor) {
        this.speculationBoard = board;
        this.computerColor = computerColor;
        this.playerColor = Board.getOppositeColor(computerColor);

        Piece[][] ComputerPieces = board.getComputerPieces();
        for (int row = 0; row < ComputerPieces.length; row++) {
            for (int col = 0; col < ComputerPieces[row].length; col++) {
                if (ComputerPieces[row][col] == Piece.FLAG) {
                    flagLocation = Point.create(row, col);
                    break;
                }
            }
            if (flagLocation != null)
                break;
        }
    }


    /**
     * this function is the first iteration of the loop.
     * it is the computer's turn, and it is trying to find the best move to make
     *
     * @return the best move.
     */
    @Override
    public Move chooseMove() {
        //this is the computer's turn
        Board board = speculationBoard.getBoard();
        if (board.isGameOver())
            throw new IllegalStateException("the game cant quite be over...");
        try {
            int[] moves = board.getOptimizedMoves(computerColor);

            int bestMove = -1;
            int bestScore = Integer.MIN_VALUE;
            int alpha = Integer.MIN_VALUE;
            int beta = Integer.MAX_VALUE;
            for (int move : moves) {
                //find the maximum score of the moves.
                Piece p1 = board.getPiece(MoveLib.unpackRowFrom(move), MoveLib.unpackColFrom(move));
                Piece p2 = board.getPiece(MoveLib.unpackRowTo(move), MoveLib.unpackColTo(move));
                board.OptimizedMoveTo(move);


                int MoveScore = playerTurn(board, maxDepth - 1, alpha, beta);
                if (MoveScore >= bestScore) {
                    bestScore = MoveScore;
                    bestMove = move;
                }
                alpha = max(MoveScore, alpha);
                board.OptimizedUndoMove(move, p1, p2, computerColor);
                if (beta <= alpha)
                    break;
            }

            return MoveLib.convertToMove(bestMove);
        } catch (Exception e) {
            List<Move> moves = board.getMoves(computerColor);
            Move bestMove = null;
            int bestScore = Integer.MIN_VALUE;
            int alpha = Integer.MIN_VALUE;
            int beta = Integer.MAX_VALUE;
            for (Move move : moves) {
                //find the maximum score of the moves.
                Piece p1 = board.getPiece(move.getP1());
                Piece p2 = board.getPiece(move.getP2());
                board.moveTo(move.getP1(), move.getP2());
                int MoveScore = playerTurn(board, maxDepth - 1, alpha, beta);
                if (MoveScore >= bestScore) {
                    bestScore = MoveScore;
                    bestMove = move;
                }
                alpha = max(MoveScore, alpha);
                board.undoMove(move.getP1(), move.getP2(), p1, p2, computerColor);
                if (beta <= alpha)
                    break;
            }

            return bestMove;
        }
    }


    /**
     * calculate the best move the opponent can make.
     *
     * @param board the board
     * @param Depth the max depth the program can go to
     * @param alpha the maximum score
     * @param beta  the minimum score
     * @return the score of the best move the player can make
     */
    private int playerTurn(Board board, int Depth, int alpha, int beta) {
        if (Depth == 0 || board.isGameOver())
            return score(board);
        try{

            int[] moves = board.getOptimizedMoves(playerColor);
            int worstScore = Integer.MAX_VALUE;
            for (int move : moves) {
                //find the maximum score of the moves.
                Piece p1 = board.getPiece(MoveLib.unpackRowFrom(move), MoveLib.unpackColFrom(move));
                Piece p2 = board.getPiece(MoveLib.unpackRowTo(move), MoveLib.unpackColTo(move));
                board.OptimizedMoveTo(move);
                int MoveScore = computerTurn(board, Depth - 1, alpha, beta);

                worstScore = min(MoveScore, worstScore);
                board.OptimizedUndoMove(move, p1, p2, playerColor);
                beta = min(MoveScore, beta);
                if (beta <= alpha)
                    break;
            }

            return worstScore;
        }catch (Exception e) {

            List<Move> moves = board.getMoves(playerColor);
            int worstScore = Integer.MAX_VALUE;
            //loop throw the other moves
            for (Move move : moves) {
                //find the maximum score of the moves.
                Piece p1 = board.getPiece(move.getP1());
                Piece p2 = board.getPiece(move.getP2());
                board.moveTo(move.getP1(), move.getP2());
                int MoveScore = computerTurn(board, Depth - 1, alpha, beta);
                worstScore = min(MoveScore, worstScore);
                board.undoMove(move.getP1(), move.getP2(), p1, p2, playerColor);
                beta = min(MoveScore, beta);
                if (beta <= alpha)
                    break;
            }
            return worstScore;
        }
    }


    /**
     * calculate the best move the computer can make.
     *
     * @param board the board
     * @param Depth the max depth the program can go to
     * @param alpha the maximum score
     * @param beta  the minimum score
     * @return the score of the best move the computer can make
     */
    private int computerTurn(Board board, int Depth, int alpha, int beta) {
        if (Depth == 0 || board.isGameOver())
            return score(board);

        try{

            int[] moves = board.getOptimizedMoves(computerColor);
            int bestScore = Integer.MIN_VALUE;
            for (int move : moves) {
                //find the maximum score of the moves.
                Piece p1 = board.getPiece(MoveLib.unpackRowFrom(move), MoveLib.unpackColFrom(move));
                Piece p2 = board.getPiece(MoveLib.unpackRowTo(move), MoveLib.unpackColTo(move));
                board.OptimizedMoveTo(move);
                int MoveScore = playerTurn(board, Depth - 1, alpha, beta);

                bestScore = max(MoveScore, bestScore);
                board.OptimizedUndoMove(move, p1, p2, computerColor);

                alpha = max(MoveScore, alpha);
                if (beta <= alpha)
                    break;
            }

            return bestScore;
        } catch (Exception e) {

            //this is the computer's turn
            List<Move> moves = board.getMoves(computerColor);
            int bestScore = Integer.MIN_VALUE;
            //loop throw the other moves
            for (Move move : moves) {
                //find the maximum score of the moves.
                Piece p1 = board.getPiece(move.getP1());
                Piece p2 = board.getPiece(move.getP2());
                board.moveTo(move.getP1(), move.getP2());
                int MoveScore = playerTurn(board, Depth - 1, alpha, beta);
                bestScore = max(MoveScore, bestScore);
                alpha = max(MoveScore, alpha);
                board.undoMove(move.getP1(), move.getP2(), p1, p2, computerColor);
                if (beta <= alpha)
                    break;
            }
            return bestScore;
        }
    }

    /**
     * Get the score of the board. if the game is over then the score is -infinity or +infinity
     * depending on whom won
     *
     * @return the score of the board
     */
    private int score(Board board) {
        if (computerColor == null) {
            throw new IllegalArgumentException("Player must be red or blue.");
        }

        if (board.isGameOver()) {
            if (computerColor.equals(board.getWinner()))
                return Integer.MAX_VALUE;
            else if (playerColor.equals(board.getWinner()))
                return Integer.MIN_VALUE;
        } else {
            return func(board);
        }
        return 0;
    }

    int func(Board board) {
        //count the amount of pieces the player has
        int[] HumanPiecesCount = new int[12];
        int[] ComputerPiecesCount = new int[12];
        Piece[][] HumanPieces = board.getPieces(playerColor);
        Piece[][] ComputerPieces = board.getPieces(computerColor);
        for (int row = 0; row < HumanPieces.length; row++) {
            for (int col = 0; col < HumanPieces[row].length; col++) {
                if (HumanPieces[row][col] != null) {
                    HumanPiecesCount[HumanPieces[row][col].PieceNumber - 1]++;
                }
                if (ComputerPieces[row][col] != null) {
                    ComputerPiecesCount[ComputerPieces[row][col].PieceNumber - 1]++;
                }
            }
        }
        int score = 0;
        for (int i = 0; i < HumanPiecesCount.length; i++) {
            score += switch (i) {
                case 2 -> (normalNumberOfPieces[i] - HumanPiecesCount[i]) * 200;
                case 10 -> (normalNumberOfPieces[i] - HumanPiecesCount[i]) * 500;
                default -> (normalNumberOfPieces[i] - HumanPiecesCount[i]) * (i + 1) * 50;
            };
        }


        //add points for moving to the end of the board
        for (int row = 0; row < ComputerPieces.length; row++) {
            for (int col = 0; col < ComputerPieces[row].length; col++) {
                if (ComputerPieces[row][col] != null) {
                    score += (row + 1) * 500;
                }
            }
        }
//        for (int i = 0; i < ComputerPiecesCount.length; i++) {
//            score -= switch (i) {
//                case 2 -> (normalNumberOfPieces[i] - ComputerPiecesCount[i]) * 200;
//                case 10 -> (normalNumberOfPieces[i] - ComputerPiecesCount[i]) * 500;
//                default -> (normalNumberOfPieces[i] - ComputerPiecesCount[i]) * i * 50;
//            };
//        }
//        //find the opponent's flag
//        int opponentFlagRow = -1;
//        int opponentFlagCol = -1;
//        for (int row = 0; row < Board.size; row++) {
//            for (int col = 0; col < Board.size; col++) {
//                if (HumanPieces[row][col] == Piece.FLAG) {
//                    opponentFlagRow = row;
//                    opponentFlagCol = col;
//                    break;
//                }
//            }
//            //if the opponent's flag is found, break
//            if (opponentFlagRow != -1)
//                break;
//        }
//
//        //find the distance between the flag and the closest Computer piece
//        int minDistance = Integer.MAX_VALUE;
//        for (int row = 0; row < Board.size / 2; row++) {
//            for (int col = 0; col < Board.size; col++) {
//                if (ComputerPieces[row][col] != null) {
//                    int distance = Math.abs(opponentFlagRow - row) + Math.abs(opponentFlagCol - col);
//                    minDistance = min(distance, minDistance);
//                }
//            }
//        }
//        try {
//            score += MAX_VALUE / (minDistance * minDistance);
//        } catch (ArithmeticException e) {
//            System.out.println(board);
//            throw e;
//        }
        return score;


//        int score = 0;
//        //has the opponent started searching for the flag?
//        int closestDistanceToFlag = 0;
//        Piece[][] ComputerPieces = board.getPieces(computerColor);
//        Piece[][] opponentPieces = board.getPieces(playerColor);
//        for (int row = 0; row < opponentPieces.length / 2; row++) {
//            for (int col = 0; col < opponentPieces[row].length; col++) {
//                if (opponentPieces[row][col] != null) {
//                    //if the opponent has a piece very close to the flag it is a VERY bad board
//                    score += MIN_VALUE / (flagLocation.distance(row, col) * flagLocation.distance(row, col));
//                }
//            }
//        }
//
//
//        for (int row = opponentPieces.length - 1; row >= opponentPieces.length / 2; row--) {
//            for (int col = 0; col < opponentPieces[row].length; col++) {
//                if (ComputerPieces[row][col] != null) {
//                    //if the opponent has a piece very close to the flag it is a VERY bad board
//                    score += MAX_VALUE / (flagLocation.distance(row, col) * flagLocation.distance(row, col));
//                }
//            }
//        }
//
//
//        //if the opponent has the MARSHAL, but he ate my spy, he is a very bad board
//        boolean doesOpponentHaveMarshal = false;
//        boolean doesOpponentHaveSpy = false;
//        boolean doesComputerHaveMarshal = false;
//        boolean doesComputerHaveSpy = false;
//        for (int i = 0; i < ComputerPieces.length; i++) {
//            for (int j = 0; j < ComputerPieces[i].length; j++) {
//                if (ComputerPieces[i][j] == Piece.SPY) {
//                    doesComputerHaveSpy = true;
//                }
//                if (ComputerPieces[i][j] == Piece.MARSHAL) {
//                    doesComputerHaveMarshal = true;
//                }
//                if (opponentPieces[i][j] == Piece.SPY) {
//                    doesOpponentHaveSpy = true;
//                }
//                if (opponentPieces[i][j] == Piece.MARSHAL) {
//                    doesOpponentHaveMarshal = true;
//                }
//            }
//        }
//        if (doesOpponentHaveMarshal && !doesComputerHaveSpy && !doesComputerHaveMarshal) {
//            score += MIN_VALUE / 2;// find out if i need to divide by the distance
//        }
//        if (doesComputerHaveMarshal && !doesOpponentHaveMarshal && !doesOpponentHaveSpy) {
//            score += MAX_VALUE / 2;
//        }
//
//
//        // count the amount of pieces the opponent ate me
//        int[] myPieceCount = new int[12];
//        for (Piece[] computerPiece : ComputerPieces) {
//            for (Piece piece : computerPiece) {
//                if (piece != null) {
//                    myPieceCount[piece.PieceNumber - 1]++;
//                }
//            }
//        }
//        //loop through my piece count
//        for (int i = 0; i < myPieceCount.length; i++) {
//            if (myPieceCount[i] != normalNumberOfPieces[i]) {
//                score -= myPieceCount[i] * MAX_VALUE / normalNumberOfPieces[i];
//            }
//        }
//        return score;
    }

    /**
     * I choose to evaluate the board by the distance from the flag
     *
     * @param attackingPieces the pieces of the attacker
     * @param defendingPieces the pieces of the defender
     * @return a number for the current position of the board
     */
    private int EvaluateBoard(Piece[][] attackingPieces, Piece[][] defendingPieces) {


        int boardEvaluation = 0;
        //find the other player's flag piece
        int flagRow = -1, flagCol = -1;
        for (int row = 0; row < defendingPieces.length; row++) {
            for (int col = 0; col < defendingPieces[row].length; col++) {
                if (defendingPieces[row][col] == Piece.FLAG) {
                    flagRow = row;
                    flagCol = col;
                    break;
                }
            }
            if (flagRow != -1)
                break;
        }
        //then add points for every piece that is close to the flag proportionally to the distance squared
        for (int row = 0; row < attackingPieces.length; row++) {
            for (int col = 0; col < attackingPieces[row].length; col++) {
                if (attackingPieces[row][col] != null) {
                    int distance = abs(flagRow - row) + abs(flagCol - col);
                    boardEvaluation += 1000 / distance * distance;
                }
            }
        }
        return boardEvaluation;
    }

}
