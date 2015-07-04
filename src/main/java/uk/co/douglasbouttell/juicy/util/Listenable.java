package uk.co.douglasbouttell.juicy.util;

/**
 * @author Douglas
 * @since 04/07/2015
 */
public interface Listenable<E> {
    void addListener(E listener);
    void removeListener(E listener);
}
