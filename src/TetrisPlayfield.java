import math.Vector2D;
import tetromino.ITetromino;
import tetromino.Tetromino;

import javax.swing.*;
import java.awt.*;

public class TetrisPlayfield extends JPanel {
    public int BLOCK_SIZE = 20;

    public Vector2D size;

    private TetrisPixel[][] blocks;

    private Tetromino currentTetromino;

    public TetrisPlayfield(Vector2D size) {
        this.size = size;
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(BLOCK_SIZE * size.x, BLOCK_SIZE * size.y));
        blocks = new TetrisPixel[size.x][size.y];
        currentTetromino = new ITetromino(new Vector2D(3, 0));
        convertTetrominoToPixel();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        for (int x = 0; x < size.x; x++) {
            for (int y = 0; y < size.y; y++) {
                if (blocks[x][y] != null) {
                    paintBlock(g, blocks[x][y], x, y);
                }
            }
        }
        paintGrid(g);
    }

    private void paintBlock(Graphics g, TetrisPixel block, int x, int y) {
        g.setColor(block.color);
        g.fillRect(x * BLOCK_SIZE, y * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
    }

    private void convertTetrominoToPixel() {
        for (Vector2D position : currentTetromino.getPositions()) {
            blocks[position.x][position.y] = new TetrisPixel(currentTetromino.getColor());
        }
    }

    public void update() {
        // Remove the old positions of the tetromino before update
        for (Vector2D position : currentTetromino.getPositions()) {
            blocks[position.x][position.y] = null;
        }
        // Update the tetromino
        currentTetromino.update();
        convertTetrominoToPixel();
        repaint();
        for (Vector2D position : currentTetromino.getPositions()) {
            // Check if it's on the bottom
            if (position.y == size.y - 1) {
                currentTetromino = new ITetromino(new Vector2D(3, 0));
            }
        }
    }

    private void updateTetromino() {

    }

    private void paintGrid(Graphics g) {
        g.setColor(Color.WHITE);
        for (int x = 0; x < size.x; x++) {
            for (int y = 0; y < size.y; y++) {
                g.drawRect(x * BLOCK_SIZE, y * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
            }
        }
    }
}
