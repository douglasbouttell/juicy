package uk.co.douglasbouttell.juicy.concurrent;

/**
 * @author Douglas
 * @since 03/07/2015
 */
public abstract class Runnable implements java.lang.Runnable {

    protected static boolean isInterrupted() {
        return Thread.currentThread().isInterrupted();
    }
}
