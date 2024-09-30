package chess.moves;

import chess.*;
import chess.pieces.*;

public class Castle implements Move {
    private final Transform rookTransform;
    private final Transform kingTransform;
    private final boolean kingside;

    public Castle(Pos pos, King king, Rook rook, boolean kingside) {
        this.kingside = kingside;
        if (king.getColor() == rook.getColor()) {
            int endY;
            if (king.getColor() == Side.WHITE) {
                endY = 1;
            } else {
                endY = 8;
            }
            if (kingside) {
                kingTransform = new Transform(pos, king.getLocation(), 7, endY, king);
                rookTransform = new Transform(pos, rook.getLocation(), 6, endY, rook);
            } else {
                kingTransform = new Transform(pos, king.getLocation(), 3, endY, king);
                rookTransform = new Transform(pos, rook.getLocation(), 4, endY, rook);
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public Class<? extends Piece> pieceType() {
        return King.class;
    }

    @Override
    public Coord getStart() {
        return kingTransform.getStart();
    }

    @Override
    public Coord getEnd() {
        return kingTransform.getEnd();
    }

    @Override
    public String toNotation() {
        if (kingside) {
            return "0-0";
        } else {
            return "0-0-0";
        }
    }

    @Override
    public String toString() {
        return toNotation();
    }

    @Override
    public void makeMove(Pos pos) {
        kingTransform.makeMove(pos);
        rookTransform.makeMove(pos);
    }

    @Override
    public Side getColor() {
        return kingTransform.getColor();
    }
}
