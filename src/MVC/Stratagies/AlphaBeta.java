package MVC.Stratagies;

import MVC.Stratagies.BoardComputerView.SpeculationBoard;
import MVC.model.*;

import static java.lang.Math.*;

public class AlphaBeta implements Strategy {
    private static final int maxDepth = 5;
    private String computerColor;
    private SpeculationBoard speculationBoard;
    private String playerColor;

    public AlphaBeta(SpeculationBoard board, String computerColor) {
        this.speculationBoard = board;
        this.computerColor = computerColor;
        this.playerColor = Board.getOppositeColor(computerColor);
    }

    @Override
    public Move chooseMove() {
        //this is the computer's turn
        Board board = speculationBoard.getBoard();
        if (board.isGameOver())
            throw new IllegalStateException("the game cant quite be over...");
        PossibleMoves moves = board.getMoves(computerColor);
        Move bestMove = null;
        int bestScore = Integer.MIN_VALUE;
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        for (Move move : moves) {
            //find the maximum score of the moves.
            Piece p1 = board.getPiece(move.getP1());
            Piece p2 = board.getPiece(move.getP2());
            movePiece(board, move);
            int MoveScore = playerTurn(board, maxDepth - 1, alpha, beta);
            if (MoveScore >= bestScore) {
                bestScore = MoveScore;
                bestMove = move;
            }
            alpha = max(MoveScore, alpha);
            board.undoMove(move.getP1(), move.getP2(), p1, p2, computerColor);
            if(beta <= alpha)
                break;
        }

        return bestMove;
    }


    private int computerTurn(Board board, int Depth, int alpha, int beta) {
        if (Depth == 0 || board.isGameOver())
            return score(board);
        //this is the computer's turn
        PossibleMoves moves = board.getMoves(computerColor);
        int bestScore = Integer.MIN_VALUE;
        for (Move move : moves) {
            //find the maximum score of the moves.
            Piece p1 = board.getPiece(move.getP1());
            Piece p2 = board.getPiece(move.getP2());
            if(p2 == Piece.FLAG || (p2 == Piece.BOMB && p1 == Piece.MINER)){
                movePiece(board, move);
                int MoveScore = playerTurn(board, Depth - 1, alpha, beta);
                bestScore = max(MoveScore, bestScore);
                alpha = max(MoveScore, alpha);
                board.undoMove(move.getP1(), move.getP2(), p1, p2, computerColor);
                if(beta <= alpha)
                    break;
            }
        }
        for (Move move : moves) {
            //find the maximum score of the moves.
            Piece p1 = board.getPiece(move.getP1());
            Piece p2 = board.getPiece(move.getP2());
            if(p2 != Piece.FLAG && !(p2 == Piece.BOMB && p1 == Piece.MINER)){
                movePiece(board, move);
                int MoveScore = playerTurn(board, Depth - 1, alpha, beta);
                bestScore = max(MoveScore, bestScore);
                alpha = max(MoveScore, alpha);
                board.undoMove(move.getP1(), move.getP2(), p1, p2, computerColor);
                if(beta <= alpha)
                    break;
            }
        }
        return bestScore;
    }

    private int playerTurn(Board board, int Depth, int alpha, int beta) {
        if (Depth == 0 || board.isGameOver())
            return score(board);
        //this is the player's turn
        PossibleMoves moves = board.getMoves(playerColor);
        int worstScore = Integer.MAX_VALUE;
        //to find killer moves
        for (Move move : moves) {
            //find the maximum score of the moves.
            Piece p1 = board.getPiece(move.getP1());
            Piece p2 = board.getPiece(move.getP2());
            if(p2 == Piece.FLAG || (p2 == Piece.BOMB && p1 == Piece.MINER)){
                movePiece(board, move);
                int MoveScore = computerTurn(board, Depth - 1, alpha, beta);
                worstScore = min(MoveScore, worstScore);
                beta = min(MoveScore, beta);
                board.undoMove(move.getP1(), move.getP2(), p1, p2, playerColor);
                if(beta <= alpha)
                    break;
            }
        }
        for (Move move : moves) {
            //find the maximum score of the moves.
            Piece p1 = board.getPiece(move.getP1());
            Piece p2 = board.getPiece(move.getP2());
            if(p2 != Piece.FLAG && !(p2 == Piece.BOMB && p1 == Piece.MINER)){
                movePiece(board, move);
                int MoveScore = computerTurn(board, Depth - 1, alpha, beta);
                worstScore = min(MoveScore, worstScore);
                board.undoMove(move.getP1(), move.getP2(), p1, p2, playerColor);
                beta = min(MoveScore, beta);
                if(beta <= alpha)
                    break;
            }
        }
        return worstScore;
    }

    private void movePiece(Board board, Move move) {
        board.moveTo(move.getP1(), move.getP2());
        board.updateMoves(move.getP1());
        board.updateMoves(move.getP2());
    }


    /**
     * Get the score of the board.
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
            boardEvaluation += EvaluateBoard(computerPieces, opponentPieces, board.getMoves(computerColor));
            boardEvaluation -= EvaluateBoard(opponentPieces, computerPieces, board.getMoves(playerColor));
            return boardEvaluation;
        }
        return 0;
    }

    private int EvaluateBoard(Piece[][] attackingPieces, Piece[][] defendingPieces, PossibleMoves attackerMoves) {
        int boardEvaluation = 0;
        //find the other player's flag piece
        int flagRow = -1, flagCol = -1;
        for (int row = 0; row < defendingPieces.length; row++) {
            for (int col = 0; col < defendingPieces[row].length; col++) {
                if(defendingPieces[row][col] == Piece.FLAG){
                    flagRow = row;
                    flagCol = col;
                    break;
                }
            }
            if(flagRow != -1)
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
//        for (Move move : attackerMoves) {
//            Piece defendingPiece = defendingPieces[move.getP2().getRow()][move.getP2().getCol()];
//            Piece attackingPiece = attackingPieces[move.getP1().getRow()][move.getP1().getCol()];
//            if(defendingPiece == null)
//                continue;
//            if (attackingPiece.Attack(defendingPiece) == attackingPiece) {
//                if (defendingPiece == Piece.FLAG) {
//                    boardEvaluation += 300;
//                } else if (defendingPiece == Piece.SPY || defendingPiece == Piece.MARSHAL) {
//                    boardEvaluation += 150;
//                } else if (defendingPiece == Piece.MINER)
//                    boardEvaluation += 60;
//                else
//                    boardEvaluation += defendingPiece.PieceNumber * 10;
//            }
//        }
        return boardEvaluation;
    }
}
