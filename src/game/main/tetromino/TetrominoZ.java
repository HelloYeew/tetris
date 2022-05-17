package game.main.tetromino;

import game.main.math.Vector2D;
import game.main.tetromino.state.TetrominoState;
import game.main.tetromino.state.TetrominoZStateOne;

import java.awt.*;
import java.util.ArrayList;

public class TetrominoZ implements Tetromino {
    private Color color = Color.RED;
    private Vector2D origin;
    private ArrayList<Vector2D> positions;
    private TetrominoState state = new TetrominoZStateOne();

    public TetrominoZ(Vector2D origin) {
        this.origin = origin;
        generateBlock();
    }

    /**
     * Initialize other block from origin
     */
    @Override
    public void generateBlock() {
        positions = new ArrayList<>();
        positions.add(new Vector2D(origin.x, origin.y - 1));
        positions.add(new Vector2D(origin.x + 1, origin.y - 1));
        positions.add(new Vector2D(origin.x + 1, origin.y));
        positions.add(new Vector2D(origin.x + 2, origin.y));
    }

    /**
     * Rotate the tetrmino
     */
    @Override
    public void rotate() {
        state.rotate(this);
    }

    /**
     * Update the position of the tetromino
     */
    @Override
    public void update() {
        for (Vector2D position : positions) {
            position.y += 1;
        }
        origin.y += 1;
    }

    /**
     * Get the color of the tetromino
     * @return Color Current color of the block
     */
    @Override
    public Color getColor() {
        return color;
    }

    /**
     * Set the tetromino's color
     * @param color Color of the tetromino
     */
    @Override
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Get the origin of the tetromino
     * @return Vector2D Origin of the tetromino
     */
    @Override
    public Vector2D getOrigin() {
        return origin;
    }

    /**
     * Set the origin of the tetromino
     * @param origin Set the origin of the tetromino
     */
    @Override
    public void setOrigin(Vector2D origin) {
        this.origin = origin;
        // after setting the origin, we need to update the position of the block
        generateBlock();
    }

    /**
     * Get the list of all blocks of the tetromino
     * @return ArrayList<Vector2D> List of all blocks of the tetromino
     */
    @Override
    public ArrayList<Vector2D> getPositions() {
        return positions;
    }

    /**
     * Set the position of all block of the tetromino to a new position
     * @param positions A new list of position that will replace the old list
     */
    @Override
    public void setPositions(ArrayList<Vector2D> positions) {
        this.positions = positions;
    }

    /**
     * Get the type of the tetromino
     * @return int type of the tetromino
     */
    @Override
    public TetrominoType getType() {
        return TetrominoType.Z;
    }

    /**
     * Set the tetromino state to a new state
     * @param state New state of the tetromino
     */
    @Override
    public void setState(TetrominoState state) {
        this.state = state;
    }

    /**
     * Get the tetromino state
     * @return Current state of the tetromino
     */
    @Override
    public TetrominoState getState() {
        return state;
    }

    /**
     * Get the next position of the next state of the tetromino
     * @return Next position of the next state of the tetromino
     */
    @Override
    public ArrayList<Vector2D> getNextStatePosition() {
        TetrominoI newTetromino = new TetrominoI(origin);
        newTetromino.setPositions(new ArrayList<>(positions));
        newTetromino.getState().rotate(newTetromino);
        return newTetromino.getPositions();
    }
}
