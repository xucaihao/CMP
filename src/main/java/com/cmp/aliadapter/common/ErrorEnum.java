package com.cmp.aliadapter.common;

public enum ErrorEnum {

    //##########公共错误code(0——50)##########
    ERR_CLOUD_INFO_NOT_FOUND(1, "cloudmp.ali.common.cloudInfoNotFoundError", "请求头中找不到云信息"),
    ERR_AUTH_INFO(2, "cloudmp.ali.common.findAuthInfoError", "查询authInfo失败"),

    ERR_DEFAULT_CODE(0, "cloudmp.core.cloud.unknownError", "未知错误");


    private int code;

    private String message;

    private String desc;

    ErrorEnum(int code, String message, String desc) {
        this.code = code;
        this.message = message;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDesc() {
        return desc;
    }

    @Override
    public String toString() {
        return "code=" + code +
                ", message='" + message + '\'' +
                ", desc='" + desc;
    }
}
