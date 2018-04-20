package com.example.mycalculator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    boolean isCalculated = false;               // 한번 계산되었는지, 계산 안되었는지 확인하는 boolean변수
    Button[] numbers;
    Button deleteButton;
    Button addButton;
    Button subtractButton;
    Button multipleButton;
    Button divideButton;
    Button dotButton;
    Button equalButton;
    Button leftButton;
    Button rightButton;
    Button nextButton;
    TextView resultView;

    int[] numbersAddres = new int[]{R.id.number0, R.id.number1,R.id.number2,R.id.number3,R.id.number4,R.id.number5,R.id.number6,R.id.number7,R.id.number8,R.id.number9,};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        numbers = new Button[10];

        for(int i = 0 ; i < 10; i++){
            numbers[i] = (Button)findViewById(numbersAddres[i]);
        }
        deleteButton = (Button)findViewById(R.id.delete);
        addButton = (Button)findViewById(R.id.add);
        subtractButton = (Button)findViewById(R.id.subtract);
        multipleButton = (Button)findViewById(R.id.multiple);
        divideButton = (Button)findViewById(R.id.divide);
        dotButton = (Button)findViewById(R.id.chardot);
        equalButton = (Button)findViewById(R.id.charequal);
        leftButton = (Button)findViewById(R.id.charleft);
        rightButton = (Button)findViewById(R.id.charRight);
        nextButton = (Button)findViewById(R.id.next);

        resultView = (TextView)findViewById(R.id.resultview);
