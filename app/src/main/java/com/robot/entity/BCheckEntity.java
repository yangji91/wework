package com.robot.entity;

import com.robot.common.StatsHelper;
import com.robot.common.Global;
import com.robot.robothook.utils.ArrayUtils;

import java.io.Serializable;

public class BCheckEntity implements Serializable {
    public long verCode;
    public String verName;
    public long fileLen;

    public int andrVer;     //android verion版本 23 24

    public long robotVerCode;
    public String robotVerName;
    public long robotFileLen;

    public BResultEntity isOk(String[] adapterVersions) {
        BResultEntity result = new BResultEntity();
        try {
            if (!verName.isEmpty() && verCode != 0) {
                if (ArrayUtils.contains(adapterVersions, (verName + "." + verCode))) {
                    result.isOk = true;
                    return result;
                }
            }
            result.tips = "企业微信版本号不对应，当前版本号为:" + verCode + " 版本名:" + verName;
            result.isOk = false;
            result.desc = verName;
            return result;
        } catch (Throwable e) {
            Global.showToast("配置环境可能存在风险-未安装企业微信或隐藏列表机器人未额外可见企微");
            StatsHelper.event("msgReport", "detector", false, "未安装企业微信或隐藏列表机器人未额外可见企微");
        }
        result.tips = "未安装企业微信或隐藏列表机器人未额外可见企微";
        result.isOk = false;
        result.desc = verName;
        return result;
    }
}
