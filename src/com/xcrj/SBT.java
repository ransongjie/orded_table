package com.xcrj;

public class SBT {
    class Node{
        int key,size;
        Node left,right;
        public Node(int key){
            this.key=key;
            this.size=1;
        }
    }

   Node root;

   public SBT(){
       this.root=null;
   }
   
   private int size(Node node){
       if(node==null) return 0;
       return node.size;
   }

   public Node search(int key){
       return searchRec(root, key);
   }
   private Node searchRec(Node node,int key){
       if(node==null) return null;
       if(node.key==key) return node;
       if(key<node.key){
           return searchRec(node.left, key);
       }
       return searchRec(node.right, key);
   }

   public void inorder() {
       inorderRec(root);
   }
   private void inorderRec(Node node){
       if(node==null) return;
       inorderRec(node.left);
       System.out.println(node.key+", ");
       inorderRec(node.right);
   }

   public void insert(int key) {
       root=insertRec(root, key);
   }
   private Node insertRec(Node node,int key){
       if(node==null) return new Node(key);
       if(key<node.key){
           node.left=insertRec(node.left, key);
       }
       else if(key>node.key){
           node.right=insertRec(node.right, key);
       }
       else{
           //已存在
           return node;
       }
       node=rebalance(node);
       return node;
   }
   private Node rotateRight(Node node){
       Node a=node.left;
       Node b=a.right;
       a.right=node;
       node.left=b;

       node.size=1+size(node.left)+size(node.right);
       a.size=1+size(a.left)+size(a.right);
       return a;
   }
   
   private Node rotateLeft(Node node){
       Node a=node.right;
       Node b=a.left;
       a.left=node;
       node.right=b;

       node.size=1+size(node.left)+size(node.right);
       a.size=1+size(a.left)+size(a.right);
       return a;
   }

   //子结点发生过变化的父节点需要rebalance
   private Node rebalance(Node node){
       if(node==null) return null;

        node.size=1+size(node.left)+size(node.right);

        int nLSize=node.left==null?0:size(node.left);
        int nRSize=node.right==null?0:size(node.right);
        int nLLsize=node.left==null?0:(node.left.left==null?0:size(node.left.left));
        int nLRsize=node.left==null?0:(node.left.right==null?0:size(node.left.right));
        int nRRSize=node.right==null?0:(node.right.right==null?0:size(node.right.right));
        int nRLSize=node.right==null?0:(node.right.left==null?0:size(node.right.left));

        //左孩子.左孩子>右叔叔
        if(nLLsize>nRSize){
            node=rotateRight(node);//右
            node.right=rebalance(node.right);//右
            node=rebalance(node);
        }
        //左孩子.右孩子>右叔叔
        else if(nLRsize>nRSize){
            node.left=rotateLeft(node.left);
            node=rotateRight(node);
            node.left=rebalance(node.left);//左
            node.right=rebalance(node.right);//右
            node=rebalance(node);//自己
        }
        //右孩子.左孩子>左叔叔
        else if(nRLSize>nLSize){
            node.right=rotateRight(node.right);
            node=rotateLeft(node);
            node.left=rebalance(node.left);//左
            node.right=rebalance(node.right);//右
            node=rebalance(node);//自己
        }
        //右孩子.右孩子>左叔叔
        else if(nRRSize>nLSize){
            node=rotateLeft(node);
            node.left=rebalance(node.left);
            node=rebalance(node);
        }

        return node;
    }

   public void delete(int key) {
       root=deleteRec(root, key);
   }

   /**
    * 为了效率一般不对delete进行平衡操作
    * 将平衡操作放到了add中
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
    SBT tree = new SBT();

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
