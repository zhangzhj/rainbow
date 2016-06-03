package net.rainbow.utils;

/**
 * TODO
 * 
 * @author (sean)zhang.zhj85@gmail.com
 * @date 2013-4-26
 * @version V1.0
 */
public class XssUtils {

    /**
     * 清除所有XSS攻击的字符串
     */
    public static String getSafeStringXSS(String s) {
        if (s == null || "".equals(s)) {
            return s;
        }
        StringBuilder sb = new StringBuilder(s.length() + 16);
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '<': // '<'
                    sb.append("&lt;");
                    break;
                case '>': // '>'
                    sb.append("&gt;");
                    break;
                case '′':  //'''
                    sb.append("&prime;");//  
                    break;
                case '＂':   
                    sb.append("&quot;");
                    break;
                case '"':
                    sb.append("&quot;");
                    break;
                case '&':
                    sb.append("& quot;");
                    break;
                case '\\': // '\\'
                    sb.append("&prime;");
                    break;
                default:
                    sb.append(c);
                    break;
            }
        }
        return sb.toString();
    }
}
