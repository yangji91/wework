package com.robot.netty.proto;

import com.robot.entity.ResEntity;
import io.netty.channel.Channel;

public interface INettyTaskInte<REQ,RSP>{
    ResEntity<RSP> sendMsg(Channel channel);

    public REQ getReq();
}
