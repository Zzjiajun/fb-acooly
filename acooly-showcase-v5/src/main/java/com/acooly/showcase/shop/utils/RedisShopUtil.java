package com.acooly.showcase.shop.utils;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Slf4j
public class RedisShopUtil {
    @Resource(name = "jsonRedisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    private static final String CACHE_KEY_SEPARATOR = ".";

    /*======================== 通用 ========================*/

    /**
     * 构建统一格式 key，例如 buildKey("user", "123") → user.123
     */
    public String buildKey(String... segments) {
        return Stream.of(segments)
                .filter(Objects::nonNull)
                .collect(Collectors.joining(CACHE_KEY_SEPARATOR));
    }

    /**
     * 判断是否存在指定 key
     */
    public boolean exist(String key) {
        Boolean hasKey = redisTemplate.hasKey(key);
        return Boolean.TRUE.equals(hasKey);
    }

    public void deleteHotResultKeys(String searchKey) {
        int count = 0;

        try {
            ScanOptions options = ScanOptions.scanOptions()
                    .match(searchKey + "*")   // 匹配前缀
                    .count(500)               // 每次扫描数量
                    .build();

            Cursor<byte[]> cursor = redisTemplate
                    .getConnectionFactory()
                    .getConnection()
                    .scan(options);

            while (cursor.hasNext()) {
                String key = new String(cursor.next());
                redisTemplate.delete(key);
                count++;
            }

            log.info("🔥 已删除 {}* 前缀缓存，共 {} 个 key", searchKey, count);

        } catch (Exception e) {
            log.error("❌ 删除 {}* 前缀缓存失败，已删除 {} 个 key，原因：{}",
                    searchKey, count, e.getMessage());
        }
    }


    /**
     * 删除指定 key
     */
    public boolean del(String key) {
        try {
            Boolean result = redisTemplate.delete(key);
            return Boolean.TRUE.equals(result);
        } catch (DataAccessException e) {
            log.error("Redis 删除 key 失败: {}", key, e);
            return false;
        }
    }

    /*======================== String 操作 ========================*/

    public <T> void set(String key, T value) {
        try {
            redisTemplate.opsForValue().set(key, value);
        } catch (Exception e) {
            log.error("Redis set 失败 key={} value={}", key, value, e);
        }
    }

    public <T> void set(String key, T value, long timeout, TimeUnit timeUnit) {
        try {
            redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
        } catch (Exception e) {
            log.error("Redis set 失败 key={} value={}", key, value, e);
        }
    }

    public boolean setNx(String key, Object value, Long time, TimeUnit timeUnit) {
        try {
            Boolean result = redisTemplate.opsForValue().setIfAbsent(key, value, time, timeUnit);
            return Boolean.TRUE.equals(result);
        } catch (Exception e) {
            log.error("Redis setNx 失败 key={}", key, e);
            return false;
        }
    }

    public <T> T get(String key, Class<T> clazz) {
        try {
            Object obj = redisTemplate.opsForValue().get(key);
            return clazz.isInstance(obj) ? clazz.cast(obj) : null;
        } catch (Exception e) {
            log.error("Redis get 失败 key={}", key, e);
            return null;
        }
    }

    public Object get(String key){
        try {
            Object obj = redisTemplate.opsForValue().get(key);
            return obj;
        } catch (Exception e) {
            log.error("Redis get 失败 key={}", key, e);
            return null;
        }
    }

    public Integer getInt(String key) {
        Object value = redisTemplate.opsForValue().get(key);
        if (value instanceof Integer) {
            return (Integer) value;
        } else if (value instanceof String) {
            try {
                return Integer.parseInt((String) value);
            } catch (NumberFormatException e) {
                log.warn("Redis key={} 值无法转换为整数: {}", key, value);
            }
        }
        return null;
    }

    public void increment(String key, int count) {
        try {
            redisTemplate.opsForValue().increment(key, count);
        } catch (Exception e) {
            log.error("Redis increment 失败 key={} count={}", key, count, e);
        }
    }

    /*======================== Hash 操作 ========================*/

    public <T> void putHash(String key, String hashKey, T hashVal) {
        try {
            redisTemplate.opsForHash().put(key, hashKey, hashVal);
        } catch (Exception e) {
            log.error("Redis putHash 失败 key={} hashKey={}", key, hashKey, e);
        }
    }

    public <T> T getHash(String key, String hashKey, Class<T> clazz) {
        try {
            Object value = redisTemplate.opsForHash().get(key, hashKey);
            return clazz.isInstance(value) ? clazz.cast(value) : null;
        } catch (Exception e) {
            log.error("Redis getHash 失败 key={} hashKey={}", key, hashKey, e);
            return null;
        }
    }

    public Map<Object, Object> getHashAndDelete(String key) {
        Map<Object, Object> result = new HashMap<>();
        try (Cursor<Map.Entry<Object, Object>> cursor =
                     redisTemplate.opsForHash().scan(key, ScanOptions.NONE)) {
            while (cursor.hasNext()) {
                Map.Entry<Object, Object> entry = cursor.next();
                result.put(entry.getKey(), entry.getValue());
                redisTemplate.opsForHash().delete(key, entry.getKey());
            }
        } catch (Exception e) {
            log.error("Redis getHashAndDelete 失败 key={}", key, e);
        }
        return result;
    }

    /*======================== ZSet 排行榜操作 ========================*/

    public boolean zAdd(String key, String member, double score) {
        try {
            Boolean added = redisTemplate.opsForZSet().add(key, member, score);
            return Boolean.TRUE.equals(added);
        } catch (Exception e) {
            log.error("Redis zAdd 失败 key={} member={} score={}", key, member, score, e);
            return false;
        }
    }

    public long countZset(String key) {
        Long size = redisTemplate.opsForZSet().size(key);
        return size == null ? 0L : size;
    }

    public Set<String> rangeZset(String key, long start, long end) {
        Set<Object> rawSet = redisTemplate.opsForZSet().range(key, start, end);
        if (rawSet == null) return Collections.emptySet();
        return rawSet.stream().map(Object::toString).collect(Collectors.toSet());
    }

    public long removeZset(String key, Object value) {
        Long removed = redisTemplate.opsForZSet().remove(key, value);
        return removed == null ? 0L : removed;
    }

    public void removeZsetList(String key, Set<String> values) {
        values.forEach(v -> redisTemplate.opsForZSet().remove(key, v));
    }

    public Double score(String key, Object member) {
        return redisTemplate.opsForZSet().score(key, member);
    }

    public Set<String> rangeByScore(String key, double min, double max) {
        Set<Object> result = redisTemplate.opsForZSet().rangeByScore(key, min, max);
        if (result == null) return Collections.emptySet();
        return result.stream().map(Object::toString).collect(Collectors.toSet());
    }

    public Double addScore(String key, Object member, double delta) {
        return redisTemplate.opsForZSet().incrementScore(key, member, delta);
    }

    public Long rank(String key, Object member) {
        return redisTemplate.opsForZSet().rank(key, member);
    }

    public Set<ZSetOperations.TypedTuple<String>> rankWithScore(String key, long start, long end) {
        Set<ZSetOperations.TypedTuple<Object>> result =
                redisTemplate.opsForZSet().reverseRangeWithScores(key, start, end);
        if (result == null) return Collections.emptySet();
        return result.stream().map(e ->
                new ZSetOperations.TypedTuple<String>() {
                    @Override
                    public String getValue() {
                        return e.getValue().toString();
                    }

                    @Override
                    public Double getScore() {
                        return e.getScore();
                    }

                    @Override
                    public int compareTo(ZSetOperations.TypedTuple<String> o) {
                        return Double.compare(getScore(), o.getScore());
                    }
                }).collect(Collectors.toSet());
    }

    /** ✅ 获取列表缓存（泛型安全反序列化） */
    public <T> List<T> getList(String key, Class<T> clazz) {
        try {
            Object value = redisTemplate.opsForValue().get(key);
            if (value == null) return null;

            if (value instanceof List<?>) {
                // 直接是 List 类型（Jackson 序列化返回的）
                return (List<T>) value;
            } else if (value instanceof String) {
                // JSON 字符串
                return JSON.parseArray((String) value, clazz);
            } else {
                // 其他对象转换为 JSON 后再反序列化
                String json = JSON.toJSONString(value);
                return JSON.parseArray(json, clazz);
            }
        } catch (Exception e) {
            log.error("❌ Redis getList error, key={}, msg={}", key, e.getMessage());
            return null;
        }
    }

    public void deleteByPattern(String pattern) {
        Set<String> keys = redisTemplate.keys(pattern);
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }

    public Set<String> scan(String pattern) {
        Set<String> keys = new HashSet<>();
        try {
            RedisConnection connection = Objects.requireNonNull(redisTemplate.getConnectionFactory())
                    .getConnection();

            // 2.1.8 版本 scan 需要用 ScanOptions
            ScanOptions options = ScanOptions.scanOptions()
                    .match(pattern)
                    .count(100)
                    .build();

            Cursor<byte[]> cursor = connection.scan(options);
            while (cursor.hasNext()) {
                keys.add(new String(cursor.next(), StandardCharsets.UTF_8));
            }
            cursor.close();
            connection.close();
        } catch (Exception e) {
            log.error("Redis scan 失败 pattern={}", pattern, e);
        }
        return keys;
    }
    public long delete(String... keys) {
        if (keys == null || keys.length == 0) {
            return 0;
        }
        try {
            Set<String> keySet = new HashSet<>();
            for (String key : keys) {
                if (key != null) {
                    keySet.add(key);
                }
            }
            if (keySet.isEmpty()) {
                return 0;
            }
            Long result = redisTemplate.delete(keySet);
            return result != null ? result : 0;
        } catch (Exception e) {
            log.error("Redis 批量删除失败 keys={}", Arrays.toString(keys), e);
            return 0;
        }
    }
}
