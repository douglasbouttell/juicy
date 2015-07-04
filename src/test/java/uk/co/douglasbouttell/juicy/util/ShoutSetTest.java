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

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Set;

/**
 * @author Douglas
 * @since 03/07/2015
 */
public class ShoutSetTest {

    @Test
    public void basicAddTest() {
        ShoutSet<Long> nb = new ShoutSet<Long>();

        nb.add(1L);
        nb.add(2L);
        nb.add(3L);
        nb.add(4L);

        List<Long> items = JuicyCollections.copySetToList(Long.class, nb);

        Assert.assertEquals("getAll() size", 4, items.size());
        Assert.assertEquals(1L, (long)items.get(0));
        Assert.assertEquals(2L, (long)items.get(1));
        Assert.assertEquals(3L, (long)items.get(2));
        Assert.assertEquals(4L, (long)items.get(3));
        nb.stop();
    }

    @Test
    public void basicRemoveTest() {
        ShoutSet<Long> nb = new ShoutSet<Long>();
        Set<Long> nbSet = nb;

        List<Long> items;

        nb.add(1L);
        nb.add(2L);
        nb.add(3L);
        nb.add(4L);

        items = JuicyCollections.copySetToList(Long.class, nbSet);

        Assert.assertEquals("getAll() size", 4, items.size());
        Assert.assertEquals(1L, (long)items.get(0));
        Assert.assertEquals(2L, (long) items.get(1));
        Assert.assertEquals(3L, (long) items.get(2));
        Assert.assertEquals(4L, (long) items.get(3));

        nb.remove(1L);
        nb.remove(2L);
        nb.remove(3L);
        nb.remove(4L);

        items = JuicyCollections.copySetToList(Long.class, nbSet);
        Assert.assertEquals("getAll() empty", 0, items.size());
        nb.stop();
    }

    @Test
    public void notificationTest() {
        ShoutSetListener<Long> listener =  Mockito.mock(ShoutSetListener.class);
        ShoutSet<Long> nb = new ShoutSet<Long>();

        nb.addListener(listener);

        nb.add(1L);
        nb.add(2L);
        nb.add(3L);
        nb.add(4L);
        nb.remove(1L);
        nb.remove(2L);
        nb.remove(3L);
        nb.remove(4L);

        Mockito.verify(listener).onAdd(1L);
        Mockito.verify(listener).onAdd(2L);
        Mockito.verify(listener).onAdd(3L);
        Mockito.verify(listener).onAdd(4L);
        Mockito.verify(listener).onRemove(1L);
        Mockito.verify(listener).onRemove(2L);
        Mockito.verify(listener).onRemove(3L);
        Mockito.verify(listener).onRemove(4L);
        Mockito.verifyNoMoreInteractions(listener);
        nb.stop();
    }

}
