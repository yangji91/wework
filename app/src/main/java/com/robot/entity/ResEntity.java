package com.robot.entity;

import com.google.gson.Gson;

public class ResEntity<T>{
    private T result;
    private boolean success;
    private String message;
    private int code;

    public T getData(){
        return this.result;
    }

    public void setData(T t){
        this.result = t;
    }

    public boolean isSucc(){
        return this.success;
    }

    public void setSucc(boolean succ){
        this.success = succ;
    }

    public String getMsg(){
        return this.message;
    }

    public void setMsg(String msg){
        this.message = msg;
    }

    public int getType(){
        return this.code;
    }

    public void setType(int type){
        this.code = type;
    }


    public static<T> ResEntity genSucc(T t,int type){
        ResEntity<T> resEntity = new ResEntity<>();
        resEntity.setType(type);
        resEntity.setSucc(true);
        resEntity.setData(t);
        return resEntity;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        String str = gson.toJson(this);
        return "ResEntity  " + str;
    }

    public static<T> ResEntity genSucc(T t){
        return genSucc(t,0);
    }
    public static ResEntity genSucc(){
        return genSucc(null);
    }
    public static<T> ResEntity genErr(T t){
        return genErr(t,"");
    }

    public static<T> ResEntity genErr(T t,String msg){
        ResEntity<T> resEntity = new ResEntity<>();
        resEntity.setSucc(false);
        resEntity.setData(t);
        resEntity.setMsg(msg);
        return resEntity;
    }


}
