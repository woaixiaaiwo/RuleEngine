package com.execution.engine.executor;

import com.execution.engine.RuleContext;
import com.execution.engine.SpringUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MethodExecutor<T> {

    private Boolean isSpringBean;

    private String beanName;

    private String methodName;

    private List<String> paramList;

    private Class[] paramClass;

    public MethodExecutor(Boolean isSpringBean, String beanName, String methodName) {
        this(isSpringBean,beanName,methodName,new ArrayList(),null);
    }

    public MethodExecutor(Boolean isSpringBean, String beanName, String methodName, List<String> paramList, Class[] paramClass) {
        this.isSpringBean = isSpringBean;
        this.beanName = beanName;
        this.methodName = methodName;
        this.paramList = paramList;
        this.paramClass = paramClass;
    }

    public T execute(RuleContext ruleContext){
        Object obj = isSpringBean? SpringUtil.getBean(beanName):ruleContext.eval(beanName);
        Object[] params = new Object[paramList.size()];
        for(int i=0;i<paramList.size();i++){
            try {
                params[i] = ruleContext.eval(paramList.get(i));
            }catch (Exception e){
                if(e instanceof NullPointerException){
                    params[i] = null;
                }else {
                    throw new RuntimeException("获取参数值失败，参数路径："+paramList.get(i));
                }
            }
        }
        Method method = null;
        Class<?> objClass = obj.getClass();
        try {
            if(paramClass != null){
                method = objClass.getDeclaredMethod(methodName,paramClass);
            }else {
                Method[] declaredMethods = objClass.getDeclaredMethods();
                for(int i=0;i<declaredMethods.length;i++){
                    if(declaredMethods[i].getName().equals(methodName) && declaredMethods[i].getParameterCount() == paramList.size()){
                        method = declaredMethods[i];
                        break;
                    }
                }
            }
        }catch (Exception e){
            throw new RuntimeException("获取指定方法异常："+methodName);
        }
        if(method == null){
            throw new RuntimeException("未找到指定方法："+methodName);
        }
        T res;
        try {
            res = (T) method.invoke(obj, params);
        }catch (Exception e){
            throw new RuntimeException("调用方法异常，方法名："+methodName+"，参数："+ Arrays.toString(params));
        }
        return res;
    }
}
