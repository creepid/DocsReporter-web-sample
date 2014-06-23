/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package by.creepid.docgeneration;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 *
 * @author rusakovich
 */
@Component
public final class ContextHelper implements ApplicationContextAware {

    private static volatile ApplicationContext context;

    private ContextHelper() {
    }

    @Override
    public void setApplicationContext(ApplicationContext context) {
        ContextHelper.context = context;
    }

    public static <T> T getbean(Class<T> requiredType) {
        return context.getBean(requiredType);
    }

    public static <T> T getBean(String string, Class<T> type) {
        return context.getBean(string, type);
    }

}
