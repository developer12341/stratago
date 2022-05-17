package MVC.model;

import generator.BoardGenerator;

import java.util.*;

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
        //check if one of the flags is missing
        if (!redFlagInBoard || !blueFlagInBoard)
            return true;
        //check if one of the players doesn't have any more moves
        if (!hasMoves(playerTurn))
            return true;
        return false;
    }

    /**
     * @param color the color of the player
     * @return amount of Moves that the player has
     */
    public int amountOfMoves(String color) {
        if (color == null)
            return 0;
        //get all the moves for a color
        int amount = 0;
        Piece[][] pieces = getPieces(color);
        for (int row = 0; row < pieces.length; row++) {
            for (int col = 0; col < pieces[row].length; col++) {
                if (pieces[row][col] != null) {
                    amount += getAmountOfMoves(row, col);
                }
            }
        }
        return amount;
    }


    /**
     * @return true if the player has any possible moves
     */
    public boolean hasMoves(String color) {
        return amountOfMoves(color) > 0;
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
        return getColor(p.getRow(), p.getCol());
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

    public void OptimizedMoveTo(int move) {
        String p1Color = getColor(MoveLib.unpackRowFrom(move), MoveLib.unpackColFrom(move));
        String p2Color = getColor(MoveLib.unpackRowTo(move), MoveLib.unpackColTo(move));
        if (isFree(MoveLib.unpackRowFrom(move), MoveLib.unpackColFrom(move)))
            throw new IllegalArgumentException(MoveLib.getFrom(move) + " doesn't have a piece");
        if (Objects.equals(p1Color, p2Color)) {
            System.out.println(this);
            System.out.println("same color: " + p1Color);
            throw new IllegalArgumentException("the piece you are trying to move has the same color...");
        }
        if (p2Color == null)
            p2Color = p1Color;
        Piece[][] player1Pieces = getPieces(p1Color);
        Piece[][] player2Pieces = getPieces(p2Color);

        if (player2Pieces[MoveLib.unpackRowTo(move)][MoveLib.unpackColTo(move)] == FLAG)
            setGameOverFlag(p2Color, false);


        Piece winner = player1Pieces[MoveLib.unpackRowFrom(move)][MoveLib.unpackColFrom(move)].Attack(
                player2Pieces[MoveLib.unpackRowTo(move)][MoveLib.unpackColTo(move)]);
        switchTurn();
        if (winner == null) {
            player1Pieces[MoveLib.unpackRowFrom(move)][MoveLib.unpackColFrom(move)] = null;
            player2Pieces[MoveLib.unpackRowTo(move)][MoveLib.unpackColTo(move)] = null;
        } else if (winner == player1Pieces[MoveLib.unpackRowFrom(move)][MoveLib.unpackColFrom(move)]) {
            player1Pieces[MoveLib.unpackRowFrom(move)][MoveLib.unpackColFrom(move)] = null;
            player2Pieces[MoveLib.unpackRowTo(move)][MoveLib.unpackColTo(move)] = null;
            player1Pieces[MoveLib.unpackRowTo(move)][MoveLib.unpackColTo(move)] = winner;
        } else {
            player1Pieces[MoveLib.unpackRowFrom(move)][MoveLib.unpackColFrom(move)] = null;
        }
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
        if (p1Color.equals(p2Color)) {
            System.out.println(Arrays.deepToString(getPieces(p1Color)));
            System.out.println(Move.create(p1, p2));
            throw new IllegalArgumentException("the piece you are trying to move has the same color...");
        }
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
     * @param p1 the starting point
     * @param p2 the destination
     * @return true if it is a valid move.
     */
    public boolean isPossibleMove(Point p1, Point p2) {
        if (p1 == p2)
            return false;
        if (isFree(p1))
            return false;
        if (!isValid(p2) || !isValid(p1))
            return false;
        if (Objects.equals(getColor(p1), getColor(p2)))
            return false;
        if (!getPiece(p1).isMovable())
            return false;

        //if the points are at a diagonal then it is not a valid move
        if (!(p1.getRow() == p2.getRow() || p1.getCol() == p2.getCol()))
            return false;
        if (p1.distance(p2) > 1 && getPiece(p1) != SCOUT)
            return false;
        if (getPiece(p1) == SCOUT) {
            Piece[][] playerPieces = getPieces(getColor(p1));
            if (p1.getCol() == p2.getCol()) {
                int direction = Integer.signum(p2.getRow() - p1.getRow());
                for (int newRow = p1.getRow() + direction; newRow != p2.getRow() + direction; newRow += direction) {
                    if (playerPieces[newRow][p1.getCol()] != null)
                        return false;
                }
                return true;
            } else if (p1.getRow() == p2.getRow()) {
                int direction = Integer.signum(p2.getCol() - p1.getCol());
                for (int newCol = p1.getCol() + direction; newCol != p2.getCol() + direction; newCol += direction) {
                    if (playerPieces[p1.getRow()][newCol] != null)
                        return false;
                }
                return true;
            } else {
                throw new IllegalStateException("this function has a problem");
            }
        } else {
            Piece[][] playerPieces = getPieces(getColor(p1));
            return playerPieces[p2.getRow()][p2.getCol()] == null;
        }
    }

    public int getAmountOfMoves(int row, int col) {
        //get the amount of possible moves
        if(!isValid(row, col))
            return 0;
        if(isFree(row, col))
            return 0;
        if(!getPiece(row, col).isMovable())
            return 0;
        int amount = 0;
        Piece[][] PlayerPieces = getPieces(getColor(row, col));
        if(getPiece(row, col) == SCOUT){
            //get the opposite color pieces
            Piece[][] OppositePieces = getPieces(getOppositeColor(getColor(row, col)));
            //if the piece is a scout then it has the ability to move in every direction
            for (int newRow = row - 1; isValid(newRow, col) &&
                    PlayerPieces[newRow][col] == null; newRow--) {
                amount++;
                if (OppositePieces[newRow][col] != null)
                    break;
            }
            for (int newRow = row + 1; isValid(newRow, col) &&
                    PlayerPieces[newRow][col] == null; newRow++) {
                amount++;
                if (OppositePieces[newRow][col] != null)
                    break;
            }
            for (int newCol = col - 1; isValid(row, newCol) &&
                    PlayerPieces[row][newCol] == null; newCol--) {
                amount++;
                if (OppositePieces[row][newCol] != null)
                    break;
            }
            for (int newCol = col + 1; isValid(row, newCol) &&
                    PlayerPieces[row][newCol] == null; newCol++) {
                amount++;
                if (OppositePieces[row][newCol] != null)
                    break;
            }
        }
        else{
            if (isValid(row - 1, col) && PlayerPieces[row - 1][col] == null)
                amount++;
            if (isValid(row + 1, col) && PlayerPieces[row + 1][col] == null)
                amount++;
            if (isValid(row, col - 1) && PlayerPieces[row][col - 1] == null)
                amount++;
            if (isValid(row, col + 1) && PlayerPieces[row][col + 1] == null)
                amount++;
        }
        return amount;
    }

    public List<Point> getMoves(Point p) {
        if (!isValid(p))
            throw new IllegalArgumentException("the point is out of bounds");
        if (isFree(p))
            return null;
        String color = getColor(p);
        Piece[][] PlayerPieces = getPieces(color);
        if (isSurrounded(p, PlayerPieces))
            return null;
        Piece[][] OppositePieces = getOppositePieces(color);
        Piece piece = PlayerPieces[p.getRow()][p.getCol()];
        if (!piece.isMovable())
            return null;
        int col = p.getCol(), row = p.getRow();
        ArrayList<Point> moves = new ArrayList<>();
        if (piece == SCOUT) {
            //if the piece is a scout then it has the ability to move in every direction
            for (int newRow = row - 1; isValid(newRow, col) &&
                    PlayerPieces[newRow][col] == null; newRow--) {
                moves.add(Point.create(newRow, col));
                if (OppositePieces[newRow][col] != null)
                    break;
            }
            for (int newCol = col - 1; isValid(row, newCol) &&
                    PlayerPieces[row][newCol] == null; newCol--) {
                moves.add(Point.create(row, newCol));
                if (OppositePieces[row][newCol] != null)
                    break;
            }
            for (int newCol = col + 1; isValid(row, newCol) &&
                    PlayerPieces[row][newCol] == null; newCol++) {
                moves.add(Point.create(row, newCol));
                if (OppositePieces[row][newCol] != null)
                    break;
            }
            for (int newRow = row + 1; isValid(newRow, col) &&
                    PlayerPieces[newRow][col] == null; newRow++) {
                moves.add(Point.create(newRow, col));
                if (OppositePieces[newRow][col] != null)
                    break;
            }
        } else {
            if (isValid(row - 1, col) && PlayerPieces[row - 1][col] == null)
                moves.add(Point.create(p.getRow() - 1, col));
            if (isValid(row + 1, col) && PlayerPieces[row + 1][col] == null)
                moves.add(Point.create(row + 1, col));
            if (isValid(row, col - 1) && PlayerPieces[row][col - 1] == null)
                moves.add(Point.create(row, col - 1));
            if (isValid(row, col + 1) && PlayerPieces[row][col + 1] == null)
                moves.add(Point.create(row, col + 1));
        }
        return moves;
    }

    /**
     * @param p      the point to check
     * @param pieces the pieces of the player
     * @return true if the piece is surrounded by the pieces in the variable pieces
     */
    boolean isSurrounded(Point p, Piece[][] pieces) {
        if(p == null || pieces == null)
            throw new IllegalArgumentException("the point or pieces is null");
        return  isSurrounded(p.getRow(), p.getCol(), pieces);
    }
    boolean isSurrounded(int row, int col, Piece[][] pieces) {
        if (isValid(row - 1, col) && pieces[row - 1][col] == null)
            return false;
        if (isValid(row + 1, col) && pieces[row + 1][col] == null)
            return false;
        if (isValid(row, col - 1) && pieces[row][col - 1] == null)
            return false;
        if (isValid(row, col + 1) && pieces[row][col + 1] == null)
            return false;
        return true;
    }

    /**
     * @param p a point
     * @return true if the point has any legal moves
     */
    public boolean doesHaveMoves(Point p) {
        if (isFree(p))
            return false;
        Piece piece = getPiece(p);
        if (!piece.isMovable())
            return false;
        String color = getColor(p);
        Piece[][] playerPieces = getPieces(color);
        if (isValid(p.getRow() - 1, p.getCol()) && playerPieces[p.getRow() - 1][p.getCol()] == null)
            return true;
        if (isValid(p.getRow() + 1, p.getCol()) && playerPieces[p.getRow() + 1][p.getCol()] == null)
            return true;
        if (isValid(p.getRow(), p.getCol() - 1) && playerPieces[p.getRow()][p.getCol() - 1] == null)
            return true;
        if (isValid(p.getRow(), p.getCol() + 1) && playerPieces[p.getRow()][p.getCol() + 1] == null)
            return true;
        return false;
    }

    public boolean isFree(Point p) {
        return isFree(p.getRow(), p.getCol());
    }

    public boolean isFree(int row, int col) {
        return isValid(row, col) && bluePieces[row][col] == null && redPieces[row][col] == null;
    }

    public int[] getOptimizedMoves(String color) {
        //get all the moves for a color
        int[] moves = new int[amountOfMoves(color)];
        int index = 0;
        Piece[][] pieces = getPieces(color);
        for (int row = 0; row < pieces.length; row++) {
            for (int col = 0; col < pieces[row].length; col++) {
                if (!isValid(row, col))
                    continue;
                if (pieces[row][col] != null) {
                    Piece[][] PlayerPieces = getPieces(color);
                    Piece[][] OppositePieces = getOppositePieces(color);
                    if (!pieces[row][col].isMovable())
                        continue;
                    if (pieces[row][col] == SCOUT) {
                        //if the piece is a scout then it has the ability to move in every direction
                        for (int newRow = row - 1; isValid(newRow, col) &&
                                PlayerPieces[newRow][col] == null; newRow--) {
                            moves[index++] = MoveLib.pack(row, col, newRow, col);
                            if (OppositePieces[newRow][col] != null)
                                break;
                        }
                        for (int newRow = row + 1; isValid(newRow, col) &&
                                PlayerPieces[newRow][col] == null; newRow++) {
                            moves[index++] = MoveLib.pack(row, col, newRow, col);
                            if (OppositePieces[newRow][col] != null)
                                break;
                        }
                        for (int newCol = col - 1; isValid(row, newCol) &&
                                PlayerPieces[row][newCol] == null; newCol--) {
                            moves[index++] = MoveLib.pack(row, col, row, newCol);
                            if (OppositePieces[row][newCol] != null)
                                break;
                        }
                        for (int newCol = col + 1; isValid(row, newCol) &&
                                PlayerPieces[row][newCol] == null; newCol++) {
                            moves[index++] = MoveLib.pack(row, col, row, newCol);
                            if (OppositePieces[row][newCol] != null)
                                break;
                        }
                    } else {
                        if (isValid(row - 1, col) && PlayerPieces[row - 1][col] == null)
                            moves[index++] = MoveLib.pack(row, col, row - 1, col);
                        if (isValid(row + 1, col) && PlayerPieces[row + 1][col] == null)
                            moves[index++] = MoveLib.pack(row, col, row + 1, col);
                        if (isValid(row, col - 1) && PlayerPieces[row][col - 1] == null)
                            moves[index++] = MoveLib.pack(row, col, row, col - 1);
                        if (isValid(row, col + 1) && PlayerPieces[row][col + 1] == null)
                            moves[index++] = MoveLib.pack(row, col, row, col + 1);
                    }
                }
            }
        }
        if(index != moves.length) {
            System.out.println(this);
            System.out.println("blue moves: " + Arrays.toString(moves));
            throw new RuntimeException("index != moves.length");
        }
        return moves;
    }

    public List<Move> getMoves(String color) {
        //get all the moves for a color
        List<Move> moves = new ArrayList<>();
        Piece[][] pieces = getPieces(color);
        for (int i = 0; i < pieces.length; i++) {
            for (int j = 0; j < pieces[i].length; j++) {
                if (pieces[i][j] != null) {
                    List<Point> points = getMoves(Point.create(i, j));
                    if (points != null) {
                        //convert the points to moves
                        for (Point p : points) {
                            moves.add(Move.create(Point.create(i, j), p));
                        }
                    }
                }
            }
        }
        return moves;
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

    public Piece getPiece(int row, int col) {
        if (!isValid(row, col))
            throw new IndexOutOfBoundsException("point " + row + "," + col + " is out of bounds");
        if (isFree(row, col))
            return null;
        return getPieces(getColor(row, col))[row][col];
    }

    String getColor(int row, int col) {
        if (!isValid(row, col))
            throw new IndexOutOfBoundsException("point (" + row + ", " + col + ") is out of bounds");
        if (redPieces[row][col] != null)
            return "red";
        else if (bluePieces[row][col] != null)
            return "blue";
        return null;
    }

    boolean isValid(Point p) {
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
                clone.redFlagInBoard = true;

                clone.bluePieces = new Piece[Board.size][Board.size];
                clone.blueFlagInBoard = false;
            } else if ("blue".equals(color)) {
                clone.bluePieces = bluePieces.clone();
                clone.blueFlagInBoard = true;

                clone.redPieces = new Piece[Board.size][Board.size];
                clone.redFlagInBoard = false;
            }
            clone.playerTurn = playerTurn;
            return clone;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param point1         the starting point of the move
     * @param point2         the ending point of the move
     * @param attackingPiece the attacking piece
     * @param defendingPiece the defending piece
     * @param color          the color of the attacker.
     */
    public void undoMove(Point point1, Point point2, Piece attackingPiece, Piece defendingPiece, String color) {
        setPiece(point1, attackingPiece, color);
        setPiece(point1, null, getOppositeColor(color));
        setPiece(point2, null, color);
        setPiece(point2, defendingPiece, getOppositeColor(color));
        switchTurn();
    }

    /**
     * @param attackingPiece the attacking piece
     * @param defendingPiece the defending piece
     * @param color          the color of the attacker.
     */
    public void OptimizedUndoMove(int move, Piece attackingPiece, Piece defendingPiece, String color) {
        setPiece(MoveLib.unpackRowFrom(move), MoveLib.unpackColFrom(move), attackingPiece, color);
        setPiece(MoveLib.unpackRowFrom(move), MoveLib.unpackColFrom(move), null, getOppositeColor(color));
        setPiece(MoveLib.unpackRowTo(move), MoveLib.unpackColTo(move), null, color);
        setPiece(MoveLib.unpackRowTo(move), MoveLib.unpackColTo(move), defendingPiece, getOppositeColor(color));
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
        if (!redFlagInBoard || !hasMoves("red"))
            return "blue";
        if (!blueFlagInBoard || !hasMoves("blue"))
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

}
