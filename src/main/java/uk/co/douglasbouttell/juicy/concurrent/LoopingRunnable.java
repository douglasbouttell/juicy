package uk.co.douglasbouttell.juicy.concurrent;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Douglas
 * @since 03/07/2015
 */
public abstract class LoopingRunnable implements Runnable {

    private final AtomicBoolean keepGoing = new AtomicBoolean(true);
    private final AtomicBoolean finished = new AtomicBoolean(false);
    private final CountDownLatch finishedLatch = new CountDownLatch(1);

    public void run() {
        setup();
        while(keepGoing.get() && !ThreadUtil.isInterrupted())
            try {
                loop();
            } catch (InterruptedException e) {
                stop();
            }
        finished.set(true);
        finishedLatch.countDown();
    }

    public void stop() {
        keepGoing.set(false);
    }

    public boolean isStopped() {
        return !keepGoing.get();
    }

    public boolean isFinished() {
        return finished.get();
    }

    public void awaitFinished() throws InterruptedException {
        finishedLatch.await();
    }

    public void awaitFinished(long timeout, TimeUnit timeUnit) throws InterruptedException {
        finishedLatch.await(timeout, timeUnit);
    }

    protected abstract void setup();

    protected abstract void loop() throws InterruptedException;
}
