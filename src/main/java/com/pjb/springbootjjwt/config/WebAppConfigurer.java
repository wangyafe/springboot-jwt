package com.pjb.springbootjjwt.config;

import com.pjb.springbootjjwt.annotation.Permission;
import com.pjb.springbootjjwt.interceptor.AuthenticationInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @program: springboot-jjwt
 * @author: Bell
 * @create: 2020-04-17 10:45
 **/
@Configuration
public class WebAppConfigurer implements WebMvcConfigurer, ApplicationContextAware {

    private static Logger logger = LoggerFactory.getLogger(WebAppConfigurer.class);

    // 验证Token拦截器
    @Autowired
    AuthenticationInterceptor tokenInterceptor;
 // SpringBoot上下文
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // 通过实现ApplicationContextAware来获取SpringBoot上下文
        this.applicationContext = applicationContext;
    }

 // 循环添加到set中　
    private void addExcludePath(Set<String> excludePath, String[] prefixValues, String[] suffixValues) {
        if (suffixValues == null) suffixValues = new String[]{""};
        for (String prefix : prefixValues) {
            for (String suffix : suffixValues) {
                String path = "/" + prefix;
                if (suffix.length() != 0) {
                    path += "/" + suffix;
                }
                excludePath.add(path.replaceAll("/+", "/"));
            }
        }
    }

 // 获取不需要Token验证的path列表
    private List<String> listExcludePath() {
        // 获取有添加RestController的Bean
        Map<String, Object> controllerBeanMap = applicationContext.getBeansWithAnnotation(Controller.class);
        Set<String> excludePath = new HashSet<>();
        try {
            for (Object controller : controllerBeanMap.values()) {
                Class<?> controllerClass = controller.getClass();
                RequestMapping controllerMapping = controllerClass.getAnnotation(RequestMapping.class);
                // 获取url的前缀
                String[] prefixValues = {""};
                if (controllerMapping != null) {
                    prefixValues = controllerMapping.value();
                }
                // 获取当前类的方法，不含父类方法
                Method[] controllerMethods = controllerClass.getMethods();
                for (Method method : controllerMethods) {
                    // 判断是否加了Permission注解
                    Permission permission = method.getAnnotation(Permission.class);
                    if (permission != null) {
                        // 获取所有注解
                        Annotation[] annotations = method.getAnnotations();
                        for (Annotation annotation : annotations) {
                            // 获取RequestMapping、GetMapping...等的value值
                            Class<? extends Annotation> type = annotation.annotationType();
                            if (type.isAnnotationPresent(RequestMapping.class)
                                    || type.isAnnotationPresent(Mapping.class)) {
                                // 反射调用value()方法，获取内容
                                Method valueMethod = type.getDeclaredMethod("value");
                                String[] value = (String[]) valueMethod.invoke(annotation);
                                addExcludePath(excludePath, prefixValues, value);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("加载permission异常", e);
        }
        return new ArrayList<>(excludePath);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        if (tokenInterceptor != null) {
            // 获取不需要token的url列表
            List<String> excludePathList = listExcludePath();

            registry.addInterceptor(tokenInterceptor)
                    .addPathPatterns("/**")
                    .excludePathPatterns(excludePathList);
        }

    }

}
