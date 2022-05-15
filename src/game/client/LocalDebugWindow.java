package game.client;

import javax.swing.*;
import java.awt.*;

/**
 * JFrame that displays all the information about the game for debugging purposes.
 */
public class LocalDebugWindow extends JFrame {
    /**
     * Player 1 information text area.
     */
    private JTextArea player1Info;

    /**
     * Player 2 information text area.
     */
    private JTextArea player2Info;

    /**
     * Game's observable information text area.
     */
    private JTextArea gameStatusInfo;

    /**
     * The Game object that this DebugWindow bind and is displaying information about.
     */
    private GameLocalClient gameClient;

    /**
     * Constructor for DebugWindow.
     * @param gameClient The Game object that this DebugWindow bind and is displaying information about.
     */
    public LocalDebugWindow(GameLocalClient gameClient) {
        super("Debug Tools");
        this.gameClient = gameClient;
        setSize(800, 350);

        JPanel player1Panel = new JPanel();
        player1Panel.add(new JLabel("Player 1"));
        player1Panel.add(player1Info = new JTextArea(""));
        player1Info.setEditable(false);
        JButton player1AttackButton = new JButton("Add permenent row [O]");
        player1AttackButton.addActionListener(e -> {
            gameClient.playfieldPlayer1.generatePermanentRow();
            gameClient.requestFocus();
        });
        player1Panel.add(player1AttackButton);
        player1Panel.setBorder(BorderFactory.createLineBorder(Color.black));

        JPanel player2Panel = new JPanel();
        player2Panel.add(new JLabel("Player 2"));
        player2Panel.add(player2Info = new JTextArea(""));
        player2Info.setEditable(false);
        JButton player2AttackButton = new JButton("Add permenent row [P]");
        player2AttackButton.addActionListener(e -> {
            gameClient.playfieldPlayer2.generatePermanentRow();
            gameClient.requestFocus();
        });
        player2Panel.add(player2AttackButton);
        player2Panel.setBorder(BorderFactory.createLineBorder(Color.black));

        JPanel gameStatusPanel = new JPanel();
        gameStatusPanel.add(new JLabel("Game status"));
        gameStatusPanel.add(gameStatusInfo = new JTextArea(""));
        gameStatusInfo.setEditable(false);

        setLayout(new GridLayout(3, 1));
        add(player1Panel);
        add(player2Panel);
        add(gameStatusPanel);
    }

    /**
     * Update the elements of the DebugWindow.
     * <br>
     * Normally this method is called by the main game loop but it can also be called manually if this window needs to be updated
     * but the game's running state is not running.
     */
    public void update() {
        String player1Detail = "";
        player1Detail += "Size : " + gameClient.playfieldPlayer1.SIZE + "\n";
        player1Detail += "Origin : " + gameClient.playfieldPlayer1.getCurrentTetromino().getOrigin() + "\n";
        player1Detail += "Spawn location : " + gameClient.playfieldPlayer1.SPAWN_POSITION + "\n";
        player1Detail += "Current tetromino : " + gameClient.playfieldPlayer1.getCurrentTetromino().toString() + "\n";
        player1Detail += "Tetromino position : " + gameClient.playfieldPlayer1.getCurrentTetromino().getPositions().toString() + "\n";
        // Since Java not have null object pattern, we need to manually check if the current tetromino is null or not.
        if (gameClient.playfieldPlayer1.getCurrentTetromino().getState() == null) {
            player1Detail += "Tetromino state : null\n";
        } else {
            player1Detail += "Tetromino state : " + (gameClient.playfieldPlayer1.getCurrentTetromino().getState().toString());
        }
        player1Info.setText(player1Detail);

        String player2Detail = "";
        player2Detail += "Size : " + gameClient.playfieldPlayer2.SIZE + "\n";
        player2Detail += "Origin : " + gameClient.playfieldPlayer1.getCurrentTetromino().getOrigin() + "\n";
        player2Detail += "Spawn location : " + gameClient.playfieldPlayer2.SPAWN_POSITION + "\n";
        player2Detail += "Current tetromino : " + gameClient.playfieldPlayer2.getCurrentTetromino().toString() + "\n";
        player2Detail += "Tetromino position : " + gameClient.playfieldPlayer2.getCurrentTetromino().getPositions().toString() + "\n";
        // Since Java not have null object pattern, we need to manually check if the current tetromino is null or not.
        if (gameClient.playfieldPlayer2.getCurrentTetromino().getState() == null) {
            player2Detail += "Tetromino state : null\n";
        } else {
            player2Detail += "Tetromino state : " + (gameClient.playfieldPlayer2.getCurrentTetromino().getState().toString());
        }
        player2Info.setText(player2Detail);

        String gameStatusDetail = "";
        gameStatusDetail += "Delayed per tick : " + gameClient.getObservable().delayedTick + "\n";
        gameStatusDetail += "Tick : " + gameClient.getObservable().getTick() + "\n";
        gameStatusDetail += "isRunning : " + gameClient.getObservable().getRunning() + "\n";
        gameStatusDetail += "isOver : " + gameClient.getObservable().getOver() + "\n";
        gameStatusInfo.setText(gameStatusDetail);
    }
}
