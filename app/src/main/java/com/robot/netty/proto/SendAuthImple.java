package com.robot.netty.proto;

import com.robot.entity.ResEntity;
import com.robot.netty.entity.TransmitData;
import com.robot.netty.listener.ISendListener;
import com.robot.util.MyLog;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

public class SendAuthImple implements INettyTaskInte<TransmitData,TransmitData>{
    private final String TAG = getClass().getSimpleName();
    private TransmitData req;
    private ISendListener mListener;

    public SendAuthImple(TransmitData req,ISendListener mListener){
        this.req = req;
        this.mListener = mListener;
    }

    @Override
    public ResEntity<TransmitData> sendMsg(Channel channel) {
        ChannelFuture channelFuture = channel.writeAndFlush(req);
        channelFuture.addListener(future -> {
            if(mListener != null){
                mListener.onSucc();
            }
            MyLog.debug(TAG,"[SendAuthImple]" + " operationComplete succ... req:" + req);
        });
        return ResEntity.genSucc(this.req);
    }

    @Override
    public TransmitData getReq() {
        return req;
    }
}
