package com.acooly.showcase.infrastructure.lock.impl;

import com.acooly.showcase.daliy.Utils.RedisUtils;
import com.acooly.showcase.infrastructure.lock.DistributedLockService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisDistributedLockService implements DistributedLockService {

    private final RedisUtils redisUtils;
    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean tryLock(String key, String value, long expireSeconds) {
        return redisUtils.setNx(key, value, expireSeconds, TimeUnit.SECONDS);
    }

    @Override
    public void unlock(String key, String value) {
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(script, Long.class);
        stringRedisTemplate.execute(redisScript, Collections.singletonList(key), value);
    }
} 