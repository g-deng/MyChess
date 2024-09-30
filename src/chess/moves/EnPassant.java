package chess.moves;

import chess.Coord;
import chess.Pos;
import chess.Side;
import chess.pieces.Pawn;
import chess.pieces.Piece;

public class EnPassant implements Move {
    private final Coord start;
    private final Coord end;
    private final Piece pawn;
    private final Piece captured;

    public EnPassant(Pos pos, Coord start, Coord end) {
        this.start = start;
        this.end = end;
        this.pawn = pos.getPieceOnSquare(start);
        if (pawn.getColor() == Side.WHITE) {
            this.captured = pos.getPieceOnSquare(Coord.coordY(end, 5));
        } else {
            this.captured = pos.getPieceOnSquare(Coord.coordY(end, 4));
        }
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
        return start.getCol() + "x" + end.toNotation();
    }

    @Override
    public void makeMove(Pos pos) {
        pos.changeCoords(pawn, end);
        pos.addCaptured(captured);
    }

    @Override
    public Side getColor() {
        return pawn.getColor();
    }
}
