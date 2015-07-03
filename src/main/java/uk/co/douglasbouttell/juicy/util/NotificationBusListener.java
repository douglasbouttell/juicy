package uk.co.douglasbouttell.juicy.util;

/**
 * @author Douglas
 * @since 03/07/2015
 */
public interface NotificationBusListener<E> {
    void onAdd(E e);
    void onRemove(E e);
}
