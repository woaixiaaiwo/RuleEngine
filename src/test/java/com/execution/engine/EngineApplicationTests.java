package com.execution.engine;

import com.execution.engine.executor.RuleExecutor;
import com.execution.engine.executor.RuleGroupExecutor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EngineApplicationTests {

	private final static RuleContext ruleContext = new RuleContext();
	static {
		ruleContext.addBeans("person",new Person(17,1,100,"123"));
		ruleContext.addBeans("test1",new Test1());
	}


	@Test
	void testMethod(){
		RuleGroupExecutor<String> ruleGroupExecutor = new RuleGroupExecutor(1,ruleContext);
		ruleGroupExecutor.addRuleExecutor(new RuleExecutor("rule", "method&&noAdult&&male&&!(TheStrongestKing||diamonds)", false,"test1","t1"));
		ruleGroupExecutor.execute();
		String result = ruleGroupExecutor.getResult();
		System.out.println(result);
	}

	@Test
	void testConstExpression(){
		RuleGroupExecutor<String> ruleGroupExecutor = new RuleGroupExecutor(1,ruleContext);
		ruleGroupExecutor.addRuleExecutor(new RuleExecutor("rule", "method&&noAdult&&male&&!(TheStrongestKing||diamonds)", "'123'"));
		ruleGroupExecutor.execute();
		String result = ruleGroupExecutor.getResult();
		System.out.println(result);
	}

	@Test
	void testCalculateExpression(){
		RuleGroupExecutor<Integer> ruleGroupExecutor = new RuleGroupExecutor(1,ruleContext);
		ruleGroupExecutor.addRuleExecutor(new RuleExecutor("rule", "method&&noAdult&&male&&!(TheStrongestKing||diamonds)", "1+2*3"));
		ruleGroupExecutor.execute();
		Integer result = ruleGroupExecutor.getResult();
		System.out.println(result);
	}
}
