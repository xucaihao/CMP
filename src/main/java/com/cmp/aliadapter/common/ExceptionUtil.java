package com.cmp.aliadapter.common;

import com.aliyuncs.exceptions.ClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExceptionUtil {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionUtil.class);

    public static void dealException(Exception e) {
        if (e instanceof ClientException) {
            throw new RestException(e.getMessage(), Integer.valueOf(((ClientException) e).getErrCode()));
        } else {
            throw (RuntimeException) e;
        }
    }

    public static void dealException(Exception e, int pageNum) {
        final StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        String method = trace[2].getClassName() + "::" + trace[2].getMethodName();
        logger.error("{} error, occur in :{}", method, trace[2].getFileName()
                + " lineNum: " + trace[2].getLineNumber());
        logger.error("{} error: {}; occurred in pageNum: {}", method, e.getMessage(), pageNum);
    }
}
