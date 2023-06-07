package com.xcrj;

import java.util.*;

public class RBT {
    static final boolean RED=false;
    static final boolean BLACK=true;
    RBNode root;
    static class RBNode {
        boolean color;
        int key;
        RBNode parent;
        RBNode left;
        RBNode right;
        RBNode(int key){
            this.key=key;
            //默认插入结点是红色，操作更简单
            this.color=RED;
        }
    }

    private boolean color(RBNode node){
        if(node==null) return BLACK;
        return node.color;
    }

    /**
     * 
     * @param node 结点的右孩子
     * @return
     */
    private RBNode succesor(RBNode node){
        while(node.left!=null){
            node=node.left;
        }
        return node;
    }

    /**
     * 左旋
     * @return 返回旋转子树的根
     */
    private RBNode leftRotate(RBNode node){
        //处理结点的孩子指针
        RBNode a=node.right;
        RBNode b=node.right.left;
        a.left=node;
        node.right=b;

        //处理父亲的儿子指针
        if(node.parent!=null){
            if(node.parent.left==node){
                node.parent.left=a;
            }else {
                node.parent.right=a;
            }
        }else{
            root=a;
        }

        //处理结点的父亲指针
        a.parent=node.parent;
        node.parent=a;
        if(b!=null){
            b.parent=node;
        }
        return a;
    }

    /**
     * 右旋
     * @return 返回旋转子树的根
     */
    private RBNode rightRotate(RBNode node){
        //处理结点的孩子指针
        RBNode a=node.left;
        RBNode b=a.right;
        a.right=node;
        node.left=b;

        //处理父亲的孩子指针
        if(node.parent==null){
            root=a;
        }else{
            if(node.parent.left==node){
                node.parent.left=a;
            }else{
                node.parent.right=a;
            }
        }

        //处理结点的父亲指针
        a.parent=node.parent;
        node.parent=a;
        if(b!=null){
            b.parent=node;
        }
        return a;
    }

    public RBNode search(int key) {
        return searchRec(root,key);
    }

    private RBNode searchRec(RBNode node, int key) {
        if(node==null) return null;
        if(node.key==key) return node;
        if(key<node.key){
            return searchRec(node.left, key);
        }
        if(key>node.key){
            return searchRec(node.right, key);
        }
        return null;
    }

    private RBNode getParent(int key){
        RBNode parent=null;
        RBNode p=root;
        while(p!=null){
            parent=p;
            if(key<p.key) p=p.left;
            else p=p.right;
        }
        return parent;
    }

    public void add(int key){
        RBNode rbNode=search(key);
        if(rbNode!=null) return;
        RBNode parent=getParent(key);

        RBNode node=new RBNode(key);
        node.parent=parent;
        if(parent==null){
            root=node;
            node.parent=null;
        }else{
            if(node.key<parent.key){
                parent.left=node;
            }else{
                parent.right=node;
            }
        }

        addBalance(node);
    }

