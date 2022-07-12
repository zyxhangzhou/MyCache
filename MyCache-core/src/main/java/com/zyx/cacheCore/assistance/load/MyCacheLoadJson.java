package com.zyx.cacheCore.assistance.load;

import com.alibaba.fastjson2.JSON;
import com.zyx.cacheApi.api.IMyCache;
import com.zyx.cacheApi.api.IMyCacheLoad;
import com.zyx.cacheCore.model.PersistRdbEntry;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/9 12:07
 * @Description JSON模式的加载
 */
@Slf4j
public class MyCacheLoadJson<K, V> implements IMyCacheLoad<K, V> {

    /**
     * 文件路径
     */
    private final String dbPath;

    public MyCacheLoadJson(String dbPath) {
        this.dbPath = dbPath;
    }

    @Override
    public void load(IMyCache<K, V> cache) {
        List<String> lines;
        try {
            lines = Files.readAllLines(Paths.get(dbPath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.info("[load] starting to process path: {}", dbPath);
        if (CollectionUtils.isEmpty(lines)) {
            log.info("[load] path: {} file is empty!", dbPath);
            return;
        }
        for (String line : lines) {
            if (null == line || "".equals(line)) {
                continue;
            }
            PersistRdbEntry<K, V> entry = JSON.parseObject(line, PersistRdbEntry.class);
            K key = entry.getKey();
            V value = entry.getValue();
            Long expire = entry.getExpire();
            cache.put(key, value);
            if (null != expire) {
                cache.expireAt(key, expire);
            }
        }
    }
}
