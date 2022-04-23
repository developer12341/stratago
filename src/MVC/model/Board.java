package MVC.model;

import generator.BoardGenerator;

import java.util.List;
import java.util.Random;

import static MVC.model.Piece.FLAG;
import static MVC.model.Piece.SCOUT;

/**
 * this class encapsulate all the data about the board
 */
public class Board implements Cloneable {
    //all of these are variables that needed only for reading purposes, so they are attributes of Board
    public static final int size = 10;
    public static final Piece[][][] defaultBoards = BoardGenerator.getDefaultBoards();
    public static final Random RANDOM_GENERATOR = new Random();
    public static final MapMask mapMask = new MapMask(Board.size, Board.size);


    private PossibleMoves possibleRedMoves;
    private PossibleMoves possibleBlueMoves;
    private Piece[][] redPieces;
    private Piece[][] bluePieces;
    private boolean redFlagInBoard;
    private boolean blueFlagInBoard;
    private String playerTurn;


    public Board() {
        //init the boards
        redPieces = new Piece[Board.size][Board.size];
        bluePieces = new Piece[Board.size][Board.size];
        //fill them
        setRandomStartingBoard("red");
        setRandomStartingBoard("blue");

        //init the rest of the variables.
        possibleRedMoves = new PossibleMoves();
        possibleBlueMoves = new PossibleMoves();
        redFlagInBoard = true;
        blueFlagInBoard = true;
        playerTurn = "red";
    }

    /**
     * there are two colors only in the board.
     *
     * @param color the color of the player
     * @return the color of the opposite player
     */
    public static String getOppositeColor(String color) {
        if (color == null)
            return null;
        if ("red".equals(color))
            return "blue";
        if ("blue".equals(color))
            return "red";
        return null;
    }

    /**
     * @return true if one of the flags is missing or if one of the players doesn't have any more moves
     */
    public boolean isGameOver() {
        return !(redFlagInBoard && blueFlagInBoard) || possibleBlueMoves.isEmpty() || possibleRedMoves.isEmpty();
    }

    private void setRandomStartingBoard(String playerColor) throws IllegalArgumentException {
        //get a random starting board
        Piece[][] startingBoard = defaultBoards[(int) (Math.random() * (defaultBoards.length))];
        Piece[][] playerPieces = getPieces(playerColor);


        //copy it to the appropriated place
        if (playerColor.equals("red")) {
            for (int row = 0; row < startingBoard.length; row++) {
                System.arraycopy(startingBoard[row],
                        0,
                        playerPieces[bluePieces.length - (startingBoard.length - row)],
                        0,
                        startingBoard[row].length);
            }
        } else if (playerColor.equals("blue")) {
            for (int row = 0; row < startingBoard.length; row++) {
                System.arraycopy(startingBoard[row],
                        0,
                        playerPieces[startingBoard.length - 1 - row],
                        0,
                        startingBoard[row].length);
            }
        }
    }

    /**
     * @param p a point in the board
     * @return the color of the piece in the board
     */
    public String getColor(Point p) {
        if (p == null)
            return null;
        if (redPieces[p.getRow()][p.getCol()] != null)
            return "red";
        else if (bluePieces[p.getRow()][p.getCol()] != null)
            return "blue";
        return null;
    }

    private void switchTurn() {
        playerTurn = getOppositeColor(playerTurn);
    }

    public Piece[][] getPieces(String playerColor) {
        if ("red".equals(playerColor))
            return redPieces;
        if ("blue".equals(playerColor))
            return bluePieces;
        throw new IllegalArgumentException("there is no " + playerColor + " player!");
    }

    private Piece[][] getOppositePieces(String playerColor) throws IllegalArgumentException {
        if (playerColor.equals("red"))
            return bluePieces;
        if (playerColor.equals("blue"))
            return redPieces;
        throw new IllegalArgumentException("there is no " + playerColor + " player!");
    }

