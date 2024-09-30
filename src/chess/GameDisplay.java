package chess;

import chess.moves.Result;
import chess.pieces.Piece;

import java.awt.event.MouseListener;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GameDisplay extends JPanel {
    private ChessGame game;
    private Coord selected;

    public enum SMode {
        GOOD, BAD, NONE, WAIT
    };

    public enum MMode {
        NONE, OFFER_DRAW, RESIGN, ENDED
    }

    private MMode mMode;

    private SMode sMode;
    private final JLabel status;
    private final JPanel moves;
    private static int boardSize = 320; // best if it's a multiple of 8

    public GameDisplay(JLabel status, ChessGame game, JPanel menu) {
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setFocusable(true);
        this.game = game;
        this.status = status;
        this.sMode = SMode.NONE;
        this.mMode = MMode.NONE;

        moves = new JPanel();
        menu.add(moves);

        final JButton draw = new JButton("Offer Draw");
        draw.addActionListener(e -> offerDraw());
        menu.add(draw);

        final JButton resign = new JButton("Resign");
        resign.addActionListener(e -> tryResign());
        menu.add(resign);

        final JButton newGame = new JButton("New Game");
        newGame.addActionListener(e -> newGame());
        menu.add(newGame);


        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (sMode == SMode.GOOD) {
                    game.actualMakeMove(selected, pointToCoord(e.getPoint()));
                    selected = null;
                    sMode = SMode.NONE;
                } else {
                    if (pointToCoord(e.getPoint()).equals(selected)) {
                        selected = null;
                        sMode = SMode.NONE;
                    } else {
                        selected = pointToCoord(e.getPoint());
                        sMode = SMode.GOOD;
                    }
                }
                updateStatus();
                repaint();
            }
            // @Override
            // public void mousePressed(MouseEvent e) {}
        });
    }

    private void updateMoves() {
        for (Component m : moves.getComponents()) {
            moves.remove(m);
        }
        for (String s : game.getMoveLines()) {
            moves.add(new JLabel(s));
        }
    }

    public void updateStatus() {
        if (game.getResult() == Result.IN_PROGRESS) {
            status.setText(game.getToMoveAsString());
        } else {
            status.setText(game.getResult().toString());
        }
    }

    public void newGame() {
        this.game = new ChessGame();
        status.setText("New game created");
        this.sMode = SMode.NONE;

        for (MouseListener ml : this.getMouseListeners()) {
            this.removeMouseListener(ml);
        }

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (sMode == SMode.GOOD) {
                    game.actualMakeMove(selected, pointToCoord(e.getPoint()));
                    selected = null;
                    sMode = SMode.NONE;
                } else {
                    if (pointToCoord(e.getPoint()).equals(selected)) {
                        selected = null;
                        sMode = SMode.NONE;
                    } else {
                        selected = pointToCoord(e.getPoint());
                        sMode = SMode.GOOD;
                    }
                }
                updateStatus();
                repaint();
            }
        });

        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        boardSize = Math.min(this.getHeight(), this.getWidth());
        paintEmptyBoard(g);
        paintCurrentPosition(g);
        paintPossibleMoves(g);
    }

    private void paintCurrentPosition(Graphics g) {
        BufferedImage icon; // = ImageIO.read(new File("path-to-file"))
        Point p;
        for (Piece piece : game.getLastBoardAsList()) {
            p = coordToPoint(piece.getLocation());
            try {
                icon = ImageIO.read(new File("src/images/" + piece.icon() + ".png"));
                g.drawImage(icon, p.x, p.y, boardSize / 8, boardSize / 8, null);
            } catch (IOException e) {
                try {
                    icon = ImageIO.read(new File("images/" + piece.icon() + ".png"));
                    g.drawImage(icon, p.x, p.y, boardSize / 8, boardSize / 8, null);
                } catch (IOException e1) {
                    System.out.println("bruh");
                }
            }
        }
    }

    private void paintPossibleMoves(Graphics g) {
        Point p;
        List<Coord> targets = game.possibleMoves(selected);
        if (sMode == sMode.GOOD && targets != null) {
            for (Coord c : targets) {
                if (c != null) {
                    if (c.isDark()) {
                        g.setColor(Color.BLUE);
                    } else {
                        g.setColor(Color.CYAN);
                    }
                    p = coordToPointCenter(c);
                    g.fillOval(p.x - 10, p.y - 10, 20, 20);
                }
            }
        }
    }

    private void paintEmptyBoard(Graphics g) {
        Point p;
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                if (Coord.isDark(i, j)) {
                    g.setColor(Color.DARK_GRAY);
                } else {
                    g.setColor(Color.LIGHT_GRAY);
                }
                p = coordToPoint(i, j);
                g.fillRect(p.x, p.y, boardSize / 8, boardSize / 8);
            }
        }
        if (selected != null) {
            p = coordToPoint(selected);
            if (selected.isDark()) {
                g.setColor(Color.BLUE);
            } else {
                g.setColor(Color.CYAN);
            }
            g.fillRect(p.x, p.y, boardSize / 8, boardSize / 8);
        }
    }

    private void tryResign() {
        int x = JOptionPane.showConfirmDialog(
                null, new Object[] {
                        new JLabel(game.getToMove() + ", are you sure you want to resign?")
                }, "Resign", JOptionPane.YES_NO_OPTION
        );

        if (x == 0) {
            if (game.getToMove() == Side.WHITE) {
                game.endGame(Result.WHITE_RESIGNS);
            } else {
                game.endGame(Result.BLACK_RESIGNS);
            }
            updateStatus();
        }
    }

    private void offerDraw() {
        int x = JOptionPane.showConfirmDialog(
                null, new Object[] {
                        new JLabel(
                                game.getToMove() + " offers draw. "
                                        + game.getToMove().otherSide() + ", do you accept?"
                        )
                }, "Draw Offer", JOptionPane.YES_NO_OPTION
        );

        if (x == 0) {
            game.endGame(Result.DRAW_BY_AGREEMENT);
            updateStatus();
        }
    }

    public static void endGameMessage(ChessGame game) {
        JOptionPane.showMessageDialog(
                null, new Object[] {
                        new JLabel(game.getResult().toString()),
                        new JLabel(game.getResult().toNotation())
                }, "Game Ended", JOptionPane.PLAIN_MESSAGE
        );
    }

    public static Coord pointToCoord(Point p) {
        int x = p.x / (boardSize / 8) + 1;
        int y = 8 - p.y / (boardSize / 8);
        return new Coord(x, y);
    }

    public static Point coordToPointCenter(Coord c) {
        int px = (c.getX() - 1) * boardSize / 8 + (boardSize / 8) / 2;
        int py = (8 - c.getY()) * boardSize / 8 + (boardSize / 8) / 2;
        return new Point(px, py);
    }

    public static Point coordToPoint(Coord c) {
        int px = (c.getX() - 1) * boardSize / 8;
        int py = (8 - c.getY()) * boardSize / 8;
        return new Point(px, py);
    }

    public static Point coordToPoint(int x, int y) {
        int px = (x - 1) * boardSize / 8;
        int py = (8 - y) * boardSize / 8;
        return new Point(px, py);
    }

    public static int getBoardSize() {
        return boardSize;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(boardSize, boardSize);
    }

    // static class MyPopUp extends JPanel {
    // MyPopUp(String text, JButton b) {
    // this.add(new JLabel(text));
    // this.add(b);
    // }
    // MyPopUp(String text, JButton b1, JButton b2) {
    // this.add(new JLabel(text));
    // this.add(b1);
    // this.add(b2);
    // System.out.println("popup created");
    // }
    //
    // @Override
    // public Dimension getPreferredSize() {
    // return new Dimension(boardSize/4, boardSize/2);
    // }
    // }
}
