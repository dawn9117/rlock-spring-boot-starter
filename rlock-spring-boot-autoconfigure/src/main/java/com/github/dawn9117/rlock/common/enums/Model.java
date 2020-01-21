package com.github.dawn9117.rlock.common.enums;

public enum Model {
    //哨兵
    SENTINEL,
    //主从
    MASTER_SLAVE,
    //单例
    SINGLE,
    //集群
    CLUSTER,
    //云托管模式
    REPLICATED
}
