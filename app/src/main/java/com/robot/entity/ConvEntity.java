package com.robot.entity;

import com.tencent.smtt.utils.Md5Utils;
import com.robot.controller.LoginController;
import com.robot.util.MyLog;

import java.io.Serializable;
import java.util.List;

public class ConvEntity implements Serializable {
    public boolean autoMarkRead;
    public long createTime;
    public long creatorId;
    public boolean exited;
    //    public Extras extras;
    public byte[] forwardLeaveMsg;
    public boolean hidden;
    public long id;
    public boolean isStickied;
    //    public Message lastMessage;
    public ConvMember[] memberList;
    public int memberCount;
    public long modifyTime;
    //    public MsgSects msgSects;
    public String name;
    public boolean notified;
    public long remoteId;
    public long searchTime;
    public int type;
    public boolean updateMember;
    public boolean isExternalGroup;
    public boolean enableExteralAdmin;//是否是管理员
    public boolean isGroupOwner;    //是否是群主
    // public Object objConv;
    public ConvExtras extras;

    public static class ConvRoomAdmin {
        public long vid;

        @Override
        public String toString() {
            return "ConvRoomAdmin{" +
                    "vid=" + vid +
                    '}';
        }
    }

    public static class ConvExtras {
        public List<ConvRoomAdmin> admins;

        public boolean isAllowRename = true;

        @Override
        public String toString() {
            return "ConvExtras{" +
                    "admins=" + admins +
                    '}';
        }
    }

    public ConvMember getMemberbyRemoteId(long remoteId) {
       if (memberList!=null)
        for (ConvMember member : memberList) {
            if (remoteId == member.userRemoteId)
                return member;
        }
        return null;
    }

    public boolean equals(ConvEntity other) {
        if (this.remoteId != other.remoteId)
            return false;
        if (this.modifyTime != other.modifyTime)
            return false;
        if (this.name != null && !this.name.equals(other.name))
            return false;
        if (this.createTime != other.createTime)
            return false;
        if (this.creatorId != other.creatorId)
            return false;
        if (this.id != other.id)
            return false;
        if (this.memberCount != other.memberCount)
            return false;
        for (ConvMember member : memberList) {
            ConvMember m = other.getMemberbyRemoteId(member.userRemoteId);
            if (m == null)
                return false;
            if (!m.equals(member))
                return false;
        }
        for (ConvMember member : other.memberList) {
            ConvMember m = this.getMemberbyRemoteId(member.userRemoteId);
            if (m == null)
                return false;
            if (!m.equals(member))
                return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        // builder.append("obj:" + objConv + "");
        builder.append(" autoMarkRead:")
                .append(autoMarkRead).append(" createTime:").append(createTime).append(" creatorId:").append(creatorId)
                .append(" exited:").append(exited)
                .append(" hidden:")  .append(hidden)
                .append(" id:").append(id)
                .append(" isStickied:").append(isStickied)
                .append(" createTime:").append(createTime)
                .append(" name:").append(name)
                .append(" notified:").append(notified)
                .append(" remoteId:").append(remoteId)
                .append(" type:").append(type)
                .append(" memberCount:").append(memberCount)
                .append(" updateMember:").append(updateMember)
                .append(" enableExteralAdmin:").append(enableExteralAdmin)
                .append(" isGroupOwner:").append(isGroupOwner);
        return builder.toString();
    }

    /**
     * 用来判断群信息是否发生了变化
     *
     * @return
     */
    public String getMD5() {
        StringBuilder builder = new StringBuilder();
        builder.append(name)
                .append(creatorId)
                .append(isGroupOwner)
                .append(enableExteralAdmin)
                .append(memberCount);
        if (memberList != null)
            for (ConvMember convMember : memberList
            ) {
                if (convMember!=null){
                    builder.append(convMember.userRemoteId);
                }else {
                    MyLog.debug("ConvEntity","name ="+name+" memberCount ="+memberCount);
                }

            }
        return Md5Utils.getMD5(builder.toString());

    }

    public String toStringSimple() {
        StringBuilder builder = new StringBuilder();
        builder.append("id:" + id + " remoteId:" + remoteId + " name:" + name + " type:" + type);
        return builder.toString();
    }

    public String memberList2String() {
        StringBuilder builder = new StringBuilder();
        if (memberList != null && memberList.length > 0) {
            for (ConvMember convMember : memberList) {
                builder.append(convMember + "   ||  ");
            }
        }
        return builder.toString();
    }

    /**
     * 是否是内部群
     *
     * @return
     */
    public boolean isInnerGroup() {
        if (type == 1 && !isExternalGroup) {
            return true;
        }
        return false;
    }

    /**
     * 是否是管理员
     *
     * @return
     */
    public boolean isAdmin() {
        boolean isAdmin = false;
        if (extras != null && extras.admins != null && extras.admins.size() > 0) {
            for (ConvRoomAdmin convRoomAdmin : extras.admins) {
                if (convRoomAdmin != null && convRoomAdmin.vid == LoginController.getInstance().getLoginUserId()) {
                    isAdmin = true;
                    break;
                }
            }
        }
        return isAdmin;
    }
}
