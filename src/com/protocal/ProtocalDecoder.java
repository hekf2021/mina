package com.protocal;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

public class ProtocalDecoder implements ProtocolDecoder {
    private final AttributeKey CONTEXT = new AttributeKey(this.getClass(),"context");
    private final Charset charset;
    private int maxPackLength=100;

    public ProtocalDecoder(){
        this(Charset.defaultCharset());
    }

    public ProtocalDecoder(Charset charset) {
        this.charset = charset;
    }

    public void decode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
        final int packHeadlength=5;
        Context ctx = this.getContext(session);
        ctx.append(in);
        IoBuffer buf = ctx.getBuf();
        buf.flip();
        while(buf.remaining()>=packHeadlength){
            buf.mark();
            int length = buf.getInt();
            byte flag = buf.get();
            if(length<0||length>maxPackLength){
                buf.reset();
                break;
            }else if(length>=packHeadlength&&(length-packHeadlength)<=buf.remaining()){
                int oldLimit = buf.limit();
                buf.limit(buf.position()+length-packHeadlength);
                String content = buf.getString(ctx.getDecoder());
                buf.limit(oldLimit);
                ProtocalPack pakeage =new ProtocalPack(flag,content);
                out.write(pakeage);
            }else{
                //半包
                buf.clear();
                break;
            }
        }
        if(buf.hasRemaining()){
            IoBuffer temp = IoBuffer.allocate(maxPackLength).setAutoExpand(true);
            temp.put(buf);
            temp.flip();
            buf.reset();
            buf.put(temp);
        }else{
            buf.reset();
        }
    }

    public void finishDecode(IoSession ioSession, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {

    }

    public void dispose(IoSession session) throws Exception {
        Context ctx = (Context) session.getAttribute(CONTEXT);
        if(ctx!=null){
            session.removeAttribute(CONTEXT);
        }
    }

    public int getMaxPackLength() {
        return maxPackLength;
    }

    public void setMaxPackLength(int maxPackLength) {
        if(maxPackLength<0){
            throw new IllegalArgumentException("maxPackLength参数："+maxPackLength);
        }
        this.maxPackLength = maxPackLength;
    }

    public Context getContext(IoSession session){
        Context ctx = (Context) session.getAttribute(CONTEXT);
        if(ctx==null){
            ctx = new Context();
            session.setAttribute(CONTEXT,ctx);
        }
        return ctx;
    }

    private class Context{
        private final CharsetDecoder decoder;
        private IoBuffer buf;
        private Context(){
            decoder= charset.newDecoder();
            buf = IoBuffer.allocate(80).setAutoExpand(true);
        }

        public void append(IoBuffer in ){
            this.getBuf().put(in);
        }

        public void reset(){
            decoder.reset();
        }

        public IoBuffer getBuf() {
            return buf;
        }

        public void setBuf(IoBuffer buf) {
            this.buf = buf;
        }

        public CharsetDecoder getDecoder() {
            return decoder;
        }
    }
}
