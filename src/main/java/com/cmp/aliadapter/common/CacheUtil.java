package com.cmp.aliadapter.common;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CacheUtil {

    private static Map<String, Object> cacheMap;

    private static void initMapCache() {
        if (null == cacheMap) {
            cacheMap = new HashMap<>(16);
        }
    }

    public static void set(String key, Object value) {
        initMapCache();
        cacheMap.put(key, value);
    }

    public static String get(String key) {
        initMapCache();
        return JsonUtil.objectToString(cacheMap.get(key));
    }

    public static void remove(String key) {
        initMapCache();
        cacheMap.remove(key);
    }

    public static Set<String> getKeys() {
        initMapCache();
        return cacheMap.keySet();
    }

    public static void clear() {
        initMapCache();
        cacheMap.clear();
    }
}
