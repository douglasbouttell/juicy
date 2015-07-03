package uk.co.douglasbouttell.juicy.util;

import uk.co.douglasbouttell.juicy.concurrent.ConsumingRunnable;

import java.io.Closeable;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Douglas Bouttell
 * @since 03/07/2015
 */
public class NotificationBus<E> implements Closeable {

    private final Map<Long, E> active = new ConcurrentHashMap<Long, E>();
    private final List<NotificationBusListener<E>> listeners = new CopyOnWriteArrayList<NotificationBusListener<E>>();
    private final Queue<Event<E>> dispatchQueue = new ConcurrentLinkedQueue<Event<E>>();
    private final AtomicLong id = new AtomicLong(0);
    private final ExecutorService exec;
    private final Dispatcher<E> dispatch;
    private final Future dispatchFuture;

    /**
     * Default contructor
     *
     * Will create an executor to dispatch events
     */
    public NotificationBus() {
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
    public NotificationBus(final ExecutorService exec) {
        this.exec = exec;
        this.dispatch = new Dispatcher<E>(dispatchQueue, listeners);
        this.dispatchFuture = this.exec.submit(dispatch);
    }

    public void close() {
        try {
            this.dispatch.stop();
            this.dispatch.awaitFinished();
        } catch (InterruptedException e) {
            this.dispatchFuture.cancel(true);
        }
    }

    /**
     * Add an element
     * @param e The item to add
     * @return the ID of the element in the internal map
     */
    public long add(E e) {
        long id = this.id.getAndIncrement();
        active.put(id, e);
        dispatchQueue.add(new Event<E>(Event.VERB.ADD, id, e));
        return id;
    }

    /**
     * Remove an element by its internal ID
     * @param id The id to remove
     * @return The element removed or null
     */
    public E removeById(long id) {
        E e = active.remove(id);
        if (e != null) {
            dispatchQueue.add(new Event<E>(Event.VERB.REMOVE, id, e));
        }
        return e;
    }

    /**
     * Remove an element
     * @param e The element to remove
     * @return The element removed or null
     */
    public E remove(E e) {
        for (Map.Entry<Long, E> entry : active.entrySet()) {
            if (entry.getValue() == e) {
                return removeById(entry.getKey());
            }
        }
        return null;
    }

    /**
     * Get all the items current stored
     * @return A list of items stored
     */
    public List<E> getAll() {
        return Collections.unmodifiableList(new ArrayList<E>(active.values()));
    }

    /**
     * Add a listener
     * @param listener
     */
    public void addListener(NotificationBusListener<E> listener) {
        listeners.add(listener);
    }

    /**
     * Remove a listener
     * @param listener
     * @return The listener removed or null
     */
    public NotificationBusListener<E> removeListener(NotificationBusListener<E> listener) {
        if (listeners.remove(listener)) {
            return listener;
        } else {
            return null;
        }
    }

    private static class Dispatcher<E> extends ConsumingRunnable<Event<E>> {
        private final List<NotificationBusListener<E>> listeners;

        public Dispatcher(Queue<Event<E>> q, List<NotificationBusListener<E>> listeners) {
            super(q);
            this.listeners = listeners;
        }

        @Override
        public void setup() {
            // Do nothing
        }

        @Override
        protected void consume(Event<E> ev) {
            for (NotificationBusListener<E> listener : listeners) {
                switch (ev.getVerb()) {
                    case ADD: listener.onAdd(ev.getEvent()); break;
                    case REMOVE: listener.onRemove(ev.getEvent()); break;
                    default: break;
                }
            }
        }
    }

    private static class Event<E> {
        public enum VERB {
            ADD,
            REMOVE
        }

        private final E e;
        private final VERB verb;
        private final long id;

        public Event(VERB v, long id, E e) {
            this.verb = v;
            this.id = id;
            this.e = e;
        }

        public long getId() {
            return id;
        }

        public E getEvent() {
            return e;
        }

        public VERB getVerb() {
            return verb;
        }
    }


}
