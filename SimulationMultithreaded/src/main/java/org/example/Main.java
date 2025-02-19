package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class Main {
    public static final int COUNT_THREAD = 3; // Количество потоков
    public static final int PROGRESSBAR_LENGTH = 20; // Длина прогресс-бара
    public static final int STEP_SLEEP = 500; // Базовая задержка между шагами (мс)
    public static final Random random = new Random(); // Генератор случайных чисел для задержек

    /**
     * Обновляет и выводит прогресс-бар для указанного потока.
     * @param threadNumber Номер потока
     * @param threadId ID потока
     * @param progress Текущий прогресс (количество заполненных символов '#')
     */
    public static void updateProgressBar(int threadNumber, long threadId, int progress, int length) {
        StringBuilder barBuilder = new StringBuilder("[");

        // Заполняем прогресс-бар символами '#'
        for (int i = 0; i < progress; i++) {
            barBuilder.append("#");
        }

        // Заполняем оставшуюся часть пробелами
        for (int i = progress; i < PROGRESSBAR_LENGTH; i++) {
            barBuilder.append(" ");
        }
        barBuilder.append("]");

        // Синхронизированный вывод в консоль, чтобы избежать накладок от разных потоков
        synchronized (System.out) {
            System.out.printf("Thread %d (ID: %d) | %3d%% %s\n",
                    threadNumber, threadId, (progress * 100) / PROGRESSBAR_LENGTH, barBuilder);
        }
    }

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(COUNT_THREAD); // Пул потоков фиксированного размера
        List<Future<Long>> futures = new ArrayList<>(); // Список для хранения результатов выполнения потоков

        // Создаем и запускаем потоки
        for (int i = 1; i <= COUNT_THREAD; i++) {
            int threadNumber = i; // Локальная переменная для лямбда-выражения
            futures.add(executor.submit(() -> {
                long threadId = Thread.currentThread().getId(); // Получаем ID текущего потока
                long startTimer = System.currentTimeMillis(); // Засекаем время начала выполнения

                // Заполняем прогресс-бар постепенно
                for (int j = 1; j <= PROGRESSBAR_LENGTH; j++) {
                    updateProgressBar(threadNumber, threadId, j, PROGRESSBAR_LENGTH); // Добавлен параметр PROGRESSBAR_LENGTH
                    try {
                        // Задержка перед следующим обновлением прогресс-бара
                        Thread.sleep(STEP_SLEEP + random.nextInt(200));
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt(); // Восстанавливаем флаг прерывания
                    }
                }

                long stopTimer = System.currentTimeMillis(); // Фиксируем время завершения
                return stopTimer - startTimer; // Возвращаем затраченное время
            }));
        }

        // Ожидаем завершения потоков и выводим их время выполнения
        for (int i = 0; i < COUNT_THREAD; i++) {
            try {
                long elapsedTime = futures.get(i).get(); // Получаем результат выполнения потока
                System.out.printf("Thread %d finished in %d ms\n", i + 1, elapsedTime);
            } catch (InterruptedException | ExecutionException e) {
                System.out.println("Error in thread execution.");
            }
        }

        executor.shutdown(); // Завершаем работу пула потоков
    }
}
