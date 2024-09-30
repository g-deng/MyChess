package chess.moves;

import chess.Coord;
import chess.Pos;
import chess.Side;
import chess.pieces.*;

public class Transform implements Move {
    private final Pos pos;
    private final Coord start;
    private final Coord end;
    private final Piece piece;

    public Transform(Pos pos, Coord start, Coord end, Piece piece) {
        this.pos = pos;
        this.start = start;
        this.end = end;
        this.piece = piece;
    }

    public Transform(Pos pos, Coord start, int endX, int endY, Piece piece) {
        this(pos, start, new Coord(endX, endY), piece);
    }

    @Override
    public Class<? extends Piece> pieceType() {
        return piece.getClass();
    }

    @Override
    public Coord getStart() {
        return start;
    }

    public Coord getEnd() {
        return end;
    }

    @Override
    public String toNotation() {
        // Deals with pieces on the same row or column
        String modifier = "";
        // if (piece.getClass() == Knight.class || piece.getClass() == Rook.class ||
        // piece.getClass() == Queen.class) {
        // ArrayList<Coord> locations =
        // pos.getLocations(piece.getClass(),piece.getColor());
        // int sameX = 0;
        // int sameY = 0;
        // for (Coord l : locations) {
        // if (l != start) {
        // if (l.getX() == start.getX()) {
        // sameX++;
        // } else if (l.getY() == start.getY()) {
        // sameY++;
        // }
        // }
        // }
        // if (sameX > 0 && sameY > 0 || sameX > 1 || sameY > 1) {
        // modifier = start.toNotation();
        // } else if (sameX == 1) {
        // modifier = start.getRow();
        // } else if (sameY == 1) {
        // modifier = start.getCol();
        // }
        // }
        return piece.toNotation() + modifier + end.toNotation();
    }

    @Override
    public String toString() {
        return toNotation();
    }

    @Override
    public void makeMove(Pos pos) {
        pos.changeCoords(piece, end);
        if (piece.getClass() == King.class || piece.getClass() == Rook.class) {
            piece.setHasMoved();
        }
    }

    @Override
    public Side getColor() {
        return piece.getColor();
    }
}
