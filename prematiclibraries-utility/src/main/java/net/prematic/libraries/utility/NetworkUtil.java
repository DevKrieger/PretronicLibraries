package net.prematic.libraries.utility;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetAddress;
import java.net.Socket;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 11.09.18 18:50
 *
 */

public class NetworkUtil {

    public static final boolean EPOLL = Epoll.isAvailable();

    public static String getHostAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        }catch (Exception exception) {
            return "0.0.0.0";
        }
    }
    public static EventLoopGroup getNewEventLoopGroup() {
        if(EPOLL) return new EpollEventLoopGroup(); else return new NioEventLoopGroup();
    }
    public static EventLoopGroup getNewEventLoopGroup(int threads) {
        if(EPOLL) return new EpollEventLoopGroup(threads); else return new NioEventLoopGroup(threads);
    }
    public static Class<? extends SocketChannel> getNewSocketChannel() {
        if(EPOLL) return EpollSocketChannel.class; else return NioSocketChannel.class;
    }
    public static Class<? extends ServerSocketChannel> getNewServerSocketChannel() {
        if(EPOLL) return EpollServerSocketChannel.class; else return NioServerSocketChannel.class;
    }
    public static String getExactIP(String address){
        String host = "";
        for(char c : address.toCharArray()){
            if(c == ':') break;
            else host += c;
        }
        return host.substring(1);
    }
    public static Boolean isIP4Address(String ip){
        try {
            if(ip == null || ip.isEmpty()) return false;
            String[] parts = ip.split("\\.");
            if(parts.length != 4) return false;
            for(String s : parts){
                int i = Integer.parseInt(s);
                if((i < 0) || (i > 255)) return false;
            }
            if(ip.endsWith(".")) return false;
            return true;
        }catch (NumberFormatException exception){
            return false;
        }
    }
    public static Boolean isPortAvailable(String host, int port){
        Socket socket = null;
        try{
            socket = new Socket(host, port);
            socket.close();
            return false;
        }catch (Exception exception) {
            return true;
        }
    }
}
