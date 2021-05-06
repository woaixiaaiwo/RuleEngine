package com.execution.engine.rule;

import com.greenpineyu.fel.FelEngineImpl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RuleRegister {

    private final static Map<String,Rule> MAP = new HashMap<>();

    static {
        register(new SimpleRule("adult","person.age>=18"));
        register(new SimpleRule("noAdult","person.age<18"));
        register(new SimpleRule("male","person.sex == 1"));
        register(new SimpleRule("female","person.sex == 2"));
        register(new SimpleRule("bronze","person.score <= 2000"));
        register(new SimpleRule("silver","person.score > 2000 && person.score <= 4000"));
        register(new SimpleRule("gold","person.score > 4000  && person.score <= 6000"));
        register(new SimpleRule("platinum","person.score > 6000 && person.score <= 8000"));
        register(new SimpleRule("diamonds","person.score>=8000 && person.score < 10000"));
        register(new SimpleRule("TheStrongestKing","person.score >= 10000"));
        register(new SimpleRule("desEqualsNull","person.des == null"));
        register(new SimpleRule("desEqualsData","'123'.equals(person.des)"));
        register(new MethodRule("method",false,"test1","t2"));
    }

    public static void register(Rule rule){
        MAP.put(rule.getName(),rule);
    }

    public static Rule getRule(String ruleName){
        return MAP.get(ruleName);
    }

    public static void main(String[] args) {
        /*RuleContext ruleContext = new RuleContext();
        ruleContext.addBeans("person",new Person(17,1,100,"123"));
        ruleContext.addBeans("test1",new Test1());
        RuleGroupExecutor<String> ruleGroupExecutor = new RuleGroupExecutor(1,ruleContext);
        ruleGroupExecutor.addRuleExecutor(new RuleExecutor("rule1", "adult&&male&&(TheStrongestKing||diamonds)", "\"男性高手\""));
        ruleGroupExecutor.addRuleExecutor(new RuleExecutor("rule2", "adult&&female&&(TheStrongestKing||diamonds)", "\"女性高手\""));
        ruleGroupExecutor.addRuleExecutor(new RuleExecutor("rule3", "noAdult&&male&&(TheStrongestKing||diamonds)", "\"男孩子高手\""));
        ruleGroupExecutor.addRuleExecutor(new RuleExecutor("rule4", "noAdult&&female&&(TheStrongestKing||diamonds)", "\"女孩子高手\""));
        ruleGroupExecutor.addRuleExecutor(new RuleExecutor("rule5", "adult&&male&&!(TheStrongestKing||diamonds)", "\"男性菜鸟\""));
        ruleGroupExecutor.addRuleExecutor(new RuleExecutor("rule6", "adult&&female&&!(TheStrongestKing||diamonds)", "\"女性菜鸟\""));
        ruleGroupExecutor.addRuleExecutor(new RuleExecutor("rule7", "method&&noAdult&&male&&!(TheStrongestKing||diamonds)", false,"test1","t1"));
        ruleGroupExecutor.execute();
        String result = ruleGroupExecutor.getResult();
        System.out.println(result);*/
        FelEngineImpl felEngine = new FelEngineImpl();
        System.out.println(felEngine.eval("nonAdult"));
    }

}
