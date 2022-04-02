package MVC.model;

import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public enum Piece {
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
    public final int PieceNumber;
    public final Image redPieceImage;
    public final Image bluePieceImage;
    Piece(int pieceNumber) {
        PieceNumber = pieceNumber;
        redPieceImage = loadImage(PiecesFolder + "red/red_" + pieceNumber + ".png");
        bluePieceImage = loadImage(PiecesFolder + "blue/blue_" + pieceNumber + ".png");
    }

    private Image loadImage(String filePath){
        try {
            return new Image(new FileInputStream(filePath));
        } catch (FileNotFoundException e) {
            return null;
        }
    }

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
            case (11), ('B') -> BOMB;
            case (12), ('F') -> FLAG;
            case (13) -> PLACEHOLDER;
            default -> throw new IllegalStateException("Unexpected value: " + value);
        };

    }



    /**
     * this function is returning the winning piece in the case that
     * this piece is attacking some other piece.
     * @param DefendingPiece the piece that is defending
     * @return the winning piece
     */
    public Piece Attack(Piece DefendingPiece) {
        if(DefendingPiece == null)
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

    public boolean isMovable() {
        return 1 <= PieceNumber && PieceNumber <= 10;
    }
}
