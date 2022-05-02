package server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import math.Vector2D;

import javax.swing.*;

public class MainServer extends JFrame {
    public static final int PORT = 5455;

    public Vector2D PLAYFIELD_SIZE = new Vector2D(10,20);

    public int DELAYED_TICKS = 200;
    private Server server;
    private JTextArea logTextArea;

    private Connection player1connection;

    private Connection player2connection;

    public MainServer() {
        super("Server");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setAlwaysOnTop(true);

        // Add scrollable text area for logging.
        logTextArea = new JTextArea();
        logTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logTextArea);
        add(scrollPane);

        server = new Server();
        server.getKryo().register(Vector2D.class);
        server.getKryo().register(ControlDirection.class);
        server.getKryo().register(GameState.class);
        server.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof ControlDirection direction) {
                    server.sendToAllTCP(direction);
                    logTextArea.append("Player sent direction: " + direction + "\n");
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
                    logTextArea.append("Player 1 connected\n");
                } else if (player1connection != null && player2connection == null) {
                    // This is the second player.
                    player2connection = connection;
                    server.sendToTCP(connection.getID(), PLAYFIELD_SIZE);
                    server.sendToTCP(connection.getID(), DELAYED_TICKS);
                    logTextArea.append("Player 2 connected\n");
                } else {
                    // TODO: If player get disconnected, reconnect and send the current game state.
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
            }

            @Override
            public void disconnected(Connection connection) {
                super.disconnected(connection);
                logTextArea.append("Player disconnected\n");
            }
        });
    }

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
