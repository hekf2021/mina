package com.protocal;

public class ProtocalPack {
    private int length;
    private byte flag;
    private String content;

    public ProtocalPack(byte flag, String content) {
        this.flag = flag;
        this.content = content;
        int len = content==null?0:content.getBytes().length;
        this.length=4+1+len;
    }


    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public byte getFlag() {
        return flag;
    }

    public void setFlag(byte flag) {
        this.flag = flag;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("length:").append(length);
        sb.append("flag:").append(flag);
        sb.append("content:").append(content);
        return sb.toString();
    }
}
