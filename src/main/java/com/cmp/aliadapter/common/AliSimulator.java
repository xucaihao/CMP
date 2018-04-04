package com.cmp.aliadapter.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.*;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

public class AliSimulator {

    private static final Logger logger = LoggerFactory.getLogger(AliSimulator.class);

    public static <T> T create(Class<T> targetType, String objectId, Map<String, Object> values) {
        try {
            T result = targetType.newInstance();
            setPropertites(targetType, values, result);
            String key = targetType.getName() + objectId;
            CacheUtil.set(key, JsonUtil.objectToString(result));
            return result;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    public static <T> void delete(Class<T> targetType, String objectId) {
        CacheUtil.remove(targetType.getName() + objectId);
    }

    public static <T> T modify(Class<T> targetType, String objectId, Map<String, Object> values) {
        String temp = CacheUtil.get(targetType.getName() + objectId);
        if (null != temp) {
            T result = JsonUtil.stringToObject(temp, targetType);
            try {
                setPropertites(targetType, values, result);
                String key = targetType.getName() + objectId;
                CacheUtil.set(key, JsonUtil.objectToString(result));
                return result;
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
        return null;
    }

    public static <T> T get(Class<T> targetType, String objectId) {
        String temp = CacheUtil.get(targetType.getName() + objectId);
        if (null != temp) {
            return JsonUtil.stringToObject(temp, targetType);
        }
        return null;
    }

    public static <T> List<T> getAll(Class<T> targetType) {
        Set<String> targetKeys = CacheUtil.getKeys().stream()
                .filter(key -> key.startsWith(targetType.getName()))
                .collect(toSet());
        return targetKeys.stream().map(key -> {
            String temp = CacheUtil.get(key);
            if (null != temp) {
                return JsonUtil.stringToObject(temp, targetType);
            } else {
                return null;
            }
        }).filter(Objects::nonNull).collect(toList());
    }

    public static void clear() {
        CacheUtil.clear();
    }

    private static <T> void setPropertites(Class<T> targetType, Map<String, Object> values, T result) {
        values.forEach((key, value) -> {
            try {
                Field targetField = targetType.getDeclaredField(key);
                targetField.setAccessible(true);
                targetField.set(result, value);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        });
    }
}
