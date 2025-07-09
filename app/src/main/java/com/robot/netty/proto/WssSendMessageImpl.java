package com.robot.netty.proto;

import com.robot.entity.ResEntity;
import com.robot.nettywss.proto.IWssNettyTaskInte;
import com.robot.util.MyLog;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

public class WssSendMessageImpl implements IWssNettyTaskInte<WebSocketFrame,Object> {
    private final String TAG = getClass().getSimpleName();
    private WebSocketFrame req;

    public WssSendMessageImpl(WebSocketFrame req){
        this.req = req;
    }

    @Override
    public ResEntity<Object> sendMsg(Channel channel) {
        ChannelFuture channelFuture = channel.writeAndFlush(req);
        channelFuture.addListener(future -> MyLog.debug(TAG,"[SendCommonImple]" + " operationComplete succ... req:" + req));
        MyLog.debug(TAG,"[SendCommonImple]" + " succ...");
        return ResEntity.genSucc(req);
    }

    @Override
    public WebSocketFrame getReq() {
        return this.req;
    }
}
