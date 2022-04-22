package MVC.model;

import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;

/**
 * this enum represent a piece on the board.
 */
public enum Piece implements Serializable {
    SPY(1),
    SCOUT(2),
    MINER(3),
    SERGEANT(4),
    LIEUTENANT(5),
    CAPTAIN(6),
    MAJOR(7),
    COLONEL(8),
    GENERAL(9),
    MARSHAL(10),
    BOMB(11),
    FLAG(12),
    PLACEHOLDER(13);

    private static final String PiecesFolder = "src/media/images/pieces/";

    /**
     * the piece's number is also the piece's rank.
     */
    public final int PieceNumber;

    //these are the images that are correlated with the piece.
    public final Image redPieceImage;
    public final Image bluePieceImage;

    Piece(int pieceNumber) {
        PieceNumber = pieceNumber;
        redPieceImage = loadImage(PiecesFolder + "red/red_" + pieceNumber + ".png");
        bluePieceImage = loadImage(PiecesFolder + "blue/blue_" + pieceNumber + ".png");
    }

    /**
     * @param value the piece's number
     * @return the piece with this number
     */
    public static Piece fromInteger(int value) {
        return switch (value) {
            case (1), ('S') -> SPY;
            case (2) -> SCOUT;
            case (3) -> MINER;
            case (4) -> SERGEANT;
            case (5) -> LIEUTENANT;
            case (6) -> CAPTAIN;
            case (7) -> MAJOR;
            case (8) -> COLONEL;
            case (9) -> GENERAL;
            case (10) -> MARSHAL;
            case (11), ('B'), ('b') -> BOMB;
            case (12), ('F'), ('f') -> FLAG;
            case (13) -> PLACEHOLDER;
            default -> throw new IllegalStateException("Unexpected value: " + value);
        };

    }

    /**
     * @param filePath the path to the image
     * @return the Image object
     */
    private Image loadImage(String filePath) {
        try {
            return new Image(new FileInputStream(filePath));
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    /**
     * this function is returning the winning piece in the case that
     * this piece is attacking some other piece.
     *
     * @param DefendingPiece the piece that is defending
     * @return the winning piece
     */
    public Piece Attack(Piece DefendingPiece) {
        if (DefendingPiece == null)
            return this;
        switch (DefendingPiece) {
            case FLAG:
                return this;
            case BOMB: {
                if (this == MINER)
                    return this;
                return DefendingPiece;
            }
            case SPY:
                if (this == MARSHAL)
                    return this;

            case MARSHAL:
                if (this == SPY)
                    return this;
        }
        if (this.PieceNumber > DefendingPiece.PieceNumber)
            return this;
        if (this.PieceNumber < DefendingPiece.PieceNumber)
            return DefendingPiece;
        return null;
    }

    @Override
    public String toString() {
        return super.toString() + "(%d)".formatted(PieceNumber);
    }

    /**
     * this function checks if the piece is movable.
     * it is movable if the piece's number is between 1 and 10
     */
    public boolean isMovable() {
        return 1 <= PieceNumber && PieceNumber <= 10;
    }
}
