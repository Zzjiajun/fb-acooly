package com.acooly.showcase.shop.utils;

import org.springframework.stereotype.Component;

@Component
public class SerialNumberGenerator {

    private static final long EPOCH = 1765970000000L; // 自定义起始时间戳
    private static long sequence = 0L;
    private static long lastTimestamp = -1L;
    private synchronized long nextId() {
        long timestamp = System.currentTimeMillis();
        if (timestamp == lastTimestamp) {
            sequence = (sequence + 1) & 4095; // 每毫秒最多 4096 个
            if (sequence == 0) {
                // 等待下一毫秒
                while (timestamp <= lastTimestamp) {
                    timestamp = System.currentTimeMillis();
                }
            }
        } else {
            sequence = 0L;
        }
        lastTimestamp = timestamp;
        return ((timestamp - EPOCH) << 12) | sequence;
    }
    /** Base36 固定长度编码 */
    private String toBase36(long num, int len) {
        String s = Long.toString(num, 36).toUpperCase();
        if (s.length() >= len) return s.substring(s.length() - len);
        return String.format("%" + len + "s", s).replace(' ', '0');
    }
    /**
     * 生成固定 10 位 ID：
     * SP + 2位时间标识（每天变化） + 6位雪花ID
     */
    public synchronized String generateShortSerial() {
        // 1）时间标识：每天一个值，Base36 2位
        long days = (System.currentTimeMillis() - EPOCH) / 86400000L;
        String timeCode = toBase36(days, 2);
        // 2）雪花序列：6位 Base36
        String seqCode = toBase36(nextId(), 6);
        // String dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        return "SP" + timeCode + seqCode; // 固定10位
    }
}
