package org.example;

import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    void testUpdateProgressBar() {
        // Перенаправляем вывод в ByteArrayOutputStream для проверки
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        // Вызываем метод updateProgressBar
        Main.updateProgressBar(1, 123, 10, 20);

        // Восстанавливаем стандартный вывод
        System.setOut(originalOut);

        // Проверяем, что вывод соответствует ожидаемому
        String expectedOutput = "Thread 1 (ID: 123) |  50% [##########          ]\n";
        assertEquals(expectedOutput, outputStream.toString());
    }

    @Test
    void testThreadExecution() throws InterruptedException, ExecutionException {
        // Создаем пул потоков
        ExecutorService executor = Executors.newFixedThreadPool(3);
        Future<Long> future = executor.submit(() -> {
            long threadId = Thread.currentThread().getId();
            long startTimer = System.currentTimeMillis();

            // Имитируем выполнение прогресс-бара
            for (int j = 1; j <= Main.PROGRESSBAR_LENGTH; j++) {
                Main.updateProgressBar(1, threadId, j, Main.PROGRESSBAR_LENGTH);
                Thread.sleep(Main.STEP_SLEEP);
            }

            long stopTimer = System.currentTimeMillis();
            return stopTimer - startTimer;
        });

        // Ожидаем завершения потока
        long elapsedTime = future.get();

        // Проверяем, что время выполнения больше 0
        assertTrue(elapsedTime > 0);

        // Завершаем пул потоков
        executor.shutdown();
    }

    @Test
    void testMainMethodOutput() throws InterruptedException {
        // Перенаправляем вывод в ByteArrayOutputStream для проверки
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        // Запускаем main метод
        Main.main(new String[]{});

        // Восстанавливаем стандартный вывод
        System.setOut(originalOut);

        // Получаем вывод программы
        String output = outputStream.toString();

        // Проверяем, что вывод содержит ожидаемые строки
        assertTrue(output.contains("Thread 1 (ID:"));
        assertTrue(output.contains("Thread 2 (ID:"));
        assertTrue(output.contains("Thread 3 (ID:"));
        assertTrue(output.contains("finished in"));
    }
}