    private void addBalance(RBNode node){
        if(node==root){
            node.color=BLACK;
            return;
        }

        if(node!=root){
            RBNode parent=node.parent;
            if(parent.color==BLACK){
                return;
            }
            if(parent.color==RED){
                RBNode grand=parent.parent;
                if(grand==null) {
                    parent.color=BLACK;
                    return;
                }
                if(grand!=null){
                    if(grand.left==parent){
                        RBNode runcle=grand.right;
                        if(runcle==null||runcle.color==BLACK){
                            if(node==parent.left){
                                parent.color=BLACK;
                                grand.color=RED;
                                rightRotate(grand);
                                return;
                            }
                            if(node==parent.right){
                                node.color=BLACK;
                                grand.color=RED;
                                leftRotate(parent);
                                rightRotate(grand);
                                return;
                            }
                            return;
                        }
                        if(runcle.color==RED){
                            if(node==parent.left){
                                parent.color=BLACK;
                                runcle.color=BLACK;
                                grand.color=RED;
                                addBalance(grand);
                                return;
                            }
                            if(node==parent.right){
                                parent.color=BLACK;
                                runcle.color=BLACK;
                                grand.color=RED;
                                addBalance(grand);
                                return;
                            }
                            return;
                        }
                        return;
                    }
                    if(grand.right==parent){
                        RBNode luncle=grand.left;
                        if(luncle==null||luncle.color==BLACK){
                            if(node==parent.left){
                                node.color=BLACK;
                                grand.color=RED;
                                rightRotate(parent);
                                leftRotate(grand);
                                return;
                            }
                            if(node==parent.right){
                                parent.color=BLACK;
                                grand.color=RED;
                                leftRotate(grand);
                                return;
                            }
                            return;
                        }
                        if(luncle.color==RED){
                            if(node==parent.left){
                                parent.color=BLACK;
                                luncle.color=BLACK;
                                grand.color=RED;
                                addBalance(grand);
                                return;
                            }
                            if(node==parent.right){
                                parent.color=BLACK;
                                luncle.color=BLACK;
                                grand.color=RED;
                                addBalance(grand);
                                return;
                            }
                            return;
                        }
                        return;
                    }
                    return;
                }
                return;
            }
            return;
        }
    }

    public void delete(int key){
        RBNode node=search(key);
        if(node==null) return;
        if(node.left==null&&node.right==null){
            if(node.parent==null){
                root=null;
                return;
            }
            if(node.parent!=null){
                if(node.color==RED){
                    if(node.parent.left==node){
                        node.parent.left=null;
                        node=null;
                        return;
                    }
                    if(node.parent.right==node){
                        node.parent.right=null;
                        node=null;
                        return;
                    }
                    return;
                }
                if(node.color==BLACK){
                    if(node.parent.left==node){
                        if(node.parent.right==null){
                            show(root);
                            System.out.println(node.key);
                            System.out.println(node.left==null?null:node.left.color);
                            System.out.println(node.right==null?null:node.right.color);
                            System.out.println(node.parent.key);
                            System.out.println("node.parent.parent="+node.parent.parent);
                            System.out.println("node.parent.color="+node.parent.color);
                            throw new RuntimeException("node.parent.right=null");
                        }
                        if(node.parent.right.color==BLACK){//
                            RBNode slib=node.parent.right;
                            if(slib.left==null&&slib.right==null){
                                node.parent.color=BLACK;
                                slib.color=RED;
                                node.parent.left=null;
                                slib=null;
                                return;
                            }
                            if(slib.left!=null&&slib.right==null){
                                RBNode parent=node.parent;
                                RBNode root=slib.left;
                                node.parent.left=null;
                                node=null;
                                rightRotate(slib);
                                leftRotate(parent);
                                root.color=parent.color;
                                root.left.color=root.right.color=BLACK;
                                return;
                            }
                            if(slib.left==null&&slib.right!=null){
                                RBNode parent=node.parent;
                                RBNode root=slib;
                                node.parent.left=null;
                                node=null;
                                leftRotate(parent);
                                root.color=parent.color;
                                root.left.color=root.right.color=BLACK;
                                return;
                            }
                            if(slib.left!=null&&slib.right!=null){
                                RBNode parent=node.parent;
                                RBNode root=slib;
                                node.parent.left=null;
                                node=null;
                                leftRotate(parent);
                                root.color=parent.color;
                                root.left.color=root.right.color=BLACK;
                                return;
                            }
                            return;
                        }
                        if(node.parent.right.color==RED){
                            RBNode slib=node.parent.right;
                            RBNode parent=node.parent;
                            slib.color=BLACK;
                            parent.color=RED;
                            rightRotate(parent);
                            delete(node.key);
                            return;
                        }
                        return;
                    }
                    if(node.parent.right==node){
                        if(node.parent.left==null){
                            System.out.println(node.key);
                            System.out.println(node.parent.key);
                            show(root);
                        }
                        if(node.parent.left.color==BLACK){//
                            RBNode slib=node.parent.left;
                            if(slib.left==null&&slib.right==null){
                                node.parent.color=BLACK;
                                slib.color=RED;
                                node.parent.right=null;
                                slib=null;
                                return;
                            }
                            if(slib.left!=null&&slib.right==null){
                                RBNode parent=node.parent;
                                RBNode root=slib;
                                node.parent.right=null;
                                node=null;
                                rightRotate(parent);
                                root.color=parent.color;
                                root.left.color=root.right.color=BLACK;
                                return;
                            }
                            if(slib.left==null&&slib.right!=null){
                                RBNode parent=node.parent;
                                RBNode root=slib.right;
                                node.parent.right=null;
                                node=null;
                                leftRotate(slib);
                                rightRotate(parent);
                                root.color=parent.color;
                                root.left.color=root.right.color=BLACK;
                                return;
                            }
                            if(slib.left!=null&&slib.right!=null){
                                RBNode parent=node.parent;
                                RBNode root=slib;
                                node.parent.right=null;
                                node=null;
                                rightRotate(parent);
                                root.color=parent.color;
                                root.left.color=root.right.color=BLACK;
                                return;
                            }
                            return;
                        }
                        if(node.parent.left.color==RED){
                            RBNode slib=node.parent.left;
                            RBNode parent=node.parent;
                            slib.color=BLACK;
                            parent.color=RED;
                            leftRotate(parent);
                            delete(node.key);
                            return;
                        }
                        return;
                    }
                    return;
                }
                return;
            }
            return;
        }
        if(node.left!=null&&node.right==null){
            if(node.parent==null){
                root=node.left;
                node.parent=null;
                node=null;
                return;
            }
            if(node.parent!=null){
                RBNode grand=node.parent;
                RBNode son=node.left;
                if(grand.left==node){
                    son.color=BLACK;
                    grand.left=son;
                    son.parent=grand;
                    node=null;
                    return;
                }
                if(grand.right==node){
                    son.color=BLACK;
                    grand.right=son;
                    son.parent=grand;
                    node=null;
                    return;
                }
                return;
            }
            return;
        }
        if(node.left==null&&node.right!=null){
            if(node.parent==null){
                root=node.right;
                node.parent=null;
                node=null;
                return;
            }
            if(node.parent!=null){
                RBNode grand=node.parent;
                RBNode son=node.right;
                if(grand.left==node){
                    son.color=BLACK;
                    grand.left=son;
                    son.parent=grand;
                    node=null;
                    return;
                }
                if(grand.right==node){
                    son.color=BLACK;
                    grand.right=son;
                    son.parent=grand;
                    node=null;
                    return;
                }
                return;
            }
            return;
        }
        if(node.left!=null&&node.right!=null){
            RBNode succesor=succesor(node.right);
            int temp=succesor.key;
            succesor.key= node.key;
            node.key=temp;
            delete(succesor.key);
            return;
        }
    }

