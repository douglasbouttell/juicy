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

import java.util.Queue;

/**
 * @author Douglas
 * @since 04/07/2015
 */
public class EventWrapper<V,E> {
    private final V verb;
    private final E element;

    public EventWrapper(V verb, E element) {
        this.verb = verb;
        this.element = element;
    }

    public V getVerb() {
        return verb;
    }

    public E getElement() {
        return element;
    }

    public static <V, E> void dispatch(Queue<EventWrapper<V,E>> q, V verb, E element) {
        q.add(new EventWrapper<V, E>(verb, element));
    }
}
