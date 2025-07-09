package com.robot.netty.proto;

import com.robot.entity.ResEntity;
import com.robot.netty.entity.TransmitData;
import com.robot.util.MyLog;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

public class SendDeviceImple implements INettyTaskInte<TransmitData,TransmitData>{
    private final String TAG = getClass().getSimpleName();
    private TransmitData req;

    public SendDeviceImple(TransmitData req){
        this.req = req;
    }


    @Override
    public ResEntity<TransmitData> sendMsg(Channel channel) {
        ChannelFuture channelFuture = channel.writeAndFlush(req);
        channelFuture.addListener(future -> MyLog.debug(TAG,"[sendDeviceInfo]" + " operationComplete succ... req:" + req));
        MyLog.debug(TAG,"[sendDeviceInfo]" + " channelFuture:" + channelFuture);
        return ResEntity.genSucc(req);
    }

    @Override
    public TransmitData getReq() {
        return this.req;
    }
}
