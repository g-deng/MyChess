package chess.moves;

import chess.*;
import chess.pieces.Piece;

public interface Move {
    Class<? extends Piece> pieceType();

    Coord getStart();

    Coord getEnd();

    String toNotation();

    void makeMove(Pos pos);

    Side getColor();
}
