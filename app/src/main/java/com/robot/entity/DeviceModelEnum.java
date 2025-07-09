package com.robot.entity;


import java.io.Serializable;

public enum  DeviceModelEnum implements Serializable {
    //0: 未知 1: 小米 2: 华为 3: vivo 4: oppo 5: 魅族
    UNKNOW(0,"未知"),
    XIAOMI(1,"xiaomi,redmi"),
    HUAWEI(2,"huawei"),
    VIVO(3,"vivo"),
    OPPO(4,"oppo"),
    MEIZU(5,"meizu");

    public int type;
    public String model;

    /**
         public static final int UITYPE_UNKNOW = 0;
         public static final int UITYPE_XIAOMI = 1;
         public static final int UITYPE_HUAWEI = 2;
         public static final int UITYPE_VIVO = 3;
         public static final int UITYPE_OPPO = 4;
         public static final int UITYPE_MEIZU = 5;
     * @param type
     * @param model
     */

    DeviceModelEnum(int type,String model){
        this.type = type;
        this.model = model;
    }

    public int getType(){
        return this.type;
    }

    public String getModel(){
        return this.model;
    }

    public static DeviceModelEnum getDeviceModelByName(String name){
        DeviceModelEnum rDeviceModelEnum = DeviceModelEnum.UNKNOW;
        for(DeviceModelEnum en : DeviceModelEnum.values()){
            if(en != null && en.getModel().contains(name.toLowerCase())){
                rDeviceModelEnum = en;
                break;
            }
        }
        return rDeviceModelEnum;
    }
}
