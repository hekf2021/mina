package com;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import java.util.Date;

public class Myhandler extends IoHandlerAdapter {
    @Override
    public void sessionCreated(IoSession session) throws Exception {
        System.out.println("sessionCreated");
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        System.out.println("sessionOpened");
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        System.out.println("sessionClosed");
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        System.out.println("sessionIdle");
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        System.out.println("exceptionCaught");
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        Date date = new Date();
        String msg = (String) message;
        System.out.println("服务端接受到的数据："+msg);
        if("exit".equals(msg)){
            session.close();
        }
        session.write(date);
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        System.out.println("messageSent");
    }
}
