package com.execution.parse;


import com.execution.parse.tree.BinaryTree;
import com.execution.parse.tree.DataTreeNode;

public class Calculator {



    public static boolean calculate(BinaryTree binaryTree){
        return calculateTreeNode(binaryTree);
    }

    private static boolean calculateTreeNode(BinaryTree binaryTree){
        String type = binaryTree.getType();
        Boolean leftRes = null,rightRes = null;
        if("data".equals(type)){
            return calculate((DataTreeNode)binaryTree);
        } else{
            //断路逻辑
            if("or".equals(type) || "and".equals(type)){
                BinaryTree left = binaryTree.getLeft();
                if(left != null){
                    leftRes = calculateTreeNode(left);
                    if((leftRes && "or".equals(type) || (!leftRes && "and".equals(type)))){
                        replaceDataNode(binaryTree,leftRes);
                        return leftRes;
                    }
                }
                BinaryTree right = binaryTree.getRight();
                rightRes = calculateTreeNode(right);
                Boolean realRes = "or".equals(type)?(leftRes||rightRes):(leftRes&&rightRes);
                replaceDataNode(binaryTree,realRes);
                return realRes;
            }
            else{
                boolean res = !calculateTreeNode(binaryTree.getRight());
                replaceDataNode(binaryTree,res);
                return res;
            }
        }
    }

    private static void replaceDataNode(BinaryTree binaryTree,Boolean res){
        BinaryTree parent = binaryTree.getParent();
        if(parent != null){
            boolean isLeft = parent.getLeft() == binaryTree;
            DataTreeNode dataTreeNode = new DataTreeNode("");
            dataTreeNode.setRes(res);
            if(isLeft){
                parent.setLeft(dataTreeNode);
            }else {
                parent.setRight(dataTreeNode);
            }
        }
    }

    private static boolean calculate(DataTreeNode dataTreeNode){
        System.out.print(dataTreeNode.getData());
        Boolean res = dataTreeNode.getRes();
        if(res != null)return  res;
        res = "T".equals(dataTreeNode.getData());
        dataTreeNode.setRes(res);
        return res;
    }

}
