package MVC.Stratagies;

import MVC.Stratagies.BoardComputerView.SpeculationBoard;
import MVC.model.Board;
import MVC.model.Move;
import MVC.model.Piece;

import java.util.List;

import static java.lang.Math.*;

/**
 * this class is the alpha beta strategy for choosing moves.
 */
public class AlphaBeta implements Strategy {
    private static final int maxDepth = 9;
    private String computerColor;
    private SpeculationBoard speculationBoard;
    private String playerColor;

    public AlphaBeta(SpeculationBoard board, String computerColor) {
        this.speculationBoard = board;
        this.computerColor = computerColor;
        this.playerColor = Board.getOppositeColor(computerColor);
    }

    /**
     * this function is the first iteration of the loop.
     * it is the computer's turn, and it is trying to find the best move to make
     * @return the best move.
     */
    @Override
    public Move chooseMove() {
        //this is the computer's turn
        Board board = speculationBoard.getBoard();
        if (board.isGameOver())
            throw new IllegalStateException("the game cant quite be over...");
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


    /**
     * calculate the best move the computer can make.
     * @param board the board
     * @param Depth the max depth the program can go to
     * @param alpha the maximum score
     * @param beta the minimum score
     * @return the score of the best move the computer can make
     */
    private int computerTurn(Board board, int Depth, int alpha, int beta) {
        if (Depth == 0 || board.isGameOver())
            return score(board);
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

    /**
     * calculate the best move the opponent can make.
     * @param board the board
     * @param Depth the max depth the program can go to
     * @param alpha the maximum score
     * @param beta the minimum score
     * @return the score of the best move the player can make
     */
    private int playerTurn(Board board, int Depth, int alpha, int beta) {
        if (Depth == 0 || board.isGameOver())
            return score(board);
        //this is the player's turn
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
            int boardEvaluation = 0;
            Piece[][] computerPieces = board.getPieces(computerColor);
            Piece[][] opponentPieces = board.getPieces(playerColor);

            // I wanted the AI to consider the other side moves too, so
            // I added his board score but subtracted the opponent's board score.
            boardEvaluation += EvaluateBoard(computerPieces, opponentPieces);
            boardEvaluation -= EvaluateBoard(opponentPieces, computerPieces);
            return boardEvaluation;
        }
        return 0;
    }

    /**
     * I choose to evaluate the board by the distance from the flag
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
