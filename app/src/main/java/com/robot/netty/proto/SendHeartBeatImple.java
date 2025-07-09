package com.robot.netty.proto;

import com.robot.entity.ResEntity;
import com.robot.netty.entity.TransmitData;
import com.robot.util.MyLog;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

/***
 * 发送心跳协议
 */
public class SendHeartBeatImple implements INettyTaskInte<TransmitData, Object> {
    private final String TAG = getClass().getSimpleName();
    private TransmitData req;

    public SendHeartBeatImple(TransmitData req) {
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
    public TransmitData getReq() {
        req.toString();
        return this.req;
    }
}
