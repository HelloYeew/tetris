package game.main.tetromino.state;

import game.main.math.Vector2D;
import game.main.tetromino.Tetromino;

import java.util.ArrayList;

/**
 * Second state of the J tetromino (diagram 2).
 */
public class TetrominoJStateTwo implements TetrominoState, TetrominoIState {
    /**
     * Rotates the Tetromino.
     * @param tetromino the Tetromino to rotate
     */
    @Override
    public void rotate(Tetromino tetromino) {
        tetromino.setState(new TetrominoJStateThree());
        Vector2D origin = tetromino.getOrigin();
        ArrayList<Vector2D> newPositions = new ArrayList<>();
        newPositions.add(new Vector2D(origin.x, origin.y));
        newPositions.add(new Vector2D(origin.x + 1, origin.y));
        newPositions.add(new Vector2D(origin.x, origin.y + 1));
        newPositions.add(new Vector2D(origin.x, origin.y + 2));
        tetromino.setPositions(newPositions);
    }
}
