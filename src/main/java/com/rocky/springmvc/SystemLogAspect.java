package com.rocky.springmvc;

import org.apache.commons.logging.Log;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 切点类
 */
@Aspect
@Component
public class SystemLogAspect {

    private static final Logger logger = LoggerFactory.getLogger(SystemLogAspect.class);

    // Controller层切点
    @Pointcut("@annotation(com.rocky.springmvc.SystemControllerLog)")
    public void controllerAspect() {
    }

    /**
     * 前置通知 用于拦截Controller层记录用户的操作
     *
     * @param joinPoint 切点
     */
    @Before("controllerAspect()")
    public void doBefore(JoinPoint joinPoint) {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        // 读取session中的用户
        // 请求的IP
        String ip = request.getRemoteAddr();
        try {
            // *========控制台输出=========*//
            System.out.println("=====前置通知开始=====");
            System.out.println("请求方法:"
                    + (joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()"));
            System.out.println("方法描述:" + getControllerMethodDescription(joinPoint));
            System.out.println("请求人:" + "XXX");
            System.out.println("请求IP:" + ip);
            // *========数据库日志=========*//
            System.out.println("=====前置通知结束=====");
        } catch (Exception e) {
            // 记录本地异常日志
            logger.error("==前置通知异常==");
            logger.error("异常信息:{}", e.getMessage());
        }
    }

    @Around("controllerAspect()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String name=request.getParameter("name");
        Object o=pjp.proceed();
        System.out.println(o);
        o="result:"+o;
        System.out.println("环绕环绕");
        System.out.println("参数"+name);
        return o;
    }



    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     *
     * @param joinPoint 切点
     * @return 方法描述
     * @throws Exception
     */
    public static String getControllerMethodDescription(JoinPoint joinPoint) throws Exception {
        //目标类名
        String targetName = joinPoint.getTarget().getClass().getName();
        System.out.println("类名：" + targetName);
        //目标方法名
        String methodName = joinPoint.getSignature().getName();
        System.out.println("方法名：" + methodName);
        //参数
        Object[] arguments = joinPoint.getArgs();
        StringBuilder params=new StringBuilder("参数：");
        for (int i = 0; i < arguments.length; i++) {
              params=params.append(arguments[i]).append(",");
        }
        System.out.println(params);
        //
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        //方法注解的描述
        String description = "";
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length == arguments.length) {
                    description = method.getAnnotation(SystemControllerLog.class).description();
                    break;
                }
            }
        }
        return description;
    }
}
