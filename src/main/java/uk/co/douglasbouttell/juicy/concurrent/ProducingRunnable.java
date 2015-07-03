package uk.co.douglasbouttell.juicy.concurrent;

import java.util.Queue;

/**
 * @author Douglas
 * @since 03/07/2015
 */
public abstract class ProducingRunnable<E> extends LoopingRunnable {

    private final Queue<E> queue;

    public ProducingRunnable(Queue<E> queue) {
        this.queue = queue;
    }

    @Override
    final protected void loop() {
        queue.add(produce());
    }

    protected abstract E produce();

}
