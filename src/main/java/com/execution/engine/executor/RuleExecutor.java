package com.execution.engine.executor;

import com.execution.engine.RuleContext;
import com.execution.engine.rule.Rule;
import com.execution.engine.rule.RuleRegister;
import com.greenpineyu.fel.FelEngineImpl;
import com.greenpineyu.fel.context.FelContext;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RuleExecutor<T> {

    //规则表达式名称
    private String name;

    //规则表达式定义
    private String ruleExpression;

    //规则map
    private Map<String,Rule> ruleMap;

    //返回值表达式
    private String valueExpression;

    //返回方法模式，方法对应的对象是否为spring对象
    private Boolean isSpringBean;

    //返回方法模式，方法对应的对象
    private String beanName;

    //返回方法模式，对应的方法名
    private String methodName;

    //返回方法模式，方法参数
    private List<String> paramList;

    //返回方法模式，方法参数类型数组
    private Class[] paramClass;

    public RuleExecutor(String name, String ruleExpression,  String valueExpression) {
        this(name,ruleExpression,valueExpression,null,null,null,new ArrayList<>(),null);
    }

    public RuleExecutor(String name, String ruleExpression, Boolean isSpringBean, String beanName, String methodName) {
        this(name,ruleExpression,null,isSpringBean,beanName,methodName,new ArrayList<>(),null);
    }

    public RuleExecutor(String name, String ruleExpression, String valueExpression, Boolean isSpringBean, String beanName, String methodName, List<String> paramList, Class[] paramClass) {
        this.name = name;
        this.ruleExpression = ruleExpression;
        this.valueExpression = valueExpression;
        this.isSpringBean = isSpringBean;
        this.beanName = beanName;
        this.methodName = methodName;
        this.paramList = paramList;
        this.paramClass = paramClass;
        init();
    }

    private void init(){
        String[] split = ruleExpression.split("\\!|\\&\\&|\\|\\||\\(|\\)");
        ruleMap = new HashMap<>();
        for(String str:split){
            if(str != null && !"".equals(str.trim())){
                Rule rule = RuleRegister.getRule(str.trim());
                ruleExpression = StringUtils.replace(ruleExpression,str,str+".fit(ruleContext)");
                ruleMap.put(rule.getName(),rule);
            }
        }
    }

    public boolean executeRule(RuleContext ruleContext){
        FelEngineImpl newFel = new FelEngineImpl();
        FelContext context = newFel.getContext();
        context.set("ruleContext",ruleContext);
        for(Map.Entry<String,Rule> entry:ruleMap.entrySet()){
            context.set(entry.getKey(),entry.getValue());
        }
        return (boolean)newFel.eval(ruleExpression);
    }

    public T getResult(RuleContext ruleContext){
        if(valueExpression != null){
            return (T)ruleContext.eval(valueExpression);
        }else {
            MethodExecutor<T> methodExecutor = new MethodExecutor(isSpringBean,beanName,methodName,paramList,paramClass);
            return methodExecutor.execute(ruleContext);
        }
    }

}
