package net.prematic.libraries.utility;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.kqueue.KQueue;
import io.netty.channel.kqueue.KQueueEventLoopGroup;
import io.netty.channel.kqueue.KQueueServerSocketChannel;
import io.netty.channel.kqueue.KQueueSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyUtil {

    public static final boolean EPOLL = Epoll.isAvailable();
    public static final boolean KQUEUE = KQueue.isAvailable();

    public static EventLoopGroup newEventLoopGroup() {
        if(EPOLL) return new EpollEventLoopGroup();
        else if(KQUEUE) return new KQueueEventLoopGroup();
        else return new NioEventLoopGroup();
    }

    public static EventLoopGroup getNewEventLoopGroup(int threads) {
        if(EPOLL) return new EpollEventLoopGroup(threads);
        else if(KQUEUE) return new KQueueEventLoopGroup(threads);
        else return new NioEventLoopGroup(threads);
    }

    public static Class<? extends SocketChannel> getSocketChannelClass() {
        if(EPOLL) return EpollSocketChannel.class;
        else if(KQUEUE) return KQueueSocketChannel.class;
        else return NioSocketChannel.class;
    }

    public static Class<? extends ServerSocketChannel> getServerSocketChannelClass() {
        if(EPOLL) return EpollServerSocketChannel.class;
        else if(KQUEUE) return KQueueServerSocketChannel.class;
        else return NioServerSocketChannel.class;
    }

}
