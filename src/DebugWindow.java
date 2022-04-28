import javax.swing.*;
import java.awt.*;

public class DebugWindow extends JFrame {

    private JTextArea player1Info;

    private JTextArea player2Info;

    private JTextArea gameStatusInfo;

    private Game game;

    public DebugWindow(Game game) {
        super("Debug Tools");
        this.game = game;
        setSize(800, 350);

        JPanel player1Panel = new JPanel();
        player1Panel.add(new JLabel("Player 1"));
        player1Panel.add(player1Info = new JTextArea(""));
        JButton player1AttackButton = new JButton("Add permenent row");
        player1AttackButton.addActionListener(e -> game.playfieldPlayer1.generatePermanentRow());
        player1Panel.add(player1AttackButton);
        player1Panel.setBorder(BorderFactory.createLineBorder(Color.black));

        JPanel player2Panel = new JPanel();
        player2Panel.add(new JLabel("Player 2"));
        player2Panel.add(player2Info = new JTextArea(""));
        JButton player2AttackButton = new JButton("Add permenent row");
        player2AttackButton.addActionListener(e -> game.playfieldPlayer2.generatePermanentRow());
        player2Panel.add(player2AttackButton);
        player2Panel.setBorder(BorderFactory.createLineBorder(Color.black));

        JPanel gameStatusPanel = new JPanel();
        gameStatusPanel.add(new JLabel("Game status"));
        gameStatusPanel.add(gameStatusInfo = new JTextArea(""));

        setLayout(new GridLayout(3, 1));
        add(player1Panel);
        add(player2Panel);
        add(gameStatusPanel);
        setLocationRelativeTo(null);
    }

    public void update() {
        String player1Detail = "";
        player1Detail += "Size : " + game.playfieldPlayer1.SIZE + "\n";
        player1Detail += "Spawn location : " + game.playfieldPlayer1.SPAWN_POSITION + "\n";
        player1Detail += "Current tetromino : " + game.playfieldPlayer1.getCurrentTetromino().toString() + "\n";
        player1Detail += "Tetromino position : " + game.playfieldPlayer1.getCurrentTetromino().getPositions().toString();
        player1Info.setText(player1Detail);

        String player2Detail = "";
        player2Detail += "Size : " + game.playfieldPlayer2.SIZE + "\n";
        player2Detail += "Spawn location : " + game.playfieldPlayer2.SPAWN_POSITION + "\n";
        player2Detail += "Current tetromino : " + game.playfieldPlayer2.getCurrentTetromino().toString() + "\n";
        player2Detail += "Tetromino position : " + game.playfieldPlayer2.getCurrentTetromino().getPositions().toString();
        player2Info.setText(player2Detail);

        String gameStatusDetail = "";
        gameStatusDetail += "Delayed per tick : " + game.getObservable().DELAYED_TICK + "\n";
        gameStatusDetail += "Tick : " + game.getObservable().getTick() + "\n";
        gameStatusDetail += "isRunning : " + game.getObservable().getRunning() + "\n";
        gameStatusDetail += "isOver : " + game.getObservable().getOver() + "\n";
        gameStatusInfo.setText(gameStatusDetail);
    }
}
