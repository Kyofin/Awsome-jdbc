//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.github.huzekang.jdbcservice.util;

import cn.hutool.core.lang.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
@Lazy(false)
public class SpringContextHolder implements ApplicationContextAware, DisposableBean {
    private static final Logger log = LoggerFactory.getLogger(SpringContextHolder.class);
    private static ApplicationContext applicationContext = null;

    public SpringContextHolder() {
    }

    public static ApplicationContext getApplicationContext() {
        assertContextInjected();
        return applicationContext;
    }

    public static <T> T getBean(String name) {
        assertContextInjected();
        return (T) applicationContext.getBean(name);
    }

    public static <T> T getBean(Class<T> requiredType) {
        assertContextInjected();
        return applicationContext.getBean(requiredType);
    }

    public static void clearHolder() {
        if (log.isDebugEnabled()) {
            log.debug("清除SpringUtils中的ApplicationContext:" + applicationContext);
        }

        applicationContext = null;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        applicationContext = applicationContext;
    }

    public void destroy() throws Exception {
        clearHolder();
    }

    private static void assertContextInjected() {
        Assert.isTrue(applicationContext != null, "applicaitonContext属性未注入", new Object[]{""});
    }
}