    /**
     * at the start of the game the player can switch between the pieces
     *
     * @param p1 the location of the first piece
     * @param p2 the location of the second piece
     * @return true if the switch was successful
     */
    public boolean switchPieces(Point p1, Point p2) {
        String p1Color = getColor(p1);
        String p2Color = getColor(p2);
        if (p1Color == null || p2Color == null)
            return false;
        if (!p1Color.equals(p2Color))
            return false;
        Piece[][] pieces = getPieces(p1Color);

        //swapping the pieces
        Piece temp = pieces[p2.getRow()][p2.getCol()];
        pieces[p2.getRow()][p2.getCol()] = pieces[p1.getRow()][p1.getCol()];
        pieces[p1.getRow()][p1.getCol()] = temp;
        return true;
    }

    /**
     * move a piece from p1 to p2
     *
     * @param p1 the piece to move
     * @param p2 the destination
     * @return the winning piece.
     */
    public Piece moveTo(Point p1, Point p2) {
        String p1Color = getColor(p1);
        String p2Color = getColor(p2);
        if (isFree(p1))
            throw new IllegalArgumentException(p1 + " doesn't have a piece");
        if (p1Color.equals(p2Color))
            throw new IllegalArgumentException("the piece you are trying to move has the same color...");
        if (p2Color == null)
            p2Color = p1Color;
        Piece[][] player1Pieces = getPieces(p1Color);
        Piece[][] player2Pieces = getPieces(p2Color);

        if (player2Pieces[p2.getRow()][p2.getCol()] == FLAG)
            setGameOverFlag(p2Color, false);


        Piece winner = player1Pieces[p1.getRow()][p1.getCol()].Attack(
                player2Pieces[p2.getRow()][p2.getCol()]);
        switchTurn();
        if (winner == null) {
            player1Pieces[p1.getRow()][p1.getCol()] = null;
            player2Pieces[p2.getRow()][p2.getCol()] = null;
        } else if (winner == player1Pieces[p1.getRow()][p1.getCol()]) {
            player1Pieces[p1.getRow()][p1.getCol()] = null;
            player2Pieces[p2.getRow()][p2.getCol()] = null;
            player1Pieces[p2.getRow()][p2.getCol()] = winner;
        } else {
            player1Pieces[p1.getRow()][p1.getCol()] = null;
        }
        return winner;
    }

    public void setGameOverFlag(String color, Boolean value) {
        if (color.equals("red")) {
            redFlagInBoard = value;
            return;
        } else if (color.equals("blue")) {
            blueFlagInBoard = value;
            return;
        }
        throw new IllegalArgumentException("there is no " + color + " player!");
    }

    /**
     * this function update the moves at a point, and it updates the not correct moves at the points
     * in all directions
     *
     * @param p a point in the board
     */
    public void updateMoves(Point p) {
        clearMoves(p);
        if (!isFree(p))
            addMoves(p.getRow(), p.getCol(), getColor(p));
        for (int row = p.getRow() + 1; row < bluePieces.length; row++) {
            if (!isFree(row, p.getCol())) {
                Point point = Point.create(row, p.getCol());
                if (isValid(point)) {
                    clearMoves(point);
                    addMoves(row, point.getCol(), getColor(point));
                }
                break;
            }
        }
        for (int row = p.getRow() - 1; row >= 0; row--) {
            if (!isFree(row, p.getCol())) {
                Point point = Point.create(row, p.getCol());
                if (isValid(point)) {
                    clearMoves(point);
                    addMoves(row, point.getCol(), getColor(point));
                }
                break;
            }
        }
        for (int col = p.getCol() + 1; col < bluePieces[0].length; col++) {
            if (!isFree(p.getRow(), col)) {
                Point point = Point.create(p.getRow(), col);
                if (isValid(point)) {
                    clearMoves(point);
                    addMoves(point.getRow(), col, getColor(point));
                }
                break;
            }
        }
        for (int col = p.getCol() - 1; col >= 0; col--) {
            if (!isFree(p.getRow(), col)) {
                Point point = Point.create(p.getRow(), col);
                if (isValid(point)) {
                    clearMoves(point);
                    addMoves(point.getRow(), col, getColor(point));
                }
                break;
            }
        }
    }

