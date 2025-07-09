package com.robot.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PWelcomeItemEntity implements Serializable {
    public long id;
    public String title;
    public long operator;
    public long createts;
    public long updatets;
    public List<PItemEntity> data;

    public static final class PItemEntity implements  Serializable{
        public String content;
        public int contentType;
        //Linked
        public String desc;
        public String imgUrl;
        public String linkUrl;
        public String linkTitle;
    }

    public static final List<PWelcomeItemEntity> convert2List(List<WelcomMsgDataInfoEntity> sList){
        List<PWelcomeItemEntity> rList = new ArrayList<>();
        if(sList != null && sList.size() > 0){
            for(WelcomMsgDataInfoEntity infoEntity : sList){
                if(infoEntity != null){
                    PWelcomeItemEntity rEntity = new PWelcomeItemEntity();
                    rEntity.id = infoEntity.id;
                    rEntity.operator = infoEntity.operator;
                    rEntity.createts = infoEntity.createts;
                    rEntity.updatets = infoEntity.updatets;
                    rEntity.title = infoEntity.title;
                    rEntity.data = new ArrayList<>();
                    StringBuilder builder = new StringBuilder();
                    if(infoEntity.data != null && infoEntity.data.items != null && infoEntity.data.items.length > 0){
                        for(WelcomMsgDataInfoEntity.WelcomeMsgDataV2 dataV2 : infoEntity.data.items){
                            PItemEntity pItemEntity = new PItemEntity();
                            if(dataV2.contentType == WelcomeMediaTypeEnum.TYPE_IMG.getType()){
                                pItemEntity.content = dataV2.itemEntity.imgMsgEntity.url;
                            }else if(dataV2.contentType == WelcomeMediaTypeEnum.TYPE_TEXT.getType()){
                                builder = new StringBuilder();
                                for(MessageItemEntity.ItemEntity itemEntity : dataV2.itemEntity.messages){
                                    builder.append(itemEntity.data);
                                }
                                pItemEntity.content = builder.toString();
                            }else if(dataV2.contentType == WelcomeMediaTypeEnum.TYPE_LINK.getType()){
                                MessageItemEntity itemEntity = dataV2.itemEntity;
                                WelcomeLinkeMsgEntity linkMsgEntity = itemEntity.linkMsgEntity;
                                pItemEntity.linkTitle = linkMsgEntity.title;
                                pItemEntity.desc = linkMsgEntity.desc;
                                pItemEntity.imgUrl = linkMsgEntity.imgUrl;
                                pItemEntity.linkUrl = linkMsgEntity.linkUrl;
                            }
                            pItemEntity.contentType = dataV2.contentType;
                            rEntity.data.add(pItemEntity);
                        }
                    }
                    if(builder != null && builder.length() > 0){
                        rEntity.title = builder.toString();
                    }
                    rList.add(rEntity);
                }
            }
        }
        return rList;
    }
}
