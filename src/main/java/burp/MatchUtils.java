package burp;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MatchUtils {
    /**
     * 检查字符串是否包含匹配指定正则表达式的关键词
     *
     * @param regx 正则表达式字符串，用于匹配关键词
     * @param str 待检查的目标字符串
     * @param NoRegxValue 当正则表达式为空或空白时的默认返回值
     * @return 如果字符串包含匹配的关键词返回true，否则返回false；
     *         当正则表达式为空时返回NoRegxValue参数指定的值
     */
    public static boolean isMatchKeywords(String regx, String str, Boolean NoRegxValue){
        //如果没有正在表达式,的情况下返回指定值 NoRegxValue
        if (regx.trim().length() == 0){
            return NoRegxValue;
        }

        Pattern pat = Pattern.compile("^.*("+regx+").*$",Pattern.CASE_INSENSITIVE);//正则判断
        Matcher mc= pat.matcher(str);//条件匹配
        return mc.find();
    }

    /**
     * 检查文件路径的后缀是否匹配指定的正则表达式
     *
     * @param regx 用于匹配后缀的正则表达式
     * @param path 待检查的文件路径字符串
     * @param NoRegxValue 当正则表达式为空或空白时的默认返回值
     * @return 如果文件后缀匹配正则表达式返回true，否则返回false；
     *         当正则表达式为空时返回NoRegxValue参数指定的值；
     *         当文件没有后缀时返回false
     */
    public static boolean isMatchBlackSuffix(String regx, String path, Boolean NoRegxValue){
        //如果没有正在表达式,的情况下返回指定值 NoRegxValue
        if (regx.trim().length() == 0){
            return NoRegxValue;
        }

        String ext = getPathExtension(path);
        //无后缀情况全部放行
        if("".equalsIgnoreCase(ext)){
            return false;
        }else {
            //Pattern pat = Pattern.compile("([\\w]+[\\.]|)("+regx+")",Pattern.CASE_INSENSITIVE);//正则判断
            Pattern pat = Pattern.compile("^("+regx+")$",Pattern.CASE_INSENSITIVE);//正则判断
            Matcher mc= pat.matcher(ext);//条件匹配
            return mc.find();
        }
    }

    //获取请求路径的扩展名
    public static String getPathExtension(String path) {
        String extension="";

        if("/".equals(path)||"".equals(path)){
            return extension;
        }

        try {
            String[] pathContents = path.split("[\\\\/]");
            int pathContentsLength = pathContents.length;
            String lastPart = pathContents[pathContentsLength-1];
            String[] lastPartContents = lastPart.split("\\.");
            if(lastPartContents.length > 1){
                int lastPartContentLength = lastPartContents.length;
                //extension
                extension = lastPartContents[lastPartContentLength -1];
            }
        }catch (Exception exception){
            BurpExtender.stderr.println(String.format("[*] GetPathExtension [%s] Occur Error [%s]", path, exception.getMessage()));
        }
        return extension;
    }


    /**
     * 判断指定的HTTP请求方法是否应该包含请求体
     *
     * @param method HTTP请求方法（如"GET", "POST"等）
     * @return 如果该请求方法应该包含请求体返回true，否则返回false
     */
    public static boolean shouldBeHasBody(String method) {
        // 定义不应该包含请求体的HTTP方法列表
        List<String> noBodyMethods = Arrays.asList("get", "head", "delete", "trace");
        // 如果方法在不包含请求体的列表中，返回false
        if (noBodyMethods.contains(method.toLowerCase())) {
            return false;
        }
        // 其他方法（如POST, PUT, PATCH等）通常应该包含请求体
        return true;
    }

}
