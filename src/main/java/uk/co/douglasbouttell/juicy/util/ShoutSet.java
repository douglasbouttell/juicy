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

package uk.co.douglasbouttell.juicy.util;

import uk.co.douglasbouttell.juicy.concurrent.ConsumingRunnable;
import uk.co.douglasbouttell.juicy.concurrent.Stoppable;

import java.util.*;
import java.util.concurrent.*;

/**
 * @author Douglas Bouttell
 * @since 03/07/2015
 */
public class ShoutSet<E> implements Set<E>, Stoppable, Listenable<ShoutSetListener<E>> {

    public enum EventVerb {
        ADD,
        REMOVE
    }

    private final Set<E> active = new CopyOnWriteArraySet<E>();
    private final List<ShoutSetListener<E>> listeners = new CopyOnWriteArrayList<ShoutSetListener<E>>();
    private final Queue<EventWrapper<EventVerb, E>> dispatchQueue = new ConcurrentLinkedQueue<EventWrapper<EventVerb, E>>();
    private final Dispatcher<E> dispatch;
    private final Future dispatchFuture;

    /**
     * Default contructor
     *
     * Will create an executor to dispatch events
     */
    public ShoutSet() {
        this(Executors.newSingleThreadExecutor(new ThreadFactory() {
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setName("notification-bus-dispatcher");
                return t;
            }
        }));
    }

    /**
     * Specify an executor for the event dispatch
     * @param exec An executor
     */
    public ShoutSet(final ExecutorService exec) {
        this.dispatch = new Dispatcher<E>(dispatchQueue, listeners);
        this.dispatchFuture = exec.submit(dispatch);
    }

    public void stop() {
        try {
            this.dispatch.stop();
            this.dispatch.awaitFinished();
        } catch (InterruptedException e) {
            this.dispatchFuture.cancel(true);
        }
    }

    public int size() {
        return active.size();
    }

    public boolean isEmpty() {
        return active.isEmpty();
    }

    public boolean contains(Object o) {
        return active.contains(o);
    }

    public Iterator<E> iterator() {
        return active.iterator();
    }

    public Object[] toArray() {
        return active.toArray();
    }

    public <T> T[] toArray(T[] a) {
        return active.toArray(a);
    }

    public boolean add(E e) {
        if (active.add(e)) {
            EventWrapper.dispatch(dispatchQueue, EventVerb.ADD, e);
            return true;
        }
        return false;
    }

    public boolean remove(Object o) {
        if (active.remove(o)) {
            EventWrapper.dispatch(dispatchQueue, EventVerb.REMOVE, (E)o);
            return true;
        }
        return false;
    }

    public boolean containsAll(Collection<?> c) {
        return active.containsAll(c);
    }

    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (E e : c) {
            changed = changed | add(e);
        }
        return changed;
    }

    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object o : c) {
            changed = changed | remove(o);
        }
        return changed;
    }

    public void clear() {
        removeAll(active);
    }

    /**
     * Add a listener
     * @param listener
     */
    public void addListener(ShoutSetListener<E> listener) {
        listeners.add(listener);
    }

    /**
     * Remove a listener
     * @param listener
     * @return The listener removed or null
     */
    public void removeListener(ShoutSetListener<E> listener) {
        listeners.remove(listener);
    }

    private static class Dispatcher<E> extends ConsumingRunnable<EventWrapper<EventVerb, E>> {
        private final List<ShoutSetListener<E>> listeners;

        public Dispatcher(Queue<EventWrapper<EventVerb, E>> q, List<ShoutSetListener<E>> listeners) {
            super(q);
            this.listeners = listeners;
        }

        @Override
        public void setup() {
            // Do nothing
        }

        @Override
        protected void consume(EventWrapper<EventVerb, E> ev) throws InterruptedException {
            for (ShoutSetListener<E> listener : listeners) {
                switch (ev.getVerb()) {
                    case ADD: listener.onAdd(ev.getElement()); break;
                    case REMOVE: listener.onRemove(ev.getElement()); break;
                    default: throw new InterruptedException("Unknown Verb");
                }
            }
        }
    }

}
