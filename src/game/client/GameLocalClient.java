package game.client;

import game.main.TetrisPlayfield;
import game.main.math.Vector2D;
import game.main.random.TraditionalRandomStrategy;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Observable;
import java.util.Observer;

/**
 * Game class that mainly for local play. (Without server)
 */
public class GameLocalClient extends JFrame implements Observer {
    /**
     * Player 1's playfield
     */
    public TetrisPlayfield playfieldPlayer1;

    /**
     * Player 2's playfield
     */
    public TetrisPlayfield playfieldPlayer2;

    /**
     * Size of the playfield in blocks
     */
    public Vector2D PLAYFIELD_SIZE = new Vector2D(10,20);

    /**
     * Show debug information.
     */
    public Boolean DEBUG = true;

    /**
     * The game's observable for updating the game time
     */
    private GameObservable observable;

    /**
     * Status text field at the right-top corner of the game window.
     */
    private JLabel statusTextField;

    /**
     * Debug window that will appear only when <code>DEBUG</code> is true.
     */
    private LocalDebugWindow debugWindow;

    private int player1RowFull = 0;

    private int player2RowFull = 0;

    /**
     * Create a new game with necessary components
     */
    public GameLocalClient() {
        super("just a tetris");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 700);
        addKeyListener(new PlayerController());
        setFocusable(true);

        // Add topPanel include the debug window button and status text field
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(1, 2));
        JPanel debugPanel = new JPanel();
        debugPanel.setLayout(new FlowLayout());
        JButton debugButton = new JButton("Launch debug window");
        debugButton.setHorizontalAlignment(SwingConstants.LEFT);
        debugWindow = new LocalDebugWindow(this);
        debugPanel.add(debugButton);
        topPanel.add(debugPanel);
        debugButton.addActionListener(e -> {
            debugWindow.setVisible(true);
            // After clicking on the button, the window's focus will focus on the button
            // This is a workaround to make the window still focus on the window after clicking on the button
            requestFocus();
        });
        if (!DEBUG) {
            debugPanel.setVisible(false);
        }
        statusTextField = new JLabel("");
        statusTextField.setHorizontalAlignment(SwingConstants.RIGHT);
        topPanel.add(statusTextField);
        add(topPanel, BorderLayout.NORTH);

        // Add mainPanel and its components at the center of the frame
        JPanel mainPanel = new JPanel();
        playfieldPlayer1 = new TetrisPlayfield(PLAYFIELD_SIZE, new TraditionalRandomStrategy());
        playfieldPlayer2 = new TetrisPlayfield(PLAYFIELD_SIZE, new TraditionalRandomStrategy());
        JPanel player1Panel = new JPanel();
        JPanel player2Panel = new JPanel();
        player1Panel.setLayout(new BoxLayout(player1Panel, BoxLayout.Y_AXIS));
        player2Panel.setLayout(new BoxLayout(player2Panel, BoxLayout.Y_AXIS));
        player1Panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        player2Panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        player1Panel.add(playfieldPlayer1);
        player2Panel.add(playfieldPlayer2);
        mainPanel.add(player1Panel);
        mainPanel.add(player2Panel);
        add(mainPanel, BorderLayout.CENTER);

        // Add GameObservable
        observable = new GameObservable(200);
        observable.addObserver(this);

        if (DEBUG) {
            debugWindow.setVisible(true);
        }
        // Make the game window spawn at the center of the screen
        setLocationRelativeTo(null);
    }

    /**
     * Class for handling key events from game window
     */
    class PlayerController extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_SPACE -> pause();
                // 192 is the unicode for the grave accent
                case 192 -> restartGame();
            }

            if (!playfieldPlayer1.isReceivedInput) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT -> playfieldPlayer1.moveLeft();
                    case KeyEvent.VK_RIGHT -> playfieldPlayer1.moveRight();
                    case KeyEvent.VK_UP -> playfieldPlayer1.rotate();
                }
                playfieldPlayer1.isReceivedInput = true;
            } else {
                System.out.println("Ignored player 1 input");
            }

            if (!playfieldPlayer2.isReceivedInput) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_A -> playfieldPlayer2.moveLeft();
                    case KeyEvent.VK_D -> playfieldPlayer2.moveRight();
                    case KeyEvent.VK_W -> playfieldPlayer2.rotate();
                }
                playfieldPlayer2.isReceivedInput = true;
            } else {
                System.out.println("Ignored player 2 input");
            }

            if (DEBUG) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_O -> playfieldPlayer1.generatePermanentRow();
                    case KeyEvent.VK_P -> playfieldPlayer2.generatePermanentRow();
                    case KeyEvent.VK_ESCAPE -> System.exit(0);
                }
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
        if (playfieldPlayer1.countFullRow() - player1RowFull > 0) {
            for (int i = 0; i < playfieldPlayer1.countFullRow() - player1RowFull; i++) {
                playfieldPlayer2.generatePermanentRow();
            }
            player1RowFull = playfieldPlayer1.countFullRow();
        }
        if (playfieldPlayer2.countFullRow() - player2RowFull > 0) {
            for (int i = 0; i < playfieldPlayer2.countFullRow() - player2RowFull; i++) {
                playfieldPlayer1.generatePermanentRow();
            }
            player2RowFull = playfieldPlayer2.countFullRow();
        }
        playfieldPlayer1.update();
        playfieldPlayer2.update();
        debugWindow.update();
        if (playfieldPlayer1.isGameOver() || playfieldPlayer2.isGameOver()) {
            statusTextField.setForeground(Color.RED);
            if (playfieldPlayer1.isGameOver()) {
                statusTextField.setText("Player 1 lost!");
            } else {
                statusTextField.setText("Player 2 lost!");
            }
            observable.setRunning(false);
            debugWindow.update();
        }
        // If the game window loses focus, pause the game
        if (!isFocused() && observable.getTick() > 1) {
            pause();
        }
        playfieldPlayer1.isReceivedInput = false;
        playfieldPlayer2.isReceivedInput = false;
        repaint();
    }

    /**
     * Get the instance of the observable that updating the game
     * @return the instance of the observable that updating the game
     */
    public GameObservable getObservable() {
        return observable;
    }

    /**
     * Start the game
     */
    public void start() {
        observable.start();
        setVisible(true);
    }

    /**
     * Set the game to pause state if the game is running, else continue the game
     */
    public void pause() {
        if (observable.getRunning()) {
            observable.setRunning(false);
            statusTextField.setForeground(Color.RED);
            statusTextField.setText("Game paused! Press SPACE to continue.");
        } else {
            observable.setRunning(true);
            statusTextField.setText("");
        }
        // When the game is paused, the game thread is stopped update due to running status, so we need to manually update
        // some element that need to be updated when game paused too
        debugWindow.update();
    }

    public void restartGame() {
        playfieldPlayer1.restartGame();
        playfieldPlayer2.restartGame();
        observable.restartGame();
        statusTextField.setForeground(Color.BLACK);
        statusTextField.setText("");
        observable.setRunning(true);
        debugWindow.update();
    }

    public static void main(String[] args) {
        GameLocalClient game = new GameLocalClient();
        game.start();
    }
}