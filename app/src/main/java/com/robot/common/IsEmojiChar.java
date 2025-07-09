package com.robot.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 判断是否含有emoji表情或者特殊字符的工具类
 *
 * @author lizhibin
 */
public class IsEmojiChar {

    /**
     * 判断是否含有emoji表情
     *
     * @param content
     * @return
     */
    public static boolean hasEmoji(String content) {
        Pattern pattern = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]"); //判断含有emoj表情的表达式
        Matcher matcher = pattern.matcher(content);
        return matcher.find();
    }

    public static String filterEmoji(String source, String slipStr) {
        if (null != source) {
            return source.replaceAll("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]", slipStr);
        } else {
            return source;
        }
    }

    /**
     * 判断是否含有特殊字符
     *
     * @param content
     * @return
     */
    public static boolean hasSpecialChar(String content) {
        Pattern p = Pattern.compile("[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]"); //判断含有特殊字符的正则
        Matcher m = p.matcher(content);
        return m.find();
    }
}
