package game.client;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import game.main.TetrisPlayfield;
import game.main.math.Vector2D;
import server.ControlDirection;
import server.GameState;
import server.MainServer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

/**
 * Main class of the game.
 */
public class GameMultiplayerClient extends JFrame implements Observer {
    /**
     * Current player's playfield.
     */
    public TetrisPlayfield ownPlayfield;

    /**
     * Opponent's playfield. Mainly broadcast opponent's control from server.
     */
    public TetrisPlayfield opponentPlayfield;

    /**
     * Size of the playfield in blocks
     */
    public Vector2D playfieldSize;

    /**
     * Tick delayed that is used to control the game speed. Normally get this from server.
     */
    public int delayedTick;

    /**
     * Show debug information.
     */
    public Boolean DEBUG = false;

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
    private MultiplayerDebugWindow debugWindow;

    /**
     * The game's client that connects to the server.
     */
    private Client client;

    /**
     * Create a new game with necessary components
     */
    public GameMultiplayerClient() {
        super("just a tetris with multiplayer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 700);
        addKeyListener(new PlayerController());
        setFocusable(true);

        // Setup server
        client = new Client();
        client.getKryo().register(Vector2D.class);
        client.getKryo().register(ControlDirection.class);
        client.getKryo().register(GameState.class);
        client.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                super.received(connection, object);
                if (object instanceof Vector2D) {
                    // Server sent a new playfield size
                    playfieldSize = (Vector2D) object;
                    System.out.println("Received new playfield size: " + playfieldSize);
                } else if (object instanceof Integer) {
                    // Server sent a delayed tick
                    delayedTick = (Integer) object;
                    System.out.println("Received delayed tick: " + delayedTick);
                } else if (object instanceof GameState gameState) {
                    // Server sent a new game state
                    if (gameState == GameState.START) {
                        // Start the observable
                        statusTextField.setText("");
                        observable.start();
                    } else if (gameState == GameState.PAUSE) {
                        // Pause the game
                        pause();
                    }
                } else if (object instanceof ControlDirection controlDirection) {
                    // Server sent a new control direction from opponent
                    if (controlDirection == ControlDirection.LEFT) {
                        opponentPlayfield.moveLeft();
                    } else if (controlDirection == ControlDirection.RIGHT) {
                        opponentPlayfield.moveRight();
                    } else if (controlDirection == ControlDirection.DOWN) {
                        opponentPlayfield.moveDown();
                    }
                    System.out.println("Received control direction: " + controlDirection);
                }
            }

            @Override
            public void connected(Connection connection) {
                super.connected(connection);
            }
        });

        // Make the game window spawn at the center of the screen
        setLocationRelativeTo(null);
    }

    /**
     * Initialize the game window.
     *
     * This method must be called after the connection to the server has been established.
     */
    private void initGui() {
        // Add topPanel include the debug window button and status text field
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(1, 2));
        JPanel debugPanel = new JPanel();
        debugPanel.setLayout(new FlowLayout());
        JButton debugButton = new JButton("Launch debug window");
        debugButton.setHorizontalAlignment(SwingConstants.LEFT);
        debugWindow = new MultiplayerDebugWindow(this);
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
        ownPlayfield = new TetrisPlayfield(playfieldSize);
        opponentPlayfield = new TetrisPlayfield(playfieldSize);
        JPanel ownPanel = new JPanel();
        JPanel opponentPanel = new JPanel();
        ownPanel.setLayout(new BoxLayout(ownPanel, BoxLayout.Y_AXIS));
        opponentPanel.setLayout(new BoxLayout(opponentPanel, BoxLayout.Y_AXIS));
        ownPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        opponentPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        ownPanel.add(ownPlayfield);
        opponentPanel.add(opponentPlayfield);
        mainPanel.add(ownPanel);
        mainPanel.add(opponentPanel);
        add(mainPanel, BorderLayout.CENTER);

        if (DEBUG) {
            debugWindow.setVisible(true);
        }
    }

    /**
     * Class for handling key events from game window and broadcast them to the server.
     */
    class PlayerController extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if (!ownPlayfield.isReceivedInput) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT -> {
                        ownPlayfield.moveLeft();
                        client.sendTCP(ControlDirection.LEFT);
                    }
                    case KeyEvent.VK_RIGHT -> {
                        ownPlayfield.moveRight();
                        client.sendTCP(ControlDirection.RIGHT);
                    }
                    case KeyEvent.VK_DOWN -> {
                        ownPlayfield.moveDown();
                        client.sendTCP(ControlDirection.DOWN);
                    }
                }
                ownPlayfield.isReceivedInput = true;
            }

            if (DEBUG) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    System.exit(0);
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
        ownPlayfield.update();
        opponentPlayfield.update();
        debugWindow.update();
        if (ownPlayfield.isGameOver() || opponentPlayfield.isGameOver()) {
            statusTextField.setForeground(Color.RED);
            if (ownPlayfield.isGameOver()) {
                statusTextField.setForeground(Color.RED);
                statusTextField.setText("You lost!");
            } else {
                statusTextField.setForeground(Color.GREEN);
                statusTextField.setText("You win!");
            }
            observable.setRunning(false);
            debugWindow.update();
        }
        ownPlayfield.isReceivedInput = false;
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
        // Start the connection to the server
        client.start();
        try {
            client.connect(5000, "localhost", MainServer.PORT);
            initGui();
            setVisible(true);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Could not connect to server\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

        // Add GameObservable after we have received the information about the tick.
        observable = new GameObservable(delayedTick);
        observable.addObserver(this);

        statusTextField.setText("Waiting for other player...");
        statusTextField.setForeground(Color.BLACK);

        // Update the debug window manually since the game is not running yet.
        debugWindow.update();
    }

    /**
     * Set the game to pause state if the game is running, else continue the game
     */
    public void pause() {
        if (observable.getRunning()) {
            observable.setRunning(false);
            statusTextField.setForeground(Color.RED);
            statusTextField.setText("Game paused!");
        } else {
            observable.setRunning(true);
            statusTextField.setText("");
        }
        // When the game is paused, the game thread is stopped update due to running status, so we need to manually update
        // some element that need to be updated when game paused too
        debugWindow.update();
    }

    public static void main(String[] args) {
        GameMultiplayerClient gameClient = new GameMultiplayerClient();
        gameClient.start();
    }
}
