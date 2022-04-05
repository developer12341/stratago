package MVC.Stratagies.BoardComputerView;

import MVC.model.Piece;

import java.util.Arrays;

public class PossiblePiece implements Cloneable {
    private float[] possiblePieces;
    private Piece piece;

    public PossiblePiece() {
        possiblePieces = new float[12];
    }

    @Override
    public PossiblePiece clone() {
        try {
            PossiblePiece clone = (PossiblePiece) super.clone();
            clone.possiblePieces = possiblePieces.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    @Override
    public String toString() {
        return "PossiblePiece{" +
                "possiblePieces=" + Arrays.toString(possiblePieces) +
                '}';
    }

    public Piece getExpectedPiece(int[] invisiblePieceCount, int invisiblePieceSum) {
        if (piece != null) {
            return piece;
        }

        //find the largest value in the array - the largest value is the expected piece.
        int piece = -1;
        float max = 0;

        for (int i = 0; i < possiblePieces.length; i++) {
            if (possiblePieces[i] * (float) invisiblePieceCount[i] / invisiblePieceSum > max) {
                max = possiblePieces[i] * (float) invisiblePieceCount[i] / invisiblePieceSum;
                piece = i;
            }
        }

        if (piece == -1)
            return Piece.PLACEHOLDER;

        return Piece.fromInteger(piece + 1);
    }

    public void updateProbability(int pieceNum, int amountOfAppearances, int sampleSize) {
        possiblePieces[pieceNum] = (float) amountOfAppearances / sampleSize;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public void setProbability(Piece piece, int probability) {
        possiblePieces[piece.PieceNumber - 1] = probability;
    }

    public float getProbability(Piece piece) {
        return possiblePieces[piece.PieceNumber - 1];
    }

    public Piece getPiece() {
        return piece;
    }
}
