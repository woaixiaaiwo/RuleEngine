package com.execution.engine;

import com.greenpineyu.fel.FelEngineImpl;
import com.greenpineyu.fel.context.FelContext;

public class RuleContext {

    ExecuteEngine executeEngine;

    public RuleContext() {
        this.executeEngine = new ExecuteEngine(new FelEngineImpl());
    }

    public void addBeans(String beanName,Object bean){
        FelContext context = executeEngine.getContext();
        context.set(beanName,bean);
    }

    public Object eval(String exp){
        return executeEngine.eval(exp);
    }

    public Object compile(String exp){
        return executeEngine.compile(exp);
    }
}
