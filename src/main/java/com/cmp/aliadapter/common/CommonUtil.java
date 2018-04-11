package com.cmp.aliadapter.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

import static com.cmp.aliadapter.common.Constance.TIME_OUT_SECONDS;
import static java.util.stream.Collectors.toList;

public class CommonUtil {

    private static final Logger logger = LoggerFactory.getLogger(CommonUtil.class);

    /**
     * 聚合异步返回结果
     *
     * @param future future
     * @param <T>    泛型
     * @return 泛型
     */
    private static <T> T getRes(CompletionStage<T> future) {
        try {
            if (null != future) {
                return future.toCompletableFuture().get(TIME_OUT_SECONDS, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            logger.error("CommonUtil::getRes error..e :{}", e);
        }
        return null;
    }

    /**
     * 聚合多线程list
     *
     * @param futures 多线程返回结果
     * @return 聚合后列表
     */
    public static <T> List<T> joinRes(List<CompletionStage<T>> futures) {
        return futures.stream()
                .map(vo -> vo.exceptionally(e -> null))
                .map(CommonUtil::getRes)
                .filter(Objects::nonNull)
                .collect(toList());
    }

    /**
     * 合并多个List为一个List
     *
     * @param lists 多个List
     * @return 合并后list
     */
    public static <T> List<T> aggregateList(List<List<T>> lists) {
        int count = lists.stream().mapToInt(List::size).sum();
        final List<T> arrays = new ArrayList<>(count);
        lists.forEach(arrays::addAll);
        return arrays;
    }
}
