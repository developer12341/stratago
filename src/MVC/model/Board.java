package MVC.model;

import MVC.model.PointClasses.Point;

import java.util.ArrayList;

public class Board {
    // this is a read only variable, so I made it static to save on memory
    private static final Piece[][][] defaultBoards = {
            {
                    {Piece.SCOUT, Piece.COLONEL, Piece.LIEUTENANT, Piece.SCOUT, Piece.CAPTAIN, Piece.SCOUT, Piece.GENERAL,
                            Piece.MINER, Piece.SCOUT, Piece.CAPTAIN},
                    {Piece.MARSHAL, Piece.SCOUT, Piece.MAJOR, Piece.COLONEL, Piece.SCOUT, Piece.CAPTAIN, Piece.BOMB,
                            Piece.LIEUTENANT, Piece.BOMB, Piece.LIEUTENANT},
                    {Piece.CAPTAIN, Piece.SERGEANT, Piece.MAJOR, Piece.SPY, Piece.MAJOR, Piece.LIEUTENANT, Piece.BOMB,
                            Piece.SERGEANT, Piece.BOMB, Piece.SERGEANT},
                    {Piece.MINER, Piece.SCOUT, Piece.MINER, Piece.MINER, Piece.SERGEANT, Piece.BOMB, Piece.FLAG, Piece.BOMB,
                            Piece.MINER, Piece.SCOUT}
            }
    };
    private Piece[][] redPieces;
    private Piece[][] bluePieces;
    //todo: add a map mask, the board has a hole in the middle...

    public void setRedPieces(Piece[][] redPieces) {
        this.redPieces = redPieces;
    }

    public void setBluePieces(Piece[][] bluePieces) {
        this.bluePieces = bluePieces;
    }

    private final PossibleMoves possibleRedMoves;
    private final PossibleMoves possibleBlueMoves;



    public Board() {
        redPieces = new Piece[10][10];
        bluePieces = new Piece[10][10];
        setRandomStartingBoard("red");
        setRandomStartingBoard("blue");
        possibleRedMoves = new PossibleMoves();
        possibleBlueMoves = new PossibleMoves();
    }

