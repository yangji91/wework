package com.robot.netty;

import com.robot.netty.entity.TransmitData;
import com.robot.netty.listener.IConnectListener;
import com.robot.util.MyLog;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

public class ClientHandler_test extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private final String TAG = getClass().getSimpleName();

    private IConnectListener mConnectListener;

    public ClientHandler_test(IConnectListener mListener) {
        this.mConnectListener = mListener;
    }

    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        MyLog.debug(TAG, "[channelActive]" + "收到服务返回数据:" + msg);
        if (mConnectListener != null) {
            TransmitData transmitData = new TransmitData();
            // transmitData.setCode();
            // transmitData.setType();
            // transmitData.setContent();
            mConnectListener.onRecv(transmitData);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        MyLog.debug(TAG, "[channelActive]" + "与服务端链接成功...");
        if (mConnectListener != null) {
            mConnectListener.onConnectSucc();
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        MyLog.debug(TAG, "[channelActive]" + "与服务端断开...");
        if (mConnectListener != null) {
            mConnectListener.connectError("断开链接");
        }
    }


    protected void messageReceived(ChannelHandlerContext channelHandlerContext, TransmitData transmitData) throws Exception {

        MyLog.debug(TAG, "[channelActive]" + "messageReceived收到服务 数据:" + transmitData);
        if (mConnectListener != null) {
            mConnectListener.onRecv(transmitData);
        }
    }
}
