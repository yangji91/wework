package com.robot.nettywss.proto;

import com.robot.entity.ResEntity;
import com.robot.util.MyLog;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

/***
 * 发送心跳协议
 */
public class WssSendHeartBeatImpl implements IWssNettyTaskInte<WebSocketFrame, Object> {
    private final String TAG = getClass().getSimpleName();
    private WebSocketFrame req;

    public WssSendHeartBeatImpl(WebSocketFrame req) {
        this.req = req;
    }

    @Override
    public ResEntity<Object> sendMsg(Channel channel) {
        ChannelFuture channelFuture = channel.writeAndFlush(req);
        channelFuture.addListener(future -> MyLog.debug(TAG, "[operationComplete]" + " succ..."));
        MyLog.debug(TAG, "[sendMsg]" + " channelFuture:" + channelFuture);
        ResEntity resEntity = ResEntity.genSucc(channel);
        MyLog.debug(TAG, "[resEntity.getType()]" + resEntity.getType() + "   " + resEntity.getMsg());
        return resEntity;
    }

    @Override
    public WebSocketFrame getReq() {
        return this.req;
    }
}
