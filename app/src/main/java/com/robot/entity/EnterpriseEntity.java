package com.robot.entity;

import java.io.Serializable;

public class  EnterpriseEntity implements Serializable{

    //result
    public long bbsId;     //企业Id
    public String bbsName;   //企业名称
    public String name;     //
    public long corpId;


    public String toString(){
        return "bbsId:" + this.bbsId + " bbsName:" + bbsName + " name:" + name + " corpId:" + this.corpId;
    }
}
