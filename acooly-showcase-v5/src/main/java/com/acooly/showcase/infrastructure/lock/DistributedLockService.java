package com.acooly.showcase.infrastructure.lock;

public interface DistributedLockService {
    boolean tryLock(String key, String value, long expireSeconds);
    void unlock(String key, String value);
} 