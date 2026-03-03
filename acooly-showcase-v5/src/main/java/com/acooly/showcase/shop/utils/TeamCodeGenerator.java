package com.acooly.showcase.shop.utils;

public class TeamCodeGenerator {

    // 雪花算法
    private static class SnowflakeIdWorker {
        private final long twepoch = 1288834974657L;

        private final long workerIdBits = 5L;        // 机器 ID
        private final long datacenterIdBits = 5L;    // 数据中心 ID
        private final long maxWorkerId = -1L ^ (-1L << workerIdBits);
        private final long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);

        private final long sequenceBits = 12L;
        private final long workerIdShift = sequenceBits;
        private final long datacenterIdShift = sequenceBits + workerIdBits;
        private final long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;
        private final long sequenceMask = -1L ^ (-1L << sequenceBits);

        private long workerId;
        private long datacenterId;
        private long sequence = 0L;
        private long lastTimestamp = -1L;

        public SnowflakeIdWorker(long workerId, long datacenterId) {
            if (workerId > maxWorkerId || workerId < 0) {
                throw new IllegalArgumentException("workerId out of range");
            }
            if (datacenterId > maxDatacenterId || datacenterId < 0) {
                throw new IllegalArgumentException("datacenterId out of range");
            }
            this.workerId = workerId;
            this.datacenterId = datacenterId;
        }

        public synchronized long nextId() {
            long timestamp = System.currentTimeMillis();

            if (timestamp < lastTimestamp) {
                throw new RuntimeException("Clock moved backwards");
            }

            if (lastTimestamp == timestamp) {
                sequence = (sequence + 1) & sequenceMask;
                if (sequence == 0) {
                    timestamp = nextMillis(lastTimestamp);
                }
            } else {
                sequence = 0L;
            }

            lastTimestamp = timestamp;

            return ((timestamp - twepoch) << timestampLeftShift)
                    | (datacenterId << datacenterIdShift)
                    | (workerId << workerIdShift)
                    | sequence;
        }

        private long nextMillis(long lastTimestamp) {
            long timestamp = System.currentTimeMillis();
            while (timestamp <= lastTimestamp) {
                timestamp = System.currentTimeMillis();
            }
            return timestamp;
        }
    }

    // Base62 编码
    private static class Base62 {
        private static final char[] BASE62 =
                "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();

        public static String encode(long value) {
            StringBuilder sb = new StringBuilder();
            while (value > 0) {
                sb.append(BASE62[(int) (value % 62)]);
                value /= 62;
            }
            return sb.reverse().toString();
        }
    }

    // 单例 Snowflake 实例（机器号 = 1，机房 = 1）
    private static final SnowflakeIdWorker WORKER = new SnowflakeIdWorker(1, 1);

    /**
     * 生成唯一团队 TeamCode
     * 格式示例：T5N8sL9a3B
     */
    public static String generateTeamCode() {
        long id = WORKER.nextId();
        return "T" + Base62.encode(id); // 加前缀 T
    }

    public static void main(String[] args) {
        // 测试 10 个生成结果
        for (int i = 0; i < 10; i++) {
            System.out.println(generateTeamCode());
        }
    }
}
