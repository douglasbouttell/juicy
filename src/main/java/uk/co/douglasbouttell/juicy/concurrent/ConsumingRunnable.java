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
    final protected void loop() throws InterruptedException {
        E e = queue.poll();
        if (e != null) {
            consume(e);
        }
    }

    protected abstract void consume(E e) throws InterruptedException;
}
