package burp;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class UrlEncodeUtil {

    /**
     * URL 编码
     * 使用 UTF-8 字符集，确保中文和特殊字符正确编码
     */
    public static String encode(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        try {
            // 必须指定字符集为 UTF-8，否则不同平台默认字符集不同会导致结果不一致
            return URLEncoder.encode(input, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            // UTF-8 是所有 Java 实现都必须支持的字符集，理论上不会抛出此异常
            throw new RuntimeException("UTF-8 encoding not supported", e);
        }
    }

    /**
     * URL 解码
     * 使用 UTF-8 字符集，将编码后的字符串还原
     */
    public static String decode(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        try {
            // 必须指定字符集为 UTF-8，与编码时保持一致
            return URLDecoder.decode(input, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UTF-8 encoding not supported", e);
        }
    }

    // --- 测试验证 ---
    public static void main(String[] args) {
        // 原始数据 (你提供的第一个字符串)
        String originalJson = "{\"item\":{\"pagesize\":16,\"currentpageindex\":1,\"queryString\":\"ordertype=&productcategoryid=&ProductKeyword=--+&orderbytype=&productzone=\"},\"devicenumber\":\"13787419095389876\",\"versionno\":\"1\",\"versioncode\":\"1\",\"platform\":\"1\",\"time\":\"20260325124720\",\"token\":\"IQExgBwL0abZICmOKeqGCsnlrsjFcxV3Gu9sAS1qbCuBgKXF6ag6htPgti6OhSzt\",\"sign\":\"B09AC2639B54B9533845C89EE8FAF0B0\"}";

        // 目标编码数据 (你提供的第二个字符串)
        String expectedEncoded = "%7B%22item%22%3A%7B%22pagesize%22%3A16%2C%22currentpageindex%22%3A1%2C%22queryString%22%3A%22ordertype%3D%26productcategoryid%3D%26ProductKeyword%3D--%2B%26orderbytype%3D%26productzone%3D%22%7D%2C%22devicenumber%22%3A%2213787419095389876%22%2C%22versionno%22%3A%221%22%2C%22versioncode%22%3A%221%22%2C%22platform%22%3A%221%22%2C%22time%22%3A%2220260325124720%22%2C%22token%22%3A%22IQExgBwL0abZICmOKeqGCsnlrsjFcxV3Gu9sAS1qbCuBgKXF6ag6htPgti6OhSzt%22%2C%22sign%22%3A%22B09AC2639B54B9533845C89EE8FAF0B0%22%7D";

        // 执行编码
        String actualEncoded = encode(originalJson);

        // 执行解码 (对目标编码数据进行解码)
        String actualDecoded = decode(expectedEncoded);

        System.out.println("=== 编码测试 ===");
        System.out.println("编码结果匹配: " + expectedEncoded.equals(actualEncoded));
        if (!expectedEncoded.equals(actualEncoded)) {
            System.out.println("期望长度: " + expectedEncoded.length());
            System.out.println("实际长度: " + actualEncoded.length());
            // 如果失败，打印前100个字符对比
            System.out.println("期望前缀: " + expectedEncoded.substring(0, Math.min(100, expectedEncoded.length())));
            System.out.println("实际前缀: " + actualEncoded.substring(0, Math.min(100, actualEncoded.length())));
        }

        System.out.println("\n=== 解码测试 ===");
        System.out.println("解码结果匹配: " + originalJson.equals(actualDecoded));

        System.out.println("\n=== 双向循环测试 ===");
        // 测试 编码 -> 解码 -> 编码 是否稳定
        String roundTrip = encode(decode(actualEncoded));
        System.out.println("循环测试匹配: " + expectedEncoded.equals(roundTrip));
    }
}