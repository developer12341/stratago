package MVC.Stratagies.mcts;

import MVC.Stratagies.BoardComputerView.SpeculationBoard;
import MVC.Stratagies.Strategy;
import MVC.model.Board;
import MVC.model.Move;
import MVC.model.Piece;

import java.util.Collections;
import java.util.Comparator;
import java.util.Stack;

public class MCTS implements Strategy {
    private static final long MAX_TRIAL = 3000;
    private String computerColor;
    private SpeculationBoard speculationBoard;

    public MCTS(SpeculationBoard board, String color) {
        this.computerColor = color;
        this.speculationBoard = board;
    }

    public static double uctValue(
            int totalVisit, double nodeWinScore, int nodeVisit) {
        if (nodeVisit == 0) {
            return Integer.MAX_VALUE;
        }
        return nodeWinScore / (double) nodeVisit
                + 1.41 * Math.sqrt(Math.log(totalVisit) / (double) nodeVisit);
    }

    public static Node findBestNodeWithUCT(Node node) {
        int parentVisit = node.getVisits();
        return Collections.max(
                node.getChildren(),
                Comparator.comparing(c -> uctValue(parentVisit, c.getScore(), c.getVisits())));
    }

    @Override
    public Move chooseMove() {
        //todo: implement.
        //choose a move from board.getPossibleMoves() with monte carlo tree search


        Board board = speculationBoard.getBoard();

        Node tree = new Node();

        Stack<Piece> gameHistory = new Stack<>();
        Stack<Move> moveHistory = new Stack<>();
        for (long trialNum = 0L; trialNum < MAX_TRIAL; trialNum++) {

            //SELECT
            Node selectedNode = selectPromisingNode(tree);

            //EXPAND
            if (selectedNode.getMove() != null) {
                Move move = selectedNode.getMove();
                Piece p1 = board.getPiece(move.getP1());
                Piece p2 = board.getPiece(move.getP2());
                gameHistory.push(p1);
                gameHistory.push(p2);
                moveHistory.push(move);
                movePiece(board, move);
            }
            selectedNode = expandNodeAndReturnRandom(board, selectedNode, board.getPlayerTurn());

            //SIMULATE
            String simulationResult = simulateGame(board, selectedNode, board.getPlayerTurn());

            //PROPAGATE
            backPropagation(board, simulationResult, selectedNode);
            while (!gameHistory.isEmpty()) {
                Piece p2 = gameHistory.pop();
                Piece p1 = gameHistory.pop();
                board.undoMove(moveHistory.pop(), p1, p2, board.getOppositeColor(board.getPlayerTurn()));
            }


        }

        Node best = tree.getChildWithMaxScore();

        return best.getMove();
    }

    private void backPropagation(Board board, String simulationResult, Node node) {
        while (node != null) {
            node.incrementVisits();
            if (board.getOppositeColor(board.getPlayerTurn()).equals(simulationResult)) {
                node.incrementScore();
            }
            node = node.getParent();
        }
    }

    private String simulateGame(Board board, Node selectedNode, String playerTurn) {
        Stack<Piece> gameHistory = new Stack<>();
        Stack<Move> moveHistory = new Stack<>();
        if (board.isGameOver())
            return board.getWinner();
        Move move = selectedNode.getMove();
        if (move != null) {
            Piece p1 = board.getPiece(move.getP1());
            Piece p2 = board.getPiece(move.getP2());
            movePiece(board, move);
            gameHistory.push(p1);
            gameHistory.push(p2);
            moveHistory.push(move);
        }

        while (!board.isGameOver()) {
            move = board.getRandomMove();
            Piece p1 = board.getPiece(move.getP1());
            Piece p2 = board.getPiece(move.getP2());
            movePiece(board, move);
            gameHistory.push(p1);
            gameHistory.push(p2);
            moveHistory.push(move);
        }
        String winner = board.getWinner();
        while (!gameHistory.isEmpty()) {
            Piece p2 = gameHistory.pop();
            Piece p1 = gameHistory.pop();
            board.undoMove(moveHistory.pop(), p1, p2, board.getOppositeColor(board.getPlayerTurn()));
        }
        return winner;
    }

    private Node expandNodeAndReturnRandom(Board board, Node selectedNode, String turn) {
        for (Move move : board.getMoves(turn)) {
            Node child = new Node(move);
            child.setParent(selectedNode);
            selectedNode.addChild(child);
        }

        int random = Board.RANDOM_GENERATOR.nextInt(selectedNode.getChildren().size());

        return selectedNode.getChild(random);
    }

    private void movePiece(Board board, Move move) {
        board.moveTo(move.getP1(), move.getP2());
        board.updateMoves(move.getP1());
        board.updateMoves(move.getP2());
    }

    /**
     * this function returns the most promising node.
     * if node is a leaf then return the node.
     *
     * @param node some node in the tree
     * @return the most promising node
     */
    private Node selectPromisingNode(Node node) {
        if (node.getChildren().size() != 0) {
            return findBestNodeWithUCT(node);
        }
        return node;
    }

    @Override
    public void HumanMove(Move move, Piece attackingPiece) {
        speculationBoard.otherPlayerMove(move.getP1(), move.getP2(), attackingPiece);
    }

}