    /**
     * @param p1 the starting point
     * @param p2 the destination
     * @return true if it is a valid move.
     */
    public boolean isPossibleMove(Point p1, Point p2) {
        PossibleMoves moves = getMoves(getColor(p1));
        return moves.contains(p1, p2);
    }

    public PossibleMoves getMoves(String color) {
        if ("red".equals(color))
            return possibleRedMoves;
        if ("blue".equals(color))
            return possibleBlueMoves;
        throw new IllegalArgumentException("there is no " + color + "player");
    }

    public List<Point> getMoves(Point p) {
        return getMoves(getColor(p)).getMoves(p);
    }

    /**
     * @param p a point
     * @return true if the point has any legal moves
     */
    public boolean doesHaveMoves(Point p) {
        String color = getColor(p);
        if (color == null)
            return false;
        return getMoves(color).contains(p);
    }

    /**
     * clear the moves at a point.
     */
    private void clearMoves(Point p) {
        possibleRedMoves.clear(p);
        possibleBlueMoves.clear(p);
    }

    public boolean isFree(Point p) {
        return isFree(p.getRow(), p.getCol());
    }

    public boolean isFree(int row, int col) {
        return isValid(row, col) && bluePieces[row][col] == null && redPieces[row][col] == null;
    }

    /**
     * this function add all the possible moves to a point
     *
     * @param row   row in the board
     * @param col   column in the board
     * @param color the color of the pieces
     */
    private void addMoves(int row, int col, String color) {
        if (!isValid(row, col))
            throw new IllegalArgumentException("the point is out of bounds");
        Piece[][] PlayerPieces = getPieces(color);
        Piece[][] OppositePieces = getOppositePieces(color);

        PossibleMoves moves = getMoves(color);
        Piece p = PlayerPieces[row][col];
        Point p1 = Point.create(row, col);
        if (!p.isMovable())
            return;
        if (p == SCOUT) {
            //if the piece is a scout then it has the ability to move in every direction
            for (int newRow = row - 1; isValid(newRow, col) &&
                    PlayerPieces[newRow][col] == null; newRow--) {
                moves.addMove(p1, Point.create(newRow, col));
                if (OppositePieces[newRow][col] != null)
                    break;
            }
            for (int newCol = col - 1; isValid(row, newCol) &&
                    PlayerPieces[row][newCol] == null; newCol--) {
                moves.addMove(p1, Point.create(row, newCol));
                if (OppositePieces[row][newCol] != null)
                    break;
            }
            for (int newCol = col + 1; isValid(row, newCol) &&
                    PlayerPieces[row][newCol] == null; newCol++) {
                moves.addMove(p1, Point.create(row, newCol));
                if (OppositePieces[row][newCol] != null)
                    break;
            }
            for (int newRow = row + 1; isValid(newRow, col) &&
                    PlayerPieces[newRow][col] == null; newRow++) {
                moves.addMove(p1, Point.create(newRow, col));
                if (OppositePieces[newRow][col] != null)
                    break;
            }
        } else {
            if (isValid(row - 1, col) && PlayerPieces[row - 1][col] == null)
                moves.addMove(p1, Point.create(row - 1, col));
            if (isValid(row + 1, col) && PlayerPieces[row + 1][col] == null)
                moves.addMove(p1, Point.create(row + 1, col));
            if (isValid(row, col - 1) && PlayerPieces[row][col - 1] == null)
                moves.addMove(p1, Point.create(row, col - 1));
            if (isValid(row, col + 1) && PlayerPieces[row][col + 1] == null)
                moves.addMove(p1, Point.create(row, col + 1));

        }
    }

    /**
     * initialize all the moves of a certain color
     * @param color the color of the pieces to initialize
     */
    public void initPossibleMoves(String color) {
        Piece[][] pieces = getPieces(color);
        for (int row = 0; row < pieces.length; row++) {
            for (int col = 0; col < pieces[row].length; col++) {
                if (isValid(row, col) && pieces[row][col] != null)
                    addMoves(row, col, color);
            }
        }
    }

    public void initPossibleMoves() {
        initPossibleMoves("red");
        initPossibleMoves("blue");
    }

