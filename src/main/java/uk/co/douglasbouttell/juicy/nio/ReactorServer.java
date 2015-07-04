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
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Douglas
 * @since 04/07/2015
 */
public abstract class ReactorServer implements Runnable {
    /* http://jeewanthad.blogspot.co.uk/2013/03/reacter-pattern-explained-part-2.html */

    final Selector selector;
    final ServerSocketChannel serverSocketChannel;

    public ReactorServer(InetSocketAddress socketAddress, ReactorServerAcceptor acceptor, boolean blocking) throws IOException {
        selector = Selector.open();
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(socketAddress);
        serverSocketChannel.configureBlocking(blocking);
        SelectionKey selectionKey0 = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        acceptor.setServerSocketChannel(serverSocketChannel);
        acceptor.setSelector(selector);
        selectionKey0.attach(acceptor);
    }

    public ReactorServer(int port, ReactorServerAcceptor acceptor, boolean blocking) throws IOException {
        this(new InetSocketAddress(port), acceptor, blocking);
    }

    public ReactorServer(int port, ReactorServerAcceptor acceptor) throws IOException {
        this(new InetSocketAddress(port), acceptor, false);
    }

    public void run() {

        System.out.println("Server listening to port: " + serverSocketChannel.socket().getLocalPort());
        try {
            while (!Thread.interrupted()) {
                selector.select();
                Set<SelectionKey> selected = selector.selectedKeys();
                for (SelectionKey aSelected : selected) {
                    dispatch(aSelected);
                }
                selected.clear();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    protected abstract void dispatch(SelectionKey selectionKey);
}
