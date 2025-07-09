package com.robot.entity;

import com.robot.netty.entity.rsp.PRspActionTextMsgEntity;
import java.io.Serializable;

public class BSendMsgEntity implements Serializable {
    public long converId;
    public String content;
    public Long msgId;
    public PRspActionTextMsgEntity.PMsgAtDetailEntity atItemEntity;

}
