import math.Vector2D;
import tetromino.Tetromino;
import tetromino.TetrominoType;

import javax.swing.*;
import java.awt.*;

/**
 * Playfield for player.
 */
public class TetrisPlayfield extends JPanel {
    /**
     * The size of one block in pixel
     */
    public int BLOCK_SIZE = 20;

    /**
     * Size of playfield
     */
    public Vector2D size;

    /**
     * Array that's store all blocks
     */
    private Color[][] blocks;

    /**
     * The current tetromino that is falling
     */
    private Tetromino currentTetromino;

    /**
     * Spawn position of a new tetromino
     */
    public Vector2D SPAWN_POSITION = new Vector2D(3,3);

    /**
     * Initialize the playfield with the given size.
     * @param size the size of the playfield in Vector2D
     */
    public TetrisPlayfield(Vector2D size) {
        this.size = size;
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(BLOCK_SIZE * size.x, BLOCK_SIZE * size.y));
        blocks = new Color[size.x][size.y];
        getNewTetromino();
        convertTetrominoToPixel();
    }

    /**
     * The method that is called by the JFrame to paint the playfield.
     * @param g  the <code>Graphics</code> context in which to paint
     */
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
     * Paint all the current tetromino posuitions to the playfield.
     */
    private void convertTetrominoToPixel() {
        for (Vector2D position : currentTetromino.getPositions()) {
            blocks[position.x][position.y] = currentTetromino.getColor();
        }
    }

    /**
     * Update the playfield and check collision.
     *
     * Normally this method is called by the main game loop.
     */
    public void update() {
        // Remove the old positions of the tetromino before update
        cleanCurrentTetrominoPositions();
        // Update the tetromino
        currentTetromino.update();
        convertTetrominoToPixel();
        repaint();
        checkCollision();
    }

    /**
     * Check that the current tetromino is collide with other tetromino or collide with the ground.
     *
     * If collide, it will create a new tetromino.
     */
    private void checkCollision() {
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

    private void getNewTetromino() {
        currentTetromino = TetrominoType.getRandomTetromino(SPAWN_POSITION);
    }

    private void cleanCurrentTetrominoPositions() {
        for (Vector2D position : currentTetromino.getPositions()) {
            blocks[position.x][position.y] = null;
        }
        repaint();
    }

    /**
     * Move the tetromino to the right
     *
     * This method need to recall almost all the paint method due to the frame stuttering.
     */
    public void moveRight() {
        if (currentTetromino.getPositions().stream().allMatch(position -> position.x < size.x - 1)) {
            cleanCurrentTetrominoPositions();
            for (Vector2D position : currentTetromino.getPositions()) {
                position.x++;
            }
            convertTetrominoToPixel();
            repaint();
        }
    }

    /**
     * Move the tetromino to the left
     *
     * This method need to recall almost all the paint method due to the frame stuttering.
     */
    public void moveLeft() {
        if (currentTetromino.getPositions().stream().allMatch(position -> position.x > 0)) {
            cleanCurrentTetrominoPositions();
            for (Vector2D position : currentTetromino.getPositions()) {
                position.x--;
            }
            convertTetrominoToPixel();
            repaint();
        }
    }

    /**
     * Paint the grid
     * @param g the graphics context in which to paint
     */
    private void paintGrid(Graphics g) {
        g.setColor(Color.WHITE);
        for (int x = 0; x < size.x; x++) {
            for (int y = 0; y < size.y; y++) {
                g.drawRect(x * BLOCK_SIZE, y * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
            }
        }
    }

    /**
     * Generate a permanent row of ghost block at the bottom of the playfield
     */
    public void generatePermanentRow() {
        for (int y = size.y - 1; y >= 0; y--) {
            if (blocks[0][y] == Color.DARK_GRAY) {
                continue;
            } else {
                for (int x = 0; x < size.x; x++) {
                    blocks[x][y] = Color.DARK_GRAY;
                }
                break;
            }
        }
    }
}
