import math.Vector2D;
import tetromino.TetrominoI;
import tetromino.Tetromino;
import tetromino.TetrominoType;

import javax.swing.*;
import java.awt.*;

public class TetrisPlayfield extends JPanel {
    public int BLOCK_SIZE = 20;

    public Vector2D size;

    private Color[][] blocks;

    private Tetromino currentTetromino;

    public Vector2D SPAWN_POSITION = new Vector2D(3,3);

    public TetrisPlayfield(Vector2D size) {
        this.size = size;
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(BLOCK_SIZE * size.x, BLOCK_SIZE * size.y));
        blocks = new Color[size.x][size.y];
        getNewTetromino();
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

    private void paintBlock(Graphics g, Color color, int x, int y) {
        g.setColor(color);
        g.fillRect(x * BLOCK_SIZE, y * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
    }

    private void convertTetrominoToPixel() {
        for (Vector2D position : currentTetromino.getPositions()) {
            blocks[position.x][position.y] = currentTetromino.getColor();
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
            // Check if there's other block below the tetromino that's not itself, unbind the current tetromino
            if (position.y < size.y - 1) {
                if (blocks[position.x][position.y + 1] != null && !currentTetromino.getPositions().contains(new Vector2D(position.x, position.y + 1))) {
                    getNewTetromino();
                }
            }

            // if it's the bottom of the playfield, create a new tetromino
            if (position.y == size.y - 1) {
                getNewTetromino();
            }
        }
    }

    private void updateTetromino() {

    }

    private void getNewTetromino() {
        currentTetromino = TetrominoType.getRandomTetromino(SPAWN_POSITION);
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
