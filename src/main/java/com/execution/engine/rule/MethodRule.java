package com.execution.engine.rule;

import com.execution.engine.RuleContext;
import com.execution.engine.executor.MethodExecutor;

import java.util.ArrayList;
import java.util.List;

public class MethodRule extends Rule{

    //是否spring对象
    private Boolean isSpringBean;

    //对象名称
    private String beanName;

    //方法名称
    private String methodName;

    //参数列表
    private List<String> paramList;

    //参数类型列表
    private Class[] paramClass;

    public MethodRule(String name, Boolean isSpringBean, String beanName, String methodName) {
        this(name,isSpringBean,beanName,methodName,new ArrayList(),null);
    }

    public MethodRule(String name, Boolean isSpringBean, String beanName, String methodName, List<String> paramList, Class[] paramClass) {
        super(name);
        this.isSpringBean = isSpringBean;
        this.beanName = beanName;
        this.methodName = methodName;
        this.paramList = paramList;
        this.paramClass = paramClass;
    }

    @Override
    public boolean fit(RuleContext ruleContext) {
        MethodExecutor<Boolean> methodExecutor = new MethodExecutor(isSpringBean,beanName,methodName,paramList,paramClass);
        Object res = methodExecutor.execute(ruleContext);
        if(res == null){
            throw new RuntimeException("调用方法返回值为空，方法名："+methodName+"，参数路径："+ paramList);
        }
        if(!(res instanceof Boolean)){
            throw new RuntimeException("方法返回值不是布尔类型，方法名："+beanName+"."+methodName+"，参数路径："+ paramList);
        }
        return (Boolean) res;
    }
}
