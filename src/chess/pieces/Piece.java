package chess.pieces;

import chess.*;
import chess.moves.*;

import java.util.List;

public interface Piece {

    static int PIECE_SIZE = GameDisplay.getBoardSize() / 8;

    /**
     * Returns all legal moves of this piece at the given position. May be empty if
     * there are no legal moves.
     * Castling is attributed as a King move and not a Rook move.
     * 
     * @param pos the position begin considered
     * @return List of all legal Moves
     */
    List<Move> legalMoves(Pos pos);

    /**
     * Returns all unoccupied squares the piece can move to. Doesn't consider check.
     * 
     * @param pos the position being considered
     * @return List of all legal Transforms (not considering check)
     */
    List<Transform> legalTransforms(Pos pos);

    List<Capture> legalCaptures(Pos pos);

    Coord getLocation();

    void setLocation(Coord coord);

    Side getColor();

    String toNotation();

    String icon();

    void setHasMoved();

    boolean getHasMoved();

    static String pieceTypeToNotation(Class<? extends Piece> p) {
        if (p == Pawn.class) {
            return "";
        } else if (p == Knight.class) {
            return "N";
        } else if (p == Bishop.class) {
            return "B";
        } else if (p == Rook.class) {
            return "R";
        } else if (p == Queen.class) {
            return "Q";
        } else if (p == King.class) {
            return "K";
        } else {
            return null;
        }
    }

}