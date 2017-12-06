package ru.spbau.alferov.javahw.streams;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class SecondPartTasks {

    private SecondPartTasks() {}

    // Найти строки из переданных файлов, в которых встречается указанная подстрока.
    public static List<String> findQuotes(List<String> paths, CharSequence sequence) {
        return paths.stream().flatMap(
                path -> {
                    System.out.println(path);
                    Stream<String> ans;
                    try {
                        ans = Files.lines(Paths.get(path))
                                  .filter(s -> s.contains(sequence));
                    } catch (IOException e) {
                        System.out.println("oups");
                        return Stream.empty();
                    }
                    return ans;
                }
        ).collect(Collectors.toList());
    }

    private static class Point {
        private final double x, y;

        Point(double xValue, double yValue) {
            x = xValue;
            y = yValue;
        }

        public double radiusVectorLength() {
            return Math.hypot(x, y);
        }
    }

    // В квадрат с длиной стороны 1 вписана мишень.
    // Стрелок атакует мишень и каждый раз попадает в произвольную точку квадрата.
    // Надо промоделировать этот процесс с помощью класса java.util.Random и посчитать, какова вероятность попасть в мишень.
    public static double piDividedBy4() {
        Random r = new Random();
        final int tries = 10000;
        return Stream.generate(() -> new Point(r.nextDouble() - 0.5, r.nextDouble() - 0.5))
                .limit(tries)
                .mapToInt(point -> point.radiusVectorLength() < 0.25 ? 1 : 0)
                .average().orElse(0);
    }

    // Дано отображение из имени автора в список с содержанием его произведений.
    // Надо вычислить, чья общая длина произведений наибольшая.
    public static String findPrinter(Map<String, List<String>> compositions) {
        return compositions
                .entrySet()
                .stream()
                .max(Comparator.comparingInt(
                        entry -> (Integer) entry.getValue().stream().mapToInt(String::length).sum()
                ))
                .map(Map.Entry::getKey)
                .orElse("");
    }

    // Вы крупный поставщик продуктов. Каждая торговая сеть делает вам заказ в виде Map<Товар, Количество>.
    // Необходимо вычислить, какой товар и в каком количестве надо поставить.
    public static Map<String, Integer> calculateGlobalOrder(List<Map<String, Integer>> orders) {
        return orders
                .stream()
                .flatMap(x -> x.entrySet().stream())
                .collect(
                        HashMap::new,
                        (h, entry) -> {
                            if (!h.containsKey(entry.getKey()))
                                h.put(entry.getKey(), 0);
                            h.put(entry.getKey(), h.get(entry.getKey()) + entry.getValue());
                        },
                        (h, t) -> {
                            for (Map.Entry<String, Integer> entry : t.entrySet()) {
                                if (!h.containsKey(entry.getKey()))
                                    h.put(entry.getKey(), 0);
                                h.put(entry.getKey(), h.get(entry.getKey()) + entry.getValue());
                            }
                        }
                );
    }
}