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
 * (C) Copyright 2019 The PrematicLibraries Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 08.02.19 16:17
 *
 * The PrematicLibraries Project is under the Apache License, version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */


/*

Todo Update Network Util
 - remove netty stuff


 */

public final class NetworkUtil {

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

    public static String getExactIp(String address) {
        return address.split("[/:]")[1];
    }

    public static boolean isIP4Address(String ip){
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

    public static boolean isPortAvailable(String host, int port){
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