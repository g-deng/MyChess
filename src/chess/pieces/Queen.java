package chess.pieces;

import chess.Coord;
import chess.Pos;
import chess.Side;
import chess.moves.Capture;
import chess.moves.Move;
import chess.moves.Transform;

import java.util.ArrayList;
import java.util.List;

public class Queen implements Piece {
    private final Side side;
    private Coord c;

    public Queen(Side side, Coord square) {
        this.side = side;
        this.c = square;
    }

    @Override
    public List<Move> legalMoves(Pos pos) {
        List<Move> moves = new ArrayList<>();
        moves.addAll(legalTransforms(pos));
        moves.addAll(legalCaptures(pos));
        moves.removeIf(pos::movesIntoCheck);
        return moves;
    }

    @Override
    public List<Transform> legalTransforms(Pos pos) {
        List<Transform> transforms = new ArrayList<>();
        transforms.addAll(Rook.straightTransforms(this, pos));
        transforms.addAll(Bishop.diagonalTransforms(this, pos));
        return transforms;
    }

    @Override
    public List<Capture> legalCaptures(Pos pos) {
        List<Capture> captures = new ArrayList<>();
        captures.addAll(Rook.straightCaptures(this, pos));
        captures.addAll(Bishop.diagonalCaptures(this, pos));
        return captures;
    }

    @Override
    public Coord getLocation() {
        return c;
    }

    @Override
    public void setLocation(Coord coord) {
        this.c = coord;
    }

    @Override
    public Side getColor() {
        return side;
    }

    @Override
    public String toNotation() {
        return "Q";
    }

    @Override
    public String icon() {
        if (side == Side.WHITE) {
            return "wQ";
        } else {
            return "bQ";
        }
    }

    @Override
    public void setHasMoved() {

    }

    @Override
    public boolean getHasMoved() {
        return false;
    }
}
