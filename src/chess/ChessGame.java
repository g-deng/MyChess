package chess;

import chess.moves.*;
import chess.pieces.*;

import java.util.ArrayList;
import java.util.List;

import static chess.GameDisplay.endGameMessage;

public class ChessGame {
    private Side toMove;
    private final Pos myPos;
    private final List<Move> moves;
    private final List<List<Piece>> captured;
    private final List<Piece[][]> boards;
    private Result result;
    private int moveCounter50;

    private boolean enPassantForced = false;

    public ChessGame() {
        toMove = Side.WHITE;
        myPos = new Pos();
        moves = new ArrayList<>();
        moves.add(null);
        captured = new ArrayList<>();
        boards = new ArrayList<>();
        boards.add(myPos.saveBoard());
        result = Result.IN_PROGRESS;
    }

    public Piece[][] getLastBoard() {
        return boards.get(boards.size() - 1);
    }

    public List<Piece> getLastBoardAsList() {
        List<Piece> ret = new ArrayList<>();
        for (int col = 1; col <= 8; col++) {
            for (int row = 1; row <= 8; row++) {
                if (!myPos.isEmptySquare(col, row)) {
                    ret.add(myPos.getPieceOnSquare(col, row));
                }
            }
        }
        return ret;
    }

    public List<Move> allPossibleMoves() {
        return myPos.allLegalMoves(toMove);
    }

    public Piece[][] getBoardAtMove(int x) {
        return boards.get(x);
    }

    public String getMoveOrder() {
        StringBuilder s = new StringBuilder();
        for (int i = 1; i < moves.size(); i++) {
            if (i % 2 == 1) {
                s.append(i / 2 + 1).append(". ");
            }
            s.append(moves.get(i).toNotation()).append("     ");
            if (i % 2 == 0) {
                s.append("\n");
            }
        }
        return s.toString();
    }

    public List<String> getMoveLines() {
        List<String> m = new ArrayList<>();
        StringBuilder s = new StringBuilder();
        for (int i = 1; i < moves.size()/2 + 1; i++) {
            m.add(i + ". " + moves.get(i/2 + 1).toNotation() + "     "
                    + moves.get(i/2 + 2).toNotation() + "     "
                    + "\n");
        }
        return m;
    }

    public List<Coord> possibleMoves(Coord c) {
        if (result == Result.IN_PROGRESS) {
            if (c != null && !myPos.isEmptySquare(c)) {
                Piece p = myPos.getPieceOnSquare(c);
                List<Coord> coords = new ArrayList<>();
                if (p.getColor() == toMove) {
                    for (Move m : p.legalMoves(myPos)) {
                        coords.add(m.getEnd());
                    }
                }
                return coords;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public void actualMakeMove(Coord start, Coord end) {
        if (result == Result.IN_PROGRESS) {
            Move m = myPos.findMove(toMove, start, end);
            if (m != null) {
                if (m.getColor() == Side.WHITE) {
                    whiteMove(m);
                } else {
                    blackMove(m);
                }
                myPos.setLastMove(m);
                // Move made, side changed
                inc50MoveCounter(m); // 50 move LOLOL
                System.out.println(m.toNotation());
            }
            // Check game status
            if (myPos.isStalemate(toMove)) {
                endGame(Result.STALEMATE);
            } else if (myPos.isCheckmate(toMove)) {
                endGame(toMove.sideToOppResult());
            } else if (moveCounter50 >= 50) {
                endGame(Result.DRAW_BY_50_MOVE);
            }
            handleEnPassant(); // auto en passant lol
        } else {
            System.out.println(result);
        }
    }

    public void handleEnPassant() {
        if (enPassantForced) {
            for (Move m : allPossibleMoves()) {
                if (m.getClass() == EnPassant.class) {
                    actualMakeMove(m.getStart(), m.getEnd());
                }
            }
        }
    }

    public void setEnPassantForced(boolean forced) {
        enPassantForced = forced;
    }

    public void endGame(Result r) {
        this.result = r;
        endGameMessage(this);
    }

    public Result getResult() {
        return result;
    }

    private void inc50MoveCounter(Move m) {
        if (m.getClass() == Capture.class || m.getClass() == Castle.class
                || m.pieceType() == Pawn.class) {
            moveCounter50 = 0;
        } else {
            moveCounter50++;
        }
    }

    private void savePosData() {
        captured.add(myPos.saveCaptured());
        boards.add(myPos.saveBoard());
    }

    private void whiteMove(Move m) {
        if (toMove == Side.WHITE && m.getColor() == Side.WHITE) {
            m.makeMove(myPos);
            toMove = Side.BLACK;
            moves.add(m);
            savePosData();
        } else {
            throw new IllegalCallerException("Not White's turn");
        }
    }

    private void blackMove(Move m) {
        if (toMove == Side.BLACK && m.getColor() == Side.BLACK) {
            m.makeMove(myPos);
            toMove = Side.WHITE;
            moves.add(m);
            savePosData();
        } else {
            throw new IllegalCallerException("Not White's turn");
        }
    }

    public Side getToMove() {
        return toMove;
    }

    public String getToMoveAsString() {
        if (toMove == Side.WHITE) {
            return "White to Move";
        } else {
            return "Black to Move";
        }
    }

    public String getPosAsString() {
        return myPos.toString();
    }
}
