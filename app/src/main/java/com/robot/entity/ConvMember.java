package com.robot.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.robot.util.StrUtils;

import java.io.Serializable;

public class ConvMember implements Serializable {
    public String avatorUrl;
   // private int banType;
   // private long changeReceiptStatusTimestamp;
    public String englishName;
    public boolean invited;
    public long joinTime;
    private long kfVid;
    public String name;
    public String nickName;
    //private long operatorRemoteId;

    //private byte[] searchExtra;
    public long userCorpId;
    public long userRemoteId;
    public String corpName;
    public int gender;

    public boolean equals(ConvMember other) {
       /* if (this.userRemoteId != other.userRemoteId)
            return false;
        if (this.joinTime != other.joinTime)
            return false;
        if (this.userCorpId != other.userCorpId)
            return false;
        if (this.invited != other.invited)
            return false;*/
        return  this.userRemoteId != other.userRemoteId;
      //  return this.changeReceiptStatusTimestamp == other.changeReceiptStatusTimestamp;
    }

    @Override
    public String toString() {
        ValueFilter profilter = new ValueFilter(){
            @Override
            public Object process(Object object, String name, Object value) {
                if (value instanceof byte[]){
                    return  StrUtils.byteToUTFStr((byte[]) value);
                }
                return value;
            }
        };
        return JSON.toJSONString(this ,profilter );
    }
}
