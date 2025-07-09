package com.robot.nettywss.proto;

import com.robot.entity.ResEntity;

import io.netty.channel.Channel;

public interface IWssNettyTaskInte<REQ, RSP> {
    ResEntity<RSP> sendMsg(Channel channel);

    public REQ getReq();
}
