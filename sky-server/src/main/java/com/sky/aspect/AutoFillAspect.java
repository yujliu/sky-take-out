package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.entity.Employee;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Locale;

@Aspect
@Component
@Slf4j
public class AutoFillAspect {

    //返回值任意 拦截该路径下的类的所有方法（所有参数类型） && 带这个注解
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointCut(){}

    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        //获取被拦截方法的OperationType
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);
        OperationType operationType = autoFill.value();

        //获取被拦截方法的参数
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0){return;}
        Object entity = args[0];//传入的实体对象

        if (operationType == OperationType.INSERT){

            Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
            Method setUpdateTime = entity.getClass().getDeclaredMethod("setUpdateTime", LocalDateTime.class);
            Method setCreateUser = entity.getClass().getDeclaredMethod("setCreateUser", Long.class);
            Method setUpdateUser = entity.getClass().getDeclaredMethod("setUpdateUser", Long.class);

            //method.invoke(传入的实体，parameter)
            setCreateTime.invoke(entity,LocalDateTime.now());
            setUpdateTime.invoke(entity,LocalDateTime.now());
            setCreateUser.invoke(entity, BaseContext.getCurrentId());
            setUpdateUser.invoke(entity,BaseContext.getCurrentId());

        }else if(operationType == OperationType.UPDATE){
            Method setUpdateTime = entity.getClass().getDeclaredMethod("setUpdateTime", LocalDateTime.class);
            Method setUpdateUser = entity.getClass().getDeclaredMethod("setUpdateUser", Long.class);
            setUpdateTime.invoke(entity,LocalDateTime.now());
            setUpdateUser.invoke(entity,BaseContext.getCurrentId());
        }

    }
}
