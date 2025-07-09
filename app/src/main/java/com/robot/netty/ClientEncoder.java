package com.robot.netty;

import com.robot.netty.entity.TransmitData;
import com.robot.netty.util.CompressUtils;

import java.io.UnsupportedEncodingException;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class ClientEncoder extends MessageToByteEncoder<TransmitData> {

    // public static final byte[] msgEnd = new byte[] { (byte) 0x0A, (byte) 0x0D };

    public static final int COMPRESS_MAX = 2000;


    @Override
    protected void encode(ChannelHandlerContext ctx, TransmitData transmitData, ByteBuf byteBuf){
        int dataLength = 0;
        try {
            dataLength = transmitData.getContent() == null ? 0 : transmitData.getContent().getBytes("UTF-8").length;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        if (dataLength > COMPRESS_MAX) {
//            transmitData.setCompress(true);
            try {
                transmitData.setContent(CompressUtils.gzipCompress(transmitData.getContent().getBytes("UTF-8")));
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
            try {
                dataLength = transmitData.getContent().getBytes("UTF-8").length;
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }else {
//            transmitData.setCompress(false);
        }
        byteBuf.ensureWritable(4 + dataLength);
        byteBuf.writeInt(dataLength);
        byteBuf.writeShort(transmitData.getCode());
//        byteBuf.writeBoolean(transmitData.isCompress());
//        byteBuf.writeByte(transmitData.getVersion());

        if (dataLength > 0) {
            try {
                byteBuf.writeBytes(transmitData.getContent().getBytes("UTF-8"));
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
