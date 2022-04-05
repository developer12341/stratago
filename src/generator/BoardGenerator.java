package generator;

import MVC.model.Piece;
import MVC.model.Point;

import java.io.*;
import java.util.Random;

import static MVC.model.Piece.*;

/**
 * this class is here to generate, save, and load a lot of starting boards.
 * in order for the AI to make predictions the board needs to have a lot of data about starting
 * moves.
 * there is no database of stratego games so I had to make some by myself.
 */
public class BoardGenerator {
    private static final String filePath = "src/generator/boards.txt";
    private static final Random randomGenerator = new Random();
    private int[] pieceCount;
    private int pieceSum;
    private Piece[][] board;

    public BoardGenerator() {
        reset();
    }

    private static String printBoard(Piece[][] b) {
        StringBuilder outMsg = new StringBuilder("{");

        for (int i = 0; i < b.length - 1; i++) {
            Piece[] pieces = b[i];
            outMsg.append("{").append(pieces[0].name());
            for (int col = 1; col < pieces.length; col++) {
                outMsg.append(", ").append(pieces[col].name());
            }
            outMsg.append("}, \n");
        }
        Piece[] pieces = b[b.length - 1];
        outMsg.append("{").append(pieces[b.length].name());
        for (int col = 1; col < pieces.length; col++) {
            outMsg.append(", ").append(pieces[col].name());
        }
        outMsg.append("}\n");
        outMsg.append("}");
        return String.valueOf(outMsg);
    }

    /**
     * load from a file a lot of starting boards.
     */
    public static Piece[][][] getDefaultBoards() {

        // Creating an object of BufferedReader class
        try {
            Piece[][][] boards = new Piece[30][4][10];
            File file = new File(filePath);
            BufferedReader br = new BufferedReader(new FileReader(file));

            // Declaring a string variable
            int i = 0, j = 0, k = 0;
            do {
                String st = br.readLine();
                if (st == null)
                    break;
                if (st.length() == 0 || st.length() == 1)
                    continue;
                for (String word : st.split(" ")) {
                    if (word == null)
                        continue;
                    boards[k][i][j] = Piece.valueOf(word);
                    j++;
                    if (j == 10)
                        j = 0;
                }
                i++;
                if (i == 4) {
                    i = 0;
                    k++;
                }

            } while (true);
            return boards;
        } catch (IOException e) {
            e.printStackTrace();
            return new Piece[][][]{{
                    {SCOUT, COLONEL, LIEUTENANT, SCOUT, CAPTAIN, SCOUT, GENERAL, MINER, SCOUT, CAPTAIN},
                    {MARSHAL, SCOUT, MAJOR, COLONEL, SCOUT, CAPTAIN, BOMB, LIEUTENANT, BOMB, LIEUTENANT},
                    {CAPTAIN, SERGEANT, MAJOR, SPY, MAJOR, LIEUTENANT, BOMB, SERGEANT, BOMB, SERGEANT},
                    {MINER, SCOUT, MINER, MINER, SERGEANT, BOMB, FLAG, BOMB, MINER, SCOUT}
            }};
        }
    }

    /**
     * save a board to the file.
     */
    public static void saveBoard(Piece[][] board) {
        try {
            File file = new File("C:\\Users\\idodo\\IdeaProjects\\stratago\\src\\generator\\boards.txt");
            FileWriter fileWriter = new FileWriter(file, true);
            for (Piece[] row : board) {
                for (Piece p : row) {
                    fileWriter.append(p.name()).append(" ");
                }
                fileWriter.append('\n');
            }
            fileWriter.append('\n');
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Piece getRandomPiece() {
        return Piece.fromInteger(randomGenerator.nextInt(pieceCount.length) + 1);
    }

    /**
     * generate a random board.
     */
    public Piece[][] generate() {
        reset();
        int flagLocation = randomGenerator.nextInt(10);
        updatePiece(3, flagLocation, FLAG);
        setBombs(flagLocation, board);
        while (pieceCount[BOMB.PieceNumber - 1] != 0) {
            int randomCol;
            int randomRow;
            do {
                randomCol = randomGenerator.nextInt(board[2].length);
                randomRow = randomGenerator.nextInt(2) + 1;
            } while (board[randomRow][randomCol] != null);
            updatePiece(randomRow, randomCol, BOMB);
        }
        while (pieceSum > 0) {
            int randomCol;
            int randomRow;
            do {
                randomCol = randomGenerator.nextInt(board[2].length);
                randomRow = randomGenerator.nextInt(board.length);
            } while (board[randomRow][randomCol] != null);
            Piece randomPiece = getRandomPiece();
            while (pieceCount[randomPiece.PieceNumber - 1] == 0) {
                randomPiece = getRandomPiece();
            }
            updatePiece(randomRow, randomCol, randomPiece);

        }

        return board.clone();
    }

    private void updatePiece(int row, int col, Piece piece) {
        if (pieceCount[piece.PieceNumber - 1] == 0)
            throw new IllegalStateException("there are no " + piece + " available.");
        board[row][col] = piece;
        pieceCount[piece.PieceNumber - 1]--;
        pieceSum--;
    }

    private void setBombs(int flagLocation, Piece[][] board) {
        updatePiece(2, flagLocation, BOMB);
        if (flagLocation > 0) {
            updatePiece(3, flagLocation - 1, BOMB);
        }
        if (flagLocation < board[3].length - 1) {
            updatePiece(3, flagLocation + 1, BOMB);
        }

    }

    /**
     * reset the data to make room for a new board
     */
    private void reset() {
        pieceCount = new int[]{1, 8, 5, 4, 4, 4, 3, 2, 1, 1, 6, 1};
        pieceSum = 40;
        board = new Piece[4][10];
    }

    public void swapPieces(Point p1, Point p2) {
        Piece temp = board[p2.getRow() - 6][p2.getCol()];
        board[p2.getRow() - 6][p2.getCol()] = board[p1.getRow() - 6][p1.getCol()];
        board[p1.getRow() - 6][p1.getCol()] = temp;
    }

    public void saveBoard() {
        saveBoard(board);
    }
}