/*
 * Copyright 2015 Douglas Bouttell
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.co.douglasbouttell.juicy.concurrent;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Douglas
 * @since 03/07/2015
 */
public abstract class LoopingRunnable implements Runnable, Stoppable {

    private final AtomicBoolean keepGoing = new AtomicBoolean(true);
    private final AtomicBoolean finished = new AtomicBoolean(false);
    private final CountDownLatch finishedLatch = new CountDownLatch(1);
    private final AtomicReference<InterruptedException> interruptedCause = new AtomicReference<InterruptedException>(null);

    public void run() {
        setup();
        while(keepGoing.get() && !ThreadUtil.isInterrupted())
            try {
                loop();
            } catch (InterruptedException e) {
                interruptedCause.set(e);
                ThreadUtil.throwUncaughtException(e);
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

    public boolean isInterrupted() {
        return interruptedCause.get() != null;
    }

    public Throwable getInterruptedCause() {
        return interruptedCause.get();
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
