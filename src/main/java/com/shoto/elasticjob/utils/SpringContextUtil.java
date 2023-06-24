package com.shoto.elasticjob.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author admin
 */
@Component
public class SpringContextUtil implements ApplicationContextAware {

        private static ApplicationContext applicationContext;
        private static Long startTime = System.currentTimeMillis();
        private static String appName = "";

        public SpringContextUtil() {
        }

        @Override
        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            SpringContextUtil.applicationContext = applicationContext;
        }

        public static ApplicationContext getApplicationContext() {
            return applicationContext;
        }

        public static String getProperties(String key) {
            return applicationContext.getEnvironment().getProperty(key);
        }

        public static String getProperties(String key, String defaultValue) {
            return applicationContext.getEnvironment().getProperty(key, defaultValue);
        }

        public static <T> T getBean(Class<T> requiredType) {
            return applicationContext == null ? null : applicationContext.getBean(requiredType);
        }

        public static String getAppName() {
            return appName;
        }

        public static void setAppName(String appName) {
            SpringContextUtil.appName = appName;
        }

        public static Long getStartTime() {
            return startTime;
        }

        public static void setStartTime(Long startTime) {
            SpringContextUtil.startTime = startTime;
        }
    }

