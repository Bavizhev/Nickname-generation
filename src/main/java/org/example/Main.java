package org.example;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    // Создаем счетчики для каждой длины слова
    private static final AtomicInteger length3Counter = new AtomicInteger(0);
    private static final AtomicInteger length4Counter = new AtomicInteger(0);
    private static final AtomicInteger length5Counter = new AtomicInteger(0);

    public static void main(String[] args) {

        // Создаем генератор текстов
        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }

        // Создаем и запускаем потоки для проверки каждого критерия

        // Поток для проверки, является ли слово палиндромом
        Thread palindromeThread = new Thread(() -> {
            for (String text : texts) {
                if (isIncreasingOrder(text)) {
                    int length = text.length();
                    incrementCounter(length, length3Counter, length4Counter, length5Counter);
                }
            }
        });

        // Поток для проверки, состоит ли слово из одной и той же букв
        Thread sameLetterThread = new Thread(() -> {
            for (String text : texts) {
                if (isSameLetter(text)) {
                    int length = text.length();
                    incrementCounter(length, length3Counter, length4Counter, length5Counter);
                }
            }
        });

        // Поток для проверки, идут ли буквы в слове по возрастанию
        Thread increasingOrderThread = new Thread(() -> {
            for (String text : texts) {
                if (isIncreasingOrder(text)) {
                    int length = text.length();
                    incrementCounter(length, length3Counter, length4Counter, length5Counter);
                }
            }
        });

        // Запускаем потоки
        palindromeThread.start();
        sameLetterThread.start();
        increasingOrderThread.start();

        // Ждем завершения всех потоков
        try {
            palindromeThread.join();
            sameLetterThread.join();
            increasingOrderThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Выводим результаты
        System.out.println("Красивых слов с длиной 3: " + length3Counter.get() + " шт");
        System.out.println("Красивых слов с длиной 4: " + length4Counter.get() + " шт");
        System.out.println("Красивых слов с длиной 5: " + length5Counter.get() + " шт");
    }

    // Метод для увелечения атомик полей
    private static void incrementCounter(int length, AtomicInteger length3Counter, AtomicInteger length4Counter, AtomicInteger length5Counter) {
        if (length == 3) {
            length3Counter.incrementAndGet();
        } else if (length == 4) {
            length4Counter.incrementAndGet();
        } else if (length == 5) {
            length5Counter.incrementAndGet();
        }
    }

    // Метод для генерации случайного текста
    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    // Метод для проверки, является ли слово палиндромом
    public static boolean isPalindrome(String text) {
        StringBuilder reverseText = new StringBuilder(text).reverse();
        return text.equals(reverseText.toString());
    }

    // Метод для проверки, состоит ли слово из одной и той же буквы
    public static boolean isSameLetter(String text) {
        char firstChar = text.charAt(0);
        for (int i = 1; i < text.length(); i++) {
            if (text.charAt(i) != firstChar) {
                return false;
            }
        }
        return true;
    }

    // Метод для проверки, идут ли буквы в слове по возрастанию
    public static boolean isIncreasingOrder(String text) {
        for (int i = 1; i < text.length(); i++) {
            if (text.charAt(i) < text.charAt(i - 1)) {
                return false;
            }
        }
        return true;
    }
}