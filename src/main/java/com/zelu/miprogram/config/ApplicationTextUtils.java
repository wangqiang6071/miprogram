package com.zelu.miprogram.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationTextUtils implements ApplicationContextAware{

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context=applicationContext;
    }
    //传入注入SpringBoot容器中的类对象 传入对象名字 首字母小写
    public static Object getBeanObj(String BeanName){
        return context.getBean(BeanName);
    }
}
