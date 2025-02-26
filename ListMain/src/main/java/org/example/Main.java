package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    private static final List <Integer>  Rl1 = new ArrayList<> ();
    private static final List <Integer>  Rl2 = new ArrayList<> ();
    private static final List <Integer>  Rl3 = new ArrayList<> ();
    private static final List <Integer>  Rl4 = new ArrayList<> ();
    private static final List <Integer>  Rl5 = new ArrayList<> ();

    private static final List<List<Integer>> lists = List.of (Rl1, Rl2, Rl3, Rl4, Rl5);

    private static final ReentrantLock [] locks = new ReentrantLock [ lists.size() ];

    static {
        for (int i = 0; i < locks.length; i++) {
            locks[i] = new ReentrantLock();
        }
    }

    public static void main (String[] args) {

        Thread [] threads = new Thread [10];

        for (int i = 0; i < 10; i++) {
            int threadId = i + 1; //номер нашего потока
            threads[i] = new Thread (() -> {
                try {
                    for (int iteracion = 0; iteracion < 5; iteracion++) { //йоу 5 итераций помним
                        Random random = new Random();
                        int index = random.nextInt(lists.size());
                        List<Integer> selectedList = lists.get(index);
                        ReentrantLock lock = locks[index];

                        //блокировка
                        if (lock.tryLock()) {
                            try {
                                System.out.println( "Thread " + threadId + " Writting to list  " + (index));
                                selectedList.add (threadId); //записуем номер потока
                            } finally {
                                lock.unlock(); //освобождаем тип
                            }
                        } else {
                            System.out.println("Thread " + threadId + " could not get access for list " + (index));
                        }
                        //
                        Thread.sleep( 1000);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("Thread " + threadId + " was interrupted");

                } finally {
                    System.out.println( "Thread " + threadId + " has finished");
                }
            }, "Thread-" + threadId);

            threads[i].start(); // Запускаем поток
        }

        // Ждем завершения всех потоков
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Main thread was interrupted");
            }
        }

    }
}