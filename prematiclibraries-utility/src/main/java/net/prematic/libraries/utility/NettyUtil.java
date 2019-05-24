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

    public static EventLoopGroup newEventLoopGroup() {
        if(Epoll.isAvailable()) return new EpollEventLoopGroup();
        else if(KQueue.isAvailable()) return new KQueueEventLoopGroup();
        else return new NioEventLoopGroup();
    }

    public static EventLoopGroup getNewEventLoopGroup(int threads) {
        if(Epoll.isAvailable()) return new EpollEventLoopGroup(threads);
        else if(KQueue.isAvailable()) return new KQueueEventLoopGroup(threads);
        else return new NioEventLoopGroup(threads);
    }

    public static Class<? extends SocketChannel> getSocketChannelClass() {
        if(Epoll.isAvailable()) return EpollSocketChannel.class;
        else if(KQueue.isAvailable()) return KQueueSocketChannel.class;
        else return NioSocketChannel.class;
    }

    public static Class<? extends ServerSocketChannel> getServerSocketChannelClass() {
        if(Epoll.isAvailable()) return EpollServerSocketChannel.class;
        else if(KQueue.isAvailable()) return KQueueServerSocketChannel.class;
        else return NioServerSocketChannel.class;
    }

}
