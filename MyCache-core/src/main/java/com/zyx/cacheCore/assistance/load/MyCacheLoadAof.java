package com.zyx.cacheCore.assistance.load;

import com.alibaba.fastjson.JSON;
import com.zyx.cacheApi.annotation.MyCacheInterceptor;
import com.zyx.cacheApi.api.IMyCache;
import com.zyx.cacheApi.api.IMyCacheLoad;
import com.zyx.cacheCore.assistance.util.ArgumentUtils;
import com.zyx.cacheCore.core.MyCache;
import com.zyx.cacheCore.model.PersistAofEntry;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/10 21:16
 * @Description AOF模式的加载
 */
@Slf4j
public class MyCacheLoadAof<K, V> implements IMyCacheLoad<K, V> {

    /**
     * 方法缓存
     */
    private static final Map<String, Method> METHOD_MAP = new HashMap<>();

    static {
        Method[] methods = MyCache.class.getMethods();

        for (Method method : methods) {
            MyCacheInterceptor cacheInterceptor = method.getAnnotation(MyCacheInterceptor.class);
            if (cacheInterceptor != null) {
                if (cacheInterceptor.aof()) {
                    String methodName = method.getName();
                    METHOD_MAP.put(methodName, method);
                }
            }
        }

    }

    /**
     * 文件路径
     */
    private final String dbPath;

    public MyCacheLoadAof(String dbPath) {
        this.dbPath = dbPath;
    }

    @Override
    public void load(IMyCache<K, V> cache) {
        Path path = Paths.get(dbPath);
        List<String> lines;
        try {
            lines = Files.readAllLines(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.info("[load] Starting to process path： {}", dbPath);
        if (CollectionUtils.isEmpty(lines)) {
            log.info("[load] path: {} file is empty! ", dbPath);
            return;
        }
        for (String line : lines) {
            if (line == null || line.length() == 0) {
                continue;
            }

            // 执行
            // 简单的类型还行，复杂的这种反序列化会失败
            PersistAofEntry entry = JSON.parseObject(line, PersistAofEntry.class);

            final String methodName = entry.getMethodName();
            final Object[] objects = entry.getParams();

            final Method method = METHOD_MAP.get(methodName);
            // 反射调用
            ArgumentUtils.requireNotNull(method, "method");
            try {
                method.invoke(cache, objects);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
