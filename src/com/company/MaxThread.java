package com.company;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class MaxThread extends Thread {

    private final ArrayBlockingQueue<String> queue;
    private final ConcurrentHashMap<Character, MaxText> map;
    private final char checkedChar;

    private int counter = 0;

    MaxThread(ArrayBlockingQueue<String> queue, ConcurrentHashMap<Character, MaxText> map, char checkedChar) {
        this.queue = queue;
        this.map = map;
        this.checkedChar = checkedChar;
    }

    @Override
    public void run() {
        try {
            while (!isInterrupted()) {
                String text = this.queue.take();
                int count = this.getCharCount(text);

                MaxText maxText = this.map.get(this.checkedChar);

                if (maxText == null || count > maxText.getCount()) {
                    this.map.put(this.checkedChar, new MaxText(count, text));
                }

                this.counter++;
            }
        } catch (InterruptedException ignored) {

        } finally {
            System.out.printf("%s завершен, обработано: %d\n", Thread.currentThread().getName(), this.counter);
        }
    }

    private int getCharCount(String text) {
        int counter = 0;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == this.checkedChar) {
                counter++;
            }
        }
        return counter;
    }
}
