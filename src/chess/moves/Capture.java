package chess.moves;

import chess.Coord;
import chess.Pos;
import chess.Side;
import chess.pieces.*;

public class Capture implements Move {
    private final Coord start;
    private final Coord end;
    private final Piece piece;
    private final Piece captured;

    public Capture(Coord start, Coord end, Piece piece, Piece captured) {
        if (captured == null) {
            throw new IllegalArgumentException();
        }
        this.start = start;
        this.end = end;
        this.piece = piece;
        this.captured = captured;
    }

    public Capture(Pos pos, Coord start, Coord end) {
        this(start, end, pos.getPieceOnSquare(start), pos.getPieceOnSquare(end));
    }

    public Capture(Piece piece, Piece captured) {
        this(piece.getLocation(), captured.getLocation(), piece, captured);
    }

    @Override
    public Class<? extends Piece> pieceType() {
        return piece.getClass();
    }

    @Override
    public Coord getStart() {
        return start;
    }

    @Override
    public Coord getEnd() {
        return end;
    }

    public Piece getCaptured() {
        return captured;
    }

    @Override
    public String toNotation() {
        String modifier = "";
        if (piece.getClass() == Pawn.class) {
            modifier = start.getCol();
        }
        return piece.toNotation() + modifier + "x" + end.toNotation();
    }

    @Override
    public String toString() {
        return toNotation();
    }

    @Override
    public void makeMove(Pos pos) {
        pos.addCaptured(captured);
        pos.changeCoords(piece, end);
    }

    @Override
    public Side getColor() {
        return piece.getColor();
    }
}
