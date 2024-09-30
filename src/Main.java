import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        Runnable game = new chess.RunChess();
        SwingUtilities.invokeLater(game);
    }
}