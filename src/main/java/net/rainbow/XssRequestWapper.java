package net.rainbow;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import net.rainbow.utils.XssUtils;

/**
 * 过滤所有的参数,,,,用于垃圾不好好些代码的，真是操碎了心啊
 * 
 * 文件上传的暂时先不处理了,
 * 
 * @author (sean)zhang.zhj85@gmail.com
 * @date 2013-4-26
 * @version V1.0
 */
public class XssRequestWapper extends HttpServletRequestWrapper {

    public XssRequestWapper(HttpServletRequest request) {
        super(request);
    }

    /**
     * (非 Javadoc)
     * <p>
     * Title: getParameter
     * </p>
     * <p>
     * Description: TODO
     * </p>
     * 
     * @param arg0
     * @return
     * @see javax.servlet.ServletRequestWrapper#getParameter(java.lang.String)
     */
    @Override
    public String getParameter(String key) {
        String tmp = super.getParameter(key);
        return XssUtils.getSafeStringXSS(tmp);
    }

    /**
     * (非 Javadoc)
     * <p>
     * Title: getParameterValues
     * </p>
     * <p>
     * Description: TODO
     * </p>
     * 
     * @param arg0
     * @return
     * @see javax.servlet.ServletRequestWrapper#getParameterValues(java.lang.String)
     */
    @Override
    public String[] getParameterValues(String key) {
        String[] results = super.getParameterValues(key);
        if (results != null) {
            for (int i = 0; i < results.length; i++) {
                results[i] = XssUtils.getSafeStringXSS(results[i]);
            }
        }
        return results;
    }

    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        if (value != null) {
            value = XssUtils.getSafeStringXSS(value);
        }
        return value;
    }

}
