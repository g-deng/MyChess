package chess.pieces;

import chess.*;
import chess.moves.*;

import java.util.ArrayList;
import java.util.List;

public class Bishop implements Piece {
    private final Side side;
    private Coord c;

    public Bishop(Side side, Coord square) {
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

    public static List<Transform> diagonalTransforms(Piece piece, Pos pos) {
        Coord c = piece.getLocation();
        ArrayList<Transform> transforms = new ArrayList<>();
        int x = c.getX() + 1;
        int y = c.getY() + 1;
        while (x <= 8 && y <= 8 && pos.isEmptySquare(x, y)) {
            transforms.add(new Transform(pos, c, x, y, piece));
            x++;
            y++;
        }
        x = c.getX() + 1;
        y = c.getY() - 1;
        while (x <= 8 && y >= 1 && pos.isEmptySquare(x, y)) {
            transforms.add(new Transform(pos, c, x, y, piece));
            x++;
            y--;
        }
        x = c.getX() - 1;
        y = c.getY() - 1;
        while (x >= 1 && y >= 1 && pos.isEmptySquare(x, y)) {
            transforms.add(new Transform(pos, c, x, y, piece));
            x--;
            y--;
        }
        x = c.getX() - 1;
        y = c.getY() + 1;
        while (x >= 1 && y <= 8 && pos.isEmptySquare(x, y)) {
            transforms.add(new Transform(pos, c, x, y, piece));
            x--;
            y++;
        }
        return transforms;
    }

    @Override
    public List<Transform> legalTransforms(Pos pos) {
        return Bishop.diagonalTransforms(this, pos);
    }

    public static List<Capture> diagonalCaptures(Piece piece, Pos pos) {
        Coord c = piece.getLocation();
        ArrayList<Capture> captures = new ArrayList<>();
        int x = c.getX() + 1;
        int y = c.getY() + 1;
        boolean search = true;
        while (x <= 8 && y <= 8 && search) {
            Piece p = pos.getPieceOnSquare(x, y);
            if (p != null) {
                search = false;
                if (p.getColor() != piece.getColor()) {
                    captures.add(new Capture(pos, c, new Coord(x, y)));
                }
            }
            x++;
            y++;
        }
        x = c.getX() + 1;
        y = c.getY() - 1;
        search = true;
        while (x <= 8 && y >= 1 && search) {
            Piece p = pos.getPieceOnSquare(x, y);
            if (p != null) {
                search = false;
                if (p.getColor() != piece.getColor()) {
                    captures.add(new Capture(pos, c, new Coord(x, y)));
                }
            }
            x++;
            y--;
        }
        x = c.getX() - 1;
        y = c.getY() - 1;
        search = true;
        while (x >= 1 && y >= 1 && search) {
            Piece p = pos.getPieceOnSquare(x, y);
            if (p != null) {
                search = false;
                if (p.getColor() != piece.getColor()) {
                    captures.add(new Capture(pos, c, new Coord(x, y)));
                }
            }
            x--;
            y--;
        }
        x = c.getX() - 1;
        y = c.getY() + 1;
        search = true;
        while (x >= 1 && y <= 8 && search) {
            Piece p = pos.getPieceOnSquare(x, y);
            if (p != null) {
                search = false;
                if (p.getColor() != piece.getColor()) {
                    captures.add(new Capture(pos, c, new Coord(x, y)));
                }
            }
            x--;
            y++;
        }
        return captures;
    }

    @Override
    public List<Capture> legalCaptures(Pos pos) {
        return diagonalCaptures(this, pos);
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
        return "B";
    }

    @Override
    public String icon() {
        if (side == Side.WHITE) {
            return "wB";
        } else {
            return "bB";
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
