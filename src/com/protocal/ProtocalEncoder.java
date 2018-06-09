package com.protocal;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import java.nio.charset.Charset;

public class ProtocalEncoder extends ProtocolEncoderAdapter {

    private final Charset charset;

    public ProtocalEncoder(Charset charset){
        this.charset=charset;
    }

    public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
        ProtocalPack value = (ProtocalPack) message;
        IoBuffer buf = IoBuffer.allocate(value.getLength());
        buf.setAutoExpand(true);
        buf.putInt(value.getLength());
        buf.put(value.getFlag());
        if(value.getContent()!=null){
            buf.put(value.getContent().getBytes());
        }
        buf.flip();
        out.write(buf);

    }
}
