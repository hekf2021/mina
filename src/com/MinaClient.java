package com;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.LineDelimiter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

public class MinaClient {
    static String host="127.0.0.1";
    static int PORT=7080;

    public static void main(String[] args){
        IoSession  session = null;
        IoConnector connector = new NioSocketConnector();
        connector.setConnectTimeout(3000);
        connector.getFilterChain().addLast("coderc",new ProtocolCodecFilter(new TextLineCodecFactory(
                Charset.forName("UTF-8"), LineDelimiter.WINDOWS.getValue(), LineDelimiter.WINDOWS.getValue()
        )));
        connector.getFilterChain().addFirst("filter",new MyClientFilter());
        connector.setHandler(new MyClientHandler());
        ConnectFuture future = connector.connect(new InetSocketAddress(host, PORT));
        future.awaitUninterruptibly();
        session = future.getSession();
        session.write("你好@watchmen");
        session.getCloseFuture().awaitUninterruptibly();
        connector.dispose();


    }

}
