package com.xcrj;

public class AVL{
    class Node{
        int key,height;
        Node left,right;
        public Node(int key){
            this.key=key;
            this.height=1;
        }
    }

    Node root;
    public AVL(){
        root=null;
    }

    private int height(Node node){
        if(node==null) return 0;
        return node.height;
    }

    private int balanceFactor(Node node){
        if(node==null) return 0;
        return height(node.left)-height(node.right);
    }

    private Node rotateRight(Node node){
        Node newRoot=node.left;
        Node leftRight=newRoot.right;

        newRoot.right=node;
        node.left=leftRight;

        node.height=Math.max(height(node.left),height(node.right))+1;
        newRoot.height=Math.max(height(newRoot.left),height(newRoot.right))+1;
        return newRoot;
    }

    private Node rotateLeft(Node node){
        Node newRoot=node.right;
        Node rightLeft=newRoot.left;

        newRoot.left=node;
        node.right=rightLeft;

        node.height=Math.max(height(node.left),height(node.right))+1;
        newRoot.height=Math.max(height(newRoot.left),height(newRoot.right))+1;
        return newRoot;
    }

    /**
     * 同于BST
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
            return searchRec(node.left, key);
        }
        return searchRec(node.right, key);
    }

    /**
     * 对比BST的insert多了自平衡操作
     * @param key
     * @return
     */
    public Node insert(int key){
        root=insertRec(root,key);
        return root;
    }

    private Node insertRec(Node node,int key){
        if(node==null){
            return new Node(key);
        }
        if(key<node.key){
            node.left=insertRec(node.left, key);
        }else if(key>node.key){
            node.right=insertRec(node.right, key);
        }else{
            //树中已经存在，不再插入
            return root;
        }
        //
        return rebalance(node,key);
    }

    private Node rebalance(Node node,int key){
        //更新高度
        node.height=Math.max(height(node.left),height(node.right))+1;
        //
        int balance=balanceFactor(node);
        //node left left, 新结点插入左孩子的左子树上
        if(balance>1&&key<node.left.key){
            return rotateRight(node);
        }
        //node left right，新结点插入左孩子的右子树上
        if(balance>1&&key>node.left.key){
            node.left=rotateLeft(node.left);
            return rotateRight(node);
        }
        //node right right，新结点插入右孩子的右子树上
        if(balance<-1&&key>node.right.key){
            return rotateLeft(node);
        }
        //node right left，新结点插入右孩子的左子树上
        if(balance<-1&&key<node.right.key){
            node.right=rotateRight(node.right);
            return rotateLeft(node);
        }
        return node;
    }

    public void inorder() {
        inorderRec(root);
    }

    private void inorderRec(Node node) {
        if(node==null) return;
        inorderRec(node.left);
        System.out.println(node.key+", ");
        inorderRec(node.right);
    }

    public void delete(int key) {
        root=deleteRec(root, key);
    }

    /**
     * 为了效率一般不对delete进行平衡操作
     * 将平衡操作放到了add中
     * todo height调整，rebalance
     * @param node
     * @param key
     * @return
     */
    private Node deleteRec(Node node,int key){
        if(node==null) return null;
        if(key<node.key){
            node.left=deleteRec(node.left, key);
        }else if(key>node.key){
            node.right=deleteRec(node.right, key);
        }else{
            //无左右子树
            if(node.left==null&&node.right==null){
                node=null;
            }
            //有左子树
            else if(node.right==null){
                node=node.left;
            }
            //有右子树
            else if(node.left==null){
                node=node.right;
            }
            //有左右子树
            else{
                Node successor=successor(node.right);
                node.key=successor.key;
                node.right=deleteRec(node.right, successor.key);
            }
        }

        return node;
    }

    private Node successor(Node node){
        while(node.left!=null){
            node=node.left;
        }
        return node;
    }

    public static void main(String[] args) {
        AVL tree = new AVL();

        // Insert
        tree.insert(10);
        tree.insert(20);
        tree.insert(30);
        tree.insert(40);
        tree.insert(50);
        tree.insert(60);
        tree.insert(70);
        tree.insert(80);
        tree.inorder();

        // Search
        Node node=tree.search(70);
        System.out.println(node==null?"不存在":("搜索值: "+node.key));

        // Delete
        tree.delete(20);
        tree.inorder();
    }
}