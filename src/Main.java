import game.client.GameLocalClient;
import game.client.GameMultiplayerClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Main class of the game to select the mode of the game to launch.
 */
public class Main extends JFrame {
    private JButton localGameButton;

    private JButton onlineGameButton;

    public Main() {
        setTitle("Welcome to tetris!");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 200);

        // Add top panel
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(1, 2));
        topPanel.add(new JLabel("Please select game mode below:"));
        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(2, 1));
        JPanel localGamePanel = new JPanel();
        localGamePanel.setLayout(new GridLayout(1, 2));
        JPanel localGameButtonPanel = new JPanel();
        localGameButtonPanel.setLayout(new FlowLayout());
        localGameButtonPanel.add(localGameButton = new JButton("Local"));
        localGamePanel.add(localGameButtonPanel);
        JTextArea localGameLabel = new JTextArea("Play locally with your friend using the same keyboard");
        localGameLabel.setEditable(false);
        localGamePanel.add(localGameLabel);
        centerPanel.add(localGamePanel);
        JPanel onlineGamePanel = new JPanel();
        onlineGamePanel.setLayout(new GridLayout(1, 2));
        JPanel onlineGameButtonPanel = new JPanel();
        onlineGameButtonPanel.setLayout(new FlowLayout());
        onlineGameButtonPanel.add(onlineGameButton = new JButton("Online"));
        onlineGamePanel.add(onlineGameButtonPanel);
        JTextArea onlineGameLabel = new JTextArea("Play online with your friend using different keyboard");
        onlineGameLabel.setEditable(false);
        onlineGamePanel.add(onlineGameLabel);
        centerPanel.add(onlineGamePanel);
        add(centerPanel, BorderLayout.CENTER);

        // Add bottom panel
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        bottomPanel.setLayout(new FlowLayout());
        JLabel bottomLabel = new JLabel("Go one more step, and you will reach the goal! (I mean 7th floor)");
        bottomLabel.setHorizontalAlignment(SwingConstants.LEFT);
        bottomPanel.add(bottomLabel);
        add(bottomPanel, BorderLayout.SOUTH);

        // TODO: Server status

        localGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                (new GameLocalClient()).start();
                dispose();
            }
        });

        onlineGameButton.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               (new GameMultiplayerClient()).start();
               dispose();
           }
        });

        setResizable(false);
        setVisible(true);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        new Main();
    }
}
