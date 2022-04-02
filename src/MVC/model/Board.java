package MVC.model;

import MVC.Boolean2DArray;

import java.util.List;

import static MVC.model.Piece.*;

public class Board implements Cloneable {
    public static final int size = 10;
    // this is a read only variable
    public static final Piece[][][] defaultBoards = {
            {{SCOUT, COLONEL, LIEUTENANT, SCOUT, CAPTAIN, SCOUT, GENERAL, MINER, SCOUT, CAPTAIN},
                    {MARSHAL, SCOUT, MAJOR, COLONEL, SCOUT, CAPTAIN, BOMB, LIEUTENANT, BOMB, LIEUTENANT},
                    {CAPTAIN, SERGEANT, MAJOR, SPY, MAJOR, LIEUTENANT, BOMB, SERGEANT, BOMB, SERGEANT},
                    {MINER, SCOUT, MINER, MINER, SERGEANT, BOMB, FLAG, BOMB, MINER, SCOUT}},

            {{CAPTAIN, SCOUT, SCOUT, LIEUTENANT, SCOUT, CAPTAIN, MINER, MARSHAL, SCOUT, CAPTAIN},
                    {LIEUTENANT, SERGEANT, BOMB, SPY, GENERAL, SCOUT, MAJOR, MAJOR, COLONEL, COLONEL},
                    {SERGEANT, BOMB, SERGEANT, MAJOR, COLONEL, LIEUTENANT, BOMB, LIEUTENANT, CAPTAIN, SERGEANT},
                    {SCOUT, MINER, BOMB, SCOUT, MINER, BOMB, FLAG, BOMB, MINER, MINER}},

            {{MARSHAL, CAPTAIN, LIEUTENANT, MINER, SCOUT, CAPTAIN, SCOUT, SCOUT, SCOUT, CAPTAIN},
                    {SERGEANT, SCOUT, COLONEL, COLONEL, GENERAL, SCOUT, SERGEANT, BOMB, BOMB, LIEUTENANT},
                    {MAJOR, SCOUT, MAJOR, SPY, CAPTAIN, LIEUTENANT, BOMB, SERGEANT, LIEUTENANT, SCOUT},
                    {MAJOR, MINER, MINER, MINER, SERGEANT, BOMB, FLAG, BOMB, BOMB, MINER}
            },

            {{GENERAL, CAPTAIN, SCOUT, SERGEANT, SCOUT, SCOUT, SCOUT, MINER, CAPTAIN, SCOUT},
                    {MINER, SCOUT, COLONEL, MAJOR, BOMB, LIEUTENANT, MARSHAL, MAJOR, LIEUTENANT, COLONEL},
                    {BOMB, CAPTAIN, SPY, MAJOR, LIEUTENANT, SCOUT, CAPTAIN, LIEUTENANT, BOMB, SERGEANT},
                    {SERGEANT, SCOUT, MINER, BOMB, SERGEANT, MINER, MINER, BOMB, FLAG, BOMB}
            },
            {{SCOUT, CAPTAIN, LIEUTENANT, GENERAL, SCOUT, CAPTAIN, SCOUT, MARSHAL, CAPTAIN, SCOUT},
                    {MAJOR, SCOUT, BOMB, SPY, COLONEL, SERGEANT, SCOUT, MAJOR, COLONEL, SERGEANT},
                    {LIEUTENANT, SERGEANT, BOMB, SCOUT, MAJOR, MINER, LIEUTENANT, CAPTAIN, LIEUTENANT, BOMB},
                    {MINER, BOMB, SERGEANT, SCOUT, MINER, BOMB, MINER, MINER, BOMB, FLAG}
            }
    };
    private PossibleMoves possibleRedMoves;
    private PossibleMoves possibleBlueMoves;
    private Piece[][] redPieces;
    private Piece[][] bluePieces;
    private Boolean2DArray mapMask;
    private boolean redFlagInBoard;
    private boolean blueFlagInBoard;


    public Board() {
        redPieces = new Piece[Board.size][Board.size];
        bluePieces = new Piece[Board.size][Board.size];
        setRandomStartingBoard("red");
        setRandomStartingBoard("blue");
        possibleRedMoves = new PossibleMoves();
        possibleBlueMoves = new PossibleMoves();
        redFlagInBoard = true;
        blueFlagInBoard = true;
        mapMask = new Boolean2DArray(Board.size, Board.size);
        mapMask.setValue(2, 2, false);
        mapMask.setValue(3, 2, false);
        mapMask.setValue(2, 3, false);
        mapMask.setValue(3, 3, false);

        mapMask.setValue(6, 6, false);
        mapMask.setValue(7, 6, false);
        mapMask.setValue(6, 7, false);
        mapMask.setValue(7, 7, false);
    }


    public boolean isGameOver() {
        return !(redFlagInBoard && blueFlagInBoard) || possibleBlueMoves.isEmpty() || possibleBlueMoves.isEmpty();
    }

