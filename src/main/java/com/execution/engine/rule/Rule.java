package com.execution.engine.rule;
import com.execution.engine.RuleContext;

public abstract class Rule {

    private String name;

    public Rule(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public abstract boolean fit(RuleContext ruleContext);

}
