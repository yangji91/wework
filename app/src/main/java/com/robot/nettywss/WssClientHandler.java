package com.robot.nettywss;

import com.robot.nettywss.listener.IWssConnectListener;
import com.robot.util.MyLog;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

public class WssClientHandler extends SimpleChannelInboundHandler<WebSocketFrame> {
    private final String TAG = getClass().getSimpleName();

    private WebSocketClientHandshaker mHandshaker;
    private IWssConnectListener mConnectListener;

    public WssClientHandler(WebSocketClientHandshaker handshaker, IWssConnectListener mListener) {
        this.mHandshaker = handshaker;
        this.mConnectListener = mListener;
    }

    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame msg) throws Exception {
        MyLog.debug(TAG, "[channelActive]" + "收到服务返回数据:" + msg);
        if (mConnectListener != null) {
            mConnectListener.onRecv(msg);
        }
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        ChannelPromise newPromise = ctx.newPromise();
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
}
