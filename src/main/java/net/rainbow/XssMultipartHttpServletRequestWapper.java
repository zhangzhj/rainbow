package net.rainbow;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.rainbow.utils.XssUtils;

import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

/**
 * TODO
 * 
 * @author (sean)zhang.zhj85@gmail.com
 * @date 2013-8-2
 * @version V1.0
 */
public class XssMultipartHttpServletRequestWapper extends DefaultMultipartHttpServletRequest {

    public XssMultipartHttpServletRequestWapper(HttpServletRequest request, Map<?,?> multipartFiles,
            Map<?,?> multipartParameters) {
        super(request, multipartFiles, multipartParameters);
    }

    /**
     * <p>
     * Title:
     * </p>
     * <p>
     * Description: TODO
     * </p>
     * 
     * @param request
     */
    public XssMultipartHttpServletRequestWapper(HttpServletRequest request) {
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
