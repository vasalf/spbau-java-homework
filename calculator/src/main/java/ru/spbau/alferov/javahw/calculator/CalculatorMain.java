package ru.spbau.alferov.javahw.calculator;

import ru.spbau.alferov.javahw.calculator.tokens.CalcToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.List;

public class CalculatorMain {
    public static void main(String[] args) throws IOException, ParseException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String s = in.readLine();
        List<CalcToken> tokens = new RPNConverter(s).convert(new MyStack<>());
        System.out.println(new Calculator(new MyStack<>()).calculate(tokens));
    }
}
