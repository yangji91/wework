package com.robot.nettywss;


import com.robot.util.MyLog;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

public class HeartbeatHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            if (e.state() == IdleState.WRITER_IDLE) {
                MyLog.debug("HeartbeatHandler", "Sending heartbeat ping");
                ctx.writeAndFlush(new PingWebSocketFrame()); // 发送 ping
//                WssProtocalManager.sendHeartBeat();

            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof PongWebSocketFrame) {
            MyLog.debug("HeartbeatHandler","Received pong");
            WssNettyEngine.getInstance().heartBeat();
            return;
        }
        super.channelRead(ctx, msg);
    }
}

