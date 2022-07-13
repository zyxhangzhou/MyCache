# 项目介绍

自己动手实现一个缓存！配合Redis和HashMap的一些设计思想实现自己的缓存框架。

## 特性

- fluent流式编程，易于使用
- 支持自定义底层map实现策略
- 支持自定义expire策略
- 支持自定义evict策略
- 支持自定义删除监听器
- 支持自定义慢操作监听器
- 支持load加载初始化缓存
  - JSON模式
  - RDB模式
  - AOF模式
- 支持RDB与AOF持久化

## 基础测试

```java
IMyCache<String, String> cache = MyCacheBootstrap.<String, String>newInstance()
                .size(3)
                .build();
        cache.put("a", "a");
        cache.put("b", "b");
        cache.put("c", "c");
        cache.put("d", "d");
        
Assert.assertEquals(3, cache.size());
System.out.println(cache.entrySet());
```

默认为FIFO模式，输出为：

```text
[b=b, c=c, d=d]
```

## 慢操作警示与耗时提醒

上述示例在开始`debug`级别的日志记录时，还有如下日志打印：

```text
[DEBUG] 2022-07-13 20:39:41,579 method:com.zyx.cacheCore.assistance.interceptor.common.MyCacheInterceptorCost.before(MyCacheInterceptorCost.java:21)
Cost is starting, method: put
[DEBUG] 2022-07-13 20:39:41,583 method:com.zyx.cacheCore.assistance.interceptor.common.MyCacheInterceptorCost.after(MyCacheInterceptorCost.java:28)
Cost ended, method: put, cost: 6ms
[DEBUG] 2022-07-13 20:39:41,585 method:com.zyx.cacheCore.assistance.interceptor.common.MyCacheInterceptorCost.before(MyCacheInterceptorCost.java:21)
Cost is starting, method: put
[DEBUG] 2022-07-13 20:39:41,585 method:com.zyx.cacheCore.assistance.interceptor.common.MyCacheInterceptorCost.after(MyCacheInterceptorCost.java:28)
Cost ended, method: put, cost: 0ms
[DEBUG] 2022-07-13 20:39:41,586 method:com.zyx.cacheCore.assistance.interceptor.common.MyCacheInterceptorCost.before(MyCacheInterceptorCost.java:21)
Cost is starting, method: put
[DEBUG] 2022-07-13 20:39:41,586 method:com.zyx.cacheCore.assistance.interceptor.common.MyCacheInterceptorCost.after(MyCacheInterceptorCost.java:28)
Cost ended, method: put, cost: 0ms
[DEBUG] 2022-07-13 20:39:41,586 method:com.zyx.cacheCore.assistance.interceptor.common.MyCacheInterceptorCost.before(MyCacheInterceptorCost.java:21)
Cost is starting, method: put
[DEBUG] 2022-07-13 20:39:41,587 method:com.zyx.cacheCore.assistance.listener.remove.MyCacheRemoveListener.listen(MyCacheRemoveListener.java:16)
Removed key: a, value: a, type: evict
[DEBUG] 2022-07-13 20:39:41,587 method:com.zyx.cacheCore.assistance.interceptor.common.MyCacheInterceptorCost.after(MyCacheInterceptorCost.java:28)
Cost ended, method: put, cost: 1ms
[DEBUG] 2022-07-13 20:39:41,588 method:com.zyx.cacheCore.assistance.interceptor.common.MyCacheInterceptorCost.before(MyCacheInterceptorCost.java:21)
Cost is starting, method: size
[DEBUG] 2022-07-13 20:39:41,588 method:com.zyx.cacheCore.assistance.interceptor.refresh.MyCacheInterceptorRefresh.before(MyCacheInterceptorRefresh.java:17)
Refresh start
[DEBUG] 2022-07-13 20:39:41,589 method:com.zyx.cacheCore.assistance.interceptor.common.MyCacheInterceptorCost.after(MyCacheInterceptorCost.java:28)
Cost ended, method: size, cost: 1ms
[DEBUG] 2022-07-13 20:39:41,590 method:com.zyx.cacheCore.assistance.interceptor.common.MyCacheInterceptorCost.before(MyCacheInterceptorCost.java:21)
Cost is starting, method: entrySet
[DEBUG] 2022-07-13 20:39:41,590 method:com.zyx.cacheCore.assistance.interceptor.refresh.MyCacheInterceptorRefresh.before(MyCacheInterceptorRefresh.java:17)
Refresh start
[DEBUG] 2022-07-13 20:39:41,590 method:com.zyx.cacheCore.assistance.interceptor.common.MyCacheInterceptorCost.after(MyCacheInterceptorCost.java:28)
Cost ended, method: entrySet, cost: 0ms
```

