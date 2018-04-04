package com.cmp.aliadapter.instance.service;

import com.cmp.aliadapter.common.ExceptionUtil;
import com.cmp.aliadapter.common.RestException;

public class Main {

    public static void main(String[] args) {
        try {
            throw new RestException("aa", 400);
        } catch (Exception e) {
            ExceptionUtil.dealException(e, 10);
        }
    }
}
