package game.main.tetromino;

import game.main.math.Vector2D;
import game.main.tetromino.state.TetrominoState;

import java.awt.*;
import java.util.ArrayList;

/**
 * Interface for tetromino.
 */
public interface Tetromino {
    /**
     * Initialize other block from origin
     */
    void generateBlock();

    /**
     * Rotate the tetrmino
     */
    void rotate();

    /**
     * Update the position of the tetromino
     */
    void update();

    /**
     * Get the color of the tetromino
     * @return Color Current color of the block
     */
    Color getColor();

    /**
     * Set the tetromino's color
     * @param color Color of the tetromino
     */
    void setColor(Color color);

    /**
     * Get the origin of the tetromino
     * @return Vector2D Origin of the tetromino
     */
    Vector2D getOrigin();

    /**
     * Set the origin of the tetromino
     * @param origin Set the origin of the tetromino
     */
    void setOrigin(Vector2D origin);

    /**
     * Get the list of all blocks of the tetromino
     * @return ArrayList<Vector2D> List of all blocks of the tetromino
     */
    ArrayList<Vector2D> getPositions();

    /**
     * Set the position of all block of the tetromino to a new position
     * @param positions A new list of position that will replace the old list
     */
    void setPositions(ArrayList<Vector2D> positions);

    /**
     * Get the name of the tetromino
     * @return String name of this tetromino
     */
    String toString();

    /**
     * Get the type of the tetromino
     * @return int type of the tetromino
     */
    TetrominoType getType();

    /**
     * Set the tetromino state to a new state
     *
     * @param state New state of the tetromino
     */
    void setState(TetrominoState state);

    /**
     * Get the tetromino state
     * @return Current state of the tetromino
     */
    TetrominoState getState();

    /**
     * Get the next position of the next state of the tetromino
     * @return Next position of the next state of the tetromino
     */
    ArrayList<Vector2D> getNextStatePosition();
}
