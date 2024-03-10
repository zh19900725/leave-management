package com.management.leave.filters;

import com.management.leave.common.util.CommonUtils;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.management.leave.common.constats.Constants.TRACE_ID;


/**
 * @author zh
 * Filter:
 * 1、add traceId to every request
 * 2、prevent xss attach
 */
@Component
public class RequestFilter implements Filter {

    public RequestFilter() {
        super();
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    /**
     * use do filter to add traceId to every request and prevent xss attach
     * @param servletRequest
     * @param servletResponse
     * @param filterChain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        try {
            // add traceId to every request
            String traceLogId = String.valueOf(System.currentTimeMillis());
            MDC.put(TRACE_ID, traceLogId + "-" + CommonUtils.getRandomStr(6, null));
            HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
            httpServletResponse.addHeader(TRACE_ID,MDC.get(TRACE_ID));

            // prevent xss attach
            HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
            XssRequestWrapper xssRequestWrapper = new XssRequestWrapper(httpRequest);

            filterChain.doFilter(xssRequestWrapper, servletResponse);
        } catch (Exception e) {
            throw e;
        } finally {
            MDC.remove(TRACE_ID);
        }
    }

    @Override
    public void destroy() {
    }
}
