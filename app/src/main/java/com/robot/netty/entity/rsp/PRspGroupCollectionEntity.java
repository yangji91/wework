package com.robot.netty.entity.rsp;

import java.io.Serializable;
import java.util.List;

public class PRspGroupCollectionEntity implements Serializable {
    public List<PGroupCollectionItem> allActions;

    public static final class PGroupCollectionItem extends BaseActionItem implements Serializable{
        public int type;
        public PRspActionTextMsgEntity.PMatcherItemEntity chatMatch;
    }
}