    public Piece getPiece(Point p) {
        if (p == null)
            throw new IllegalArgumentException("point cant be null");
        if (!isValid(p))
            throw new IndexOutOfBoundsException("point " + p + " is out of bounds");
        if (isFree(p))
            return null;
        return getPieces(getColor(p))[p.getRow()][p.getCol()];
    }

    private boolean isValid(Point p) {
        return isValid(p.getRow(), p.getCol());
    }

    public void setPiece(int row, int col, Piece piece, String color) {
        if (!isValid(row, col))
            throw new IndexOutOfBoundsException("point (%d, %d) is out of bounds".formatted(row, col));
        getPieces(color)[row][col] = piece;
    }

    public boolean isValid(int row, int col) {
        return redPieces.length > row && row >= 0
                && redPieces[0].length > col && col >= 0 &&
                mapMask.getValue(row, col);
    }

    /**
     * clone this object with the pieces of color.
     */
    public Board clone(String color) {
        try {
            Board clone = (Board) super.clone();
            if ("red".equals(color)) {
                clone.redPieces = redPieces.clone();
                clone.bluePieces = new Piece[Board.size][Board.size];
                clone.possibleBlueMoves = new PossibleMoves();
                clone.possibleRedMoves = possibleRedMoves.clone();
                clone.redFlagInBoard = true;
                clone.blueFlagInBoard = false;
            } else if ("blue".equals(color)) {
                clone.bluePieces = bluePieces.clone();
                clone.redPieces = new Piece[Board.size][Board.size];
                clone.possibleRedMoves = new PossibleMoves();
                clone.possibleBlueMoves = new PossibleMoves();
                clone.blueFlagInBoard = true;
                clone.redFlagInBoard = false;
            }
            return clone;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param point1 the starting point of the move
     * @param point2 the ending point of the move
     * @param attackingPiece the attacking piece
     * @param defendingPiece the defending piece
     * @param color the color of the attacker.
     */
    public void undoMove(Point point1, Point point2, Piece attackingPiece, Piece defendingPiece, String color) {
        possibleRedMoves.clear(point1);
        possibleRedMoves.clear(point2);
        possibleBlueMoves.clear(point1);
        possibleBlueMoves.clear(point2);
        setPiece(point1, attackingPiece, color);
        setPiece(point1, null, getOppositeColor(color));
        setPiece(point2, null, color);
        setPiece(point2, defendingPiece, getOppositeColor(color));
        updateMoves(point1);
        updateMoves(point2);
        switchTurn();
    }

    private void setPiece(Point p, Piece Piece, String color) {
        setPiece(p.getRow(), p.getCol(), Piece, color);
    }

    /**
     * @return the winner of the game
     */
    public String getWinner() {
        if (!isGameOver())
            return null;
        if (!redFlagInBoard || possibleRedMoves.isEmpty())
            return "blue";
        if (!blueFlagInBoard || possibleBlueMoves.isEmpty())
            return "red";
        throw new IllegalStateException("something went wrong");
    }

    public String getPlayerTurn() {
        return playerTurn;
    }

    @Override
    public String toString() {
        StringBuilder outMsg = new StringBuilder();
        for (int row = 0; row < redPieces.length; row++) {
            for (int col = 0; col < redPieces[row].length; col++) {
                if (redPieces[row][col] != null)
                    outMsg.append(redPieces[row][col].toString()).append(' ');
                else if (bluePieces[row][col] != null)
                    outMsg.append(bluePieces[row][col].toString()).append(' ');
                else
                    outMsg.append("null ");
            }
            outMsg.append('\n');
        }
        return String.valueOf(outMsg);
    }

    public void setPieces(String color, Piece[][] board) {
        if ("red".equals(color))
            redPieces = board;
        if ("blue".equals(color))
            bluePieces = board;
    }

    public void undoMove(Move move, Piece p1, Piece p2, String color) {
        undoMove(move.getP1(), move.getP2(), p1, p2, color);
    }

    public Move getRandomMove() {
        return getMoves(playerTurn).chooseRandomMove();
    }
}
