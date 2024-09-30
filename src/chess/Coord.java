package chess;

public class Coord {
    private int x;
    private int y;

    public Coord(int x, int y) {
        if (x >= 0 && x <= 8 && y >= 0 && y <= 8) {
            this.x = x;
            this.y = y;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String toNotation() {
        return getCol() + y;
    }

    public String toString() {
        return toNotation();
    }

    public String getRow() {
        return "" + y;
    }

    public String getCol() {
        return Coord.intToColumn(x);
    }

    public static String intToColumn(int x) {
        String column;
        if (x == 1) {
            column = "a";
        } else if (x == 2) {
            column = "b";
        } else if (x == 3) {
            column = "c";
        } else if (x == 4) {
            column = "d";
        } else if (x == 5) {
            column = "e";
        } else if (x == 6) {
            column = "f";
        } else if (x == 7) {
            column = "g";
        } else {
            column = "h";
        }
        return column;
    }

    public static Coord coordX(Coord c, int newX) {
        return (new Coord(newX, c.getY()));
    }

    public static Coord coordY(Coord c, int newY) {
        return (new Coord(c.getX(), newY));
    }

    public static boolean isDark(Coord c) {
        return ((c.getX() + c.getY()) % 2 == 0);
    }

    public static boolean isDark(int x, int y) {
        return isDark(new Coord(x, y));
    }

    public boolean isDark() {
        return isDark(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Coord coord = (Coord) o;
        return x == coord.x && y == coord.y;
    }
}
