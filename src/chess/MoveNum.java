package chess;

public class MoveNum {
    private int n;
    private Side c;

    public MoveNum(int n, Side c) {
        this.n = n;
        this.c = c;
    }

    public Side getC() {
        return c;
    }

    public int getN() {
        return n;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MoveNum moveNum = (MoveNum) o;
        return n == moveNum.n && c == moveNum.c;
    }
}
