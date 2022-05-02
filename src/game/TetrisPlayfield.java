package game;

import math.Vector2D;
import tetromino.Tetromino;
import tetromino.TetrominoType;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

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
    public Vector2D SIZE;

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
     * The Y position that will check the game over condition there.
     */
    private int gameOverYPosition = 5;

    /**
     * Initialize the playfield with the given size.
     * @param size the size of the playfield in Vector2D
     */
    public TetrisPlayfield(Vector2D size) {
        this.SIZE = size;
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(BLOCK_SIZE * size.x, BLOCK_SIZE * size.y));
        blocks = new Color[size.x][size.y];
        createNewTetromino();
        convertTetrominoToPixel();
    }

    /**
     * The method that is called by the JFrame to paint the playfield.
     * @param g  the <code>Graphics</code> context in which to paint
     */
    @Override
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
     * Paint all the current tetromino posuitions to the playfield.
     */
    private void convertTetrominoToPixel() {
        for (Vector2D position : currentTetromino.getPositions()) {
            blocks[position.x][position.y] = currentTetromino.getColor();
        }
    }

    /**
     * Update the playfield and check collision.
     * <br>
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
     * If collided, it will create a new tetromino.
     */
    private Boolean checkCollision() {
        for (Vector2D position : currentTetromino.getPositions()) {
            // Check if there's other block below the tetromino that's not itself, unbind the current tetromino
            if (position.y < SIZE.y - 1) {
                if (blocks[position.x][position.y + 1] != null && blocks[position.x][position.y + 1] != currentTetromino.getColor()) {
                    createNewTetromino();
                    return true;
                } else if (blocks[position.x][position.y + 1] != null && blocks[position.x][position.y + 1] == currentTetromino.getColor()) {
                    // Check that is the block below is not inside itself
                    if (currentTetromino.getPositions().stream().noneMatch(p -> p.x == position.x && p.y == position.y + 1)) {
                        createNewTetromino();
                        return true;
                    }
                }
            }

            // if it's the bottom of the playfield, create a new tetromino
            if (position.y == SIZE.y - 1) {
                createNewTetromino();
                return true;
            }
        }
        return false;
    }

    /**
     * Set a new current tetromino by using random utilities in the tetromino type enum.
     */
    private void createNewTetromino() {
        currentTetromino = TetrominoType.getRandomTetromino(SPAWN_POSITION);
    }

    /**
     * Remove the old positions of tetromino that's painted on the playfield.
     */
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
        if (currentTetromino.getPositions().stream().allMatch(position -> position.x < SIZE.x - 1)) {
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
     * Move down the tetromino quicker than normal drop.
     *
     * This method will check on collision too to make it bind a new tetromino if it's collide with another one or go to the bottom of the playfield.
     *
     * This method need to recall almost all the paint method due to the frame stuttering.
     */
    public void moveDown() {
        // TODO: The update of the tetromino position has conflict with the main update loop. Need to fix it.
        if (currentTetromino.getPositions().stream().allMatch(position -> position.y < SIZE.y - 2) && !checkCollision()) {
            cleanCurrentTetrominoPositions();
            for (Vector2D position : currentTetromino.getPositions()) {
                position.y++;
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
        for (int x = 0; x < SIZE.x; x++) {
            for (int y = 0; y < SIZE.y; y++) {
                g.drawRect(x * BLOCK_SIZE, y * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
            }
        }
        // paint the border as red at game over
        g.setColor(Color.RED);
        for (int x = 0; x < SIZE.x; x++) {
            g.drawRect(x * BLOCK_SIZE, gameOverYPosition * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
        }
    }

    /**
     * Generate a permanent row of ghost block at the bottom of the playfield
     */
    public void generatePermanentRow() {
        // TODO: When generate, it need to push up the entire playfield by 1
        for (int y = SIZE.y - 1; y >= 0; y--) {
            if (blocks[0][y] == Color.DARK_GRAY) {
                continue;
            } else {
                for (int x = 0; x < SIZE.x; x++) {
                    blocks[x][y] = Color.DARK_GRAY;
                }
                break;
            }
        }
    }

    private void checkRow() {
        // if all blocks fill in a row, remove the row
        for (int y = 0; y < SIZE.y; y++) {
            boolean isRowFull = true;
            for (int x = 0; x < SIZE.x; x++) {
                if (blocks[x][y] == null && blocks[x][y] != Color.DARK_GRAY) {
                    isRowFull = false;
                }
            }
            if (isRowFull) {
                // remove the row and move all the rows above down
                for (int x = 0; x < SIZE.x; x++) {
                    blocks[x][y] = null;
                }
                for (int y2 = y; y2 > 0; y2--) {
                    for (int x = 0; x < SIZE.x; x++) {
                        blocks[x][y2] = blocks[x][y2 - 1];
                    }
                }
                y++;
            }
        }
    }

    /**
     * Check the game over condition by check that is there any block in the row that we set as game over row.
     *
     * @return true if there is any block in the game over row, false otherwise
     */
    public Boolean isGameOver() {
        // if row in gameOverYPosition has a block and is not currentTetromino, game over
        for (int x = 0; x < SIZE.x; x++) {
            if (blocks[x][gameOverYPosition] != null && blocks[x][gameOverYPosition] != currentTetromino.getColor()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get the current tetromino that player is controlling
     *
     * @return the current tetromino that player is controlling
     */
    public Tetromino getCurrentTetromino() {
        return currentTetromino;
    }
}