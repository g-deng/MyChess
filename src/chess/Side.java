package chess;

import chess.moves.Result;

public enum Side {
    BLACK, WHITE;

    public Side otherSide() {
        if (this == WHITE) {
            return BLACK;
        } else {
            return WHITE;
        }
    }

    public Result sideToOppResult() {
        if (this == WHITE) {
            return Result.BLACK;
        } else {
            return Result.WHITE;
        }
    }
}
