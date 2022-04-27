import java.util.Observable;

public class GameObservable extends Observable {
    private int tick;

    private Boolean isRunning;

    private Boolean isOver;

    private Thread updateThread;

    public long DELAYED_TICK = 200;

    public GameObservable() {
        this.tick = 0;
        this.isRunning = false;
        this.isOver = false;
    }

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

    private void tick() {
        this.tick++;
        setChanged();
        notifyObservers();
    }

    public int getTick() {
        return tick;
    }
}
