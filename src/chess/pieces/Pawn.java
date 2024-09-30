package chess.pieces;

import chess.*;
import chess.moves.*;

import java.util.ArrayList;
import java.util.List;

public class Pawn implements Piece {
    private final Side side;
    private Coord square;

    public Pawn(Side side, Coord square) {
        this.side = side;
        this.square = square;
    }


    public List<Move> legalMoves(Pos pos) {
        List<Move> moves = new ArrayList<>();
        moves.addAll(legalTransforms(pos));
        moves.addAll(legalCaptures(pos));
        moves.addAll(legalPromotions(pos));
        moves.addAll(legalEnPassants(pos));
        moves.removeIf(pos::movesIntoCheck);
        return moves;
    }

    /**
     * All pawn unoccupied pawn transforms. Excludes promotion.
     * 
     * @param pos the position being considered
     * @return List of pawn transforms
     */
    @Override
    public List<Transform> legalTransforms(Pos pos) {
        ArrayList<Transform> transforms = new ArrayList<>();
        if (side == Side.WHITE) {
            if (square.getY() == 2) {
                transforms.add(new Transform(pos, square, square.getX(), 3, this));
                if (pos.isEmptySquare(square.getX(), 3)) {
                    transforms.add(new Transform(pos, square, square.getX(), 4, this));
                }
            } else if (square.getY() < 7) {
                transforms.add(new Transform(pos, square, square.getX(), square.getY() + 1, this));
            }
        } else {
            if (square.getY() == 7) {
                transforms.add(new Transform(pos, square, square.getX(), 6, this));
                if (pos.isEmptySquare(square.getX(), 6)) {
                    transforms.add(new Transform(pos, square, square.getX(), 5, this));
                }
            } else if (square.getY() > 2) {
                transforms.add(new Transform(pos, square, square.getX(), square.getY() - 1, this));
            }
        }
        transforms.removeIf(t -> !pos.isEmptySquare(t.getEnd()));
        return transforms;
    }

    /**
     * Excludes promotions
     * 
     * @param pos
     * @return
     */
    @Override
    public List<Capture> legalCaptures(Pos pos) {
        List<Capture> legalCaptures = new ArrayList<>();
        if (side == Side.WHITE && square.getY() < 7) {
            Piece p;
            if (square.getX() <= 7) {
                p = pos.getPieceOnSquare(square.getX() + 1, square.getY() + 1);
                if (p != null) {
                    if (p.getColor() == Side.BLACK) {
                        legalCaptures.add(new Capture(this, p));
                    }
                }
            }
            if (square.getX() >= 2) {
                p = pos.getPieceOnSquare(square.getX() - 1, square.getY() + 1);
                if (p != null) {
                    if (p.getColor() == Side.BLACK) {
                        legalCaptures.add(new Capture(this, p));
                    }
                }
            }

        } else if (side == Side.BLACK && square.getY() > 2) {
            Piece p;
            if (square.getX() <= 7) {
                p = pos.getPieceOnSquare(square.getX() + 1, square.getY() - 1);
                if (p != null) {
                    if (p.getColor() == Side.WHITE) {
                        legalCaptures.add(new Capture(this, p));
                    }
                }
            }
            if (square.getX() >= 2) {
                p = pos.getPieceOnSquare(square.getX() - 1, square.getY() - 1);
                if (p != null) {
                    if (p.getColor() == Side.WHITE) {
                        legalCaptures.add(new Capture(this, p));
                    }
                }
            }
        }
        return legalCaptures;
    }

    private List<EnPassant> legalEnPassants(Pos pos) {
        int x = square.getX();
        int y = square.getY();
        List<EnPassant> enPassants = new ArrayList<>();
        Move lastMove = pos.getLastMove();
        if (lastMove != null && lastMove.getClass() == Transform.class
                && lastMove.pieceType() == Pawn.class) {
            if (y == 5 && side == Side.WHITE) {
                if (x <= 7 && lastMove.getStart().equals(new Coord(x + 1, 7))
                        && lastMove.getEnd().getY() == 5) {
                    enPassants.add(new EnPassant(pos, square, new Coord(x + 1, 6)));
                }
                if (x >= 2 && lastMove.getStart().equals(new Coord(x - 1, 7))
                        && lastMove.getEnd().getY() == 5) {
                    enPassants.add(new EnPassant(pos, square, new Coord(x - 1, 6)));
                }
            } else if (y == 4 && side == Side.BLACK) {
                if (x <= 7 && lastMove.getStart().equals(new Coord(x + 1, 2))
                        && lastMove.getEnd().getY() == 4) {
                    enPassants.add(new EnPassant(pos, square, new Coord(x + 1, 3)));
                }
                if (x >= 2 && lastMove.getStart().equals(new Coord(x - 1, 2))
                        && lastMove.getEnd().getY() == 4) {
                    enPassants.add(new EnPassant(pos, square, new Coord(x - 1, 3)));
                }
            }

        }
        return enPassants;
    }

    private List<Promote> allPromoteOptions(Pos pos, Piece captured) {
        List<Promote> promotes = new ArrayList<>();
        if (captured == null) {
            promotes.add(new Promote(pos, square, Queen.class));
            promotes.add(new Promote(pos, square, Rook.class));
            promotes.add(new Promote(pos, square, Bishop.class));
            promotes.add(new Promote(pos, square, Knight.class));
        } else {
            promotes.add(new Promote(pos, square, Queen.class, captured));
            promotes.add(new Promote(pos, square, Rook.class, captured));
            promotes.add(new Promote(pos, square, Bishop.class, captured));
            promotes.add(new Promote(pos, square, Knight.class, captured));
        }
        return promotes;
    }

    public List<Promote> legalPromotions(Pos pos) {
        List<Promote> promotes = new ArrayList<>();
        if (side == Side.WHITE && square.getY() == 7) {
            if (pos.isEmptySquare(Coord.coordY(square, 8))) {
                promotes.addAll(allPromoteOptions(pos, null));
            }
            Piece p;
            if (square.getX() <= 7) {
                p = pos.getPieceOnSquare(new Coord(square.getX() + 1, 8));
                if (p != null && p.getColor() == Side.BLACK) {
                    promotes.addAll(allPromoteOptions(pos, p));
                }
            }
            if (square.getX() >= 2) {
                p = pos.getPieceOnSquare(new Coord(square.getX() - 1, 8));
                if (p != null && p.getColor() == Side.BLACK) {
                    promotes.addAll(allPromoteOptions(pos, p));
                }
            }

        } else if (side == Side.BLACK && square.getY() == 2) {
            if (pos.isEmptySquare(Coord.coordY(square, 1))) {
                promotes.addAll(allPromoteOptions(pos, null));
            }
            Piece p;
            if (square.getX() <= 7) {
                p = pos.getPieceOnSquare(new Coord(square.getX() + 1, 1));
                if (p != null && p.getColor() == Side.WHITE) {
                    promotes.addAll(allPromoteOptions(pos, p));
                }
            }
            if (square.getX() >= 2) {
                p = pos.getPieceOnSquare(new Coord(square.getX() - 1, 1));
                if (p != null && p.getColor() == Side.WHITE) {
                    promotes.addAll(allPromoteOptions(pos, p));
                }
            }
        }
        return promotes;
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
        return "";
    }

    @Override
    public String icon() {
        if (side == Side.WHITE) {
            return "wP";
        } else {
            return "bP";
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