    private void delBalance(RBNode node){

    }

    public int[] inorder(){
        List<Integer> list=new ArrayList<>();
        inorderRec(root,list);
        int[]ds=new int[list.size()];
        for (int i = 0; i < ds.length; i++) {
            ds[i]=list.get(i);
        }
        return ds;
    }

    private void inorderRec(RBNode rbNode,List<Integer> list){
        if(rbNode==null) return;
        inorderRec(rbNode.left,list);
        list.add(rbNode.key);
        inorderRec(rbNode.right,list);
    }

    public void clear(){
        root=null;
    }

    public void show(RBNode root) {
        if (root == null) System.out.println("EMPTY!");
        // 得到树的深度
        int treeDepth = getTreeDepth(root);

        // 最后一行的宽度为2的（n - 1）次方乘3，再加1
        // 作为整个二维数组的宽度
        int arrayHeight = treeDepth * 2 - 1;
        int arrayWidth = (2 << (treeDepth - 2)) * 3 + 1;
        // 用一个字符串数组来存储每个位置应显示的元素
        String[][] res = new String[arrayHeight][arrayWidth];
        // 对数组进行初始化，默认为一个空格
        for (int i = 0; i < arrayHeight; i ++) {
            for (int j = 0; j < arrayWidth; j ++) {
                res[i][j] = " ";
            }
        }

        // 从根节点开始，递归处理整个树
        // res[0][(arrayWidth + 1)/ 2] = (char)(root.val + '0');
        writeArray(root, 0, arrayWidth/ 2, res, treeDepth);

        // 此时，已经将所有需要显示的元素储存到了二维数组中，将其拼接并打印即可
        for (String[] line: res) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < line.length; i ++) {
                sb.append(line[i]);
                if (line[i].length() > 1 && i <= line.length - 1) {
                    i += line[i].length() > 4 ? 2: line[i].length() - 1;
                }
            }
            System.out.println(sb.toString());
        }
    }

    private int getTreeDepth(RBNode root) {
        return root == null ? 0 : (1 + Math.max(getTreeDepth(root.left), getTreeDepth(root.right)));
    }

    private void writeArray(RBNode currNode, int rowIndex, int columnIndex, String[][] res, int treeDepth) {
        // 保证输入的树不为空
        if (currNode == null) return;
        // 先将当前节点保存到二维数组中
        res[rowIndex][columnIndex] = String.valueOf(currNode.key+"-"+(currNode.color==RBT.RED?"R":"B")+"");

        // 计算当前位于树的第几层
        int currLevel = ((rowIndex + 1) / 2);
        // 若到了最后一层，则返回
        if (currLevel == treeDepth) return;
        // 计算当前行到下一行，每个元素之间的间隔（下一行的列索引与当前元素的列索引之间的间隔）
        int gap = treeDepth - currLevel - 1;

        // 对左儿子进行判断，若有左儿子，则记录相应的"/"与左儿子的值
        if (currNode.left != null) {
            res[rowIndex + 1][columnIndex - gap] = "/";
            writeArray(currNode.left, rowIndex + 2, columnIndex - gap * 2, res, treeDepth);
        }

        // 对右儿子进行判断，若有右儿子，则记录相应的"\"与右儿子的值
        if (currNode.right != null) {
            res[rowIndex + 1][columnIndex + gap] = "\\";
            writeArray(currNode.right, rowIndex + 2, columnIndex + gap * 2, res, treeDepth);
        }
    }

    public static void main(String[] args) {
        int times=10000;
        int maxLen=1000;
        int maxV=1000;
        RBT rbTree=new RBT();
        for (int i = 0; i < times; i++) {
            int[]as=getAs(maxLen,maxV);
            for (int a:as) {
                rbTree.add(a);
            }
//            rbTree.show(rbTree.root);
            int[] bs=cp(as);
            int[] cs=rbTree.inorder();
            if(!Arrays.equals(bs,cs)){
                System.out.println(Arrays.toString(as));
                System.out.println(Arrays.toString(bs));
                System.out.println(Arrays.toString(cs));
                throw new RuntimeException();
            }

            int idx=(int)(Math.random()*as.length);
            int d=as[idx];
            int e=rbTree.search(d).key;
            if(e!=d){
                System.out.println(d);
                int[]es=rbTree.inorder();
                System.out.println(Arrays.toString(es));
                throw new RuntimeException();
            }

            rbTree.delete(d);
            if(rbTree.search(d)!=null){
                System.out.println(d);
                int[]es=rbTree.inorder();
                rbTree.show(rbTree.root);
                System.out.println(Arrays.toString(as));
                System.out.println(Arrays.toString(es));
                throw new RuntimeException();
            }
            rbTree.clear();
        }
    }

    public static int[] getAs(int maxLen,int maxV){
        int len=(int)(Math.random()*maxLen+1);
        int[]as=new int[len];
        for (int i = 0; i < len; i++) {
            as[i]=(int)(Math.random()*maxV);
        }
        return as;
    }

    public static int[] cp(int[]as){
        int[]bs= Arrays.copyOf(as,as.length);
        Arrays.sort(bs);
        List<Integer> list=new ArrayList<>();
        for (int b:
             bs) {
            if(list.contains(b))continue;
            list.add(b);
        }
        int[]ds=new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            ds[i]=list.get(i);
        }
        return ds;
    }
}