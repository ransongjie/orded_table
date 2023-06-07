package com.xcrj;

import java.util.*;
/**
 * 优化 跳表 规定 左边界最小 右边界最大
 */
public class SkipTable2 {
    private class Node {
        boolean start;//起始结点
        boolean end;//终止结点
        int key;
        int level;
        Node next;
        Node down;
        Node(int level,boolean start,boolean end){
            this.level=level;
            this.start=start;
            this.end=end;
        }
        Node(int key,int level){
            this.key=key;
            this.level=level;
            this.start=false;
            this.end=false;
        }
    }

    LinkedList<Node> headColumn;
    LinkedList<Node> tailColumn;
    int maxLevel;
    int size;
    public SkipTable2(){
        headColumn=new LinkedList<>();
        tailColumn=new LinkedList<>();
        maxLevel=-1;
        size=0;
    }

    public Node search(int key){
        Node head=headColumn.getFirst();
        Node p=head;
        while(p!=null&&p.next!=null){
            if(!p.next.end&&p.next.key==key){
                return p.next;
            }
            if(!p.next.end&&p.next.key<key){
                p=p.next;
                continue;
            }
            if(p.next.end||p.next.key>key){
                p=p.down;
                continue;
            }
        }
        return null;
    }

    private Node searchPre(int key){
        Node head=headColumn.getFirst();
        Node p=head;
        while(p!=null&&p.next!=null){
            if(!p.next.end&&p.next.key==key){
                return p;
            }
            if(!p.next.end&&p.next.key<key){
                p=p.next;
                continue;
            }
            if(p.next.end||p.next.key>key) {
                p = p.down;
                continue;
            }
        }
        return null;
    }

    public void clear(){
        headColumn=new LinkedList<>();
        tailColumn=new LinkedList<>();
        maxLevel=-1;//从第0层开始
        size=0;
    }

    public void show(){
        System.out.println("跳表层数："+maxLevel+", 元素个数为："+size);
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

    public void add(int key){
        if(maxLevel!=-1&&search(key)!=null) return;

        int level=randLevel();
        //处理跳表边界
        if(level>maxLevel){
            int diff=level-maxLevel;
            for (int i = 0; i < diff; i++) {
                Node newHeadFirst=new Node(maxLevel+i+1,true,false);
                Node newTailFirst=new Node(maxLevel+i+1,false,true);
                if(headColumn.size()>0) {
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
        //找到跳表所在层头结点
        Node head= headColumn.get(maxLevel-node.level);
        Node p=head;
        while (p!=null&&p.next!=null){
            //往后走
            while (!p.next.end&&p.next.key<node.key){
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

    private int randLevel(){
        int level=0;
        while (Math.random()>0.5){
            level++;
        }
        return level;
    }

    public static void main(String[] args) {
        SkipTable2 skipTable = new SkipTable2();

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