package com.cmp.aliadapter.common;


import static com.cmp.aliadapter.common.ErrorEnum.ERR_DEFAULT_CODE;

public class AliException extends RuntimeException {

    private ErrorEnum errorEnum;

    public AliException(ErrorEnum errorEnum) {
        super(errorEnum.getDesc());
        this.errorEnum = errorEnum;
    }

    public static AliException failure() {
        return new AliException(ERR_DEFAULT_CODE);
    }

    public ErrorEnum getErrorEnum() {
        return errorEnum;
    }

    public void setErrorEnum(ErrorEnum errorEnum) {
        this.errorEnum = errorEnum;
    }
}
