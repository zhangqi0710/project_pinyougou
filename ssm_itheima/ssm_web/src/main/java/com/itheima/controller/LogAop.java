package com.itheima.controller;

import com.itheima.domain.SysLog;
import com.itheima.service.SysLogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
@Aspect
public class LogAop {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private SysLogService sysLogService;
    /**
     * 环绕通知，收集并且保存日志
     */
    @Around("execution(* com.itheima.controller.*.*(..))")
    public Object recordLog(ProceedingJoinPoint pjp) throws Throwable {
        SysLog sysLog = new SysLog();
        //1.收集日志
        //访问时间
        long visitTime = System.currentTimeMillis();
        sysLog.setVisitTime(new Date(visitTime));
        //操作用户
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        sysLog.setUsername(user.getUsername());
        //访问IP
        String ip = request.getRemoteAddr();
        sysLog.setIp(ip);
        //访问URL
        String uri = request.getRequestURI();
        sysLog.setUrl(uri);
        //执行目标方法,并且计算目标方法执行时长
        Object result = pjp.proceed();
        long endTime = System.currentTimeMillis();
        sysLog.setExecutionTime(endTime-visitTime);
        //访问类名+方法名
        String className = pjp.getTarget().getClass().getName();
        String methodName = pjp.getSignature().getName();
        sysLog.setMethod(className+methodName);

        //2.保存日志
        sysLogService.save(sysLog);
        return result;
    }

}
