package com.robot.exception;

import com.robot.entity.ActionResultEnum;

/***
 *@author 
 *@date 2021/6/21
 *@description 自定义异常信息
 * 错误信息通过抛出异常的方式 提交上层
 ****/
public class MessageException extends Exception {

    private ActionResultEnum actionResultEnum;
   public   MessageException(ActionResultEnum actionResultEnum){
        super(actionResultEnum.getMsg());
        this.actionResultEnum=actionResultEnum;
    }
    public   MessageException(String message){
        super(message);
    }
    public ActionResultEnum getActionResultEnum() {
        return actionResultEnum;
    }

    @Override
    public String getMessage() {
       if (actionResultEnum!=null){
           return actionResultEnum.getMsg();
       }
        return super.getMessage();
    }
}
