package com.execution.parse;


import com.execution.parse.tree.*;

import java.util.*;

public class Parser {

    private final static Map<Character, Integer> OPERATOR = new HashMap(){{
        put('!',2);
        put('&',3);
        put('|',4);
        put('(',1);
        put(')',1);
    }};
    private final static char LEFT_BRACKET = '(';
    private final static char RIGHT_BRACKET = ')';

    //转成逆波兰表达式，再转成树
    public static BinaryTree parse(String str){
        int i=0,j=str.length();
        List<Character> result = new ArrayList();
        Stack<Character> operStack = new Stack();
        while (i < j){
            char c = str.charAt(i++);
            if(!OPERATOR.containsKey(c)){
                result.add(c);
            }else {
                if(operStack.empty()){
                    operStack.push(c);
                }else{
                    while (true){
                        Character peek = operStack.peek();
                        if(OPERATOR.get(c) > OPERATOR.get(peek) && peek != LEFT_BRACKET){
                            result.add(operStack.pop());
                            if(operStack.empty()){
                                operStack.push(c);
                                break;
                            }
                        }else if (OPERATOR.get(c) == OPERATOR.get(peek) && peek != '!' && peek != '('){
                            result.add(operStack.pop());
                            if(operStack.empty()){
                                operStack.push(c);
                                break;
                            }
                        }
                        else {
                            if(c == RIGHT_BRACKET){
                                Character pop;
                                do{
                                    pop = operStack.pop();
                                    if(pop != LEFT_BRACKET){
                                        result.add(pop);
                                    }
                                }while (pop != LEFT_BRACKET);
                            }else {
                                operStack.push(c);
                            }
                            break;
                        }
                    }
                }
            }
        }
        while (!operStack.empty()){
            result.add(operStack.pop());
        }
        BinaryTree binaryTree = transferTree(result);
        return transferTree(result);
    }

    private static BinaryTree transferTree(List<Character> list){
        Stack<Object> treeStack = new Stack<>();
        for(int i=0;i<list.size();i++){
            Character c = list.get(i);
            if(!OPERATOR.containsKey(c)){
                treeStack.push(c);
            }else{
                if(c == '!'){
                    Object pop = treeStack.pop();
                    treeStack.push(createNode("non",null,pop));
                }else{
                    Object rightValue = treeStack.pop();
                    Object leftValue = treeStack.pop();
                    treeStack.push(createNode(c=='|'?"or":"and",leftValue,rightValue));
                }
            }
        }
        return (BinaryTree) treeStack.pop();
    }


    private static BinaryTree createNode(String type,Object leftValue,Object rightValue){
        BinaryTree binaryTree = new BinaryTree("");
        switch (type){
            case "and":
                binaryTree = new AndTreeNode("");
                break;
            case "or":
                binaryTree = new OrTreeNode("");
                break;
            case "non":
                binaryTree = new NonTreeNode("");
                break;
        }
        BinaryTree left = null,right = null;
        if(leftValue != null){
            left = leftValue instanceof BinaryTree?(BinaryTree) leftValue:new DataTreeNode(String.valueOf(leftValue));
        }
        if(rightValue != null){
            right = rightValue instanceof BinaryTree?(BinaryTree) rightValue:new DataTreeNode(String.valueOf(rightValue));
        }
        if(left != null){
            binaryTree.setLeft(left);
            left.setParent(binaryTree);
        }
        if(right != null){
            binaryTree.setRight(right);
            right.setParent(binaryTree);
        }
        return binaryTree;
    }
}
