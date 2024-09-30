package chess.pieces;

import chess.Coord;
import chess.Pos;
import chess.Side;
import chess.moves.Capture;
import chess.moves.Castle;
import chess.moves.Move;
import chess.moves.Transform;

import java.util.ArrayList;
import java.util.List;

public class King implements Piece {
    private final Side side;
    private Coord c;
    private boolean hasMoved;

    public King(Side side, Coord square) {
        this.side = side;
        this.c = square;
        this.hasMoved = false;
    }

    public void setHasMoved() {
        hasMoved = true;
    }

    @Override
    public boolean getHasMoved() {
        return hasMoved;
    }

    @Override
    public List<Move> legalMoves(Pos pos) {
        List<Move> moves = new ArrayList<>();
        moves.addAll(legalTransforms(pos));
        moves.addAll(legalCaptures(pos));
        moves.addAll(legalCastles(pos));
        moves.removeIf(pos::movesIntoCheck);
        return moves;
    }

    private Transform kTransform(Pos pos, Coord target) {
        return new Transform(pos, c, target, this);
    }

    private List<Coord> kingPointsTo(Pos pos) {
        List<Coord> coords = new ArrayList<>();
        int x;
        int y;
        for (int i = -1; i <= 1; i++) {
            x = c.getX() + i;
            y = c.getY() + 1;
            if (x <= 8 && y <= 8) {
                coords.add(new Coord(x, y));
            }
            y = c.getY() - 1;
            if (x <= 8 && y >= 1) {
                coords.add(new Coord(x, y));
            }
        }
        x = c.getX() + 1;
        y = c.getY();
        if (x <= 8) {
            coords.add(new Coord(x, y));
        }
        x = c.getX() - 1;
        if (x >= 1) {
            coords.add(new Coord(x, y));
        }
        return coords;
    }

    /**
     * All unoccupied legal King transforms. Does not consider check.
     * 
     * @param pos the position being considered
     * @return King transforms
     */
    @Override
    public List<Transform> legalTransforms(Pos pos) {
        List<Transform> transforms = new ArrayList<>();
        for (Coord target : kingPointsTo(pos)) {
            if (pos.isEmptySquare(target)) {
                transforms.add(kTransform(pos, target));
            }
        }
        return transforms;
    }

    /**
     * King captures. Does not consider check.
     * 
     * @param pos the position being considered
     * @return King captures
     */
    @Override
    public List<Capture> legalCaptures(Pos pos) {
        List<Capture> captures = new ArrayList<>();
        Piece p;
        for (Coord target : kingPointsTo(pos)) {
            p = pos.getPieceOnSquare(target);
            if (p != null && p.getColor() != this.getColor()) {
                captures.add(new Capture(pos, c, target));
            }
        }
        return captures;
    }

    public List<Castle> legalCastles(Pos pos) {
        List<Castle> castles = new ArrayList<>();
        if (!hasMoved && !pos.isCheck(side)) {
            if (c.equals(new Coord(5, 1)) && side == Side.WHITE) {
                Piece r = pos.getPieceOnSquare(8, 1);
                if (r != null && r.getClass() == Rook.class && r.getColor() == Side.WHITE
                        && !r.getHasMoved()
                        && pos.isEmptySquare(6, 1) && pos.isEmptySquare(7, 1)) {
                    castles.add(new Castle(pos, this, (Rook) r, true));
                }
                r = pos.getPieceOnSquare(1, 1);
                if (r != null && r.getClass() == Rook.class && r.getColor() == Side.WHITE
                        && !r.getHasMoved()
                        && pos.isEmptySquare(2, 1) && pos.isEmptySquare(3, 1)
                        && pos.isEmptySquare(4, 1)) {
                    castles.add(new Castle(pos, this, (Rook) r, false));
                }

            } else if (c.equals(new Coord(5, 8)) && side == Side.BLACK) {
                Piece r = pos.getPieceOnSquare(8, 8);
                if (r != null && r.getClass() == Rook.class && r.getColor() == Side.BLACK
                        && !r.getHasMoved()
                        && pos.isEmptySquare(6, 8) && pos.isEmptySquare(7, 8)) {
                    castles.add(new Castle(pos, this, (Rook) r, true));
                }
                r = pos.getPieceOnSquare(1, 8);
                if (r != null && r.getClass() == Rook.class && r.getColor() == Side.BLACK
                        && !r.getHasMoved()
                        && pos.isEmptySquare(2, 8) && pos.isEmptySquare(3, 8)
                        && pos.isEmptySquare(4, 8)) {
                    castles.add(new Castle(pos, this, (Rook) r, false));
                }

            }
        }
        return castles;
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
        return "K";
    }

    @Override
    public String icon() {
        if (side == Side.WHITE) {
            return "wK";
        } else {
            return "bK";
        }
    }
}
