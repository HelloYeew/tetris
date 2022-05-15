package game.main.tetromino.state;

import game.main.math.Vector2D;
import game.main.tetromino.Tetromino;

import java.util.ArrayList;

/**
 * Third state of the J tetromino (diagram 3).
 */
public class TetrominoJStateThree implements TetrominoState, TetrominoIState {
    /**
     * Rotates the Tetromino.
     *
     * @param tetromino the Tetromino to rotate
     */
    @Override
    public void rotate(Tetromino tetromino) {
        tetromino.setState(new TetrominoJStateOne());
        Vector2D origin = tetromino.getOrigin();
        ArrayList<Vector2D> newPositions = new ArrayList<>();
        newPositions.add(new Vector2D(origin.x, origin.y));
        newPositions.add(new Vector2D(origin.x, origin.y - 1));
        newPositions.add(new Vector2D(origin.x + 1, origin.y));
        newPositions.add(new Vector2D(origin.x + 2, origin.y));
        tetromino.setPositions(newPositions);
    }
}