    private void setRandomStartingBoard(String playerColor) throws IllegalArgumentException {
//        Piece[][] startingBoard = defaultBoards[(int) (Math.random() * (defaultBoards.length))];
        //for debugging
        Piece[][] startingBoard = defaultBoards[0];
        Piece[][] playerPieces = getPieces(playerColor);

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


    public String getColor(Point p) {
        if (p == null)
            return null;
        if (redPieces[p.getRow()][p.getCol()] != null)
            return "red";
        else if (bluePieces[p.getRow()][p.getCol()] != null)
            return "blue";
        return null;
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

    public void updateMoves(Point p) {
        clearMoves(p);
        if (!isFree(p))
            addMoves(p.getRow(), p.getCol(), getColor(p));
        for (int row = p.getRow() + 1; row < bluePieces.length; row++) {
            if (!isFree(row, p.getCol())) {
                Point point = Point.create(row, p.getCol());
                clearMoves(point);
                addMoves(row, point.getCol(), getColor(point));
                break;
            }
        }
        for (int row = p.getRow() - 1; row >= 0; row--) {
            if (!isFree(row, p.getCol())) {
                Point point = Point.create(row, p.getCol());
                clearMoves(point);
                addMoves(row, point.getCol(), getColor(point));
                break;
            }
        }
        for (int col = p.getCol() + 1; col < bluePieces[0].length; col++) {
            if (!isFree(p.getRow(), col)) {
                Point point = Point.create(p.getRow(), col);
                clearMoves(point);
                addMoves(point.getRow(), col, getColor(point));
                break;
            }
        }
        for (int col = p.getCol() - 1; col >= 0; col--) {
            if (!isFree(p.getRow(), col)) {
                Point point = Point.create(p.getRow(), col);
                clearMoves(point);
                addMoves(point.getRow(), col, getColor(point));
                break;
            }
        }
    }

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

    public boolean doesHaveMoves(Point p) {
        String color = getColor(p);
        if (color == null)
            return false;
        return getMoves(color).contains(p);
    }


    private void clearMoves(Point p) {
        possibleRedMoves.clear(p);
        possibleBlueMoves.clear(p);
    }

    public boolean isFree(Point p) {
        return isFree(p.getRow(), p.getCol());
    }

    private boolean isFree(int row, int col) {
        return isValid(row, col) && bluePieces[row][col] == null && redPieces[row][col] == null;
    }


    /**
     * there must be a piece in board[row][col]
     *
     * @param row   row in the board
     * @param col   column in the board
     * @param color the color of the pieces
     */
    private void addMoves(int row, int col, String color) {
        Piece[][] PlayerPieces = getPieces(color);
        Piece[][] OppositePieces = getOppositePieces(color);

        PossibleMoves moves = getMoves(color);
        Piece p = PlayerPieces[row][col];
        Point p1 = Point.create(row, col);
        if (!p.isMovable())
            return;
        if (p == SCOUT) {
            if (row > 0) {
                for (int newRow = row - 1; newRow >= 0 &&
                        PlayerPieces[newRow][col] == null; newRow--) {
                    moves.addMove(p1, Point.create(newRow, col));
                    if (OppositePieces[newRow][col] != null)
                        break;
                }
            }
            if (col > 0) {
                for (int newCol = col - 1; newCol >= 0 &&
                        PlayerPieces[row][newCol] == null; newCol--) {
                    moves.addMove(p1, Point.create(row, newCol));
                    if (OppositePieces[row][newCol] != null)
                        break;
                }
            }
            if (col < PlayerPieces[row].length - 1) {

                for (int newCol = col + 1; newCol < PlayerPieces[row].length &&
                        PlayerPieces[row][newCol] == null; newCol++) {
                    moves.addMove(p1, Point.create(row, newCol));
                    if (OppositePieces[row][newCol] != null)
                        break;
                }
            }
            if (row < PlayerPieces.length - 1) {
                for (int newRow = row + 1; newRow < PlayerPieces.length &&
                        PlayerPieces[newRow][col] == null; newRow++) {
                    moves.addMove(p1, Point.create(newRow, col));
                    if (OppositePieces[newRow][col] != null)
                        break;
                }
            }
        } else {
            if (row > 0 && PlayerPieces[row - 1][col] == null)
                moves.addMove(p1, Point.create(row - 1, col));
            if (row < PlayerPieces.length - 1 && PlayerPieces[row + 1][col] == null)
                moves.addMove(p1, Point.create(row + 1, col));
            if (col > 0 && PlayerPieces[row][col - 1] == null)
                moves.addMove(p1, Point.create(row, col - 1));
            if (col < PlayerPieces[row].length - 1 && PlayerPieces[row][col + 1] == null)
                moves.addMove(p1, Point.create(row, col + 1));

        }
    }


    public void initPossibleMoves(String color) {
        Piece[][] pieces = getPieces(color);
        for (int row = 0; row < pieces.length; row++) {
            for (int col = 0; col < pieces[row].length; col++) {
                if (pieces[row][col] != null)
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

    private boolean isValid(int row, int col) {
        return redPieces.length > row && row >= 0
                && redPieces[0].length > col && col >= 0 &&
                mapMask.getValue(row, col);
    }

    public String getOppositeColor(String color) {
        if (color == null)
            return null;
        if ("red".equals(color))
            return "blue";
        if ("blue".equals(color))
            return "red";
        return null;
    }


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
    }

    private void setPiece(Point p, Piece Piece, String color) {
        setPiece(p.getRow(), p.getCol(), Piece, color);
    }

    public String getWinner() {
        if (!isGameOver())
            return null;
        if (!redFlagInBoard || possibleRedMoves.isEmpty())
            return "blue";
        if (!blueFlagInBoard || possibleBlueMoves.isEmpty())
            return "red";
        throw new IllegalStateException("something went wrong");
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
}
