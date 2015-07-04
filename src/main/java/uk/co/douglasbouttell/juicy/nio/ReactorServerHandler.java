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

package uk.co.douglasbouttell.juicy.nio;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * @author Douglas
 * @since 04/07/2015
 */
public abstract class ReactorServerHandler implements Runnable {

    private final SelectionKey selectionKey;
    private final SocketChannel socketChannel;

    public ReactorServerHandler(Selector selector, SocketChannel socketChannel) throws IOException {
        this(selector, socketChannel, false);
    }

    public ReactorServerHandler(Selector s, SocketChannel c, boolean blocking) throws IOException {
        this.socketChannel = c;
        c.configureBlocking(blocking);
        selectionKey = socketChannel.register(s, 0);
        selectionKey.attach(this);
        selectionKey.interestOps(SelectionKey.OP_READ);
        s.wakeup();
    }

    public SelectionKey getSelectionKey() {
        return selectionKey;
    }

    public SocketChannel getSocketChannel() {
        return socketChannel;
    }
}
