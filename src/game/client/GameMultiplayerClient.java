package game.client;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import game.main.TetrisPlayfield;
import game.main.math.Vector2D;
import game.main.random.NoneStrategy;
import game.main.random.RandomStrategyEnum;
import game.main.tetromino.Tetromino;
import game.main.tetromino.TetrominoType;
import server.ControlDirection;
import server.GameState;
import server.MainServer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
     * The random strategy that get from server.
     */
    private RandomStrategyEnum randomStrategy;

    /**
     * The number of the current full row of our playfield.
     */
    private int ownPlayfieldFullRow;

    /**
     * The number of the current full row of opponent's playfield.
     */
    private int opponentPlayfieldFullRow;

    /**
     * Current opponent's next list of tetromino. This need to create due to the fact that
     * kyronet cannot handle a list of object so we need to get it one by one.
     */
    private ArrayList<TetrominoType> opponentTetrominoPool = new ArrayList<>();

    /**
     * Boolean value indicate that the client has sent the first own tetromino to the server.
     * <br>
     * This use as the flag to make the first tetromino sync with the server.
     */
    private Boolean hasSentFirstTime = false;

    /**
     * Create a new game with necessary components
     */
    public GameMultiplayerClient() {
        super("just a tetris with multiplayer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 700);
        addKeyListener(new PlayerController());
        setFocusable(true);
        setResizable(false);
        setAlwaysOnTop(true);

        // Setup server
        client = new Client();
        client.getKryo().register(Vector2D.class);
        client.getKryo().register(ControlDirection.class);
        client.getKryo().register(GameState.class);
        client.getKryo().register(RandomStrategyEnum.class);
        client.getKryo().register(TetrominoType.class);
        client.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                super.received(connection, object);
                if (object instanceof Vector2D vector2D) {
                    // Server sent a new playfield size
                    playfieldSize = vector2D;
                    System.out.println("Received new playfield size: " + playfieldSize);
                } else if (object instanceof Integer integer) {
                    // Server sent a delayed tick
                    delayedTick = integer;
                    System.out.println("Received delayed tick: " + delayedTick);
                } else if (object instanceof GameState gameState) {
                    // Server sent a new game state
                    if (gameState == GameState.START) {
                        // Start the observable
                        sendCurrentPoolToServer();
                        statusTextField.setText("");
                        observable.start();
                    } else if (gameState == GameState.PAUSE) {
                        // Pause the game
                        pause();
                    } else if (gameState == GameState.DISCONNECT) {
                        // Server requested disconnect
                        observable.setRunning(false);
                        JOptionPane.showMessageDialog(GameMultiplayerClient.this, "You have been disconnected from the server.");
                        observable.setRunning(false);
                        client.close();
                        System.exit(0);
                    }
                } else if (object instanceof ControlDirection controlDirection) {
                    // Server sent a new control direction from opponent
                    switch (controlDirection) {
                        case LEFT -> opponentPlayfield.moveLeft();
                        case RIGHT -> opponentPlayfield.moveRight();
                        case UP -> opponentPlayfield.rotate();
                    }
                    System.out.println("Received control direction: " + controlDirection);
                } else if (object instanceof RandomStrategyEnum randomStrategyEnum) {
                    // Server sent an initial random strategy for playfield
                    randomStrategy = randomStrategyEnum;
                    System.out.println("Received random strategy: " + randomStrategy);
                } else if (object instanceof TetrominoType tetrominoType) {
                    // Server send current tetromino type of opponent
                    // Server will send this every tick and we will use it to update the opponent playfield
                    // But due to the kyronet cannot handle the big list so +we need to send it by parts
                    // We set that opponent (the other side's client) will send the 3 tetromino types
                    // and we will use the first one to update the opponent playfield
                    if (opponentTetrominoPool.size() <= 3) {
                        opponentTetrominoPool.add(tetrominoType);
                        System.out.println("Received tetromino type: " + tetrominoType);
                        if (opponentTetrominoPool.size() == 3) {
                            System.out.println("Received opponent tetromino pool: " + opponentTetrominoPool);
                            // We have all the tetromino types
                            // We can update the opponent playfield
                            List<Tetromino> tetrominos = new ArrayList<>();
                            for (TetrominoType type : opponentTetrominoPool) {
                                Tetromino tetrominoToAdd = TetrominoType.convertTypeToTetromino(type);
                                tetrominoToAdd.setOrigin(Vector2D.clone(opponentPlayfield.SPAWN_POSITION));
                                tetrominos.add(tetrominoToAdd);
                            }
                            if (opponentPlayfield.randomStrategy.getNextTetromino() == null) {
                                // This is the first time we receive the tetromino type
                                // We need to update the opponent playfield too
                                Tetromino tetrominoToSet = tetrominos.get(0);
                                tetrominoToSet.setOrigin(Vector2D.clone(opponentPlayfield.SPAWN_POSITION));
                                tetrominoToSet.generateBlock();
                                opponentPlayfield.cleanCurrentTetrominoPositions();
                                opponentPlayfield.setCurrentTetromino(tetrominoToSet);
                            }
                            opponentPlayfield.randomStrategy.setTetrominoList(tetrominos);
                            opponentTetrominoPool.clear();
                        }
                    }

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
     * This method must be called after the connection to the server has been established since we need some information from the server.
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
        try {
            ownPlayfield = new TetrisPlayfield(playfieldSize, RandomStrategyEnum.convertToClass(randomStrategy));
            opponentPlayfield = new TetrisPlayfield(playfieldSize, new NoneStrategy());
        } catch (Exception e) {
            if (e instanceof NullPointerException) {
                // This client is getting kicked out of the server due to server is full or some error on server
                JOptionPane.showMessageDialog(this, "Server is full or something is wrong on server. Please check the server condition!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
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
                    case KeyEvent.VK_UP -> {
                        ownPlayfield.rotate();
                        client.sendTCP(ControlDirection.UP);
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
        // Send the information about current tetromino pool to the server
        sendCurrentPoolToServer();
        if (ownPlayfield.countFullRow() - ownPlayfieldFullRow > 0) {
            for (int i = 0; i < ownPlayfield.countFullRow() - ownPlayfieldFullRow; i++) {
                opponentPlayfield.generatePermanentRow();
            }
            ownPlayfieldFullRow = ownPlayfield.countFullRow();
        }
        if (opponentPlayfield.countFullRow() - opponentPlayfieldFullRow > 0) {
            for (int i = 0; i < opponentPlayfield.countFullRow() - opponentPlayfieldFullRow; i++) {
                ownPlayfield.generatePermanentRow();
            }
            opponentPlayfieldFullRow = opponentPlayfield.countFullRow();
        }
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
     * Send the information about current tetromino pool to the server
     */
    private void sendCurrentPoolToServer() {
        if (!hasSentFirstTime) {
            // This is the first time we send the pool to the server
            // The opponent also need current tetromino that's waiting in the playfield too
            client.sendTCP(TetrominoType.convertTetrominoToType(ownPlayfield.getCurrentTetromino()));
            for (Tetromino tetromino : ownPlayfield.randomStrategy.getTetrominoList().subList(0, 2)) {
                TetrominoType sentType = TetrominoType.convertTetrominoToType(tetromino);
                client.sendTCP(sentType);
                System.out.println("Sent: " + sentType);
            }
            hasSentFirstTime = true;
        }
        for (Tetromino tetromino : ownPlayfield.randomStrategy.getTetrominoList().subList(0, 3)) {
            System.out.println(ownPlayfield.randomStrategy.getTetrominoList());
            TetrominoType sentType = TetrominoType.convertTetrominoToType(tetromino);
            client.sendTCP(sentType);
            System.out.println("Sent: " + sentType);
        }
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
            System.out.println("Could not connect to server");
            e.printStackTrace();
            System.exit(0);
        }

        // Add GameObservable after we have received the information about the tick.
        observable = new GameObservable(delayedTick);
        observable.addObserver(this);

        statusTextField.setText("Waiting for other player... (opponent's playfield will be updated when opponent's connect)");
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
