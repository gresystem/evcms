package kr.co.watchpoint.evcms.utils;

import org.springframework.context.ApplicationContext;

import kr.co.watchpoint.evcms.ApplicationContextProvider;

public class BeanUtils {
 
    public static Object getBean(String beanName) {
        ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
        return applicationContext.getBean(beanName);
    }
}

