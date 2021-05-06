# RuleEngine
简单的规则引擎，用于大量if判断场景下提高代码可读性和可拓展性。基于Fel表达式计算引擎和Spring容器
##解决痛点：
日常开发过程中，随着业务逻辑的变化或者需求变更，通常会出现一些逻辑判断的代码。若逻辑判断分支过多，就会影响代码的
可读性和拓展性。为了解决这个问题，通常有如下处理办法：  

1.将if判断改成switch判断，并且将每个分支的代码单独提出作为一个方法执行。  
优点：一定程度上提高了代码可读性。  
缺点：没有解决拓展性问题，添加新的分支有可能导致bug。  
  
2.使用表驱动法，新维护一个map结构，将if判断条件作为key值，执行的逻辑单独封装成对象，作为value值存储  
优点：提高了代码可读性和可拓展性，有新的条件分支时在map中添加即可  
缺点：该方法只适合判断逻辑简单的场景   
 
3.使用简单工厂+策略模式，定义策略接口，执行的逻辑实现策略接口。策略的选择在工厂类中进行，可以结合表驱动法  
优点：提高了代码可读性和可拓展性，可以支持复杂的判断逻辑：根据传入的参数，在工厂类中执行判断逻辑，获取不同的策略接口
实现类  
缺点：复杂度高，对开发人员和维护人员有一定要求      

为了解决上述情况中的缺点，设计了一套傻瓜式的规则引擎执行器，可以定义任意规则，使用与、或、非等逻辑计算对规则进行任意组合，形成
规则表达式，引擎对规则表达式进行求值并执行规则表达式对应的动作。开发人员只要定义好规则、规则表达式和对应的执行逻辑即可
## 使用方法
1.引擎上下文
要使用规则引擎，首先要定义引擎上下文环境，即引擎可以使用的对象或者参数。引擎上下文对应的类是RuleContext类。使用引擎之前，要将
后续使用的对象通过addBeans方法注册到引擎中：
```
RuleContext ruleContext = new RuleContext();
ruleContext.addBeans("person",new Person(17,1,100,"123"));
```
addBeans的第一个参数代表被注册对象的名称，第二个参数为实际对象。当对象注册好之后，引擎就可以使用它了
  
2.定义规则  
规则分为简单规则和方法规则。  
简单规则使用表达式定义，对应类为SimpleRule。比如以下表达式：
```
new SimpleRule("adult","person.age>=18")
```
SimpleRule构造器的第一个参数为规则名称，*_注意，所有的规则名称都不能重复_*  
其中person为引擎上下文中对象名称（注意：这里的对象名称必须与注册到引擎上下文中的对象名称一样，否则执行过程中
可能会报NPE），age为person对象的字段名，这个规则的意思就是注册到引擎的person对象的age值>=18才会符合该规则  

方法规则使用通过对象名称和方法名定义，比如以下规则：
```
new MethodRule("method",false,"test1","t2")
```
该规则会通过test1对象的t2方法的返回值判断，这是一个无参数的方法，可以通过下面的方式定义有参数方法的规则：
```
new MethodRule("method",false,"test1","t2", Arrays.asList("person.age","person.sex"),new Class[]{Integer.class,Integer.class})
```
该规则会通过test1对象的t2方法的返回值判断，方法的参数为person对象的age字段值和sex字段值。注意，这里的参数都是从上下文中定义的对象中获取的

注册规则  
规则定义好之后，要进行规则注册，使用RuleRegister.register方法注册规则
```
RuleRegister.register(new SimpleRule("adult","person.age>=18"));
```

3.定义规则表达式

```
new RuleExecutor("rule", "noAdult&&male&&!(TheStrongestKing||diamonds)", false,"test1","t1")
```
RuleExecutor构造器的第一个参数为规则表达式名称（暂时未用到，但是最好不要重复），第二个参数为规则表达式，将多个规则的名称通过||、&&、！和左右括号拼接在一起，即构成了
规则表达式。
当规则表达式成立时，引擎会执行对应的逻辑，逻辑定义分为两种：返回表达式模式和返回方法模式

使用返回表达式模式时，通过下面的方式定义表达式：
```
//返回1+2*3的计算结果
new RuleExecutor("rule", "method&&noAdult&&male&&!(TheStrongestKing||diamonds)", "1+2*3")
```
上面的方式表示如果规则成立，引擎会返回表达式1+2*3的结果。如果需要返回静态的字符串，将表达式用单引号括起来即可：
```
//返回1+2*3字符串本身
new RuleExecutor("rule", "method&&noAdult&&male&&!(TheStrongestKing||diamonds)", "'1+2*3'")
```

使用返回方法模式时，通过下面的方式定义表达式
```
new RuleExecutor("rule", "method&&noAdult&&male&&!(TheStrongestKing||diamonds)", false,"test1","t1")
```
test1为对象名称，t1为方法名称。当表达式成立时，引擎会返回test1对象的t1方法的执行结果。这里是无参数的方法，有参数的方法定义参考方法规则
第三个参数false的意思代表test1对象要从引擎上下文中获取，如果是true的话，引擎会从Spring容器中通过对象名称获取对象

4.定义规则表达式组  

最后，要定义一组规则表达式来实现业务逻辑。对应的类为RuleGroupExecutor，通过它的addRuleExecutor方法来添加一组规则表达式：
```
RuleGroupExecutor<String> ruleGroupExecutor = new RuleGroupExecutor(1,ruleContext);
ruleGroupExecutor.addRuleExecutor(new RuleExecutor("rule1", "method&&noAdult&&male&&!(TheStrongestKing||diamonds)", false,"test1","t1"));
ruleGroupExecutor.addRuleExecutor(new RuleExecutor("rule2", "method&&noAdult&&male&&!(TheStrongestKing||diamonds)", false,"test1","t2"));
```
RuleGroupExecutor的构造器有两个参数，第一个参数是规则表达式组的执行类型，有互斥方式（1）和组合方式（2）。互斥方式表示所有的规则表达式中遇到第一个成立的就会中断。组合模式会判断所有
的表达式，只要成立的话就会执行表达式对应的逻辑。  
第二个参数即引擎上下文。  
规则组添加完毕后，调用execute方法开始计算  
若需要计算结果，可以执行getResult方法获取。（组合模式下的计算结果为最后一个表达式成立时计算的结果，后续可以优化为返回map结构）

5.简单demo
```
RuleContext ruleContext = new RuleContext();
ruleContext.addBeans("person",new Person(17,1,100,"123"));
ruleContext.addBeans("test1",new Test1());
RuleGroupExecutor<String> ruleGroupExecutor = new RuleGroupExecutor(1,ruleContext);
ruleGroupExecutor.addRuleExecutor(new RuleExecutor("rule1", "method&&noAdult&&male&&!(TheStrongestKing||diamonds)", false,"test1","t1"));
ruleGroupExecutor.addRuleExecutor(new RuleExecutor("rule2", "method&&adult&&male&&!(TheStrongestKing||diamonds)", false,"test1","t3"));
ruleGroupExecutor.execute();
String result = ruleGroupExecutor.getResult();
System.out.println(result);
```

6.后续优化

1.组合模式下的计算结果可以优化为返回map结构  
2.相同对象，相同方法，相同参数的执行结果添加缓存