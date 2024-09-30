package chess;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RunChess implements Runnable {

    /**
     * When an object implementing interface {@code Runnable} is used
     * to create a thread, starting the thread causes the object's
     * {@code run} method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method {@code run} is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        final JFrame frame = new JFrame("Chess");
        frame.setLocation(0, 0);

        JOptionPane.showMessageDialog(
                null, new Object[] {
                        new JLabel("Standard Game of Chess:"),
                        new JLabel(
                                "Rules of Chess found here: " +
                                        "https://www.chess.com/learn-how-to-play-chess"
                        ),
                        new JLabel("Click on pieces to move.")
                }, "Instructions", JOptionPane.PLAIN_MESSAGE
        );

        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);

        final JLabel status = new JLabel("Setting up...");
        status_panel.add(status);

        final JPanel menu = new JPanel();
        frame.add(menu, BorderLayout.EAST);

        final GameDisplay board = new GameDisplay(status, new ChessGame(), menu);
        frame.add(board, BorderLayout.CENTER);

//        final GameMenu controls = new GameMenu(board);
//        frame.add(controls, BorderLayout.EAST);

        frame.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                board.repaint();
            }
        });

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

}
