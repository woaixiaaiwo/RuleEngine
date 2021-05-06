package com.execution.engine.executor;

import com.execution.engine.RuleContext;

import java.util.ArrayList;
import java.util.List;

public class RuleGroupExecutor<T> {

    private String name;

    List<RuleExecutor> ruleExecutors;

    /**
     * 1：互斥；2：组合
     */
    private Integer type;

    private RuleContext ruleContext;

    private T result;

    public RuleGroupExecutor(Integer type, RuleContext ruleContext) {
        this.ruleExecutors = new ArrayList<>();
        this.type = type;
        this.ruleContext = ruleContext;
    }

    public void addRuleExecutor(RuleExecutor<T> ruleExecutor){
        ruleExecutors.add(ruleExecutor);
    }

    public void execute(){
        for(RuleExecutor ruleExecutor:ruleExecutors){
            boolean res = ruleExecutor.executeRule(ruleContext);
            if(res){
                result = (T)ruleExecutor.getResult(ruleContext);
                if(type == 1){
                    break;
                }
            }
        }
    }

    public T getResult(){
        return result;
    }

}
