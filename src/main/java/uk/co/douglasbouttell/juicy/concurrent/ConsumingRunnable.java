package uk.co.douglasbouttell.juicy.concurrent;

import java.util.Queue;

/**
 * @author Douglas
 * @since 03/07/2015
 */
public abstract class ConsumingRunnable<E> extends LoopingRunnable {

    private final Queue<E> queue;

    public ConsumingRunnable(Queue<E> queue) {
        this.queue = queue;
    }

    @Override
    final protected void loop() {
        E e = queue.poll();
        if (e != null) {
            consume(e);
        }
    }

    protected abstract void consume(E e);
}
