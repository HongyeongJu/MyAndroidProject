package com.example.mycalculator;

/**
 * Created by Hong on 2018-04-18.
 */

public class CharStack {
    char[] datas = new char[100];
    int top = -1;

    public boolean isFull(){
        return top == (datas.length - 1);
    }

    public boolean isEmpty() {
        return top == -1;
    }

    public void push(char ch) throws Exception{
        if(isFull()) throw new Exception();
        datas[++top] = ch;
    }

    public char pop() throws Exception {
        if(isEmpty()) throw new Exception();
        return datas[top--];
    }

    public char peek() throws Exception {
        if(isEmpty()) throw new Exception();
        return datas[top];
    }
}
