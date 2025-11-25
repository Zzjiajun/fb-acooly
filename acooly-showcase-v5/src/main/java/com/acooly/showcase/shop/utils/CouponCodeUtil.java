package com.acooly.showcase.shop.utils;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
public class CouponCodeUtil {
    private static final String CHARSET = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"; // 去掉易混字符
    private static final SecureRandom random = new SecureRandom();
    private static final String SECRET = "mySuperSecretKey"; // 建议放到配置文件中

    /** 生成唯一优惠码 */
    public static String generateCoupon(String prefix) {
        long timestamp = Instant.now().getEpochSecond();
        String base62 = toBase62(timestamp) + randomString(4);
        String hash = shortHash(base62 + SECRET);
        return prefix + "-" + base62 + "-" + hash;
    }

    /** 验证优惠码是否有效 */
    public static boolean verifyCoupon(String code, String prefix) {
        if (code == null || !code.startsWith(prefix + "-")) return false;
        String[] parts = code.split("-");
        if (parts.length != 3) return false;
        String base62 = parts[1];
        String hash = parts[2];
        return hash.equals(shortHash(base62 + SECRET));
    }

    /** Base62 编码 */
    private static String toBase62(long num) {
        StringBuilder sb = new StringBuilder();
        while (num > 0) {
            sb.append(CHARSET.charAt((int) (num % CHARSET.length())));
            num /= CHARSET.length();
        }
        return sb.reverse().toString();
    }

    /** 生成随机字符串 */
    private static String randomString(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(CHARSET.charAt(random.nextInt(CHARSET.length())));
        }
        return sb.toString();
    }

    /** 生成短哈希 */
    private static String shortHash(String input) {
        return Base64.getEncoder().encodeToString(input.getBytes())
                .replaceAll("[^A-Z0-9]", "")
                .substring(0, 3);
    }

    public static void main(String[] args) {
        String coupon = generateCoupon("SALE");
        boolean b = verifyCoupon(coupon, "SALE");
        System.out.println("验证结果："+b);
        System.out.println("生成优惠码: " + coupon);
        System.out.println("验证结果: " + verifyCoupon(coupon, "SALE"));
    }
}