package com.execution.engine.rule;

import com.execution.engine.RuleContext;

public class SimpleRule extends Rule{

    private String expression;

    public SimpleRule(String name,String expression) {
        super(name);
        this.expression = expression;
    }

    @Override
    public boolean fit(RuleContext ruleContext) {
        Object eval = ruleContext.eval(expression);
        if(eval == null){
            throw new RuntimeException("表达式错误");
        }
        return (boolean)eval;
    }
}
