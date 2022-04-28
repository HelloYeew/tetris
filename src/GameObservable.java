import java.util.Observable;

/**
 * The class that's act as the observable for the game. It will update the time in the game and call the update method
 * in the observer.
 */
public class GameObservable extends Observable {
    /**
     * The time that's passed in the game.
     */
    private int tick;

    /**
     * Boolean that's used to check if the game is running.
     */
    private Boolean isRunning;

    /**
     * Boolean that's used to check if the game is over.
     */
    private Boolean isOver;

    /**
     * Main thread that's used to run the game.
     */
    private Thread updateThread;

    /**
     * Delay between each update of the game time in milliseconds.
     */
    public long DELAYED_TICK = 200;

    /**
     * Create a new observable with initial state.
     */
    public GameObservable() {
        this.tick = 0;
        this.isRunning = false;
        this.isOver = false;
    }

    /**
     * Start the game by starting the thread.
     */
    public void start() {
        this.isRunning = true;
        this.updateThread = new Thread(() -> {
            while (true) {
                if (this.isRunning) {
                    tick();
                }
                try {
                    Thread.sleep(DELAYED_TICK);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        this.updateThread.start();
    }

    /**
     * Update the game time and notify the observers.
     */
    private void tick() {
        this.tick++;
        setChanged();
        notifyObservers();
    }

    /**
     * Get running state.
     * @return the running state.
     */
    public Boolean getRunning() {
        return isRunning;
    }

    /**
     * Set running state.
     * @param running the new running state.
     */
    public void setRunning(Boolean running) {
        isRunning = running;
    }

    /**
     * Get over state.
     * @return the over state.
     */
    public Boolean getOver() {
        return isOver;
    }

    /**
     * Set over state.
     * @param over the new over state.
     */
    public void setOver(Boolean over) {
        isOver = over;
    }

    /**
     * Get current tick.
     * @return the current tick.
     */
    public int getTick() {
        return tick;
    }
}
