package MVC.Stratagies;

import MVC.Stratagies.BoardComputerView.SpeculationBoard;
import MVC.model.Board;
import MVC.model.Move;
import MVC.model.Piece;
import MVC.model.PossibleMoves;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class minmax implements Strategy {
    private final int maxDepth;
    private String computerColor;
    private SpeculationBoard speculationBoard;

    public minmax(SpeculationBoard board, int maxDepth, String computerColor) {
        this.speculationBoard = board;
        this.maxDepth = maxDepth;

        this.computerColor = computerColor;
    }

    @Override
    public void HumanMove(Move move, Piece attackingPiece) {
        speculationBoard.otherPlayerMove(move.getP1(), move.getP2(), attackingPiece);
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
        for (Move move : moves) {
            System.out.println(move);
            //find the maximum score of the moves.
            Piece p1 = board.getPiece(move.getP1());
            Piece p2 = board.getPiece(move.getP2());
            movePiece(board, move);
            int MoveScore = playerTurn(board, maxDepth - 1);
            System.out.println(MoveScore);
            if (MoveScore >= bestScore) {
                bestScore = MoveScore;
                bestMove = move;
            }
            board.undoMove(move.getP1(), move.getP2(), p1, p2, computerColor);
        }

        return bestMove;
    }


    private int computerTurn(Board board, int Depth) {
        if (Depth == 0 || board.isGameOver())
            return score(board);
        //this is the computer's turn
        PossibleMoves moves = board.getMoves(computerColor);
        int bestScore = Integer.MIN_VALUE;
        for (Move move : moves) {
            //find the maximum score of the moves.
            Piece p1 = board.getPiece(move.getP1());
            Piece p2 = board.getPiece(move.getP2());
            movePiece(board, move);
            int MoveScore = playerTurn(board, Depth - 1);
            bestScore = max(MoveScore, bestScore);
            board.undoMove(move.getP1(), move.getP2(), p1, p2, computerColor);
        }
        return bestScore;
    }

    private int playerTurn(Board board, int Depth) {
        if (Depth == 0 || board.isGameOver())
            return score(board);
        //this is the player's turn
        PossibleMoves moves = board.getMoves(board.getOppositeColor(computerColor));
        int worstScore = Integer.MAX_VALUE;
        for (Move move : moves) {
            //find the maximum score of the moves.
            Piece p1 = board.getPiece(move.getP1());
            Piece p2 = board.getPiece(move.getP2());
            movePiece(board, move);
            int MoveScore = computerTurn(board, Depth - 1);
            worstScore = min(MoveScore, worstScore);
            board.undoMove(move.getP1(), move.getP2(), p1, p2, board.getOppositeColor(computerColor));
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

        String opponent = board.getOppositeColor(computerColor);
        if (board.isGameOver()) {
            if (computerColor.equals(board.getWinner()))
                return Integer.MAX_VALUE;
            else if (opponent.equals(board.getWinner()))
                return Integer.MIN_VALUE;
        } else {
            int boardEvaluation = 0;
            //todo: evaluate board.
            Piece[][] computerPieces = board.getPieces(computerColor);
            Piece[][] opponentPieces = board.getPieces(opponent);
            boardEvaluation += EvaluateBoard(computerPieces, opponentPieces, board.getMoves(computerColor));
            boardEvaluation -= EvaluateBoard(opponentPieces, computerPieces, board.getMoves(opponent));
            return boardEvaluation;
        }
        return 0;
    }

    private int EvaluateBoard(Piece[][] attackingPieces, Piece[][] defendingPieces, PossibleMoves attackerMoves) {
        int boardEvaluation = 0;
        for (Move move : attackerMoves) {
            Piece defendingPiece = defendingPieces[move.getP2().getRow()][move.getP2().getCol()];
            Piece attackingPiece = attackingPieces[move.getP1().getRow()][move.getP1().getCol()];
            if(defendingPiece == null)
                continue;
            if (attackingPiece.Attack(defendingPiece) == attackingPiece) {
                if (defendingPiece == Piece.FLAG) {
                    boardEvaluation += 300;
                } else if (defendingPiece == Piece.SPY || defendingPiece == Piece.MARSHAL) {
                    boardEvaluation += 150;
                } else if (defendingPiece == Piece.MINER)
                    boardEvaluation += 60;
                else
                    boardEvaluation += defendingPiece.PieceNumber * 10;
            }
        }
        return boardEvaluation;
    }
}
