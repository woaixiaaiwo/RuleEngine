package com.execution.engine;

import com.greenpineyu.fel.Expression;
import com.greenpineyu.fel.FelEngine;
import com.greenpineyu.fel.context.FelContext;

public class ExecuteEngine {

    private FelEngine fel;

    public ExecuteEngine(FelEngine fel) {
        this.fel = fel;
    }

    public Object eval(String exp){
        try {
          return this.fel.eval(exp);
        }catch (Exception e){
            if(e instanceof NullPointerException) throw e;
            if(e instanceof SecurityException){
                throw new RuntimeException("禁止调用不安全方法");
            }
            throw new RuntimeException("表达式错误");
        }
    }

    public Object compile(String exp){
        try {
            FelContext context = fel.getContext();
            Expression expression = fel.compile(exp,context);
            return expression.eval(context);
        }catch (Exception e){
            if(e instanceof NullPointerException) throw e;
            throw new RuntimeException("表达式错误");
        }
    }

    public FelContext getContext(){
        return fel.getContext();
    }
}
