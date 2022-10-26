package com.company;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class Main {
    private static final int countWords = 100_000;
    private static final int countChar = 100_000;

    private static final ArrayBlockingQueue<String> maxA = new ArrayBlockingQueue<>(100, true);
    private static final ArrayBlockingQueue<String> maxB = new ArrayBlockingQueue<>(100, true);
    private static final ArrayBlockingQueue<String> maxC = new ArrayBlockingQueue<>(100, true);

    private static final ConcurrentHashMap<Character, MaxText> resultMap = new ConcurrentHashMap<>();

    public static void main(String[] args) throws InterruptedException {
        MaxThread threadA = new MaxThread(maxA, resultMap, 'a');
        MaxThread threadB = new MaxThread(maxB, resultMap, 'b');
        MaxThread threadC = new MaxThread(maxC, resultMap, 'c');

        ThreadGroup mainGroup = new ThreadGroup("main group");
        new Thread(mainGroup, threadA).start();
        new Thread(mainGroup, threadB).start();
        new Thread(mainGroup, threadC).start();

        for (int i = 0; i < countWords; i++) {
            String text = generateText("abc", countChar);
            maxA.put(text);
            maxB.put(text);
            maxC.put(text);
        }

        while (!maxA.isEmpty() || !maxB.isEmpty() || !maxC.isEmpty()) {
            Thread.sleep(100);
        }

        mainGroup.interrupt();

        System.out.println("Для a текст содержит максимум " + resultMap.get('a').getCount() + " символов");
        System.out.println("Для b текст содержит максимум " + resultMap.get('b').getCount() + " символов");
        System.out.println("Для c текст содержит максимум " + resultMap.get('c').getCount() + " символов");
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

}
