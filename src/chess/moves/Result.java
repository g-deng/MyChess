package chess.moves;

public enum Result {
    WHITE(100), BLACK_RESIGNS(101), BLACK(200), WHITE_RESIGNS(201), DRAW_BY_AGREEMENT(301),
    STALEMATE(300), DRAW_BY_50_MOVE(302), DRAW_BY_3_MOVE_REP(303),
    DRAW_BY_INSUFFICIENT_MATERIAL(304),
    IN_PROGRESS(400);

    private final int i;

    Result(int i) {
        this.i = i;
    }

    public int getI() {
        return i;
    }

    @Override
    public String toString() {
        return switch (this) {
            case WHITE -> "White Wins by Checkmate";
            case BLACK_RESIGNS -> "White Wins by Resignation";
            case BLACK -> "Black Wins by Checkmate";
            case WHITE_RESIGNS -> "Black Wins by Resignation";
            case DRAW_BY_AGREEMENT -> "Drawn by Agreement";
            case DRAW_BY_3_MOVE_REP -> "Drawn by 3 Move Repetition";
            case DRAW_BY_50_MOVE -> "Drawn by 50 Move Rule";
            case DRAW_BY_INSUFFICIENT_MATERIAL -> "Drawn by Insufficient Material";
            case STALEMATE -> "Drawn by Stalemate";
            case IN_PROGRESS -> "";
        };
    }

    public String toNotation() {
        int a = i / 100;
        if (a == 1) {
            return "1-0";
        } else if (a == 2) {
            return "0-1";
        } else if (a == 3) {
            return "1/2-1/2";
        } else {
            return "";
        }
    }
}
