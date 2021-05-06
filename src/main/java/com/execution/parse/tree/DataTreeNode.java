package com.execution.parse.tree;

public class DataTreeNode extends BinaryTree{

    private Boolean res;

    public DataTreeNode(String data) {
        super(data,"data");
    }

    public Boolean getRes() {
        return res;
    }

    public void setRes(Boolean res) {
        this.res = res;
    }
}
