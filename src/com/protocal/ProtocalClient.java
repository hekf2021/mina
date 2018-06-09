package com.protocal;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.IoFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

public class ProtocalClient {

    private static final String HOST="127.0.0.1";
    private static final int PORT=7080;
    static long counter =0;
    final static int fil=100;
    static long start=0;

    public static void main(String[] args){
        start = System.currentTimeMillis();
        IoConnector connector = new NioSocketConnector();
        connector.getFilterChain().addLast("coderc", new ProtocolCodecFilter(new ProtocalFactory(Charset.forName("UTF-8"))));
        connector.getSessionConfig().setReadBufferSize(1024);
        connector.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE,10);
        connector.setHandler(new MyClientHandler());
        ConnectFuture connetfutrue = connector.connect(new InetSocketAddress(HOST, PORT));

        connetfutrue.addListener(new IoFutureListener<ConnectFuture>() {
            public void operationComplete(ConnectFuture future) {
                if(future.isConnected()){
                    IoSession session = future.getSession();
                    sendata(session);
                }
            }
        });
    }

    public static void sendata(IoSession session){
        for(int i=0;i<fil;i++){
            String content ="watchmen:"+i;
            ProtocalPack pack = new ProtocalPack((byte) i,content);
            session.write(pack);
            System.out.println("客户端发送数据："+pack);
        }
    }
}


class MyClientHandler extends IoHandlerAdapter{
    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        if(status==IdleStatus.READER_IDLE){
            session.close(true);
        }
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
       ProtocalPack pack = (ProtocalPack) message;
        System.out.println("client->"+pack);
    }
}
