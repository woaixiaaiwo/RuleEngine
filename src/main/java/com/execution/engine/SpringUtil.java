package com.execution.engine;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringUtil implements ApplicationContextAware{

    private static ApplicationContext appContext;

    public static <T> T getBean(String beanName,Class<T> tClass){
        return appContext.getBean(beanName,tClass);
    }

    public static Object getBean(String beanName){
        return appContext.getBean(beanName);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        appContext = applicationContext;
    }
}
