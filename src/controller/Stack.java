package controller;

public interface Stack {

    boolean isEmpty();//判空
    void push(int[][] data);//入栈
    int[][] peek();//返回栈顶元素，不移除

    //String pop();//返回栈顶元素，并且移除
    boolean isFull();
}
