package com.robot.robothook;


import java.lang.reflect.Member;

public class RobotMethodParam {
    public Object methodParam;
    public Object thisObject;
    public Object[] args;
    public Member method;

    private RobotMethodParam(Object methodParam) {
        this.methodParam = methodParam;
        thisObject = RobotHelpers.getObjectField(methodParam, "thisObject");
        args = (Object[]) RobotHelpers.getObjectField(methodParam, "args");
        method = (Member) RobotHelpers.getObjectField(methodParam, "method");
    }

    public RobotMethodParam(Object methodParam, Object[] args, Object thisObject, Member method) {
        this.methodParam = methodParam;

        this.thisObject = thisObject;
        this.args = args;
        this.method = method;


    }

    public Object getResult() {
        return RobotHelpers.callMethod(methodParam, "getResult");

    }

    public void setResult(Object o) {
        RobotHelpers.callMethod(methodParam, "setResult", o);
    }

    public void setArgs(Object[] args) {
        RobotHelpers.setObjectField(methodParam, "args", args);
    }

    public void setThrowable(Exception e) {
        RobotHelpers.callMethod(methodParam, "setThrowable", e);
    }
}
