package chess;

import chess.moves.Capture;
import chess.moves.Move;
import chess.pieces.*;
import java.util.ArrayList;
import java.util.List;

public class Pos {
    // Uses column-major layout (board [column] [row])
    private final Piece[][] board;
    private final List<Piece> captured;

    private Move lastMove;

    /**
     * Creates a position based on a pre-determined board array. Must be 9x9 with
     * row and column 0 empty.
     * Throws IllegalArgumentException if it's the wrong size.
     *
     * @param board the board
     */
    public Pos(Piece[][] board) {
        this(board, new ArrayList<>());
    }

    public Pos(Piece[][] board, List<Piece> captured) {
        if (board.length == 9 && board[0].length == 9) {
            this.board = board;
            this.captured = captured;
        } else {
            throw new IllegalArgumentException();
        }
        this.lastMove = null;
    }

    public Pos() {
        board = new Piece[9][9];
        captured = new ArrayList<>();
        this.lastMove = null;

        for (int col = 1; col <= 8; col++) {
            board[col][2] = new Pawn(Side.WHITE, new Coord(col, 2));
            board[col][7] = new Pawn(Side.BLACK, new Coord(col, 7));
        }

        board[2][1] = new Knight(Side.WHITE, new Coord(2, 1));
        board[7][1] = new Knight(Side.WHITE, new Coord(7, 1));
        board[2][8] = new Knight(Side.BLACK, new Coord(2, 8));
        board[7][8] = new Knight(Side.BLACK, new Coord(7, 8));

        board[3][1] = new Bishop(Side.WHITE, new Coord(3, 1));
        board[6][1] = new Bishop(Side.WHITE, new Coord(6, 1));
        board[3][8] = new Bishop(Side.BLACK, new Coord(3, 8));
        board[6][8] = new Bishop(Side.BLACK, new Coord(6, 8));

        board[1][1] = new Rook(Side.WHITE, new Coord(1, 1));
        board[8][1] = new Rook(Side.WHITE, new Coord(8, 1));
        board[1][8] = new Rook(Side.BLACK, new Coord(1, 8));
        board[8][8] = new Rook(Side.BLACK, new Coord(8, 8));

        board[4][1] = new Queen(Side.WHITE, new Coord(4, 1));
        board[4][8] = new Queen(Side.BLACK, new Coord(4, 8));

        board[5][1] = new King(Side.WHITE, new Coord(5, 1));
        board[5][8] = new King(Side.BLACK, new Coord(5, 8));
    }

    public List<Piece> allPiecesOfSide(Side s) {
        return allPiecesOfSide(this.board, s);
    }

    public static List<Piece> allPiecesOfSide(Piece[][] board, Side s) {
        List<Piece> p = new ArrayList<>();
        Piece mP;
        for (int col = 1; col <= 8; col++) {
            for (int row = 1; row <= 8; row++) {
                mP = board[col][row];
                if (mP != null && mP.getColor() == s) {
                    p.add(mP);
                }
            }
        }
        return p;
    }

    public List<Move> allLegalMoves(Side toMove) {
        List<Move> moves = new ArrayList<>();
        for (Piece p : allPiecesOfSide(toMove)) {
            if (p != null) {
                moves.addAll(p.legalMoves(this));
            }
        }
        moves.removeIf(this::movesIntoCheck);
        return moves;
    }

    public Move findMove(Side toMove, Coord start, Coord end) {
        for (Move m : allLegalMoves(toMove)) {
            if (m.getStart().equals(start) && m.getEnd().equals(end) && m.getColor() == toMove) {
                return m;
            }
        }
        return null;
    }

