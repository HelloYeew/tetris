package tetromino;

import math.Vector2D;

import java.awt.*;
import java.util.ArrayList;

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
}
