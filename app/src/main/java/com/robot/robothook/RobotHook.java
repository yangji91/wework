package com.robot.robothook;

import java.lang.reflect.Member;

public interface RobotHook {
    public void hookMethod(Member m, RobotMethodHook callback);

    public void hookAllConstructors(Class<?> hookClass, RobotMethodHook callback) ;
}
