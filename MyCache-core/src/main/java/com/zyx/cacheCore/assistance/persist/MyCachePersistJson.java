package com.zyx.cacheCore.assistance.persist;

import com.alibaba.fastjson.JSON;
import com.zyx.cacheApi.api.IMyCache;
import com.zyx.cacheCore.model.PersistRdbEntry;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/8 17:26
 * @Description JSON模式的持久化
 */
public class MyCachePersistJson<K, V> extends MyCachePersistAdaptor<K, V> {


    /**
     * json的路径
     */
    private final String dbPath;

    public MyCachePersistJson(String dbPath) {
        this.dbPath = dbPath;
    }

    @Override
    public void persist(IMyCache<K, V> cache) {
        Set<Map.Entry<K, V>> entrySet = cache.entrySet();
        Path path = Paths.get(dbPath);
        // 创建并清空文件
        try {
            Files.write(path, "".getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (Map.Entry<K, V> entry : entrySet) {
            K key = entry.getKey();
            Long expireTime = cache.expire().expireTime(key);
            PersistRdbEntry<K, V> persistEntry = new PersistRdbEntry<>();
            persistEntry.setKey(key);
            persistEntry.setValue(entry.getValue());
            persistEntry.setExpire(expireTime);
            String line = JSON.toJSONString(persistEntry) + "\n";
            try {
                Files.write(path, line.getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 立即执行：0
     *
     * @return long
     */
    @Override
    public long delay() {
        return 0;
    }

    /**
     * 执行之后每隔多长时间执行一下
     *
     * @return long
     */
    @Override
    public long period() {
        return 5;
    }

    @Override
    public TimeUnit timeUnit() {
        return TimeUnit.MINUTES;
    }
}
