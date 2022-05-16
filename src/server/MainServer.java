package server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import game.main.math.Vector2D;
import game.main.random.RandomStrategyEnum;

import javax.swing.*;
import java.awt.*;

/**
 * Main server of the game.
 */
public class MainServer extends JFrame {
    /**
     * The port of the server.
     */
    public static final int PORT = 5455;

    /**
     * Playfield size that server will broadcast to clients to set their playfield size.
     */
    public Vector2D PLAYFIELD_SIZE = new Vector2D(10,20);

    /**
     * Tick per millisecond that server will broadcast to clients to set their tick per millisecond.
     */
    public int DELAYED_TICKS = 200;

    /**
     * The server.
     */
    private Server server;

    /**
     * Main text area for logging server events.
     */
    private JTextArea logTextArea;

    /**
     * Connection from player 1.
     */
    private Connection player1connection;

    /**
     * Connection from player 2.
     */
    private Connection player2connection;

    public RandomStrategyEnum randomStrategy = RandomStrategyEnum.Normal;

    /**
     * Initialize the server and its GUI.
     */
    public MainServer() {
        super("Server");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setAlwaysOnTop(true);

        // Add top panel
        JPanel topPanel = new JPanel();
        JButton pauseButton = new JButton("Pause");
        JButton cleanConnectionButton = new JButton("Clean Connections");
        topPanel.add(pauseButton);
        topPanel.add(cleanConnectionButton);
        add(topPanel, BorderLayout.NORTH);

        // Add scrollable text area for logging.
        logTextArea = new JTextArea();
        logTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logTextArea);
        add(scrollPane, BorderLayout.CENTER);

        // Initialize the server
        server = new Server();
        server.getKryo().register(Vector2D.class);
        server.getKryo().register(ControlDirection.class);
        server.getKryo().register(GameState.class);
        server.getKryo().register(RandomStrategyEnum.class);
        server.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof ControlDirection direction) {
                    if (connection == player1connection) {
                        player2connection.sendTCP(direction);
                        logTextArea.append("Player 1 sent direction " + direction + "\n");
                    } else {
                        player1connection.sendTCP(direction);
                        logTextArea.append("Player 2 sent direction " + direction + "\n");
                    }
                    // scroll the scroll pane to the bottom
                    logTextArea.setCaretPosition(logTextArea.getDocument().getLength());
                }
            }

            @Override
            public void connected(Connection connection) {
                logTextArea.append("Player connected: " + connection.getRemoteAddressTCP().getHostString() + "\n");
                if (player1connection == null && player2connection == null) {
                    // This is the first player.
                    player1connection = connection;
                    server.sendToTCP(connection.getID(), PLAYFIELD_SIZE);
                    server.sendToTCP(connection.getID(), DELAYED_TICKS);
                    server.sendToTCP(connection.getID(), randomStrategy);
                    logTextArea.append("Player 1 connected\n");
                } else if (player1connection != null && player2connection == null) {
                    // This is the second player.
                    player2connection = connection;
                    server.sendToTCP(connection.getID(), PLAYFIELD_SIZE);
                    server.sendToTCP(connection.getID(), DELAYED_TICKS);
                    server.sendToTCP(connection.getID(), randomStrategy);
                    logTextArea.append("Player 2 connected\n");
                } else {
                    // Server is full.
                    connection.close();
                    logTextArea.append("There are already two players connected.\n");
                }

                if (player1connection != null && player2connection != null) {
                    // Both players are connected, can start the game.
                    // Add delay before starting the game.
                    logTextArea.append("Both players connected. Starting game in 3 seconds.\n");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            server.sendToAllTCP(GameState.START);
                        }
                    }).start();
                }

                // TODO: Current can play with normal strategy (not shuffle) but we need to implement the shuffle strategy too.
            }

            @Override
            public void disconnected(Connection connection) {
                super.disconnected(connection);
                logTextArea.append("Player disconnected\n");
            }
        });

        pauseButton.addActionListener(e -> server.sendToAllTCP(GameState.PAUSE));
        cleanConnectionButton.addActionListener(e -> {
            server.sendToAllTCP(GameState.DISCONNECT);
            player1connection = null;
            player2connection = null;
            logTextArea.append("Cleaned connection\n");
        });
    }

    /**
     * Start the server
     */
    public void start() {
        setVisible(true);
        server.start();
        try {
            server.bind(PORT);
            logTextArea.append("Server started on port " + PORT + ".\n");
        } catch (Exception e) {
            logTextArea.append("Could not bind to port " + PORT + "\n");
            logTextArea.append(e.getMessage());
        }
    }

    public static void main(String[] args) {
        MainServer server = new MainServer();
        server.start();
    }
}