通过设置慢操作监听器`addSlowListener(new MySlowListener())`，还可以得到慢操作警示（测试容忍时间设定为0）。

```text
[DEBUG] 2022-07-13 20:42:47,399 method:com.zyx.cacheCore.assistance.interceptor.common.MyCacheInterceptorCost.before(MyCacheInterceptorCost.java:21)
Cost is starting, method: put
[DEBUG] 2022-07-13 20:42:47,402 method:com.zyx.cacheCore.assistance.interceptor.common.MyCacheInterceptorCost.after(MyCacheInterceptorCost.java:28)
Cost ended, method: put, cost: 7ms
【慢日志】name: put
```

## 删除监听

```java
IMyCache<String, String> cache = MyCacheBootstrap.<String, String>newInstance()
        .size(2)
        .addRemoveListener(new MyRemoveListener<>())
        .build();
cache.put("math", "98");
cache.put("reading", "90");
cache.put("writing", "94");
```

日志如下：
```text
【删除警告】Oops，竟然被删除了！ key: math, value: 98, type: evict
```

上述监听器均可以自定义。

## 淘汰策略

可以通过`MyCacheEvicts`工具类来创建不同的淘汰策略。


| 策略               | 说明                                                |
|:-----------------|:--------------------------------------------------|
| none             | 空淘汰策略                                             |
| fifo             | 先进先出（默认策略）                                        |
| lru              | 最基本的朴素 LRU 策略，性能一般                                |
| lruDoubleListMap | 基于双向链表+MAP 实现的朴素 LRU，性能优于 lru                     |
| lruLinkedHashMap | 基于 LinkedHashMap 实现的朴素 LRU，与 lruDoubleListMap 差不多 |
| lru2Q            | 基于 LRU 2Q 的改进版 LRU 实现，命中率优于朴素LRU                  |
| lru2             | 基于 LRU-2 的改进版 LRU 实现，命中率优于 lru2Q                  |
| CLOCK            | 又称为最近未用算法，性能较好                                    |

## 支持过期

```java
IMyCache<String, String> cache = MyCacheBootstrap.<String, String>newInstance()
                .size(3)
                .build();
cache.put("a", "a");
cache.put("b", "b");
cache.expire("a", 10); // 当前的10ms后过期
Assert.assertEquals(2, cache.size());
TimeUnit.MILLISECONDS.sleep(120);
Assert.assertEquals(1, cache.size());
System.out.println(cache.keySet());
```
采用惰性删除的特性，每隔100ms检查一次惰性删除的键值对。但如果遇到`keySet()/size()`等这样的操作就会触发`refresh`机制立即刷新。

## 持久化数据

 内存中的cache数据容易灭失，框架支持将缓存数据持久化到RDB文件或AOF文件中去。

```java
IMyCache<String, String> cache = MyCacheBootstrap.<String, String>newInstance()
                .persist(MyCachePersists.json("1.rdb"))
                //.persist(MyCachePersists.aof("1.aof"))
                .build();

cache.put("x", "x");
cache.expire("x", 10);
cache.remove("2");
TimeUnit.SECONDS.sleep(1);
```
rdb和aof文件如下：
```text
// RDB

{"key":"paper","value":"thesis"}
{"key":"sun","value":"sunday"}

// AOF

{"methodName":"put","params":["x","x"]}
{"methodName":"remove","params":["2"]}
```

其中，RDB采用JSON格式。

## 加载数据

加载数据可以为缓存初始化。可以自定义数据，也可以从RDB文件、AOF文件中获取加载数据。

```java
IMyCache<String, String> cache = MyCacheBootstrap.<String, String>newInstance()
                .load(MyCacheLoads.aof("default.aof"))
                .build();

Assert.assertEquals(1, cache.size());
System.out.println(cache.entrySet());
```

`default.aof`文件如下：

```text
{"methodName":"put","params":["1","1"]}
{"methodName":"put","params":["x","x"]}
{"methodName":"remove","params":["2"]}
{"methodName":"remove","params":["1"]}
```
支持拦截器注解操作，使得代码便于后期调整。

## 后续展望

- 实现类似于Redis的渐进式rehash
- 实现过期删除的随机删除
- 支持并发操作


