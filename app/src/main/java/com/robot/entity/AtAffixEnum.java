package com.robot.entity;

public enum  AtAffixEnum {
    PREFIX(1,"前缀"),
    TAIL(2,"后缀");

    private int type;
    private String desc;

    AtAffixEnum(int type,String desc){
        this.type = type;
        this.desc = desc;
    }

    public int getType(){
        return this.type;
    }

    public String getDesc(){
        return this.desc;
    }

    public static final AtAffixEnum getAtAffixEnumByType(int type){
        AtAffixEnum result = null;
        for(AtAffixEnum en : AtAffixEnum.values()){
            if(en != null && en.getType() == type){
                result = en;
                break;
            }
        }
        return result;
    }
}
