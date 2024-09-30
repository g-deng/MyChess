package chess.pieces;

import chess.*;
import chess.moves.Capture;
import chess.moves.Move;
import chess.moves.Transform;

import java.util.ArrayList;
import java.util.List;

public class Knight implements Piece {
    private final Side side;
    private Coord c;

    public Knight(Side side, Coord square) {
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

    private Transform nTransform(Pos pos, Coord target) {
        return new Transform(pos, c, target, this);
    }

    private List<Coord> knightPointsTo(Pos pos) {
        List<Coord> coords = new ArrayList<>();
        int x;
        int y;
        for (int i = 1; i <= 2; i++) {
            int j = 3 - i;
            x = c.getX() + i;
            y = c.getY() + j;
            if (x <= 8 && y <= 8) {
                coords.add(new Coord(x, y));
            }
            y = c.getY() - j;
            if (x <= 8 && y >= 1) {
                coords.add(new Coord(x, y));
            }
            x = c.getX() - i;
            if (x >= 1 && y >= 1) {
                coords.add(new Coord(x, y));
            }
            y = c.getY() + j;
            if (x >= 1 && y <= 8) {
                coords.add(new Coord(x, y));
            }
        }
        return coords;
    }

    @Override
    public List<Transform> legalTransforms(Pos pos) {
        List<Transform> transforms = new ArrayList<>();
        for (Coord target : knightPointsTo(pos)) {
            if (pos.isEmptySquare(target)) {
                transforms.add(nTransform(pos, target));
            }
        }
        return transforms;
    }

    @Override
    public List<Capture> legalCaptures(Pos pos) {
        List<Capture> captures = new ArrayList<>();
        Piece p;
        for (Coord target : knightPointsTo(pos)) {
            p = pos.getPieceOnSquare(target);
            if (p != null && p.getColor() != this.getColor()) {
                captures.add(new Capture(pos, c, target));
            }
        }
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
        return "N";
    }

    @Override
    public String icon() {
        if (side == Side.WHITE) {
            return "wN";
        } else {
            return "bN";
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
