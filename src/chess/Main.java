package chess;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        Runnable game = new RunChess();
        SwingUtilities.invokeLater(game);
    }
}