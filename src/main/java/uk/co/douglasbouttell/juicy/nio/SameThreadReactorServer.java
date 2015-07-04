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

/**
 * @author Douglas
 * @since 04/07/2015
 */
public class SameThreadReactorServer extends ReactorServer {
    
    public SameThreadReactorServer(InetSocketAddress socketAddress, ReactorServerAcceptor acceptor, boolean blocking) throws IOException {
        super(socketAddress, acceptor, blocking);
    }

    public SameThreadReactorServer(int port, ReactorServerAcceptor acceptor, boolean blocking) throws IOException {
        super(port, acceptor, blocking);
    }

    public SameThreadReactorServer(int port, ReactorServerAcceptor acceptor) throws IOException {
        super(port, acceptor);
    }

    @Override
    protected void dispatch(SelectionKey selectionKey) {
        Object attachment = selectionKey.attachment();
        if (attachment instanceof Runnable) {
            Runnable r = (Runnable) attachment;
            r.run();
        }
    }
}