    private void setRandomStartingBoard(String playerColor) throws IllegalArgumentException {
        //todo:fix the copping
        Piece[][] startingBoard = defaultBoards[(int) (Math.random() * (defaultBoards.length))];
        Piece[][] playerPieces = getPieces(playerColor);

        if (playerColor.equals("red")) {
            for (int row = startingBoard.length - 1; row >= 0; row--) {
                System.arraycopy(startingBoard[startingBoard.length - 1 - row],
                        0,
                        playerPieces[redPieces.length - 1 - row],
                        0,
                        startingBoard[row].length);
            }
        } else if (playerColor.equals("blue")) {
            for (int row = 0; row < startingBoard.length; row++) {
                System.arraycopy(startingBoard[row],
                        0,
                        playerPieces[row],
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


    public Point moveTo(Point p1, Point p2) {
        String p1Color = getColor(p1);
        String p2Color = getColor(p2);
        if (p1Color == null)
            throw new IllegalArgumentException(p1 + " doesn't have a piece");
        if (p1Color.equals(p2Color))
            throw new IllegalArgumentException("the piece you are trying to move has the same color...");
        if (p2Color == null)
            p2Color = p1Color;
        Piece[][] player1Pieces = getPieces(p1Color);
        Piece[][] player2Pieces = getPieces(p2Color);

        Piece winner = player1Pieces[p1.getRow()][p1.getCol()].Attack(
                player2Pieces[p2.getRow()][p2.getCol()]);

        if (winner == null) {
            player1Pieces[p1.getRow()][p1.getCol()] = null;
            player2Pieces[p2.getRow()][p2.getCol()] = null;
            return null;
        } else if (winner == player1Pieces[p1.getRow()][p1.getCol()]) {
            player1Pieces[p1.getRow()][p1.getCol()] = null;
            player2Pieces[p2.getRow()][p2.getCol()] = null;
            player1Pieces[p2.getRow()][p2.getCol()] = winner;
            return p1;
        } else {
            player1Pieces[p1.getRow()][p1.getCol()] = null;
            return p2;
        }

    }

    public void updateMoves(Point p){
        clearMoves(p);
        if(!isFree(p))
            addMoves(p.getRow(), p.getCol(),getColor(p)); // hours spent on this bug = 1
        for (int row = p.getRow() + 1; row < bluePieces.length; row++) {
            if(!isFree(row,p.getCol())){
                Point point = Point.create(row, p.getCol());
                clearMoves(point);
                addMoves(row, point.getCol(),getColor(point));
                break;
            }
        }
        for (int row = p.getRow() - 1; row >= 0; row--) {
            if(!isFree(row,p.getCol())){
                Point point = Point.create(row, p.getCol());
                clearMoves(point);
                addMoves(row, point.getCol(),getColor(point));
                break;
            }
        }
        for (int col = p.getCol() + 1; col < bluePieces[0].length; col++) {
            if(!isFree(p.getRow(), col)){
                Point point = Point.create(p.getRow(), col);
                clearMoves(point);
                addMoves(point.getRow(), col, getColor(point));
                break;
            }
        }
        for (int col = p.getCol() - 1; col >= 0; col--) {
            if(!isFree(p.getRow(), col)){
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
        if("blue".equals(color))
            return possibleBlueMoves;
        throw new IllegalArgumentException("there is no " + color + "player");
    }

    public ArrayList<Point> getMoves(Point p) {
        return getMoves(getColor(p)).getMoves(p);
    }

    public boolean doesHaveMoves(Point p) {
        String color = getColor(p);
        if(color == null)
            return false;
        return getMoves(color).contains(p);
    }


    private void clearMoves(Point p) {
        possibleRedMoves.clear(p);
        possibleBlueMoves.clear(p);
    }

    private boolean isFree(Point p) {
        return isFree(p.getRow(), p.getCol());
    }

    private boolean isFree(int row, int col) {
        return bluePieces[row][col] == null && redPieces[row][col] == null;
    }


    /**
     * there must be a piece in board[row][col]
     * @param row row in the board
     * @param col column in the board
     * @param color the color of the pieces
     */
    private void addMoves(int row, int col, String color){
        //todo:refactor this function, just for beatification purposes
        Piece[][] PlayerPieces = getPieces(color);
        Piece[][] OppositePieces = getOppositePieces(color);

        PossibleMoves moves = getMoves(color);
        Piece p = PlayerPieces[row][col];
        Point p1 = Point.create(row,col);
        if(!p.isMovable())
            return;
        if (p == Piece.SCOUT) {
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
                    moves.addMove(p1,Point.create(row, newCol));
                    if (OppositePieces[row][newCol] != null)
                        break;
                }
            }
            if (col < PlayerPieces[row].length - 1) {

                for (int newCol = col + 1; newCol < PlayerPieces[row].length &&
                        PlayerPieces[row][newCol] == null; newCol++) {
                    moves.addMove(p1,Point.create(row, newCol));
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
                moves.addMove(p1,Point.create(row - 1, col));
            if (row < PlayerPieces.length - 1 && PlayerPieces[row + 1][col] == null)
                moves.addMove(p1,Point.create(row + 1, col));
            if (col > 0 && PlayerPieces[row][col - 1] == null)
                moves.addMove(p1,Point.create(row, col - 1));
            if (col < PlayerPieces[row].length - 1 && PlayerPieces[row][col + 1] == null)
                moves.addMove(p1,Point.create(row, col + 1));

        }
    }


    private void initPossibleMoves(String color){
        Piece[][] pieces = getPieces(color);
        for (int row = 0; row < pieces.length; row++) {
            for (int col = 0; col < pieces[row].length; col++) {
                if(pieces[row][col] != null)
                    addMoves(row,col,color);
            }
        }
    }
    public void initPossibleMoves() {
        initPossibleMoves("red");
        initPossibleMoves("blue");

    }
}
