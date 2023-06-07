package com.xcrj;

public class BST {
    class Node{
        public int key;
        public Node left,right;
        public Node(int key){
            this.key=key;
        }
    }

    Node root;
    /**
     * 插入
     * 更小走左边
     * 更大走右边
     * @param key
     */
    public void insert(int key){
        root=insertRec(root,key);
    }

    /**
     * 已有的key不会被插入
     * @param node
     * @param key
     * @return
     */
    private Node insertRec(Node node,int key){
        if(node==null){
            return new Node(key);
        }
        if(key<node.key){
            node.left=insertRec(node.left, key);
        }else if(key>node.key){
            node.right=insertRec(node.right, key);
        }
        return node;
    }

    /**
     * 查找
     * 更小走左边
     * 更大走右边
     * @param key
     * @return
     */
    public Node search(int key){
        return searchRec(root, key);
    }

    private Node searchRec(Node node,int key){
        if(node==null) return null;
        if(key==node.key) return node;
        if(key<node.key){
            return searchRec(node.left,key);
        }
        return searchRec(node.right,key);
    }

    public void delete(int key){
        //执行删除之后新的根结点
        root=deleteRec(root, key);
    }

    private Node deleteRec(Node node,int key){
        if(node==null) return null;
        if(key<node.key){
            node.left=deleteRec(node.left,key);
        }else if(key>node.key){
            node.right=deleteRec(node.right,key);
        }else{//key=node.key
            //无左右子树
            if(node.left==null&&node.right==null){
                node=null;
            }
            //只有左子树
            else if(node.right==null){
                node=node.left;
            }
            //只有右子树
            else if(node.left==null){
                node=node.right;
            }
            //有左右子树
            else{
                Node successor=getSuccessor(node.right);
                node.key=successor.key;
                //这里并没有将 successor.key=node.key。直接删除 successor.key 即可
                node.right=deleteRec(node.right, successor.key);
            }
        }
        return node;
    }

    private Node getSuccessor(Node node){
        while(node.left!=null){
            node=node.left;
        }
        return node;
    }

    /**
     * 中序遍历得到二叉搜索树得到有序序列
     */
    public void inorder(){
        inorderRec(root);
    }

    private void inorderRec(Node node){
        if(node==null) return;
        inorderRec(node.left);
        System.out.println(node.key+",");
        inorderRec(node.right);
    }

    public static void main(String[] args) {
        BST tree = new BST();

        // test insert
        tree.insert(50);
        tree.insert(30);
        tree.insert(20);
        tree.insert(40);
        tree.insert(70);
        tree.insert(60);
        tree.insert(80);
        tree.inorder();

        // test search
        Node node = tree.search(60);
        if (node != null) {
            System.out.println("node.key=" + node.key);
        }
        else {
            System.out.println("没找到");
        }

        //test delete
        tree.delete(40);
        tree.inorder();
    }
}