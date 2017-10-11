package ru.spbau.alferov.javacw.LinkedHashMap;

import java.util.Map;

public class Main {
    public static void main(String[] args) {
        LinkedHashMap<String, Integer> lhm = new LinkedHashMap<>();
        lhm.put("abc", 1);
        lhm.put("def", 2);
        lhm.put("ghi", 3);
        lhm.put("123", 4);

        for (Map.Entry<String, Integer> entry : lhm.entrySet()) {
            System.out.print(entry.getKey());
            System.out.print(" ");
            System.out.println(entry.getValue());
        }

    }
}
