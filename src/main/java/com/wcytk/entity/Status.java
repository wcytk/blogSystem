package com.wcytk.entity;

public class Status {
    private int code;

    private String msg;

    public void setCode(boolean code) {
        if(code) {
            this.code = 0;
        } else {
            this.code = 1;
        }
    }

    public void setMsg(String msg, String success, boolean code) {
        if(code){
            this.msg = success;
        } else {
            this.msg = msg;
        }
    }

    public int getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }
}
