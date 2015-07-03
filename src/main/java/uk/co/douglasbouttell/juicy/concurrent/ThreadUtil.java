package uk.co.douglasbouttell.juicy.concurrent;

import org.omg.SendingContext.RunTime;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Douglas
 * @since 03/07/2015
 */
public abstract class ThreadUtil {

    public static boolean isInterrupted() {
        return isInterrupted(Thread.currentThread());
    }

    public static boolean isInterrupted(Thread t) {
        return t.isInterrupted();
    }

    public static void addExecutorShutdownHook(final ExecutorService exec) {
        addExecutorShutdownHook(exec, 60, TimeUnit.SECONDS);
    }

    public static void addExecutorShutdownHook(final ExecutorService exec, final long time, final TimeUnit timeUnit) {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                exec.shutdownNow();
            }
        }));
    }
}
