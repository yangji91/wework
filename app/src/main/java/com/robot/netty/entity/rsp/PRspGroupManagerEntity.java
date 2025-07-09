package com.robot.netty.entity.rsp;

import java.io.Serializable;
import java.util.List;

/**
 * 群管理动作下发
 */
public class PRspGroupManagerEntity implements Serializable{
    public List<ActionItem> allActions;

    public static class ActionItem extends BaseActionItem implements Serializable{
        public String rename;
        public String notice;
        public Integer forbidRename;
        public int saveToContacts;
        public int invitationApproval;
        public String alias;
        public int initGroup;
        public int initGroupType;
        public int type;
        public int retreat;
        public long welcomeMsgId;
        public List<Long> spamRuleIds;
        public String note;
        public boolean setupEnterGroupVerification;
        public PRspActionTextMsgEntity.PMatcherItemEntity chatMatch;
        public List<PRspActionTextMsgEntity.PMatcherItemEntity> matchers;
        public String linkUrl;//邀请进群链接
        //添加群成员为好友操作：
        //当下发群成员id 只有name时，移动端过滤掉企业人员及好友后，name仍然存在冲突，则根据该冲突做处理
        /*1：全部添加
         2：全部拒绝
        3：默认取第一个群成员*/
        public  int conflictStrategy;

        public long outerBbsId;//企业id
        public String outerBbsName;//企业名称
    }

    /***
     *     public String uid;  //任务唯一id
     *     public String description;  //任务描述
     *     public Integer broadCast;
     *     public Long actionType;
     *     public Long executorUin;
     *     public Integer callback;
     */
}
