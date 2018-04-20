package com.example.mycalculator;

import android.util.Log;
import android.widget.TextView;

import java.util.StringTokenizer;

/**
 * Created by Hong on 2018-04-17.
 *
 * 계산 하는 스태틱 클래스를 작성 해야됨. (스택 , 후위 수식 계산 메소드, 중위 -> 후위 수식메소드)를 이용한다.
 */

public final class Calculate {
    static String expression;           // resultView에 있는 표현식
    static TextView resultView;         // resultView 주소를 담고있는 객체

    static public void calculate(TextView resultView){
        Calculate.resultView = resultView;
        expression = resultView.getText().toString();           // 화면에 떠져있는 식
        String postStr = midExprToPost(expression);             // 중위수식을 후위수식으로 바꾼다.

        double value = calcPostfixExpr(postStr);

        int value2 = (int)value;

        if(value == value2){            // 정수로 표현한거랑 소수로 표현한 것이랑 같다면 굳이 소수로 표현하지 않는다.
            Calculate.resultView.setText(String.valueOf(value2));
        }
        else{
            Calculate.resultView.setText(String.valueOf(value));
        }
    }

    //후위수식을 계산하는 프로그램 => resultView를 수정한다.
    //띄어쓰기로 구분한다.
    private static double calcPostfixExpr(String postStr) {
        NumberStack st = new NumberStack();
        double finalValue = 0;
        String[] strings = postStr.split(" ");
        try{
            for(String string: strings){
                if(string.equals("+") || string.equals("-") || string.equals("*") || string.equals("/")){// 문자열이 연산자이면.
                    double val2 = st.pop();         // 스택에 쌓여있는 숫자들을 꺼내서 해당하는 연산자에 맞게 계산해서 다시 스택에 넣는다.
                    double val1 = st.pop();
                    if(string.equals("+")){
                        st.push(val1 + val2);
                    }else if(string.equals("-")){
                        st.push(val1 - val2);
                    }else if(string.equals("*")){
                        st.push(val1 * val2);
                    }else if(string.equals("/")){
                        st.push(val1 / val2);
                    }
                }else{                      // 사칙연산이 아니면 무조건 숫자이므로.  (다른 연산은 나중에 추가하도록 하자!)
                    if(string != null && string.length() > 0){              // 이걸 무조건 해주어야된다. ( 근데 string이 걸러져서 나오지 않았을까??)
                        double val = Double.parseDouble(string);
                        st.push(val);
                    }
                }
            }
            finalValue = st.pop();
            Log.d("fianl Value:", String.valueOf(finalValue));
        }catch(Exception e){
            resultView.setText("Error");
            e.printStackTrace();
        }
        return finalValue;
    }

    // 중위 수식을 후위수식으로 바꾸는 메소드
    private static String midExprToPost(String midStr){
        CharStack st = new CharStack();
        String postStr = "";
        String spaceStr = ""; //띄어쓰기로 구분된 문자열
        // midStr(정제되지 않은 str)을 연산자 + - / * ( ) 을 기준으로 나눠야 된다. StringTokenizer사용.

        StringTokenizer stringTokenizer = new StringTokenizer(midStr, "+-/*()", true);
        while(stringTokenizer.hasMoreTokens()){     // 토큰이 있을 때 까지
            spaceStr = spaceStr + " " + stringTokenizer.nextToken();            // 띄어쓰기를 해서 띄어쓰기가 있는 중위 수식으로 바꾼다.
        }

        String[] strings = spaceStr.split(" ");             // 띄어쓰기가 된 것들을 더욱 구분하기 좋게 문자열 배열로 만들고
        try{
            for(String string : strings){           // 배열이 끝날때까지 입력을 받는다.
                if(string.equals("+") || string.equals("-") || string.equals("*") || string.equals("/")){       // 입력 받은게 연산자라면
                    char nowCh = string.charAt(0);          // 문자열을 문자형으로 형변환 시킨다.
                    while(!st.isEmpty()){           // 스택이 비어있지 않으면.
                        char op = st.peek();        // 먼저 스택에 들어와 있던 연산자가

                        if(precedence(nowCh) <= precedence(op)){        // 높거나 같다면 출력해준다.
                            postStr = postStr + " " + op;
                            st.pop();
                        }else break;                                // 작다면 while문을 나가고
                    }
                    st.push(nowCh);                 // 스택에 쌓아준다.
                }else if(string.equals("(")){
                    st.push(string.charAt(0));
                }else if(string.equals(")")){
                    while(!st.isEmpty()){                           // 스택에 아무것도 없을 때까지
                        char op = st.pop();                        //  스택에 있는 연산자를 꺼내고
                        if(op == '(') break;                        // (이면 끝내고 아니면
                        else postStr = postStr + " " + op;          // 아니면 후위수식 문자열에 띄어쓰기를 하고 출력한다.
                    }
                }else{                                              // 연산자도 아니고 괄호도 아니라면 숫자이므로.
                    postStr = postStr + " " + string;               // 그대로 출력하도록 한다.
                }
            }

            // 마지막에는 쌓여있던 스택을 모두 출력해준다.
            while(!st.isEmpty()){
                postStr = postStr + " " + st.pop();
            }
        }catch(Exception e){
            resultView.setText("Error");
        }

        return postStr;     //마지막 후위 수식을 출력해준다.
    }

    //연산자 우선순위를 계산하는 메소드
    private static int precedence(char op){
        switch(op) {
            case '(' :case ')' : return 0;
            case '+' :case '-' : return 1;
            case '*' :case '/' : return 2;
        }
        return -1;          // 오류
    }
}
