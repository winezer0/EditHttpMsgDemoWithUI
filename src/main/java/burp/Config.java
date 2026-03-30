package burp;

import java.util.Map;
import java.util.TreeMap;

/**
 * 配置类，存储扩展的全局配置
 */
public class Config {
    public static String EXTENSION_NAME = "EditHttpMsgUIDemo"; //扩展名
    public static String EXTENSION_VERSION = "v0.0.1"; //扩展版本号
    // 请求HOST关键字匹配正则
    public static String WHITE_HOST_REGX = ""; //白名单HOST匹配正则
    public static String BLACK_HOST_REGX = ""; //黑名单HOST匹配正则
    // 请求方法关键字匹配正则
    public static String WHITE_METHOD_REGX = ""; //白名单方法匹配正则
    public static String BLACK_METHOD_REGX = ""; //黑名单方法匹配正则
    // 请求路径关键字匹配正则
    public static String WHITE_PATH_REGX = ""; //白名单路径匹配正则
    public static String BLACK_PATH_REGX = ""; //黑名单路径匹配正则
    //黑名单扩展名匹配正则
    public static String BLACK_SUFFIX_REGX = "";
    // 请求体关键字匹配正则
    public static String WHITE_BODY_REGX = ""; // 请求体关键字
    // 工具模块监听开关配置
    public static boolean LISTEN_PROXY = true;      // 监听 Proxy 模块
    public static boolean LISTEN_REPEATER = true;   // 监听 Repeater 模块
    public static boolean LISTEN_SCANNER = false;   // 监听 Scanner 模块
    public static boolean LISTEN_INTRUDER = false;  // 监听 Intruder 模块
    public static boolean LISTEN_EXTENDER = false;  // 监听 Extender 模块



    // 加载当前的Config Map 到 config
    public static void updateConfigVarsFromMap(Map<String, Object> configMap) {
        Config.WHITE_HOST_REGX = (String) configMap.getOrDefault("WHITE_HOST_REGX", "");
        Config.BLACK_HOST_REGX = (String) configMap.getOrDefault("BLACK_HOST_REGX", "");
        Config.WHITE_METHOD_REGX = (String) configMap.getOrDefault("WHITE_METHOD_REGX", "");
        Config.BLACK_METHOD_REGX = (String) configMap.getOrDefault("BLACK_METHOD_REGX", "");
        Config.WHITE_PATH_REGX = (String) configMap.getOrDefault("WHITE_PATH_REGX", "");
        Config.BLACK_PATH_REGX = (String) configMap.getOrDefault("BLACK_PATH_REGX", "");
        Config.BLACK_SUFFIX_REGX = (String) configMap.getOrDefault("BLACK_SUFFIX_REGX", "");
        Config.WHITE_BODY_REGX = (String) configMap.getOrDefault("WHITE_BODY_REGX", "");
        Config.LISTEN_PROXY = (Boolean) configMap.getOrDefault("LISTEN_PROXY", false);
        Config.LISTEN_REPEATER = (Boolean) configMap.getOrDefault("LISTEN_REPEATER", true);
        Config.LISTEN_SCANNER = (Boolean) configMap.getOrDefault("LISTEN_SCANNER", false);
        Config.LISTEN_INTRUDER = (Boolean) configMap.getOrDefault("LISTEN_INTRUDER", false);
        Config.LISTEN_EXTENDER = (Boolean) configMap.getOrDefault("LISTEN_EXTENDER", false);
    }


    // 加载当前的Config配置到Map格式（按key排序）
    public static Map<String, Object> loadConfigVarsToMap() {
        Map<String, Object> configMap = new TreeMap<>();
        configMap.put("WHITE_HOST_REGX", Config.WHITE_HOST_REGX);
        configMap.put("BLACK_HOST_REGX", Config.BLACK_HOST_REGX);
        configMap.put("WHITE_METHOD_REGX", Config.WHITE_METHOD_REGX);
        configMap.put("BLACK_METHOD_REGX", Config.BLACK_METHOD_REGX);
        configMap.put("WHITE_PATH_REGX", Config.WHITE_PATH_REGX);
        configMap.put("BLACK_PATH_REGX", Config.BLACK_PATH_REGX);
        configMap.put("BLACK_SUFFIX_REGX", Config.BLACK_SUFFIX_REGX);
        configMap.put("WHITE_BODY_REGX", Config.WHITE_BODY_REGX);
        configMap.put("LISTEN_PROXY", Config.LISTEN_PROXY);
        configMap.put("LISTEN_REPEATER", Config.LISTEN_REPEATER);
        configMap.put("LISTEN_SCANNER", Config.LISTEN_SCANNER);
        configMap.put("LISTEN_INTRUDER", Config.LISTEN_INTRUDER);
        configMap.put("LISTEN_EXTENDER", Config.LISTEN_EXTENDER);
        return configMap;
    }
}

