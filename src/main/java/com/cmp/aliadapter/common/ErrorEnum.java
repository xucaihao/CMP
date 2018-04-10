package com.cmp.aliadapter.common;

public enum ErrorEnum {

    //##########公共错误code(0——50)##########
    ERR_CLOUD_INFO_NOT_FOUND(1, "cloudmp.ali.common.cloudInfoNotFoundError", "请求头中找不到云信息"),
    ERR_AUTH_INFO(2, "cloudmp.ali.common.findAuthInfoError", "查询authInfo失败"),

    ERR_DEFAULT_CODE(0, "cloudmp.ali.cloud.unknownError", "未知错误");


    private int errorCode;

    private String message;

    private String desc;

    ErrorEnum(int errorCode, String message, String desc) {
        this.errorCode = errorCode;
        this.message = message;
        this.desc = desc;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }

    public String getDesc() {
        return desc;
    }

    @Override
    public String toString() {
        return "{\n" +
                "\"errorCode\" : \"" + errorCode + "\",\n" +
                "\"msg\" : \"" + message + "\",\n" +
                "\"des\" : \"" + desc + "\"\n" +
                "}";
    }
}
