package MVC.model;

import java.text.MessageFormat;

public class MoveLib {
    public static int pack(int RowFrom, int ColFrom, int RowTo, int ColTo) {
        return (RowFrom << 12) | (ColFrom << 8) | (RowTo << 4) | ColTo;
    }
    public static int unpackRowFrom(int packed) {
        return packed >> 12;
    }

    public static int unpackColFrom(int packed) {
        return (packed >> 8) & 0xF;
    }

    public static int unpackRowTo(int packed) {
        return (packed >> 4) & 0xF;
    }

    public static int unpackColTo(int packed) {
        return packed & 0xF;
    }

    public static String getFrom(int move) {
        return MessageFormat.format("({0}, {1})", unpackRowFrom(move), unpackColFrom(move));
    }

    public static Move convertToMove(int bestMove) {
        return Move.create(unpackRowFrom(bestMove), unpackColFrom(bestMove), unpackRowTo(bestMove), unpackColTo(bestMove));
    }
}
