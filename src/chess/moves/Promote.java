package chess.moves;

import chess.*;
import chess.pieces.*;

public class Promote implements Move {
    private final Pawn p;
    private final Class<? extends Piece> promotedTo;
    private final Coord start;
    private final Coord end;
    private final Piece captured;

    /**
     * Throws IllegalArgumentException if promoted is the wrong type
     * MAIN CONSTRUCTOR
     * 
     * @param pos
     * @param p
     * @param promotedTo
     * @param start
     * @param end
     * @param captured
     */
    public Promote(
            Pawn p, Class<? extends Piece> promotedTo, Coord start, Coord end, Piece captured
    ) {
        this.p = p;
        if (promotedTo == Pawn.class || promotedTo == King.class) {
            throw new IllegalArgumentException();
        }
        this.promotedTo = promotedTo;
        this.start = start;
        this.end = end;
        this.captured = captured;
    }

    private static Coord findEnd(Pawn p) {
        if (p.getColor() == Side.WHITE) {
            return Coord.coordY(p.getLocation(), 8);
        } else {
            return Coord.coordY(p.getLocation(), 1);
        }
    }

    public Promote(Pos pos, Coord start, Class<? extends Piece> promotedTo) {
        this((Pawn) pos.getPieceOnSquare(start), promotedTo);
    }

    public Promote(Pawn p, Class<? extends Piece> promotedTo) {
        this(p, promotedTo, p.getLocation(), findEnd(p), null);
    }

    public Promote(Pawn p, Class<? extends Piece> promotedTo, Piece captured) {
        this(p, promotedTo, p.getLocation(), captured.getLocation(), captured);
    }

    public Promote(Pos pos, Coord start, Class<? extends Piece> promotedTo, Piece captured) {
        this((Pawn) pos.getPieceOnSquare(start), promotedTo, captured);
    }

    @Override
    public Class<? extends Piece> pieceType() {
        return Pawn.class;
    }

    @Override
    public Coord getStart() {
        return start;
    }

    @Override
    public Coord getEnd() {
        return end;
    }

    @Override
    public String toNotation() {
        if (captured == null) {
            return end.toNotation() + "=" + Piece.pieceTypeToNotation(promotedTo);
        } else {
            return start.getCol() + "x" + end.toNotation() + "="
                    + Piece.pieceTypeToNotation(promotedTo);
        }
    }

    @Override
    public String toString() {
        return toNotation();
    }

    @Override
    public void makeMove(Pos pos) {
        if (captured != null) {
            pos.addCaptured(captured);
        }
        pos.removePiece(p);
        Piece newPiece;
        if (promotedTo == Knight.class) {
            newPiece = new Knight(p.getColor(), end);
        } else if (promotedTo == Bishop.class) {
            newPiece = new Bishop(p.getColor(), end);
        } else if (promotedTo == Rook.class) {
            newPiece = new Rook(p.getColor(), end);
        } else {
            newPiece = new Queen(p.getColor(), end);
        }
        pos.addPiece(newPiece, end);
    }

    @Override
    public Side getColor() {
        return p.getColor();
    }
}
