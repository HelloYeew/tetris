package game.main;

import game.main.math.Vector2D;
import game.main.tetromino.Tetromino;

import javax.swing.*;
import java.awt.*;

/**
 * Panel for displaying the next tetromino. This panel can set tetromino manually or update automatically by
 * binding it to the playfield.
 */
public class NextTetrominoPanel extends JPanel {
    /**
     * Array that's store all blocks
     */
    private Color[][] blocks;

    /**
     * The size of one block in pixel
     */
    public int BLOCK_SIZE = 15;

    /**
     * Size of the panel
     */
    public Vector2D SIZE = new Vector2D(5, 5);

    /**
     * Origin of the tetromino that will be drawn
     */
    public Vector2D BlockOrigin = new Vector2D(1,2);

    /**
     * Current tetromino that is showing or will be drawn
     */
    public Tetromino currentShowingTetromino;

    /**
     * Initialize the blank panel
     */
    public NextTetrominoPanel() {
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(BLOCK_SIZE * SIZE.x, BLOCK_SIZE * SIZE.y));
        blocks = new Color[SIZE.x][SIZE.y];
    }

    /**
     * The method that is called by the JFrame to paint the playfield.
     * @param g  the <code>Graphics</code> context in which to paint
     */
    public void paint(Graphics g) {
        super.paint(g);
        for (int x = 0; x < SIZE.x; x++) {
            for (int y = 0; y < SIZE.y; y++) {
                if (blocks[x][y] != null) {
                    paintBlock(g, blocks[x][y], x, y);
                }
            }
        }
        paintGrid(g);
    }

    /**
     * Paint the target block on the playfield.
     * @param g the graphics context in which to paint
     * @param color the color of the block
     * @param x the x position of the block
     * @param y the y position of the block
     */
    private void paintBlock(Graphics g, Color color, int x, int y) {
        g.setColor(color);
        g.fillRect(x * BLOCK_SIZE, y * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
    }

    /**
     * Paint the grid
     * @param g the graphics context in which to paint
     */
    private void paintGrid(Graphics g) {
        g.setColor(Color.WHITE);
        for (int x = 0; x < SIZE.x; x++) {
            for (int y = 0; y < SIZE.y; y++) {
                g.drawRect(x * BLOCK_SIZE, y * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
            }
        }
    }

    /**
     * Paint all the current tetromino positions to the panel.
     */
    private void convertTetrominoToPixel() {
        for (Vector2D position : currentShowingTetromino.getPositions()) {
            // check if the block is in the bounds of the panel
            if (position.x >= 0 && position.x < SIZE.x && position.y >= 0 && position.y < SIZE.y) {
                blocks[position.x][position.y] = currentShowingTetromino.getColor();
            }
        }
    }

    /**
     * Clear the panel by setting all blocks to null
     */
    private void clearPanel() {
        for (int x = 0; x < SIZE.x; x++) {
            for (int y = 0; y < SIZE.y; y++) {
                blocks[x][y] = null;
            }
        }
    }

    /**
     * Set the current tetromino that will be drawn
     * @param tetromino the tetromino that will be drawn
     */
    public void setCurrentTetromino(Tetromino tetromino) {
        currentShowingTetromino = tetromino;
    }

    /**
     * Set the current tetromino and draw it to the panel
     * @param tetromino the tetromino that will be drawn
     */
    public void setAndUpdateTetromino(Tetromino tetromino) {
        clearPanel();
        currentShowingTetromino = tetromino;
        currentShowingTetromino.setOrigin(Vector2D.clone(BlockOrigin));
        convertTetrominoToPixel();
        repaint();
    }

    /**
     * Update the panel by drawing the current tetromino and will clear if the current tetromino is null
     */
    public void update() {
        clearPanel();
        if (currentShowingTetromino != null) {
            convertTetrominoToPixel();
        }
        repaint();
    }

}
