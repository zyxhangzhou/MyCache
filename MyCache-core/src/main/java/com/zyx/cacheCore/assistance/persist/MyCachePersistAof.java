package com.zyx.cacheCore.assistance.persist;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSON;
import com.zyx.cacheApi.api.IMyCache;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/10 16:53
 * @Description 持久化AOF
 */
@Slf4j
public class MyCachePersistAof<K, V> extends MyCachePersistAdaptor<K, V> {
    /**
     * 缓存列表
     */
    private final List<String> bufferList = new ArrayList<>();

    /**
     * 数据持久化路径
     */
    private final String dbPath;

    public MyCachePersistAof(String dbPath) {
        this.dbPath = dbPath;
    }

    /**
     * AOF模式的持久化
     * 后期可以考虑把写文件的操作抽象成一个工具类，减少暴露细节
     *
     * @param cache 缓存
     */
    @Override
    public void persist(IMyCache<K, V> cache) {
        log.info("开始 AOF 持久化到文件");
        // 1. 创建文件
        Path path = Paths.get(dbPath);
        if (Files.notExists(path)) {
            try {
                Files.write(path, "".getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        // 2. 持久化追加到文件中
        StringBuffer sb = new StringBuffer();
        bufferList.forEach(buffer -> sb.append(buffer).append('\n'));
        try {
            Files.writeString(path, sb.toString(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // 3. 清空 buffer 列表
        bufferList.clear();
        log.info("完成 AOF 持久化到文件");
    }

    @Override
    public long delay() {
        return 1;
    }

    @Override
    public long period() {
        return 1;
    }

    @Override
    public TimeUnit timeUnit() {
        return super.timeUnit();
    }

    public void append(final String json) {
        if (null == json || json.length() == 0) return;
        bufferList.add(json);
    }
}
