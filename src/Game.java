import math.Vector2D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Observable;
import java.util.Observer;

/**
 * Main class of the game.
 */
public class Game extends JFrame implements Observer {
    /**
     * Player 1's playfield
     */
    private TetrisPlayfield playfieldPlayer1;

    /**
     * Player 2's playfield
     */
    private TetrisPlayfield playfieldPlayer2;

    /**
     * Size of the playfield in blocks
     */
    public Vector2D PLAYFIELD_SIZE = new Vector2D(10,20);

    /**
     * The game's observable for updating the game time
     */
    private GameObservable observable;

    /**
     * Create a new game with necessary components
     */
    public Game() {
        super("just a tetris");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        addKeyListener(new PlayerController());

        playfieldPlayer1 = new TetrisPlayfield(PLAYFIELD_SIZE);
        playfieldPlayer2 = new TetrisPlayfield(PLAYFIELD_SIZE);
        JPanel player1Panel = new JPanel();
        JPanel player2Panel = new JPanel();
        player1Panel.setLayout(new BoxLayout(player1Panel, BoxLayout.Y_AXIS));
        player2Panel.setLayout(new BoxLayout(player2Panel, BoxLayout.Y_AXIS));
        player1Panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        player2Panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        player1Panel.add(playfieldPlayer1);
        player2Panel.add(playfieldPlayer2);

        setLayout(new FlowLayout());
        add(player1Panel);
        add(player2Panel);

        observable = new GameObservable();
        observable.addObserver(this);
    }

    /**
     * Class for handling key events from game window
     */
    class PlayerController extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT -> playfieldPlayer1.moveLeft();
                case KeyEvent.VK_RIGHT -> playfieldPlayer1.moveRight();
                case KeyEvent.VK_A -> playfieldPlayer2.moveLeft();
                case KeyEvent.VK_D -> playfieldPlayer2.moveRight();
                // For debug purpose
                case KeyEvent.VK_O -> playfieldPlayer1.generatePermanentRow();
                case KeyEvent.VK_P -> playfieldPlayer2.generatePermanentRow();
            }
        }
    }

    /**
     * This method is called whenever the observed object is changed. An
     * application calls an {@code Observable} object's
     * {@code notifyObservers} method to have all the object's
     * observers notified of the change.
     *
     * @param o   the observable object.
     * @param arg an argument passed to the {@code notifyObservers}
     *            method.
     */
    @Override
    public void update(Observable o, Object arg) {
        playfieldPlayer1.update();
        playfieldPlayer2.update();
        repaint();
    }

    /**
     * Start the game
     */
    public void start() {
        observable.start();
        setVisible(true);
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.start();
    }
}
