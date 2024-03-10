package com.management.leave.filters;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * @author zh
 */
public class XssRequestWrapper extends HttpServletRequestWrapper {
    public XssRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getParameter(String name) {
        String value = super.getParameter(name);
        return cleanXss(value);
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if (values == null) {
            return null;
        }
        String[] result = new String[values.length];
        for (int i = 0; i< values.length; i++) {
            result[i] = cleanXss(values[i]);
        }
        return result;
    }

    private String cleanXss(String value) {
        if (value == null) {
            return null;
        }
        // 这里可以添加更多的过滤规则
        String cleanValue = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
        cleanValue = cleanValue.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
        cleanValue = cleanValue.replaceAll("'", "&#39;");
        cleanValue = cleanValue.replaceAll("eval\\((.*)\\)", "");
        cleanValue = cleanValue.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
        return cleanValue;
    }
}
