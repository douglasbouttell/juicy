package uk.co.douglasbouttell.juicy.concurrent;

/**
 * @author Douglas
 * @since 03/07/2015
 */
public abstract class LoopingRunnable extends Runnable {

    public void run() {
        setup();
        while(!isInterrupted())
            loop();
    }

    public abstract void setup();

    public abstract void loop();
}
