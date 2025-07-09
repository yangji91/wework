package com.robot.util;

import com.robot.common.Global;
import com.robot.entity.QueryCRMAntiSpamRuleRspEntity;
import com.robot.hook.KeyConst;

import java.util.ArrayList;
import java.util.List;

import com.robot.robothook.RobotHelpers;

public class CRSpamRuleParseUtil {

    public static final QueryCRMAntiSpamRuleRspEntity parseInfo(byte[] bs)  {
        QueryCRMAntiSpamRuleRspEntity rEntity = null;
        Class clazzRsp = RobotHelpers.findClassIfExists(KeyConst.C_QueryCRMAntiSpamRuleRsp, Global.loadPackageParam.classLoader);
        Class[] clazzArray = {byte[].class};
        Object[] objArray = {bs};
        Object objQueryCRMAntiSpamRuleRsp = RobotHelpers.callStaticMethod(clazzRsp, KeyConst.M_QueryCRMAntiSpamRuleRsp_parseFrom, clazzArray, objArray);
        if (objQueryCRMAntiSpamRuleRsp != null) {
            rEntity = new QueryCRMAntiSpamRuleRspEntity();
            boolean isEnd = RobotHelpers.getBooleanField(objQueryCRMAntiSpamRuleRsp, KeyConst.F_QueryCRMAntiSpamRuleRsp_parseFrom_isEnd);
            int lastTime = RobotHelpers.getIntField(objQueryCRMAntiSpamRuleRsp, KeyConst.F_QueryCRMAntiSpamRuleRsp_parseFrom_lastTime);
            Object ruleList = RobotHelpers.getObjectField(objQueryCRMAntiSpamRuleRsp, KeyConst.F_QueryCRMAntiSpamRuleRsp_parseFrom_list);
            rEntity.isEnd = isEnd;
            rEntity.lastTime = lastTime;
            List<QueryCRMAntiSpamRuleRspEntity.CRMAntiSpamRule> rList = new ArrayList<>();
            if (ruleList != null) {
                Object[] items = (Object[]) RobotHelpers.getObjectField(ruleList, "items");
                if (items != null && items.length > 0) {
                    for (Object item : items) {
                        QueryCRMAntiSpamRuleRspEntity.CRMAntiSpamRule ruleItem = new QueryCRMAntiSpamRuleRspEntity.CRMAntiSpamRule();
                        ruleItem.createTs = RobotHelpers.getIntField(item, "createTs");
                        ruleItem.creator = RobotHelpers.getLongField(item, "creator");
                        ruleItem.id = RobotHelpers.getLongField(item, "id");
                        ruleItem.type = RobotHelpers.getIntField(item, "type");
                        ruleItem.updateTs = RobotHelpers.getIntField(item, "updateTs");
                        byte[] bsName = (byte[]) RobotHelpers.getObjectField(item, "name");
                        ruleItem.name = StrUtils.byteToUTFStr(bsName);

                        try {
                            //skfly add begin
                            Object tempObj = RobotHelpers.getObjectField(item, "action");
                            if (null == tempObj) {
                                ruleItem.action = null;
                            } else {
                                ruleItem.action.addBlackList = RobotHelpers.getBooleanField(tempObj, "addBlackList");
                                ruleItem.action.type = RobotHelpers.getIntField(tempObj, "type");
                                ruleItem.action.words = StrUtils.byteToUTFStr((byte[]) RobotHelpers.getObjectField(tempObj, "words"));
                            }

                            tempObj = RobotHelpers.getObjectField(item, "msgFre");
                            if (null == tempObj) {
                                ruleItem.msgFre = null;
                            } else {
                                ruleItem.msgFre.frequency = RobotHelpers.getIntField(tempObj, "frequency");
                                ruleItem.msgFre.isOpen = RobotHelpers.getBooleanField(tempObj, "isOpen");
                                ruleItem.msgFre.sort = RobotHelpers.getIntField(tempObj, "sort");
                                ruleItem.msgFre.timeInterval = RobotHelpers.getIntField(tempObj, "timeInterval");
                            }

                            tempObj = RobotHelpers.getObjectField(item, "msgKeyword");
                            if (tempObj == null) {
                                ruleItem.msgKeyword = null;
                            } else {
                                ruleItem.msgKeyword.isOpen = RobotHelpers.getBooleanField(tempObj, "isOpen");
                                byte[][] tempByteArr = (byte[][]) RobotHelpers.getObjectField(tempObj, "items");
                                for (int i = 0;
                                     i < tempByteArr.length;
                                     i++) {
                                    ruleItem.msgKeyword.items.add(StrUtils.byteToUTFStr(tempByteArr[i]));
                                }
                                ruleItem.msgKeyword.sort = RobotHelpers.getIntField(tempObj, "sort");
                            }

                            tempObj = RobotHelpers.getObjectField(item, "nicknameKeyword");
                            if (null == tempObj) {
                                ruleItem.nicknameKeyword = null;
                            } else {
                                ruleItem.nicknameKeyword.isOpen = RobotHelpers.getBooleanField(tempObj, "isOpen");
                                byte[][] tempByteArr = (byte[][]) RobotHelpers.getObjectField(tempObj, "items");
                                for (int i = 0;
                                     i < tempByteArr.length;
                                     i++) {
                                    ruleItem.nicknameKeyword.items.add(StrUtils.byteToUTFStr(tempByteArr[i]));
                                }
                                ruleItem.nicknameKeyword.sort = RobotHelpers.getIntField(tempObj, "sort");
                            }


                            tempObj = RobotHelpers.getObjectField(item, "msgLen");
                            if (null == tempObj) {
                                ruleItem.msgLen = null;
                            } else {
                                ruleItem.msgLen.isOpen = RobotHelpers.getBooleanField(tempObj, "isOpen");
                                ruleItem.msgLen.len = RobotHelpers.getIntField(tempObj, "len");
                                ruleItem.msgLen.line = RobotHelpers.getIntField(tempObj, "line");
                                ruleItem.msgLen.sort = RobotHelpers.getIntField(tempObj, "sort");
                            }

                            tempObj = RobotHelpers.getObjectField(item, "msgType");
                            if (null == tempObj) {
                                ruleItem.msgType = null;
                            } else {
                                Object[] tempObjArr = (Object[]) RobotHelpers.getObjectField(tempObj, "msgtype");
                                for (int i = 0;
                                     i < tempObjArr.length;
                                     i++) {
                                    QueryCRMAntiSpamRuleRspEntity.CRMAntiSpamDetailMsgType.CRMSpamMsgType tempMT = new QueryCRMAntiSpamRuleRspEntity.CRMAntiSpamDetailMsgType.CRMSpamMsgType();
                                    tempMT.isOpen = RobotHelpers.getBooleanField(tempObjArr[i], "isOpen");
                                    tempMT.msgtype = RobotHelpers.getIntField(tempObjArr[i], "msgtype");
                                    tempMT.sort = RobotHelpers.getIntField(tempObjArr[i], "sort");
                                    byte[][] tempByteArr = (byte[][]) RobotHelpers.getObjectField(tempObjArr[i], "whitelist");
                                    for (int j = 0;
                                         j < tempByteArr.length;
                                         j++) {
                                        tempMT.whitelist.add(StrUtils.byteToUTFStr(tempByteArr[j]));
                                    }
                                    ruleItem.msgType.msgtype.add(tempMT);
                                }
                            }
                            //skfly add end
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        rList.add(ruleItem);
                    }
                }
            }
            rEntity.list = rList;
        }
        return rEntity;
    }
}
