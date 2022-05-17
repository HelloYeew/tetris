package game.main;

import game.main.strategy.TetrominoRandomStrategy;
import game.main.strategy.TraditionalRandomStrategy;
import game.main.math.Vector2D;
import game.main.tetromino.Tetromino;
import game.main.tetromino.TetrominoI;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

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
     * Boolean value indicating if the playfield received the control from current tick
     */
    public Boolean isReceivedInput = false;

    /**
     * The random strategy that will be used to spawn and control the tetromino
     */
    public TetrominoRandomStrategy randomStrategy;

    /**
     * The next tetromino panel that's bind to the playfield
     */
    public NextTetrominoPanel nextTetrominoPanel;

    /**
     * Initialize the playfield with the given size.
     * @param size the size of the playfield in Vector2D
     */
    public TetrisPlayfield(Vector2D size, TetrominoRandomStrategy randomStrategy) {
        this.randomStrategy = randomStrategy;
        this.SIZE = size;
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(BLOCK_SIZE * size.x, BLOCK_SIZE * size.y));
        blocks = new Color[size.x][size.y];
        createNewTetromino();
        convertTetrominoToPixel();
    }

    /**
     * Bind a new next tetromino panel to the playfield.
     * @param nextTetrominoPanel the next tetromino panel to bind
     */
    public void addNextTetrominoPanel(NextTetrominoPanel nextTetrominoPanel) {
        this.nextTetrominoPanel = nextTetrominoPanel;
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
     * Paint all the current tetromino positions to the playfield.
     */
    private void convertTetrominoToPixel() {
        cleanCurrentTetrominoPositions();
        for (Vector2D position : currentTetromino.getPositions()) {
            // check if the position is not out of bound
            if (position.x >= 0 && position.x < SIZE.x && position.y >= 0 && position.y < SIZE.y) {
                blocks[position.x][position.y] = currentTetromino.getColor();
            }
        }
    }

    /**
     * Update the playfield and check collision.
     * <br>
     * Normally this method is called by the main game loop.
     */
    public void update() {
        if (nextTetrominoPanel != null) {
            try {
                nextTetrominoPanel.setAndUpdateTetromino(randomStrategy.getTetrominoList().get(0));
            } catch (NullPointerException e) {
                // This is in case on multiplayer mode that's no information on this
                nextTetrominoPanel.update();
            }
        }
        // Remove the old positions of the tetromino before update
        cleanCurrentTetrominoPositions();
        // Update the tetromino
        currentTetromino.update();
        convertTetrominoToPixel();
        repaint();
        checkCollision(true);
    }

    /**
     * Check that the current tetromino is collide with other tetromino or collide with the ground.
     * If collided, it will create a new tetromino.
     */
    public Boolean checkCollision(Boolean createNewWhenColliding) {
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

            // if it's the bottom of the playfield, create a new tetromino if it's collided
            if (position.y == SIZE.y - 1) {
                if (createNewWhenColliding) {
                    createNewTetromino();
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Set a new current tetromino by using random utilities in the tetromino type enum.
     */
    private void createNewTetromino() {
        Tetromino nextTetromino = randomStrategy.getNextTetromino();
        if (nextTetromino != null) {
            nextTetromino.setOrigin(Vector2D.clone(SPAWN_POSITION));
            currentTetromino = nextTetromino;
        } else {
            // maybe sometime the random strategy is not working or this client is using in multiplayer
            // that's waiting for the next tetromino from the server
            currentTetromino = new TetrominoI(Vector2D.clone(SPAWN_POSITION));
        }
    }

    /**
     * Remove the old positions of tetromino that's painted on the playfield.
     */
    public void cleanCurrentTetrominoPositions() {
        for (Vector2D position : currentTetromino.getPositions()) {
            // check if the block is not out of bound
            if (position.x >= 0 && position.x < SIZE.x && position.y >= 0 && position.y < SIZE.y) {
                blocks[position.x][position.y] = null;
            }
        }
        repaint();
    }

    /**
     * Move the tetromino to the right
     * <br>
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
            currentTetromino.setOrigin(currentTetromino.getOrigin().add(new Vector2D(1, 0)));
        }
    }

    /**
     * Move the tetromino to the left
     * <br>
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
            currentTetromino.setOrigin(currentTetromino.getOrigin().add(new Vector2D(-1, 0)));
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
        // create a new board
        Color[][] newBoard = new Color[SIZE.x][SIZE.y];
        // the new board need to put everything up from old board by 1
        for (int x = 0; x < SIZE.x; x++) {
            for (int y = 0; y < SIZE.y; y++) {
                if (y < SIZE.y - 1) {
                    newBoard[x][y] = blocks[x][y + 1];
                }
            }
        }
        // copy the new board to the old board
        blocks = newBoard;
        // clean the current tetromino since the new board is out of sync with cleanCurrentTetrominoPositions method
        // This is not the best way to do this, but it works for now
        for (Vector2D position : currentTetromino.getPositions()) {
            blocks[position.x][position.y - 1] = null;
        }
        // fullfill the row with ghost block
        for (int y = SIZE.y - 1; y >= 0; y--) {
            if (blocks[0][y] == Color.DARK_GRAY) {
                // this row is already a permanent row, can skip
                continue;
            } else {
                for (int x = 0; x < SIZE.x; x++) {
                    blocks[x][y] = Color.DARK_GRAY;
                }
                break;
            }
        }
    }

    /**
     * Return the row number that's full and not the permanent row
     * @return the row number that's full and not the permanent row
     */
    public int countFullRow() {
        int fullRow = 0;
        for (int y = 0; y < SIZE.y; y++) {
            boolean isRowFull = true;
            for (int x = 0; x < SIZE.x; x++) {
                if (blocks[x][y] == null || (blocks[x][y] != null && blocks[x][y] == Color.DARK_GRAY)) {
                    isRowFull = false;
                    break;
                }
            }
            if (isRowFull) {
                fullRow++;
            }
        }
        return fullRow;
    }

    /**
     * Clear the row that's full and not the permanent row
     * @return the row number that's full and not the permanent row
     */
    public int checkAndRemoveRow() {
        int fullRow = 0;
        for (int y = 0; y < SIZE.y; y++) {
            boolean isRowFull = true;
            for (int x = 0; x < SIZE.x; x++) {
                if (blocks[x][y] == null || (blocks[x][y] != null && blocks[x][y] == Color.DARK_GRAY)) {
                    isRowFull = false;
                    break;
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
                fullRow++;
                y++;
            }
        }
        return fullRow;
    }

    /**
     * Check the game over condition by check that is there any block in the row that we set as game over row.
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
     * Restart the game by set everything back to default
     */
    public void restartGame() {
        randomStrategy = new TraditionalRandomStrategy();
        blocks = new Color[SIZE.x][SIZE.y];
        createNewTetromino();
        convertTetrominoToPixel();
    }

    /**
     * Get the current tetromino that player is controlling
     * @return the current tetromino that player is controlling
     */
    public Tetromino getCurrentTetromino() {
        return currentTetromino;
    }

    /**
     * Set the current tetromino that player is controlling
     * @param currentTetromino the current tetromino that player is controlling
     */
    public void setCurrentTetromino(Tetromino currentTetromino) {
        this.currentTetromino = currentTetromino;
    }

    /**
     * Rotate the current tetromino to next tetromino state.
     */
    public void rotate() {
        cleanCurrentTetrominoPositions();
        if (checkCanBeRotated(currentTetromino.getNextStatePosition())) {
            currentTetromino.rotate();
        } else {
            System.out.println("Cannot rotate");
        }
        convertTetrominoToPixel();
        repaint();
    }

    /**
     * Check if the current tetromino can be rotated to next state by checking the next state position.
     * @param newPositions the next state position of the current tetromino
     * @return true if the current tetromino can be rotated to next state, false otherwise
     */
    public Boolean checkCanBeRotated(ArrayList<Vector2D> newPositions) {
        // Can be rotate when the new position is not out of bound and there is no block in the new position
        // Check if the new position is out of bound
        for (Vector2D position : newPositions) {
            if (position.x < 0 || position.x >= SIZE.x || position.y < 0 || position.y >= SIZE.y) {
                System.out.println("Cannot rotate due to out of bound");
                return false;
            }
        }
        // Check if there is any block in the new position
        for (Vector2D position : newPositions) {
            if (blocks[position.x][position.y] != null) {
                System.out.println("Cannot rotate due to block in the new position");
                return false;
            }
        }
        return true;
    }
}
