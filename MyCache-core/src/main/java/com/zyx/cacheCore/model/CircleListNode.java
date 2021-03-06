package com.zyx.cacheCore.model;

import java.util.StringJoiner;

/**
 * @Author Zhang Yuxiao
 * @Date 2022/7/12 13:57
 * @Description 循环链表节点
 */
public class CircleListNode<K, V> {

    /**
     * 键
     */
    private K key;

    /**
     * 值
     */
    private V value = null;

    /**
     * 是否被访问过
     */
    private boolean accessFlag = false;

    /**
     * 后一个节点
     */
    private CircleListNode<K, V> pre;

    /**
     * 后一个节点
     */
    private CircleListNode<K, V> next;

    public CircleListNode(K key) {
        this.key = key;
    }

    public K key() {
        return key;
    }

    public CircleListNode<K, V> key(K key) {
        this.key = key;
        return this;
    }

    public V value() {
        return value;
    }

    public CircleListNode<K, V> value(V value) {
        this.value = value;
        return this;
    }

    public boolean accessFlag() {
        return accessFlag;
    }

    public CircleListNode<K, V> accessFlag(boolean accessFlag) {
        this.accessFlag = accessFlag;
        return this;
    }

    public CircleListNode<K, V> pre() {
        return pre;
    }

    public CircleListNode<K, V> pre(CircleListNode<K, V> pre) {
        this.pre = pre;
        return this;
    }

    public CircleListNode<K, V> next() {
        return next;
    }

    public CircleListNode<K, V> next(CircleListNode<K, V> next) {
        this.next = next;
        return this;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CircleListNode.class.getSimpleName() + "[", "]")
                .add("key=" + key)
                .add("value=" + value)
                .add("accessFlag=" + accessFlag)
                .add("pre=" + pre)
                .add("next=" + next)
                .toString();
    }
}
