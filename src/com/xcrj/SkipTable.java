package com.xcrj;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
/**
 * 跳表 左边界是Integer.MIN_VALUE 右边界是Integer.MAX_VALUE
 */
public class SkipTable {
    private class Node{
        int key;
        int level;//结点行
        Node next;
        Node down;
        Node(int key,int level){
            this.key=key;
            this.level=level;
        }
    }

    LinkedList<Node> headColumn;
    LinkedList<Node> tailColumn;
    int maxLevel;//跳表行数
    int size;//结点总数
    public SkipTable(){
        headColumn=new LinkedList<>();
        tailColumn=new LinkedList<>();
        maxLevel=-1;//从第0层开始
        size=0;
    }

    /**
     * 添加元素，跳表元素不能重复
     * - 获取随机层数level
     * - if level>maxLevel (跳表level), 扩充跳表边界
     * - 创建新结点
     * - ！找到新结点所在层头结点
     * - if 新结点key>下1个结点key，“往后走” else 连接前后插入新结点
     * - if 新结点level>0，创建相同新结点，“往下走”
     * @param key
     */
    public void add(int key){
        //跳表存在的情况下，key已经存在，则退出
        if(maxLevel!=-1&&search(key)!=null) return;

        int level=randLevel();
        //处理跳表边界
        if(level>maxLevel){
            int diff=level-maxLevel;
            for (int i = 0; i < diff; i++) {
                Node newHeadFirst=new Node(Integer.MIN_VALUE,maxLevel+i+1);
                Node newTailFirst=new Node(Integer.MAX_VALUE,maxLevel+i+1);
                if(headColumn.size()>0) {//跳表非空，则处理down指针
                    Node headFirst = headColumn.getFirst();
                    newHeadFirst.down = headFirst;
                }
                if(tailColumn.size()>0){
                    Node tailFirst = tailColumn.getFirst();
                    newTailFirst.down=tailFirst;
                }
                newHeadFirst.next=newTailFirst;
                headColumn.addFirst(newHeadFirst);
                tailColumn.addFirst(newTailFirst);
            }
            maxLevel=level;
        }

        //新增跳表结点
        Node node=new Node(key,level);
        //找到新结点所在层头结点
        Node head= headColumn.get(maxLevel-node.level);
        Node p=head;
        while (p!=null&&p.next!=null){
            //往后走
            while (p.next.key<node.key){
                p=p.next;
            }
            //连接前后
            node.next=p.next;
            p.next=node;
            //不是第0层 则连接上下
            if(node.level>0){
                Node nodeDown=new Node(key,node.level-1);
                node.down=nodeDown;
                node=nodeDown;
            }
            //往下走
            p=p.down;
        }
        size++;
    }

    /**
     * 至少有一层，至少为0
     * @return
     */
    private int randLevel(){
        int level=0;
        while (Math.random()>0.5){
            level++;
        }
        return level;
    }

    /**
     * 删除key所在的列
     * @param key
     */
    public void delete(int key){
        Node pre=searchPre(key);
        if(pre==null) return;
        while(true){
            pre.next=pre.next.next;
            pre=pre.down;
            if(pre==null) break;
            while(pre.next.key!=key){
                pre=pre.next;
            }
        }
        size--;
    }

    /**
     * 搜索相等key结点
     * 一直往右走，不能继续往右走则往下走
     * @param key
     * @return
     */
    public Node search(int key){
        Node head=headColumn.getFirst();
        //一直往右走，不能继续往右走则往下走
        Node p=head;
        while(p!=null&&p.next!=null){//补充！while中使用相同级别指针
            if(p.next.key==key){
                return p.next;
            }
            if(p.next.key<key){
                p=p.next;
                continue;
            }
            if(p.next.key>key){
                p=p.down;
                continue;
            }
        }
        return null;
    }

    /**
     * 搜索相等key的前1个结点
     * @param key
     * @return
     */
    private Node searchPre(int key){
        Node head=headColumn.getFirst();
        //一直往右走，不能继续往右走则往下走
        Node p=head;
        while(p!=null&&p.next!=null){
            if(p.next.key==key){
                return p;
            }
            if(p.next.key<key){
                p=p.next;
                continue;
            }
            if(p.next.key>key) {
                p = p.down;
                continue;
            }
        }
        return null;
    }

    /**
     * 清空跳表
     */
    public void clear(){
        headColumn=new LinkedList<>();
        tailColumn=new LinkedList<>();
        maxLevel=-1;//从第0层开始
        size=0;
    }

    /**
     * 从上到下打印跳表的内容
     */
    public void show(){
        System.out.println("跳表层数："+maxLevel+", 元素个数为："+size);
        //从上往下逐层打印
        for (int i = 0; i <=maxLevel ; i++) {
            Node linFirst = headColumn.get(i);
            System.out.print("第"+linFirst.level+"层:\t"+"head ->\t");
            linFirst = linFirst.next;//跳过第一列的元素
            while (linFirst != null){
                if(linFirst.next != null){
                    System.out.print(""+linFirst.key +'\t'+"->\t");
                }else {
                    System.out.println("tail");
                }
                linFirst = linFirst.next;
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        SkipTable skipTable = new SkipTable();

        int times=100000;
        int maxLen=100;
        int maxV=100;
        for (int i = 0; i < times; i++) {
            int[]as=getAs(maxLen,maxV);
            int[]bs=cp(as);
            for (int a : as) {
                skipTable.add(a);
            }

            int[]cs=new int[bs.length];//cs不存在重复元素
            Node head0=skipTable.headColumn.getLast();
            Node p=head0.next;
            for (int j = 0; j < cs.length; j++) {
                cs[j]= p.key;
                if(p.next.next==null) break;//忽略边界
                p=p.next;
            }
            if(!Arrays.equals(bs,cs)){
                System.out.println(Arrays.toString(as));
                System.out.println(Arrays.toString(bs));
                System.out.println(Arrays.toString(cs));
                throw new RuntimeException();
            }

            int sizea=bs.length;
            int sizeb=skipTable.size;
            if(sizea!=sizeb){
                System.out.println(sizea);
                System.out.println(sizeb);
                throw new RuntimeException();
            }

            int idx=(int)(Math.random()* as.length);
            int a=as[idx];
            int b=skipTable.search(a).key;
            if(a!=b){
                System.out.println(a);
                System.out.println(b);
                throw new RuntimeException();
            }

            skipTable.delete(a);
            Node c=skipTable.search(a);
            if(c!=null){
                System.out.println(a);
                System.out.println(c.key);
                throw new RuntimeException();
            }
            if(skipTable.size!=bs.length-1){
                System.out.println(bs.length-1);
                System.out.println(skipTable.size);
                throw new RuntimeException();
            }

            skipTable.clear();
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
        for (int b:bs) {
            if(list.contains(b)) continue;
            list.add(b);
        }
        int[]ds=new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            ds[i]=list.get(i);
        }
        return ds;
    }
}