    public boolean isCheck(Side toMove) {
        for (Piece p : allPiecesOfSide(toMove.otherSide())) {
            if (p != null) {
                for (Capture c : p.legalCaptures(this)) {
                    if (c.getCaptured().getClass() == King.class) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean isCheck(Piece[][] board, Side toMove) {
        Pos pos = new Pos(board);
        for (Piece p : pos.allPiecesOfSide(toMove.otherSide())) {
            if (p != null) {
                for (Capture c : p.legalCaptures(pos)) {
                    if (c.getCaptured().getClass() == King.class) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean movesIntoCheck(Move m) {
        Piece[][] hypBoard = saveBoard();
        Coord end = m.getEnd();
        Coord start = m.getStart();
        hypBoard[end.getX()][end.getY()] = hypBoard[start.getX()][start.getY()];
        hypBoard[start.getX()][start.getY()] = null;
        return Pos.isCheck(hypBoard, m.getColor());
    }

    public boolean isCheckmate(Side toMove) {
        return (isCheck(toMove) && allLegalMoves(toMove).isEmpty());
    }

    public boolean isStalemate(Side toMove) {
        return !isCheck(toMove) && allLegalMoves(toMove).isEmpty();
    }

    public void addPiece(Piece p, Coord c) {
        board[c.getX()][c.getY()] = p;
        p.setLocation(c);
    }

    public void setLastMove(Move m) {
        this.lastMove = m;
    }

    public Move getLastMove() {
        return lastMove;
    }

    public Piece[][] saveBoard() {
        Piece[][] ret = new Piece[9][9];
        for (int col = 0; col <= 8; col++) {
            System.arraycopy(board[col], 0, ret[col], 0, 9);
        }
        return ret;
    }

    public List<Piece> saveCaptured() {
        return new ArrayList<>(captured);
    }

    // Not encapsulated LOL ;)
    public Piece getPieceOnSquare(int col, int row) {
        return board[col][row];
    }

    public Piece getPieceOnSquare(Coord coord) {
        return getPieceOnSquare(coord.getX(), coord.getY());
    }

    public boolean isEmptySquare(Coord coord) {
        return (board[coord.getX()][coord.getY()] == null);
    }

    public boolean isEmptySquare(int x, int y) {
        return isEmptySquare(new Coord(x, y));
    }

    /**
     * Gets an ArrayList of coordinates for all pieces of a certain type
     *
     * @param pieceType any class implementing Piece
     * @return coordinates of all SAME COLOR pieces of that type, empty if none
     */
    public ArrayList<Coord> getLocations(Class<? extends Piece> pieceType, Side side) {
        ArrayList<Coord> locations = new ArrayList<>();
        for (int col = 1; col <= 8; col++) {
            for (int row = 1; row <= 8; row++) {
                Piece p = board[col][row];
                if (p != null) {
                    if (p.getClass() == pieceType) {
                        if (p.getColor() == side) {
                            locations.add(p.getLocation());
                        }
                    }
                }
            }
        }
        return locations;
    }

    private void setSquare(Piece piece, Coord square) {
        board[square.getX()][square.getY()] = piece;
    }

    public void removePiece(Piece p) {
        setSquare(null, p.getLocation());
        p.setLocation(new Coord(0, 0));
    }

    public void changeCoords(Piece piece, Coord end) {
        setSquare(null, piece.getLocation());
        setSquare(piece, end);
        piece.setLocation(end);
    }

    public void addCaptured(Piece p) {
        removePiece(p);
        captured.add(p);
    }

    public List<Piece> getCaptured() {
        return new ArrayList<>(captured);
    }

    public String toString() {
        StringBuilder ret = new StringBuilder();
        for (int row = 8; row >= 1; row--) {
            ret.append(row).append(" ");
            for (int col = 1; col <= 8; col++) {
                Piece p = board[col][row];
                ret.append("|");
                if (p == null) {
                    ret.append("   ");
                } else {
                    if (p.getColor() == Side.WHITE) {
                        ret.append("'");
                    } else {
                        ret.append(" ");
                    }
                    if (p.getClass() == Pawn.class) {
                        ret.append("P");
                    } else {
                        ret.append(p.toNotation());
                    }
                    if (p.getColor() == Side.WHITE) {
                        ret.append("'");
                    } else {
                        ret.append(" ");
                    }
                }
                ret.append("|");
            }
            ret.append("\n");
            ret.append("-----------------------------------------");
            ret.append("\n");
        }
        ret.append("  ");
        for (int col = 1; col <= 8; col++) {
            ret.append("  ").append(Coord.intToColumn(col)).append("  ");
        }
        return ret.toString();
    }

}
