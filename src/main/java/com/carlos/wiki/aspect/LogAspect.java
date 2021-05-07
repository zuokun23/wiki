package com.carlos.wiki.aspect;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.support.spring.PropertyPreFilters;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

@Aspect
@Component//交给spring来管理
public class LogAspect {

    private static final Logger LOG = LoggerFactory.getLogger(LogAspect.class);

    /**
     * 第一个*表示所有的返回值void int...
     * 第二个*表示如果有新项目com.carlos.blog就不用修改
     * .*Controller表示TestController、DemoController..
     * 最后一个*表示所有的方法
     * ..表示使用任意参数
     */
    @Pointcut("execution(public * com.carlos.*.controller..*Controller.*(..))")
    public void controllerPointcut(){}

    @Before("controllerPointcut()")
    public void doBefore(JoinPoint joinPoint){
        //下面是很经典的从非controller类中获取request的方法
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        //得到joinPoint的相关信息
        Signature signature = joinPoint.getSignature();
        String name = signature.getName();

        //打印请求信息
        LOG.info("------------------开始-----------------");
        LOG.info("请求地址：{} {}", request.getRequestURL().toString(), request.getMethod());
        LOG.info("类名方法：{} {}", signature.getDeclaringTypeName(), name);
        LOG.info("远程地址：{}", request.getRemoteAddr());

        // 打印请求参数
        Object[] args = joinPoint.getArgs();
//        LOG.info("请求参数：{}", JSONObject.toJSONString(args));
        Object[] arguments = new Object[args.length];
        for (int i = 0 ; i < args.length ; i++) {
            if(args[i] instanceof ServletRequest
                    || args[i] instanceof ServletResponse
                    || args[i] instanceof MultipartFile){
                continue;
            }
            arguments[i] = args[i];
        }
        //排除字段，敏感字段或太长的字段不显示，比如文件内容太长
        String[] excludeProperties = {"password", "file"};
        PropertyPreFilters filters = new PropertyPreFilters();
        PropertyPreFilters.MySimplePropertyPreFilter excludeFilter = filters.addFilter();
        excludeFilter.addExcludes(excludeProperties);
        LOG.info("请求参数：{}", JSONObject.toJSONString(arguments, excludeFilter));
    }

    @Around("controllerPointcut()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        //业务逻辑
        Object result = proceedingJoinPoint.proceed();
        //排除字段，敏感字段或太长的字段不显示
        String[] excludeProperties = {"password", "file"};
        PropertyPreFilters filters = new PropertyPreFilters();
        PropertyPreFilters.MySimplePropertyPreFilter mySimplePropertyPreFilter = filters.addFilter();
        LOG.info("返回结果：{}", JSONObject.toJSONString(result, mySimplePropertyPreFilter));
        LOG.info("------------------结束耗时：{}ms-----------------", System.currentTimeMillis() - startTime);
        return result;
    }
}
