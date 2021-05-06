package com.execution.parse;

import com.greenpineyu.fel.FelEngine;
import com.greenpineyu.fel.context.FelContext;
import org.springframework.util.StringUtils;

import java.util.Random;

public class Test {

    private static String generateRandomExpression(){
        Random random = new Random();
        int paramNum = random.nextInt(10)+2;
        StringBuilder sb = new StringBuilder();
        int num = 0;
        for(int i=0;i<paramNum;i++){
            boolean b;
            if(i != paramNum - 1){
                b = random.nextBoolean();
                if(b){
                    sb.append("!");
                }
                b = random.nextBoolean();
                if(random.nextBoolean()){
                    sb.append("(");
                    num++;
                }
                sb.append(b?"T":"F");
                if(num > 0 && random.nextBoolean()){
                    sb.append(")");
                    num--;
                }
                b = random.nextBoolean();
                sb.append(b?"&":"|");
            }else {
                b = random.nextBoolean();
                sb.append(b?"T":"F");
            }
        }
        while (num > 0){
            sb.append(")");
            num--;
        }
        return sb.toString();
    }

    private static String transfer(String origin){
        String result = StringUtils.replace(origin, "|", "||");
        result = StringUtils.replace(result, "&", "&&");
        result = StringUtils.replace(result, "T", "test.t(true,\"T\")");
        return StringUtils.replace(result, "F", "test.t(false,\"F\")");
    }

    private static void test(FelEngine fel){
        String s = generateRandomExpression();
        System.out.println("测试串："+s);
        String transfer = transfer(s);
        boolean r1 = Calculator.calculate(Parser.parse(s));
        System.out.println();
        System.out.println("------");
        FelContext ctx = fel.getContext();
        ctx.set("test",new TestMethod());
        boolean r2 = (boolean) fel.eval(transfer);
        boolean res = r1 == r2;
        if(!res){
            System.out.println("测试串："+s);
            System.out.println("转换串："+transfer);
            System.err.println("失败！！！");
            System.out.println("结果1："+r1+"，结果2："+r2);
            System.out.println("-------------------------------");
        }
    }

}

