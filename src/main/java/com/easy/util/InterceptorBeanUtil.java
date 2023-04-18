package com.easy.util;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author tanyongpeng
 * <p>des</p>
 **/
public class InterceptorBeanUtil<T> {

    public static <T> T getBean(Class<T> cls, HttpServletRequest request) {
        WebApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
        return applicationContext.getBean(cls);
    }

}
