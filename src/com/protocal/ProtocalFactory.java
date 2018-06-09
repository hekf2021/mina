package com.protocal;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import com.protocal.ProtocalDecoder;
import com.protocal.ProtocalEncoder;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

import java.nio.charset.Charset;

public class ProtocalFactory implements ProtocolCodecFactory {
    private final ProtocalDecoder decoder;
    private final ProtocalEncoder encoder;

    public ProtocalFactory(Charset charset) {
        this.encoder = new ProtocalEncoder(charset);
        this.decoder = new ProtocalDecoder(charset);
    }

    public ProtocolEncoder getEncoder(IoSession ioSession) throws Exception {
        return encoder;
    }

    public ProtocolDecoder getDecoder(IoSession ioSession) throws Exception {
        return decoder;
    }
}
