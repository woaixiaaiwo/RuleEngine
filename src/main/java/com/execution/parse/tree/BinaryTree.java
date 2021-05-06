package com.execution.parse.tree;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class BinaryTree {

    private String data;

    private String type;

    private BinaryTree left;

    private BinaryTree right;

    private BinaryTree parent;

    public BinaryTree(String data) {
        this(data,"Normal");
    }

    public BinaryTree(String data,String type) {
        this.data = data;
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public BinaryTree getLeft() {
        return left;
    }

    public void setLeft(BinaryTree left) {
        this.left = left;
    }

    public BinaryTree getRight() {
        return right;
    }

    public void setRight(BinaryTree right) {
        this.right = right;
    }

    public BinaryTree getParent() {
        return parent;
    }

    public void setParent(BinaryTree parent) {
        this.parent = parent;
    }

    public String getType() {
        return type;
    }

    public void printTree(){
        LinkedList<BinaryTree> linkedList = new LinkedList();
        linkedList.add(this);
        while (!linkedList.isEmpty()){
            int length = linkedList.size();
            for(int i=0;i<length;i++){
                BinaryTree binaryTree = linkedList.removeFirst();
                System.out.print(binaryTree.getType()+":"+binaryTree.getData()+"        ");
                if(binaryTree.left != null){
                    linkedList.add(binaryTree.left);
                }
                if(binaryTree.right != null){
                    linkedList.add(binaryTree.right);
                }
            }
            System.out.println("-------------------------");
        }
    }

    public void mid(BinaryTree binaryTree){
        if(binaryTree.left != null){
            mid(binaryTree.left);
        }
        System.out.print(binaryTree.getType()+":"+binaryTree.getData() + "--->");
        if(binaryTree.right != null){
            mid(binaryTree.right);
        }
    }

    public List<BinaryTree> midByStack(BinaryTree binaryTree){
        Stack<BinaryTree> stack = new Stack<>();
        List<BinaryTree> list = new ArrayList<>();
        BinaryTree cur = binaryTree;
        stack.push(cur);
        while (cur != null && !stack.empty()){
           while (cur != null && cur.left != null){
               stack.push(cur.left);
               cur = cur.left;
           }
           cur = stack.pop();
           list.add(cur);
           if(cur.right != null){
               stack.push(cur.right);
               cur = cur.right;
           }
        }
        return list;
    }
}
