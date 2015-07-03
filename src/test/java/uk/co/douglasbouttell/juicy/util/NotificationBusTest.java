package uk.co.douglasbouttell.juicy.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * @author Douglas
 * @since 03/07/2015
 */
public class NotificationBusTest {

    @Test
    public void basicAddTest() {
        NotificationBus<Long> nb = new NotificationBus<Long>();

        nb.add(1L);
        nb.add(2L);
        nb.add(3L);
        nb.add(4L);

        List<Long> items = nb.getAll();

        Assert.assertEquals("getAll() size", 4, items.size());
        Assert.assertEquals(1L, (long)items.get(0));
        Assert.assertEquals(2L, (long)items.get(1));
        Assert.assertEquals(3L, (long)items.get(2));
        Assert.assertEquals(4L, (long)items.get(3));
    }

    @Test
    public void basicRemoveTest() {
        NotificationBus<Long> nb = new NotificationBus<Long>();
        List<Long> items;

        nb.add(1L);
        nb.add(2L);
        nb.add(3L);
        nb.add(4L);

        items = nb.getAll();

        Assert.assertEquals("getAll() size", 4, items.size());
        Assert.assertEquals(1L, (long)items.get(0));
        Assert.assertEquals(2L, (long)items.get(1));
        Assert.assertEquals(3L, (long)items.get(2));
        Assert.assertEquals(4L, (long)items.get(3));

        nb.remove(1L);
        nb.remove(2L);
        nb.remove(3L);
        nb.remove(4L);

        items = nb.getAll();
        Assert.assertEquals("getAll() empty", 0, items.size());
    }
}
