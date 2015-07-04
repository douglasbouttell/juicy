package uk.co.douglasbouttell.juicy.util;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * @author Douglas
 * @since 04/07/2015
 */
public class JuicyCollections {

    public static <E> List<E> copySetToList(Class<E> clazz, Set<E> set) {
        @SuppressWarnings("unchecked")
        E[] arr = (E[]) Array.newInstance(clazz,set.size());
        return Arrays.asList(set.toArray(arr));
    }
}
