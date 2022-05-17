package game.main.tetromino.state;

import game.main.math.Vector2D;
import game.main.tetromino.Tetromino;

import java.util.ArrayList;

/**
 * First state of the L tetromino (diagram 1).
 */
public class TetrominoLStateOne implements TetrominoState, TetrominoLState {
    /**
     * Rotates the Tetromino.
     * @param tetromino the Tetromino to rotate
     */
    @Override
    public void rotate(Tetromino tetromino) {
        tetromino.setState(new TetrominoLStateTwo());
        Vector2D origin = tetromino.getOrigin();
        ArrayList<Vector2D> newPositions = new ArrayList<>();
        newPositions.add(new Vector2D(origin.x, origin.y));
        newPositions.add(new Vector2D(origin.x + 1, origin.y));
        newPositions.add(new Vector2D(origin.x + 2, origin.y));
        newPositions.add(new Vector2D(origin.x, origin.y + 1));
        tetromino.setPositions(newPositions);
    }
}
