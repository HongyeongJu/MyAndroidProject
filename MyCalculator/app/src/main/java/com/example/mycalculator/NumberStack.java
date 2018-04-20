package com.example.mycalculator;

/**
 * Created by Hong on 2018-04-18.
 */

public class NumberStack {
    double[] data = new double[100];
    int top = -1;

    public void push(double n) throws Exception{
        if(isFull()) throw new Exception();

        data[++top] = n;
    }

    public boolean isFull() {
        return top == (data.length -1);
    }
    public boolean isEmpty() {
        return top == -1;
    }

    public double pop() throws Exception{
        if(isEmpty()) throw new Exception();
        return data[top--];
    }
    //개수를 출력하는 메소드
    public int length() {
        return top + 1;
    }
}
