package chess.pieces;

import chess.Coord;
import chess.Pos;
import chess.Side;
import chess.moves.Capture;
import chess.moves.Move;
import chess.moves.Transform;

import java.util.ArrayList;
import java.util.List;

public class Rook implements Piece {
    private final Side side;
    private Coord square;

    private boolean hasMoved;

    public Rook(Side side, Coord square) {
        this.side = side;
        this.square = square;
        this.hasMoved = false;
    }

    @Override
    public List<Move> legalMoves(Pos pos) {
        List<Move> moves = new ArrayList<>();
        moves.addAll(legalTransforms(pos));
        moves.addAll(legalCaptures(pos));
        moves.removeIf(pos::movesIntoCheck);
        return moves;
    }

    public static List<Transform> straightTransforms(Piece piece, Pos pos) {
        Coord square = piece.getLocation();
        ArrayList<Transform> transforms = new ArrayList<>();
        for (int col = square.getX() + 1; col <= 8
                && pos.isEmptySquare(col, square.getY()); col++) {
            transforms.add(new Transform(pos, square, col, square.getY(), piece));
        }
        for (int col = square.getX() - 1; col >= 1
                && pos.isEmptySquare(col, square.getY()); col--) {
            transforms.add(new Transform(pos, square, col, square.getY(), piece));
        }
        for (int row = square.getY() + 1; row <= 8
                && pos.isEmptySquare(square.getX(), row); row++) {
            transforms.add(new Transform(pos, square, square.getX(), row, piece));
        }
        for (int row = square.getY() - 1; row >= 1
                && pos.isEmptySquare(square.getX(), row); row--) {
            transforms.add(new Transform(pos, square, square.getX(), row, piece));
        }
        return transforms;
    }

    @Override
    public List<Transform> legalTransforms(Pos pos) {
        return straightTransforms(this, pos);
    }

    public static List<Capture> straightCaptures(Piece piece, Pos pos) {
        Coord square = piece.getLocation();
        ArrayList<Capture> captures = new ArrayList<>();
        boolean search = true;
        for (int col = square.getX() + 1; col <= 8 && search; col++) {
            Piece p = pos.getPieceOnSquare(col, square.getY());
            if (p != null) {
                search = false;
                if (p.getColor() != piece.getColor()) {
                    captures.add(new Capture(piece, p));
                }
            }
        }
        search = true;
        for (int col = square.getX() - 1; col >= 1 && search; col--) {
            Piece p = pos.getPieceOnSquare(col, square.getY());
            if (p != null) {
                search = false;
                if (p.getColor() != piece.getColor()) {
                    captures.add(new Capture(piece, p));
                }
            }
        }
        search = true;
        for (int row = square.getY() + 1; row <= 8 && search; row++) {
            Piece p = pos.getPieceOnSquare(square.getX(), row);
            if (p != null) {
                search = false;
                if (p.getColor() != piece.getColor()) {
                    captures.add(new Capture(piece, p));
                }
            }
        }
        search = true;
        for (int row = square.getY() - 1; row >= 1 && search; row--) {
            Piece p = pos.getPieceOnSquare(square.getX(), row);
            if (p != null) {
                search = false;
                if (p.getColor() != piece.getColor()) {
                    captures.add(new Capture(piece, p));
                }
            }
        }
        return captures;
    }

    @Override
    public List<Capture> legalCaptures(Pos pos) {
        return straightCaptures(this, pos);
    }

    @Override
    public Coord getLocation() {
        return square;
    }

    @Override
    public void setLocation(Coord coord) {
        this.square = coord;
    }

    @Override
    public Side getColor() {
        return side;
    }

    @Override
    public String toNotation() {
        return "R";
    }

    @Override
    public String icon() {
        if (side == Side.WHITE) {
            return "wR";
        } else {
            return "bR";
        }
    }

    @Override
    public void setHasMoved() {
        hasMoved = true;
    }

    @Override
    public boolean getHasMoved() {
        return hasMoved;
    }
}