//////// 버튼들의 리스너 구현
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 아직 미구현
            }
        });

        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isCalculated){               // 한번 계산되었다면 바로 문자를 출력한다.
                    resultView.setText(")");
                    isCalculated = false;
                }else{                          // 계산이 처음이라면 이어서 붙인다.
                    String tmp = resultView.getText().toString();           // 보통 결과 창의 텍스트를 추출하고
                    tmp = tmp +")";                                         // 텍스트 마지막에 원하는 글자 추가하고
                    resultView.setText(tmp);                                // 저장한다.
                }
            }
        });

        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isCalculated){               // 한번 계산되었다면 바로 문자를 출력한다.
                    resultView.setText("(");
                    isCalculated = false;
                }else{                          // 계산이 처음이라면 이어서 붙인다.
                    String tmp = resultView.getText().toString();           // 보통 결과 창의 텍스트를 추출하고
                    tmp = tmp +"(";                                         // 텍스트 마지막에 원하는 글자 추가하고
                    resultView.setText(tmp);                                // 저장한다.
                }
            }
        });

        equalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 계산 프로그램 작성
                Calculate.calculate(resultView);
                isCalculated = true;
            }
        });

        dotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isCalculated){               // 한번 계산되었다면 바로 문자를 출력한다.
                    resultView.setText(".");
                    isCalculated = false;
                }else{                          // 계산이 처음이라면 이어서 붙인다.
                    String tmp = resultView.getText().toString();
                    tmp = tmp +".";
                    resultView.setText(tmp);
                }
            }
        });

        divideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tmp = resultView.getText().toString();
                if(tmp.length() == 0 || tmp.charAt(tmp.length() -1) == '('){
                    tmp = tmp + "0" + "/";
                }else{
                    tmp = tmp + "/";
                }
                resultView.setText(tmp);
                isCalculated = false;
            }
        });

        multipleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tmp = resultView.getText().toString();
                if(tmp.length() == 0 || tmp.charAt(tmp.length() - 1) == '(' ){
                    tmp = tmp +"0" + "*";
                }
                else{
                    tmp = tmp +"*";

                }
                resultView.setText(tmp);
                isCalculated = false;
            }
        });

        subtractButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tmp = resultView.getText().toString();
                if(tmp.length() == 0 || tmp.charAt(tmp.length() -1) == '('){
                    tmp = tmp + "0" +"-";
                }
                else{
                    tmp = tmp + "-";
                }
                resultView.setText(tmp);
                isCalculated = false;
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tmp = resultView.getText().toString();       // 문자열 추출
                if(tmp.length() == 0 || tmp.charAt(tmp.length()-1) == '(' ){
                    tmp = tmp + "0" +"+";
                }
                else {
                    tmp = tmp + "+";                                    // 뒷 부분에 +를 추가
                }
                resultView.setText(tmp);                            // 화면의 텍스트를 바꿈
                isCalculated = false;

            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isCalculated){
                    resultView.setText("");
                    isCalculated = false;
                }
                else {
                    String tmp = resultView.getText().toString();
                    if(!tmp.equals("")){
                        tmp = tmp.substring(0, tmp.length()-1);
                        resultView.setText(tmp);
                    }
                }

            }
        });

        numbers[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isCalculated){                   // 한번 계산되었다면 바로 0을 출력한다.
                    resultView.setText("0");
                    isCalculated = false;
                }else{                              // 계산이 처음이라면 이어서 붙인다.
                    String tmp = resultView.getText().toString();
                    tmp = tmp +String.valueOf(0);
                    resultView.setText(tmp);
                }
            }
        });

        numbers[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isCalculated){               // 한번 계산되었다면 바로 숫자를 출력한다.
                    resultView.setText("1");
                    isCalculated = false;
                }else{                          // 계산이 처음이라면 이어서 붙인다.
                    String tmp = resultView.getText().toString();
                    tmp = tmp +String.valueOf(1);
                    resultView.setText(tmp);
                }
            }
        });
        numbers[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isCalculated){               // 한번 계산되었다면 바로 숫자를 출력한다.
                    resultView.setText("2");
                    isCalculated = false;
                }else{                          // 계산이 처음이라면 이어서 붙인다.
                    String tmp = resultView.getText().toString();
                    tmp = tmp +String.valueOf(2);
                    resultView.setText(tmp);
                }
            }
        });
        numbers[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isCalculated){               // 한번 계산되었다면 바로 숫자를 출력한다.
                    resultView.setText("3");
                    isCalculated = false;
                }else{                          // 계산이 처음이라면 이어서 붙인다.
                    String tmp = resultView.getText().toString();
                    tmp = tmp +String.valueOf(3);
                    resultView.setText(tmp);
                }
            }
        });
        numbers[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isCalculated){               // 한번 계산되었다면 바로 숫자를 출력한다.
                    resultView.setText("4");
                    isCalculated = false;
                }else{                          // 계산이 처음이라면 이어서 붙인다.
                    String tmp = resultView.getText().toString();
                    tmp = tmp +String.valueOf(4);
                    resultView.setText(tmp);
                }
            }
        });
        numbers[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isCalculated){               // 한번 계산되었다면 바로 숫자를 출력한다.
                    resultView.setText("5");
                    isCalculated = false;
                }else{                          // 계산이 처음이라면 이어서 붙인다.
                    String tmp = resultView.getText().toString();
                    tmp = tmp +String.valueOf(5);
                    resultView.setText(tmp);
                }
            }
        });
        numbers[6].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isCalculated){               // 한번 계산되었다면 바로 숫자를 출력한다.
                    resultView.setText("6");
                    isCalculated = false;
                }else{                          // 계산이 처음이라면 이어서 붙인다.
                    String tmp = resultView.getText().toString();
                    tmp = tmp +String.valueOf(6);
                    resultView.setText(tmp);
                }
            }
        });
        numbers[7].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isCalculated){               // 한번 계산되었다면 바로 숫자를 출력한다.
                    resultView.setText("7");
                    isCalculated = false;
                }else{                          // 계산이 처음이라면 이어서 붙인다.
                    String tmp = resultView.getText().toString();
                    tmp = tmp +String.valueOf(7);
                    resultView.setText(tmp);
                }
            }
        });
        numbers[8].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isCalculated){               // 한번 계산되었다면 바로 숫자를 출력한다.
                    resultView.setText("8");
                    isCalculated = false;
                }else{                          // 계산이 처음이라면 이어서 붙인다.
                    String tmp = resultView.getText().toString();
                    tmp = tmp +String.valueOf(8);
                    resultView.setText(tmp);
                }
            }
        });
        numbers[9].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isCalculated){               // 한번 계산되었다면 바로 숫자를 출력한다.
                    resultView.setText("9");
                    isCalculated = false;
                }else{                          // 계산이 처음이라면 이어서 붙인다.
                    String tmp = resultView.getText().toString();
                    tmp = tmp +String.valueOf(9);
                    resultView.setText(tmp);
                }
            }
        });


    }
}
